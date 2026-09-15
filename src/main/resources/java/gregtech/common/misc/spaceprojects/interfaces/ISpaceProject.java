package gregtech.common.misc.spaceprojects.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import gregtech.common.misc.spaceprojects.enums.SpaceBodyType;
import gregtech.common.misc.spaceprojects.enums.StarType;
import gregtech.common.misc.spaceprojects.enums.UpgradeStatus;

/**
 * @author BlueWeabo
 */
public interface ISpaceProject {

    /**
     * @return the internal name of the project.
     */
    String getProjectName();

    /**
     * @return Unlocalized name of the project.
     */
    String getUnlocalizedName();

    /**
     * @return Localized name of the project using StatCollect#translateToLocal.
     */
    String getLocalizedName();

    /**
     * @return The voltage the project requires to be built at. Used by the project manager.
     */
    long getProjectVoltage();

    /**
     * @return The duration it takes to build out one(1) stage of the project. The time returned is in ticks
     */
    int getProjectBuildTime();

    /**
     * @return The Current Progress of the project in percentage form. 1 being 100% and 0 being 0%.
     */
    float getProjectCurrentProgress();

    /**
     * @return Currently unused, but this is the project's tier. Will be used to determine the min-tier motors needed on
     *         the Space Elevator
     */
    int getProjectTier();

    /**
     * @return The Current stage of the project
     */
    int getCurrentStage();

    /**
     * @return The Total amount of stages the project has
     */
    int getTotalStages();

    /**
     * @return a Collection of all upgrades the project has
     */
    Collection<ISP_Upgrade> getAllUpgrades();

    /**
     * @return a Map of all upgrades that have been built.
     */
    Map<String, ISP_Upgrade> getUpgradesBuiltMap();

    /**
     * @return all built upgrades
     */
    Collection<ISP_Upgrade> getAllBuiltUpgrades();

    /**
     * @param upgradeName The name of the upgrade wanted
     * @return The upgrade with the appropriate name found in the available upgrades for the project
     */
    ISP_Upgrade getUpgrade(String upgradeName);

    /**
     * @return The Items cost required per stage in an array form. Used for making the recipe.
     */
    ItemStack[] getItemsCostPerStage();

    /**
     * @param index Index at which the itemstack is found at
     * @return an item's cost at the appropriate index. Null otherwise if it goes above the index or there are no item
     *         costs
     */
    ItemStack getItemCostPerStage(int index);

    /**
     * @return The Items current progress in an array form.
     */
    ItemStack[] getCurrentItemsProgress();

    /**
     * @param index Index at which the itemstack is found at
     * @return an item's current progress at the appropriate index. Null otherwise if it goes above the index or there
     *         are no item costs
     */
    ItemStack getCurrentItemProgress(int index);

    /**
     * @return The items total cost required in an array form.
     */
    ItemStack[] getTotalItemsCost();

    /**
     * @param index Index at which the itemstack is found at
     * @return an item's total cost at the appropriate index. Null otherwise if it goes above the index or there are no
     *         item costs
     */
    ItemStack getTotalItemCost(int index);

    /**
     * @return The fluids cost required per stage in an array form. Used for making the recipe.
     */
    FluidStack[] getFluidsCostPerStage();

    /**
     * @param index Index at which the fluidstack is found at
     * @return a fluid's cost at the appropriate index. Null otherwise if it goes above the index or there are no fluid
     *         costs
     */
    FluidStack getFluidCostPerStage(int index);

    /**
     * @return The fluids current progress in an array form. Null if there are no fluid costs
     */
    FluidStack[] getCurrentFluidsProgress();

    /**
     * @param index Index at which the fluidstack is found at
     * @return a fluid's current progress at the appropriate index. Null otherwise if it goes above the index or there
     *         are no fluid costs
     */
    FluidStack getCurrentFluidProgress(int index);

    /**
     * @return The fluids total cost required in an array form. Null if there are no fluid costs
     */
    FluidStack[] getTotalFluidsCost();

    /**
     * @param index Index at which the fluidstack is found at
     * @return a fluid's total cost at the appropriate index. Null otherwise if it goes above the index or there are no
     *         fluid costs
     */
    FluidStack getTotalFluidCost(int index);

    /**
     * @return The current upgrade for this project, which is being built
     */
    ISP_Upgrade getUpgradeBeingBuilt();

    /**
     * @return The location of the project
     */
    ISpaceBody getProjectLocation();

    /**
     * @return The texture used in GUIs for the project
     */
    UITexture getTexture();

    /**
     * Sets the current stage of the project
     */
    void setProjectCurrentStage(int stage);

    /**
     * Sets the current upgrade, which needs to be built
     */
    void setCurrentUpgradeBeingBuilt(ISP_Upgrade upgrade);

    /**
     * Sets the project's location when it starts being built
     */
    void setProjectLocation(ISpaceBody newLocation);

    /**
     * Sets the project's upgrades, which have been built
     */
    void setBuiltUpgrade(ISP_Upgrade... upgrades);

    /**
     * Goes to the next stage of the project
     */
    void goToNextStage();

    /**
     * Creates a copy of the space project
     */
    ISpaceProject copy();

    /**
     * Checks if the project meets all requirements with its current location
     *
     * @param team Team wanting said project and checking their projects
     * @return true if all requirements met, false otherwise
     */
    boolean meetsRequirements(UUID team);

    /**
     * Checks if the project meets requirements if it requires other projects, unless {@code checkLocation} is true,
     * then it also checks for the location
     *
     * @param team          Team wanting said project and checking their projects
     * @param checkLocation If the location position should be checked
     * @return true if all requirements met, false otherwise
     */
    boolean meetsRequirements(UUID team, boolean checkLocation);

    /**
     * Checks if the projects is finished
     */
    boolean isFinished();

    /**
     * Checks if the project has a certain upgrade installed or not
     *
     * @param upgradeName Upgrade being searched for
     * @return True if that upgrade has been installed, false otherwise
     */
    boolean hasUpgrade(String upgradeName);

    /**
     * @author BlueWeabo
     */
    interface ISP_Upgrade {

        /**
         * @return internal name of the upgrade
         */
        String getUpgradeName();

        /**
         * @return unlocalized name of the upgrade
         */
        String getUnlocalizedName();

        /**
         * @return localized name of the upgrade
         */
        String getLocalizedName();

        /**
         * @return The Items cost required per stage in an array form. Used for making the recipe.
         */
        ItemStack[] getItemsCostPerStage();

        /**
         * @param index Index at which the itemstack is found at
         * @return an item's cost at the appropriate index. Null otherwise if it goes above the index or there are no
         *         item costs
         */
        ItemStack getItemCostPerStage(int index);

        /**
         * @return The Items current progress in an array form.
         */
        ItemStack[] getCurrentItemsProgress();

        /**
         * @param index Index at which the itemstack is found at
         * @return an item's current progress at the appropriate index. Null otherwise if it goes above the index or
         *         there are no item costs
         */
        ItemStack getCurrentItemProgress(int index);

        /**
         * @return The items total cost required in an array form.
         */
        ItemStack[] getTotalItemsCost();

        /**
         * @param index Index at which the itemstack is found at
         * @return an item's total cost at the appropriate index. Null otherwise if it goes above the index or there are
         *         no item costs
         */
        ItemStack getTotalItemCost(int index);

        /**
         * @return The fluids cost required per stage in an array form. Used for making the recipe.
         */
        FluidStack[] getFluidsCostPerStage();

        /**
         * @param index Index at which the fluidstack is found at
         * @return a fluid's cost at the appropriate index. Null otherwise if it goes above the index or there are no
         *         fluid costs
         */
        FluidStack getFluidCostPerStage(int index);

        /**
         * @return The fluids current progress in an array form. Null if there are no fluid costs
         */
        FluidStack[] getCurrentFluidsProgress();

        /**
         * @param index Index at which the fluidstack is found at
         * @return a fluid's current progress at the appropriate index. Null otherwise if it goes above the index or
         *         there are no fluid costs
         */
        FluidStack getCurrentFluidProgress(int index);

        /**
         * @return The fluids total cost required in an array form. Null if there are no fluid costs
         */
        FluidStack[] getTotalFluidsCost();

        /**
         * @param index Index at which the fluidstack is found at
         * @return a fluid's total cost at the appropriate index. Null otherwise if it goes above the index or there are
         *         no fluid costs
         */
        FluidStack getTotalFluidCost(int index);

        /**
         * @return the total stages an upgrade has
         */
        int getTotalStages();

        /**
         * @return the build time for the upgrade to go to its next stage
         */
        int getUpgradeBuildTime();

        /**
         * @return current stage of the upgrade
         */
        int getCurrentStage();

        /**
         * @return The Current Progress of the upgrade in percentage form. 1 being 100% and 0 being 0%.
         */
        float getCurrentProgress();

        /**
         * @return The voltage at which the upgrade requires to be build at.
         */
        long getVoltage();

        /**
         * Unused, unsure if it will get a sure
         */
        UpgradeStatus getStatus();

        /**
         * @return the requirements the upgrade has
         */
        ISP_Requirements getUpgradeRequirements();

        /**
         * @return the parent project, which the upgrade belongs to
         */
        ISpaceProject getParentProject();

        /**
         * @param project The project the upgrade belongs to
         */
        void setUpgradeProject(ISpaceProject project);

        /**
         * Sets the current stage of the upgrade
         *
         * @param stage the stage to set
         */
        void setUpgradeCurrentStage(int stage);

        /**
         * Checks if the team has met all requirements to be able to build said upgrade
         *
         * @param team The one starting the upgrade
         * @return true if all requirements are met, false otherwise
         */
        boolean meetsRequirements(UUID team);

        /**
         * Creates a copy of the upgrade
         */
        ISP_Upgrade copy();

        /**
         * Goes to the next stage of the upgrade
         */
        void goToNextStage();

        /**
         * @return true if the upgrade has finished all of its stages, false otherwise
         */
        boolean isFinished();
    }

    /**
     * @author BlueWeabo
     */
    interface ISP_Requirements {

        /**
         * @return Space Body Type required by the project/upgrade
         */
        SpaceBodyType getBodyType();

        /**
         * @return Star Type required by the project/upgrade
         */
        StarType getStarType();

        /**
         * @return a list of all project required for the team to have to unlock it
         */
        List<ISpaceProject> getProjects();

        /**
         * @return a list of all upgrades an upgrade can have as required.
         */
        List<ISP_Upgrade> getUpgrades();
    }
}
