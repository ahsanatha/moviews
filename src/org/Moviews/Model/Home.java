package org.Moviews.Model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.Moviews.Database.Database;

public class Home extends Model {

    public Home() {
    }

    @Override
    public ArrayList<Movies> findData(String key) {
        Movies m = new Movies();
        ArrayList<Movies> arm = new ArrayList();
        try {
            Database db = new Database();
            db.Connect();
            String query = "SELECT id_mov FROM `movies` WHERE `title` LIKE '%" + key + "%' or `director` LIKE '%" + key + "%' or `studio` LIKE '%" + key + "%' GROUP BY id_mov";
            System.out.println(query);
            db.setRs(query);
            ResultSet rs = db.getRs();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                arm.add(m.findData(rs.getString("id_mov")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arm;
    }

    public DefaultListModel makeDLM(String fromSearchBox) {
        ArrayList<Movies> arm = null;
        arm = findData(fromSearchBox);
        DefaultListModel dlm = new DefaultListModel();
        for (Movies x : arm) {
            dlm.addElement(x.getTitle());
        }
        return dlm;
    }
}
