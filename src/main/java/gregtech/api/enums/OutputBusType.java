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

    public boolean isFiltered() {
        return switch (this) {
            case Void, StandardFiltered, MECacheFiltered, MEFiltered, CompressedFiltered -> true;
            case StandardUnfiltered, MECacheUnfiltered, MEUnfiltered, CompressedUnfiltered -> false;
        };
    }
}
