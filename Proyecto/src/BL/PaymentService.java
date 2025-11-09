package BL;

import DAL.PaymentDAL;
import ET.Loan;
import ET.Payment;

import java.util.Date;
import java.util.List;

public class PaymentService {

    private final PaymentDAL paymentDAL;
    private final LoanService loanService;

    public PaymentService(LoanService loanService) throws Exception {
        this.loanService = loanService;
        this.paymentDAL = new PaymentDAL();
    }

    public List<Payment> getAllPayments() {
        return paymentDAL.getPayments();
    }

    public List<Payment> getPaymentsForLoan(int loanId) {
        return paymentDAL.getPaymentsByLoanId(loanId);
    }

    public List<Payment> getPaymentsForClient(int clientId) {
        return paymentDAL.getPaymentsByClientId(clientId);
    }

    public long getTotalPaidForLoan(int loanId) {
        long total = 0;
        for (Payment p : getPaymentsForLoan(loanId)) {
            total += p.getAmountPaid();
        }
        return total;
    }

    public Payment registrarPago(Loan loan, long amount) throws Exception {
        if (loan == null) {
            throw new IllegalArgumentException("El préstamo es obligatorio");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a 0");
        }

        long totalToPay = loan.getTotalToPay();
        long pagadoAntes = getTotalPaidForLoan(loan.getLoanId());
        long saldoAntes = totalToPay - pagadoAntes;

        if (saldoAntes <= 0) {
            throw new IllegalStateException("El préstamo ya está totalmente pagado.");
        }
        if (amount > saldoAntes) {
            throw new IllegalArgumentException("El pago no puede ser mayor al saldo pendiente (₡ " + saldoAntes + ").");
        }

        Payment payment = new Payment();
        payment.setLoanId(loan);
        payment.setAmountPaid(amount);
        payment.setPaymentDate(new Date());

        paymentDAL.insertPayment(payment);

        // Recalcular saldo y estado del préstamo
        long pagadoDespues = pagadoAntes + amount;
        long saldoDespues = totalToPay - pagadoDespues;
        if (saldoDespues <= 0) {
            loan.setStatus("LIQUIDADO");
            saldoDespues = 0;
        } else {
            double tasaMensual = (loan.getInterestRate() / 100.0) / 12.0;
            double cuotaEstimada = loanService.calculateCuota(
                    loan.getAmount(),
                    loan.getTermMonths(),
                    tasaMensual
            );
            if (cuotaEstimada > 0) {
                int cuotasRestantes = (int) Math.ceil(saldoDespues / cuotaEstimada);
                if (cuotasRestantes < 0) cuotasRestantes = 0;
                loan.setTermMonths((short) cuotasRestantes);
            }
        }
        loanService.updateLoan(loan);

        return payment;
    }

    public void updatePayment(Payment payment) throws Exception {
        paymentDAL.updatePayment(payment);
    }

    public void deletePayment(Payment payment) throws Exception {
        paymentDAL.deletePayment(payment);
    }
}
