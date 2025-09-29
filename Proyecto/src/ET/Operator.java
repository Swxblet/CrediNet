package ET;

import java.util.Date;

public class Operator {
    private int operatorId;
    private String operatorName;
    private String role;
    private String phoneNumber;
    private String email;
    private String password;
    private Date registerDate;

    // Empty constructor
    public Operator() {
    }

    // Overloaded constructor (all-args)
    public Operator(int operatorId, String operatorName, String role, String phoneNumber, String email, String password, Date registerDate) {
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
