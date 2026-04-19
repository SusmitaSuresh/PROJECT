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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import gui.DocumentFilter2;
class SubmitButton2 implements ActionListener
{
	JTextField t1,t4,t5;
	JComboBox<Integer> t3;
	JComboBox<String> t2, t6;
	PreparedStatement pstml;
	PreparedStatement pstat;
	public SubmitButton2(JTextField t1, JComboBox<Integer> t3, JTextField t4, JTextField t5, JComboBox<String> t6, PreparedStatement pstml, PreparedStatement pstat)
	{
		this.t1 = t1;
		this.t3 = t3;
		this.t4 = t4;
		this.t5 = t5;
		this.t6 = t6;
		this.pstml = pstml;
		this.pstat = pstat;
	}
	public void actionPerformed(ActionEvent e) 
	{
		try 
		{
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
					pstml.setString(2, t4.getText());
					pstml.setString(2, t5.getText());
					pstml.setString(3, (String)t2.getSelectedItem());
					pstml.setInt(4, (int)t3.getSelectedItem());
					pstml.setString(6, (String)t6.getSelectedItem());
					pstml.executeUpdate();
				}
				else
				{
					t1.setText("");
					t4.setText("");
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

public class ManageCourses extends SQLConnection
{
	public void view()
	{
		try 
		{
			String[] columns = {"syllabus","credits","enrollment limits", "prerequisites", "office timings"};	//wrong
			ResultSet rs;
			pstat = con.prepareStatement("select * from email", 
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
			frame.setTitle("add course");
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
	public void update()
	{
		try 
		{		
			pstml = con.prepareStatement("UPDATE email SET col1=?, col2=?, col3=?, col4=?, col5=? WHERE col6=?\"");	//wrong
			pstat = con.prepareStatement("select count(*) from email where courseCode = ?");
			PreparedStatement p = con.prepareStatement("select * from users where usertype = 'professor'");
			ResultSet rs = p.executeQuery();
			ArrayList<String> names = new ArrayList<String>();
			while(rs.next())
			{
				names.add(rs.getString("name"));
			}
			JFrame frame = new JFrame();
			frame.setTitle("update course");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(null);
			frame.setResizable(false);
			frame.setSize(500, 500);
			JLabel label1 = new JLabel("enter syllabus:");
	        label1.setBounds(50, 100, 150, 30);
	        JTextField t1 = new JTextField();
	        t1.setBounds(210, 100, 150, 30);
	      
	        JLabel label3 = new JLabel("select credits:");
	        label3.setBounds(50, 150, 150, 30);
	        JComboBox<Integer> t3 = new JComboBox<Integer>();
	        t3.addItem(2);
	        t3.addItem(4);
	        t3.setBounds(210, 150, 150, 30);
	        
	        JLabel label4 = new JLabel("enter prerequisites:");
	        label4.setBounds(50, 200, 150, 30);
	        JTextField t4 = new JTextField();
	        
	        t4.setBounds(210, 200, 150, 30);
	        
	        JLabel label5 = new JLabel("select enrollment limits:");
	        label5.setBounds(50, 250, 150, 30);
	        JTextField t5 = new JTextField();
	        t5.setBounds(210, 250, 150, 30);
	        
	        String[] timings = {"8:30-9:20", "9:30-10:20", "10:30-11:20", "11:30-12:20", "2:00-2:50", "3:00-3:50", "4:00-4:50", "5:00-5:50"};
	        
	        JLabel label6 = new JLabel("enter timings:");
	        label6.setBounds(50, 300, 150, 30);
	        JComboBox<String> t6 = new JComboBox<String>(timings);
	        t6.setBounds(210, 300, 150, 30);
	   
	        JButton submit = new JButton("submit");
	        submit.setBounds(180, 380, 80, 30);
	        submit.addActionListener(new SubmitButton2(t1,t3, t4, t5, t6, pstml, pstat));
	        
			frame.add(submit);
			frame.add(label1);
			frame.add(label3);
			frame.add(label4);
			frame.add(label5);
			frame.add(label6);
			frame.add(t1);
			frame.add(t3);
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
	public static void main(String[] args) {
		(new ManageCourses()).update();
	}
}