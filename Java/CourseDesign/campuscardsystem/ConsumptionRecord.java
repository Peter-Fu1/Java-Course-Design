package campuscardsystem;

import java.util.Date;

class ConsumptionRecord {
    private double amount;
    private Date date;
    private String location;

    public ConsumptionRecord(double amount, Date date, String location) {
        this.amount = amount;
        this.date = date;
        this.location = location;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "消费金额: " + amount + ", 日期: " + date + ", 地点: " + location;
    }
}