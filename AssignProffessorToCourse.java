package jdbc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

class Button1 extends SQLConnection implements ActionListener
{
	JFrame frame;
	JButton b2;
	JComboBox<String> cb;
	public Button1(JFrame frame, JButton b2, JComboBox<String> cb) 
	{
		this.frame = frame;
		this.b2 = b2;
		this.cb = cb;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		try 
		{			
			pstat = con.prepareStatement("select department, timings from courseCatalogue where courseCode = ?");
			pstat.setString(1, (String)cb.getSelectedItem());
			
			ResultSet r = pstat.executeQuery();
			String dep = null;
			String timings = null;
			if(r.next())
			{
				dep = r.getString(1);
				timings = r.getString(2);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "DATABASE ERROR");
			}
			
			ArrayList<String> profs = new ArrayList<String>();
			pstat = con.prepareStatement("select name from proffessors where department = ? and `"+timings+"` = 0");
			pstat.setString(1, dep);
			
			r= pstat.executeQuery();
			while(r.next())
			{
				profs.add(r.getString("name"));
			}
			if(profs.isEmpty())
			{
			    JOptionPane.showMessageDialog(null, "NO PROFFESSORS AVAILABLE");
			    return;
			}
			else
			{
				b2.setVisible(true);
			}
			JComboBox<String> prof = new JComboBox<String>(profs.toArray(new String[0]));
			
			prof.setBounds(220, 120, 150, 30);

			JLabel l2 = new JLabel("select professor:");
			l2.setBounds(50, 200, 150, 30);

			frame.add(l2);
			frame.add(prof);

			
			for(ActionListener al : b2.getActionListeners())
			{
			    b2.removeActionListener(al);
			}
			b2.addActionListener(new Button3(prof, cb, timings));
			
		} 
		catch (SQLException ex) 
		{
			JOptionPane.showMessageDialog(null, "DATABASE ERROR");
			ex.printStackTrace();
		}
		
	}
}

class Button3 extends SQLConnection implements ActionListener 
{
	JComboBox<String> prof, cb;
	String timings;
	Button3(JComboBox<String> prof, JComboBox<String> cb, String timings)
	{
		this.prof = prof;
		this.cb = cb;
		this.timings = timings;
	}
	public void actionPerformed(ActionEvent e) 
	{
		try
		{
			pstat = con.prepareStatement("update proffessors set `"+timings+"` = 1 where name = ?");
			pstat.setString(1, (String)prof.getSelectedItem());
			pstml = con.prepareStatement("update courseCatalogue set proffessor = ? where courseCode = ?");
			pstml.setString(1, (String)prof.getSelectedItem());
			pstml.setString(2, (String)cb.getSelectedItem());
			
			
			if(pstat.executeUpdate()>0 && pstml.executeUpdate()>0)
			{
				JOptionPane.showMessageDialog(null, "SUCCESS");
			}		
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
	}
}

public class AssignProffessorToCourse extends SQLConnection 
{
	public AssignProffessorToCourse() 
	{
		try 
		{
			JFrame frame = new JFrame();
			frame.setTitle("add course");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(null);
			frame.setResizable(false);
			frame.setSize(500, 500);
			
			//in prof table, if prof is free default is 0, else its 1
			
			JLabel l1 = new JLabel("select course: ");
			l1.setBounds(50, 50, 150, 30);
			
			pstml = con.prepareStatement("select * from courseCatalogue");
			ResultSet rs = pstml.executeQuery();
			
			ArrayList<String> courses = new ArrayList<String>();
			
			while(rs.next())
			{
				courses.add(rs.getString("courseCode"));
			}
			
			JComboBox<String> cb = new JComboBox<String>(courses.toArray(new String[0]));
			cb.setBounds(200, 50, 150, 30);
			
			
			JButton b1 = new JButton("course selected");
			b1.setBounds(50, 120, 150, 30);

			JButton b2 = new JButton("confirm");
			b2.setBounds(200, 200, 150, 30);
			
			b1.addActionListener(new Button1(frame, b2, cb));
			
			frame.add(l1);
			frame.add(cb);
			frame.add(b1);
			frame.add(b2);
			b2.setVisible(false);
			
			frame.getContentPane().setBackground(Color.white);  //there is an opaque content pane over the frame so we color that
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
	}
}
