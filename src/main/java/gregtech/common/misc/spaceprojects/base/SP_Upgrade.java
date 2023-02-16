package gregtech.common.misc.spaceprojects.base;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.enums.UpgradeStatus;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Upgrade;

public class SP_Upgrade implements ISP_Upgrade {

    // #region Variables

    private String name;
    private String unlocalizedName;
    private ItemStack[] itemsCost;
    private FluidStack[] fluidsCost;
    private int totalStages;
    private int currentStage;
    private int buildTime;
    private long voltage;
    private SP_Requirements requirements;
    private SpaceProject projectBelongingTo;

    // #endregion

    // #region Getters

    @Override
    public String getUpgradeName() {
        return name;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(unlocalizedName);
    }

    @Override
    public ItemStack[] getItemsCostPerStage() {
        return itemsCost;
    }

    @Override
    public ItemStack getItemCostPerStage(int index) {
        if (itemsCost == null || index < 0 || index >= itemsCost.length) {
            return null;
        }

        return itemsCost[index];
    }

    @Override
    public ItemStack[] getCurrentItemsProgress() {
        ItemStack[] currentItemsProgress = new ItemStack[itemsCost.length];
        int index = 0;
        for (ItemStack item : itemsCost) {
            ItemStack copy = item.copy();
            copy.stackSize *= getCurrentStage();
            currentItemsProgress[index++] = copy;
        }

        return currentItemsProgress;
    }

    @Override
    public ItemStack getCurrentItemProgress(int index) {
        if (itemsCost == null || index < 0 || index >= itemsCost.length) {
            return null;
        }

        ItemStack item = itemsCost[index].copy();
        item.stackSize *= getCurrentStage();
        return item;
    }

    @Override
    public ItemStack[] getTotalItemsCost() {
        ItemStack[] totalItemsCost = new ItemStack[itemsCost.length];
        int index = 0;
        for (ItemStack item : itemsCost) {
            ItemStack copy = item.copy();
            copy.stackSize *= getTotalStages();
            totalItemsCost[index++] = copy;
        }

        return totalItemsCost;
    }

    @Override
    public ItemStack getTotalItemCost(int index) {
        if (itemsCost == null || index < 0 || index >= itemsCost.length) {
            return null;
        }

        ItemStack item = itemsCost[index].copy();
        item.stackSize *= getTotalStages();
        return item;
    }

    @Override
    public FluidStack[] getFluidsCostPerStage() {
        return fluidsCost;
    }

    @Override
    public FluidStack getFluidCostPerStage(int index) {
        if (fluidsCost == null || index < 0 || index >= fluidsCost.length) {
            return null;
        }

        return fluidsCost[index];
    }

    @Override
    public FluidStack[] getCurrentFluidsProgress() {
        if (fluidsCost == null) {
            return null;
        }

        FluidStack[] currentFluidsProgress = new FluidStack[fluidsCost.length];
        int index = 0;
        for (FluidStack fluid : fluidsCost) {
            FluidStack copy = fluid.copy();
            copy.amount *= getCurrentStage();
            currentFluidsProgress[index++] = copy;
        }

        return currentFluidsProgress;
    }

    @Override
    public FluidStack getCurrentFluidProgress(int index) {
        if (fluidsCost == null || index < 0 || index >= fluidsCost.length) {
            return null;
        }

        FluidStack fluid = fluidsCost[index].copy();
        fluid.amount *= getCurrentStage();
        return fluid;
    }

    @Override
    public FluidStack[] getTotalFluidsCost() {
        if (fluidsCost == null) {
            return null;
        }

        FluidStack[] totalFluidsCost = new FluidStack[fluidsCost.length];
        int index = 0;
        for (FluidStack fluid : fluidsCost) {
            FluidStack copy = fluid.copy();
            copy.amount *= getTotalStages();
            totalFluidsCost[index++] = copy;
        }

        return totalFluidsCost;
    }

    @Override
    public FluidStack getTotalFluidCost(int index) {
        if (fluidsCost == null || index < 0 || index >= fluidsCost.length) {
            return null;
        }

        FluidStack fluid = fluidsCost[index].copy();
        fluid.amount *= getTotalStages();
        return fluid;
    }

    @Override
    public int getTotalStages() {
        return totalStages;
    }

    @Override
    public int getTotalBuildTime() {
        return buildTime;
    }

    @Override
    public int getCurrentStage() {
        return currentStage;
    }

    @Override
    public float getCurrentProgress() {
        return currentStage / totalStages * 100.0f;
    }

    @Override
    public long getVoltage() {
        return voltage;
    }

    @Override
    public UpgradeStatus getStatus() {
        if (requirements == null) {
            return UpgradeStatus.Unlocked;
        }

        if (isFinished()) {
            return UpgradeStatus.Finished;
        }
        return UpgradeStatus.Locked;
    }

    @Override
    public SP_Requirements getUpgradeRequirements() {
        return requirements;
    }

    // #endregion

    // #region Setter/Builder

    public SP_Upgrade() {}

    public SP_Upgrade setUpgradeName(String upgradeName) {
        name = upgradeName;
        return this;
    }

    public SP_Upgrade setUpgradeUnlocalizedName(String upgradeUnlocalizedName) {
        unlocalizedName = upgradeUnlocalizedName;
        return this;
    }

    public SP_Upgrade setUpgradeItemsCost(ItemStack... upgradeItemsCost) {
        itemsCost = upgradeItemsCost;
        return this;
    }

    public SP_Upgrade setUpgradeFluidsCost(FluidStack... upgradeFluidsCost) {
        fluidsCost = upgradeFluidsCost;
        return this;
    }

    public SP_Upgrade setUpgradeRequirements(SP_Requirements upgradeRequirements) {
        requirements = upgradeRequirements;
        return this;
    }

    public SP_Upgrade setUpgradeTotalStages(int upgradeTotalStages) {
        totalStages = upgradeTotalStages;
        return this;
    }

    public SP_Upgrade setUpgradeBuildTime(int upgradeBuildTime) {
        buildTime = upgradeBuildTime;
        return this;
    }

    public SP_Upgrade setUpgradeVoltage(long upgradeVoltage) {
        voltage = upgradeVoltage;
        return this;
    }

    @Override
    public void setUpgradeProject(SpaceProject project) {
        projectBelongingTo = project;
    }

    // #endregion

    // #region Other

    @Override
    public boolean meetsRequirements(UUID aTeam) {
        if (requirements == null) {
            return true;
        }

        if (requirements.getBodyType() != null) {
            if (!requirements.getBodyType().equals(projectBelongingTo.getProjectLocation().getType())) {
                return false;
            }
        }

        if (requirements.getStarType() != null) {
            if (!requirements.getStarType().equals(projectBelongingTo.getProjectLocation().getStarType())) {
                return false;
            }
        }

        if (requirements.getProjects() != null) {
            for (SpaceProject tProject : requirements.getProjects()) {
                if (!SpaceProjectManager.teamHasProject(aTeam, tProject)) {
                    return false;
                }
            }
        }

        if (requirements.getUpgrades() != null) {
            for (SP_Upgrade upgrade : requirements.getUpgrades()) {
                if (!projectBelongingTo.hasUpgrade(upgrade.getUpgradeName())) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public SP_Upgrade copy() {
        SP_Upgrade copy = new SP_Upgrade().setUpgradeName(name).setUpgradeUnlocalizedName(unlocalizedName)
                .setUpgradeBuildTime(buildTime).setUpgradeFluidsCost(fluidsCost).setUpgradeItemsCost(itemsCost)
                .setUpgradeRequirements(requirements).setUpgradeTotalStages(totalStages).setUpgradeVoltage(voltage);
        return copy;
    }

    @Override
    public void goToNextStage() {
        currentStage++;
    }

    @Override
    public boolean isFinished() {
        return currentStage == totalStages;
    }

    // #endregion

}
