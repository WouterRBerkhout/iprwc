package nl.birchwood.iprwc.user.model;

public enum Role {
    SUPERUSER("superuser"),
    USER("user");


    public final String value;

    Role(String value) { this.value = value; }
}
