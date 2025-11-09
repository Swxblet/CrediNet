package BL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NotificationCenter {

    // Tipos para colorear bonito en la GUI
    public enum Type { INFO, SUCCESS, WARNING, ERROR }

    public static class Notification {
        private final String title;
        private final String message;
        private final Date   when;
        private final Type   type;

        public Notification(String title, String message, Type type) {
            this.title = title;
            this.message = message;
            this.type = type;
            this.when = new Date();
        }

        public String getTitle()   { return title; }
        public String getMessage() { return message; }
        public Date   getWhen()    { return when; }
        public Type   getType()    { return type; }
    }

    public interface Listener {
        void notificationsUpdated();
    }

    private final List<Notification> notifications = new ArrayList<>();
    private final List<Listener> listeners = new ArrayList<>();

    // No es persistente: solo vive en memoria mientras la app está abierta
    public void push(String title, String message, Type type) {
        notifications.add(0, new Notification(title, message, type)); // última arriba
        notifyListeners();
    }

    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public void clear() {
        notifications.clear();
        notifyListeners();
    }

    public void addListener(Listener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    private void notifyListeners() {
        for (Listener l : new ArrayList<>(listeners)) {
            l.notificationsUpdated();
        }
    }

    // Helpers rápidos
    public void info(String title, String msg)    { push(title, msg, Type.INFO); }
    public void success(String title, String msg) { push(title, msg, Type.SUCCESS); }
    public void warn(String title, String msg)    { push(title, msg, Type.WARNING); }
    public void error(String title, String msg)   { push(title, msg, Type.ERROR); }
}
