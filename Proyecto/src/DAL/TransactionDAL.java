package DAL;

import ET.Transaction;

import java.io.*;
import java.util.ArrayList;

public class TransactionDAL {
    private static final String DATA_DIR = "Proyecto/data/Transactions";
    private static final String FILE_PATH = DATA_DIR + File.separator + "transactions.dat";

    private static ArrayList<Transaction> listOfTransactions = new ArrayList<>();
    private static int idCounter = 0;
    public TransactionDAL() throws Exception{
        listOfTransactions = loadTransactions();
    }
    public void insertTransaction(Transaction transactionToInsert) throws Exception{
        transactionToInsert.setTransactionId(idCounter++);
        listOfTransactions.add(transactionToInsert);
        saveTransactions();
    }
    public ArrayList<Transaction> getTransactions(){
        return listOfTransactions;
    }
    public void updateTransaction(Transaction transactionToUpdate) throws Exception{
        for (int i = 0; i < listOfTransactions.size(); i++){
            if(listOfTransactions.get(i).getTransactionId() == transactionToUpdate.getTransactionId()){
                listOfTransactions.set(i, transactionToUpdate);
                saveTransactions();
                return;
            }
        }
    }
    public void deleteTransaction(Transaction transactionToDelete) throws Exception{
        for (int i = 0; i < listOfTransactions.size(); i++){
            if(listOfTransactions.get(i).getTransactionId() == transactionToDelete.getTransactionId()){
                listOfTransactions.remove(i);
                saveTransactions();
                return;
            }
        }
    }
    public void saveTransactions() throws Exception{
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            fos.writeObject(listOfTransactions);
        }
    }
    @SuppressWarnings("unchecked")
    private ArrayList<Transaction> loadTransactions() throws Exception{
        File file = new File(FILE_PATH);
        if (!file.exists()){
            return new ArrayList<>();
        }
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (ArrayList<Transaction>) fis.readObject();
        }
    }
}
