package ET;

import java.io.Serializable;
import java.util.Date;

public class Report implements Serializable {
    private int id;
    private String type;
    private Date generatedDate;
    private Date periodStartDate;
    private Date periodEndDate;
    private Operator generatedByOperator;
    private String content;
    private int totalLoans;
    private double totalPayments;
    private int totalClients;
    private int totalOverdueLoans;
    private double totalIncome;
    private String status;

    public Report() {
    }

    public Report(int id, String type, Date generatedDate, Date periodStartDate, Date periodEndDate,
                  Operator generatedByOperator, String content, int totalLoans, double totalPayments,
                  int totalClients, int totalOverdueLoans, double totalIncome, String status) {
        this.id = id;
        this.type = type;
        this.generatedDate = generatedDate;
        this.periodStartDate = periodStartDate;
        this.periodEndDate = periodEndDate;
        this.generatedByOperator = generatedByOperator;
        this.content = content;
        this.totalLoans = totalLoans;
        this.totalPayments = totalPayments;
        this.totalClients = totalClients;
        this.totalOverdueLoans = totalOverdueLoans;
        this.totalIncome = totalIncome;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(Date generatedDate) {
        this.generatedDate = generatedDate;
    }

    public Date getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(Date periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public Date getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(Date periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public Operator getGeneratedByOperator() {
        return generatedByOperator;
    }

    public void setGeneratedByOperator(Operator generatedByOperator) {
        this.generatedByOperator = generatedByOperator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTotalLoans() {
        return totalLoans;
    }

    public void setTotalLoans(int totalLoans) {
        this.totalLoans = totalLoans;
    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(double totalPayments) {
        this.totalPayments = totalPayments;
    }

    public int getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(int totalClients) {
        this.totalClients = totalClients;
    }

    public int getTotalOverdueLoans() {
        return totalOverdueLoans;
    }

    public void setTotalOverdueLoans(int totalOverdueLoans) {
        this.totalOverdueLoans = totalOverdueLoans;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
