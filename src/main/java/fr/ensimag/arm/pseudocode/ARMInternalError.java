package fr.ensimag.arm.pseudocode;

public class ARMInternalError extends RuntimeException {
    public ARMInternalError(String message, Throwable cause) {
        super(message, cause);
    }

    public ARMInternalError(String message) {
        super(message);
    }

    private static final long serialVersionUID = 3929345355905773361L;

}
