package ET;

import java.util.Date;

public class Payment {
    private int paymentId;
    private Loan loanId;
    private long amountPaid;
    private Date paymentDate;

    public Payment() {
    }
    
    public Payment(int paymentId, Loan loanId, long amountPaid, Date paymentDate) {
        this.paymentId = paymentId;
        this.loanId = loanId;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Loan getLoanId() {
        return loanId;
    }

    public void setLoanId(Loan loanId) {
        this.loanId = loanId;
    }

    public long getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(long amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
