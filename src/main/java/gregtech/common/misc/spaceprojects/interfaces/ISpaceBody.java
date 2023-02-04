package gregtech.common.misc.spaceprojects.interfaces;

import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;

public interface ISpaceBody {

    StarType getStarType();

    SpaceBodyType getType();
}
