package ET;

import java.io.Serializable;

public class Collateral implements Serializable {
    private int collateralId;
    private Loan loanId;
    private Operator operatorId;
    private String type;
    private String description;
    private double calculatedValue;
    private String currency;
    private String legalStatus;
    private String status;

    public Collateral() {
    }

    public Collateral(int collateralId, Loan loanId, Operator operatorId, String type, String description, double calculatedValue, String currency, String legalStatus, String status) {
        this.collateralId = collateralId;
        this.loanId = loanId;
        this.operatorId = operatorId;
        this.type = type;
        this.description = description;
        this.calculatedValue = calculatedValue;
        this.currency = currency;
        this.legalStatus = legalStatus;
        this.status = status;
    }

    public int getCollateralId() {
        return collateralId;
    }

    public void setCollateralId(int collateralId) {
        this.collateralId = collateralId;
    }

    public Loan getLoanId() {
        return loanId;
    }

    public void setLoanId(Loan loanId) {
        this.loanId = loanId;
    }

    public Operator getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Operator operatorId) {
        this.operatorId = operatorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCalculatedValue() {
        return calculatedValue;
    }

    public void setCalculatedValue(double calculatedValue) {
        this.calculatedValue = calculatedValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLegalStatus() {
        return legalStatus;
    }

    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
