package DAL;

import ET.Payment;
import ET.Loan;
import ET.Client;

import java.io.*;
import java.util.ArrayList;

public class PaymentDAL {
    private static final String DATA_DIR = "Proyecto/data/Payments";
    private static final String FILE_PATH = DATA_DIR + File.separator + "payments.dat";

    private static ArrayList<Payment> listOfPayments = new ArrayList<>();
    private static int idCounter = 0;

    public PaymentDAL() throws Exception {
        listOfPayments = loadPayment();

        for (Payment p : listOfPayments) {
            if (p.getPaymentId() >= idCounter) {
                idCounter = p.getPaymentId() + 1;
            }
        }
    }

    public void insertPayment(Payment paymentToInsert) throws Exception {
        paymentToInsert.setPaymentId(idCounter++);
        listOfPayments.add(paymentToInsert);
        savePayment();
    }

    public ArrayList<Payment> getPayments() {
        return listOfPayments;
    }

    public void updatePayment(Payment paymentToUpdate) throws Exception {
        for (int i = 0; i < listOfPayments.size(); i++) {
            if (listOfPayments.get(i).getPaymentId() == paymentToUpdate.getPaymentId()) {
                listOfPayments.set(i, paymentToUpdate);
                savePayment();
                return;
            }
        }
    }

    public void deletePayment(Payment paymentToDelete) throws Exception {
        for (int i = 0; i < listOfPayments.size(); i++) {
            if (listOfPayments.get(i).getPaymentId() == paymentToDelete.getPaymentId()) {
                listOfPayments.remove(i);
                savePayment();
                return;
            }
        }
    }

    public ArrayList<Payment> getPaymentsByLoanId(int loanId) {
        ArrayList<Payment> result = new ArrayList<>();
        for (Payment p : listOfPayments) {
            Loan l = p.getLoanId();
            if (l != null && l.getLoanId() == loanId) {
                result.add(p);
            }
        }
        return result;
    }

    public ArrayList<Payment> getPaymentsByClientId(int clientId) {
        ArrayList<Payment> result = new ArrayList<>();
        for (Payment p : listOfPayments) {
            Loan l = p.getLoanId();
            if (l != null) {
                Client c = l.getClientId();
                if (c != null && c.getClientId() == clientId) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    private void savePayment() throws Exception {
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            fos.writeObject(listOfPayments);
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Payment> loadPayment() throws Exception {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (ArrayList<Payment>) fis.readObject();
        }
    }
}
