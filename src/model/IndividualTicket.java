package model;

/**
 * Билет для физического лица (аналог UserSubscription).
 */
public class IndividualTicket extends ConcertTicket {

    public IndividualTicket(String eventName, double baseCost) {
        super(eventName, baseCost);
    }

    public IndividualTicket(String eventName, double baseCost, String zoneType) {
        super(eventName, baseCost, zoneType);
    }

    /**
     * Расчёт как в ЛР №1: базовая цена * коэффициент тарифа.
     */
    @Override
    public double calculateFinalPrice() {
        return roundToTwoDecimals(getZonePrice());
    }
}
