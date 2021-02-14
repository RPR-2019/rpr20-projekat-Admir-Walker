package app.classes;

import app.exceptions.UserTypeNotFoundException;

public enum UserType {
    GUEST, STUDENT, PROFESOR;

    public static UserType of(int redniBroj) throws UserTypeNotFoundException {
        return switch (redniBroj) {
            case 1 -> UserType.GUEST;
            case 2 -> UserType.STUDENT;
            case 3 -> UserType.PROFESOR;
            default -> throw new UserTypeNotFoundException("User type not found");
        };
    }
}
