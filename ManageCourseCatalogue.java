package jdbc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import gui.DocumentFilter2;


class SubmitButton implements ActionListener
{
	JTextField t1, t2, t5;
	JComboBox<Integer> t4;
	JComboBox<String> t6, t7;
	PreparedStatement pstml;
	PreparedStatement pstat;
	PreparedStatement q;
	Connection con;
	public SubmitButton(JTextField t1, JTextField t2, JComboBox<Integer> t4, JTextField t5, JComboBox<String> t6, JComboBox<String> t7, PreparedStatement pstml, PreparedStatement pstat, Connection con)
	{
		this.t1 = t1;
		this.t2 = t2;
		this.t4 = t4;
		this.t5 = t5;
		this.t6 = t6;
		this.t7 = t7;
		this.pstml = pstml;
		this.pstat = pstat;
		this.con = con;
	}
	public void actionPerformed(ActionEvent e) 
	{
		try 
		{
			try 
			{
				Statement s = con.createStatement();
				s.execute("alter table enrolledStudents add column `"+ t1.getText()+"` int default -2");
			} 
			catch (SQLException ex) 
			{
				ex.printStackTrace();
			}
			pstat.setString(1, t1.getText());
			ResultSet rs = pstat.executeQuery();
			if(!rs.next())
			{
				System.out.println("error");
			}			
			else
			{
				if(rs.getInt(1)==0)
				{
					if((t1.getText()).equals(""))
					{
						return;
					}
					pstml.setString(1, t1.getText());
					pstml.setString(2, t2.getText());
					pstml.setInt(3, (int)t4.getSelectedItem());
					pstml.setString(4, t5.getText());
					pstml.setString(5, (String)t6.getSelectedItem());
					pstml.setString(6, (String)t7.getSelectedItem());
					pstml.executeUpdate();
				}
				else
				{
					t1.setText("");
					t2.setText("");
					t5.setText("");
				}
			}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}

class ConfirmButton3 implements ActionListener
{
	PreparedStatement pstat;
	JComboBox<String> course;
	Connection con;
	public ConfirmButton3(PreparedStatement pstat, JComboBox<String> course, Connection con)
	{
		this.con = con;
		this.pstat = pstat;
		this.course = course;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		try 
		{
			pstat.setString(1, (String)course.getSelectedItem());
			pstat.executeUpdate();
			try 
			{
				Statement s = con.createStatement();
				s.execute("alter table enrolledStudents drop `"+ (String)course.getSelectedItem()+"`");
			} 
			catch (SQLException ex) 
			{
				ex.printStackTrace();
			}
		} 
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}


public class ManageCourseCatalogue extends SQLConnection
{
	public void view()
	{
		try 
		{
			String[] columns = {"courseCode", "title", "proffessor", "credits", "prerequisites", "timings"};
			ResultSet rs;
			pstat = con.prepareStatement("select * from courseCatalogue", 
				    ResultSet.TYPE_SCROLL_INSENSITIVE, 
				    ResultSet.CONCUR_READ_ONLY);
			rs = pstat.executeQuery();
			rs.last();
	        int rows = rs.getRow();	//it returns the index of last row
	        int cols = rs.getMetaData().getColumnCount();
	        rs.beforeFirst(); 		//goes back to initial position
	        
	        Object[][] data = new Object[rows][cols];
	        int i = 0;
	        while (rs.next()) 
	        {
	        	for (int j = 0; j < cols; j++) 
	            {
	            	data[i][j] = rs.getObject(j + 1);
	            }
	            i++;
	        }
	        
	    	JFrame frame = new JFrame();
			frame.setTitle("view course");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(null);
			frame.setResizable(false);
			frame.setSize(500, 500);
			
				
			JTable table = new JTable(data, columns);
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(50, 50, 400, 300);
			
			frame.add(scrollPane);
			frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
		}
	}
	public void add()
	{
		try 
		{		
			pstml = con.prepareStatement("insert into courseCatalogue (courseCode, title, credits, prerequisites, timings, department) values(?,?,?,?,?,?)");
			pstat = con.prepareStatement("select count(*) from courseCatalogue where courseCode = ?");
			PreparedStatement p = con.prepareStatement("select * from users where usertype = 'proffessor'");
			
			ResultSet rs = p.executeQuery();
			ArrayList<String> names = new ArrayList<String>();
			while(rs.next())
			{
				names.add(rs.getString("name"));
			}
			
			
			JFrame frame = new JFrame();
			frame.setTitle("add course");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(null);
			frame.setResizable(false);
			frame.setSize(500, 500);
			
			JLabel label1 = new JLabel("enter course code:");
	        label1.setBounds(50, 50, 150, 30);
	        JTextField t1 = new JTextField();
	        t1.setBounds(210, 50, 150, 30);
	        addLimit(t1, 5);
			
	        JLabel label2 = new JLabel("Enter course name:");
	        label2.setBounds(50, 100, 150, 30);
	        JTextField t2 = new JTextField();
	        t2.setBounds(210, 100, 150, 30);
	        addLimit(t2, 100);
	        
//	    	JLabel label3 = new JLabel("select proffessor name:");
//	        label3.setBounds(50, 150, 150, 30);
//	        String c[] = names.toArray(new String[0]);
//	        JComboBox<String> t3 = new JComboBox<String>(c);
//	        t3.setBounds(210, 150, 150, 30);
	        
	        JLabel label4 = new JLabel("select amount of credits:");
	        label4.setBounds(50, 150, 150, 30);
	        JComboBox<Integer> t4 = new JComboBox<Integer>();
	        t4.addItem(2);
	        t4.addItem(4);
	        t4.setBounds(210, 150, 150, 30);
	        
	        JLabel label5 = new JLabel("enter prerequisites:");
	        label5.setBounds(50, 200, 150, 30);
	        JTextField t5 = new JTextField();
	        t5.setBounds(210, 200, 150, 30);
	        
	        String[] timings = {"8:30-9:20", "9:30-10:20", "10:30-11:20", "11:30-12:20", "2:00-2:50", "3:00-3:50", "4:00-4:50", "5:00-5:50"};
	        
	        JLabel label6 = new JLabel("enter timings:");
	        label6.setBounds(50, 250, 150, 30);
	        JComboBox<String> t6 = new JComboBox<String>(timings);
	        t6.setBounds(210, 250, 150, 30);
	   
	        JLabel label7 = new JLabel("enter department:");
	        label7.setBounds(50, 300, 150, 30);
	        String dept[] = {"ai", "cse", "chem", "mech", "ece", "ee", "math", "phy", "hss", "civil"};
	        JComboBox<String> t7 = new JComboBox<String>(dept);
	        t7.setBounds(210, 300, 150, 30);
	        
	        JButton submit = new JButton("submit");
	        submit.setBounds(180, 350, 80, 30);
	        submit.addActionListener(new SubmitButton(t1, t2, t4, t5, t6, t7, pstml, pstat, con));
	        
			frame.add(submit);
			frame.add(label7);
			frame.add(t7);
			frame.add(label1);
			frame.add(label2);
			frame.add(label4);
			frame.add(label5);
			frame.add(label6);
			frame.add(t1);
			frame.add(t2);
			frame.add(t4);
			frame.add(t5);
			frame.add(t6);
			frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void delete()
	{
		try 
		{
			pstat = con.prepareStatement("delete from courseCatalogue where courseCode = ?");
			pstml = con.prepareStatement("select * from courseCatalogue");
			ResultSet rs = pstml.executeQuery();
			
			ArrayList<String> courses = new ArrayList<String>();
			
			while(rs.next())
			{
				courses.add(rs.getString("courseCode"));
			}
			
			JFrame frame = new JFrame();
			frame.setTitle("delete course");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(null);
			frame.setResizable(false);
			frame.setSize(500, 300);
			
			JLabel label1 = new JLabel("enter course code:");
	        label1.setBounds(50, 50, 150, 30);
	        String c[] = courses.toArray(new String[0]);
	        JComboBox<String> course = new JComboBox<String>(c);
	        course.setBounds(220,50,150,30);	        
	        
	        JButton confirm = new JButton("confirm");
			confirm.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
			confirm.setBounds(50, 100, 150, 50);	
			confirm.addActionListener(new ConfirmButton3(pstat, course, con));
			
			frame.add(label1);
			frame.add(confirm);
			frame.add(course);
			frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
		catch (SQLException ex) 
		{
			ex.printStackTrace();
		}
	}
	
	public static void addLimit(JTextField textField, int limit) 	//this adds limit to number of characters in email or password
	{
        AbstractDocument doc = (AbstractDocument) textField.getDocument();
        doc.setDocumentFilter(new DocumentFilter2(limit));		//here we override document filter class
    }
	public static void main(String[] args) {
		(new ManageCourseCatalogue()).add();
	}
}
