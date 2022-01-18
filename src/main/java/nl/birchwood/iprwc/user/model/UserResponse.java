package nl.birchwood.iprwc.user.model;

import lombok.Value;

import java.util.UUID;

@Value
public class UserResponse {

    UUID id;
    String username;
    Role role;

    public UserResponse(AppUser appUser) {
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.role = appUser.getRole();
    }

    public UserResponse(Role role, String username) {
        this.role = role;
        this.username = username;
        this.id = null;
    }
}
