package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private UserServiceImpl userServiceImpl;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserServiceImpl userServiceImpl) {
        this.successUserHandler = successUserHandler;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()//
                .antMatchers("/").permitAll()//Любой пользователь, независимо от роли, имеет доступ к корневому пути /.
                .antMatchers("/403").permitAll()
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated() // любой запрос, который не подходит под предыдущие правила, требует аутентификации.
                .and()
                .formLogin().successHandler(successUserHandler)//.formLogin() — активирует стандартную форму входа Spring Security.
                //.successHandler(successUserHandler) — это обработчик, который будет вызван после успешной аутентификации. Он, вероятно,
                // выполняет дополнительные действия, например, перенаправление пользователя на определенную страницу или запись в лог.
                .permitAll()//.permitAll() — указывает, что доступ к странице входа (обычно /login) открыт для всех пользователей.
                .and()
                .logout().permitAll() //Выход доступен всем пользователям.
                .and()
                .exceptionHandling().accessDeniedPage("/403");//Если пользователь пытается получить доступ к ресурсу,
        // к которому у него нет прав, он будет перенаправлен на страницу /403.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userServiceImpl);
        return authProvider;
    }
}