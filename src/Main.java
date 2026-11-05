import billing.TicketOffice;
import model.ConcertTicket;
import model.CorporateTicket;
import model.FanZonePass;
import model.IndividualTicket;
import service.NotificationService;
import service.impl.EmailNotification;
import service.impl.SmsNotification;

/**
 * Точка входа: демонстрация гибкости архитектуры.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=== 1. Физическое лицо, уведомление по SMS ===");
        TicketOffice smsOffice = new TicketOffice(new SmsNotification());

        ConcertTicket personal = new IndividualTicket("Рок-фестиваль 'Гром'", 2000.0, "FanZone");
        FanZonePass meetAndGreet = new FanZonePass("Meet & Greet с артистом", 2500.0);
        double personalPrice = personal.calculateFinalPrice(meetAndGreet, true);
        System.out.printf("Цена билета: %.2f руб.%n", personalPrice);   // 2000*1.6 + 2500 + 500 = 6200
        smsOffice.processPayment(personal, "+7 (999) 123-45-67", personalPrice);

        System.out.println();
        System.out.println("=== 2. Юридическое лицо (5 мест), уведомление по E-mail ===");
        TicketOffice emailOffice = new TicketOffice(new EmailNotification());

        ConcertTicket corporate = new CorporateTicket("Джаз-вечер 'Blue Note'", 3500.0, "VIP", 5);
        double corporatePrice = corporate.calculateFinalPrice(meetAndGreet); // опция не применится
        System.out.printf("Цена заказа: %.2f руб.%n", corporatePrice);  // 3500*3.5*5 = 61250
        emailOffice.processPayment(corporate, "hr@company.ru", corporatePrice);

        System.out.println();
        TicketOffice.printGlobalStats();                                // 2 заказа, 67450.00 руб.

        System.out.println();
        System.out.println("=== 3. Демонстрация проверок (без оплаты) ===");
        ConcertTicket badPrice = new IndividualTicket("Классика в парке", -50.0, "Партер");
        System.out.printf("Цена: %.2f руб.%n", badPrice.calculateFinalPrice());

        ConcertTicket badSeats =
                new CorporateTicket("Поп-концерт 'Звёзды FM'", 2500.0, "DanceFloor", 0);
        System.out.printf("Цена: %.2f руб.%n", badSeats.calculateFinalPrice());

        NotificationService sms = new SmsNotification();
        sms.sendNotification("Тест", "12345");
        sms.sendNotification("Тест", "8-912-000-11-22");

        NotificationService email = new EmailNotification();
        email.sendNotification("Тест", "user.mail.ru");
        email.sendNotification("Тест", "  User@Mail.RU ");
    }
}
