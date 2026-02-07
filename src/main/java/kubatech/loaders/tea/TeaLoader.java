package kubatech.loaders.tea;

import static gregtech.api.enums.ItemList.FluidExtractorUHV;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static kubatech.api.enums.ItemList.BlackTea;
import static kubatech.api.enums.ItemList.BlackTeaLeaf;
import static kubatech.api.enums.ItemList.BruisedTeaLeaf;
import static kubatech.api.enums.ItemList.EarlGrayTea;
import static kubatech.api.enums.ItemList.FermentedTeaLeaf;
import static kubatech.api.enums.ItemList.GreenTea;
import static kubatech.api.enums.ItemList.GreenTeaLeaf;
import static kubatech.api.enums.ItemList.LegendaryUltimateTea;
import static kubatech.api.enums.ItemList.LemonTea;
import static kubatech.api.enums.ItemList.MilkTea;
import static kubatech.api.enums.ItemList.OolongTea;
import static kubatech.api.enums.ItemList.OolongTeaLeaf;
import static kubatech.api.enums.ItemList.OxidizedTeaLeaf;
import static kubatech.api.enums.ItemList.PartiallyOxidizedTeaLeaf;
import static kubatech.api.enums.ItemList.PeppermintTea;
import static kubatech.api.enums.ItemList.PuerhTea;
import static kubatech.api.enums.ItemList.PuerhTeaLeaf;
import static kubatech.api.enums.ItemList.RolledTeaLeaf;
import static kubatech.api.enums.ItemList.SteamedTeaLeaf;
import static kubatech.api.enums.ItemList.TeaAcceptor;
import static kubatech.api.enums.ItemList.TeaAcceptorResearchNote;
import static kubatech.api.enums.ItemList.TeaLeafDehydrated;
import static kubatech.api.enums.ItemList.WhiteTea;
import static kubatech.api.enums.ItemList.WhiteTeaLeaf;
import static kubatech.api.enums.ItemList.YellowTea;
import static kubatech.api.enums.ItemList.YellowTeaLeaf;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;
import kubatech.loaders.item.kubaitem.items.ItemPlaceHolder;
import kubatech.loaders.tea.components.Tea;
import tconstruct.library.crafting.DryingRackRecipes;

public class TeaLoader {

    public static Tea BLACK_TEA;
    public static Tea EARL_GRAY_TEA;
    public static Tea GREEN_TEA;
    public static Tea LEMON_TEA;
    public static Tea MILK_TEA;
    public static Tea OOLONG_TEA;
    public static Tea PEPPERMINT_TEA;
    public static Tea PUERH_TEA;
    public static Tea WHITE_TEA;
    public static Tea YELLOW_TEA;

    public static TeaTablet TABLET;

    public static void run() {
        Tea.init();

        BLACK_TEA = Tea.createTea("black")
            .setCustomCup(BlackTea.get(1L));
        EARL_GRAY_TEA = Tea.createTea("earl_gray")
            .setCustomCup(EarlGrayTea.get(1L));
        GREEN_TEA = Tea.createTea("green")
            .setCustomCup(GreenTea.get(1L));
        LEMON_TEA = Tea.createTea("lemon")
            .setCustomCup(LemonTea.get(1L));
        MILK_TEA = Tea.createTea("milk")
            .setCustomCup(MilkTea.get(1L));
        OOLONG_TEA = Tea.createTea("oolong")
            .setCustomCup(OolongTea.get(1L));
        PEPPERMINT_TEA = Tea.createTea("peppermint")
            .setCustomCup(PeppermintTea.get(1L));
        PUERH_TEA = Tea.createTea("pu-erh")
            .setCustomCup(PuerhTea.get(1L));
        WHITE_TEA = Tea.createTea("white")
            .setCustomCup(WhiteTea.get(1L));
        YELLOW_TEA = Tea.createTea("yellow")
            .setCustomCup(YellowTea.get(1L));

        TABLET = new TeaTablet();
        GameRegistry.registerItem(TABLET, "tea_tablet");

        Tea.finish();
    }

    // dev helper
    private static ItemStack getModItemOrPlaceholder(String modID, String itemName, int stackSize, int meta) {
        ItemStack stack = GameRegistry.findItemStack(modID, itemName, stackSize);
        if (stack == null) {
            stack = ItemPlaceHolder.getItem(modID, itemName, meta);
        }
        return stack;
    }

    private static ItemStack getModItemOrPlaceholder(String modID, String itemName, int stackSize) {
        return getModItemOrPlaceholder(modID, itemName, stackSize, 0);
    }

    private static ItemStack getModItemOrPlaceholder(String modID, String itemName) {
        return getModItemOrPlaceholder(modID, itemName, 1, 0);
    }

    public static void registerTeaLine() {
        // TEA LINE //
        // if (PamsHarvestCraft.isModLoaded()) {

        // empty cup
        GTValues.RA.stdBuilder()
            .itemInputs(gregtech.api.enums.ItemList.Shape_Mold_Cylinder.get(1L))
            .fluidInputs(Materials.Glass.getFluid(144L))
            .itemOutputs(Tea.EMPTY_CUP.copy())
            .eut(TierEU.RECIPE_LV)
            .duration(10 * SECONDS)
            .addTo(fluidSolidifierRecipes);

        // empty bucket
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.bucket))
            .fluidInputs(Materials.Glass.getFluid(1000L))
            .itemOutputs(Tea.EMPTY_CUP.copy())
            .eut(TierEU.RECIPE_LV)
            .duration(10 * SECONDS)
            .addTo(fluidSolidifierRecipes);

        // base tea processing
        GTValues.RA.stdBuilder()
            .itemInputs(getModItemOrPlaceholder("harvestcraft", "tealeafItem", 1))
            .itemOutputs(TeaLeafDehydrated.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // drying rack alternative
        DryingRackRecipes.addDryingRecipe(
            getModItemOrPlaceholder("harvestcraft", "tealeafItem", 1),
            50 * SECONDS,
            TeaLeafDehydrated.get(1));

        GTValues.RA.stdBuilder()
            .itemInputs(TeaLeafDehydrated.get(1))
            .itemOutputs(WhiteTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // drying rack alternative
        DryingRackRecipes.addDryingRecipe(TeaLeafDehydrated.get(1), 50 * SECONDS, WhiteTeaLeaf.get(1));

        GTValues.RA.stdBuilder()
            .itemInputs(TeaLeafDehydrated.get(1))
            .itemOutputs(SteamedTeaLeaf.get(1))
            .fluidInputs(Materials.Water.getFluid(50))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(SteamedTeaLeaf.get(1))
            .itemOutputs(YellowTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // drying rack alternative
        DryingRackRecipes.addDryingRecipe(SteamedTeaLeaf.get(1), 50 * SECONDS, YellowTeaLeaf.get(1));

        GTValues.RA.stdBuilder()
            .itemInputs(TeaLeafDehydrated.get(1), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(RolledTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(RolledTeaLeaf.get(1))
            .itemOutputs(GreenTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // drying rack alternative
        DryingRackRecipes.addDryingRecipe(RolledTeaLeaf.get(1), 50 * SECONDS, GreenTeaLeaf.get(1));

        GTValues.RA.stdBuilder()
            .itemInputs(RolledTeaLeaf.get(1), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(OxidizedTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(OxidizedTeaLeaf.get(1))
            .itemOutputs(BlackTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // drying rack alternative
        DryingRackRecipes.addDryingRecipe(OxidizedTeaLeaf.get(1), 50 * SECONDS, BlackTeaLeaf.get(1));

        GTValues.RA.stdBuilder()
            .itemInputs(RolledTeaLeaf.get(1), GTUtility.getIntegratedCircuit(2))
            .itemOutputs(FermentedTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(10 * SECONDS)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(FermentedTeaLeaf.get(1))
            .itemOutputs(PuerhTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // drying rack alternative
        DryingRackRecipes.addDryingRecipe(FermentedTeaLeaf.get(1), 50 * SECONDS, PuerhTeaLeaf.get(1));

        GTValues.RA.stdBuilder()
            .itemInputs(TeaLeafDehydrated.get(1))
            .itemOutputs(BruisedTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(BruisedTeaLeaf.get(1), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(PartiallyOxidizedTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(2 * SECONDS + 10 * TICKS)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(PartiallyOxidizedTeaLeaf.get(1))
            .itemOutputs(OolongTeaLeaf.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // drying rack alternative
        DryingRackRecipes.addDryingRecipe(PartiallyOxidizedTeaLeaf.get(1), 50 * SECONDS, OolongTeaLeaf.get(1));

        // Tea Assembling

        GTValues.RA.stdBuilder()
            .itemInputs(BlackTeaLeaf.get(1), Tea.EMPTY_CUP.copy())
            .itemOutputs(BLACK_TEA.coldCup.copy())
            .eut(TierEU.RECIPE_LV)
            .duration(2 * SECONDS)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(BLACK_TEA.cup.copy(), getModItemOrPlaceholder("harvestcraft", "limejuiceItem", 1))
            .itemOutputs(EARL_GRAY_TEA.cup.copy())
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GreenTeaLeaf.get(1), Tea.EMPTY_CUP.copy())
            .itemOutputs(GREEN_TEA.coldCup.copy())
            .eut(TierEU.RECIPE_LV)
            .duration(2 * SECONDS)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(BLACK_TEA.cup.copy())
            .itemOutputs(LEMON_TEA.cup.copy())
            .fluidInputs(FluidRegistry.getFluidStack("potion.lemonjuice", 10))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(BLACK_TEA.cup.copy())
            .itemOutputs(MILK_TEA.cup.copy())
            .fluidInputs(Materials.Milk.getFluid(100))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(OolongTeaLeaf.get(1), Tea.EMPTY_CUP.copy())
            .itemOutputs(OOLONG_TEA.coldCup.copy())
            .eut(TierEU.RECIPE_LV)
            .duration(2 * SECONDS)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItemOrPlaceholder("harvestcraft", "peppermintItem", 1))
            .itemOutputs(PEPPERMINT_TEA.cup.copy())
            .fluidInputs(Materials.Water.getFluid(1_000))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(PuerhTeaLeaf.get(1), Tea.EMPTY_CUP.copy())
            .itemOutputs(PUERH_TEA.coldCup.copy())
            .eut(TierEU.RECIPE_LV)
            .duration(2 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WhiteTeaLeaf.get(1), Tea.EMPTY_CUP.copy())
            .itemOutputs(WHITE_TEA.coldCup.copy())
            .eut(TierEU.RECIPE_LV)
            .duration(2 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(YellowTeaLeaf.get(1), Tea.EMPTY_CUP.copy())
            .itemOutputs(YELLOW_TEA.coldCup.copy())
            .eut(TierEU.RECIPE_LV)
            .duration(2 * SECONDS)
            .addTo(mixerRecipes);

        // smelting cold to hot tea
        for (Tea tea : Tea.teas.values()) {
            GameRegistry.addSmelting(tea.coldCup.copy(), tea.cup.copy(), 10);
        }

        // }
        // if (Avaritia.isModLoaded() && NewHorizonsCoreMod.isModLoaded()) {
        // Tea Acceptor
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, TeaAcceptorResearchNote.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 40 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                LegendaryUltimateTea.get(0),
                gregtech.api.enums.ItemList.Machine_Multi_NeutroniumCompressor.get(1),
                gregtech.api.enums.ItemList.Quantum_Tank_EV.get(1),
                FluidExtractorUHV.get(10),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 16L })
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(3 * STACKS + 8 * INGOTS))
            .itemOutputs(TeaAcceptor.get(1))
            .eut(TierEU.RECIPE_UHV)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);
        // }
    }

}
