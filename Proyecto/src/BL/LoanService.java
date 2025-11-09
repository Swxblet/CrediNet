package BL;

import DAL.LoanDAL;
import ET.Client;
import ET.Loan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoanService {

    private final LoanDAL loanDAL;

    private static final double DEFAULT_MONTHLY_RATE = 0.019; // 1.9% mensual

    public LoanService() throws Exception {
        loanDAL = new LoanDAL();
    }

    public List<Loan> getAllLoans() {
        return loanDAL.getLoans();
    }

    public List<Loan> getLoansByClientId(int clientId) {
        return loanDAL.getLoansByClientId(clientId);
    }

    public Loan getActiveLoanForClient(int clientId) {
        ArrayList<Loan> activos = loanDAL.getActiveLoansByClientId(clientId);
        if (activos.isEmpty()) return null;

        Loan latest = activos.get(0);
        for (Loan loan : activos) {
            Date s1 = latest.getStartDate();
            Date s2 = loan.getStartDate();

            if (s1 == null && s2 != null) {
                latest = loan;
            } else if (s1 != null && s2 != null && s2.after(s1)) {
                latest = loan;
            }
        }
        return latest;
    }

    public Loan getLoanById(int loanId) {
        for (Loan l : loanDAL.getLoans()) {
            if (l.getLoanId() == loanId) {
                return l;
            }
        }
        return null;
    }

    public double calculateCuota(double monto, int plazoMeses) {
        return calculateCuota(monto, plazoMeses, DEFAULT_MONTHLY_RATE);
    }

    public double calculateCuota(double monto, int plazoMeses, double tasaMensual) {
        if (monto <= 0 || plazoMeses <= 0 || tasaMensual <= 0) {
            return 0;
        }
        double i = tasaMensual;
        return (monto * i) / (1 - Math.pow(1 + i, -plazoMeses));
    }

    public Loan crearPrestamoParaCliente(Client client,
                                         long amount,
                                         short interestRateAnnual,
                                         short termMonths,
                                         String statusInicial) throws Exception {

        if (client == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (termMonths <= 0) {
            throw new IllegalArgumentException("El plazo en meses debe ser mayor a 0");
        }

        double tasaMensual = (interestRateAnnual / 100.0) / 12.0;

        double cuota = calculateCuota(amount, termMonths, tasaMensual);
        long totalToPay = Math.round(cuota * termMonths);

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, termMonths);
        Date endDate = cal.getTime();
        Loan loan = new Loan();
        loan.setClientId(client);
        loan.setAmount(amount);
        loan.setInterestRate(interestRateAnnual);
        loan.setTermMonths(termMonths);
        loan.setTotalToPay(totalToPay);
        loan.setStartDate(startDate);
        loan.setEndDate(endDate);
        loan.setStatus(statusInicial != null ? statusInicial : "VIGENTE");

        loanDAL.insertLoan(loan);
        return loan;
    }

    public void updateLoan(Loan loan) throws Exception {
        if (loan == null) return;
        loanDAL.updateLoan(loan);
    }

    public void deleteLoan(Loan loan) throws Exception {
        if (loan == null) return;
        loanDAL.deleteLoan(loan);
    }
}
