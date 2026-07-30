package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BuildCraftSilicon;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTOreDictUnificator;

public class FormingPressRecipes implements Runnable {

    @Override
    public void run() {
        if (BuildCraftSilicon.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.plate, (int) (1)),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 1))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.CastIron, Shapes.plate, (int) (1)),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 1))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Gold, Shapes.plate, (int) (1)),
                    getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                .itemOutputs(getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 2))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Diamond, Shapes.plate, (int) (1)),
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
                    MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.plate, (int) (1)),
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
                    MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.plate, (int) (1)),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 13))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 16))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Diamond, Shapes.plate, (int) (1)),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 14))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 17))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Gold, Shapes.plate, (int) (1)),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 0L, 15))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 18))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.SiliconSolarGrade, 1L),
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
                MaterialLibAPI.getStack(Materials.Cupronickel, Shapes.plate, (int) (1)),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Credit_Greg_Cupronickel.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Brass, Shapes.plate, (int) (1)),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Coin_Doge.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.plate, (int) (1)),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Credit_Iron.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CastIron, Shapes.plate, (int) (1)),
                ItemList.Shape_Mold_Credit.get(0L))
            .itemOutputs(ItemList.Credit_Iron.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Brick, Shapes.dust, (int) (1)),
                ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(new ItemStack(Items.brick, 1, 0))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);
        // Bartworks Glass Tube
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Glass, Shapes.dust, (int) (2)),
                ItemList.Shape_Mold_Rod_Long.get(0L))
            .itemOutputs(new ItemStack(ItemRegistry.PUMPPARTS, 1, 0))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Magmatter, 1L),
                MaterialLibAPI.getStack(Materials.WhiteDwarfMatter, Shapes.plate, (int) (1)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 4L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.SixPhasedCopper, 4L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Eternity, 4L),
                MaterialLibAPI.getStack(Materials.BlackDwarfMatter, Shapes.plate, (int) (1)))
            .fluidInputs(MaterialUtils.fluid(Materials.UUMatter, 4_096_000L))
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
                MaterialLibAPI.getStack(Materials.TungstenTitaniumCarbide, Shapes.gearGt, 16),
                MaterialLibAPI.getStack(Materials.MARM200Steel, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.AdemicSteel, Shapes.ring, 2),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.screw, (int) (16)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SolderingAlloy,
                    FluidShapes.fluidMolten,
                    (int) (10 * INGOTS)))
            .itemOutputs(ItemList.T1Sawblade.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.MysteriousCrystal, 16),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.plate, (int) (8)),
                MaterialLibAPI.getStack(Materials.HighDurabilityCompoundSteel, Shapes.ring, 2),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.prismaticnaquadah, 16L))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Indalloy140, 10 * INGOTS))
            .itemOutputs(ItemList.T2Sawblade.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 16),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, (int) (8)),
                MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.ring, 2),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.screw, (int) (16)))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.MutatedLivingSolder, 10 * INGOTS))
            .itemOutputs(ItemList.T3Sawblade.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.TranscendentMetal, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.protohalkonite, 8),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.ring, (int) (2)),
                MaterialLibAPI.getStack(Materials.MetastableOganesson, Shapes.screw, 16))
            .fluidInputs(MaterialUtils.fluid(Materials.dimensionallyshiftedsuperfluid, 10 * INGOTS))
            .itemOutputs(ItemList.T4Sawblade.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(formingPressRecipes);

        // Hexanite Borosilicate Glass
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.stick, (int) (12)),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Netherite, 1))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 9))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.UnformedHexanite,
                    FluidShapes.fluidLiquid,
                    (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(formingPressRecipes);

        // Peace Enforcement Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tairitsu, Materials2PipeShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.itemCasing, (int) (2)))
            .itemOutputs(ItemList.PeaceEnforcementCasing.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Shijima, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(formingPressRecipes);

        // Conflict Inducement Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Shijima, 1),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.itemCasing, (int) (2)))
            .itemOutputs(ItemList.ConflictInducementCasing.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Tairitsu, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(formingPressRecipes);
    }
}
