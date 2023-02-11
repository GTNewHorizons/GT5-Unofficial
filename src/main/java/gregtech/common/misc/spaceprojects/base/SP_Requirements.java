package gregtech.common.misc.spaceprojects.base;

import java.util.List;

import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Requirements;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Upgrade;

public class SP_Requirements implements ISP_Requirements {

    // #region Variables

    private SpaceBodyType mSpaceBodyType;
    private StarType mStarType;
    private List<ISpaceProject> mSpaceProjects;
    private List<ISP_Upgrade> mUpgrades;

    // #endregion

    // #region Getters

    @Override
    public SpaceBodyType getBodyType() {
        return mSpaceBodyType;
    }

    @Override
    public StarType getStarType() {
        return mStarType;
    }

    @Override
    public List<ISpaceProject> getProjects() {
        return mSpaceProjects;
    }

    @Override
    public List<ISP_Upgrade> getUpgrades() {
        return mUpgrades;
    }

    // #endregion
}
