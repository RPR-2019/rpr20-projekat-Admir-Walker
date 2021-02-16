package app.models;

import app.classes.Document;
import app.classes.Subject;
import app.classes.User;
import app.settings.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {
    private static DocumentDAO instance;
    private static Database database;

    private PreparedStatement fetchDocuments;

    private DocumentDAO() throws SQLException {
        database = Database.getInstance();
        fetchDocuments = database.addPreparedStatement(
                "select * from document d\n" +
                "inner join user u on d.author = u.userID\n" +
                "where subject = ?;"
        );
    }

    public static DocumentDAO getInstance() throws SQLException {
        if (instance == null) instance = new DocumentDAO();
        return instance;
    }

    public static void removeInstance() throws SQLException {
        if (instance == null) return;
        Database.removeInstance();
        instance = null;
    }
    public List<Document> fetchDocumentList(Subject subject){
        try {
            fetchDocuments.setInt(1, subject.getId());
            ResultSet resultSet = fetchDocuments.executeQuery();
            List<Document> documentList = new ArrayList<>();
            while (resultSet.next()){
                int documentID = resultSet.getInt(1);
                String documentName = resultSet.getString(2);
                String documentPath = resultSet.getString(3);
                String documentUploadDate = resultSet.getString(4);
                boolean documentIsDownloadable = resultSet.getInt(7) == 1;
                int size = resultSet.getInt(8);
                String type = resultSet.getString(9);
                int userID = resultSet.getInt(10);
                User author = UserDAO.getInstance().fetchUserViaID(userID);
                documentList.add(new Document(documentID, documentName, documentPath, documentUploadDate, subject, author, documentIsDownloadable, size, type));
            }
            return documentList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}

