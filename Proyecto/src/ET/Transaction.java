package ET;

import java.util.Date;

public class Transaction {
    private int transactionId;
    private Client clientId;
    private Operator operatorId;
    private Loan loanId;
    private String type;
    private double amount;
    private String currency;
    private Date transactionDate;
    private String paymentMethod;
    private String referenceNumber;
    private String notes;
    private String status;

    public Transaction() {
    }

    public Transaction(int transactionId, Client clientId, Operator operatorId, Loan loanId,
                       String type, double amount, String currency, Date transactionDate,
                       String paymentMethod, String referenceNumber, String notes, String status) {
        this.transactionId = transactionId;
        this.clientId = clientId;
        this.operatorId = operatorId;
        this.loanId = loanId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.transactionDate = transactionDate;
        this.paymentMethod = paymentMethod;
        this.referenceNumber = referenceNumber;
        this.notes = notes;
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
    }

    public Operator getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Operator operatorId) {
        this.operatorId = operatorId;
    }

    public Loan getLoanId() {
        return loanId;
    }

    public void setLoanId(Loan loanId) {
        this.loanId = loanId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
