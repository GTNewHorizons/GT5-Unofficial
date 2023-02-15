package gregtech.common.misc.spaceprojects.base;

import java.util.List;

import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Requirements;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Upgrade;

public class SP_Requirements implements ISP_Requirements {

    // #region Variables

    private SpaceBodyType mSpaceBodyType = SpaceBodyType.NONE;
    private StarType mStarType = StarType.NotAStar;
    private List<SpaceProject> mSpaceProjects;
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
    public List<SpaceProject> getProjects() {
        return mSpaceProjects;
    }

    @Override
    public List<ISP_Upgrade> getUpgrades() {
        return mUpgrades;
    }

    // #endregion

    // #region Setters/Builder

    public SP_Requirements setSpaceBodyType(SpaceBodyType aSpaceBodyType) {
        mSpaceBodyType = aSpaceBodyType;
        return this;
    }

    public SP_Requirements setStarType(StarType aStarType) {
        mStarType = aStarType;
        return this;
    }

    public SP_Requirements setUpgrades(ISP_Upgrade... aUpgrades) {
        for (ISP_Upgrade tUpgrade : aUpgrades) {
            mUpgrades.add(tUpgrade);
        }
        return this;
    }

    public SP_Requirements setSpaceProjects(SpaceProject... aSpaceProjects) {
        for (SpaceProject aSpaceProject : aSpaceProjects) {
            mSpaceProjects.add(aSpaceProject);
        }
        return this;
    }

    // #endregion
}
