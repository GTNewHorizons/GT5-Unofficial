package gregtech.common.misc.spaceprojects.interfaces;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;

/**
 * @author BlueWeabo
 */
public interface ISpaceBody {

    /**
     * @return The star type of the space body, if its a star
     */
    StarType getStarType();

    /**
     * @return The type of space body it is
     */
    SpaceBodyType getType();

    /**
     * @return The internal name of the space body
     */
    String getName();

    /**
     * @return The texture of the space body used for UI
     */
    UITexture getTexture();

    /**
     * @return The Unlocalized name for this body
     */
    String getUnlocalizedName();
}
