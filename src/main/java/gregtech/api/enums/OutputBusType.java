package gregtech.api.enums;

/**
 * This controls the ejection order for output busses. Busses higher in the enum are checked before the lower busses.
 */
public enum OutputBusType {

    Void,
    CompressedFiltered,
    StandardFiltered,
    MEFiltered,
    CompressedUnfiltered,
    StandardUnfiltered,
    MEUnfiltered,
    //
    ;

    public boolean isFiltered() {
        return switch (this) {
            case Void, StandardFiltered, MEFiltered, CompressedFiltered -> true;
            case StandardUnfiltered, MEUnfiltered, CompressedUnfiltered -> false;
        };
    }
}
