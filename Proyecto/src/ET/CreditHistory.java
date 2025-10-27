package ET;

import java.io.Serializable;
import java.util.Date;

public class CreditHistory implements Serializable {
    private int creditId;
    private Client clientId;
    private Date movementDate;
    private String movementType;
    private double amount;
    private double remainingBalance;
    private String loanStatus;
    private String notes;
    private Operator operatorId;

    public CreditHistory() {
    }

    public CreditHistory(int creditId, Client clientId, Date movementDate, String movementType,
                         double amount, double remainingBalance, String loanStatus, String notes,
                         Operator operatorId) {
        this.creditId = creditId;
        this.clientId = clientId;
        this.movementDate = movementDate;
        this.movementType = movementType;
        this.amount = amount;
        this.remainingBalance = remainingBalance;
        this.loanStatus = loanStatus;
        this.notes = notes;
        this.operatorId = operatorId;
    }

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
    }

    public Date getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(Date movementDate) {
        this.movementDate = movementDate;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Operator getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Operator operatorId) {
        this.operatorId = operatorId;
    }
}
