package goodgenerator.loader;

import static goodgenerator.items.MyMaterial.*;
import static goodgenerator.main.GG_Config_Loader.EnableNaquadahRework;
import static gregtech.common.items.GT_MetaGenerated_Item_01.registerCauldronCleaningFor;

import java.lang.reflect.*;
import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.PlatinumSludgeOverHaul;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.crossmod.LoadedList;
import goodgenerator.main.GoodGenerator;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.*;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GT_Bees;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.lib.CORE;

@SuppressWarnings("deprecation")
public class NaquadahReworkRecipeLoader {

    public static void RecipeLoad() {

        if (!EnableNaquadahRework) return;

        if (LoadedList.GTPP) {
            try {
                // Naquadah (UEV)
                CORE.RA.addQuantumTransformerRecipe(
                        new ItemStack[] { naquadahEarth.get(OrePrefixes.dust, 32), Materials.Sodium.getDust(64),
                                Materials.Carbon.getDust(1),
                                GT_Utility.copyAmount(0, GenericChem.mSimpleNaquadahCatalyst) },
                        new FluidStack[] { Materials.Hydrogen.getGas(64000L), Materials.Fluorine.getGas(64000L),
                                Materials.Oxygen.getGas(100L) },
                        new FluidStack[] {},
                        new ItemStack[] { inertNaquadah.get(OrePrefixes.dust, 1), Materials.Titanium.getDust(64),
                                Materials.Adamantium.getDust(64), Materials.Gallium.getDust(64) },
                        new int[] { 2500, 2500, 2500, 2500 },
                        10 * 20,
                        (int) GT_Values.VP[10],
                        2);
                // Enriched Naquadah (UIV)
                CORE.RA.addQuantumTransformerRecipe(
                        new ItemStack[] { enrichedNaquadahEarth.get(OrePrefixes.dust, 32), Materials.Zinc.getDust(64),
                                Materials.Carbon.getDust(1),
                                GT_Utility.copyAmount(0, GenericChem.mSimpleNaquadahCatalyst) },
                        new FluidStack[] { Materials.SulfuricAcid.getFluid(16000), Materials.Oxygen.getGas(100L) },
                        new FluidStack[] { wasteLiquid.getFluidOrGas(32000) },
                        new ItemStack[] { inertEnrichedNaquadah.get(OrePrefixes.dust, 1),
                                Materials.Trinium.getDust(64), },
                        new int[] { 3300, 3300, 3300 },
                        10 * 20,
                        (int) GT_Values.VP[11],
                        2);
                // Naquadria (UMV)
                CORE.RA.addQuantumTransformerRecipe(
                        new ItemStack[] { naquadriaEarth.get(OrePrefixes.dust, 32), Materials.Magnesium.getDust(64),
                                GT_Utility.copyAmount(0, GenericChem.mAdvancedNaquadahCatalyst) },
                        new FluidStack[] { Materials.PhosphoricAcid.getFluid(16000),
                                Materials.SulfuricAcid.getFluid(16000), Materials.Oxygen.getGas(100L) },
                        new FluidStack[] {},
                        new ItemStack[] { inertNaquadria.get(OrePrefixes.dust, 1), Materials.Barium.getDust(64),
                                Materials.Indium.getDust(64), ItemList.NaquadriaSupersolid.get(1) },
                        new int[] { 2500, 2500, 2500, 2500 },
                        5 * 20,
                        (int) GT_Values.VP[12],
                        3);
                // Activate Them
                MyRecipeAdder.instance.addNeutronActivatorRecipe(
                        new FluidStack[] { Materials.Nickel.getPlasma(144 * 16) },
                        new ItemStack[] { inertNaquadah.get(OrePrefixes.dust, 64),
                                inertNaquadah.get(OrePrefixes.dust, 32) },
                        new FluidStack[] { Materials.Naquadah.getMolten(144 * 9216) },
                        new ItemStack[] { Materials.Nickel.getDust(16) },
                        2000,
                        600,
                        500);
                MyRecipeAdder.instance.addNeutronActivatorRecipe(
                        new FluidStack[] { Materials.Titanium.getPlasma(16 * 144) },
                        new ItemStack[] { inertEnrichedNaquadah.get(OrePrefixes.dust, 64),
                                inertEnrichedNaquadah.get(OrePrefixes.dust, 32) },
                        new FluidStack[] { Materials.NaquadahEnriched.getMolten(144 * 9216) },
                        new ItemStack[] { Materials.Titanium.getDust(16) },
                        2000,
                        900,
                        850);
                MyRecipeAdder.instance.addNeutronActivatorRecipe(
                        new FluidStack[] { Materials.Americium.getPlasma(144 * 16) },
                        new ItemStack[] { inertNaquadria.get(OrePrefixes.dust, 64),
                                inertNaquadria.get(OrePrefixes.dust, 32) },
                        new FluidStack[] { Materials.Naquadria.getMolten(144 * 9216) },
                        new ItemStack[] { Materials.Americium.getDust(16) },
                        2000,
                        1100,
                        1080);
            } catch (Throwable t) {
                // Cry about it
            }
        }

        // Fix shit
        GT_Values.RA.addPulveriserRecipe(
                lowQualityNaquadriaSolution.get(OrePrefixes.cell, 1),
                new ItemStack[] { Materials.Tin.getDust(2) },
                new int[] { 10000 },
                334,
                4);

        // Naquadah Rework Line
        GT_Values.RA.addBlastRecipe(
                naquadahEarth.get(OrePrefixes.dust, 2),
                GT_Utility.getIntegratedCircuit(1),
                fluoroantimonicAcid.getFluidOrGas(3000),
                lowQualityNaquadahEmulsion.getFluidOrGas(2000),
                titaniumTrifluoride.get(OrePrefixes.dust, 4),
                null,
                100,
                480,
                3000);

        // TiF3 + 3H = Ti + 3HF
        GT_Values.RA.addBlastRecipe(
                titaniumTrifluoride.get(OrePrefixes.dust, 4),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(3000),
                Materials.HydrofluoricAcid.getFluid(3000),
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Titanium, 1),
                null,
                120,
                1920,
                2000);

        GT_Values.RA.addChemicalRecipe(
                GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 1)),
                Materials.Hydrogen.getCells(8),
                FluidRegistry.getFluidStack("seedoil", 3000),
                towEthyl1Hexanol.getFluidOrGas(1000),
                ItemList.Cell_Empty.get(8),
                400,
                480);

        // 2C8H18O + H3PO4 =Na,C2H6O= C16H35O3P + 2H2O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 2) },
                new FluidStack[] { towEthyl1Hexanol.getFluidOrGas(2000), Materials.PhosphoricAcid.getFluid(1000),
                        Materials.Ethanol.getFluid(2000) },
                new FluidStack[] { P507.getFluidOrGas(1000) },
                null,
                1200,
                1920);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                lowQualityNaquadahSolution.get(OrePrefixes.cell, 36),
                P507.get(OrePrefixes.cell, 4),
                null,
                fluorineRichWasteLiquid.getFluidOrGas(10000),
                naquadahAdamantiumSolution.get(OrePrefixes.cell, 30),
                ItemList.Cell_Empty.get(10),
                4000,
                1920);

        GT_Values.RA.addMultiblockChemicalRecipe(
                null,
                new FluidStack[] { P507.getFluidOrGas(4000), lowQualityNaquadahSolution.getFluidOrGas(36000), },
                new FluidStack[] { fluorineRichWasteLiquid.getFluidOrGas(10000),
                        naquadahAdamantiumSolution.getFluidOrGas(30000) },
                null,
                4000,
                1920);

        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, 40),
                null,
                fluorineRichWasteLiquid.getFluidOrGas(1500),
                wasteLiquid.getFluidOrGas(1000),
                WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 60),
                1000,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                wasteLiquid.getFluidOrGas(10000),
                new FluidStack[] { Materials.SaltWater.getFluid(3000), FluidRegistry.getFluidStack("phenol", 2000),
                        Materials.HydrochloricAcid.getFluid(5000) },
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 3),
                300,
                480);

        MyRecipeAdder.instance.addNeutronActivatorRecipe(
                new FluidStack[] { naquadahAdamantiumSolution.getFluidOrGas(3000) },
                null,
                new FluidStack[] { naquadahRichSolution.getFluidOrGas(2000) },
                new ItemStack[] { adamantine.get(OrePrefixes.dust, 4), naquadahEarth.get(OrePrefixes.dust, 2),
                        concentratedEnrichedNaquadahSludge.get(OrePrefixes.dust, 1) },
                100,
                230,
                200);

        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 27),
                null,
                naquadahRichSolution.getFluidOrGas(5000),
                P507.getFluidOrGas(1000),
                naquadahine.get(OrePrefixes.dust, 30),
                10000,
                1000,
                120,
                false);

        // NqO2 + C = CO2 + Nq
        GT_Values.RA.addBlastRecipe(
                naquadahine.get(OrePrefixes.dust, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1),
                null,
                Materials.CarbonDioxide.getGas(1000),
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Naquadah, 1),
                null,
                40,
                7680,
                5000);

        GT_Values.RA.addCentrifugeRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 27),
                GT_Utility.getIntegratedCircuit(1),
                lowQualityNaquadahEmulsion.getFluidOrGas(10000),
                lowQualityNaquadahSolution.getFluidOrGas(9000),
                galliumHydroxide.get(OrePrefixes.dust, 64),
                galliumHydroxide.get(OrePrefixes.dust, 48),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Antimony, 15),
                null,
                null,
                null,
                new int[] { 6250, 6250, 10000 },
                1000,
                1920);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { enrichedNaquadahEarth.get(OrePrefixes.dust, 4), },
                new FluidStack[] { P507.getFluidOrGas(1000), Materials.SulfuricAcid.getFluid(18000) },
                new FluidStack[] { enrichedNaquadahRichSolution.getFluidOrGas(4000), wasteLiquid.getFluidOrGas(1000) },
                new ItemStack[] { naquadahEarth.get(OrePrefixes.dust, 1), triniumSulphate.get(OrePrefixes.dust, 1) },
                400,
                1920);

        // ZnSO4 + 2H = H2SO4 + Zn
        GT_Values.RA.addChemicalRecipe(
                WerkstoffLoader.ZincSulfate.get(OrePrefixes.dust, 6),
                null,
                Materials.Hydrogen.getGas(2000),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.Zinc.getDust(1),
                30,
                7);

        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 60),
                null,
                enrichedNaquadahRichSolution.getFluidOrGas(10000),
                P507.getFluidOrGas(2500),
                concentratedEnrichedNaquadahSludge.get(OrePrefixes.dust, 8),
                10000,
                1000,
                480,
                false);

        MyRecipeAdder.instance.addNeutronActivatorRecipe(
                null,
                new ItemStack[] { concentratedEnrichedNaquadahSludge.get(OrePrefixes.dust, 16), },
                null,
                new ItemStack[] { enrichedNaquadahSulphate.get(OrePrefixes.dust, 64),
                        enrichedNaquadahSulphate.get(OrePrefixes.dust, 64),
                        enrichedNaquadahSulphate.get(OrePrefixes.dust, 37),
                        WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 64),
                        WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 64),
                        WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 12),
                        lowQualityNaquadriaSulphate.get(OrePrefixes.dust, 2), },
                120,
                480,
                460);

        // Nq+(SO4)2 + 2Zn = Nq+ + 2ZnSO4
        GT_Values.RA.addBlastRecipe(
                enrichedNaquadahSulphate.get(OrePrefixes.dust, 11),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 2),
                null,
                null,
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.NaquadahEnriched, 1),
                WerkstoffLoader.ZincSulfate.get(OrePrefixes.dust, 12),
                100,
                7680,
                7500);

        // KeSO4 + 2H = Ke + H2SO4
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { triniumSulphate.get(OrePrefixes.dust, 6), },
                new FluidStack[] { Materials.Hydrogen.getGas(2000) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(1000) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Trinium, 1), },
                120,
                480);

        GT_Values.RA.addCentrifugeRecipe(
                naquadriaEarth.get(OrePrefixes.dust, 4),
                null,
                Materials.PhosphoricAcid.getFluid(4000),
                null,
                indiumPhosphate.get(OrePrefixes.dust, 6),
                lowQualityNaquadriaPhosphate.get(OrePrefixes.dust, 4),
                null,
                null,
                null,
                null,
                new int[] { 2000, 10000 },
                400,
                122880);

        // Ga(OH)3 + 3Na = Ga + 3NaOH
        GT_Values.RA.addChemicalRecipe(
                galliumHydroxide.get(OrePrefixes.dust, 7),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 3),
                null,
                null,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 9),
                40,
                30);

        // 2InPO4 + 3Ca = 2In + Ca3(PO4)2
        GT_Values.RA.addChemicalRecipe(
                indiumPhosphate.get(OrePrefixes.dust, 12),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 3),
                null,
                null,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 5),
                20,
                30);

        GT_Values.RA.addChemicalRecipe(
                lowQualityNaquadriaPhosphate.get(OrePrefixes.dust, 10),
                Materials.SulfuricAcid.getCells(30),
                null,
                naquadriaRichSolution.getFluidOrGas(9000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Barite, 1),
                ItemList.Cell_Empty.get(30),
                1000,
                7680);

        MyRecipeAdder.instance.addNeutronActivatorRecipe(
                new FluidStack[] { naquadriaRichSolution.getFluidOrGas(9000) },
                null,
                null,
                new ItemStack[] { naquadriaSulphate.get(OrePrefixes.dust, 44),
                        lowQualityNaquadriaSulphate.get(OrePrefixes.dust, 6) },
                100,
                1100,
                1050);

        GT_Values.RA.addChemicalRecipe(
                lowQualityNaquadriaSulphate.get(OrePrefixes.dust, 3),
                Materials.Water.getCells(3),
                P507.getFluidOrGas(500),
                lowQualityNaquadriaSolution.getFluidOrGas(3500),
                ItemList.Cell_Empty.get(3),
                500,
                1920);

        GT_Values.RA.addUniversalDistillationRecipe(
                lowQualityNaquadriaSolution.getFluidOrGas(7000),
                new FluidStack[] { P507.getFluidOrGas(1000), naquadriaRichSolution.getFluidOrGas(5400),
                        Materials.DilutedSulfuricAcid.getFluid(12000) },
                enrichedNaquadahEarth.get(OrePrefixes.dust, 2),
                500,
                7680);

        // Nq*(SO4)2 + 2Mg = Nq* + 2MgSO4
        GT_Values.RA.addBlastRecipe(
                naquadriaSulphate.get(OrePrefixes.dust, 11),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2),
                null,
                null,
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Naquadria, 1),
                magnesiumSulphate.get(OrePrefixes.dust, 12),
                100,
                122880,
                9100);

        // InPO4 + Ga(OH)3 = InGaP
        GT_Values.RA.addMixerRecipe(
                indiumPhosphate.get(OrePrefixes.dust, 6),
                galliumHydroxide.get(OrePrefixes.dust, 7),
                GT_Utility.getIntegratedCircuit(2),
                null,
                null,
                null,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IndiumGalliumPhosphide, 3),
                15,
                7);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0),
                naquadahGoo.getFluidOrGas(72),
                ItemRefer.Naquadah_Mass.get(1),
                100,
                30);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0),
                enrichedNaquadahGoo.getFluidOrGas(72),
                ItemRefer.Enriched_Naquadah_Mass.get(1),
                100,
                30);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0),
                naquadriaGoo.getFluidOrGas(72),
                ItemRefer.Naquadria_Mass.get(1),
                100,
                30);

        GT_Values.RA.addPulveriserRecipe(
                ItemRefer.Naquadah_Mass.get(1),
                new ItemStack[] { naquadahEarth.get(OrePrefixes.dust, 1),
                        enrichedNaquadahEarth.get(OrePrefixes.dust, 1) },
                new int[] { 10000, 100 },
                100,
                2);

        GT_Values.RA.addPulveriserRecipe(
                ItemRefer.Enriched_Naquadah_Mass.get(1),
                new ItemStack[] { enrichedNaquadahEarth.get(OrePrefixes.dust, 1),
                        naquadriaEarth.get(OrePrefixes.dust, 1) },
                new int[] { 10000, 100 },
                100,
                2);

        GT_Values.RA.addPulveriserRecipe(
                ItemRefer.Naquadria_Mass.get(1),
                new ItemStack[] { naquadriaEarth.get(OrePrefixes.dust, 1), naquadriaEarth.get(OrePrefixes.dust, 1) },
                new int[] { 10000, 100 },
                100,
                2);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 16),
                naquadahine.get(OrePrefixes.dust, 3),
                ItemList.GalliumArsenideCrystal.get(1L),
                null,
                Materials.Argon.getGas(8000),
                null,
                ItemList.Circuit_Silicon_Ingot3.get(1),
                null,
                null,
                null,
                1000,
                7680,
                4484);

        // NqO2 + 4Na = 2Na2O + Nq
        GT_Values.RA.addChemicalRecipe(
                naquadahine.get(OrePrefixes.dust, 3),
                Materials.Sodium.getDust(4),
                null,
                null,
                Materials.Naquadah.getDust(1),
                Materials.SodiumOxide.getDust(6),
                100,
                1920);

        GT_Values.RA.addBlastRecipe(
                naquadahEarth.get(OrePrefixes.dust, 2),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Nitrogen.getGas(1000),
                null,
                Materials.Naquadah.getNuggets(1),
                null,
                2400,
                7680,
                5000);

        // C2H4 + H2O(g) = C2H6O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Ethylene.getGas(1000), FluidRegistry.getFluidStack("steam", 2000) },
                new FluidStack[] { Materials.Ethanol.getFluid(1000) },
                null,
                400,
                480);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Ethylene.getCells(1),
                GT_Utility.getIntegratedCircuit(24),
                FluidRegistry.getFluidStack("steam", 2000),
                null,
                Materials.Ethanol.getCells(1),
                null,
                400,
                480);

        GT_Values.RA.addVacuumFreezerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Naquadah, 1),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1),
                330,
                7680);
    }

    public static void SmallRecipeChange() {

        GT_Recipe tRecipe;

        tRecipe = GT_Recipe.GT_Recipe_Map.sChemicalRecipes.findRecipe(
                null,
                false,
                1 << 30,
                new FluidStack[] { Materials.SulfuricAcid.getFluid(500) },
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 16),
                ItemList.Empty_Board_Elite.get(2));
        if (tRecipe != null) {
            GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList.remove(tRecipe);
            GT_Recipe tRecipe2 = tRecipe.copy();
            tRecipe2.mInputs = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadah, 8),
                    ItemList.Empty_Board_Elite.get(2) };
            GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList.add(tRecipe2);
            GT_Recipe.GT_Recipe_Map.sChemicalRecipes.reInit();
        }

        tRecipe = GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.findRecipe(
                null,
                false,
                1 << 30,
                new FluidStack[] { Materials.SulfuricAcid.getFluid(500) },
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 16),
                ItemList.Empty_Board_Elite.get(2));
        if (tRecipe != null) {
            GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.mRecipeList.remove(tRecipe);
            GT_Recipe tRecipe2 = tRecipe.copy();
            tRecipe2.mInputs = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadah, 8),
                    ItemList.Empty_Board_Elite.get(2) };
            GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.mRecipeList.add(tRecipe2);
            GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.reInit();
        }

        tRecipe = GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.findRecipe(
                null,
                false,
                1 << 30,
                new FluidStack[] { Materials.Polybenzimidazole.getMolten(36) },
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 2),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.HSSS, 1),
                GT_Utility.getIntegratedCircuit(1));
        if (tRecipe != null) {
            GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList.remove(tRecipe);
            GT_Recipe tRecipe2 = tRecipe.copy();
            tRecipe2.mInputs = new ItemStack[] {
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 2),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NaquadahEnriched, 1) };
            GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList.add(tRecipe2);
            GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.reInit();
        }
    }

    public static void Remover() {

        if (!EnableNaquadahRework) return;

        GT_Log.out.print(GoodGenerator.MOD_ID + ": Begin to remove pure Naquadah, Enriched Naquadah and Naquadria.\n");

        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);

        // For Crusher
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if ((OreDictionary.getOreName(oreDictID).startsWith("ore")
                            || OreDictionary.getOreName(oreDictID).startsWith("crushed"))
                            && OreDictionary.getOreName(oreDictID).contains("Naq")) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.reInit();

        GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Crusher done!\n");

        // For Washer
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("crushed")
                            && OreDictionary.getOreName(oreDictID).contains("Naq")) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.reInit();

        GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Washer done!\n");

        // For Thermal Centrifuge
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("crushed")
                            && OreDictionary.getOreName(oreDictID).contains("Naq")) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.reInit();

        GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Thermal Centrifuge done!\n");

        // For Centrifuge
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList) {
            ItemStack input = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                if (input.isItemEqual(GT_Bees.combs.getStackForType(CombType.DOB))) {
                    GT_Recipe tRecipe = recipe.copy();
                    boolean modified = false;
                    for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                        if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                        if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDustTiny(1))) {
                            tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2L,
                                    naquadahEarth.get(OrePrefixes.dustTiny, 1));
                            modified = true;
                        }
                    }
                    if (modified) {
                        reAdd.add(tRecipe);
                        remove.add(recipe);
                    }
                } else for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("dustPureNaq")
                            || OreDictionary.getOreName(oreDictID).startsWith("dustImpureNaq")
                            || OreDictionary.getOreName(oreDictID).startsWith("dustSpace")
                            || OreDictionary.getOreName(oreDictID).startsWith("dustNaq")) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadahEarth.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        enrichedNaquadahEarth.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadriaEarth.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadahEarth.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        enrichedNaquadahEarth.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadriaEarth.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.reInit();

        GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Centrifuge done!\n");

        // For Hammer
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sHammerRecipes.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("crushed")
                            && OreDictionary.getOreName(oreDictID).contains("Naq")) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sHammerRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sHammerRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sHammerRecipes.reInit();

        GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Hammer done!\n");

        // For Chemical Reactor
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList) {
            if (recipe.mFluidOutputs == null) continue;
            boolean isAny = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadah.getMolten(1))
                        || recipe.mFluidOutputs[i].isFluidEqual(Materials.NaquadahEnriched.getMolten(1))
                        || recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadria.getMolten(1))) {
                    isAny = true;
                    break;
                }
            }
            if (!isAny) continue;
            GT_Recipe tRecipe = recipe.copy();
            boolean modified = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadah.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = naquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(Materials.NaquadahEnriched.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = enrichedNaquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadria.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = naquadriaGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                }
            }
            if (modified) {
                reAdd.add(tRecipe);
                remove.add(recipe);
            }
        }
        GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sChemicalRecipes.reInit();

        GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Chemical Reactor done!\n");

        // For Multi Chemical Reactor
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.mRecipeList) {
            if (recipe.mFluidOutputs == null) continue;
            boolean isAny = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadah.getMolten(1))
                        || recipe.mFluidOutputs[i].isFluidEqual(Materials.NaquadahEnriched.getMolten(1))
                        || recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadria.getMolten(1))) {
                    isAny = true;
                    break;
                }
            }
            if (!isAny) continue;
            GT_Recipe tRecipe = recipe.copy();
            boolean modified = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadah.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = naquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(Materials.NaquadahEnriched.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = enrichedNaquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadria.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = naquadriaGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                }
            }
            if (modified) {
                reAdd.add(tRecipe);
                remove.add(recipe);
            }
        }
        GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.reInit();

        GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Multi Chemical Reactor done!\n");

        if (LoadedList.GTPP) {
            // For Multi Centrifuge
            // Blame alk. She made some shit in it, NEI will break down if anyone modify the hash list directly.
            // GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mRecipeList.clear();
            // RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
            // GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
            // GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT);
            // GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.reInit();

            // For Simple Washer
            for (GT_Recipe recipe : GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.mRecipeList) {
                ItemStack input = recipe.mInputs[0];
                if (GT_Utility.isStackValid(input)) {
                    int[] oreDict = OreDictionary.getOreIDs(input);
                    for (int oreDictID : oreDict) {
                        if (OreDictionary.getOreName(oreDictID).startsWith("dustImpureNaq")) {
                            GT_Recipe tRecipe = recipe.copy();
                            boolean modified = false;
                            for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                                if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                                if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                    tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                            tRecipe.mOutputs[i].stackSize,
                                            naquadahEarth.get(OrePrefixes.dust, 1));
                                    modified = true;
                                } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                    tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                            tRecipe.mOutputs[i].stackSize,
                                            enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                    modified = true;
                                } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                    tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                            tRecipe.mOutputs[i].stackSize,
                                            naquadriaEarth.get(OrePrefixes.dust, 1));
                                    modified = true;
                                }
                            }
                            if (modified) {
                                reAdd.add(tRecipe);
                                remove.add(recipe);
                            }
                            break;
                        }
                    }
                }
            }
            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.mRecipeList.removeAll(remove);
            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.mRecipeList.addAll(reAdd);
            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.reInit();

            GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

            remove.clear();
            reAdd.clear();

            GT_Log.out.print("Simple Washer done!\n");
        }

        // For Cauldron Wash
        registerCauldronCleaningFor(Materials.Naquadah, naquadahEarth.getBridgeMaterial());
        registerCauldronCleaningFor(Materials.NaquadahEnriched, enrichedNaquadahEarth.getBridgeMaterial());
        registerCauldronCleaningFor(Materials.Naquadria, naquadriaEarth.getBridgeMaterial());
        GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace 3! ");
        GT_Log.out.print("Cauldron Wash done!\n");

        // For Crafting Table
        CraftingManager.getInstance().getRecipeList().forEach(NaquadahReworkRecipeLoader::replaceInCraftTable);

        GT_Log.out.print(GoodGenerator.MOD_ID + ": Replace Unknown! ");
        GT_Log.out.print("Crafting Table done!\n");
    }

    // I don't understand. . .
    // I use and copy some private methods in Bartworks because his system runs well.
    // Bartworks is under MIT License
    /*
     * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a
     * copy of this software and associated documentation files (the "Software"), to deal in the Software without
     * restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute,
     * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
     * subject to the following conditions: The above copyright notice and this permission notice shall be included in
     * all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
     * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
     * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
     * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
     * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
     */
    public static void replaceInCraftTable(Object obj) {

        Constructor<?> cs = null;
        PlatinumSludgeOverHaul BartObj = null;
        try {
            cs = PlatinumSludgeOverHaul.class.getDeclaredConstructor();
            cs.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (cs == null) return;

        try {
            BartObj = (PlatinumSludgeOverHaul) cs.newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Method recipeCheck = null;

        try {
            recipeCheck = PlatinumSludgeOverHaul.class.getDeclaredMethod("checkRecipe", Object.class, Materials.class);
            recipeCheck.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String inputName = "output";
        String inputItemName = "input";
        if (!(obj instanceof ShapedOreRecipe || obj instanceof ShapelessOreRecipe)) {
            if (obj instanceof ShapedRecipes || (obj instanceof ShapelessRecipes)) {
                inputName = "recipeOutput";
                inputItemName = "recipeItems";
            }
        }
        IRecipe recipe = (IRecipe) obj;
        ItemStack result = recipe.getRecipeOutput();

        Field out = FieldUtils.getDeclaredField(recipe.getClass(), inputName, true);
        if (out == null) out = FieldUtils.getField(recipe.getClass(), inputName, true);

        Field in = FieldUtils.getDeclaredField(recipe.getClass(), inputItemName, true);
        if (in == null) in = FieldUtils.getField(recipe.getClass(), inputItemName, true);
        if (in == null) return;

        // this part here is NOT MIT LICENSED BUT LICSENSED UNDER THE Apache License, Version 2.0!
        try {
            if (Modifier.isFinal(in.getModifiers())) {
                // Do all JREs implement Field with a private ivar called "modifiers"?
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                boolean doForceAccess = !modifiersField.isAccessible();
                if (doForceAccess) {
                    modifiersField.setAccessible(true);
                }
                try {
                    modifiersField.setInt(in, in.getModifiers() & ~Modifier.FINAL);
                } finally {
                    if (doForceAccess) {
                        modifiersField.setAccessible(false);
                    }
                }
            }
        } catch (NoSuchFieldException ignored) {
            // The field class contains always a modifiers field
        } catch (IllegalAccessException ignored) {
            // The modifiers field is made accessible
        }
        // END OF APACHE COMMONS COLLECTION COPY

        Object input;
        try {
            input = in.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        if (out == null || recipeCheck == null) return;

        if (GT_Utility.areStacksEqual(result, Materials.Naquadah.getDust(1), true)) {

            recipeCheck.setAccessible(true);
            boolean isOk = true;

            try {
                isOk = (boolean) recipeCheck.invoke(BartObj, input, Materials.Naquadah);
            } catch (InvocationTargetException | IllegalAccessException ignored) {}

            if (isOk) return;
            try {
                out.set(recipe, naquadahEarth.get(OrePrefixes.dust, 2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (GT_Utility.areStacksEqual(result, Materials.NaquadahEnriched.getDust(1), true)) {

            recipeCheck.setAccessible(true);
            boolean isOk = true;

            try {
                isOk = (boolean) recipeCheck.invoke(BartObj, input, Materials.NaquadahEnriched);
            } catch (InvocationTargetException | IllegalAccessException ignored) {}

            if (isOk) return;
            try {
                out.set(recipe, enrichedNaquadahEarth.get(OrePrefixes.dust, 2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (GT_Utility.areStacksEqual(result, Materials.Naquadria.getDust(1), true)) {

            recipeCheck.setAccessible(true);
            boolean isOk = true;

            try {
                isOk = (boolean) recipeCheck.invoke(BartObj, input, Materials.Naquadria);
            } catch (InvocationTargetException | IllegalAccessException ignored) {}

            if (isOk) return;
            try {
                out.set(recipe, naquadriaEarth.get(OrePrefixes.dust, 2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
