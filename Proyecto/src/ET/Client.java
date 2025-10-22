package ET;

import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable {
    private int clientId;
    private String fullName;
    private String identificationNumber;
    private String address;
    private String phone;
    private String email;
    //private Date registerDate; TODO

    public Client() {
    }

    public Client(int clientId, String fullName, String identificationNumber, String address, String phone, String email) {
        this.clientId = clientId;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.address = address;
        this.phone = phone;
        this.email = email;
        //this.registerDate = registerDate; TODO
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }*/
}
