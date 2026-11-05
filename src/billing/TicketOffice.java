package billing;

import model.ConcertTicket;
import service.NotificationService;

/**
 * Сервисный слой учёта продаж (аналог Billing).
 * Не создаёт сервис уведомлений сам: получает его «снаружи» через конструктор
 * (Dependency Injection) и работает с ним только через интерфейс.
 */
public class TicketOffice {

    // Глобальная статистика: общая для ВСЕХ объектов TicketOffice
    private static int totalOrdersCount;
    private static double totalRevenue;

    // Зависимость объекта: тип поля — интерфейс, а не конкретный класс
    private final NotificationService notificationService;

    public TicketOffice(NotificationService notificationService) {
        if (notificationService == null) {
            throw new IllegalArgumentException("Сервис уведомлений не может быть null!");
        }
        this.notificationService = notificationService;
    }

    /**
     * Регистрирует оплату и отправляет клиенту чек через внедрённый сервис.
     */
    public void processPayment(ConcertTicket ticket, String contact, double finalPrice) {
        if (ticket == null) {
            System.out.println("[WARNING] Билет не передан. Оплата не зарегистрирована.");
            return;
        }
        if (finalPrice <= 0) {
            System.out.println("[WARNING] Сумма оплаты " + finalPrice
                    + " некорректна (должна быть > 0). Оплата не зарегистрирована.");
            return;
        }

        registerSale(finalPrice);

        String receipt = String.format("Чек: билет %s. Оплачено: %.2f руб.",
                ticket.getTicketInfo(), finalPrice);
        notificationService.sendNotification(receipt, contact);
    }

    private static void registerSale(double amount) {
        totalOrdersCount++;
        totalRevenue += amount;
    }

    public static int getTotalOrdersCount() {
        return totalOrdersCount;
    }

    public static double getTotalRevenue() {
        return roundToTwoDecimals(totalRevenue);
    }

    public static void printGlobalStats() {
        System.out.println("ГЛОБАЛЬНАЯ СТАТИСТИКА TICKET OFFICE");
        System.out.printf("Всего оформлено заказов: %-10d%n", totalOrdersCount);
        System.out.printf("Суммарная выручка:       %-10.2f руб.%n", getTotalRevenue());
    }

    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
