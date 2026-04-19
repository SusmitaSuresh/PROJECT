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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import gui.DocumentFilter2;



class UpdatePersonalInformation implements ActionListener
{
	JComboBox<String>  email;
	PreparedStatement pstat;
	PreparedStatement p;
	PreparedStatement q;
	public UpdatePersonalInformation(JComboBox<String> email, PreparedStatement pstat, PreparedStatement p, PreparedStatement q) 
	{
		this.email = email;
		this.pstat = pstat;
		this.p = p;
		this.q = q;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		try 
		{
			pstat.setString(1, (String)email.getSelectedItem());
			ResultSet rs = pstat.executeQuery();
			JFrame frame = new JFrame();
			frame.setTitle("UpdatePersonalInformation");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(null);
			frame.setResizable(false);
			frame.setSize(400, 400);
			
			if(!rs.next())
			{
				JOptionPane.showMessageDialog(null, "DATABASE ERROR");
				return;
			}
			
			JLabel l1 = new JLabel("name: ");
			l1.setBounds(50, 50, 50, 30);
			JTextField t1 = new JTextField();
			t1.setBounds(120, 50, 100, 30);
			t1.setText(rs.getString("name"));
			
			JLabel l2 = new JLabel("rno: ");
			l2.setBounds(50, 100, 50, 30);
			JTextField t2 = new JTextField();
			t2.setBounds(120, 100, 100, 30);
			t2.setText(rs.getString("rno"));
			
			JLabel l3 = new JLabel("phoneno: ");
			l3.setBounds(50, 150, 50, 30);
			JTextField t3 = new JTextField();
			t3.setBounds(120, 150, 100, 30);
			t3.setText(rs.getString("phoneno"));
			
			JButton confirm = new JButton("confirm");
			confirm.setBounds(50, 200, 100, 50);
			confirm.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{
						try 
						{
							p.setString(1, t1.getText());
							p.setString(2, t2.getText());
							p.setString(3, t3.getText());
							p.setString(4, (String)email.getSelectedItem());
							q.setString(1, t1.getText());
							q.setString(2, (String)email.getSelectedItem());
							p.execute();
							q.execute();
							JOptionPane.showMessageDialog(null, "UPDATE SUCCESSFUL");
						} 
						catch (SQLException e1) 
						{
							JOptionPane.showMessageDialog(null, "UPDATE UNSUCCESSFUL");
							e1.printStackTrace();
						}			
					}
				});
			
			frame.add(l1); 
			frame.add(t1);
			frame.add(l2); 
			frame.add(t2);
			frame.add(l3); 
			frame.add(t3);
			frame.add(confirm);
			frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);	
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
		}	
        
	}
}

class ManageGrades implements ActionListener
{
	PreparedStatement pstat;
	ArrayList<String> courses;
	JComboBox<String> email;
	Connection con;
	public ManageGrades(PreparedStatement pstat, ArrayList<String> courses, JComboBox<String> email, Connection con)
	{
		this.pstat = pstat;
		this.courses = courses;
		this.email = email;
		this.con = con;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		ResultSet rs;
		try 
		{
			JFrame frame = new JFrame();
			frame.setTitle("ManageGrades");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(null);
			frame.setResizable(false);
			frame.setSize(400, 400);
			
			
			
			ArrayList<String> changable = new ArrayList<String>();
			
			pstat.setString(1, (String)email.getSelectedItem());
			rs = pstat.executeQuery();
			
			if(!rs.next())
			{
				JOptionPane.showMessageDialog(null, "DATABASE ERROR");
				return;
			}
			else
			{
			    for(String course : courses)   // your ArrayList
			    {
			        int value = rs.getInt(course);
			        if(value > -2)
			        {
			            changable.add(course);
			        }
			    }
			}
			
			if(changable.isEmpty())
			{
			    JOptionPane.showMessageDialog(null, "NO COURSES TO UPDATE");
			    return;
			}
			
			JComboBox<String> cb = new JComboBox<String>(changable.toArray(new String[0]));
			JLabel l1 = new JLabel("select course to change grade");
			JTextField t1 = new JTextField();
			addLimit(t1, 2);
			JButton jb = new JButton("confirm");
			l1.setBounds(50, 50, 100, 30);
			cb.setBounds(170, 50, 100, 30);
			t1.setBounds(50, 100, 50, 30);
			jb.setBounds(50, 150, 50, 30);			
			
			jb.addActionListener(ee->
			{
				try 
				{
					int grade;
					try
					{
					    grade = Integer.parseInt(t1.getText());
					    if(grade < 0 || grade > 100)
					    {
					        JOptionPane.showMessageDialog(null, "INVALID GRADE: grade = [0,100]");
					        return;
					    }
					}
					catch(NumberFormatException ex)
					{
					    JOptionPane.showMessageDialog(null, "INVALID INPUT");
					    return;
					}
					
					PreparedStatement s = con.prepareStatement("update enrolledStudents set "+(String)cb.getSelectedItem()+" = ? where email = ?");
					s.setInt(1, grade);
					s.setString(2, (String)email.getSelectedItem());
					s.executeUpdate();
				} 
				catch (SQLException e2) 
				{
					e2.printStackTrace();
				}
			});
			
			frame.add(l1);
			frame.add(cb);
			frame.add(t1);
			frame.add(jb);
			frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);	
			
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
		
	}
	public static void addLimit(JTextField textField, int limit) 	//this adds limit to number of characters in email or password
	{
        AbstractDocument doc = (AbstractDocument) textField.getDocument();
        doc.setDocumentFilter(new DocumentFilter2(limit));		//here we override document filter class
    }
	
}

public class ManageStudentRecords  extends SQLConnection
{
	public void view()
	{
		try 
		{
			throw new SQLException();
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
		}
	}
	
	
	public ManageStudentRecords()
	{
		try 
		{
			pstml = con.prepareStatement("select *from users where usertype = 'student' or usertype = 'teacherAssistant'");
			pstat = con.prepareStatement("select *from enrolledStudents where email = ?");
			PreparedStatement p = con.prepareStatement("update enrolledStudents set name = ?, rno = ?, phoneno = ? where email = ?");
			PreparedStatement q = con.prepareStatement("update users set name = ? where email = ?");
			PreparedStatement r = con.prepareStatement("select *from courseCatalogue");
			
			ResultSet rr = r.executeQuery();
			
			ArrayList<String> courses = new ArrayList<String>();
			while(rr.next())
			{
				courses.add(rr.getString("courseCode"));
			}
			
			ResultSet rs = pstml.executeQuery();
			ArrayList<String> arl = new ArrayList<String>();
			
			while(rs.next())
			{
				arl.add(rs.getString("email"));
			}
			
			JFrame frame = new JFrame();
			frame.setTitle("Manage Student Records");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(null);
			frame.setResizable(false);
			frame.setSize(400, 400);
			
			JLabel l1 = new JLabel("Select the student");
			l1.setBounds(20, 20, 200, 50);
			JComboBox<String> email = new JComboBox<String>(arl.toArray(new String[0]));
			email.setBounds(20, 80, 200, 50);
			
			JButton b1 = new JButton("Update Personal Information");
			b1.setBounds(20, 150, 200, 50);
			b1.addActionListener(new UpdatePersonalInformation(email, pstat, p, q));
			
			JButton b2 = new JButton("Update Grades");
			b2.setBounds(20, 220, 200, 50);
			b2.addActionListener(new ManageGrades(pstat, courses, email, con));
			
			frame.add(b2);
	        frame.add(b1);
	        frame.add(l1);
	        frame.add(email);
	        frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);			
			
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
		}
	}
}
