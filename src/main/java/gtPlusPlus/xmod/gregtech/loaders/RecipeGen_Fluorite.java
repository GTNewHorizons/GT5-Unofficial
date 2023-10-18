package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class RecipeGen_Fluorite extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_Fluorite(final Material material) {
        this.toGenerate = material;
        mRecipeGenMap.add(this);

        /**
         * Shaped Crafting
         */
        RecipeUtils.addShapedRecipe(
                CI.craftingToolHammer_Hard,
                null,
                null,
                material.getCrushedPurified(1),
                null,
                null,
                null,
                null,
                null,
                material.getDustPurified(1));

        RecipeUtils.addShapedRecipe(
                CI.craftingToolHammer_Hard,
                null,
                null,
                material.getCrushed(1),
                null,
                null,
                null,
                null,
                null,
                material.getDustImpure(1));

        RecipeUtils.addShapedRecipe(
                CI.craftingToolHammer_Hard,
                null,
                null,
                material.getCrushedCentrifuged(1),
                null,
                null,
                null,
                null,
                null,
                material.getDust(1));

        final ItemStack normalDust = material.getDust(1);
        final ItemStack smallDust = material.getSmallDust(1);
        final ItemStack tinyDust = material.getTinyDust(1);

        if (RecipeUtils.addShapedRecipe(
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                normalDust)) {
            Logger.WARNING("9 Tiny dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("9 Tiny dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Failed");
        }

        if (RecipeUtils
                .addShapedRecipe(normalDust, null, null, null, null, null, null, null, null, material.getTinyDust(9))) {
            Logger.WARNING("9 Tiny dust from 1 Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("9 Tiny dust from 1 Recipe: " + material.getLocalizedName() + " - Failed");
        }

        if (RecipeUtils.addShapedRecipe(
                smallDust,
                smallDust,
                null,
                smallDust,
                smallDust,
                null,
                null,
                null,
                null,
                normalDust)) {
            Logger.WARNING("4 Small dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("4 Small dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Failed");
        }

        if (RecipeUtils.addShapedRecipe(
                null,
                normalDust,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                material.getSmallDust(4))) {
            Logger.WARNING("4 Small dust from 1 Dust Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("4 Small dust from 1 Dust Recipe: " + material.getLocalizedName() + " - Failed");
        }
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    public static void generateRecipes(final Material material) {

        int tVoltageMultiplier = MaterialUtils.getVoltageForTier(material.vTier);

        final ItemStack dustStone = ItemUtils.getItemStackOfAmountFromOreDict("dustStone", 1);

        ItemStack tinyDustA = FLUORIDES.FLUORITE.getTinyDust(1);
        ItemStack tinyDustB = FLUORIDES.FLUORITE.getTinyDust(1);
        ItemStack matDust = FLUORIDES.FLUORITE.getDust(1);
        ItemStack matDustA = FLUORIDES.FLUORITE.getDust(1);

        /**
         * Package
         */
        // Allow ore dusts to be packaged
        if (ItemUtils.checkForInvalidItems(material.getSmallDust(1))
                && ItemUtils.checkForInvalidItems(material.getTinyDust(1))) {
            RecipeGen_DustGeneration.generatePackagerRecipes(material);
        }

        /**
         * Macerate
         */
        // Macerate ore to Crushed
        if (GT_Values.RA.addPulveriserRecipe(
                material.getOre(1),
                new ItemStack[] { material.getCrushed(2) },
                new int[] { 10000 },
                20 * 20,
                tVoltageMultiplier / 2)) {
            Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate ore to Crushed ore'");
        }
        // Macerate Centrifuged to Pure Dust
        if (GT_Values.RA.addPulveriserRecipe(
                material.getCrushedCentrifuged(1),
                new ItemStack[] { matDust, matDustA },
                new int[] { 10000, 1000 },
                20 * 20,
                tVoltageMultiplier / 2)) {
            Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Centrifuged ore to Pure Dust'");
        }
        if (GT_ModHandler.addThermalCentrifugeRecipe(
                material.getCrushedPurified(1),
                (int) Math.min(5000L, Math.abs(material.getMass() * 20L)),
                material.getCrushedCentrifuged(1),
                tinyDustA,
                dustStone)) {
            Logger.MATERIALS(
                    "[ThermalCentrifuge] Added Recipe: 'Washed ore to Centrifuged Ore' | Input: "
                            + material.getCrushedPurified(1).getDisplayName()
                            + " | Outputs: "
                            + material.getCrushedCentrifuged(1).getDisplayName()
                            + ", "
                            + tinyDustA.getDisplayName()
                            + ", "
                            + dustStone.getDisplayName()
                            + ".");
        }

        GT_Values.RA.addChemicalBathRecipe(
                FLUORIDES.FLUORITE.getCrushed(2),
                FluidUtils.getFluidStack("hydrogen", 2000),
                FLUORIDES.FLUORITE.getCrushedPurified(8),
                FLUORIDES.FLUORITE.getDustImpure(4),
                FLUORIDES.FLUORITE.getDustPurified(2),
                new int[] { 10000, 5000, 1000 },
                30 * 20,
                240);

        /**
         * Forge Hammer
         */
        if (GT_Values.RA.addForgeHammerRecipe(material.getCrushedCentrifuged(1), matDust, 10, tVoltageMultiplier / 4)) {
            Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Crushed Centrifuged to Pure Dust'");
        }

        /**
         * Centrifuge
         */
        // Purified Dust to Clean
        if (GT_Values.RA.addCentrifugeRecipe(
                material.getDustPurified(1),
                null,
                null, // In Fluid
                null, // Out Fluid
                matDust,
                tinyDustA,
                null,
                null,
                null,
                null,
                new int[] { 10000, 10000 }, // Chances
                (int) Math.max(1L, material.getMass() * 8L), // Time
                tVoltageMultiplier / 2)) { // Eu
            Logger.MATERIALS("[Centrifuge] Added Recipe: Purified Dust to Clean Dust");
        }

        // Impure Dust to Clean
        if (GT_Values.RA.addCentrifugeRecipe(
                material.getDustImpure(1),
                null,
                null, // In Fluid
                null, // Out Fluid
                matDust,
                tinyDustB,
                null,
                null,
                null,
                null,
                new int[] { 10000, 10000 }, // Chances
                (int) Math.max(1L, material.getMass() * 8L), // Time
                tVoltageMultiplier / 2)) { // Eu
            Logger.MATERIALS("[Centrifuge] Added Recipe: Inpure Dust to Clean Dust");
        }

        // CaF2 + H2SO4 â†’ CaSO4(solid) + 2 HF
        FluidStack aGregtechHydro = FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 16000);
        if (aGregtechHydro == null) {
            aGregtechHydro = FluidUtils.getFluidStack("hydrofluoricacid", 16000);
        }

        CORE.RA.addDehydratorRecipe(
                new ItemStack[] { CI.getNumberedAdvancedCircuit(5), FLUORIDES.FLUORITE.getDust(37), },
                FluidUtils.getFluidStack("sulfuricacid", 8000),
                aGregtechHydro, // Fluid output (slot 2)
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumSulfate", 15),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", 1),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustGold", 2),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 2) },
                new int[] { 10000, 1000, 1000, 3000, 2000 },
                10 * 60 * 20,
                240); // EU
    }
}
