package jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;


public class ManageAccount extends SQLConnection
{
	public void SignUpAnAccount(String email, char[] password, String user, String rollno, String phoneno, String name, String expertise, String department, String proftoassist)
	{
		try 
		{
			s = con.createStatement();
			pstml = con.prepareStatement("insert into users values(?,?,?,?)");
			pstat = con.prepareStatement("select count(*) from users where email = ?");
			pstat.setString(1, email);
			ResultSet rs = pstat.executeQuery();	//this query returns the number of times email exists	
			if(!rs.next())
			{
				JOptionPane.showMessageDialog(null, "ERROR");
			}
			else
			{
				if(rs.getInt(1) == 0)
				{
					pstml.setString(1, email);
					pstml.setString(2, String.valueOf(password));
					pstml.setString(3, user);
					pstml.setString(4, name);
					if(user.equals("student"))
					{
						if(!emailExistsOrNot(email))
						{
							pstml.executeUpdate();
							s.executeUpdate("insert into enrolledStudents (email, name, rno, phoneno, cgpa) values('"+email+"','"+name+"','"+rollno+"','"+phoneno+"', 'na')" );
							JOptionPane.showMessageDialog(null, "SUCCESS, PLEASE LOG-IN NOW");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "THIS STUDENT ALREADY EXISTS");
						}
					}
					else if(user.equals("proffessor"))
					{
						if(!tableExists(email))
						{
							pstml.executeUpdate();
							s.executeUpdate("insert into proffessors (name, department, email, phoneno, expertise) values('"+name+"','"+department+"','"+email+"','"+phoneno+"','"+expertise+"')");
							JOptionPane.showMessageDialog(null, "SUCCESS, PLEASE LOG-IN NOW");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "THIS PROFFESSOR ALREADY EXISTS PLEASE LOG-IN");
						}
					}	
					else if(user.equals("teacherAssistant"))
					{
						if(!emailExistsOrNot(email))
						{
							pstml.executeUpdate();
							s.executeUpdate("insert into enrolledStudents (email, name, rno, phoneno, cgpa) values('"+email+"','"+name+"','"+rollno+"','"+phoneno+"', 'na')" );
							s.executeUpdate("insert into teacherAssistants (email, name, rno, phoneno, cgpa, proftoassist) values('"+email+"','"+name+"','"+rollno+"','"+phoneno+"', 'na','"+proftoassist+"')" );
							JOptionPane.showMessageDialog(null, "SUCCESS, PLEASE LOG-IN NOW");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "THIS TEACHER ASSISTANT ALREADY EXISTS");
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "EMAIL ALREADY EXISTS");
				}
			} 
		}
		catch (SQLException ex) 
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "DATABASE ERROR");
		}
	}
	
	public boolean tableExists(String tableName) {
	    try 
	    {
	        DatabaseMetaData dbm = con.getMetaData();
	        // The arguments are: catalog, schemaPattern, tableNamePattern, types
	        ResultSet tables = dbm.getTables(null, null, tableName, null);
	        return tables.next(); // Returns true if the table was found
	    } 
	    catch (SQLException ex) 
	    {
	        ex.printStackTrace();
	        return false;
	    }
	}
	
	public boolean checkPassword(String email, char[] password)
	{
		try
		{
			pstml = con.prepareStatement("select psword from users where email=?");
			pstml.setString(1,email);
			ResultSet rs = pstml.executeQuery();
			if(!rs.next())
			{
				return false;
			}
			else
			{
				if((String.valueOf(password).equals(rs.getString("psword"))))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
			
		catch(SQLException ex)
		{
			ex.printStackTrace();			
			return false;
		}
	}
	
	public static boolean emailExistsOrNot(String email)
	{
		try 
		{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select count(*) from users where email='"+email+"'");
			if(!rs.next())
			{
				JOptionPane.showMessageDialog(null, "ERROR");
				return false;
			}
			else
			{
				if(rs.getInt(1) == 0)
				{
					return false;
				}
				else 
				{
					return true;
				}
			}
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null, "DATABASE ERROR");
			e.printStackTrace();
			return false;
		}
		
	}
	public void deleteAnAccount(String email, char[] password)
	{
		try
		{
			s = con.createStatement();
			if(checkPassword(email, password))
			{
				s.execute("delete from users where email="+email);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "WRONG PASSWORD, TRY AGAIN");
			}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}
