package com.robe_ortiz_questions.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.robe_ortiz_questions.entity.user.User;
import com.robe_ortiz_questions.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	
	private final UserRepository userRepository;
	
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Carga la información del usuario desde Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");        

        User user = userRepository.findByEmail(email);
        if (user == null) {
        	String nombre = oAuth2User.getAttribute("name");
            user = new User(email, nombre);
            userRepository.save(user); 
        }  

        return oAuth2User;
    }
}
