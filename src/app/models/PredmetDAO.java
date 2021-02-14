package app.models;

import app.classes.Predmet;
import app.classes.User;
import app.settings.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PredmetDAO {

    private static PredmetDAO instance;
    private static Database database;

    private PreparedStatement dajSvePredmete;

    private PredmetDAO() throws SQLException {
        database = Database.getInstance();
        dajSvePredmete = database.addPreparedStatement("select * from predmet");
    }

    public static PredmetDAO getInstance() throws SQLException {
        if (instance == null) instance = new PredmetDAO();
        return instance;
    }

    public static void removeInstance() throws SQLException {
        if (instance == null) return;
        Database.removeInstance();
        instance = null;
    }

    public Predmet regenerisiPodatke(ResultSet resultSet) {

        try {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int profID = resultSet.getInt(3);
            User professor = UserDAO.getInstance().dajKorisnikaPrekoID(profID);
            UserDAO.removeInstance();
            return new Predmet(id, name, professor);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<Predmet> dajListuSvihPredmeta() {
        try {
            ResultSet resultSet = dajSvePredmete.executeQuery();
            List<Predmet> predmetList = new ArrayList<>();
            while (!resultSet.isClosed() && resultSet.next()) {
                Predmet predmet = regenerisiPodatke(resultSet);
                if (predmet != null) {
                    predmetList.add(predmet);
                }
            }
            return predmetList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
