package gtPlusPlus.core.material;

import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.item.base.BaseItemComponent.ComponentTypes;
import gtPlusPlus.core.item.base.bolts.BaseItemBolt;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.dusts.BaseItemDust.DustState;
import gtPlusPlus.core.item.base.gears.BaseItemGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.ingots.BaseItemIngotHot;
import gtPlusPlus.core.item.base.nugget.BaseItemNugget;
import gtPlusPlus.core.item.base.ore.BaseItemCentrifugedCrushedOre;
import gtPlusPlus.core.item.base.ore.BaseItemCrushedOre;
import gtPlusPlus.core.item.base.ore.BaseItemImpureDust;
import gtPlusPlus.core.item.base.ore.BaseItemPurifiedCrushedOre;
import gtPlusPlus.core.item.base.ore.BaseItemPurifiedDust;
import gtPlusPlus.core.item.base.ore.BaseItemRawOre;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDense;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.base.rings.BaseItemRing;
import gtPlusPlus.core.item.base.rods.BaseItemRod;
import gtPlusPlus.core.item.base.rods.BaseItemRodLong;
import gtPlusPlus.core.item.base.rotors.BaseItemRotor;
import gtPlusPlus.core.item.base.screws.BaseItemScrew;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_AlloySmelter;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Assembler;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_BlastSmelter;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_DustGeneration;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Extruder;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_FluidCanning;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Fluids;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Fluorite;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_MaterialProcessing;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_MetalRecipe;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Ore;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Plasma;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Plates;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Recycling;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_ShapedCrafting;

public class MaterialGenerator {

    public static final AutoMap<Set<RunnableWithInfo<Material>>> mRecipeMapsToGenerate = new AutoMap<>();

    @SuppressWarnings("unused")
    private static volatile Item temp;

    @SuppressWarnings("unused")
    private static volatile Block tempBlock;

    @Deprecated
    public static boolean addFluidCannerRecipe(ItemStack aEmpty, ItemStack aFullContainer, FluidStack aFluidIn,
        FluidStack rFluidOut) {
        return addFluidCannerRecipe(aEmpty, aFullContainer, aFluidIn, rFluidOut, null, null);
    }

    @Deprecated
    public static boolean addFluidCannerRecipe(ItemStack aEmpty, ItemStack aFullContainer, FluidStack aFluidIn,
        FluidStack rFluidOut, Integer aTime, Integer aEu) {

        RecipeGen_FluidCanning g = new RecipeGen_FluidCanning(false, aEmpty, aFullContainer, aFluidIn, null, null, 0);
        if (g != null && g.valid()) {
            return true;
        }
        return false;
    }

    public static void generate(final Material matInfo) {
        generate(matInfo, true);
    }

    public static void generate(final Material matInfo, final boolean generateEverything) {
        generate(matInfo, generateEverything, true);
    }

    public static boolean generate(final Material matInfo, final boolean generateEverything,
        final boolean generateBlastSmelterRecipes) {
        try {
            final String unlocalizedName = matInfo.getUnlocalizedName();
            final String materialName = matInfo.getLocalizedName();
            final short[] C = matInfo.getRGBA();
            final int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);
            final boolean hotIngot = matInfo.requiresBlastFurnace();
            int materialTier = matInfo.vTier; // TODO

            if ((materialTier > 10) || (materialTier <= 0)) {
                materialTier = 2;
            }

            int sRadiation = 0;
            if (ItemUtils.isRadioactive(materialName) || (matInfo.vRadiationLevel != 0)) {
                sRadiation = matInfo.vRadiationLevel;
            }

            if (matInfo.getState() == MaterialState.SOLID) {
                if (generateEverything == true) {
                    if (sRadiation >= 1) {
                        tempBlock = new BlockBaseModular(matInfo, BlockTypes.STANDARD);
                        temp = new BaseItemIngot(matInfo);

                        temp = new BaseItemDust(matInfo);
                        temp = new BaseItemNugget(matInfo);
                        temp = new BaseItemPlate(matInfo);
                        temp = new BaseItemRod(matInfo);
                        temp = new BaseItemRodLong(matInfo);
                    } else {
                        tempBlock = new BlockBaseModular(matInfo, BlockTypes.STANDARD);
                        tempBlock = new BlockBaseModular(matInfo, BlockTypes.FRAME);
                        temp = new BaseItemIngot(matInfo);
                        if (hotIngot) {
                            temp = new BaseItemIngotHot(matInfo);
                        }
                        temp = new BaseItemDust(matInfo);
                        temp = new BaseItemNugget(matInfo);
                        temp = new BaseItemPlate(matInfo);
                        temp = new BaseItemPlateDouble(matInfo);
                        temp = new BaseItemBolt(matInfo);
                        temp = new BaseItemRod(matInfo);
                        temp = new BaseItemRodLong(matInfo);
                        temp = new BaseItemRing(matInfo);
                        temp = new BaseItemScrew(matInfo);
                        temp = new BaseItemRotor(matInfo);
                        temp = new BaseItemGear(matInfo);
                        temp = new BaseItemPlateDense(matInfo);
                    }
                } else {
                    tempBlock = new BlockBaseModular(matInfo, BlockTypes.STANDARD);

                    temp = new BaseItemIngot(matInfo);
                    temp = new BaseItemDust(matInfo);
                    temp = new BaseItemNugget(matInfo);
                    temp = new BaseItemPlate(matInfo);
                    temp = new BaseItemPlateDouble(matInfo);
                }
            } else if (matInfo.getState() == MaterialState.LIQUID) {
                if (generateEverything == true) {
                    tempBlock = new BlockBaseModular(matInfo, BlockTypes.STANDARD);
                }
                temp = new BaseItemIngot(matInfo);
                temp = new BaseItemDust(matInfo);
                temp = new BaseItemNugget(matInfo);
                temp = new BaseItemPlate(matInfo);
                temp = new BaseItemPlateDouble(matInfo);
            } else if (matInfo.getState() == MaterialState.GAS) {
                temp = new BaseItemDust(matInfo);
                FluidUtils.generateGas(unlocalizedName, materialName, matInfo.getMeltingPointK(), C, true);
            } else if (matInfo.getState() == MaterialState.PURE_GAS) {
                FluidUtils.generateGas(unlocalizedName, materialName, matInfo.getMeltingPointK(), C, true);
                return true;
            } else if (matInfo.getState() == MaterialState.PURE_LIQUID) {
                FluidUtils.generateFluidNoPrefix(unlocalizedName, materialName, matInfo.getMeltingPointK(), C);
                return true;
            } else if (matInfo.getState() == MaterialState.ORE) {

            }

            // Add A jillion Recipes - old code
            new RecipeGen_AlloySmelter(matInfo);
            new RecipeGen_Assembler(matInfo);
            if (generateBlastSmelterRecipes) {
                new RecipeGen_BlastSmelter(matInfo);
            }
            new RecipeGen_MetalRecipe(matInfo);
            new RecipeGen_Extruder(matInfo);
            new RecipeGen_Fluids(matInfo);
            new RecipeGen_Plates(matInfo);
            new RecipeGen_ShapedCrafting(matInfo);
            new RecipeGen_MaterialProcessing(matInfo);

            new RecipeGen_DustGeneration(matInfo);
            new RecipeGen_Recycling(matInfo);
            new RecipeGen_Plasma(matInfo);

            return true;

        } catch (final Throwable t) {

            Logger.MATERIALS("" + matInfo.getLocalizedName() + " failed to generate.");
            return false;
        }
    }

    public static void generateDusts(final Material matInfo) {
        final String unlocalizedName = matInfo.getUnlocalizedName();
        final String materialName = matInfo.getLocalizedName();
        final short[] C = matInfo.getRGBA();
        final int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);
        int materialTier = matInfo.vTier; // TODO

        if ((materialTier > 10) || (materialTier <= 0)) {
            materialTier = 2;
        }

        int sRadiation = 0;
        if (ItemUtils.isRadioactive(materialName) || (matInfo.vRadiationLevel != 0)) {
            sRadiation = matInfo.vRadiationLevel;
        }

        if (matInfo.getState() == MaterialState.SOLID) {
            temp = new BaseItemDust(matInfo);
        }

        // Add A jillion Recipes - old code
        try {
            RecipeGen_DustGeneration.addMixerRecipe_Standalone(matInfo);
            new RecipeGen_Fluids(matInfo);
            new RecipeGen_MaterialProcessing(matInfo);
        } catch (Throwable t) {
            Logger.MATERIALS("Failed to generate some recipes for " + materialName);
            Logger.ERROR("Failed to generate some recipes for " + materialName);
            t.printStackTrace();
        }
        // RecipeGen_Recycling.generateRecipes(matInfo);
    }

    public static void generateNuclearMaterial(final Material matInfo) {
        generateNuclearMaterial(matInfo, true);
    }

    public static void generateNuclearDusts(final Material matInfo) {
        generateNuclearDusts(matInfo, true);
    }

    public static void generateNuclearDusts(final Material matInfo, boolean generateDehydratorRecipe) {
        generateNuclearMaterial(matInfo, false, true, false, false, true);
        if (generateDehydratorRecipe && matInfo.getFluid() != null && matInfo.getDust(0) != null) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(20))
                .itemOutputs(matInfo.getDust(1))
                .fluidInputs(matInfo.getFluidStack(144))
                .eut(matInfo.vVoltageMultiplier)
                .duration(10 * (matInfo.vVoltageMultiplier / 5))
                .addTo(chemicalDehydratorRecipes);
        } else {
            Logger.INFO(
                "Nuclear Dehydrator: Did not generate recipe for " + matInfo.getLocalizedName()
                    + " | Null Fluid? "
                    + (matInfo.getFluid() == null)
                    + " | Null Dust? "
                    + (matInfo.getDust(0) == null));
        }
    }

    public static void generateNuclearMaterial(final Material matInfo, final boolean generatePlates) {
        generateNuclearMaterial(matInfo, true, true, true, generatePlates, true);
    }

    public static void generateNuclearMaterial(final Material matInfo, final boolean generateBlock,
        final boolean generateDusts, final boolean generateIngot, final boolean generatePlates,
        final boolean disableOptionalRecipes) {
        try {

            if (generateBlock) {
                tempBlock = new BlockBaseModular(matInfo, BlockTypes.STANDARD);
            }
            if (generateDusts) {
                temp = new BaseItemDust(matInfo);
            }
            if (generateIngot) {
                temp = new BaseItemIngot(matInfo);
                temp = new BaseItemNugget(matInfo);
            }

            if (generatePlates) {
                temp = new BaseItemPlate(matInfo);
                temp = new BaseItemPlateDouble(matInfo);
                new RecipeGen_Plates(matInfo);
                new RecipeGen_Extruder(matInfo);
                new RecipeGen_Assembler(matInfo);
            }

            if (!disableOptionalRecipes) {
                new RecipeGen_ShapedCrafting(matInfo);
                new RecipeGen_Fluids(matInfo);
                new RecipeGen_MaterialProcessing(matInfo);
                new RecipeGen_Recycling(matInfo);
            }

            new RecipeGen_MetalRecipe(matInfo);
            new RecipeGen_DustGeneration(matInfo, disableOptionalRecipes);
            new RecipeGen_Plasma(matInfo);

        } catch (final Throwable t) {
            Logger.MATERIALS("" + matInfo.getLocalizedName() + " failed to generate.");
        }
    }

    public static void generateOreMaterial(final Material matInfo) {
        generateOreMaterial(matInfo, true, true, true, matInfo.getRGBA());
    }

    @SuppressWarnings("unused")
    public static void generateOreMaterial(final Material matInfo, boolean generateOre, boolean generateDust,
        boolean generateSmallTinyDusts, short[] customRGB) {
        try {

            if (matInfo == null) {
                Logger.DEBUG_MATERIALS("Invalid Material while constructing null material.");
                return;
            }

            final String unlocalizedName = matInfo.getUnlocalizedName();
            final String materialName = matInfo.getLocalizedName();
            final short[] C = customRGB;
            final Integer Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);

            if (Colour == null) {
                Logger.DEBUG_MATERIALS("Invalid Material while constructing " + materialName + ".");
                return;
            }

            int sRadiation = 0;
            if (matInfo.vRadiationLevel > 0) {
                sRadiation = matInfo.vRadiationLevel;
            }

            if (generateOre) {
                tempBlock = new BlockBaseOre(matInfo, BlockTypes.ORE);
            }

            DustState aState = new DustState(generateDust, generateSmallTinyDusts, generateSmallTinyDusts);

            if (!aState.generatesDust()) {
                if (aState.generatesSmallDust()) {
                    temp = new BaseItemComponent(matInfo, ComponentTypes.DUSTSMALL);
                }
                if (aState.generatesTinyDust()) {
                    temp = new BaseItemComponent(matInfo, ComponentTypes.DUSTTINY);
                }
            } else {
                temp = new BaseItemDust(aState, matInfo);
            }

            temp = new BaseItemCrushedOre(matInfo);
            temp = new BaseItemCentrifugedCrushedOre(matInfo);
            temp = new BaseItemPurifiedCrushedOre(matInfo);
            temp = new BaseItemImpureDust(matInfo);
            temp = new BaseItemPurifiedDust(matInfo);
            temp = new BaseItemRawOre(matInfo);

            Logger.MATERIALS(
                "Generated all ore components for " + matInfo.getLocalizedName()
                    + ", now generating processing recipes.");

            if (matInfo == FLUORIDES.FLUORITE) {
                new RecipeGen_Fluorite(matInfo);
            } else {
                new RecipeGen_Ore(matInfo);
            }

        } catch (final Throwable t) {
            Logger.MATERIALS(
                "[Error] " + (matInfo != null ? matInfo.getLocalizedName() : "Null Material") + " failed to generate.");
            t.printStackTrace();
        }
    }

    public static boolean generateOreMaterialWithAllExcessComponents(final Material matInfo) {
        try {
            if (matInfo == null) {
                Logger.DEBUG_MATERIALS("Invalid Material while constructing null material.");
                return false;
            }
            final short[] C = matInfo.getRGBA();
            final Integer Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);

            tempBlock = new BlockBaseOre(matInfo, BlockTypes.ORE);
            tempBlock = new BlockBaseModular(matInfo, BlockTypes.STANDARD);
            temp = new BaseItemIngot(matInfo);
            temp = new BaseItemDust(matInfo);
            temp = new BaseItemNugget(matInfo);
            temp = new BaseItemPlate(matInfo);
            temp = new BaseItemPlateDouble(matInfo);

            temp = new BaseItemCrushedOre(matInfo);
            temp = new BaseItemCentrifugedCrushedOre(matInfo);
            temp = new BaseItemPurifiedCrushedOre(matInfo);
            temp = new BaseItemImpureDust(matInfo);
            temp = new BaseItemPurifiedDust(matInfo);
            temp = new BaseItemRawOre(matInfo);

            Logger.MATERIALS(
                "Generated all ore & base components for " + matInfo.getLocalizedName()
                    + ", now generating processing recipes.");

            new RecipeGen_Ore(matInfo, true);
            new RecipeGen_AlloySmelter(matInfo);
            new RecipeGen_Assembler(matInfo);
            new RecipeGen_BlastSmelter(matInfo);
            new RecipeGen_MetalRecipe(matInfo);
            new RecipeGen_Extruder(matInfo);
            new RecipeGen_Fluids(matInfo);
            new RecipeGen_Plates(matInfo);
            new RecipeGen_ShapedCrafting(matInfo);
            new RecipeGen_MaterialProcessing(matInfo);
            new RecipeGen_DustGeneration(matInfo);
            new RecipeGen_Recycling(matInfo);
            new RecipeGen_Plasma(matInfo);
            return true;
        } catch (final Throwable t) {
            Logger.MATERIALS("" + matInfo.getLocalizedName() + " failed to generate.");
            t.printStackTrace();
            return false;
        }
    }
}
