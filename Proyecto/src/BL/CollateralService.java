package BL;

import DAL.CollateralDAL;
import ET.Collateral;

public class CollateralService {
    CollateralDAL collateralDAL = new CollateralDAL();
    public CollateralService() throws Exception {

    }
    public Collateral searchCollateral(int id){
        for(Collateral eachCollateral : collateralDAL.getCollaterals()){
            if(eachCollateral.getCollateralId() == id){
                return eachCollateral;
            }
        }
        return null;
    }
    public void addCollateral(Collateral collateralToAdd) throws Exception{
        collateralDAL.collateralInsert(collateralToAdd);
    }
    public void deleteCollateral(Collateral collateralToDelete) throws Exception{
        collateralDAL.collateralDelete(collateralToDelete);
    }
    public void updateCollateral(Collateral collateralToUpdate) throws Exception{
        collateralDAL.collateralUpdate(collateralToUpdate);
    }
}
