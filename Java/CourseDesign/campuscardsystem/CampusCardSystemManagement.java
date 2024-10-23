package campuscardsystem;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CampusCardSystemManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Administrator admin = new Administrator("admin", "admin123");
        List<Student> students = new ArrayList<>();

        while (true) {
            System.out.println("欢迎来到校园卡系统");
            System.out.println("1. 学生登录\n2. 管理员登录\n3. 学生注册\n4. 退出");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 3) {
                System.out.print("请输入用户名：");
                String username = scanner.nextLine();
                String password;
                do {
                    System.out.print("请输入密码（至少8位）：");
                    password = scanner.nextLine();
                    if (password.length() < 8) {
                        System.out.println("密码长度必须至少为8位，请重新输入。");
                    }
                } while (password.length() < 8);                
                System.out.print("请输入学生ID：");
                String studentId = scanner.nextLine();
                students.add(new Student(username, password, studentId));
                System.out.println("学生注册成功，请等待管理员审核。");
            } else if (choice == 4) {
                System.out.println("感谢使用校园卡系统，再见！");
                break;
            }

            if (choice == 1) {
                System.out.print("请输入用户名：");
                String username = scanner.nextLine();
                System.out.print("请输入密码：");
                String password = scanner.nextLine();

                Student loggedInStudent = null;
                for (Student student : students) {
                    if (student.login(username, password)) {
                        if(student.registerConfirmed){
                            loggedInStudent = student;
                            break;
                        } else {
                            System.out.println ("您的用户尚未通过审核。");
                            break;
                        }
                    }
                }

                if (loggedInStudent != null) {
                    System.out.println("登录成功。");
                    boolean studentMenu = true;
                    if(loggedInStudent.isInsuffcientBalance()) System.out.println ("余额不足，请及时充值！");
                    while (studentMenu) {
                        System.out.println("1. 修改密码\n2. 充值\n3. 消费\n4. 查看消费记录\n5. 挂失\n6. 解挂\n7. 退出登录");
                        int studentChoice = scanner.nextInt();
                        scanner.nextLine();  // Consume newline

                        switch (studentChoice) {
                            case 1:
                                System.out.print("请输入新密码：");
                                String newPassword = scanner.nextLine();
                                loggedInStudent.changePassword(newPassword);
                                break;
                            case 2:
                                System.out.print("请输入充值金额：");
                                double amount = scanner.nextDouble();
                                loggedInStudent.recharge(amount);
                                break;
                            case 3:
                                System.out.print("请输入消费金额：");
                                double consumeAmount = scanner.nextDouble();
                                scanner.nextLine();
                                System.out.print("请输入消费地点：");
                                String location = scanner.nextLine();
                                loggedInStudent.consume(consumeAmount, location);
                                break;
                            case 4:
                                System.out.print("请输入开始日期（yyyy-mm-dd）：");
                                String start = scanner.nextLine();
                                System.out.print("请输入结束日期（yyyy-mm-dd）：");
                                String end = scanner.nextLine();
                                System.out.print("请输入地点：");
                                String recordLocation = scanner.nextLine();
                                try {
                                    Date startDate = Date.from(LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
                            Date endDate = Date.from(LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    .atStartOfDay(ZoneId.systemDefault()).toInstant());  
                                    loggedInStudent.viewConsumptionRecords(startDate, endDate, recordLocation);
                                } catch (Exception e) {
                                    System.out.println("日期格式无效。");
                                }
                                break;
                            case 5:
                                loggedInStudent.reportLoss();
                                break;
                            case 6:
                                loggedInStudent.cancelLoss();
                                break;
                            case 7:
                                studentMenu = false;
                                break;
                        }
                    }
                } else {
                    System.out.println("登录凭证无效。");
                }
            } else if (choice == 2) {
                System.out.print("请输入用户名：");
                String username = scanner.nextLine();
                System.out.print("请输入密码：");
                String password = scanner.nextLine();

                if (admin.login(username, password)) {
                    System.out.println("管理员登录成功。");
                    boolean adminMenu = true;
                    while (adminMenu) {
                        System.out.println("1. 审核注册\n2. 确认挂失\n3. 确认解挂\n4. 统计用户消费\n5. 退出登录");
                        int adminChoice = scanner.nextInt();
                        scanner.nextLine();  // Consume newline

                        switch (adminChoice) {
                            case 1:
                            System.out.print("请输入要审核的学生ID：");
                            String studentId = scanner.nextLine();
                            boolean studentFound = false;
                        
                            for (Student student : students) {
                                if (student.studentId.equals(studentId)) {
                                    admin.approveRegistration(student);
                                    studentFound = true;
                                    break; // 找到学生后立即跳出循环
                                }
                            }
                            if(!studentFound) {
                                System.out.println("没有该ID的注册申请。");
                            }
                            break;
                            case 2:
                                System.out.print("请输入要确认挂失的学生ID：");
                                studentId = scanner.nextLine();
                                for (Student student : students) {
                                    if (student.studentId.equals(studentId)) {
                                        if (!student.isLost) {
                                            System.out.println("该用户没有待处理的挂失申请。");
                                        } else if (student.isLost) {
                                            admin.confirmLoss(student);
                                        } else {
                                            System.out.println ("用户不存在。");
                                        }
                                        break;
                                    }
                                }
                                break;
                            case 3:
                                System.out.print("请输入要确认解挂的学生ID：");
                                studentId = scanner.nextLine();
                                for (Student student : students) {
                                    if (student.studentId.equals(studentId)) {
                                        if (student.isLost) {
                                            admin.confirmUnblock(student);
                                            System.out.println("学生解挂已确认：" + studentId);
                                        } else if (student.isLost){
                                            System.out.println("该用户没有待处理的解挂申请。");
                                        } else {
                                            System.out.println ("用户不存在。");
                                        }
                                        break;
                                    }
                                }
                                break;
                            case 4:
                                System.out.print("请输入开始日期（yyyy-mm-dd）：");
                                String start = scanner.nextLine();
                                System.out.print("请输入结束日期（yyyy-mm-dd）：");
                                String end = scanner.nextLine();
                                System.out.print("请输入地点：");
                                String recordLocation = scanner.nextLine();
                                try {
                                    Date startDate = Date.from(LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                    Date endDate = Date.from(LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                    admin.statistics(startDate, endDate, recordLocation, students);
                                } catch (Exception e) {
                                    System.out.println("日期格式无效。");
                                }
                                break;
                            case 5:
                                adminMenu = false;
                                break;
                        }
                    }
                } else {
                    System.out.println("管理员登录凭证无效。");
                }
            }
        }
        scanner.close();
    }
}
