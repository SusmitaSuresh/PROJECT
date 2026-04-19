package jdbc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;


class Confirm1Button implements ActionListener
{
	PreparedStatement pstml;
	ButtonGroup bg;
	JTable table;
	public Confirm1Button(PreparedStatement pstml, ButtonGroup bg, JTable table)
	{
		this.pstml = pstml;
		this.bg = bg;
		this.table = table;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		try 
		{
			int row = table.getSelectedRow();			
			
			pstml.setString(1, bg.getSelection().getActionCommand());
			pstml.setString(2, (String)table.getValueAt(row, 1));
			pstml.executeUpdate();
			
		} 
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}


public class HandleComplaints extends SQLConnection
{
	public HandleComplaints()
	{		
		try 
		{
			pstml = con.prepareStatement("update complaints SET status = ? WHERE complaint = ?");
			
			String[] columns = {"email", "complaint", "status"};
			ResultSet rs;
			pstat = con.prepareStatement("select * from complaints", 
				    ResultSet.TYPE_SCROLL_INSENSITIVE, 
				    ResultSet.CONCUR_READ_ONLY);
			rs = pstat.executeQuery();
			rs.last();
	        int rows = rs.getRow();	//it returns the index of last row
	        int cols = rs.getMetaData().getColumnCount();
	        rs.beforeFirst(); 		//goes back to initial position
	        
	        Object[][] data = new Object[rows][cols];
	        int i = 0;
	        System.out.println("Attempting to loop through " + rows + " rows...");
	        while (rs.next()) 
	        {
	        	System.out.println(i);
	        	data[i][0] = rs.getObject(1);
	            System.out.println("Reading row " + i + ": " + data[i][0]); // Check if data is actually being read
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
			frame.setSize(650, 500);
			
				
			JLabel l = new JLabel("Select required complaint to update (it will become blue)");
			l.setBounds(50, 50, 350, 30);
			JTable table = new JTable(data, columns);
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(50, 100, 400, 300);
			
			ButtonGroup bg = new ButtonGroup();
			JRadioButton r1 = new JRadioButton("pending");
			r1.setBounds(480, 100, 100, 30);
			r1.setActionCommand("pending");
			JRadioButton r2 = new JRadioButton("resolved");
			r2.setBounds(480, 150, 100, 30);
			r2.setActionCommand("resolved");
			bg.add(r2);
			bg.add(r1);
			
			JButton confirm = new JButton("comfirm");
			confirm.setBounds(480, 200, 100, 30);
			confirm.addActionListener(new Confirm1Button(pstml, bg, table));
			
			frame.add(l);
			frame.add(confirm);			
			frame.add(r2);
			frame.add(r1);
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
	
	public void updateStatus(String status)
	{
		try 
		{
			pstml = con.prepareStatement("");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}
