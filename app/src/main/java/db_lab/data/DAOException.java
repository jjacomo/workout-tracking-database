package db_lab.data;

public final class DAOException extends RuntimeException {
    public DAOException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public DAOException(String message) {
        super(message);
    }
}
