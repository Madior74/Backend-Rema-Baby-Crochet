package com.ecommerce.rema_baby_crochet.user;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.rema_baby_crochet.auth.dto.RegisterRequest;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public User createUser(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new UserAlreadyExistsException();
        }

        Role defaultRole = roleRepository.findByName(Role.ROLE_CLIENT)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER is missing from the database"));



        User user = userMapper.toEntity(request);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(new HashSet<>(Set.of(defaultRole)));
        user.setEnabled(true);

        try {
            // Flush now so a concurrent duplicate is translated to the public 409 error.
            return userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UserAlreadyExistsException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return userRepository.findByEmailIgnoreCase(normalizeEmail(email));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String[] authorities = user.getRoles().stream()
                .map(Role::getName)
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .authorities(authorities)
                .build();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
