package model;

/**
 * Дополнительная опция к билету (Meet & Greet, мерч и т.п.).
 * Класс оставлен без изменений по сравнению с частью 1.
 */
public class FanZonePass {

    private final String name;
    private double cost;

    public FanZonePass(String name, double cost) {
        this.name = name;
        setCost(cost);
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Стоимость опции не может быть отрицательной!");
        }
        this.cost = roundToTwoDecimals(cost);
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
