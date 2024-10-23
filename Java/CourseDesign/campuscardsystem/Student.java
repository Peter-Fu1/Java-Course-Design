package campuscardsystem;

import java.util.*;

class Student extends User {
    protected String studentId;
    private double balance;
    private List<ConsumptionRecord> consumptionRecords;
    protected boolean isLost;
    protected boolean lostConfirmed;

    public Student(String username, String password, String studentId) {
        super(username, password);
        this.studentId = studentId;
        this.balance = 0;
        this.consumptionRecords = new ArrayList<>();
        this.isLost = false;
        this.lostConfirmed = false;
    }

    public void recharge(double amount) {
        if (lostConfirmed) {
            System.out.println("校园卡已挂失，无法进行此操作。");
            return;
        }
        else if (amount > 500) {
            System.out.println("充值金额超过500元限制。");
        } else {
            balance += amount;
            System.out.println("充值成功。当前余额：" + balance);
        }
    }

    public void consume(double amount, String location) {
        if (lostConfirmed) {
            System.out.println("校园卡已挂失，无法进行此操作。");
            return;
        }
        else if (balance < amount) {
            System.out.println("余额不足，请充值。");
        } else {
            double todayTotal = getTodayConsumptionTotal();
            if (todayTotal + amount > 50) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("请输入密码进行验证：");
                String inputPassword = scanner.nextLine();
                if (!inputPassword.equals(password)) {
                    System.out.println("密码错误，交易被拒绝。");
                    return;
                }
            }
            balance -= amount;
            consumptionRecords.add(new ConsumptionRecord(amount, new Date(), location));
            System.out.println("消费成功。当前余额：" + balance);
        }
    }

    public void reportLoss() {
        isLost = true;
        System.out.println("校园卡已挂失，请联系管理员确认。");
    }

    public void cancelLoss() {
        isLost = false;
        System.out.println("校园卡已解挂，请联系管理员确认。");
    }

    public void viewConsumptionRecords(Date startDate, Date endDate, String location) {
        for (ConsumptionRecord record : consumptionRecords) {
            if ((record.getDate().after(startDate) || record.getDate().equals(startDate)) && (record.getDate().before(endDate) || record.getDate().equals(endDate)) && record.getLocation().equals(location)) {
                System.out.println(record);
            }
        }
    }

    private double getTodayConsumptionTotal() {
        double total = 0;
        Date today = new Date();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(today);
        for (ConsumptionRecord record : consumptionRecords) {
            cal2.setTime(record.getDate());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                             cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if (sameDay) {
                total += record.getAmount();
            }
        }
        return total;
    }

    public boolean isInsuffcientBalance(){
            if (balance < 10) {
                return true;
            }
            return false;
    }

}