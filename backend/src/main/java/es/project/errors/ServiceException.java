package es.project.errors;


public abstract class ServiceException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String errorKey = ErrorConstants.ERR_VALIDATION;

    public ServiceException(String message, String errorKey) {
        super(message);
        this.errorKey = errorKey;
    }

    public ServiceException(String message) {
        super(message);
    }

    public abstract String getEntity();

    public String getErrorKey() {
        return errorKey;
    }
}
