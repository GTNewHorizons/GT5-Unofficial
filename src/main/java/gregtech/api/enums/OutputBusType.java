package gregtech.api.enums;

/**
 * This controls the ejection order for output busses. Busses higher in the enum are checked before the lower busses.
 */
public enum OutputBusType {

    Void,
    StandardFiltered,
    MECacheFiltered,
    MEFiltered,
    StandardUnfiltered,
    MECacheUnfiltered,
    MEUnfiltered,
    //
    ;

    public boolean isFiltered() {
        return switch (this) {
            case Void, StandardFiltered, MECacheFiltered, MEFiltered -> true;
            case StandardUnfiltered, MECacheUnfiltered, MEUnfiltered -> false;
        };
    }
}
