package nl.birchwood.iprwc.auth;

import nl.birchwood.iprwc.auth.model.UserLogin;
import nl.birchwood.iprwc.auth.model.UserLoginResult;
import nl.birchwood.iprwc.exceptions.WrongCredentialsException;
import nl.birchwood.iprwc.user.UserPrincipalService;
import nl.birchwood.iprwc.user.UserService;
import nl.birchwood.iprwc.user.model.AppUser;
import nl.birchwood.iprwc.user.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Value("${jwt.cookie-name}")
    private String cookieName;

    @Value("${jwt.cookie-secure}")
    private Boolean secureCookie;

    @Value("${jwt.cookie.restrict-site}")
    private Boolean sameSiteStrict;

    private final UserPrincipalService userPrincipalService;
    private final Argon2PasswordEncoder argon2PasswordEncoder;
    private final JwtHelper jwtHelper;
    private final UserService userService;

    @Autowired
    public AuthService(UserPrincipalService userPrincipalService, Argon2PasswordEncoder argon2PasswordEncoder, JwtHelper jwtHelper, UserService userService) {
        this.userPrincipalService = userPrincipalService;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.jwtHelper = jwtHelper;
        this.userService = userService;
    }

    public UserLoginResult loginUser(UserLogin userLogin) {
        UserDetails userDetails;
        try {
            userDetails = userPrincipalService.loadUserByUsername(userLogin.getUsername());
        } catch (UsernameNotFoundException e) {
                throw new WrongCredentialsException();
        }

        if (!argon2PasswordEncoder.matches(userLogin.getPassword(), userDetails.getPassword())) {
            throw new WrongCredentialsException();
        }

        Map<String, Object> claims = new HashMap<>();
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        claims.put(HttpSecurityConfig.AUTHORITIES_CLAIM_NAME, authorities);

        String jwt = jwtHelper.createJwtForClaims(userLogin.getUsername(), claims);

        AppUser user = this.userService.findOneByUsername(userDetails.getUsername());
        return new UserLoginResult(jwt, new UserResponse(user));
    }

    public UserResponse getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = this.userService.findOneByUsername(username);
        return new UserResponse(user);
    }

    public HttpCookie createCookie(String value, long maxAge) {
        return ResponseCookie.from(cookieName, value)
                .path("/")
                .httpOnly(true)
                .maxAge(maxAge)
                .secure(secureCookie)
                .sameSite(sameSiteStrict ? "Strict" : "Lax")
                .build();
    }
}
