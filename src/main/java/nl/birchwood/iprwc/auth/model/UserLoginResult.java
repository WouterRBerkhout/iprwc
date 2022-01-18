package nl.birchwood.iprwc.auth.model;

import lombok.Data;
import lombok.NonNull;
import nl.birchwood.iprwc.user.model.UserResponse;

@Data
public class UserLoginResult {

    @NonNull
    private String token;
    @NonNull
    private UserResponse user;

}
