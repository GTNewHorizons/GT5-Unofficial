package gregtech.common.misc.spaceprojects.base;

import java.util.List;

import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Requirements;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Upgrade;

public class SP_Requirements implements ISP_Requirements {

    // #region Variables

    private SpaceBodyType spaceBody = SpaceBodyType.NONE;
    private StarType star = StarType.NotAStar;
    private List<SpaceProject> spaceProjects;
    private List<ISP_Upgrade> upgrades;

    // #endregion

    // #region Getters

    @Override
    public SpaceBodyType getBodyType() {
        return spaceBody;
    }

    @Override
    public StarType getStarType() {
        return star;
    }

    @Override
    public List<SpaceProject> getProjects() {
        return spaceProjects;
    }

    @Override
    public List<ISP_Upgrade> getUpgrades() {
        return upgrades;
    }

    // #endregion

    // #region Setters/Builder

    public SP_Requirements setSpaceBodyType(SpaceBodyType spaceBodyType) {
        spaceBody = spaceBodyType;
        return this;
    }

    public SP_Requirements setStarType(StarType starType) {
        star = starType;
        return this;
    }

    public SP_Requirements setUpgrades(ISP_Upgrade... requirementUpgrades) {
        for (ISP_Upgrade upgrade : requirementUpgrades) {
            upgrades.add(upgrade);
        }
        return this;
    }

    public SP_Requirements setSpaceProjects(SpaceProject... requirementProjects) {
        for (SpaceProject project : requirementProjects) {
            spaceProjects.add(project);
        }
        return this;
    }

    // #endregion
}
