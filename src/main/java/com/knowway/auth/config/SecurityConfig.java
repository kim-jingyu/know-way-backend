package com.knowway.auth.config;

import com.knowway.auth.filter.JwtAuthorizationFilter;
import com.knowway.auth.filter.UserAuthenticationFilter;
import com.knowway.auth.handler.AccessTokenHandler;
import com.knowway.auth.handler.RefreshTokenHandler;
import com.knowway.auth.handler.RefreshTokenProcessor;
import com.knowway.auth.handler.SystemAuthenticationSuccessHandler;
import com.knowway.auth.manager.AdminAuthenticationProvider;
import com.knowway.auth.manager.UserAuthenticationManager;
import com.knowway.auth.service.AccessTokenInvalidationStrategy;
import com.knowway.auth.service.AccessTokenSetAsBlackListWhenInvalidating;
import com.knowway.auth.service.AccessTokenWithRefreshTokenService;
import com.knowway.auth.service.JwtAccessTokenProcessor;
import com.knowway.auth.service.ReAuthenticationStrategy;
import com.knowway.auth.service.RefreshTokenPersistAtRedis;
import com.knowway.auth.service.RefreshTokenPersistLocationStrategy;
import com.knowway.auth.service.RtrRefreshTokenReIssueStrategy;
import com.knowway.auth.util.TypeConvertor;
import com.knowway.user.repository.MemberRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * SecurityConfig
 *
 * @author 구지웅
 * @since 2024.8.1
 * @version 1.0

 */
@Configuration
@EnableWebSecurity
public class SecurityConfig<K extends String, V extends String, USERID extends Long> {

  private final RedisTemplate<String, String> blackListRedisTemplate;
  private final RedisTemplate<K, V> refreshRedisTemplate;
  private final MemberRepository memberRepository;


  @Value("${encrypt.access.life-time}")
  private long accessKeyLifeTime;
  @Value("${application.domain}")
  private String clientDomain;
  @Value("${encrypt.access.key}")
  private String accessKey;
  @Value("${encrypt.refresh.life-time}")
  private long refreshKeyLifeTime;
  @Value("${admin.pk}")
  private String adminId;
  @Value("${admin.password}")
  private String adminPassword;

  public SecurityConfig(
      @Qualifier("redisTemplate") RedisTemplate<String, String> blackListRedisTemplate,
      @Qualifier("refreshRedisTemplate") RedisTemplate<K, V> refreshRedisTemplate,
      MemberRepository memberRepository
  ) {
    this.blackListRedisTemplate = blackListRedisTemplate;
    this.refreshRedisTemplate = refreshRedisTemplate;
    this.memberRepository = memberRepository;
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      @Qualifier("userAuthenticationFilter") UsernamePasswordAuthenticationFilter userAuthenticationFilter,
      @Qualifier("jwtAuthenticationFilter") OncePerRequestFilter jwtAuthorizationFilter
  )
      throws Exception {
    http
        .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(Collections.singletonList(clientDomain));
          config.setAllowedMethods(List.of("GET", "POST", "PATCH", "OPTIONS", "DELETE", "PUT"));
          config.setAllowCredentials(false);
          config.setAllowedHeaders(
              List.of("Host", "User-Agent", "Accept", "Accept-Language", "Accept-Encoding",
                  "Connection", "Origin"));
          config.addExposedHeader("Authorization");
          return config;
        }))
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(request -> {
          request.requestMatchers(HttpMethod.POST, "/login").permitAll();
          request.requestMatchers(HttpMethod.POST, "/users").permitAll();
          request.requestMatchers(HttpMethod.POST, "/users/emails").permitAll();
          request.requestMatchers(HttpMethod.POST,"/depts").hasAuthority("ROLE_ADMIN");
          request.requestMatchers(HttpMethod.DELETE,"/depts/**").hasAuthority("ROLE_ADMIN");
          request.requestMatchers(HttpMethod.DELETE,"/depts/**").hasAuthority("ROLE_ADMIN");

          request.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
          request.anyRequest().authenticated();
        })
        .addFilterBefore(userAuthenticationFilter, CorsFilter.class)
        .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean("jwtAuthorizationFilter")
  public JwtAuthorizationFilter<K, V,USERID> jwtAuthorizationFilter(
      AccessTokenHandler accessTokenHandler,
      @Qualifier("tokenToKeyConverter") TypeConvertor<String, K> tokenToKeyConvertor,
      @Qualifier("keyToTokenConvertor") TypeConvertor<String, V> subjectToValueConvertor,
      AccessTokenWithRefreshTokenService<K, V, USERID> accessTokenWithRefreshTokenService) {
    return new JwtAuthorizationFilter<>(accessTokenHandler, accessTokenWithRefreshTokenService,
        tokenToKeyConvertor, subjectToValueConvertor);
  }

  @Primary
  @Bean("systemAuthentication")
  public ProviderManager systemAuthentication(AuthenticationManager parentManager,
      @Qualifier("adminAuthenticationProvider")
          AuthenticationProvider adminProvider) {
    return new ProviderManager(List.of(adminProvider), parentManager);
  }


  @Bean
  public UsernamePasswordAuthenticationFilter userAuthenticationFilter(
      @Qualifier("systemAuthentication") ProviderManager authenticationManager,
      AuthenticationSuccessHandler systemAuthenticationSuccessHandler) {
    UserAuthenticationFilter authenticationFilter = new UserAuthenticationFilter(
        authenticationManager);
    authenticationFilter.setAuthenticationSuccessHandler(systemAuthenticationSuccessHandler);
    authenticationFilter.setFilterProcessesUrl("/login");
    return authenticationFilter;
  }

  @Bean("inMemoryUserDetailsService")
  public UserDetailsService inMemoryUserDetailsService() {
    UserDetails admin = User.builder()
        .username(adminId)
        .password(bCryptPasswordEncoder().encode(adminPassword))
        .roles("ADMIN")
        .build();

    return new InMemoryUserDetailsManager(admin);
  }

  @Qualifier("adminAuthenticationProvider")
  @Bean
  public AuthenticationProvider adminAuthenticationProvider(
      @Qualifier("inMemoryUserDetailsService") UserDetailsService inMemoryUserDetailsService,
      PasswordEncoder encoder) {
    return new AdminAuthenticationProvider<>(inMemoryUserDetailsService,
        encoder);
  }

  @Qualifier("parentManager")
  @Bean
  public AuthenticationManager parentManager(PasswordEncoder encoder) {
    return new UserAuthenticationManager(memberRepository,encoder);
  }


  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AccessTokenInvalidationStrategy tokenInvalidationStrategy() {
    return new AccessTokenSetAsBlackListWhenInvalidating(refreshKeyLifeTime,
        blackListRedisTemplate);
  }


  @Bean
  public JwtAccessTokenProcessor jwtTokenProcessor() {
    return new JwtAccessTokenProcessor(accessKey, accessKeyLifeTime, tokenInvalidationStrategy());
  }

  @Bean
  public RefreshTokenPersistLocationStrategy<K, V> refreshPersistStrategy() {
    return new RefreshTokenPersistAtRedis<>(refreshRedisTemplate);
  }

  @Bean
  public ReAuthenticationStrategy<K, V> reAuthenticationStrategy() {
    return new RtrRefreshTokenReIssueStrategy<>(refreshPersistStrategy(), refreshKeyLifeTime);
  }

  @Bean
  public RefreshTokenProcessor<K, V> refreshTokenProcessor() {
    return new RefreshTokenProcessor<>(refreshKeyLifeTime, refreshPersistStrategy(),
        reAuthenticationStrategy());
  }


  @Bean
  public RefreshTokenHandler<K, V> refreshTokenHandler() {
    return new RefreshTokenHandler<>(refreshTokenProcessor());
  }




  @Qualifier("accessTokenHandler")
  @Bean
  public AccessTokenHandler accessTokenHandler() {
    return new AccessTokenHandler(jwtTokenProcessor());
  }

  @Bean
  public AuthenticationSuccessHandler successHandler(
      AccessTokenWithRefreshTokenService<K, V, USERID> accessTokenWithRefreshTokenService) {
    return new SystemAuthenticationSuccessHandler<>(accessTokenWithRefreshTokenService);
  }


  @Bean
  public AccessTokenWithRefreshTokenService<K, V, USERID> accessTokenWithRefreshTokenService(
      @Qualifier("tokenToKeyConverter") TypeConvertor<String, K> tokenToKeyConvertor,
      @Qualifier("userIdToSubjectConverter") TypeConvertor<USERID, String> userIdToSubjectConvertor,
      @Qualifier("memberRepository") MemberRepository memberRepository,
      @Qualifier("userIdToValueConverter") TypeConvertor<USERID, V> userIdToValueConvertor,
      @Qualifier("refreshTokenHandler") RefreshTokenHandler<K, V> refreshTokenHandler,
      @Qualifier("accessTokenHandler") AccessTokenHandler valueAccessTokenHandler) {

    return new AccessTokenWithRefreshTokenService<>(
        tokenToKeyConvertor,
        userIdToSubjectConvertor,
        memberRepository,
        userIdToValueConvertor,
        refreshTokenHandler,
        valueAccessTokenHandler
    );
  }

  @Configuration
  public static class Convertor {

    @Qualifier("tokenToKeyConverter")
    @Bean
    public static TypeConvertor<String, String> tokenToKeyConverter() {
      return token -> token;
    }

    @Qualifier("keyToTokenConvertor")
    @Bean
    public static TypeConvertor<String, String> keyToTokenConvertor() {
      return key -> key;
    }

    @Qualifier("valueToUserIdConvertor")
    @Bean
    public static TypeConvertor<String, Long> valueToUserIdConvertor() {
      return Long::parseLong;
    }

    @Qualifier("userIdToKeyConverter")
    @Bean
    public static TypeConvertor<Long, String> userIdToKeyConverter() {
      return String::valueOf;
    }

    @Qualifier("userIdToSubjectConverter")
    @Bean
    public static TypeConvertor<Long, String> userIdToSubjectConverter() {
      return String::valueOf;
    }

    @Qualifier("userIdToValueConverter")
    @Bean
    public static TypeConvertor<Long, String> userIdToValueConverter() {
      return String::valueOf;
    }
  }
}

