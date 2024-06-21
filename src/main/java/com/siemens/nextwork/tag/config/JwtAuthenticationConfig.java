package com.siemens.nextwork.tag.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@Configuration
@EnableAutoConfiguration(exclude = { UserDetailsServiceAutoConfiguration.class })
@EnableWebSecurity
public class JwtAuthenticationConfig {

	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	public String jwkSetUri;

	public static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationConfig.class);

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {

		return http
				.authorizeHttpRequests(
						requests -> requests
								.requestMatchers("/v3/api-docs", "/configuration/**", "/swagger-resources/**",
										"/swagger-ui/**", "/swagger-ui.html", "/webjars/**", "/lib/**", "/fonts/**",
										"/*", "/actuator/health/**", "/actuator/info/**")
								.permitAll().anyRequest().authenticated())
				.oauth2ResourceServer(oAuth2 -> oAuth2.jwt(jwt -> jwt.decoder(jwtDecoder())))
				.build();
	}

	@Bean
	@Profile("!Test")
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).jwsAlgorithm(SignatureAlgorithm.RS384)
				// .restOperations(rest)
				.build();
	}

}
