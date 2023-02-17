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
    protected Map<String, SP_Upgrade> upgradesAvailable = new HashMap<>();
    protected Map<String, SP_Upgrade> upgradesInstalled = new HashMap<>();
    protected SP_Requirements requirements;
    protected SP_Upgrade currentUpgrade;
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
    public Collection<SP_Upgrade> getAllUpgrades() {
        return upgradesAvailable.values();
    }

    @Override
    public Map<String, SP_Upgrade> getUpgradesBuilt() {
        return upgradesInstalled;
    }

    @Override
    public SP_Upgrade getUpgrade(String upgradeName) {
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
        for (FluidStack tFluid : fluidsCost) {
            FluidStack copy = tFluid.copy();
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
    public SP_Upgrade getUpgradeBeingBuilt() {
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

    public SpaceProject setTotalStages(int spaceProjectTotalStages) {
        totalStage = spaceProjectTotalStages;
        return this;
    }

    public SpaceProject setItemCosts(ItemStack... spaceProjectItemsCost) {
        itemsCost = spaceProjectItemsCost;
        return this;
    }

    public SpaceProject setFluidCosts(FluidStack... spaceProjectFluidsCost) {
        fluidsCost = spaceProjectFluidsCost;
        return this;
    }

    public SpaceProject setUpgrades(SP_Upgrade... spaceProjectUpgrades) {
        for (SP_Upgrade upgrade : spaceProjectUpgrades) {
            upgradesAvailable.put(upgrade.getUpgradeName(), upgrade);
        }
        return this;
    }

    public SpaceProject setProjectTexture(UITexture projectTexture) {
        texture = projectTexture;
        return this;
    }

    @Override
    public void setCurrentUpgradeBeingBuilt(SP_Upgrade newCurrentUpgrade) {
        if (totalStage == currentStage) {
            currentUpgrade = newCurrentUpgrade.copy();
            currentUpgrade.setUpgradeProject(this);
        }
    }

    @Override
    public void setProjectStage(int aStage) {
        currentStage = aStage;
    }

    @Override
    public void setProjectLocation(ISpaceBody newLocation) {
        location = newLocation;
    }

    // #endregion

    // #region Other

    @Override
    public SpaceProject copy() {
        SpaceProject copy = new SpaceProject().setProjectName(name).setProjectUnlocalizedName(unlocalizedName)
                .setProjectVoltage(voltage).setProjectBuildTime(buildTime).setItemCosts(itemsCost)
                .setFluidCosts(fluidsCost).setTotalStages(totalStage);
        if (upgradesAvailable != null) {
            SP_Upgrade[] upgrades = new SP_Upgrade[upgradesAvailable.size()];
            int index = 0;
            for (SP_Upgrade upgrade : upgradesAvailable.values()) {
                upgrades[index++] = upgrade.copy();
            }
            copy.setUpgrades(upgrades);
        }
        return copy;
    }

    @Override
    public void goToNextStage() {
        currentStage++;
    }

    @Override
    public boolean meetsRequirements(UUID team) {
        if (requirements == null) {
            return true;
        }

        if (requirements.getBodyType() != null) {
            if (!requirements.getBodyType().equals(location.getType())) {
                return false;
            }
        }

        if (requirements.getStarType() != null) {
            if (!requirements.getStarType().equals(location.getStarType())) {
                return false;
            }
        }

        if (requirements.getProjects() != null) {
            for (SpaceProject project : requirements.getProjects()) {
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
