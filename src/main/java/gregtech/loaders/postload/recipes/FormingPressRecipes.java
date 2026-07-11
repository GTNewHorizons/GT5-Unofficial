package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BuildCraftSilicon;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;

public class FormingPressRecipes implements Runnable {

    @Override
    public void run() {
        if (BuildCraftSilicon.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapePlate, (int) (1)),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 1))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.CastIron, Materials2Shapes.shapePlate, (int) (1)),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 1))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapePlate, (int) (1)),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 2))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapePlate, (int) (1)),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 3))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 2L, 4))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.shapePlate, (int) (1)),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 5))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Items.comparator, 1, 32767),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 6))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);
        }

        if (AppliedEnergistics2.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 13))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 16))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapePlate, (int) (1)),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 13))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 16))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapePlate, (int) (1)),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 14))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 17))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapePlate, (int) (1)),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 15))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 18))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.SiliconSG, 1L),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 19))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 20))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(formingPressRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Dough_Sugar.get(4L), ItemList.Shape_Mold_Cylinder.get(0L))
            .itemOutputs(ItemList.Food_Raw_Cake.get(1L))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(4)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Cupronickel, Materials2Shapes.shapePlate, (int) (1)),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Credit_Greg_Cupronickel.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.shapePlate, (int) (1)),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Coin_Doge.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapePlate, (int) (1)),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Credit_Iron.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CastIron, Materials2Shapes.shapePlate, (int) (1)),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Credit_Iron.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Brick, Materials2Shapes.shapeDust, (int) (1)),
                ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(new ItemStack(Items.brick, 1, 0))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);
        // Bartworks Glass Tube
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.shapeDust, (int) (2)),
                ItemList.Shape_Mold_Rod_Long.get(0L))
            .itemOutputs(new ItemStack(ItemRegistry.PUMPPARTS, 1, 0))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MagMatter, 1L),
                MaterialLibAPI.getStack(Materials2Materials.WhiteDwarfMatter, Materials2Shapes.shapePlate, (int) (1)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 4L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.SixPhasedCopper, 4L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Eternity, 4L),
                MaterialLibAPI.getStack(Materials2Materials.BlackDwarfMatter, Materials2Shapes.shapePlate, (int) (1)))
            .fluidInputs(Materials.UUMatter.getFluid(4_096_000L))
            .itemOutputs(ItemList.NaniteFramework.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(formingPressRecipes);

        // From ProcessingFood - foodDough forming press
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("foodDough", 1), ItemList.Shape_Mold_Bun.get(0L))
            .itemOutputs(ItemList.Food_Raw_Bun.get(1L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("foodDough", 2), ItemList.Shape_Mold_Bread.get(0L))
            .itemOutputs(ItemList.Food_Raw_Bread.get(1L))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(4)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("foodDough", 3), ItemList.Shape_Mold_Baguette.get(0L))
            .itemOutputs(ItemList.Food_Raw_Baguette.get(1L))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(4)
            .addTo(formingPressRecipes);

        // Cutting Sawblades
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.TUNGSTEN_TITANIUM_CARBIDE.getGear(16),
                GGMaterial.marM200.get(OrePrefixes.plate, 8),
                WerkstoffLoader.AdemicSteel.get(OrePrefixes.ring, 2),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.shapeScrew, (int) (16)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SolderingAlloy,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (10 * INGOTS)))
            .itemOutputs(ItemList.T1Sawblade.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.MysteriousCrystal, 16),
                MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.shapePlate, (int) (8)),
                WerkstoffLoader.HDCS.get(OrePrefixes.ring, 2),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.PrismaticNaquadah, 16L))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS))
            .itemOutputs(ItemList.T2Sawblade.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 16),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.shapePlate, (int) (8)),
                GGMaterial.tairitsu.get(OrePrefixes.ring, 2),
                MaterialLibAPI.getStack(Materials2Materials.ElectrumFlux, Materials2Shapes.shapeScrew, (int) (16)))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(10 * INGOTS))
            .itemOutputs(ItemList.T3Sawblade.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.TranscendentMetal, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ProtoHalkonite, 8),
                MaterialLibAPI.getStack(Materials2Materials.Churitsu, Materials2Shapes.shapeRing, (int) (2)),
                GGMaterial.metastableOganesson.get(OrePrefixes.screw, 16))
            .fluidInputs(Materials.DimensionallyShiftedSuperfluid.getFluid(10 * INGOTS))
            .itemOutputs(ItemList.T4Sawblade.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(formingPressRecipes);

        // Hexanite Borosilicate Glass
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.shapeStick, (int) (12)),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Netherite, 1))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 9))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.UnformedHexanite,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(formingPressRecipes);

        // Peace Enforcement Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.tairitsu.get(OrePrefixes.frameGt, 1),
                MaterialLibAPI.getStack(Materials2Materials.Churitsu, Materials2Shapes.shapeItemCasing, (int) (2)))
            .itemOutputs(ItemList.PeaceEnforcementCasing.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Shijima,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(formingPressRecipes);

        // Conflict Inducement Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Shijima, 1),
                MaterialLibAPI.getStack(Materials2Materials.Churitsu, Materials2Shapes.shapeItemCasing, (int) (2)))
            .itemOutputs(ItemList.ConflictInducementCasing.get(1))
            .fluidInputs(GGMaterial.tairitsu.getMolten(2 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(formingPressRecipes);
    }
}
