package gregtech.common.misc.spaceprojects.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

/**
 * @author BlueWeabo
 */
public class SpaceProject implements ISpaceProject {

    // #region Variables

    protected String name;
    protected String unlocalizedName;
    protected long voltage;
    protected int buildTime;
    protected int projectTier;
    protected int currentStage;
    protected int totalStage;
    protected Map<String, ISP_Upgrade> upgradesAvailable = new HashMap<>();
    protected Map<String, ISP_Upgrade> upgradesInstalled = new HashMap<>();
    protected ISP_Requirements requirements;
    protected ISP_Upgrade currentUpgrade;
    protected ItemStack[] itemsCost;
    protected FluidStack[] fluidsCost;
    protected ISpaceBody location;
    protected UITexture texture;

    // #endregion

    // #region Getters

    @Override
    public String getProjectName() {
        return name;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(unlocalizedName);
    }

    @Override
    public long getProjectVoltage() {
        return voltage;
    }

    @Override
    public int getProjectBuildTime() {
        return buildTime;
    }

    @Override
    public float getProjectCurrentProgress() {
        return currentStage / totalStage * 100.0f;
    }

    @Override
    public int getProjectTier() {
        return projectTier;
    }

    @Override
    public int getCurrentStage() {
        return currentStage;
    }

    @Override
    public int getTotalStages() {
        return totalStage;
    }

    @Override
    public Collection<ISP_Upgrade> getAllUpgrades() {
        return upgradesAvailable.values();
    }

    @Override
    public Map<String, ISP_Upgrade> getUpgradesBuiltMap() {
        return upgradesInstalled;
    }

    @Override
    public Collection<ISP_Upgrade> getAllBuiltUpgrades() {
        return upgradesInstalled.values();
    }

    @Override
    public ISP_Upgrade getUpgrade(String upgradeName) {
        return upgradesAvailable.get(upgradeName);
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
            if (item == null) {
                currentItemsProgress[index++] = null;
                continue;
            }
            ItemStack copy = item.copy();
            copy.stackSize *= getCurrentStage();
            currentItemsProgress[index++] = copy;
        }

        return currentItemsProgress;
    }

    @Override
    public ItemStack getCurrentItemProgress(int index) {
        if (itemsCost == null || index < 0 || index >= itemsCost.length || itemsCost[index] == null) {
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
            if (item == null) {
                totalItemsCost[index++] = null;
                continue;
            }
            ItemStack copy = item.copy();
            copy.stackSize *= getTotalStages();
            totalItemsCost[index++] = copy;
        }

        return totalItemsCost;
    }

    @Override
    public ItemStack getTotalItemCost(int index) {
        if (itemsCost == null || index < 0 || index >= itemsCost.length || itemsCost[index] == null) {
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
        if (fluidsCost == null || index < 0 || index >= fluidsCost.length || fluidsCost[index] == null) {
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
            if (fluid == null) {
                currentFluidsProgress[index++] = null;
                continue;
            }
            FluidStack copy = fluid.copy();
            copy.amount *= getCurrentStage();
            currentFluidsProgress[index++] = copy;
        }

        return currentFluidsProgress;
    }

    @Override
    public FluidStack getCurrentFluidProgress(int index) {
        if (fluidsCost == null || index < 0 || index >= fluidsCost.length || fluidsCost[index] == null) {
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
            if (fluid == null) {
                totalFluidsCost[index++] = null;
                continue;
            }
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
    public ISP_Upgrade getUpgradeBeingBuilt() {
        return currentUpgrade;
    }

    @Override
    public ISpaceBody getProjectLocation() {
        return location;
    }

    @Override
    public UITexture getTexture() {
        return texture;
    }

    // #endregion

    // #region Setter/Builder

    public SpaceProject setProjectName(String spaceProjectName) {
        name = spaceProjectName;
        return this;
    }

    public SpaceProject setProjectUnlocalizedName(String spaceProjectUnlocalizedName) {
        unlocalizedName = spaceProjectUnlocalizedName;
        return this;
    }

    public SpaceProject setProjectVoltage(long spaceProjectVoltage) {
        voltage = spaceProjectVoltage;
        return this;
    }

    public SpaceProject setProjectBuildTime(int spaceProjectBuildTime) {
        buildTime = spaceProjectBuildTime;
        return this;
    }

    public SpaceProject setProjectStages(int spaceProjectTotalStages) {
        totalStage = spaceProjectTotalStages;
        return this;
    }

    public SpaceProject setProjectItemsCost(ItemStack... spaceProjectItemsCost) {
        itemsCost = spaceProjectItemsCost;
        return this;
    }

    public SpaceProject setProjectFluidsCost(FluidStack... spaceProjectFluidsCost) {
        fluidsCost = spaceProjectFluidsCost;
        return this;
    }

    public SpaceProject setProjectUpgrades(ISP_Upgrade... spaceProjectUpgrades) {
        for (ISP_Upgrade upgrade : spaceProjectUpgrades) {
            upgrade.setUpgradeProject(this);
            upgradesAvailable.put(upgrade.getUpgradeName(), upgrade);
        }
        return this;
    }

    public SpaceProject setProjectTexture(UITexture projectTexture) {
        texture = projectTexture;
        return this;
    }

    public SpaceProject setProjectRequirements(ISP_Requirements projectRequirements) {
        requirements = projectRequirements;
        return this;
    }

    @Override
    public void setCurrentUpgradeBeingBuilt(ISP_Upgrade newCurrentUpgrade) {
        if (newCurrentUpgrade == null) {
            return;
        }

        if (totalStage == currentStage) {
            currentUpgrade = newCurrentUpgrade.copy();
            currentUpgrade.setUpgradeProject(this);
        }
    }

    @Override
    public void setProjectCurrentStage(int newCurrentStage) {
        currentStage = newCurrentStage;
    }

    @Override
    public void setProjectLocation(ISpaceBody newLocation) {
        location = newLocation;
    }

    @Override
    public void setBuiltUpgrade(ISP_Upgrade... upgrades) {
        if (upgrades == null) {
            return;
        }

        for (ISP_Upgrade upgrade : upgrades) {
            if (upgrade.equals(currentUpgrade)) {
                currentUpgrade = null;
            }
            upgradesInstalled.put(upgrade.getUpgradeName(), upgrade);
        }
    }

    // #endregion

    // #region Other

    @Override
    public ISpaceProject copy() {
        SpaceProject copy = new SpaceProject().setProjectName(name)
                                              .setProjectUnlocalizedName(unlocalizedName)
                                              .setProjectVoltage(voltage)
                                              .setProjectBuildTime(buildTime)
                                              .setProjectItemsCost(itemsCost)
                                              .setProjectFluidsCost(fluidsCost)
                                              .setProjectStages(totalStage)
                                              .setProjectTexture(texture)
                                              .setProjectRequirements(requirements);
        if (upgradesAvailable != null) {
            ISP_Upgrade[] upgrades = new SP_Upgrade[upgradesAvailable.size()];
            int index = 0;
            for (ISP_Upgrade upgrade : upgradesAvailable.values()) {
                upgrades[index++] = upgrade.copy();
            }
            copy.setProjectUpgrades(upgrades);
        }
        return copy;
    }

    @Override
    public void goToNextStage() {
        currentStage++;
    }

    @Override
    public boolean meetsRequirements(UUID team) {
        return meetsRequirements(team, true);
    }

    @Override
    public boolean meetsRequirements(UUID team, boolean checkLocation) {
        if (requirements == null) {
            return true;
        }

        if (requirements.getBodyType() != null && checkLocation) {
            if (!requirements.getBodyType()
                             .equals(location.getType())) {
                return false;
            }
        }

        if (requirements.getStarType() != null && checkLocation) {
            if (!requirements.getStarType()
                             .equals(location.getStarType())) {
                return false;
            }
        }

        if (requirements.getProjects() != null) {
            for (ISpaceProject project : requirements.getProjects()) {
                if (!SpaceProjectManager.teamHasProject(team, project)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpaceProject)) {
            return false;
        }
        return getProjectName().equals(((SpaceProject) obj).getProjectName());
    }

    @Override
    public boolean isFinished() {
        return currentStage == totalStage;
    }

    @Override
    public boolean hasUpgrade(String upgradeName) {
        return upgradesInstalled.containsKey(upgradeName);
    }

    // #endregion
}
