package jdbc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.Color;
public class TADuty extends SQLConnection
{
    String taEmail;

    // 🔹 Constructor gets TA email after login
    public TADuty(String taEmail)
    {
        this.taEmail = taEmail;
        loadCourses();
    }

    // 🔹 Step 1: Get professor → courses
    public void loadCourses()
    {
        try
        {
            // Get professor assigned to TA
            PreparedStatement p1 = con.prepareStatement(
                "SELECT professor_email FROM ta_professor WHERE ta_email=?"
            );
            p1.setString(1, taEmail);
            ResultSet rs1 = p1.executeQuery();

            if (!rs1.next())
            {
                JOptionPane.showMessageDialog(null, "No professor assigned");
                return;
            }

            String profEmail = rs1.getString("professor_email");

            // Get courses under professor
            PreparedStatement p2 = con.prepareStatement(
                "SELECT courseCode FROM courseCatalogue WHERE professor_email=?"
            );
            p2.setString(1, profEmail);
            ResultSet rs2 = p2.executeQuery();

            ArrayList<String> courses = new ArrayList<>();

            while (rs2.next())
            {
                courses.add(rs2.getString("courseCode"));
            }

            if (courses.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "No courses found");
                return;
            }

            showCourseSelection(courses);

        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // 🔹 Step 2: Course selection UI
    public void showCourseSelection(ArrayList<String> courses)
    {
        JFrame frame = new JFrame("Select Course");
        frame.setSize(400, 300);
        frame.setLayout(null);

        JLabel label = new JLabel("Select Course:");
        label.setBounds(120, 20, 200, 30);

        JComboBox<String> courseBox = new JComboBox<>(courses.toArray(new String[0]));
        courseBox.setBounds(100, 60, 200, 30);

        JButton next = new JButton("Manage");
        next.setBounds(140, 120, 100, 30);

        next.addActionListener(e -> {
            String selectedCourse = (String) courseBox.getSelectedItem();
            frame.dispose();
            loadStudents(selectedCourse);
        });

        frame.add(label);
        frame.add(courseBox);
        frame.add(next);

        frame.getContentPane().setBackground(Color.white);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // 🔹 Step 3: Load students under selected course
    public void loadStudents(String course)
    {
        try
        {
            PreparedStatement p = con.prepareStatement(
                "SELECT email FROM enrolledStudents WHERE courseCode=?"
            );
            p.setString(1, course);

            ResultSet rs = p.executeQuery();

            ArrayList<String> students = new ArrayList<>();

            while (rs.next())
            {
                students.add(rs.getString("email"));
            }

            if (students.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "No students found");
                return;
            }

            showStudentSelection(course, students);

        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // 🔹 Step 4: Student + Grade UI
    public void showStudentSelection(String course, ArrayList<String> students)
    {
        JFrame frame = new JFrame("Manage Grades");
        frame.setSize(400, 350);
        frame.setLayout(null);

        JLabel l1 = new JLabel("Select Student:");
        l1.setBounds(100, 20, 200, 30);

        JComboBox<String> studentBox = new JComboBox<>(students.toArray(new String[0]));
        studentBox.setBounds(100, 60, 200, 30);

        JLabel l2 = new JLabel("Enter Grade:");
        l2.setBounds(100, 110, 200, 30);

        JTextField gradeField = new JTextField();
        gradeField.setBounds(100, 140, 100, 30);

        JButton update = new JButton("Update");
        update.setBounds(120, 200, 120, 30);

        update.addActionListener(e -> {
            try
            {
                int grade = Integer.parseInt(gradeField.getText());

                if (grade < 0 || grade > 100)
                {
                    JOptionPane.showMessageDialog(null, "Grade must be between 0–100");
                    return;
                }

                PreparedStatement p = con.prepareStatement(
                    "UPDATE enrolledStudents SET grade=? WHERE email=? AND courseCode=?"
                );

                p.setInt(1, grade);
                p.setString(2, (String) studentBox.getSelectedItem());
                p.setString(3, course);

                int rows = p.executeUpdate();

                if (rows > 0)
                    JOptionPane.showMessageDialog(null, "Grade Updated Successfully");
                else
                    JOptionPane.showMessageDialog(null, "Update Failed");

            } 
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(null, "Invalid input");
            } 
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        });

        frame.add(l1);
        frame.add(studentBox);
        frame.add(l2);
        frame.add(gradeField);
        frame.add(update);

        frame.getContentPane().setBackground(Color.white);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void main(String[] args)
    {
        new TADuty(null);
    }
}