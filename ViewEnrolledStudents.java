package jdbc;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

	public class ViewEnrolledStudents extends SQLConnection 
	{
	    private JScrollPane scrollPane; // to update table dynamically

	    public ViewEnrolledStudents(String email) 
	    {
	        try 
	        {
	            // Get professor name from email
	            PreparedStatement p = con.prepareStatement("select name from users where email = ?");
	            p.setString(1, email);
	            ResultSet r = p.executeQuery();

	            String name;
	            if(r.next())
	            {
	                name = r.getString(1);
	            }
	            else
	            {
	                JOptionPane.showMessageDialog(null, "DATABASE ERROR: User not found");
	                return; // stop execution
	            }

	            // Get courses taught by professor
	            PreparedStatement pstatCourses = con.prepareStatement(
	                "select * from courseCatalogue where proffessor = ?");
	            pstatCourses.setString(1, name);

	            r = pstatCourses.executeQuery();
	            ArrayList<String> courses = new ArrayList<>();

	            while(r.next())
	            {
	                courses.add(r.getString("courseCode"));
	            }

	            if(courses.isEmpty())
	            {
	                JOptionPane.showMessageDialog(null, "No courses found");
	                return;
	            }

	            // Table column names
	            String[] columns = {"email","name","rno","phoneno","grade"};

	           
	            PreparedStatement pstat = con.prepareStatement(
	                "select email, name, rno, phoneno, grade from enrolledStudents where course = ?",
	                ResultSet.TYPE_SCROLL_INSENSITIVE,
	                ResultSet.CONCUR_READ_ONLY
	            );

	        
	            JFrame frame = new JFrame();
	            frame.setTitle("View Enrolled Students");
	            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            frame.setLayout(null);
	            frame.setResizable(false);
	            frame.setSize(520, 500);

	            JComboBox<String> courseBox = new JComboBox<>(courses.toArray(new String[0]));
	            courseBox.setBounds(50, 10, 200, 30);

	            JButton btn = new JButton("Confirm");
	            btn.setBounds(270, 10, 100, 30);

	            // Button action
	            btn.addActionListener(e -> 
	            {
	                try 
	                {
	                    pstat.setString(1, (String) courseBox.getSelectedItem());
	                    ResultSet rs = pstat.executeQuery();

	                    rs.last();
	                    int rows = rs.getRow();
	                    int cols = rs.getMetaData().getColumnCount();
	                    rs.beforeFirst();

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

	                    JTable table = new JTable(data, columns);

	                    // Remove old table if exists
	                    if(scrollPane != null)
	                    {
	                        frame.remove(scrollPane);
	                    }

	                    scrollPane = new JScrollPane(table);
	                    scrollPane.setBounds(50, 60, 400, 350);

	                    frame.add(scrollPane);

	                    // Refresh UI
	                    frame.revalidate();
	                    frame.repaint();
	                }
	                catch(SQLException ex)
	                {
	                    ex.printStackTrace();
	                }
	            });

	        
	            frame.add(courseBox);
	            frame.add(btn);

	            frame.getContentPane().setBackground(Color.white);
	            frame.setLocationRelativeTo(null);
	            frame.setVisible(true);
	        } 
	        catch (SQLException ex) 
	        {
	            ex.printStackTrace();
	        }
	    }
	}

