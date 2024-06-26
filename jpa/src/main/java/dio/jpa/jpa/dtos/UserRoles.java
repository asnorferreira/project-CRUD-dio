package dio.jpa.jpa.dtos;

import java.util.List;

public class UserRoles {
    private String name;
    private String username;
    private List<String> roles;

    public UserRoles(String name, String username, List<String> roles) {
        this.name = name;
        this.username = username;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
