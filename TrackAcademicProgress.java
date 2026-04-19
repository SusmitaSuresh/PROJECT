package jdbc;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.sql.*;
import javax.swing.*;
public class TrackAcademicProgress extends SQLConnection
{
    JFrame frame;
    JTable table;
    String email;
    public TrackAcademicProgress(String email) 
    {
		this.email = email;
	}
	public void view()
    {
        try
        {
        	loadTable();
        	JScrollPane scrollPane = new JScrollPane(table);
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());
			frame.add(scrollPane, BorderLayout.CENTER);
            frame.setResizable(false);
            frame.setSize(500, 500);
            frame.setTitle("Track Academic Progress");
            frame.getContentPane().setBackground(Color.white);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void loadTable()
    {
        try
        {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM enrolledStudents where email = ?",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            rs.last();
            int rows = rs.getRow();
            int cols = rs.getMetaData().getColumnCount();
            rs.beforeFirst();
            Object[][] data = new Object[rows][cols];
            String[] colNames = new String[cols];
            for (int i = 0; i < cols; i++)
            {
                colNames[i] = rs.getMetaData().getColumnName(i + 1);
            }
            int i = 0;
            while (rs.next())
            {
            	    for (int j = 0; j < cols; j++)
            	    {
            	        Object value = rs.getObject(j + 1); // sql starts its resultset from 1

            	        // Apply logic ONLY to the grade column
            	        if (rs.getMetaData().getColumnName(j + 1).equalsIgnoreCase("course_code")) 
            	        {
            	            int v = rs.getInt(j + 1);

            	            if (v == -2) {
            	                data[i][j] = "";  // not registered
            	            }
            	            else if (v == -1) {
            	                data[i][j] = "Pending";  // registered but not graded
            	            }
            	            else {
            	                data[i][j] = v;  // actual grade
            	            }
            	        }
            	        else {
            	            data[i][j] = value; // normal columns
            	        }
            	    }
            	    i++;
            }
            table = new JTable(data, colNames);
            JScrollPane scrollPane = new JScrollPane(table);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}