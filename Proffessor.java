package gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jdbc.ManageCourseCatalogue;
import jdbc.ManageCourses;
import jdbc.ViewEnrolledStudents;
class McButton implements ActionListener
{
	JComboBox mcb;
	McButton(JComboBox mcb)
	{
		this.mcb=mcb;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		mcb.setVisible(true);
	}
}
class confirmButton implements ActionListener
{
	JComboBox<String> mcb;
	String operation;
	public confirmButton(JComboBox<String> mcb, String operation)
	{
		this.mcb = mcb;
		this.operation = operation;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(operation.equals("Manage Course Catalogue"))
		{
			ManageCourses m = new ManageCourses();
			if(((String)mcb.getSelectedItem()).equals("view"))
			{
				m.view();
			}
			else if(((String)mcb.getSelectedItem()).equals("update"))
			{
				m.update();
			}	
		}
	}
}


class VESButton implements ActionListener
{
	String email;
	public VESButton(String email)
	{
		this.email = email;
	}
	public void actionPerformed(ActionEvent e) 
	{
		new ViewEnrolledStudents(email);
	}
}

public class Proffessor
{
	Proffessor(String email)
	{
		JFrame frame = new JFrame();
		frame.setTitle("PROFESSOR");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setSize(1200,800);
		JLabel wlc = new JLabel("Welcome, PROFESSOR");
		wlc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		wlc.setBounds(10, 5, 200, 50);
		JButton lo = new JButton("Log-Out");
		lo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));
		lo.setBounds(1070, 5, 100, 50);
		JButton mc = new JButton("Manage Courses");
		mc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		mc.setBounds(30, 200, 350, 50);
		String items[] = {"view","update"};
		JComboBox mcb = new JComboBox(items);
		JButton confirm = new JButton("confirm");
		confirm.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		confirm.setBounds(600, 200, 150, 50);
		
		mc.addActionListener(new MccButton(mcb, confirm));
		confirm.addActionListener(new confirmButton(mcb, mc.getText()));
		
		mcb.setBounds(400,200,200,50);
		JButton ves = new JButton("View Enrolled Students");
		ves.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		ves.setBounds(30, 280, 350, 50);
		ves.addActionListener(new VESButton(email));
		lo.addActionListener(new LogOutButton(frame));
		frame.add(mc);
		frame.add(wlc);
		frame.add(lo);
		frame.add(ves);
		frame.add(mcb);
		mcb.setVisible(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
		frame.setVisible(true);
	}	
	public static void main(String[] args) 
	{
		new Proffessor(null);
	}
}