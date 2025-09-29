package UTILS;

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
            return phone.matches("8");
        }
    }

    public static boolean identificationNumberChecker(String id){
        return emptyChecker(id);
    }

    public static boolean amountChecker(double number){
        return number >= 0;
    }
}
