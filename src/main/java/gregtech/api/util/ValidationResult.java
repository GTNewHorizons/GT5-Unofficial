package gregtech.api.util;

public class ValidationResult<T> {

    private final ValidationType type;
    private final T result;

    private ValidationResult(ValidationType type, T result) {
        this.type = type;
        this.result = result;
    }

    public ValidationType getType() {
        return this.type;
    }

    public T getResult() {
        return this.result;
    }

    public static <T> ValidationResult<T> of(ValidationType result, T value) {
        return new ValidationResult<>(result, value);
    }
}
