package br.com.movieflix.controller;

import br.com.movieflix.config.TokenService;
import br.com.movieflix.controller.request.LoginRequest;
import br.com.movieflix.controller.request.UserRequest;
import br.com.movieflix.controller.response.LoginResponse;
import br.com.movieflix.controller.response.UserResponse;
import br.com.movieflix.entity.User;
import br.com.movieflix.exception.UsernameOrPasswordNotFoundException;
import br.com.movieflix.mapper.UserMapper;
import br.com.movieflix.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movieflix/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> save(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserMapper.toUserResponse(userService.save(UserMapper.toUser(userRequest))));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {


        try {
            UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(loginRequest.email(),
                    loginRequest.password());
            Authentication authentication = authenticationManager.authenticate(userAndPass);

            User user = (User) authentication.getPrincipal();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new LoginResponse(tokenService.generateToken(user)));
        } catch (BadCredentialsException e) {
            throw new UsernameOrPasswordNotFoundException("Usuario ou Senha inválido");
        }


    }
}
