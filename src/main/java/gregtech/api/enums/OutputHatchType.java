package gregtech.api.enums;

/**
 * This controls the ejection order for output busses. Busses higher in the enum are checked before the lower busses.
 */
public enum OutputHatchType {

    Void,
    StandardFiltered,
    MECacheFiltered,
    MEFiltered,
    StandardUnfiltered,
    MECacheUnfiltered,
    MEUnfiltered,
    //
    ;

    /**
     * Cached values() array for frequent read-only operations, the array should NOT be mutated.
     */
    public static final OutputHatchType[] VALUES = values();

    public boolean isFiltered() {
        return switch (this) {
            case Void, StandardFiltered, MECacheFiltered, MEFiltered -> true;
            case StandardUnfiltered, MECacheUnfiltered, MEUnfiltered -> false;
        };
    }
}
