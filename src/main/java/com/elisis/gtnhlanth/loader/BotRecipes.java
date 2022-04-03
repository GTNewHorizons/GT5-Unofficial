package com.elisis.gtnhlanth.loader;

import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Acetylhydrazine;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.AmmoniaBoronfluorideSolution;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.AmmoniumDinitramide;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.AmmoniumNitrate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.AmmoniumNnitrourethane;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.BoronTrifluoride;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.BoronTrioxide;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.DimethylSulfate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.DinitrogenPentoxide;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.EthylDinitrocarbamate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.EthylNnitrocarbamate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Ethylcarbamate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Ethylchloroformate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Formaldehyde;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Hydrazine;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.HydrogenPeroxide;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.LMP103S;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Monomethylhydrazine;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.MonomethylhydrazineFuelMix;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Nitromethane;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.NitroniumTetrafluoroborate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.OXylene;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Phosgene;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.PhthalicAnhydride;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.SodiumFluoride;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.SodiumTetrafluoroborate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.SodiumTungstate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.TertButylbenzene;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Tetrafluoroborate;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.Trinitramid;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.TungstenTrioxide;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.TungsticAcid;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.TwoTertButylAnthrahydroquinone;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.TwoTertButylAnthraquinone;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.UnsymmetricalDimethylhydrazine;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.UnsymmetricalDimethylhydrazineFuelMix;
import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.VanadiumPentoxide;
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustTiny;
import static gregtech.api.enums.OrePrefixes.ingotHot;
import static gregtech.api.enums.OrePrefixes.item;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

import com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
/*
 * 
 * Originally authored by botn365 under the MIT License. See BotdustriesLICENSE
 *
 */

public class BotRecipes {

	public static void addGTRecipe() {
        ItemStack C1 = GT_Utility.getIntegratedCircuit(1);
        ItemStack C2 = GT_Utility.getIntegratedCircuit(2);
        ItemStack C24 = GT_Utility.getIntegratedCircuit(24);

        GT_Values.RA.addChemicalRecipe(Materials.Calcite.getDust(1), Materials.Empty.getCells(1),
                Materials.HydrochloricAcid.getFluid(2000), Materials.Water.getFluid(1000),
                Materials.CarbonDioxide.getCells(1), WerkstoffLoader.CalciumChloride.get(dust, 3), 80, 120);

        // tungsten chain
        FluidStack sodiumTungsten = SodiumTungstate.getFluidOrGas(4000);
        ItemStack scheelite = Materials.Scheelite.getDust(7);

        GT_Values.RA.addAutoclaveRecipe(Materials.Tungstate.getDust(7), Materials.SodiumHydroxide.getDust(4),
                Materials.Water.getFluid(4000), sodiumTungsten, Materials.Lithium.getDust(2), 10000, 100, 1920, false);

        GT_Values.RA.addAutoclaveRecipe(WerkstoffLoader.Huebnerit.get(dust, 5), Materials.SodiumHydroxide.getDust(4),
                Materials.Water.getFluid(4000), sodiumTungsten, Materials.Manganese.getDust(1), 10000, 100, 1920, false);

        GT_Values.RA.addAutoclaveRecipe(WerkstoffLoader.Ferberite.get(dust, 5), Materials.SodiumHydroxide.getDust(4),
                Materials.Water.getFluid(4000), sodiumTungsten, Materials.Iron.getDust(1), 10000, 100, 1920, false);

        ItemStack Ca2Cl = WerkstoffLoader.CalciumChloride.get(dust, 6);
        GT_Values.RA.addChemicalRecipe(Ca2Cl, null, sodiumTungsten, null,
                scheelite, Materials.Salt.getDust(8), 100, 480);

        Ca2Cl.stackSize = 6;
        ItemStack H2WO4 = TungsticAcid.get(dust, 1);
        GT_Values.RA.addChemicalRecipe(scheelite, null, Materials.HydrochloricAcid.getFluid(4000), null,
                H2WO4, Ca2Cl, 50, 1920);

        ItemStack WO3 = TungstenTrioxide.get(dust, 1);
        GT_Values.RA.addBlastRecipe(H2WO4, null, null, null, WO3, null,
                200, 480, 1200);

//        ItemStack WO3Fe = TungstenSteelOxide.get(dust, 2);
//        GT_Values.RA.addMixerRecipe(WO3, Materials.Steel.getDust(1), null, null, null, null,
//                WO3Fe, 100, 1920);

        GT_Values.RA.addBlastRecipe(WO3,C2,Materials.Hydrogen.getGas(3000),
                null,Materials.Tungsten.getDust(1),null,100,1920,1000);

        WO3.stackSize = 2;
        GT_Values.RA.addBlastRecipe(WO3, Materials.Carbon.getDust(3), null, Materials.CarbonDioxide.getGas(6000),
                GT_OreDictUnificator.get(ingotHot, Materials.Tungsten, 2L), null, 8000, 1920, 3000);

//        WO3Fe.stackSize = 4;
//        GT_Values.RA.addBlastRecipe(WO3Fe, Materials.Carbon.getDust(3), Materials.Nitrogen.getGas(4000), Materials.CarbonDioxide.getGas(6000),
//                GT_OreDictUnificator.get(ingotHot, Materials.TungstenSteel, 4L), null, 10000, 1920, 3000);

        //rocket fuels
        //LMP103S
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), C2, Materials.Chlorine.getGas(2000),
                null, Phosgene.get(cell, 1), 50, 480);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), C2, Materials.CarbonMonoxide.getGas(1000),
            null, Phosgene.get(cell, 1),  Materials.Empty.getCells(1),50, 480);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), C2, Materials.Chlorine.getGas(2000),
            BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(1000), null,  Materials.Empty.getCells(1),50, 480);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), C2, Materials.CarbonMonoxide.getGas(1000),
            BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(1000), null,  Materials.Empty.getCells(2),50, 480);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), Materials.Chlorine.getCells(2), null,
            null, Phosgene.get(cell, 1),  Materials.Empty.getCells(2), 50, 480);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), Materials.CarbonMonoxide.getCells(1), null,
            null, Phosgene.get(cell, 1),  Materials.Empty.getCells(2),50, 480);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), Materials.Chlorine.getCells(2), null,
            BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(1000), null,  Materials.Empty.getCells(3),50, 480);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), Materials.CarbonMonoxide.getCells(1), null,
            BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(1000), null,  Materials.Empty.getCells(3),50, 480);

        GT_Values.RA.addChemicalRecipe(Phosgene.get(cell, 1), C2, Materials.Ethanol.getFluid(1000),
                Materials.HydrochloricAcid.getGas(1000), Ethylchloroformate.get(cell, 1), 20, 1920);

        GT_Values.RA.addChemicalRecipe(Ethylchloroformate.get(cell, 1), C2, Materials.Ammonia.getGas(2000),
                WerkstoffLoader.AmmoniumChloride.getFluidOrGas(1000), Ethylcarbamate.get(cell, 1), 200, 120);

        GT_Values.RA.addChemicalRecipe(Ethylcarbamate.get(cell, 1), C2, Materials.NitricAcid.getFluid(1000),
                Materials.Water.getFluid(1000), EthylNnitrocarbamate.get(cell, 1), 40, 1024);

        GT_Values.RA.addChemicalRecipe(EthylNnitrocarbamate.get(cell, 1), C2, Materials.Ammonia.getGas(1000),
                null, AmmoniumNnitrourethane.get(cell, 1), 40, 1920);

        GT_Values.RA.addChemicalRecipe(AmmoniumNnitrourethane.get(cell, 1), DinitrogenPentoxide.get(dust, 1), null, null,
                EthylDinitrocarbamate.get(cell, 1), AmmoniumNitrate.get(dust, 1), 200, 480);

        GT_Values.RA.addChemicalRecipe(EthylDinitrocarbamate.get(cell, 1), C2, Materials.Ammonia.getGas(2000),
                Ethylcarbamate.getFluidOrGas(980), AmmoniumDinitramide.get(cell, 1), 200, 1920);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{C24},
                new FluidStack[]{
                        AmmoniumDinitramide.getFluidOrGas(6000),
                        Materials.Methanol.getFluid(2000),
                        Materials.Ammonia.getGas(500),
                        Materials.Water.getFluid(1500)},
                new FluidStack[]{LMP103S.getFluidOrGas(10000)},
                null,
                1200, 1920);

        GT_Values.RA.addChemicalRecipe(Materials.PhosphorousPentoxide.getDust(1), C2, Materials.NitricAcid.getFluid(12000),
                Materials.PhosphoricAcid.getFluid(4000), DinitrogenPentoxide.get(dust, 6), 200, 1920);

        GT_Values.RA.addDistilleryRecipe(C2, Materials.PhosphoricAcid.getFluid(1000),
                Materials.Water.getFluid(500), Materials.Phosphorus.getDust(1), 20, 480, false);

        ItemStack cells = Ic2Items.cell.copy();
        cells.stackSize = 1;
        GT_Values.RA.addDistilleryRecipe(cells,WerkstoffLoader.AmmoniumChloride.getFluidOrGas(1000),
                Materials.HydrochloricAcid.getFluid(1000),Materials.Ammonia.getCells(1),50,120,false);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(AmmoniumNitrate.get(dust, 1), Materials.SodiumHydroxide.getDust(1), null,
                Materials.Ammonia.getGas(1000), WerkstoffLoader.SodiumNitrate.get(dust, 2), null, 100, 480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{AmmoniumNitrate.get(dust, 1), Materials.SodiumHydroxide.getDust(1), C2},
                null,
                new FluidStack[]{Materials.Ammonia.getGas(1000)},
                new ItemStack[]{WerkstoffLoader.SodiumNitrate.get(dust, 2)},
                100, 480
        );

        GT_Values.RA.addChemicalRecipe(WerkstoffLoader.SodiumNitrate.get(dust, 4), C2, Materials.SulfuricAcid.getFluid(2000),
                Materials.NitricAcid.getFluid(2000), WerkstoffLoader.Sodiumsulfate.get(dust, 1), 200, 120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{
                        C24,
                        AmmoniumNitrate.get(dust, 2),
                        Materials.SodiumHydroxide.getDust(2)
                },
                new FluidStack[]{
                        Materials.SulfuricAcid.getFluid(1000),
                        Materials.Hydrogen.getGas(2000)
                },
                new FluidStack[]{
                        Materials.Ammonia.getGas(2000),
                        Materials.NitricAcid.getFluid(2000)
                },
                new ItemStack[]{Materials.Sodium.getDust(2)},
                300, 480
        );

        //Monomethylhydrazine
        cells.stackSize = 1;
        GT_Values.RA.addCrackingRecipe(1, Materials.Toluene.getFluid(1000), Materials.Methanol.getFluid(1000),
                OXylene.getFluidOrGas(1000), 600, 4096);

        GT_Values.RA.addChemicalRecipe(OXylene.get(cell, 1), VanadiumPentoxide.get(dustTiny), Materials.Oxygen.getGas(3000),
                Materials.Water.getFluid(3000), PhthalicAnhydride.get(dust, 1), 800, 1920);

        GT_Values.RA.addBlastRecipe(Materials.Vanadium.getDust(2), C2, null,
                null, VanadiumPentoxide.get(dust, 5), null, 200, 120, 700);

        GT_Values.RA.addChemicalRecipe(Materials.Benzene.getCells(1), C2, Materials.Butene.getGas(1000),
                TertButylbenzene.getFluidOrGas(1000), cells, 100, 1920);

        GT_Values.RA.addChemicalRecipe(PhthalicAnhydride.get(dust, 1), C2, TertButylbenzene.getFluidOrGas(1000),
                TwoTertButylAnthraquinone.getFluidOrGas(1000), null, 200, 7680);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Hydrogen.getCells(10), Materials.Palladium.getDustTiny(1),
                TwoTertButylAnthraquinone.getFluidOrGas(10000),
                TwoTertButylAnthrahydroquinone.getFluidOrGas(10000), null, null, 1200, 7680);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{C2, Materials.Palladium.getDustTiny(1)},
                new FluidStack[]{
                        Materials.Hydrogen.getGas(10000),
                        TwoTertButylAnthraquinone.getFluidOrGas(10000)
                },
                new FluidStack[]{TwoTertButylAnthrahydroquinone.getFluidOrGas(10000)},
                null,
                1200, 7680
        );

        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1), C2, TwoTertButylAnthrahydroquinone.getFluidOrGas(1000),
                TwoTertButylAnthraquinone.getFluidOrGas(900), HydrogenPeroxide.get(cell, 1), 40, 1920);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{
                        C24,
                        Materials.Palladium.getDustTiny(1)
                },
                new FluidStack[]{
                        Materials.Hydrogen.getGas(10000),
                        Materials.Oxygen.getGas(10000),
                        TwoTertButylAnthraquinone.getFluidOrGas(10000)
                },
                new FluidStack[]{
                        HydrogenPeroxide.getFluidOrGas(10000),
                        TwoTertButylAnthraquinone.getFluidOrGas(9000)
                },
                null,
                1400, 7680
        );

        GT_Values.RA.addChemicalRecipe(HydrogenPeroxide.get(cell, 1), C2, Materials.Ammonia.getGas(2000),
                Materials.Water.getFluid(2000), Hydrazine.get(cell, 2), 100, 120);

        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), C2, Materials.Methanol.getFluid(2000),
                null, DimethylSulfate.get(cell, 1), 50, 480);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.SulfuricAcid.getCells(1), C1, Materials.Methanol.getFluid(2000),
                DimethylSulfate.getFluidOrGas(1000), cells, null, 50, 480);

        GT_Values.RA.addChemicalRecipe(Hydrazine.get(cell, 2), C2, DimethylSulfate.getFluidOrGas(1000),
                Materials.SulfurTrioxide.getGas(1000), Monomethylhydrazine.get(cell, 2), 80, 16000);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(DimethylSulfate.get(cell, 1), C1, Hydrazine.getFluidOrGas(2000),
                Monomethylhydrazine.getFluidOrGas(2000), Materials.SulfurTrioxide.getCells(1), null, 80, 16000);

        GT_Values.RA.addMixerRecipe(AmmoniumDinitramide.get(cell, 1), C1, null, null, Monomethylhydrazine.getFluidOrGas(2000),
                MonomethylhydrazineFuelMix.getFluidOrGas(3000), cells, 20, 480);
        cells.stackSize = 2;

        GT_Values.RA.addMixerRecipe(Monomethylhydrazine.get(cell, 2), C2, null, null, AmmoniumDinitramide.getFluidOrGas(1000),
                MonomethylhydrazineFuelMix.getFluidOrGas(3000), cells, 20, 480);
        cells.stackSize = 1;

        //unsimetrical hydazine

        GT_Values.RA.addCrackingRecipe(2,Materials.Propane.getGas(1000),Materials.NitricAcid.getFluid(1000),
                Nitromethane.getFluidOrGas(1000),300,1920);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{C2},
                new FluidStack[]{
                        Materials.HydrofluoricAcid.getFluid(1000),
                        BoronTrifluoride.getFluidOrGas(1000),
                        Nitromethane.getFluidOrGas(1000),
                        Materials.NitricAcid.getFluid(1000)},
                new FluidStack[]{
                     Materials.Methane.getGas(1000),
                     Materials.Water.getFluid(1000),
                     Materials.NitricOxide.getGas(1000)},
                new ItemStack[]{NitroniumTetrafluoroborate.get(dust,1)},
                100,7_680
        );

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{C2,NitroniumTetrafluoroborate.get(dust,1)},
                new FluidStack[]{AmmoniumDinitramide.getFluidOrGas(1000)},
                new FluidStack[]{
                        Trinitramid.getFluidOrGas(1000),
                        AmmoniaBoronfluorideSolution.getFluidOrGas(1000)
                },
                null,
                20,30_720
        );

        cells.stackSize = 3;
        GT_Values.RA.addChemicalRecipe(BoronTrioxide.get(dust,1),cells,Materials.HydrofluoricAcid.getFluid(6000),
                BoronTrifluoride.getFluidOrGas(2000),Materials.Water.getCells(3),50,480);

        GT_Values.RA.addChemicalRecipe(Materials.Borax.getDust(23),C2,Materials.SulfuricAcid.getFluid(1000),
                null,WerkstoffLoader.Sodiumsulfate.get(dust,1),BoronTrioxide.get(dust,2),400,1920);

        cells.stackSize = 1;
        GT_Values.RA.addDistilleryRecipe(cells,AmmoniaBoronfluorideSolution.getFluidOrGas(1000),
                 Tetrafluoroborate.getFluidOrGas(1000),Materials.Ammonia.getCells(1),20,30_720,false);

        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1),cells,Tetrafluoroborate.getFluidOrGas(1000),
                SodiumTetrafluoroborate.getFluidOrGas(1000),Materials.Water.getCells(1),100,1920);

        GT_Values.RA.addDistilleryRecipe(C2,SodiumTetrafluoroborate.getFluidOrGas(1000),
                BoronTrifluoride.getFluidOrGas(1000),SodiumFluoride.get(dust,1),40,480,false);

        GT_Values.RA.addChemicalRecipe(SodiumFluoride.get(dust,2),C2,Materials.SulfuricAcid.getFluid(1000),
                Materials.HydrofluoricAcid.getFluid(2000),WerkstoffLoader.Sodiumsulfate.get(dust,1),50,1920);

        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(4),Materials.Silver.getDustTiny(1),Materials.Methanol.getFluid(4000),
                Formaldehyde.getFluidOrGas(4000),cells,100,480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{C2},
                new FluidStack[]{
                        Materials.AceticAcid.getFluid(1000),
                        Materials.Ethanol.getFluid(1000),
                        Hydrazine.getFluidOrGas(1000)},
                new FluidStack[]{
                        Acetylhydrazine.getFluidOrGas(1000),
                        Materials.Ethanol.getFluid(900)},
                null,
                40,30_720
        );

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{C2},
                new FluidStack[]{
                        Acetylhydrazine.getFluidOrGas(1000),
                        Formaldehyde.getFluidOrGas(1000),
                        Materials.Hydrogen.getGas(2000)},
                new FluidStack[]{
                        UnsymmetricalDimethylhydrazine.getFluidOrGas(2000),
                        Materials.AceticAcid.getFluid(900)},
                null,
                20, 122_880
        );

        GT_Values.RA.addMixerRecipe(UnsymmetricalDimethylhydrazine.get(cell,2),C2,null,null,
                Trinitramid.getFluidOrGas(1000),UnsymmetricalDimethylhydrazineFuelMix.getFluidOrGas(3000),cells,10,120);

        GT_Values.RA.addMixerRecipe(Trinitramid.get(cell,1),C2,null,null,
                UnsymmetricalDimethylhydrazine.getFluidOrGas(2000),UnsymmetricalDimethylhydrazineFuelMix.getFluidOrGas(3000),cells,10,120);

    }
    public static void addFuels()
    {
        try {
            if (Loader.isModLoaded(GT_Values.MOD_ID_GC_CORE)) {
                Class<?> rocket = Class.forName("micdoodle8.mods.galacticraft.api.recipe.RocketFuelRecipe");
                Method addFuel = rocket.getMethod("addFuel", Fluid.class, int.class);
                addFuel.invoke(null, LMP103S.getFluidOrGas(1).getFluid(), 4);
                addFuel.invoke(null, MonomethylhydrazineFuelMix.getFluidOrGas(1).getFluid(), 6);
                addFuel.invoke(null, UnsymmetricalDimethylhydrazineFuelMix.getFluidOrGas(1).getFluid(), 8);
            }
            if (Loader.isModLoaded("miscutils"))
            {
                Class gtppRecipeMap = Class.forName("gregtech.api.util.GTPP_Recipe$GTPP_Recipe_Map");
                Field rocketFuels = gtppRecipeMap.getDeclaredField("sRocketFuels");
                rocketFuels.setAccessible(true);
                Class rocketFuelsClass = rocketFuels.getType();
                Object rocketFuelsObject = rocketFuels.get(null);
                Method addFuel = rocketFuelsClass.getDeclaredMethod("addFuel",FluidStack.class,FluidStack.class,int.class);
                addFuel.invoke(rocketFuelsObject,LMP103S.getFluidOrGas(1000),null,666);
                addFuel.invoke(rocketFuelsObject,MonomethylhydrazineFuelMix.getFluidOrGas(1000),null,1500);
                addFuel.invoke(rocketFuelsObject,UnsymmetricalDimethylhydrazineFuelMix.getFluidOrGas(1000),null,3000);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        GT_Recipe.GT_Recipe_Map.sTurbineFuels.addFuel(TertButylbenzene.get(cell,1),null,420);
    }

    public static void removeRecipes()
    {
        BotRecipes.removeTungstenElectro();
    }

    public static void removeTungstenElectro() {
        Collection<GT_Recipe> electroRecipeMap = GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList;
        HashSet<GT_Recipe> toDel = new HashSet<>();
        ItemStack[] toRemove = {
                Materials.Scheelite.getDust(1),
                Materials.Tungstate.getDust(1),
                WerkstoffLoader.Ferberite.get(dust, 1),
                WerkstoffLoader.Huebnerit.get(dust, 1)};
        for (GT_Recipe tRecipe : electroRecipeMap) {
            if (tRecipe.mFakeRecipe)
                continue;
            for (int i = 0; i < tRecipe.mInputs.length; i++) {
                ItemStack tItem = tRecipe.mInputs[i];
                if (item == null || !GT_Utility.isStackValid(tItem))
                    continue;
                for (ItemStack tStack : toRemove) {
                    if (GT_Utility.areStacksEqual(tItem, tStack)) {
                        toDel.add(tRecipe);
                        continue;
                    }
                }
            }
        }
        electroRecipeMap.removeAll(toDel);
        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.reInit();
    }

}
