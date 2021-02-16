package app.classes;

import app.exceptions.UserTypeNotFoundException;

public enum UserType {
    GUEST, STUDENT, PROFESSOR;

    public static UserType of(int userTypeID) throws UserTypeNotFoundException {
        return switch (userTypeID) {
            case 1 -> UserType.GUEST;
            case 2 -> UserType.STUDENT;
            case 3 -> UserType.PROFESSOR;
            default -> throw new UserTypeNotFoundException("User type not found");
        };
    }
}
