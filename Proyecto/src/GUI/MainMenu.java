package GUI;

import javax.swing.*;

public class MainMenu {
    int option;
    public void showClientMenu() {
        while(true) {
            String option = JOptionPane.showInputDialog(
                    "==== Menú Principal ====\n" +
                            "1. Gestión de Clientes\n" +
                            "2. Gestión de Préstamos\n" +
                            "3. Pagos\n" +
                            "0. Salir"
            );

            if (option == null) break; // cancelar cierra
            switch (option) {
                case "1":
                    new ClientMenu().show();
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, "Gestión de Préstamos (en desarrollo)");
                    break;
                case "3":
                    JOptionPane.showMessageDialog(null, "Pagos (en desarrollo)");
                    break;
                case "0":
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida");
            }
        }
    }
}
