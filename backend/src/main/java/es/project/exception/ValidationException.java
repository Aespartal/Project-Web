package es.project.exception;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class ValidationException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public ValidationException(String message, String errorKey) {
        super(message, errorKey);
    }

    public ValidationException(String message) {
        super(message);
    }

    @Override
    public String getEntity() {
        return null;
    }
}
