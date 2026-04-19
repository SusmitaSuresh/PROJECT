package jdbc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewSchedule extends SQLConnection 
{

    JFrame frame;
    JTable table;
    DefaultTableModel model;

    public ViewSchedule() 
    {
        frame = new JFrame("Course Registration Status");

        model = new DefaultTableModel();
        table = new JTable(model);

        // Columns
        model.addColumn("Course Code");
        model.addColumn("Timing");
        loadData();

        frame.add(new JScrollPane(table));
        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    public void loadData() 
    {
        try 
        {
            String query = "SELECT courseCode, timings FROM courseCatalogue";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String code = rs.getString("courseCode");
                String timing = rs.getString("timings");
                int status = rs.getInt("status");

                // 🎯 CONDITION
                if (status == -1) {
                    // Registered → show timing
                    model.addRow(new Object[]{code, timing});
                } else if (status == -2) {
                    // Not registered → hide timing
                    model.addRow(new Object[]{code,""});
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ViewSchedule();
    }
}