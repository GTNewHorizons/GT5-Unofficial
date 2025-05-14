package gtnhintergalactic.recipe;

import static gregtech.api.enums.Mods.OpenComputers;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import net.minecraft.item.ItemStack;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.base.SPRequirements;
import gregtech.common.misc.spaceprojects.base.SPUpgrade;
import gregtech.common.misc.spaceprojects.base.SpaceProject;
import gregtech.common.misc.spaceprojects.enums.SolarSystem;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtnhintergalactic.gui.IG_UITextures;
import gtnhintergalactic.spaceprojects.ProjectAsteroidOutpost;
import tectech.thing.CustomItemList;

/**
 * All space projects of this mod
 *
 * @author BlueWeabo
 */
public class SpaceProjectRegistration implements Runnable {

    /**
     * Register space projects
     */
    @Override
    public void run() {
        registerAsteroidOutpost();
        registerPlanetScan();
    }

    /**
     * Register the asteroid outpost
     */
    private void registerAsteroidOutpost() {
        ISpaceProject.ISP_Upgrade reinforcedStructure = new SPUpgrade().setUpgradeName("ReinforcedStructure")
            .setUpgradeUnlocalizedName("ig.sp.upgrade.reinforcedstructure")
            .setUpgradeTotalStages(40)
            .setUpgradeVoltage(TierEU.RECIPE_UHV)
            .setUpgradeBuildTime(500 * 20)
            .setUpgradeItemsCost(
                getModItem(SuperSolarPanels.ID, "AdminSolarPanel", 1L, 0),
                // Neutronium Borosilicate Glass
                new ItemStack(ItemRegistry.bw_realglas, 24, 5),
                getModItem(OpenComputers.ID, "item", 8, 103),
                CustomItemList.Machine_Multi_Computer.get(1),
                ItemList.Sensor_UHV.get(1),
                ItemList.Emitter_UHV.get(1))
            .setUpgradeFluidsCost(MaterialsAlloy.INDALLOY_140.getFluidStack(30 * INGOTS));
        ISpaceProject.ISP_Upgrade improvedComputation = new SPUpgrade().setUpgradeName("ImprovedComputation")
            .setUpgradeUnlocalizedName("ig.sp.upgrade.improvedcomputation")
            .setUpgradeTotalStages(20)
            .setUpgradeVoltage(TierEU.RECIPE_UEV)
            .setUpgradeBuildTime(750 * 20)
            .setUpgradeRequirements(new SPRequirements().setUpgrades(reinforcedStructure))
            .setUpgradeItemsCost(
                getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", 1L, 0),
                getModItem(OpenComputers.ID, "item", 32, 103),
                // Cosmic Neutronium Borosilicate Glass
                new ItemStack(ItemRegistry.bw_realglas, 32, 14),
                CustomItemList.Machine_Multi_Computer.get(16),
                ItemList.Sensor_UEV.get(1),
                ItemList.Emitter_UEV.get(1))
            .setUpgradeFluidsCost(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(10 * INGOTS));
        ISpaceProject asteroidOutpost = new ProjectAsteroidOutpost().setProjectName("AsteroidOutpost")
            .setProjectUnlocalizedName("ig.spaceproject.asteroidoutpost")
            .setProjectUpgrades(reinforcedStructure, improvedComputation)
            .setProjectRequirements(new SPRequirements().setSpaceBodyType(SolarSystem.KuiperBelt.getType()))
            .setProjectBuildTime(250 * 20)
            .setProjectStages(40)
            .setProjectVoltage(TierEU.RECIPE_UV)
            .setProjectTexture(IG_UITextures.PICTURE_SPACE_PROJECT_ASTEROID_OUTPOST)
            .setProjectItemsCost(
                // Osmium Borosilicate Glass
                new ItemStack(ItemRegistry.bw_realglas, 16, 5),
                ItemList.UltraHighStrengthConcrete.get(32),
                ItemList.Machine_LuV_SolarPanel.get(1),
                ItemList.Block_NeutroniumPlate.get(16),
                ItemList.Sensor_UV.get(1),
                ItemList.Emitter_UV.get(1))
            .setProjectFluidsCost(MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS));

        SpaceProjectManager.addProject(asteroidOutpost);
    }

    /**
     * Register the plant scan
     */
    private void registerPlanetScan() {
        ISpaceProject planetScan = new SpaceProject().setProjectName("PlanetScan")
            .setProjectUnlocalizedName("ig.spaceproject.planetscan")
            .setProjectBuildTime(250 * 20)
            .setProjectTexture(IG_UITextures.PICTURE_SPACE_PROJECT_PLANETARY_SCAN)
            .setProjectStages(8)
            .setProjectVoltage(TierEU.RECIPE_UV)
            .setProjectItemsCost(
                // Osmium Borosilicate Glass
                new ItemStack(ItemRegistry.bw_realglas, 8, 5),
                ItemList.Block_NeutroniumPlate.get(4),
                ItemList.Sensor_UV.get(2),
                ItemList.Emitter_UV.get(2))
            .setProjectFluidsCost(MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS));

        SpaceProjectManager.addProject(planetScan);
    }
}
