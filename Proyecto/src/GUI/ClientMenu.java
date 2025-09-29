package GUI;

import BL.ClientService;
import ET.Client;

import javax.swing.*;

public class ClientMenu {
    ClientService clientService = new ClientService();
    public void show() {
        String option;
        do{
            option = JOptionPane.showInputDialog(
                    "=== Gestión de Clientes ===\n" +
                            "1. Crear Cliente\n" +
                            "2. Mostrar Clientes\n" +
                            "3. Actualizar Cliente\n" +
                            "4. Eliminar Cliente\n" +
                            "0. Volver"
            );

            if (option == null) break;
            switch (option) {
                case "1":
                    createClient();
                    break;
                case "2":
                    showClients();
                    break;
                case "3":
                    updateClient();
                    break;
                case "4":
                    deleteClient();
                    break;
                case "0":
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida");
            }
        }while (!option.equals("0"));
    }

    private void createClient() {
        String id = JOptionPane.showInputDialog("Cédula:");
        String name = JOptionPane.showInputDialog("Nombre completo:");
        String email = JOptionPane.showInputDialog("Correo:");
        String address = JOptionPane.showInputDialog("Dirección:");
        String phone = JOptionPane.showInputDialog("Teléfono:");

        boolean success = clientService.addClient(id, name, email, address, phone);
        JOptionPane.showMessageDialog(null, success ? "Cliente creado con éxito" : "Error al crear cliente");
    }

    private void showClients() {
        StringBuilder sb = new StringBuilder("=== Lista de Clientes ===\n");
        for (Client c : clientService.getAllClients()) {
            sb.append("ID interno: ").append(c.getClientId())
                    .append(" | Cédula: ").append(c.getIdentificationNumber())
                    .append(" | Nombre: ").append(c.getFullName())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void updateClient() {
        String id = JOptionPane.showInputDialog("Ingrese cédula del cliente a actualizar:");
        String newName = JOptionPane.showInputDialog("Nuevo nombre:");
        String newEmail = JOptionPane.showInputDialog("Nuevo correo:");
        String newAddress = JOptionPane.showInputDialog("Nueva dirección:");
        String newPhone = JOptionPane.showInputDialog("Nuevo teléfono:");

        Client updated = new Client(0, id, newName, newAddress, newPhone, newEmail);
        boolean success = clientService.updateClient(id, updated);
        JOptionPane.showMessageDialog(null, success ? "Cliente actualizado" : "Cliente no encontrado");
    }

    private void deleteClient() {
        String id = JOptionPane.showInputDialog("Ingrese cédula del cliente a eliminar:");
        boolean success = clientService.deleteClient(id);
        JOptionPane.showMessageDialog(null, success ? "Cliente eliminado" : "Cliente no encontrado");
    }
}
