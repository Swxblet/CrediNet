// BL/LoanService.java
package BL;

import DAL.LoanDAL;
import ET.Loan;

import java.util.ArrayList;

public class LoanService {

    private final LoanDAL loanDAL;

    public LoanService() throws Exception {
        this.loanDAL = new LoanDAL();
    }

    public ArrayList<Loan> getAllLoans() {
        return loanDAL.getLoans();
    }

    public ArrayList<Loan> getLoansByClientId(int clientId) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan loan : loanDAL.getLoans()) {
            if (loan.getClientId() != null &&
                    loan.getClientId().getClientId() == clientId) {
                result.add(loan);
            }
        }
        return result;
    }

    public Loan getActiveLoanForClient(int clientId) {
        for (Loan loan : getLoansByClientId(clientId)) {
            if ("VIGENTE".equalsIgnoreCase(loan.getStatus())) {
                return loan;
            }
        }
        return null;
    }

    // Si luego quieres crear/actualizar préstamos:
    public void addLoan(Loan loan) throws Exception {
        loanDAL.insertLoan(loan);
    }

    public void updateLoan(Loan loan) throws Exception {
        loanDAL.updateLoan(loan);
    }

    public void deleteLoan(Loan loan) throws Exception {
        loanDAL.deleteLoan(loan);
    }

    public double calculateMonthlyPayment(double loanAmount, double interestRate, int numberOfYears) {
        return loanAmount * (interestRate / 100) * Math.pow(1 + (interestRate / 100), numberOfYears * 12);
    }
    //TODO Hacer una función que me ayude a calcular el total de vista previa del valor de un préstamo
}