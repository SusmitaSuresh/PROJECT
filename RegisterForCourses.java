package jdbc;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import gui.Student;
import gui.DocumentFilter2;
class ConfirmButton5 implements ActionListener
{
	PreparedStatement pstat;
	Connection con;
	JComboBox comboBox;
	String email;
	public ConfirmButton5(Connection con,JComboBox comboBox, String email)
	{
		this.con = con;
		this.comboBox = comboBox;
		this.email = email;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		try 
		{
			pstat=con.prepareStatement("UPDATE enrolledstudents SET "+(String)comboBox.getSelectedItem()+ "=-1 WHERE "+(String)comboBox.getSelectedItem()+"=-2 AND email=?");
			pstat.setString(1, email);
			
			pstat.executeUpdate();
		} 
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}
public class RegisterForCourses extends SQLConnection
{
	public RegisterForCourses(String email)
	{
		try 
		{	
			JFrame frame = new JFrame();
			frame.setTitle("register for course");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(null);
			frame.setResizable(false);
			frame.setSize(350, 350);
	 
            pstml = con.prepareStatement("select * from courseCatalogue");
			ResultSet rs = pstml.executeQuery();
			
			ArrayList<String> courses = new ArrayList<String>();
			while(rs.next())
			{
				courses.add(rs.getString("courseCode"));
			}
			
			JComboBox comboBox = new JComboBox(courses.toArray());
	        comboBox.setBounds(100, 100, 150, 30);
	        JButton Confirm = new JButton("Confirm");
	        Confirm.setBounds(100, 150, 80, 30);
	        
	        frame.add(Confirm);
            frame.add(comboBox);
			Confirm.addActionListener(new ConfirmButton5(con, comboBox, email));
			frame.getContentPane().setBackground(Color.white);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}