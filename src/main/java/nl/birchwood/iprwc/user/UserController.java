package nl.birchwood.iprwc.user;

import nl.birchwood.iprwc.user.model.AppUser;
import nl.birchwood.iprwc.user.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    UserResponse createUser(@RequestBody AppUser appUser) {
        return userService.create(appUser);
    }

}
