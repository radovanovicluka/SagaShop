package rs.saga.obuka.sagashop.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import rs.saga.obuka.sagashop.security.JwtAuthenticationFilter;

@Configuration
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                        authorizeRequests -> {
                            authorizeRequests
                                    .antMatchers(HttpMethod.GET, "/category", "/paypalaccount", "/product", "/user")
                                    .hasAnyAuthority("ADMIN", "USER")
                                    .antMatchers(HttpMethod.GET, "/category/*", "/paypalaccount/*", "/product/*", "/user/*")
                                    .hasAnyAuthority("USER", "ADMIN")
                                    .antMatchers("/shoppingcart", "/shoppingcart/**")
                                    .hasAnyAuthority("USER", "ADMIN")
                                    .antMatchers(HttpMethod.POST, "/category", "/paypalaccount", "/product", "/user")
                                    .hasAuthority("ADMIN")
                                    .antMatchers(HttpMethod.PUT, "/category", "/paypalaccount", "/product", "/user")
                                    .hasAuthority("ADMIN")
                                    .antMatchers(HttpMethod.DELETE, "/category/*", "/paypalaccount/*", "/product/*", "/user/*")
                                    .hasAuthority("ADMIN")
                                    .antMatchers("/product/category/*", "/product/name/*", "/product/price/*")
                                    .hasAuthority("ADMIN")
                                    .antMatchers(HttpMethod.POST, "/auth/login", "/auth/register")
                                    .permitAll();
                        }
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
