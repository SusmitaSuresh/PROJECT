package jdbc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class SubmitComplaints extends JFrame implements ActionListener
{
	PreparedStatement pstml, pstat;
	public static void main(String[] args) 
	{
		SubmitComplaints sc=new SubmitComplaints();
		sc.setVisible(true);
		sc.setSize(500,600);
		sc.setTitle("complaints");
	}
	JTextField tcomplaint;
	JButton bsubmit;
	public SubmitComplaints()
	{
		setLayout(new FlowLayout());
		tcomplaint=new JTextField(350);
		bsubmit=new JButton("SUBMIT");
		add(tcomplaint);
		add(bsubmit);
		bsubmit.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			SQLConnection sc=new SQLConnection();
			pstml = sc.con.prepareStatement("insert into complaints(complaint, status) values(?, 'pending')");
			pstat = sc.con.prepareStatement("select count(*) from complaints where complaint = ?");
			pstml.setString(1, tcomplaint.getText());
			pstml.executeUpdate();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
	}
}
