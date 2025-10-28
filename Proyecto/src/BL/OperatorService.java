package BL;

import DAL.OperatorDAL;
import ET.Operator;

public class OperatorService {
    OperatorDAL operatorDAL = new OperatorDAL();
    public OperatorService() throws Exception {

    }
    public Operator searchOperator(int id){
        for(Operator eachOperator : operatorDAL.getOperators()){
            if(eachOperator.getOperatorId() == id){
                return eachOperator;
            }
        }
        return null;
    }
    //TODO
}
