package DAL;

import ET.Client;
import javax.swing.*;
import java.util.ArrayList;

public class ClientDAL {
    private static final ArrayList<Client> listOfClients = new ArrayList<>();
    private static int idCounter = 0;
    public ClientDAL() {

    }
    public void insertClient(Client clientToInsert){
        clientToInsert.setClientId(idCounter++);
        listOfClients.add(clientToInsert);
    }

    public ArrayList<Client> getClients(){
        return listOfClients;
    }

    public boolean updateClient(Client clientToUpdate){
        for (int i = 0; i < listOfClients.size(); i++){
            if(listOfClients.get(i).getClientId() == clientToUpdate.getClientId()){
                listOfClients.set(i, clientToUpdate);
                return true;
            }
        }
        return false;
    }

    public void deleteClient(Client clientToDelete){
        listOfClients.remove(clientToDelete);
    }
}
