package gregtech.common.misc.spaceprojects.interfaces;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

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

    ItemStack[] getTotalItemCost();

    FluidStack[] getFluidCostPerStage();

    FluidStack[] getTotalFluidCost();

    ISP_Upgrade getUpgradeBeingBuilt();

    void saveExtraSavedWorldData(NBTTagCompound aNBT);

    void loadExtraSavedWorldData(NBTTagCompound aNBT);

    ISpaceProject copy();

    public interface ISP_Upgrade {

        String getUpgradeName();

        String getUnlocalizedName();

        ItemStack[] getItemCost();

        ItemStack[] getTotalItemCost();

        FluidStack[] getFluidCost();

        FluidStack[] getTotalFLuidCost();

        int getTotalStages();

        int getTotalBuildTime();

        int getCurrentStage();

        long getVoltage();

        UpgradeStatus getStatus();

        List<ISP_Upgrade> getRequiredUpgrades();
    }

    public interface ISP_Requirement {

    }
}
