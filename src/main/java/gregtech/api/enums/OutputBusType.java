package gregtech.api.enums;

/**
 * This controls the ejection order for output busses. Busses higher in the enum are checked before the lower busses.
 */
public enum OutputBusType {

    Void,
    CompressedFiltered,
    StandardFiltered,
    MECacheFiltered,
    MEFiltered,
    CompressedUnfiltered,
    StandardUnfiltered,
    MECacheUnfiltered,
    MEUnfiltered,
    //
    ;

    /**
     * Cached values() array for frequent read-only operations, the array should NOT be mutated.
     */
    public static final OutputBusType[] VALUES = values();

    public boolean isFiltered() {
        return switch (this) {
            case Void, StandardFiltered, MECacheFiltered, MEFiltered, CompressedFiltered -> true;
            case StandardUnfiltered, MECacheUnfiltered, MEUnfiltered, CompressedUnfiltered -> false;
        };
    }
}
