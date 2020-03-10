package bo.com.ahosoft.visit.web.rest.errors.my;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BadRequestException extends AbstractThrowableProblem {

    private final String errorKey;

    public BadRequestException(String detail) {
        this(null, null, detail);
    }

    public BadRequestException(String errorKey, String detail) {
        this(null, errorKey, detail);
    }

    public BadRequestException(Enum errorKey, String detail) {
        this(null, errorKey.name(), detail);
    }

    public BadRequestException(URI type, String errorKey, String detail) {
        super(type, null, Status.BAD_REQUEST, detail, null, null, getAlertParameters(errorKey, detail));
        this.errorKey = errorKey;
    }

    public BadRequestException(URI type, String errorKey, String detail, Map<String, Object> externalParams) {
        super(type, null, Status.BAD_REQUEST, detail, null, null, externalParams);
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String errorKey, String detail) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", detail);
        return parameters;
    }
}
