package service;

/**
 * Архитектурный контракт: абстрактный сервис уведомлений.
 * Интерфейс описывает ЧТО умеет сервис, но не КАК он это делает.
 */
public interface NotificationService {

    /**
     * Отправляет сообщение получателю.
     *
     * @param message   текст сообщения (например, чек об оплате)
     * @param recipient адрес получателя (e-mail, номер телефона и т.п.)
     */
    void sendNotification(String message, String recipient);
}
