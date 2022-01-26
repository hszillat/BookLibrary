package de.szillat.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @SuppressWarnings("unused")
    private static final Logger _log = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void initialize(AuthenticationManagerBuilder builder, DataSource dataSource) throws Exception {
        // XXX
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
//        jdbcUserDetailsManager.setDataSource(dataSource);
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        User user = new User();
//        user.setUsername("user@web.de");
//        user.setPassword(encoder.encode("password"));
//        user.setEnabled(true);
//
//        jdbcUserDetailsManager.createUser(new UserDetailsImpl(user));
        // XXX
        builder.jdbcAuthentication().dataSource(dataSource)
                .withUser("dave@web.de")
                .password("secret")
                .roles("USER");
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        assert userDetailsService != null;

        return this.userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/fonts/**")
                .permitAll();

        http
                .authorizeRequests()
                .antMatchers("/", "/books", "/service").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/books", true)
                .permitAll()
                .and()
                .logout()
                .permitAll()
        // Sinnvoll fuer REST-Services:
        //.and()
        //.sessionManagement()
        //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;

        // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
        http.headers().frameOptions().sameOrigin();
    }
}
