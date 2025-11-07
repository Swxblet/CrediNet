// BL/PaymentService.java
package BL;

import DAL.LoanDAL;
import DAL.PaymentDAL;
import ET.Loan;
import ET.Payment;

import java.util.ArrayList;

public class PaymentService {

    private final PaymentDAL paymentDAL;
    private final LoanDAL loanDAL;

    public PaymentService() throws Exception {
        this.paymentDAL = new PaymentDAL();
        this.loanDAL = new LoanDAL();
    }

    public ArrayList<Payment> getAllPayments() {
        return paymentDAL.getPayments();
    }

    public ArrayList<Payment> getPaymentsForLoan(int loanId) {
        ArrayList<Payment> result = new ArrayList<>();
        for (Payment p : paymentDAL.getPayments()) {
            if (p.getLoanId() != null &&
                    p.getLoanId().getLoanId() == loanId) {
                result.add(p);
            }
        }
        return result;
    }

    public ArrayList<Payment> getPaymentsForClient(int clientId) {
        ArrayList<Payment> result = new ArrayList<>();
        for (Loan loan : loanDAL.getLoans()) {
            if (loan.getClientId() != null &&
                    loan.getClientId().getClientId() == clientId) {
                result.addAll(getPaymentsForLoan(loan.getLoanId()));
            }
        }
        return result;
    }

    public void addPayment(Payment payment) throws Exception {
        paymentDAL.insertPayment(payment);
    }
}