package com.ecommerce.rema_baby_crochet.user;


import com.ecommerce.rema_baby_crochet.auth.dto.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    User createUser(RegisterRequest request);

    Optional<User> findByEmail(String email);
}
