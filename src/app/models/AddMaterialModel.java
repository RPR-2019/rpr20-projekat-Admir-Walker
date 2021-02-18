package app.models;

public class AddMaterialModel {
    private int authorID;
    private int subjectID;

    public AddMaterialModel(int authorID, int subjectID) {
        this.authorID = authorID;
        this.subjectID = subjectID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }
}
