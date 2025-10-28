package DAL;

import ET.Collateral;

import java.io.*;
import java.util.ArrayList;

public class CollateralDAL {
    private static final String DATA_DIR = "Proyecto/data/Collaterals";
    private static final String FILE_PATH = DATA_DIR + File.separator + "collateral.dat";

    private static ArrayList<Collateral> listOfCollaterals = new ArrayList<>();
    private static int idCounter = 0;
    public CollateralDAL() throws Exception{
        listOfCollaterals = loadCollateral();
    }
    public void collateralInsert(Collateral collateralToInsert) throws Exception{
        collateralToInsert.setCollateralId(idCounter++);
        listOfCollaterals.add(collateralToInsert);
        saveCollateral();
    }
    public ArrayList<Collateral> getCollaterals(){
        return listOfCollaterals;
    }
    public void collateralUpdate(Collateral collateralToUpdate) throws Exception{
        for (int i = 0; i < listOfCollaterals.size(); i++){
            if(listOfCollaterals.get(i).getCollateralId() == collateralToUpdate.getCollateralId()){
                listOfCollaterals.set(i, collateralToUpdate);
                saveCollateral();
            }
        }
    }
    public void collateralDelete(Collateral collateralToDelete) throws Exception{
        for (int i = 0; i < listOfCollaterals.size(); i++){
            if(listOfCollaterals.get(i).getCollateralId() == collateralToDelete.getCollateralId()){
                listOfCollaterals.remove(i);
                saveCollateral();
            }
        }
    }
    private void saveCollateral() throws Exception{
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            fos.writeObject(listOfCollaterals);
        }
    }
    @SuppressWarnings("unchecked")
    private ArrayList<Collateral> loadCollateral() throws Exception{
        File file = new File(FILE_PATH);
        if (!file.exists()){
            return new ArrayList<>();
        }
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (ArrayList<Collateral>) fis.readObject();
        }
    }
}
