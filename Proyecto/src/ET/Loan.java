package ET;

import java.util.Date;

public class Loan {
    private int loanId;
    private int clientId;
    private int operatorId;
    private long amount;
    private short interestRate;
    private short termMonths;
    private long totalToPay;
    private Date StartDate;
    private Date EndDate;
    private String status;

    // Empty constructor
    public Loan() {
    }

    // Overloaded constructor (all-args)
    public Loan(int loanId, int clientId, int operatorId, long amount, short interestRate, short termMonths, long totalToPay, Date startDate, Date endDate, String status) {
        this.loanId = loanId;
        this.clientId = clientId;
        this.operatorId = operatorId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.totalToPay = totalToPay;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.status = status;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public short getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(short interestRate) {
        this.interestRate = interestRate;
    }

    public short getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(short termMonths) {
        this.termMonths = termMonths;
    }

    public long getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(long totalToPay) {
        this.totalToPay = totalToPay;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
