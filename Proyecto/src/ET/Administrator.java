package ET;

import java.io.Serializable;
import java.util.Date;

public class Administrator implements Serializable {
    private int adminId;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private Date registerDate;

    public Administrator() {
    }

    public Administrator(int adminId, String fullName, String username, String password, String email, Date registerDate) {
        this.adminId = adminId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerDate = registerDate;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
