package DAL;

import ET.Client;
import javax.swing.*;
import java.util.ArrayList;
import java.io.*;
import java.nio.*;

public class ClientDAL {
    private static final String DATA_DIR = "DATA";
    private static final String FILE_PATH = DATA_DIR + File.separator + "clients.dat";

    private static ArrayList<Client> listOfClients = new ArrayList<>();
    private static int idCounter = 0;
    public ClientDAL() throws Exception{
        listOfClients = loadClient();
    }
    public void insertClient(Client clientToInsert){
        clientToInsert.setClientId(idCounter++);
        listOfClients.add(clientToInsert);
    }

    public ArrayList<Client> getClients(){
        return listOfClients;
    }

    public void updateClient(Client clientToUpdate) throws Exception{
        for (int i = 0; i < listOfClients.size(); i++){
            if(listOfClients.get(i).getClientId() == clientToUpdate.getClientId()){
                listOfClients.set(i, clientToUpdate);
                saveClient();
            }
        }
    }

    public void deleteClient(Client clientToDelete){
        listOfClients.remove(clientToDelete);
    }

    private void saveClient() throws Exception {
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            fos.writeObject(listOfClients);
        }
    }
    @SuppressWarnings("unchecked")
    private ArrayList<Client> loadClient() throws Exception {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new  ArrayList<>();
        }
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return  (ArrayList<Client>) fis.readObject();
        }
    }
}
