package example.services.imp;

import example.exceptions.AuthorizationException;
import example.services.AuthorizationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImp implements AuthorizationService {

    @Override
    public void authorize(String username) {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!username.equals(authenticatedUsername)) {
            throw new AuthorizationException("You are not allowed to perform this operation!");
        }
    }
}
