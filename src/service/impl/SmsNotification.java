package service.impl;

import service.NotificationService;

/**
 * Реализация уведомлений через SMS.
 */
public class SmsNotification implements NotificationService {

    private static final int LENGTH_WITH_PLUS_7 = 12; // +7XXXXXXXXXX
    private static final int LENGTH_WITH_8 = 11;      // 8XXXXXXXXXX

    @Override
    public void sendNotification(String message, String recipient) {
        if (recipient == null || recipient.isBlank()) {
            System.out.println("[SMS][WARNING] Номер получателя не указан. Сообщение не отправлено.");
            return;
        }

        // Исправление «на лету»: убираем пробелы, дефисы и скобки
        // Пример: "+7 (999) 123-45-67" -> "+79991234567"
        String phone = recipient.replace(" ", "")
                                .replace("-", "")
                                .replace("(", "")
                                .replace(")", "");
        if (!phone.equals(recipient)) {
            System.out.println("[SMS][INFO] Номер исправлен: '" + recipient + "' -> '" + phone + "'");
        }

        if (!isValidPhone(phone)) {
            System.out.println("[SMS][WARNING] Некорректный номер '" + phone
                    + "' (должен начинаться с +7 или 8 и содержать 11 цифр). Сообщение не отправлено.");
            return;
        }

        System.out.println("[SMS] На номер " + phone + " отправлено сообщение: " + message);
    }

    /**
     * Проверка номера: начинается с +7 или 8, нужная длина, после префикса только цифры.
     */
    private boolean isValidPhone(String phone) {
        if (phone.startsWith("+7")) {
            return phone.length() == LENGTH_WITH_PLUS_7 && isDigitsOnly(phone.substring(1));
        } else if (phone.startsWith("8")) {
            return phone.length() == LENGTH_WITH_8 && isDigitsOnly(phone);
        } else {
            return false;
        }
    }

    private boolean isDigitsOnly(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
