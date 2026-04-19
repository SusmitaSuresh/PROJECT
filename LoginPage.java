package gui;
import jdbc.ManageAccount;
import jdbc.SQLConnection;

import gui.Student;
import gui.Proffessor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;	
import javax.swing.*;	//imports every class from javax.swing
import javax.swing.text.AbstractDocument;

class InvalidLoginException extends Exception
{
	public InvalidLoginException() 
	{
		super("invalid login details!!!");
	}
}


class FinishsetupButton implements ActionListener
{
	ManageAccount m;
	JTextField email;
	JPasswordField password;
	JComboBox<String> user, department, proftoassist;
	JTextField rollno, phoneno, name, expertise;
	public FinishsetupButton(ManageAccount m, JTextField email, JPasswordField psword, JComboBox<String> user, JTextField rollno, JTextField t2, JTextField t, JTextField t4, JComboBox<String> t3, JComboBox<String> proftoassist) 
	{
	    this.m = m;
	    this.email = email;
	    this.password = psword;
	    this.user = user;	
	    this.rollno = rollno;
	    this.phoneno = t2;
	    this.name = t;
	    this.expertise = t4;
	    this.department = t3;
	    this.proftoassist = proftoassist;
	}
	public void actionPerformed(ActionEvent e) 
	{
	    //if it exists it assigns the variables with value otherwise null...... sidenote: ternary operator is so cool
		String r = (rollno != null) ? rollno.getText() : null;
	    String p = (phoneno != null) ? phoneno.getText() : null;
	    String n = (name != null) ? name.getText() : null;
	    String x = (expertise != null) ? expertise.getText() : null;
	    String d = (department != null) ? (String)department.getSelectedItem() : null;
	    String ap = (proftoassist != null) ? (String)proftoassist.getSelectedItem() : null;

	    m.SignUpAnAccount(email.getText(), password.getPassword(), (String)user.getSelectedItem(), r, p, n, x, d, ap);	    
	}
}

class SignUpButton implements ActionListener
{
	JComboBox<String> user;
	ManageAccount m;
	JTextField email; 
	JPasswordField psword;
	public SignUpButton(JComboBox<String> user, ManageAccount m, JTextField email, JPasswordField psword) 
	{
		this.user = user;
		this.m = m;
		this.email = email;
		this.psword = psword;
	}
	
	public boolean isValidEmail()
	{
		String s = email.getText();
		int IndexAt=s.indexOf('@');
		int LastIndexAt=s.lastIndexOf('@');
		if(IndexAt!=LastIndexAt || IndexAt<=0 )
		{
			return false;
		}
		int indexDot = s.indexOf('.', IndexAt);
		if (indexDot == -1 || indexDot == IndexAt + 1) 
		{
	        return false;
		}
		if (indexDot == s.length() - 1) 
		{
	        return false;
		}
		if(s.contains("#") || s.contains("!") || s.contains("%") || s.contains("&") ||s.contains("*") || s.contains(")") || s.contains("("))
		{
			return false;
		}
		return true;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(isValidEmail())
		{
			if(user.getSelectedItem().equals("administrator"))
			{
				email.setText("administrator must LOG-IN");
			}
			else if(((String)user.getSelectedItem()).equals("student"))
			{
				JFrame frame = new JFrame();
				frame.setTitle("sign-up");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(null);
				frame.setResizable(false);
				frame.setSize(500, 500);
				
				JLabel l1 = new JLabel("enter your rollno:");
		        l1.setBounds(50, 50, 150, 30);
				JTextField t = new JTextField();
				t.setBounds(200, 50, 100, 30);
				LoginPage.addLimit(t, 5);
				
				JLabel l2 = new JLabel("enter your phone number:");
		        l2.setBounds(50, 100, 150, 30);
		        JTextField t2 = new JTextField();
		        t2.setBounds(200, 100, 200, 30);
		        LoginPage.addLimit(t2, 10);
		        
		        JLabel l3 = new JLabel("enter your name:");
		        l3.setBounds(50, 150, 150, 30);
		        JTextField t3 = new JTextField();
		        t3.setBounds(200, 150, 200, 30);
		        LoginPage.addLimit(t3, 50);			        
		    	
		        JButton finishsetup = new JButton("finish setup");
		        finishsetup.setBounds(200, 350, 80, 30);
		        finishsetup.addActionListener(new FinishsetupButton(m, email, psword, user, t, t2, t3, null, null,null));
				
		        
		        frame.add(finishsetup);
		        frame.add(t3);
		        frame.add(l3);
		        frame.add(t2);
		        frame.add(l2);
		        frame.add(l1);
		        frame.add(t);
				frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
			else if(((String)user.getSelectedItem()).equals("proffessor"))
			{
				JFrame frame = new JFrame();
				frame.setTitle("sign-up");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(null);
				frame.setResizable(false);
				frame.setSize(500, 400);
				
				JLabel l1 = new JLabel("enter your name:");
		        l1.setBounds(50, 50, 150, 30);
				JTextField t = new JTextField();
				t.setBounds(200, 50, 100, 30);
				LoginPage.addLimit(t, 20);
				
				JLabel l2 = new JLabel("enter your phone number:");
		        l2.setBounds(50, 100, 150, 30);
		        JTextField t2 = new JTextField();
		        t2.setBounds(200, 100, 200, 30);
		        LoginPage.addLimit(t2, 10);
		        
		        String dept[] = {"ai", "cse", "chem", "mech", "ece", "ee", "math", "phy", "hss", "civil"};
		        
		        JLabel l3 = new JLabel("enter your department:");
		        l3.setBounds(50, 150, 150, 30);
		        JComboBox<String> t3 = new JComboBox<String>(dept);
		        t3.setBounds(180, 150, 200, 30);
		        
		        JLabel l4 = new JLabel("enter your expertise:");
		        l4.setBounds(50, 200, 150, 30);
		        JTextField t4 = new JTextField();
		        t4.setBounds(180, 200, 200, 30);
		    	
		        JButton finishsetup = new JButton("finish setup");
		        finishsetup.setBounds(180, 200, 300, 30);
		        finishsetup.addActionListener(new FinishsetupButton(m, email, psword, user, null, t2, t, t4, t3,null));
		        
		        frame.add(finishsetup);
		        frame.add(t4);
		        frame.add(l4);
		        frame.add(t3);
		        frame.add(t2);
		        frame.add(t);
		        frame.add(l3);
		        frame.add(l2);
		        frame.add(l1);
				frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
			else if(((String)user.getSelectedItem()).equals("teacherAssistant"))
			{
				JFrame frame = new JFrame();
				frame.setTitle("sign-up");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(null);
				frame.setResizable(false);
				frame.setSize(500, 500);
				
				JLabel l1 = new JLabel("enter your rollno:");
		        l1.setBounds(50, 50, 150, 30);
				JTextField t = new JTextField();
				t.setBounds(200, 50, 100, 30);
				LoginPage.addLimit(t, 5);
				
				JLabel l2 = new JLabel("enter your phone number:");
		        l2.setBounds(50, 100, 150, 30);
		        JTextField t2 = new JTextField();
		        t2.setBounds(200, 100, 200, 30);
		        LoginPage.addLimit(t2, 10);
		        
		        JLabel l3 = new JLabel("enter your name:");
		        l3.setBounds(50, 150, 150, 30);
		        JTextField t3 = new JTextField();
		        t3.setBounds(200, 150, 200, 30);
		        LoginPage.addLimit(t3, 50);
		        
		        JLabel l4 = new JLabel("enter proffessor to assist:");
		        l4.setBounds(50, 200, 150, 30);
		        SQLConnection s = new SQLConnection();
		        ArrayList<String> profs = new ArrayList<String>();
		        ResultSet r;
				try 
				{
			        s.pstat = s.con.prepareStatement("select name from proffessors");
					
					r= s.pstat.executeQuery();
					while(r.next())
					{
						profs.add(r.getString("name"));
					}
					if(profs.isEmpty())
					{
					    JOptionPane.showMessageDialog(null, "NO PROFFESSORS AVAILABLE");
					    return;
					}
				}
				catch(SQLException ex)
				{
					ex.printStackTrace();
				}
				
		        JComboBox<String> t4 = new JComboBox<String>(profs.toArray(new String[0]));
		        t4.setBounds(200, 200, 200, 30);
		    	
		        JButton finishsetup = new JButton("finish setup");
		        finishsetup.setBounds(200, 350, 80, 30);
		        finishsetup.addActionListener(new FinishsetupButton(m, email, psword, user, t, t2, t3, null, null,t4));
				
		        
		        frame.add(finishsetup);
		        frame.add(t3);
		        frame.add(l4);
		        frame.add(t4);
		        frame.add(l3);
		        frame.add(t2);
		        frame.add(l2);
		        frame.add(l1);
		        frame.add(t);
				frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "INVALID EMAIL, PLEASE TRY AGAIN");
			email.setText("");
		}
	}
}

class LogInButton implements ActionListener
{
	JComboBox<String> user;
	JFrame frame;
	ManageAccount m;
	JTextField email;
	JPasswordField psword;
	public LogInButton(JComboBox<String> user, JFrame frame, ManageAccount m, JTextField email, JPasswordField psword)
	{
		this.user = user;
		this.frame = frame;
		this.m = m;
		this.email = email;
		this.psword = psword;
	}
	public void actionPerformed(ActionEvent e)		//if students password is right, they can get into admin, we will fix that
	{	
		if(ManageAccount.emailExistsOrNot(email.getText()))
		{
			if(m.checkPassword(email.getText(), psword.getPassword()))
			{
				if(((String)user.getSelectedItem()).equals("administrator"))
				{
					new Admin();
					frame.dispose();
				}
				else if(((String)user.getSelectedItem()).equals("student"))
				{
					
					new Student(email.getText());
					frame.dispose();
				}
				else if(((String)user.getSelectedItem()).equals("proffessor"))
				{
					new Proffessor(email.getText());
					frame.dispose();
				}
				else if(((String)user.getSelectedItem()).equals("teacherAssistant"))
				{
					new TeacherAssistant(email.getText());
					frame.dispose();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "WRONG PASSWORD, TRY AGAIN");
				try
				{
					throw new InvalidLoginException();
				}
				catch(InvalidLoginException ex)
				{
					ex.printStackTrace();
				}
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "EMAIL DOES NOT EXIST PLEASE TRY AGAIN");
			try
			{
				throw new InvalidLoginException();
			}
			catch(InvalidLoginException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}


public class LoginPage 
{
	JTextField email = new JTextField();
    JPasswordField psword = new JPasswordField();
	public LoginPage() 
	{
		JFrame frame = new JFrame();
		frame.setTitle("login/sign-up");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//closes all frames
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setSize(500, 500);

		JLabel ulabel = new JLabel("select your type:");
        ulabel.setBounds(50, 200, 150, 30);
		String[] items = {"student", "proffessor", "administrator", "teacherAssistant"};
		JComboBox<String> user = new JComboBox<String>(items);
		user.setBounds(180, 200, 100, 30);
		JLabel label = new JLabel("Enter your email:");
        label.setBounds(50, 250, 150, 30);
        email.setBounds(180, 250, 200, 30);
        addLimit(email, 50);
    	JLabel plabel = new JLabel("Password:");
        plabel.setBounds(50, 300, 150, 30);
        addLimit(psword, 20);
        psword.setBounds(180, 300, 200, 30);
        JButton login = new JButton("login");
        login.setBounds(180, 350, 80, 30);
        login.addActionListener(new LogInButton(user, frame, new ManageAccount(), email, psword));
        JButton signUp = new JButton("sign-up");
        signUp.setBounds(262, 350, 80, 30);
        signUp.addActionListener(new SignUpButton(user, new ManageAccount(), email, psword));

        frame.add(psword);
        frame.add(email);
        frame.add(label);
        frame.add(plabel);
        frame.add(login);
        frame.add(signUp);
        frame.add(user);
        
        frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public static void addLimit(JTextField textField, int limit) 	//this adds limit to number of characters in email or password
	{
        AbstractDocument doc = (AbstractDocument) textField.getDocument();
        doc.setDocumentFilter(new DocumentFilter2(limit));		//here we override document filter class
    }
	public String getEmail()
	{
		return email.getText();
	}
	public String getPassword()
	{
		return String.valueOf(psword.getPassword());
	}
	public static void main(String[] args) {
		new LoginPage();
	}
}
