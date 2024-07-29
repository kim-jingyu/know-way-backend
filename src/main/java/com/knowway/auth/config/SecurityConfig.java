package com.knowway.auth.config;

import com.knowway.auth.filter.JwtAuthenticationFilter;
import com.knowway.auth.filter.UserAuthenticationFilter;
import com.knowway.auth.handler.SystemAuthenticationSuccessHandler;
import com.knowway.auth.handler.TokenHandler;
import com.knowway.auth.manager.UserAuthenticationManager;
import com.knowway.auth.service.AccessTokenInvalidationStrategy;
import com.knowway.auth.service.IssueRefreshTokenService;
import com.knowway.auth.service.JwtAccessTokenProcessor;
import com.knowway.auth.service.PersistAccessTokenAtRedis;
import com.knowway.auth.service.RefreshTokenPersistLocationStrategy;
import com.knowway.user.repository.MemberRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {


  private final MemberRepository memberRepository;
  @Value("${encrypt.key.life-time}")
  public long accessKeyLifeTime;
  @Value("${encrypt.key.access}")
  public String accessKey;
  @Value("${encrypt.key.refresh.life-time}")
  public long refreshKeyLifeTime;
  @Value("${encrypt.key.refresh.key}")
  public String refreshKey;
  @Value("${application.domain}")
  private String clientDomain;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(Collections.singletonList(clientDomain));
          config.setAllowedMethods(List.of("GET", "POST", "PATCH", "OPTIONS", "DELETE", "PUT"));
          config.setAllowCredentials(false);
          config.setAllowedHeaders(List.of(
              "Host",
              "User-Agent",
              "Accept",
              "Accept-Language",
              "Accept-Encoding",
              "Connection",
              "Origin"
          ));
          config.addExposedHeader("Authorization");
          return config;
        }));

    http.csrf(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable).logout(
        AbstractHttpConfigurer::disable);

    http.sessionManagement(
        (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authorizeHttpRequests((request) -> {
      request.requestMatchers(HttpMethod.POST, "/login").permitAll();
      request.requestMatchers(HttpMethod.POST, "/users").permitAll();
      request.requestMatchers(HttpMethod.POST, "/users/emails").permitAll();
      request.anyRequest().permitAll();
    });

    http
        .addFilterBefore(userAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();

  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(tokenHandler());
  }


  @Bean
  public UsernamePasswordAuthenticationFilter userAuthenticationFilter() {
    UserAuthenticationFilter authenticationFilter = new UserAuthenticationFilter(
        userAuthenticationManager());
    authenticationFilter.setAuthenticationSuccessHandler(successHandler());
    authenticationFilter.setFilterProcessesUrl("/**/login");
    return authenticationFilter;
  }


  @Bean
  public AuthenticationManager userAuthenticationManager() {
    return new UserAuthenticationManager(memberRepository, bCryptPasswordEncoder());
  }


  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public AccessTokenInvalidationStrategy tokenInvalidationStrategy() {
    return new PersistAccessTokenAtRedis();
  }

  @Bean
  public JwtAccessTokenProcessor jwtTokenProcessor() {
    return new JwtAccessTokenProcessor(accessKey, accessKeyLifeTime, tokenInvalidationStrategy());
  }


  @Bean
  public RefreshTokenPersistLocationStrategy refreshPersistStrategy(){
    return new

  }

  @Bean
  public RefreshTokenProccessor refreshTokenProccessor() {
    return new RefreshTokenProccessor(refreshKey, refreshKeyLifeTime,);
  }


  @Bean
  public TokenHandler tokenHandler() {
    return new TokenHandler(jwtTokenProcessor(), refreshTokenProcessor);
  }

  @Bean
  public AuthenticationSuccessHandler successHandler() {
    return new SystemAuthenticationSuccessHandler(tokenHandler());
  }
}
