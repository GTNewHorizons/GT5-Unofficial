package gregtech.common.misc.spaceprojects.interfaces;

import java.util.List;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;
import gregtech.common.misc.spaceprojects.enums.UpgradeStatus;

public interface ISpaceProject {

    String getProjectName();

    String getUnlocalizedName();

    String getLocalizedName();

    long getProjectVoltage();

    int getProjectBuildTime();

    int getProjectCurrentProgress();

    int getProjectTier();

    int getTotalStages();

    List<ISP_Upgrade> getUpgradesAvailable();

    int getProgressForUpgrade();

    ItemStack[] getItemCostPerStage();

    ItemStack[] getCurrentItemProgress();

    ItemStack[] getTotalItemCost();

    FluidStack[] getFluidCostPerStage();

    FluidStack[] getCurrentFluidProgress();

    FluidStack[] getTotalFluidCost();

    ISP_Upgrade getUpgradeBeingBuilt();

    void saveExtraSavedWorldData(NBTTagCompound aNBT);

    void loadExtraSavedWorldData(NBTTagCompound aNBT);

    void setProjectStage(int aStage);

    void setCurrentUpgradeBeingBuilt(ISP_Upgrade aUpgrade);

    void goToNextStage();

    ISpaceProject copy();

    int getCurrentStage();

    boolean meetsRequirements(UUID aTeam, ISpaceBody aLocation);

    public interface ISP_Upgrade {

        String getUpgradeName();

        String getUnlocalizedName();

        String getLocalizedName();

        ItemStack[] getItemCost();

        ItemStack[] getTotalItemCost();

        FluidStack[] getFluidCost();

        FluidStack[] getTotalFLuidCost();

        int getTotalStages();

        int getTotalBuildTime();

        int getCurrentStage();

        long getVoltage();

        UpgradeStatus getStatus();

        ISP_Requirements getUpgradeRequirements();

        boolean meetsRequirements();
    }

    public interface ISP_Requirements {

        SpaceBodyType getBodyType();

        StarType getStarType();

        List<ISpaceProject> getProjects();

        List<ISP_Upgrade> getUpgrades();
    }
}
