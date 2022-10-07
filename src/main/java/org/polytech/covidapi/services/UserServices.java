package org.polytech.covidapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.entities.ERole;
import org.polytech.covidapi.entities.Role;
import org.polytech.covidapi.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServices(final UserRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createUserDefault() {
        User user = new User();
        List<String> roles = new ArrayList<String>();
        roles.add("USER");
        user.setEmail("user@gmail.com");
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode("password"));
        this.userRepository.save(user);
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        List<String> rolesAdmin = new ArrayList<String>();
        rolesAdmin.add("ADMIN");
        admin.setRoles(rolesAdmin);
        admin.setPassword(passwordEncoder.encode("password"));
        this.userRepository.save(admin);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findFirstByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    user.getRoles().stream().map(SimpleGrantedAuthority::new).toList());
        } else {
            throw new UsernameNotFoundException("L'utilisateur" + email + " n'existe pas");
        }

    }
}
