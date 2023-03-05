package es.project.errors;

public class CurrentUserNotFoundException extends BadRequestAlertException {
    private static final long serialVersionUID = 1L;

    public CurrentUserNotFoundException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Current user login not found", "account", "currentUserNotFound");
    }
}
