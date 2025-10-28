package DAL;

import ET.CreditHistory;

import java.io.*;
import java.util.ArrayList;

public class CreditHistoryDAL {
    private static final String DATA_DIR = "Proyecto/data/CreditHistory";
    private static final String FILE_PATH = DATA_DIR + File.separator + "creditHistory.dat";

    private static ArrayList<CreditHistory> listOfCreditHistory = new ArrayList<>();
    private static int idCounter = 0;
    public CreditHistoryDAL() throws Exception{
        listOfCreditHistory = loadCreditHistory();
    }
    public void insertCreditHistory(CreditHistory creditHistoryToInsert) throws Exception{
        creditHistoryToInsert.setCreditId(idCounter++);
        listOfCreditHistory.add(creditHistoryToInsert);
        saveCreditHistory();
    }
    public ArrayList<CreditHistory> getCreditHistory(){
        return listOfCreditHistory;
    }
    public void updateCreditHistory(CreditHistory creditHistoryToUpdate) throws Exception{
        for (int i = 0; i < listOfCreditHistory.size(); i++){
            if(listOfCreditHistory.get(i).getCreditId() == creditHistoryToUpdate.getCreditId()){
                listOfCreditHistory.set(i, creditHistoryToUpdate);
                saveCreditHistory();
            }
        }
    }
    public void deleteCreditHistory(CreditHistory creditHistoryToDelete) throws Exception{
        for (int i = 0; i < listOfCreditHistory.size(); i++){
            if(listOfCreditHistory.get(i).getCreditId() == creditHistoryToDelete.getCreditId()){
                listOfCreditHistory.remove(i);
                saveCreditHistory();
            }
        }
    }
    private void saveCreditHistory() throws Exception{
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            fos.writeObject(listOfCreditHistory);
        }
    }
    @SuppressWarnings("unchecked")
    private ArrayList<CreditHistory> loadCreditHistory() throws Exception{
        File file = new File(FILE_PATH);
        if (!file.exists()){
            return new ArrayList<>();
        }
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (ArrayList<CreditHistory>) fis.readObject();
        }
    }
}
