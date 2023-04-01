package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

public class ProcessingShaping implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingShaping() {
        OrePrefixes.ingot.add(this);
        OrePrefixes.dust.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
            ItemStack aStack) {
        if (((aMaterial == Materials.Glass) || (GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null))
                && (!aMaterial.contains(SubTag.NO_SMELTING))) {
            long aMaterialMass = aMaterial.getMass();
            int tAmount = (int) (aPrefix.mMaterialAmount / 3628800L);
            if ((tAmount > 0) && (tAmount <= 64) && (aPrefix.mMaterialAmount % 3628800L == 0L)) {
                int tVoltageMultiplier = aMaterial.mBlastFurnaceTemp >= 2800 ? 60 : 15;
                int tTrueVoltage = aMaterial.getProcessingMaterialTierEU();

                if (aMaterial.contains(SubTag.NO_SMASHING)) {
                    tVoltageMultiplier /= 4;
                } else if (aPrefix.name()
                                  .startsWith(OrePrefixes.dust.name())) {
                                      return;
                                  }

                if (!OrePrefixes.block.isIgnored(aMaterial.mSmeltInto)) {
                    GT_Values.RA.addExtruderRecipe(
                            GT_Utility.copyAmount(9L, aStack),
                            ItemList.Shape_Extruder_Block.get(0L),
                            GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount),
                            10 * tAmount,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));

                    // Allow creation of alloy smelter recipes for material recycling if < IV tier.
                    if (tTrueVoltage < TierEU.IV) {
                        GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(9L, aStack),
                                ItemList.Shape_Mold_Block.get(0L),
                                GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount),
                                5 * tAmount,
                                calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier));
                    }
                }
                if (((aPrefix != OrePrefixes.ingot) || (aMaterial != aMaterial.mSmeltInto))) {
                    GT_Values.RA.addExtruderRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            ItemList.Shape_Extruder_Ingot.get(0L),
                            GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, tAmount),
                            10,
                            calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier));
                }

                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        ItemList.Shape_Extruder_Pipe_Tiny.get(0L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial.mSmeltInto, tAmount * 2),
                        4 * tAmount,
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));

                if (!(aMaterial == Materials.Redstone || aMaterial == Materials.Glowstone)) {
                    GT_Values.RA.addExtruderRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            ItemList.Shape_Extruder_Pipe_Small.get(0L),
                            GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial.mSmeltInto, tAmount),
                            8 * tAmount,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));

                    GT_Values.RA.addExtruderRecipe(
                            GT_Utility.copyAmount(3L, aStack),
                            ItemList.Shape_Extruder_Pipe_Medium.get(0L),
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial.mSmeltInto, tAmount),
                            24 * tAmount,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));

                    GT_Values.RA.addExtruderRecipe(
                            GT_Utility.copyAmount(6L, aStack),
                            ItemList.Shape_Extruder_Pipe_Large.get(0L),
                            GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial.mSmeltInto, tAmount),
                            48 * tAmount,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                }

                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(12L, aStack),
                        ItemList.Shape_Extruder_Pipe_Huge.get(0L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial.mSmeltInto, tAmount),
                        96 * tAmount,
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        ItemList.Shape_Extruder_Plate.get(0L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                        ItemList.Shape_Extruder_Small_Gear.get(0L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(6L, aStack),
                        ItemList.Shape_Extruder_Turbine_Blade.get(0L),
                        GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));

                if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Ring.get(0L),
                            aMaterial.getMolten(36L),
                            GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L),
                            100,
                            calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Screw.get(0L),
                            aMaterial.getMolten(18L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L),
                            50,
                            calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Rod.get(0L),
                            aMaterial.getMolten(72L),
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                            150,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Bolt.get(0L),
                            aMaterial.getMolten(18L),
                            GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L),
                            50,
                            calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Round.get(0L),
                            aMaterial.getMolten(18L),
                            GT_OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L),
                            50,
                            calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Rod_Long.get(0L),
                            aMaterial.getMolten(144L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                            300,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Turbine_Blade.get(0L),
                            aMaterial.getMolten(864L),
                            GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L),
                            400,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Pipe_Tiny.get(0L),
                            aMaterial.getMolten(72L),
                            GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 1L),
                            20,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Pipe_Small.get(0L),
                            aMaterial.getMolten(144L),
                            GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 1L),
                            40,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Pipe_Medium.get(0L),
                            aMaterial.getMolten(432L),
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1L),
                            80,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Pipe_Large.get(0L),
                            aMaterial.getMolten(864L),
                            GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L),
                            160,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                    GT_Values.RA.addFluidSolidifierRecipe(
                            ItemList.Shape_Mold_Pipe_Huge.get(0L),
                            aMaterial.getMolten(1728L),
                            GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L),
                            320,
                            calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                }
                if (tAmount * 2 <= 64) {
                    if (!(aMaterial == Materials.Aluminium)) {
                        GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Rod.get(0L),
                                GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, tAmount * 2),
                                (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                                calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier));
                    } else {
                        GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Rod.get(0L),
                                GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, tAmount * 2),
                                200,
                                calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier));
                    }
                }
                if (tAmount * 2 <= 64) GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        ItemList.Shape_Extruder_Wire.get(0L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial.mSmeltInto, tAmount * 2),
                        (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier));
                if (tAmount * 8 <= 64) GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        ItemList.Shape_Extruder_Bolt.get(0L),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial.mSmeltInto, tAmount * 8),
                        (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                if (tAmount * 4 <= 64) {
                    GT_Values.RA.addExtruderRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            ItemList.Shape_Extruder_Ring.get(0L),
                            GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial.mSmeltInto, tAmount * 4),
                            (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                            calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier));
                    if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)
                            && !aMaterial.contains(SubTag.NO_SMASHING))

                        // If material tier < IV then add manual recipe.
                        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                            GT_ModHandler.addCraftingRecipe(
                                    GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L),
                                    GT_Proxy.tBits,
                                    new Object[] { "h ", "fX", 'X', OrePrefixes.stick.get(aMaterial) });
                        }
                }

                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(2L, aStack),
                        ItemList.Shape_Extruder_Sword.get(0L, new Object[0]),
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(3L, aStack),
                        ItemList.Shape_Extruder_Pickaxe.get(0L, new Object[0]),
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * 3L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        ItemList.Shape_Extruder_Shovel.get(0L, new Object[0]),
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * 1L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(3L, aStack),
                        ItemList.Shape_Extruder_Axe.get(0L, new Object[0]),
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * 3L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(2L, aStack),
                        ItemList.Shape_Extruder_Hoe.get(0L, new Object[0]),
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(6L, aStack),
                        ItemList.Shape_Extruder_Hammer.get(0L, new Object[0]),
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * 6L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(2L, aStack),
                        ItemList.Shape_Extruder_File.get(0L, new Object[0]),
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(2L, aStack),
                        ItemList.Shape_Extruder_Saw.get(0L, new Object[0]),
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));
                GT_Values.RA.addExtruderRecipe(
                        GT_Utility.copyAmount(4L, aStack),
                        ItemList.Shape_Extruder_Gear.get(0L, new Object[0]),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, tAmount),
                        (int) Math.max(aMaterialMass * 5L * tAmount, tAmount),
                        calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier));

                if (!(aMaterial == Materials.StyreneButadieneRubber || aMaterial == Materials.Silicone)) {
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                        GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(2L, aStack),
                                ItemList.Shape_Mold_Plate.get(0L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount),
                                (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                                calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier));
                    }
                } else {
                    // If tier < IV then add ability to turn ingots into plates via alloy smelter.
                    if (tTrueVoltage < TierEU.IV) {
                        GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Mold_Plate.get(0L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount),
                                (int) Math.max(aMaterialMass * 2L * tAmount, tAmount),
                                calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier));
                    }
                }

                // If tier < IV then add ability to turn ingots into gears via alloy smelter.
                if (tTrueVoltage < TierEU.IV) {
                    GT_Values.RA.addAlloySmelterRecipe(
                            GT_Utility.copyAmount(8L, aStack),
                            ItemList.Shape_Mold_Gear.get(0L),
                            GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, tAmount),
                            (int) Math.max(aMaterialMass * 10L * tAmount, tAmount),
                            calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier));
                }

                switch (aMaterial.mSmeltInto.mName) {
                    case "Glass":
                        GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Bottle.get(0L),
                                new ItemStack(Items.glass_bottle, 1),
                                tAmount * 32,
                                16);
                        GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Mold_Bottle.get(0L),
                                new ItemStack(Items.glass_bottle, 1),
                                tAmount * 64,
                                4);
                        break;
                    case "Steel":
                        GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Cell.get(0L),
                                ItemList.Cell_Empty.get(tAmount),
                                tAmount * 128,
                                30);
                        if (tAmount * 2 <= 64) GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingadviron", tAmount * 2),
                                tAmount * 32,
                                3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64) GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(2L, aStack),
                                ItemList.Shape_Mold_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingadviron", tAmount * 3),
                                tAmount * 128,
                                1 * tVoltageMultiplier);
                        break;
                    case "Iron":
                    case "WroughtIron":
                        GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Cell.get(0L),
                                GT_ModHandler.getIC2Item("fuelRod", tAmount),
                                tAmount * 128,
                                30);
                        if (tAmount * 2 <= 64) GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingiron", tAmount * 2),
                                tAmount * 32,
                                3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64) GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(2L, aStack),
                                ItemList.Shape_Mold_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingiron", tAmount * 3),
                                tAmount * 128,
                                1 * tVoltageMultiplier);
                        if (tAmount * 31 <= 64) GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(31L, aStack),
                                ItemList.Shape_Mold_Anvil.get(0L),
                                new ItemStack(Blocks.anvil, 1, 0),
                                tAmount * 512,
                                4 * tVoltageMultiplier);
                        break;
                    case "Tin":
                        GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(2L, aStack),
                                ItemList.Shape_Extruder_Cell.get(0L),
                                ItemList.Cell_Empty.get(tAmount),
                                tAmount * 128,
                                30);
                        if (tAmount * 2 <= 64) GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingtin", tAmount * 2),
                                tAmount * 32,
                                3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64) GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(2L, aStack),
                                ItemList.Shape_Mold_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingtin", tAmount * 3),
                                tAmount * 128,
                                1 * tVoltageMultiplier);
                        break;
                    case "Lead":
                        if (tAmount * 2 <= 64) GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casinglead", tAmount * 2),
                                tAmount * 32,
                                3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64) GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(2L, aStack),
                                ItemList.Shape_Mold_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casinglead", tAmount * 3),
                                tAmount * 128,
                                1 * tVoltageMultiplier);
                        break;
                    case "Copper":
                    case "AnnealedCopper":
                        if (tAmount * 2 <= 64) GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingcopper", tAmount * 2),
                                tAmount * 32,
                                3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64) GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(2L, aStack),
                                ItemList.Shape_Mold_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingcopper", tAmount * 3),
                                tAmount * 128,
                                1 * tVoltageMultiplier);
                        break;
                    case "Bronze":
                        if (tAmount * 2 <= 64) GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingbronze", tAmount * 2),
                                tAmount * 32,
                                3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64) GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(2L, aStack),
                                ItemList.Shape_Mold_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casingbronze", tAmount * 3),
                                tAmount * 128,
                                1 * tVoltageMultiplier);
                        break;
                    case "Gold":
                        if (tAmount * 2 <= 64) GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casinggold", tAmount * 2),
                                tAmount * 32,
                                3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64) GT_Values.RA.addAlloySmelterRecipe(
                                GT_Utility.copyAmount(2L, aStack),
                                ItemList.Shape_Mold_Casing.get(0L),
                                GT_ModHandler.getIC2Item("casinggold", tAmount * 3),
                                tAmount * 128,
                                1 * tVoltageMultiplier);
                        break;
                    case "Polytetrafluoroethylene": // Recipe for cells from PTFE, why is it here?
                        GT_Values.RA.addExtruderRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Shape_Extruder_Cell.get(0L),
                                ItemList.Cell_Empty.get(tAmount * 4),
                                tAmount * 128,
                                30);
                        break;
                }
            }
        }
    }
}
