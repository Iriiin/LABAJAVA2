package service.impl;

import service.NotificationService;

/**
 * Реализация уведомлений по электронной почте.
 */
public class EmailNotification implements NotificationService {

    @Override
    public void sendNotification(String message, String recipient) {
        if (recipient == null || recipient.isBlank()) {
            System.out.println("[EMAIL][WARNING] Адрес получателя не указан. Письмо не отправлено.");
            return;
        }

        // Исправление «на лету»: убираем пробелы по краям и приводим адрес к нижнему регистру
        String email = recipient.trim().toLowerCase();
        if (!email.equals(recipient)) {
            System.out.println("[EMAIL][INFO] Адрес исправлен: '" + recipient + "' -> '" + email + "'");
        }

        if (!isValidEmail(email)) {
            System.out.println("[EMAIL][WARNING] Некорректный адрес '" + email
                    + "' (должен содержать '@' и '.'). Письмо не отправлено.");
            return;
        }

        System.out.println("[EMAIL] На адрес " + email + " отправлено письмо: " + message);
    }

    /**
     * Проверка адреса: есть символ '@' (не первым), а после него есть точка (и не в самом конце).
     */
    private boolean isValidEmail(String email) {
        int atIndex = email.indexOf('@');
        int dotIndex = email.lastIndexOf('.');

        if (atIndex <= 0) {
            return false;                       // нет '@' или '@' стоит первым символом
        } else if (dotIndex < atIndex + 2) {
            return false;                       // нет точки в домене (после '@')
        } else if (dotIndex == email.length() - 1) {
            return false;                       // точка стоит последним символом
        } else {
            return true;
        }
    }
}
