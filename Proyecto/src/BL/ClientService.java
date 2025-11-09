package BL;

import DAL.ClientDAL;
import ET.Client;

import java.util.ArrayList;

public class ClientService {
    ClientDAL clientDAL = new ClientDAL();
    public ClientService() throws Exception {

    }

    public Client searchClientByIN(String identificationNumber){
        for(Client eachClient : clientDAL.getClients()){
            if(eachClient.getIdentificationNumber().equals(identificationNumber)){
                return eachClient;
            }
        }
        return null;
    }

    public Client searchClientToVerify(String email, String password){
        for(Client eachClient : clientDAL.getClients()){
            if(eachClient.getEmail().equals(email) && eachClient.getPassword().equals(password)){
                return eachClient;
            }
        }
        return null;
    }

    public boolean addClient(String fullName, String identificationNumber, String email,
                             String address, String phone, String password) throws Exception{
        if (ValidationUtils.emptyChecker(fullName) ||
                ValidationUtils.emailChecker(email) ||
                ValidationUtils.identificationNumberChecker(identificationNumber) ||
                ValidationUtils.emptyChecker(address) ||
                ValidationUtils.emptyChecker(phone)||
                ValidationUtils.emptyChecker(password)){
            return false;
        }

        if (searchClientByIN(identificationNumber)!=null){
            return false;
        }

        Client newClient1 = new Client(0, fullName, identificationNumber, address, phone, email, password);
        clientDAL.insertClient(newClient1);
        return true;
    }

    public boolean updateClient(String in,Client newClient) throws Exception{
        Client existingClient = searchClientByIN(in);
        if (existingClient != null){
            existingClient.setFullName(newClient.getFullName());
            existingClient.setEmail(newClient.getEmail());
            existingClient.setAddress(newClient.getAddress());
            existingClient.setPhone(newClient.getPhone());

            clientDAL.updateClient(existingClient);
            return true;
        }
        return false;
    }

    public boolean deleteClient(Client client) throws Exception{
        if (client != null){
            clientDAL.deleteClient(client);
            return true;
        }
        return false;
    }

    public ArrayList<Client> getAllClients(){
        return clientDAL.getClients();
    }
}
