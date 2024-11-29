package org.iit.ticketKeeper.auth;

import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.config.JwtService;
import org.iit.ticketKeeper.dto.User;
import org.iit.ticketKeeper.entity.UserEntity;
import org.iit.ticketKeeper.repository.UserRepository;
import org.iit.ticketKeeper.utill.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User request) {
        System.out.println("this is req : "+ request);

        Optional<UserEntity> userEntity = repository.findByEmail(request.getEmail());
        if(userEntity.isPresent()) return AuthenticationResponse.builder()
                    .message("User already exist")
                    .build();

        //Enum role = request.getRole().equals("Customer") ? Role.CUSTOMER : Role.VENDOR;
        var user = UserEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .address(request.getAddress())
                .country(request.getCountry())
                .contactNumber(request.getContactNumber())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole().equals("Customer") ? Role.CUSTOMER : Role.VENDOR)
                .isVipCustomer(false)
                .build();

        System.out.println("this is user :" + user);
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Done")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Optional<UserEntity> user = repository.findByEmail(request.getEmail());
        if(user.isEmpty()) return AuthenticationResponse.builder()
                .message("user not exist")
                .build();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

//        var user = repository.findByEmail(request.getEmail())
//                .orElseThrow();




        var jwtToken = jwtService.generateToken(user.get());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Done")
                .build();
    }

}
