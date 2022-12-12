package org.polytech.covidapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.exception.ResourceNotFoundException;
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
    private final CenterRepository centerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServices(final UserRepository utilisateurRepository, CenterRepository centerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = utilisateurRepository;
        this.centerRepository = centerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createUserDefault() throws ResourceNotFoundException {
        User patient = new User();
        List<String> rolesPatient = new ArrayList<String>();
        rolesPatient.add("PATIENT");
        patient.setEmail("patient@gmail.com");
        patient.setLastName("patient");
        patient.setFirstName("patient");
        Optional<User> patientSearch = userRepository.findFirstByEmail("patient@gmail.com");
        patient.setRoles(rolesPatient);
        patient.setPassword(passwordEncoder.encode("password"));
        if (patientSearch.isEmpty()) {
            this.userRepository.save(patient);
        }

        User superadmin = new User();
        superadmin.setEmail("superadmin@gmail.com");
        superadmin.setLastName("superadmin");
        superadmin.setFirstName("superadmin");
        List<String> rolesSuperadmin = new ArrayList<String>();
        rolesSuperadmin.add("SUPERADMIN");
        superadmin.setRoles(rolesSuperadmin);
        superadmin.setPassword(passwordEncoder.encode("password"));
        Optional<User> superadminSearch = userRepository.findFirstByEmail("superadmin@gmail.com");
        if (superadminSearch.isEmpty()) {
            this.userRepository.save(superadmin);
        }
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
