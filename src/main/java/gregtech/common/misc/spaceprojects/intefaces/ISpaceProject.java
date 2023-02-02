package gregtech.common.misc.spaceprojects.intefaces;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import gregtech.common.misc.spaceprojects.UpgradeStatus;

public interface ISpaceProject {

    String getProjectName();

    String getUnlocalizedName();

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
}
