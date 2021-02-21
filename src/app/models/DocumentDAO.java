package app.models;

import app.classes.Document;
import app.classes.Subject;
import app.classes.User;
import app.settings.Database;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DocumentDAO {
    private static DocumentDAO instance;
    private static Database database;

    private PreparedStatement fetchDocuments;
    private PreparedStatement updateDocument;
    private PreparedStatement deleteDocument;
    private PreparedStatement deleteDocumentsFromSubject;

    private DocumentDAO() throws SQLException {
        database = Database.getInstance();
        fetchDocuments = database.addPreparedStatement(
                "select * from document d\n" +
                "inner join user u on d.author = u.userID\n" +
                "where subject = ?;"
        );
        updateDocument = database.addPreparedStatement(
                "update document \n" +
                "set\n" +
                "name = ?,\n" +
                "subject = ?\n" +
                "where documentID = ?;"
        );
        deleteDocument = database.addPreparedStatement("delete from document where documentID = ?;");
        deleteDocumentsFromSubject = database.addPreparedStatement("delete from document where subject = ?;");

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

    public List<Document> fetchDocumentList(Subject subject) {
        try {
            fetchDocuments.setInt(1, subject.getId());
            ResultSet resultSet = fetchDocuments.executeQuery();
            List<Document> documentList = new ArrayList<>();
            while (resultSet.next()) {
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

    public Document update(Document document) {
        try {
            updateDocument.setString(1, document.getName());
            updateDocument.setInt(2, document.getSubjectID());
            updateDocument.setInt(3, document.getId());
            updateDocument.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void delete(Document document) {
        try {
            deleteDocument.setInt(1, document.getId());
            deleteFromServer(document);
            deleteDocument.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
    public void deleteFromServer(Document document){
        try {

            URL url = new URL("http://localhost/fileupload/remove.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            Map<String, String> args = new HashMap<>();
            args.put("documentID",""+document.getId());
            StringJoiner stringJoiner = new StringJoiner("&");
            for(Map.Entry<String,String> entry : args.entrySet())
                stringJoiner.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
            byte[] out = stringJoiner.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;
            httpURLConnection.setFixedLengthStreamingMode(length);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpURLConnection.connect();
            try(OutputStream os = httpURLConnection.getOutputStream()) {
                os.write(out);
            }
            httpURLConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleteFromSubject(Subject subject){
        // TO-DO
    }
}

