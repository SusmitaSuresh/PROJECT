package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jdbc.AssignProffessorToCourse;
import jdbc.HandleComplaints;
import jdbc.ManageCourseCatalogue;
import jdbc.ManageStudentRecords;
import jdbc.SQLConnection;

class LogOutButton extends SQLConnection implements ActionListener 
{
	JFrame frame;
	public LogOutButton(JFrame frame) 
	{
		this.frame = frame;
	}
	public void actionPerformed(ActionEvent e) 
	{
		(new SQLConnection()).closeConnection();
		frame.dispose();
	}
}

class AptcButton implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		new AssignProffessorToCourse();	
	}
}

class MsrButton implements ActionListener
{
	public MsrButton() 
	{
		
	}
	public void actionPerformed(ActionEvent e) 
	{
		new ManageStudentRecords();
	}
	
}

class MccButton implements ActionListener
{
	JComboBox<String> mccb;
	JButton confirm;
	MccButton(JComboBox<String> mccb, JButton confirm)
	{
		this.mccb = mccb;
		this.confirm = confirm;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		mccb.setVisible(true);
		confirm.setVisible(true);
	}
}

class ConfirmButton implements ActionListener
{
	JComboBox<String> mccb;
	String operation;
	public ConfirmButton(JComboBox<String> mccb, String operation)
	{
		this.mccb = mccb;
		this.operation = operation;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(operation.equals("Manage Course Catalogue"))
		{
			ManageCourseCatalogue m = new ManageCourseCatalogue();
			if(((String)mccb.getSelectedItem()).equals("view"))
			{
				m.view();
			}
			else if(((String)mccb.getSelectedItem()).equals("add"))
			{
				m.add();
			}
			else
			{
				m.delete();
			}
		}
	}
}

class HandleComplaintsButton implements ActionListener
{
	public void actionPerformed(ActionEvent e) 
	{
		new HandleComplaints();
	}
}


public class Admin 
{
	Admin()
	{
		JFrame frame = new JFrame();
		frame.setTitle("ADMINISTRATOR");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setSize(1200,800);
		
		JLabel wlc = new JLabel("Welcome, ADMIN");
		wlc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		wlc.setBounds(10, 5, 200, 50);
		
		JButton lo = new JButton("Log-Out");
		lo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));
		lo.setBounds(1070, 5, 100, 50);
		lo.addActionListener(new LogOutButton(frame));
		
		JButton mcc = new JButton("Manage Course Catalogue");
		mcc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		mcc.setBounds(30, 200, 350, 50);
		
		String items[] = {"view", "add", "delete"};
		JComboBox<String> mccb = new JComboBox<String>(items);
		mccb.setBounds(450, 200, 100, 50);
		mccb.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		
		JButton confirm = new JButton("confirm");
		confirm.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		confirm.setBounds(600, 200, 150, 50);
		
		mcc.addActionListener(new MccButton(mccb, confirm));
		confirm.addActionListener(new ConfirmButton(mccb, mcc.getText()));
		
		JButton msr = new JButton("Manage Student Records");
		msr.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		msr.setBounds(30, 280, 350, 50);
		String item[] = {"view", "update"};
		JComboBox<String> msrb = new JComboBox<String>(item);
		msr.addActionListener(new MsrButton());
		
		JButton aptc = new JButton("Assign Proffessor to Course");
		aptc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		aptc.setBounds(30, 360, 350, 50);
		aptc.addActionListener(new AptcButton());
		
		JButton hc = new JButton("Handle Complaints");
		hc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		hc.setBounds(30, 440, 350, 50);
		hc.addActionListener(new HandleComplaintsButton());
		
		frame.add(mccb);
		mccb.setVisible(false);
		frame.add(confirm);
		confirm.setVisible(false);
		frame.add(msr);
		frame.add(hc);
		frame.add(aptc);
		frame.add(mcc);
		frame.add(wlc);
		frame.add(lo);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
		frame.setVisible(true);
	}	
	
	public static void main(String[] args) {
		new Admin();
	}
}
