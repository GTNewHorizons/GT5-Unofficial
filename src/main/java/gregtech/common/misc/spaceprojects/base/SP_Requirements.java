package gregtech.common.misc.spaceprojects.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Requirements;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Upgrade;

/**
 * @author BlueWeabo
 */
public class SP_Requirements implements ISP_Requirements {

    // #region Variables

    protected SpaceBodyType spaceBody = SpaceBodyType.NONE;
    protected StarType star = StarType.NotAStar;
    protected List<ISpaceProject> spaceProjects = new ArrayList<>();
    protected List<ISP_Upgrade> upgrades = new ArrayList<>();

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
    public List<ISpaceProject> getProjects() {
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
        upgrades.addAll(Arrays.asList(requirementUpgrades));
        return this;
    }

    public SP_Requirements setSpaceProjects(ISpaceProject... requirementProjects) {
        spaceProjects.addAll(Arrays.asList(requirementProjects));
        return this;
    }

    // #endregion
}
