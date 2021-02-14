package app.classes;

import app.exceptions.UserTypeNotFoundException;

public enum UserType {
    GOST, STUDENT, PROFESOR;

    public static UserType of(int redniBroj) throws UserTypeNotFoundException {
        switch (redniBroj){
            case 1:
                return UserType.GOST;
            case 2:
                return UserType.STUDENT;
            case 3:
                return UserType.PROFESOR;
            default:
                throw new UserTypeNotFoundException("User type not found");
        }
    }
}
