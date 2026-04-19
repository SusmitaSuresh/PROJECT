package gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;

import jdbc.DropCourses;
import jdbc.RegisterForCourses;
import jdbc.SQLConnection;
import jdbc.SubmitComplaints;
import jdbc.TrackAcademicProgress;
import jdbc.ViewAvailableCourses;
import jdbc.ViewSchedule;
class Vac implements ActionListener
{
	JComboBox vacb;
	Vac	(JComboBox vacb)
	{
		this.vacb=vacb;
	}
	public void actionPerformed(ActionEvent e) 
	{
		vacb.setVisible(true);
	}
}
class ViewAvailableCourse implements ActionListener
{
	public ViewAvailableCourse()
	{
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		new ViewAvailableCourses();
	}
}
class RegisterForCourse implements ActionListener
{
	String email;
	public RegisterForCourse(String email)
	{
		this.email = email;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		new RegisterForCourses(email);
	}
}
class DropCourse implements ActionListener
{
	String email;
	public DropCourse(String email)
	{
		this.email = email;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		new DropCourses(email);
	}
}
class SubmitComplaint implements ActionListener
{
	public SubmitComplaint()
	{
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		new SubmitComplaints();
	}
}
class TrackAcademicProgresss implements ActionListener
{
	String email;
	public TrackAcademicProgresss(String email)
	{
		this.email = email;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		new TrackAcademicProgress(email);
	}
}
class ViewSchedules implements ActionListener
{
	public ViewSchedules()
	{
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		new ViewSchedule();
	}
}



public class Student 
{
	JFrame frame = new JFrame();
	JLabel wlc = new JLabel("Welcome, STUDENT");
	Student(String email)
	{        
		frame.setTitle("STUDENT");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setSize(1200,800);
		wlc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		wlc.setBounds(10, 5, 800, 50);
		JButton lo = new JButton("Log-Out");
		lo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));
		lo.setBounds(1070, 5, 100, 50);
		JButton vac = new JButton("View Available Courses");
		vac.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		vac.setBounds(50, 400, 200, 50);
		JButton rfc = new JButton("Register for Courses");
		rfc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		rfc.setBounds(50, 500, 200, 50);
		
      
		JButton vs = new JButton("View Schedule");
		vs.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		vs.setBounds(100, 150, 200, 30);
		JButton ap = new JButton("Academic Progress");
		ap.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		ap.setBounds(100, 200, 200, 30);
		JButton dc = new JButton("Drop Course");
		dc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		dc.setBounds(100, 250, 200, 30);
		JButton sc = new JButton("Submit Complaints");
		sc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		sc.setBounds(100, 300, 200, 30);
		
		vac.addActionListener(new ViewAvailableCourse());
		rfc.addActionListener(new RegisterForCourse(email));
		dc.addActionListener(new DropCourse(email));
		sc.addActionListener(new SubmitComplaint());
		ap.addActionListener(new TrackAcademicProgresss(email));
		vs.addActionListener(new ViewSchedules());
		lo.addActionListener(new LogOutButton(frame));
		frame.add(vac);
		frame.add(rfc);
		frame.add(wlc);
		frame.add(lo);
		frame.add(vs);
		frame.add(ap);
		frame.add(dc);
		frame.add(sc);
		frame.setVisible(true);
        
}
	public static void main(String[] args) 
	{
	    new Student("qwertyuiop@gmail.com");
	}
}