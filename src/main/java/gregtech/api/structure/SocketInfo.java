package gregtech.api.structure;

import gregtech.api.objects.CoordinateList;

public class SocketInfo {

    public final char original, replacement;

    public final CoordinateList coordinates = new CoordinateList();

    public SocketInfo(char original, char replacement) {
        this.original = original;
        this.replacement = replacement;
    }
}
