package gregtech.common.misc.spaceprojects.interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
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

    List<ISP_Upgrade> getUpgradesAvailable();

    Map<String, ISP_Upgrade> getUpgradesBuilt();

    ItemStack[] getItemsCostPerStage();

    ItemStack getItemCostPerStage(int aIndex);

    ItemStack[] getCurrentItemsProgress();

    ItemStack getCurrentItemProgress(int aIndex);

    ItemStack[] getTotalItemsCost();

    ItemStack getTotalItemCost(int aIndex);

    FluidStack[] getFluidsCostPerStage();

    FluidStack getFluidCostPerStage(int aIndex);

    FluidStack[] getCurrentFluidsProgress();

    FluidStack getCurrentFluidProgress(int aIndex);

    FluidStack[] getTotalFluidsCost();

    FluidStack getTotalFluidCost(int aIndex);

    ISP_Upgrade getUpgradeBeingBuilt();

    void setProjectStage(int aStage);

    void setCurrentUpgradeBeingBuilt(ISP_Upgrade aUpgrade);

    void goToNextStage();

    ISpaceProject copy();

    boolean meetsRequirements(UUID aTeam, ISpaceBody aLocation);

    boolean isFinished();

    public interface ISP_Upgrade {

        String getUpgradeName();

        String getUnlocalizedName();

        String getLocalizedName();

        ItemStack[] getItemsCostPerStage();

        ItemStack getItemCostPerStage(int aIndex);

        ItemStack[] getCurrentItemsProgress();

        ItemStack getCurrentItemProgress(int aIndex);

        ItemStack[] getTotalItemsCost();

        ItemStack getTotalItemCost(int aIndex);

        FluidStack[] getFluidsCostPerStage();

        FluidStack getFluidCostPerStage(int aIndex);

        FluidStack[] getCurrentFluidsProgress();

        FluidStack getCurrentFluidProgress(int aIndex);

        FluidStack[] getTotalFluidsCost();

        FluidStack getTotalFluidCost(int aIndex);

        int getTotalStages();

        int getTotalBuildTime();

        int getCurrentStage();

        float getCurrentProgress();

        long getVoltage();

        UpgradeStatus getStatus();

        ISP_Requirements getUpgradeRequirements();

        boolean meetsRequirements();

        ISP_Upgrade copy();

        void goToNextStage();
    }

    public interface ISP_Requirements {

        SpaceBodyType getBodyType();

        StarType getStarType();

        List<SpaceProject> getProjects();

        List<ISP_Upgrade> getUpgrades();
    }
}
