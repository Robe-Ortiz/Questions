package com.robe_ortiz_questions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.robe_ortiz_questions.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        return httpSecurity.authorizeHttpRequests(request -> {
                request.requestMatchers("/css/**", "/img/**","/js/**").permitAll();
                request.requestMatchers(HttpMethod.GET, "/", "/question/all", "/question/id/*", "/question/category/**").permitAll();
                request.requestMatchers("/question/new", "/question/new/question-file", "/question/borrar", "/question/cargar/**").hasAuthority("ROLE_ADMIN");
                request.anyRequest().authenticated();
            })
            .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> 
                userInfo.userService(customOAuth2UserService))
            		.defaultSuccessUrl("/", true)
            )  
            .logout(logout -> logout
                    .logoutUrl("/logout")  
                    .logoutSuccessUrl("/") 
                    .invalidateHttpSession(true) 
                    .clearAuthentication(true) 
                    .deleteCookies("JSESSIONID")
            )
            .exceptionHandling(exception -> exception //provisional hasta crear página personalizada para error 403
            	    .accessDeniedHandler((request, response, accessDeniedException) -> {
            	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            	        response.getWriter().write("Acceso denegado. No tienes permisos para esta acción.");
            	    })
        	)
            .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/logout") //Tendría que evitar esto y hacer que la función js logout() incluya el certificado csrf
            		)
            .build();
    }    
}
