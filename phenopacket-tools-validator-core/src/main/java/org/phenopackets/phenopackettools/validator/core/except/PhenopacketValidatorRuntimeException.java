package org.phenopackets.phenopackettools.validator.core.except;

import org.phenopackets.phenopackettools.core.PhenopacketToolsRuntimeException;

public class PhenopacketValidatorRuntimeException extends PhenopacketToolsRuntimeException {

    public PhenopacketValidatorRuntimeException() {
        super();
    }

    public PhenopacketValidatorRuntimeException(String message) {
        super(message);
    }

    public PhenopacketValidatorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhenopacketValidatorRuntimeException(Throwable cause) {
        super(cause);
    }

    protected PhenopacketValidatorRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
