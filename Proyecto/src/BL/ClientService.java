package BL;

import DAL.ClientDAL;
import ET.Client;

public class ClientService {
    ClientDAL clientDAL = new ClientDAL();
    public ClientService() {

    }

    public Client searchClientByIN(String identificationNumber){
        for(Client eachClient : clientDAL.getClients()){
            if(eachClient.getIdentificationNumber().equals(identificationNumber)){
                return eachClient;
            }
        }
        return null;
    }
}
