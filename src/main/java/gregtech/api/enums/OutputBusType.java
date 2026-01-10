package gregtech.api.enums;

/**
 * This controls the ejection order for output busses. Busses higher in the enum are checked before the lower busses.
 */
public enum OutputBusType {

    Void,
    StandardFiltered,
    MEFiltered,
    StandardUnfiltered,
    MEUnfiltered,
    //
    ;

    public boolean isFiltered() {
        return switch (this) {
            case Void, StandardFiltered, MEFiltered -> true;
            case StandardUnfiltered, MEUnfiltered -> false;
        };
    }
}
