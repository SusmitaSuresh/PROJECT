package jdbc;

import java.sql.*;

public class SQLConnection 
{
	public static Connection con = null;
	public PreparedStatement pstat = null;
	public PreparedStatement pstml = null;
	Statement s = null;
	static Statement st = null;
	public SQLConnection() 
	{
		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/mydb";
		String username = "root";
		String password = "#creepy@pasta503";
		
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
		}
		catch(ClassNotFoundException ex)
		{
			System.out.println("issue");
			ex.printStackTrace();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void closeConnection()
	{
		try
		{
			if(pstml!=null)
				{
					pstml.close();
					pstml = null;
				}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		try
		{
			if(pstat!=null)
				{
					pstat.close();
					pstat = null;
				}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		try
		{
			if(s!=null)
				{
					s.close();
					s = null;
				}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		try
		{
			if(con!=null)
			{
				con.close();
				con = null;
			}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}
