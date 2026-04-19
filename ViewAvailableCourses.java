package jdbc;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ViewAvailableCourses extends SQLConnection
{
	public ViewAvailableCourses()
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
}
