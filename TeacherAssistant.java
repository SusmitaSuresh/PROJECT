package gui;

import javax.swing.JButton;
import javax.swing.JLabel;

public class TeacherAssistant extends Student
{
	TeacherAssistant(String email)
	{
		super(email);
		frame.setTitle("TEACHER ASSISTANT");
		wlc.setText("WELCOME, TEACHER ASSISTANT");
		JButton jb = new JButton("manage grades");
		jb.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		jb.setBounds(50, 600, 200, 50);
		
		frame.add(jb);
		
	}
	
	public static void main(String[] args) 
	{
		new TeacherAssistant("qwertyuiop@gmail.com");
	}
	
}
