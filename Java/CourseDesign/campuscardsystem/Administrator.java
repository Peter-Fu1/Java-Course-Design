package campuscardsystem;

import java.util.Date;
import java.util.*;

class Administrator extends User {
    public Administrator(String username, String password) {
        super(username, password);
    }

    public void approveRegistration(Student student) {
        student.registerConfirmed = true;
        System.out.println("学生注册已审核通过：" + student.studentId);
    }

    public void confirmLoss(Student student) {
        if (student != null) {
            student.lostConfirmed = true;
            System.out.println("学生挂失已确认：" + student.studentId);
        }
    }

    public void confirmUnblock(Student student) {
        if (student != null) {
            student.lostConfirmed = false;
            System.out.println("学生解挂已确认：" + student.studentId);
        }
    }

    public void statistics(Date startDate, Date endDate, String location, List<Student> students) {
        for (Student student : students) {
            student.viewConsumptionRecords(startDate, endDate, location);
        }
    }
}
