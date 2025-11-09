package DAL;

import ET.Loan;

import java.io.*;
import java.util.ArrayList;

public class LoanDAL {
    private static final String DATA_DIR = "Proyecto/data/Loans";
    private static final String FILE_PATH = DATA_DIR + File.separator + "loans.dat";

    private static ArrayList<Loan> listOfLoans = new ArrayList<>();
    private static int idCounter = 0;

    public LoanDAL() throws Exception {
        listOfLoans = loadLoan();
        recalcularIdCounter();
    }

    private void recalcularIdCounter() {
        int maxId = -1;
        for (Loan loan : listOfLoans) {
            if (loan.getLoanId() > maxId) {
                maxId = loan.getLoanId();
            }
        }
        idCounter = maxId + 1;
    }

    public void insertLoan(Loan loanToInsert) throws Exception {
        loanToInsert.setLoanId(idCounter++);
        listOfLoans.add(loanToInsert);
        saveLoan();
    }

    public ArrayList<Loan> getLoans() {
        return listOfLoans;
    }

    public void updateLoan(Loan loanToUpdate) throws Exception {
        for (int i = 0; i < listOfLoans.size(); i++) {
            if (listOfLoans.get(i).getLoanId() == loanToUpdate.getLoanId()) {
                listOfLoans.set(i, loanToUpdate);
                saveLoan();
                return;
            }
        }
    }

    public void deleteLoan(Loan loanToDelete) throws Exception {
        for (int i = 0; i < listOfLoans.size(); i++) {
            if (listOfLoans.get(i).getLoanId() == loanToDelete.getLoanId()) {
                listOfLoans.remove(i);
                saveLoan();
                return;
            }
        }
    }

    public Loan getLoanById(int loanId) {
        for (Loan loan : listOfLoans) {
            if (loan.getLoanId() == loanId) {
                return loan;
            }
        }
        return null;
    }

    public ArrayList<Loan> getLoansByClientId(int clientId) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan loan : listOfLoans) {
            if (loan.getClientId() != null &&
                    loan.getClientId().getClientId() == clientId) {
                result.add(loan);
            }
        }
        return result;
    }

    public ArrayList<Loan> getActiveLoansByClientId(int clientId) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan loan : listOfLoans) {
            if (loan.getClientId() != null &&
                    loan.getClientId().getClientId() == clientId &&
                    loan.getStatus() != null &&
                    loan.getStatus().equalsIgnoreCase("VIGENTE")) {
                result.add(loan);
            }
        }
        return result;
    }


    private void saveLoan() throws Exception {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            fos.writeObject(listOfLoans);
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Loan> loadLoan() throws Exception {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (ArrayList<Loan>) fis.readObject();
        }
    }
}
