package ET;

import java.util.Date;

public class Payment {
    private int paymentId;
    private int loanId;
    private long amountPaid;
    private Date paymentDate;


    // Empty constructor
    public Payment() {
    }
    
    public Payment(int paymentId, int loanId, long amountPaid, Date paymentDate) {
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

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
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
