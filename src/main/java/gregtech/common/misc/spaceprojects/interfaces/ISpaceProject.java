package gregtech.common.misc.spaceprojects.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.common.misc.spaceprojects.base.SP_Upgrade;
import gregtech.common.misc.spaceprojects.base.SpaceProject;
import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;
import gregtech.common.misc.spaceprojects.enums.UpgradeStatus;

public interface ISpaceProject {

    String getProjectName();

    String getUnlocalizedName();

    String getLocalizedName();

    long getProjectVoltage();

    int getProjectBuildTime();

    float getProjectCurrentProgress();

    int getProjectTier();

    int getCurrentStage();

    int getTotalStages();

    Collection<SP_Upgrade> getAllUpgrades();

    Map<String, SP_Upgrade> getUpgradesBuilt();

    ISP_Upgrade getUpgrade(String upgradeName);

    ItemStack[] getItemsCostPerStage();

    ItemStack getItemCostPerStage(int index);

    ItemStack[] getCurrentItemsProgress();

    ItemStack getCurrentItemProgress(int index);

    ItemStack[] getTotalItemsCost();

    ItemStack getTotalItemCost(int index);

    FluidStack[] getFluidsCostPerStage();

    FluidStack getFluidCostPerStage(int index);

    FluidStack[] getCurrentFluidsProgress();

    FluidStack getCurrentFluidProgress(int index);

    FluidStack[] getTotalFluidsCost();

    FluidStack getTotalFluidCost(int index);

    ISP_Upgrade getUpgradeBeingBuilt();

    ISpaceBody getProjectLocation();

    void setProjectStage(int stage);

    void setCurrentUpgradeBeingBuilt(SP_Upgrade upgrade);

    void setProjectLocation(ISpaceBody newLocation);

    void goToNextStage();

    ISpaceProject copy();

    boolean meetsRequirements(UUID team);

    boolean isFinished();

    boolean hasUpgrade(String upgradeName);

    public interface ISP_Upgrade {

        String getUpgradeName();

        String getUnlocalizedName();

        String getLocalizedName();

        ItemStack[] getItemsCostPerStage();

        ItemStack getItemCostPerStage(int index);

        ItemStack[] getCurrentItemsProgress();

        ItemStack getCurrentItemProgress(int index);

        ItemStack[] getTotalItemsCost();

        ItemStack getTotalItemCost(int index);

        FluidStack[] getFluidsCostPerStage();

        FluidStack getFluidCostPerStage(int index);

        FluidStack[] getCurrentFluidsProgress();

        FluidStack getCurrentFluidProgress(int index);

        FluidStack[] getTotalFluidsCost();

        FluidStack getTotalFluidCost(int index);

        int getTotalStages();

        int getTotalBuildTime();

        int getCurrentStage();

        float getCurrentProgress();

        long getVoltage();

        UpgradeStatus getStatus();

        ISP_Requirements getUpgradeRequirements();

        void setUpgradeProject(SpaceProject project);

        boolean meetsRequirements(UUID team);

        SP_Upgrade copy();

        void goToNextStage();

        boolean isFinished();
    }

    public interface ISP_Requirements {

        SpaceBodyType getBodyType();

        StarType getStarType();

        List<SpaceProject> getProjects();

        List<SP_Upgrade> getUpgrades();
    }
}
