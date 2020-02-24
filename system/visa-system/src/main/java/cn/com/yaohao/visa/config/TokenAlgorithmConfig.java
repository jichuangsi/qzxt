package cn.com.yaohao.visa.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenAlgorithmConfig {

	@Value("${custom.token.secret}")
	private String secret;

	@Bean
	public Algorithm getTokenAlgorithm() throws IllegalArgumentException, Exception {
		return Algorithm.HMAC512(secret);
	}

}
