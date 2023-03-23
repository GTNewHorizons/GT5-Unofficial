/*
 * KubaTech - Gregtech Addon Copyright (C) 2022 - 2023 kuba6000 This library is free software; you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version. This library is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this library. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package kubatech.loaders;

import static kubatech.api.enums.ItemList.*;

import java.lang.reflect.InvocationTargetException;

import kubatech.Tags;
import kubatech.api.LoaderReference;
import kubatech.api.enums.ItemList;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeExterminationChamber;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeIndustrialGreenhouse;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_MegaIndustrialApiary;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dreammaster.gthandler.CustomItemList;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;

public class RecipeLoader {

    private static final Logger LOG = LogManager.getLogger(Tags.MODID + "[Recipe Loader]");
    protected static final long bitsd = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
            | GT_ModHandler.RecipeBits.BUFFERED
            | GT_ModHandler.RecipeBits.DISMANTLEABLE;

    private static int MTEID = 14201;
    private static final int MTEIDMax = 14300;

    public static void addRecipes() {
        if (registerMTE(
                ExtremeExterminationChamber,
                GT_MetaTileEntity_ExtremeExterminationChamber.class,
                "multimachine.exterminationchamber",
                "Extreme Extermination Chamber",
                LoaderReference.EnderIO)) {
            GT_ModHandler.addCraftingRecipe(
                    ItemList.ExtremeExterminationChamber.get(1),
                    bitsd,
                    new Object[] { "RCR", "CHC", "VVV", 'R', gregtech.api.enums.ItemList.Robot_Arm_EV, 'C',
                            OrePrefixes.circuit.get(Materials.Data), 'H', gregtech.api.enums.ItemList.Hull_EV, 'V',
                            GT_ModHandler.getModItem("OpenBlocks", "vacuumhopper", 1, new ItemStack(Blocks.hopper)) });
        }
        if (registerMTE(
                ExtremeIndustrialApiary,
                GT_MetaTileEntity_MegaIndustrialApiary.class,
                "multimachine.extremeapiary",
                "Industrial Apicultural Acclimatiser and Drone Domestication Station",
                LoaderReference.Forestry)) {
            GT_Values.RA.addAssemblylineRecipe(
                    gregtech.api.enums.ItemList.Machine_IndustrialApiary.get(1),
                    10000,
                    new Object[] { gregtech.api.enums.ItemList.Machine_IndustrialApiary.get(64L),
                            gregtech.api.enums.ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64L),
                            gregtech.api.enums.ItemList.IndustrialApiary_Upgrade_STABILIZER.get(64L),
                            gregtech.api.enums.ItemList.Robot_Arm_UV.get(16L),
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L }, },
                    new FluidStack[] { FluidRegistry.getFluidStack("molten.indalloy140", 28800),
                            FluidRegistry.getFluidStack("for.honey", 20000) },
                    ExtremeIndustrialApiary.get(1),
                    6000,
                    2_048_000);
        }
        if (registerMTEUsingID(
                12_792,
                ExtremeIndustrialGreenhouse,
                GT_MetaTileEntity_ExtremeIndustrialGreenhouse.class,
                "multimachine.extremegreenhouse",
                "Extreme Industrial Greenhouse",
                true /* IC2 is always loaded */)) {
            GT_ModHandler.addCraftingRecipe(
                    ExtremeIndustrialGreenhouse.get(1),
                    bitsd,
                    new Object[] { "AZA", "BRB", "AZA", 'B', gregtech.api.enums.ItemList.Casing_CleanStainlessSteel,
                            'R',
                            GT_ModHandler
                                    .getModItem("EnderIO", "blockFarmStation", 1, new ItemStack(Items.diamond_hoe)),
                            'A',
                            LoaderReference.GTNHCoreMod ? CustomItemList.AcceleratorIV.get(1)
                                    : gregtech.api.enums.ItemList.Robot_Arm_IV,
                            'Z', OrePrefixes.circuit.get(Materials.Ultimate) });
        }
        RegisterTeaLine();
        if (MTEID > MTEIDMax + 1) throw new RuntimeException("MTE ID's");
    }

    private static boolean registerMTE(ItemList item, Class<? extends MetaTileEntity> mte, String aName,
            String aNameRegional) {
        return registerMTE(item, mte, aName, aNameRegional, true);
    }

    private static boolean registerMTE(ItemList item, Class<? extends MetaTileEntity> mte, String aName,
            String aNameRegional, boolean... deps) {
        boolean dep = true;
        for (boolean i : deps) if (!i) {
            dep = false;
            break;
        }
        return registerMTE(item, mte, aName, aNameRegional, dep);
    }

    private static boolean registerMTE(ItemList item, Class<? extends MetaTileEntity> mte, String aName,
            String aNameRegional, boolean dep) {
        if (MTEID > MTEIDMax) throw new RuntimeException("MTE ID's");
        registerMTEUsingID(MTEID, item, mte, aName, aNameRegional, dep);
        MTEID++;
        return dep;
    }

    private static boolean registerMTEUsingID(int ID, ItemList item, Class<? extends MetaTileEntity> mte, String aName,
            String aNameRegional, boolean dep) {
        if (dep) {
            try {
                item.set(
                        mte.getConstructor(int.class, String.class, String.class).newInstance(ID, aName, aNameRegional)
                                .getStackForm(1));
            } catch (InvocationTargetException ex) {
                Throwable original_ex = ex.getCause();
                if (original_ex instanceof RuntimeException) throw (RuntimeException) original_ex;
                throw new RuntimeException(original_ex.getMessage());
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return dep;
    }

    private static boolean lateRecipesInitialized = false;

    public static void addRecipesLate() {
        // Runs on server start
        if (lateRecipesInitialized) return;
        lateRecipesInitialized = true;

        MobRecipeLoader.generateMobRecipeMap();
        MobRecipeLoader.processMobRecipeMap();
    }

    private static void RegisterTeaLine() {
        // TEA LINE //
        if (LoaderReference.GTPlusPlus && LoaderReference.HarvestCraft) {
            CORE.RA.addDehydratorRecipe(
                    new ItemStack[] { GameRegistry.findItemStack("harvestcraft", "tealeafItem", 1) },
                    null,
                    null,
                    new ItemStack[] { TeaLeafDehydrated.get(1) },
                    null,
                    100,
                    32);
            CORE.RA.addDehydratorRecipe(
                    new ItemStack[] { TeaLeafDehydrated.get(1) },
                    null,
                    null,
                    new ItemStack[] { WhiteTeaLeaf.get(1) },
                    null,
                    100,
                    32);
            GT_Values.RA.addMixerRecipe(
                    new ItemStack[] { TeaLeafDehydrated.get(1) },
                    new FluidStack[] { FluidRegistry.getFluidStack("water", 50) },
                    new ItemStack[] { SteamedTeaLeaf.get(1) },
                    null,
                    100,
                    32);
            CORE.RA.addDehydratorRecipe(
                    new ItemStack[] { SteamedTeaLeaf.get(1) },
                    null,
                    null,
                    new ItemStack[] { YellowTeaLeaf.get(1) },
                    null,
                    100,
                    32);
            GT_Values.RA.addBenderRecipe(TeaLeafDehydrated.get(1), RolledTeaLeaf.get(1), 100, 32);
            CORE.RA.addDehydratorRecipe(
                    new ItemStack[] { RolledTeaLeaf.get(1) },
                    null,
                    null,
                    new ItemStack[] { GreenTeaLeaf.get(1) },
                    null,
                    100,
                    32);
            GT_Values.RA.addChemicalRecipe(
                    RolledTeaLeaf.get(1),
                    GT_Utility.getIntegratedCircuit(1),
                    OxidizedTeaLeaf.get(1),
                    100,
                    32);
            CORE.RA.addDehydratorRecipe(
                    new ItemStack[] { OxidizedTeaLeaf.get(1) },
                    null,
                    null,
                    new ItemStack[] { BlackTeaLeaf.get(1) },
                    null,
                    100,
                    32);
            GT_Values.RA.addChemicalRecipe(
                    RolledTeaLeaf.get(1),
                    GT_Utility.getIntegratedCircuit(2),
                    FermentedTeaLeaf.get(1),
                    200,
                    32);
            CORE.RA.addDehydratorRecipe(
                    new ItemStack[] { FermentedTeaLeaf.get(1) },
                    null,
                    null,
                    new ItemStack[] { PuerhTeaLeaf.get(1) },
                    null,
                    100,
                    32);
            GT_Values.RA.addCutterRecipe(
                    new ItemStack[] { TeaLeafDehydrated.get(1) },
                    new ItemStack[] { BruisedTeaLeaf.get(1) },
                    100,
                    32,
                    false);
            GT_Values.RA.addChemicalRecipe(
                    BruisedTeaLeaf.get(1),
                    GT_Utility.getIntegratedCircuit(1),
                    PartiallyOxidizedTeaLeaf.get(1),
                    50,
                    32);
            CORE.RA.addDehydratorRecipe(
                    new ItemStack[] { PartiallyOxidizedTeaLeaf.get(1) },
                    null,
                    null,
                    new ItemStack[] { OolongTeaLeaf.get(1) },
                    null,
                    100,
                    32);

            // Tea Assembly
            GameRegistry.addSmelting(BlackTeaLeaf.get(1), BlackTea.get(1), 10);
            GT_Values.RA.addMixerRecipe(
                    new ItemStack[] { BlackTea.get(1), GameRegistry.findItemStack("harvestcraft", "limejuiceItem", 1) },
                    null,
                    new ItemStack[] { EarlGrayTea.get(1) },
                    null,
                    100,
                    32);
            GameRegistry.addSmelting(GreenTeaLeaf.get(1), GreenTea.get(1), 10);
            GT_Values.RA.addMixerRecipe(
                    new ItemStack[] { BlackTea.get(1) },
                    new FluidStack[] { FluidRegistry.getFluidStack("potion.lemonjuice", 1000) },
                    new ItemStack[] { LemonTea.get(1) },
                    null,
                    100,
                    32);
            GT_Values.RA.addMixerRecipe(
                    new ItemStack[] { BlackTea.get(1) },
                    new FluidStack[] { FluidRegistry.getFluidStack("milk", 1000) },
                    new ItemStack[] { MilkTea.get(1) },
                    null,
                    100,
                    32);
            GameRegistry.addSmelting(OolongTeaLeaf.get(1), OolongTea.get(1), 10);
            GT_Values.RA.addMixerRecipe(
                    new ItemStack[] { GameRegistry.findItemStack("harvestcraft", "peppermintItem", 1) },
                    new FluidStack[] { FluidRegistry.getFluidStack("water", 1000) },
                    new ItemStack[] { PeppermintTea.get(1) },
                    null,
                    100,
                    32);
            GameRegistry.addSmelting(PuerhTeaLeaf.get(1), PuerhTea.get(1), 10);
            GameRegistry.addSmelting(WhiteTeaLeaf.get(1), WhiteTea.get(1), 10);
            GameRegistry.addSmelting(YellowTeaLeaf.get(1), YellowTea.get(1), 10);
        }
        if (LoaderReference.Avaritia && LoaderReference.GTNHCoreMod) {
            GT_Values.RA.addAssemblylineRecipe(
                    TeaAcceptorResearchNote.get(1),
                    10000,
                    new Object[] { LegendaryUltimateTea.get(0),
                            GameRegistry.findItemStack("Avaritia", "Neutronium_Compressor", 1),
                            gregtech.api.enums.ItemList.Quantum_Tank_EV.get(1),
                            CustomItemList.FluidExtractorUHV.get(10),
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                            new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L }, },
                    new FluidStack[] { FluidRegistry.getFluidStack("molten.indalloy140", 28800) },
                    TeaAcceptor.get(1),
                    6000,
                    2_048_000);
        }
    }
}
