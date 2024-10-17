package com.trilobiet.oapen.oapentoolkit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// https://docs.spring.io/spring-security/reference/5.7/servlet/exploits/headers.html

		http
			.httpBasic().disable() // no login!
			.headers(headers -> headers.defaultsDisabled()
				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
				.contentSecurityPolicy(csp -> csp.policyDirectives(
					  "script-src 'self' 'unsafe-inline' cdn.jsdelivr.net *.websitepolicies.io *.googletagmanager.com *.google-analytics.com; "
					+ "style-src 'self' 'unsafe-inline' cdn.jsdelivr.net *.websitepolicies.io ;"))
				.referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.NO_REFERRER_WHEN_DOWNGRADE))
				.httpStrictTransportSecurity(hsts -> hsts
					.includeSubDomains(true)
					.preload(true)
					.maxAgeInSeconds(31536000)
				)
				.contentTypeOptions()
			);

		return http.build();
	}

}
