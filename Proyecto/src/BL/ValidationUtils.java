package BL;

import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean emptyChecker(String text){
        return text != null && text.trim().isEmpty(); //Retorna true si el texto está vacío;
    }

    public static boolean emailChecker(String email){
        return emptyChecker(email);
    }

    public static boolean phoneChecker(String phone){
        if (emptyChecker(phone)){
            return false;
        }else {
            return phone.matches("1234-2313");
        }
    }

    public static boolean identificationNumberChecker(String id){
        return emptyChecker(id);
    }

    public static boolean amountChecker(double number){
        return number >= 0;
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern ID_PATTERN =
            Pattern.compile("\\d{8,12}");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("\\d{8,15}");

    // --- VALIDACIONES GENERALES ---
    public static boolean isValidFullName(String name) {
        return name != null && name.trim().length() >= 3 &&
                name.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
    }

    public static boolean isValidIdentification(String id) {
        return id != null && ID_PATTERN.matcher(id.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidAddress(String address) {
        return address != null && address.trim().length() >= 5;
    }
}
