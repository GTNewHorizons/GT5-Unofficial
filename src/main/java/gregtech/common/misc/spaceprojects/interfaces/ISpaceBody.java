package gregtech.common.misc.spaceprojects.interfaces;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;

/**
 * @author BlueWeabo
 */
public interface ISpaceBody {

    StarType getStarType();

    SpaceBodyType getType();

    String getName();

    UITexture getTexture();
}
