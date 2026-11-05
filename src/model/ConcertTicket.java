package model;

import java.util.Locale;

/**
 * Абстрактный билет на концерт (аналог абстрактного класса Subscription).
 * Хранит общие для всех билетов данные и общую логику,
 * но НЕ знает, как считать итоговую цену: это решают наследники.
 */
public abstract class ConcertTicket {

    protected static final double MIN_BASE_COST = 100.0;
    protected static final double FAST_TRACK_FEE = 500.0;

    private static final double MULTIPLIER_DANCE_FLOOR = 1.0;
    private static final double MULTIPLIER_FAN_ZONE = 1.6;
    private static final double MULTIPLIER_VIP = 3.5;
    private static final String DEFAULT_ZONE = "DanceFloor";

    private final String eventName;
    private final double baseCost;
    private final String zoneType;
    private FanZonePass appliedOption;

    protected ConcertTicket(String eventName, double baseCost) {
        this(eventName, baseCost, DEFAULT_ZONE);
    }

    protected ConcertTicket(String eventName, double baseCost, String zoneType) {
        this.eventName = validateEventName(eventName);
        this.baseCost = validateBaseCost(baseCost);
        this.zoneType = validateZoneType(zoneType);
        this.appliedOption = null;
    }

    // ---------- Проверки входных данных («исправление на лету») ----------

    private static String validateEventName(String eventName) {
        if (eventName == null || eventName.isBlank()) {
            System.out.println("[WARNING] Название концерта не указано. Установлено: 'Без названия'.");
            return "Без названия";
        }
        return eventName.trim();
    }

    private static double validateBaseCost(double baseCost) {
        if (baseCost <= 0) {
            System.out.println("[WARNING] Базовая цена " + baseCost
                    + " некорректна (должна быть > 0). Установлена минимальная цена: " + MIN_BASE_COST);
            return MIN_BASE_COST;
        }
        return baseCost;
    }

    private static String validateZoneType(String zoneType) {
        if (zoneType == null || zoneType.isBlank()) {
            System.out.println("[WARNING] Тариф не указан. Установлен тариф по умолчанию: "
                    + DEFAULT_ZONE);
            return DEFAULT_ZONE;
        }
        return zoneType.trim();
    }

    // ---------- Геттеры ----------

    public String getEventName() {
        return eventName;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public String getZoneType() {
        return zoneType;
    }

    public FanZonePass getAppliedOption() {
        return appliedOption;
    }

    // ---------- Общая логика для всех наследников ----------

    /**
     * Коэффициент тарифа. Многострочный switch-выражение с yield.
     */
    protected final double getZoneMultiplier() {
        return switch (zoneType.toUpperCase(Locale.ROOT)) {
            case "DANCEFLOOR", "ТАНЦПОЛ", "1" -> MULTIPLIER_DANCE_FLOOR;
            case "FANZONE", "ФАН-ЗОНА", "2" -> MULTIPLIER_FAN_ZONE;
            case "VIP", "VIP-ЛОЖА", "3" -> MULTIPLIER_VIP;
            default -> {
                System.out.println("[ADMIN WARNING] Неизвестный тариф '" + zoneType + "' у билета на '"
                        + eventName + "'. Применён базовый множитель " + MULTIPLIER_DANCE_FLOOR
                        + ". Проверьте справочник тарифов!");
                yield MULTIPLIER_DANCE_FLOOR;
            }
        };
    }

    /**
     * Цена одного места с учётом тарифа: базовая цена * коэффициент.
     * Вынесена сюда, чтобы не дублировать формулу в наследниках.
     */
    protected final double getZonePrice() {
        return getBaseCost() * getZoneMultiplier();
    }

    protected final double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // ---------- Расчёт стоимости ----------

    /**
     * Общих правил расчёта больше нет: каждый тип билета считает цену сам.
     */
    public abstract double calculateFinalPrice();

    /**
     * Цена с доп. опцией. Внутри вызывается calculateFinalPrice() того объекта,
     * который реально лежит в переменной (полиморфизм).
     */
    public double calculateFinalPrice(FanZonePass option) {
        if (option == null) {
            return calculateFinalPrice();
        }
        this.appliedOption = option;
        return roundToTwoDecimals(calculateFinalPrice() + option.getCost());
    }

    /**
     * Цена с доп. опцией и проходом без очереди (Fast Track).
     */
    public double calculateFinalPrice(FanZonePass option, boolean fastTrack) {
        double price = calculateFinalPrice(option);
        if (fastTrack) {
            price += FAST_TRACK_FEE;
        }
        return roundToTwoDecimals(price);
    }

    /**
     * Краткое описание билета для чека. Наследники могут дополнить его.
     */
    public String getTicketInfo() {
        return "«" + eventName + "», тариф " + zoneType;
    }
}
