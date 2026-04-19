package jdbc;
import java.awt.Color;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import gui.Student;
import gui.DocumentFilter2;
import java.util.ArrayList;


public class DropCourses extends SQLConnection
{
	public DropCourses(String email)
	{
	try 
	{
		JFrame frame=new JFrame();
		frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pstml = con.prepareStatement("SELECT *from courseCatalogue");
		ResultSet rs = pstml.executeQuery();
		ArrayList<String> course = new ArrayList<String>();
		while(rs.next())
		{
			course.add(rs.getString("courseCode"));
		}
		String[] coursess = course.toArray(new String[0]);
		JComboBox<String> comboBoxx = new JComboBox<>(coursess);
		comboBoxx.setBounds(100, 100, 150, 30);
		JButton Confirmm = new JButton("Confirm");
		Confirmm.setBounds(100, 150, 80, 30);
		 Confirmm.addActionListener(e -> {
			 try
             {
                 pstat = con.prepareStatement("UPDATE enrolledstudents SET "+(String)comboBoxx.getSelectedItem()+ "=-2 WHERE "+(String)comboBoxx.getSelectedItem()+"=-1 AND email=?");
				 pstat.setString(1, email);
                 int rows = pstat.executeUpdate();

                 if (rows > 0)
                     JOptionPane.showMessageDialog(frame, "Course Dropped!");
                 else
                     JOptionPane.showMessageDialog(frame, "No course found!");

             }
             catch (SQLException ex)
             {
                 ex.printStackTrace();
             }
         });
		frame.add(comboBoxx);
		frame.add(Confirmm);
		frame.setVisible(true);
	}
	
	catch (SQLException ex) 
	{
		ex.printStackTrace();
	}
}
}