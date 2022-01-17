package nl.birchwood.iprwc.auth;

import nl.birchwood.iprwc.auth.model.UserLogin;
import nl.birchwood.iprwc.auth.model.UserLoginResult;
import nl.birchwood.iprwc.user.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Value("${jwt.lifetime}")
    private Long lifetime;

    private static final int millisecondsToSeconds = 1000;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserLogin userLogin) {
        UserLoginResult result = authService.loginUser(userLogin);
        HttpCookie cookie = authService.createCookie(result.getToken(), lifetime / millisecondsToSeconds);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.getUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<UserLoginResult> logoutUser() {
        HttpCookie cookie = authService.createCookie("", 0);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(null);
    }

    @GetMapping("/user")
    public UserResponse getUser() {
        return authService.getUser();
    }

}
