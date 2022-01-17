package nl.birchwood.iprwc.user;

import nl.birchwood.iprwc.auth.Argon2PasswordEncoder;
import nl.birchwood.iprwc.exceptions.EntityNotFoundException;
import nl.birchwood.iprwc.exceptions.ForbiddenException;
import nl.birchwood.iprwc.exceptions.UsernameExistsException;
import nl.birchwood.iprwc.user.model.AppUser;
import nl.birchwood.iprwc.user.model.Role;
import nl.birchwood.iprwc.user.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Argon2PasswordEncoder argon2PasswordEncoder;

    @Value("${superuser.username}")
    private String superUserUsername;

    @Value("${superuser.hashed-password}")
    private String superUserPassword;

    @Autowired
    public UserService(UserRepository userRepository, Argon2PasswordEncoder argon2PasswordEncoder) {
        this.userRepository = userRepository;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
    }

    public UserResponse create(AppUser appUser) {
        isAllowedToCreateUser(appUser.getRole());
        String password = appUser.getPassword();
        appUser.setPassword(hashPassword(password));

        if (userRepository.existsUserByUsername(appUser.getUsername())) {
            throw new UsernameExistsException("This username is already taken");
        }

        userRepository.save(appUser);
        return new UserResponse(appUser);
    }

    public UserResponse findOne(UUID id) {
        AppUser user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AppUser.class));
        return new UserResponse(user);
    }

    public AppUser findOneByUsername(String username) {
        if (username.equals(superUserUsername)) {
            AppUser user = new AppUser();
            user.setRole(Role.SUPERUSER);
            user.setUsername(superUserUsername);
            user.setPassword(new String(Base64.getDecoder().decode(superUserPassword)));
            return user;
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(AppUser.class));
    }

    private void isAllowedToCreateUser(Role toCreate) {
        Role userRole = getUserRole();
        if (userRole == Role.USER) {
            throw new ForbiddenException("Users can't make new users");
        }
    }

    private Role getUserRole() {
        List<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().
                getAuthorities().stream().toList();
        try {
            return Role.valueOf(authorities.get(0).toString());
        } catch (IllegalArgumentException e) {
            if (authorities.size() != 1) {
                return Role.valueOf(authorities.get(1).toString());
            } else {
                return null;
            }
        }
    }



    private String hashPassword(String password) {
        return argon2PasswordEncoder.encode(password);
    }
}
