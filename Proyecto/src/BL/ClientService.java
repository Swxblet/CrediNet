package BL;

import DAL.ClientDAL;
import ET.Client;

import java.util.ArrayList;

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

    public boolean addClient(String identificationNumber, String fullName, String email,
                             String address, String phone){
        if (ValidationUtils.emptyChecker(fullName) ||
                ValidationUtils.emailChecker(email) ||
                ValidationUtils.identificationNumberChecker(identificationNumber) ||
                ValidationUtils.emptyChecker(address) ||
                ValidationUtils.emptyChecker(phone)){
            return false;
        }

        if (searchClientByIN(identificationNumber)!=null){
            return false;
        }

        Client newClient1 = new Client(0, identificationNumber, fullName, address, phone, email);
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

    public boolean deleteClient(String in){
        Client existingClient = searchClientByIN(in);
        if (existingClient != null){
            clientDAL.deleteClient(existingClient);
            return true;
        }
        return false;
    }

    public ArrayList<Client> getAllClients(){
        return clientDAL.getClients();
    }
}
