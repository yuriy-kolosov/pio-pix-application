package pro.sky.pio_pix.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ProjectConfig {

    @Value("${keySetURI}")
    private String keySetURI;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/login"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.oauth2ResourceServer(
                c -> c.jwt(
                        j -> j.jwkSetUri(keySetURI)
                )
        );

        httpSecurity.authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .requestMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .requestMatchers("/user/**")
                                        .hasAnyAuthority("SCOPE_USER")
                                        .anyRequest().authenticated())
                .cors(withDefaults())
                .csrf((csrf) -> csrf.disable())
                .httpBasic(withDefaults());

        return httpSecurity.build();
    }

}
