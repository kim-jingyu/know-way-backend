package com.knowway.auth.config;

import com.knowway.auth.filter.JwtAuthenticationFilter;
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
import com.knowway.auth.service.LongToStringConverter;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final RedisTemplate<String, String> blackListRedisTemplate;
  private final RedisTemplate<String, String> refreshRedisTemplate;
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
      @Qualifier("refreshRedisTemplate") RedisTemplate<String, String> refreshRedisTemplate,
      MemberRepository memberRepository
  ) {
    this.blackListRedisTemplate = blackListRedisTemplate;
    this.refreshRedisTemplate = refreshRedisTemplate;
    this.memberRepository = memberRepository;
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      @Qualifier("userAuthenticationFilter") UsernamePasswordAuthenticationFilter userAuthenticationFilter,
      @Qualifier("jwtAuthenticationFilter") OncePerRequestFilter jwtAuthenticationFilter
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
          request.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
          request.requestMatchers(HttpMethod.POST,"/depts").hasAuthority("ROLE_ADMIN");
          request.requestMatchers(HttpMethod.DELETE,"/depts/**").hasAuthority("ROLE_ADMIN");
          request.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
          request.anyRequest().authenticated();
        })
        .addFilterBefore(userAuthenticationFilter, CorsFilter.class)
        .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean("jwtAuthenticationFilter")
  public JwtAuthenticationFilter<String, String> jwtAuthenticationFilter(
      AccessTokenHandler<String> accessTokenHandler,
      @Qualifier("tokenToKeyConverter") TypeConvertor<String, String> tokenToKeyConvertor,
      @Qualifier("subjectToValueConvertor") TypeConvertor<String, String> subjectToValueConvertor,
      AccessTokenWithRefreshTokenService<String,String,Long> accessTokenWithRefreshTokenService) {
    return new JwtAuthenticationFilter<>(accessTokenHandler, accessTokenWithRefreshTokenService,
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
    return new UserAuthenticationManager(memberRepository, encoder);
  }

  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AccessTokenInvalidationStrategy<String> tokenInvalidationStrategy() {
    return new AccessTokenSetAsBlackListWhenInvalidating<>("BlackListFixedValue",
        refreshKeyLifeTime, blackListRedisTemplate);
  }


  @Bean
  public JwtAccessTokenProcessor jwtTokenProcessor() {
    return new JwtAccessTokenProcessor(accessKey, accessKeyLifeTime, tokenInvalidationStrategy());
  }

  @Bean
  public RefreshTokenPersistLocationStrategy<String, String> refreshPersistStrategy() {
    return new RefreshTokenPersistAtRedis<>(refreshRedisTemplate);
  }

  @Bean
  public ReAuthenticationStrategy<String, String> reAuthenticationStrategy() {
    return new RtrRefreshTokenReIssueStrategy<>(refreshPersistStrategy(), refreshKeyLifeTime);
  }

  @Bean
  public RefreshTokenProcessor<String, String> refreshTokenProcessor() {
    return new RefreshTokenProcessor<>(refreshKeyLifeTime, refreshPersistStrategy(),
        reAuthenticationStrategy());
  }


  @Bean
  public TypeConvertor<Long, String> userIdToKeyConverter() {
    return new LongToStringConverter();
  }

  @Bean
  public RefreshTokenHandler<String, String> refreshTokenHandler() {
    return new RefreshTokenHandler<>(refreshTokenProcessor());
  }

  @Bean
  public AccessTokenWithRefreshTokenService<String, String, Long> accessTokenWithRefreshTokenService(
      @Qualifier("tokenToKeyConverter") TypeConvertor<String, String> tokenToKeyConvertor,
      @Qualifier("userIdToValueConverter") TypeConvertor<Long, String> userIdToSubjectConvertor,
      @Qualifier("accessTokenHandler") AccessTokenHandler<String> accessTokenHandler,
      MemberRepository memberRepository,
      @Qualifier("keyToTokenConverter") TypeConvertor<String, String> keyToTokenConverter,
      @Qualifier("userIdToValueConverter") TypeConvertor<Long, String> userIdToValueConvertor,
      @Qualifier("refreshTokenHandler") RefreshTokenHandler<String, String> refreshTokenHandler,
      @Qualifier("accessTokenHandler") AccessTokenHandler<String> valueAccessTokenHandler) {

    return new AccessTokenWithRefreshTokenService<>(
        tokenToKeyConvertor,
        userIdToSubjectConvertor,
        accessTokenHandler,
        memberRepository,
        keyToTokenConverter,
        userIdToValueConvertor,
        refreshTokenHandler,
        valueAccessTokenHandler
    );
  }

  @Bean
  @Qualifier("tokenToKeyConverter")
  public TypeConvertor<String, String> tokenToKeyConverter() {
    return token -> token;
  }

  @Bean
  @Qualifier("userIdToValueConverter")
  public TypeConvertor<Long, String> userIdToValueConverter() {
    return String::valueOf;
  }

  @Bean
  @Qualifier("keyToTokenConverter")
  public TypeConvertor<String, String> keyToTokenConverter() {
    return key -> key;
  }

  @Bean
  @Qualifier("valueToUserIdConverter")
  public TypeConvertor<String, Long> valueToUserIdConverter() {
    return Long::valueOf;
  }

  @Bean
  @Qualifier("subjectToValueConvertor")
  public TypeConvertor<String, String> subjectToValueConvertor() {
    return String::valueOf;
  }

  @Bean
  public AccessTokenHandler<String> accessTokenHandler() {
    return new AccessTokenHandler<>(keyToTokenConverter(), jwtTokenProcessor());
  }

  @Bean
  public AuthenticationSuccessHandler successHandler(
      AccessTokenWithRefreshTokenService<String, String, Long> accessTokenWithRefreshTokenService) {
    return new SystemAuthenticationSuccessHandler<>(accessTokenWithRefreshTokenService);
  }

  @Configuration
  public static class ConversionConfig {

    @Bean
    public TypeConvertor<String, String> tokenToKeyConverter() {
      return token -> token;
    }

    @Bean
    public TypeConvertor<Long, String> userIdToValueConverter() {
      return String::valueOf;
    }

    @Bean
    public TypeConvertor<String, String> keyToTokenConvertor() {
      return key -> key;
    }

    @Bean
    public TypeConvertor<String, Long> valueToUserIdConvertor() {
      return Long::parseLong;
    }
  }
}
