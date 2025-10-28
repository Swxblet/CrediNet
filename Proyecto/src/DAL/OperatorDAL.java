package DAL;

import ET.Operator;

import java.io.*;
import java.util.ArrayList;

public class OperatorDAL {
    private static final String DATA_DIR = "Proyecto/data/Operators";
    private static final String FILE_PATH = DATA_DIR + File.separator + "operators.dat";

    private static ArrayList<Operator> listOfOperators = new ArrayList<>();
    private static int idCounter = 0;
    public OperatorDAL() throws Exception{
        listOfOperators = loadOperators();
    }
    public void insertOperator(Operator operatorToInsert) throws Exception{
        operatorToInsert.setOperatorId(idCounter++);
        listOfOperators.add(operatorToInsert);
        saveOperators();
    }
    public ArrayList<Operator> getOperators(){
        return listOfOperators;
    }
    public void updateOperator(Operator operatorToUpdate) throws Exception{
        for (int i = 0; i < listOfOperators.size(); i++){
            if(listOfOperators.get(i).getOperatorId() == operatorToUpdate.getOperatorId()){
                listOfOperators.set(i, operatorToUpdate);
                saveOperators();
                return;
            }
        }
    }
    public void deleteOperator(Operator operatorToDelete) throws Exception{
        for (int i = 0; i < listOfOperators.size(); i++){
            if(listOfOperators.get(i).getOperatorId() == operatorToDelete.getOperatorId()){
                listOfOperators.remove(i);
                saveOperators();
                return;
            }
        }
    }
    private void saveOperators() throws Exception{
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            fos.writeObject(listOfOperators);
        }
    }
    @SuppressWarnings("unchecked")
    private ArrayList<Operator> loadOperators() throws Exception{
        File file = new File(FILE_PATH);
        if (!file.exists()){
            return new ArrayList<>();
        }
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (ArrayList<Operator>) fis.readObject();
        }
    }
}
