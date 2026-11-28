package gregtech.api.structure.error;

public enum ErrorType {

    TOO_FEW, // need hatch count >= target
    NOT_MATCH, // need hatch count == target
    TOO_MANY; // need hatch count <= target

    private static final ErrorType[] cachedValues = ErrorType.values();

    public static ErrorType from(int ord) {
        return cachedValues[ord];
    }
}
