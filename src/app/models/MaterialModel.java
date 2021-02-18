package app.models;

import app.classes.Document;
import app.classes.Subject;
import app.classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

public class MaterialModel {

    private final User author;
    private final Subject selectedSubject;
    private final DocumentDAO documentDAO;
    private List<Document> documentList;

    public MaterialModel(User author, Subject selectedSubject, DocumentDAO documentDAO) {
        this.author = author;
        this.selectedSubject = selectedSubject;
        this.documentDAO = documentDAO;
    }

    public User getAuthor() {
        return author;
    }

    public Subject getSelectedSubject() {
        return selectedSubject;
    }

    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    public ObservableList<Document> search(String searchParam) {
        ObservableList<Document> filteredDocuments = FXCollections.observableArrayList();
        documentList.forEach(document -> {
            if (document.isSubstring(searchParam)) {
                filteredDocuments.add(document);
            }
        });
        return filteredDocuments;
    }

    public ObservableList<Document> fetchDocuments(){
        this.documentList = documentDAO.fetchDocumentList(selectedSubject);
        return FXCollections.observableArrayList(this.documentList);
    }
    public int getAuthorID(){
        return author.getId();
    }
    public int getSelectedSubjectID(){
        return selectedSubject.getId();
    }
    public void delete(Document document){
        documentDAO.delete(document);
    }

}
