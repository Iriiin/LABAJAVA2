package model;

/**
 * Корпоративный (групповой) билет для юридического лица (аналог CorporateSubscription).
 * Компания покупает сразу несколько мест для сотрудников.
 */
public class CorporateTicket extends ConcertTicket {

    private static final int MIN_SEATS_COUNT = 1;

    private final int seatsCount; // количество мест для сотрудников (аналог activeLicensesCount)

    public CorporateTicket(String eventName, double baseCost, String zoneType, int seatsCount) {
        super(eventName, baseCost, zoneType); // сначала инициализируется «родительская» часть объекта
        if (seatsCount < MIN_SEATS_COUNT) {
            System.out.println("[WARNING] Количество мест " + seatsCount
                    + " некорректно (должно быть >= " + MIN_SEATS_COUNT + ")."
                    + " Установлено: " + MIN_SEATS_COUNT);
            this.seatsCount = MIN_SEATS_COUNT;
        } else {
            this.seatsCount = seatsCount;
        }
    }

    public int getSeatsCount() {
        return seatsCount;
    }

    /**
     * (Базовая цена * коэффициент тарифа) * количество мест.
     */
    @Override
    public double calculateFinalPrice() {
        return roundToTwoDecimals(getZonePrice() * seatsCount);
    }

    /**
     * Корпоративным клиентам доп. опции не применяются: предупреждение + стандартная цена.
     */
    @Override
    public double calculateFinalPrice(FanZonePass option) {
        printExtrasNotAllowedWarning();
        return calculateFinalPrice();
    }

    @Override
    public double calculateFinalPrice(FanZonePass option, boolean fastTrack) {
        printExtrasNotAllowedWarning();
        return calculateFinalPrice();
    }

    @Override
    public String getTicketInfo() {
        return super.getTicketInfo() + ", мест: " + seatsCount;
    }

    private void printExtrasNotAllowedWarning() {
        System.out.println("[WARNING] Для корпоративных билетов доп. опции и Fast Track"
                + " не применяются. Рассчитана стандартная цена.");
    }
}
