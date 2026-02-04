/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bwcrossmod.galacticgreg;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;

public class MTEVoidMiners {

    public static class VMLUV extends MTEVoidMinerBase<VMLUV> {

        private static final String STRUCTURE_PIECE_MAIN = "main";
        private static final String STRUCTURE_PIECE_OLD = "old";

        private static final IStructureDefinition<VMLUV> STRUCTURE_DEFINITION = StructureDefinition.<VMLUV>builder()
            // spotless:off
            .addShape(
                STRUCTURE_PIECE_MAIN,
                transpose(
                    new String[][] {
                        { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", " B   B ", "  DAD  ", "  ACA  ", "  DAD  ", " B   B ", "       " },
                        { "  D D  ", " BA~AB ", " A   A ", " B C B ", " A   A ", " BABAB ", "       " },
                        { "  E E  ", " BBBBB ", "EB   BE", " B C B ", "EB   BE", " BBBBB ", "  E E  " } }))
            // spotless:on
            .addElement('A', Casings.MiningOsmiridiumCasing.asElement())
            .addElement('B', ofFrame(Materials.Osmiridium))
            .addElement('C', Casings.ReboltedOsmiridiumCasing.asElement())
            .addElement('D', Casings.BoltedOsmiridiumCasing.asElement())
            .addElement(
                'E',
                buildHatchAdder(VMLUV.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .hint(1)
                    .casingIndex(Casings.MiningOsmiridiumCasing.getTextureId())
                    .buildAndChain(Casings.MiningOsmiridiumCasing.asElement()))
            .build();

        @Override
        public IStructureDefinition<VMLUV> getStructureDefinition() {
            return STRUCTURE_DEFINITION;
        }

        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addMachineType("Miner, VM")
                .addInfo("Consumes " + numberFormat.format(GTValues.V[this.getMinTier()]) + " EU/t")
                .addInfo(
                    "Can be supplied with " + EnumChatFormatting.AQUA
                        + "2 L/s"
                        + EnumChatFormatting.GRAY
                        + " of Noble gases to boost "
                        + EnumChatFormatting.GOLD
                        + "output")
                .addInfo(createGasString(EnumChatFormatting.LIGHT_PURPLE, "Neon", 4))
                .addInfo(createGasString(EnumChatFormatting.AQUA, "Krypton", 8))
                .addInfo(createGasString(EnumChatFormatting.DARK_AQUA, "Xenon", 16))
                .addInfo(createGasString(EnumChatFormatting.BLUE, "Oganesson", 64))
                .addInfo(
                    "Will output " + 2 * this.TIER_MULTIPLIER
                        + " Ores per Second depending on the Dimension it is build in")
                .addInfo("Put the Ore into the input bus to set the Whitelist/Blacklist")
                .addInfo("Use a screwdriver to toggle Whitelist/Blacklist")
                .addInfo(
                    "Blacklist or non Whitelist Ore will be " + EnumChatFormatting.DARK_RED
                        + "VOIDED"
                        + EnumChatFormatting.RESET
                        + ".")
                .beginStructureBlock(7, 9, 7, false)
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "20x" + EnumChatFormatting.GRAY + " Mining Osmiridium Casing")
                .addStructureInfo(EnumChatFormatting.GOLD + "42x" + EnumChatFormatting.GRAY + " Osmiridium Frame Box")
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "6x" + EnumChatFormatting.GRAY + " Bolted Osmiridium Casing")
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "6x" + EnumChatFormatting.GRAY + " Rebolted Osmiridium Casing")
                .addEnergyHatch(VN[this.getMinTier()] + "+, Any base casing")
                .addMaintenanceHatch("Any base casing")
                .addInputBus("(Optional) For mining pipes or ores, any base casing")
                .addInputHatch("(Optional) For noble gas, any base casing")
                .addOutputBus("Any base casing")
                .toolTipFinisher();
            return tt;
        }

        public void construct(ItemStack stackSize, boolean hintsOnly) {
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 7, 1);
        }

        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            if (mMachine) return -1;
            return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 7, 1, elementBudget, env, false, true);
        }

        @Override
        public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
            return (checkPiece(STRUCTURE_PIECE_MAIN, 3, 7, 1)) && checkHatches()
                && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
                && mMaintenanceHatches.size() == 1;
        }

        public VMLUV(String aName, int tier) {
            super(aName, tier);
            casingTextureIndex = Casings.MiningOsmiridiumCasing.getTextureId();
        }

        public VMLUV(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 1);
        }

        @Override
        protected int getControllerTextureIndex() {
            return Casings.MiningOsmiridiumCasing.getTextureId();
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMLUV(this.mName, this.TIER_MULTIPLIER);
        }

        protected static Block getCasingBlock() {
            return ModBlocks.blockCustomPipeGearCasings;
        }

        protected static int getCasingMeta() {
            return 5;
        }
    }

    public static class VMZPM extends MTEVoidMinerBase<VMZPM> {

        private static final String STRUCTURE_PIECE_MAIN = "main";
        private static final String STRUCTURE_PIECE_OLD = "old";

        private static final IStructureDefinition<VMZPM> STRUCTURE_DEFINITION = StructureDefinition.<VMZPM>builder()
            // spotless:off
            .addShape(
                STRUCTURE_PIECE_MAIN,
                transpose(
                    new String[][]{
                        {"         ","         ","         ","    C    ","         ","         ","         ","         "},
                        {"         ","         ","         ","    C    ","         ","         ","         ","         "},
                        {"         ","         ","         ","    C    ","         ","         ","         ","         "},
                        {"         ","         ","         ","    C    ","         ","         ","         ","         "},
                        {"         ","         ","    C    ","   CEC   ","    C    ","         ","         ","         "},
                        {"         ","         ","    C    ","   CEC   ","    C    ","         ","         ","         "},
                        {"         ","         ","    C    ","   CEC   ","    C    ","         ","         ","         "},
                        {"         ","  C   C  ","    C    ","   CEC   ","    C    ","  C   C  ","         ","         "},
                        {"         ","  C   C  ","   DCD   ","   CEC   ","   DCD   ","  C   C  ","         ","         "},
                        {"         ","  CCCCC  ","  CDADC  ","  CAEAC  ","  CDADC  ","  CCCCC  ","         ","         "},
                        {"   D D   ","  CA~AC  ","  A   A  ","  B E B  ","  A   A  ","  CABAC  ","         ","         "},
                        {"   F F   ","  CABAC  ","FBA   ABF","  B E B  ","FBA   ABF","  CABAC  ","   B B   ","   F F   "},
                        {"   F F   ","  CCCCC  ","F C   C F","  C E C  ","F C   C F","  CCCCC  ","         ","   F F   "}}))
            // spotless:on
            .addElement('A', Casings.BlackPlutoniumItemPipeCasing.asElement())
            .addElement('B', Casings.MiningBlackPlutoniumCasing.asElement())
            .addElement('C', ofFrame(Materials.NaquadahAlloy))
            .addElement('D', Casings.BoltedNaquadahAlloyCasing.asElement())
            .addElement('E', Casings.ReboltedNaquadahAlloyCasing.asElement())
            .addElement(
                'F',
                buildHatchAdder(VMZPM.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .hint(1)
                    .casingIndex(Casings.MiningBlackPlutoniumCasing.getTextureId())
                    .buildAndChain(Casings.MiningBlackPlutoniumCasing.asElement()))
            .build();

        @Override
        public IStructureDefinition<VMZPM> getStructureDefinition() {
            return STRUCTURE_DEFINITION;
        }

        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addMachineType("Miner")
                .addInfo("Consumes " + numberFormat.format(GTValues.V[this.getMinTier()]) + "EU/t")
                .addInfo(
                    "Can be supplied with " + EnumChatFormatting.AQUA
                        + "2 L/s"
                        + EnumChatFormatting.GRAY
                        + " of Noble gases to boost "
                        + EnumChatFormatting.GOLD
                        + "output")
                .addInfo(createGasString(EnumChatFormatting.LIGHT_PURPLE, "Neon", 4))
                .addInfo(createGasString(EnumChatFormatting.AQUA, "Krypton", 8))
                .addInfo(createGasString(EnumChatFormatting.DARK_AQUA, "Xenon", 16))
                .addInfo(createGasString(EnumChatFormatting.BLUE, "Oganesson", 64))
                .addInfo(
                    "Will output " + 2 * this.TIER_MULTIPLIER
                        + " Ores per Second depending on the Dimension it is build in")
                .addInfo("Put the Ore into the input bus to set the Whitelist/Blacklist")
                .addInfo("Use a screwdriver to toggle Whitelist/Blacklist")
                .addInfo(
                    "Blacklist or non Whitelist Ore will be " + EnumChatFormatting.DARK_RED
                        + "VOIDED"
                        + EnumChatFormatting.RESET
                        + ".")
                .beginStructureBlock(9, 13, 8, false)
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "29x" + EnumChatFormatting.GRAY + " Mining Black Plutonium Casing")
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "20x" + EnumChatFormatting.GRAY + " Black Plutonium Item Pipe Casing")
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "72x" + EnumChatFormatting.GRAY + " Naquadah Alloy Frame Box")
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "10x" + EnumChatFormatting.GRAY + " Bolted Naquadah Alloy Casing")
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "9x" + EnumChatFormatting.GRAY + " Rebolted Naquadah Alloy Casing")
                .addEnergyHatch(VN[this.getMinTier()] + "+, Any base casing")
                .addMaintenanceHatch("Any base casing")
                .addInputBus("Ores, optional, any base casing")
                .addInputHatch("Optional noble gas, any base casing")
                .addOutputBus("Any base casing")
                .toolTipFinisher();
            return tt;
        }

        public void construct(ItemStack stackSize, boolean hintsOnly) {
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 10, 1);
        }

        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            if (mMachine) return -1;
            return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 10, 1, elementBudget, env, false, true);
        }

        @Override
        public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
            return (checkPiece(STRUCTURE_PIECE_MAIN, 4, 10, 1)) && checkHatches()
                && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
                && mMaintenanceHatches.size() == 1;
        }

        public VMZPM(String aName, int tier) {
            super(aName, tier);
            casingTextureIndex = Casings.MiningBlackPlutoniumCasing.getTextureId();
        }

        public VMZPM(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 2);

        }

        @Override
        protected int getControllerTextureIndex() {
            return Casings.MiningBlackPlutoniumCasing.getTextureId();
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMZPM(this.mName, this.TIER_MULTIPLIER);
        }
    }

    public static class VMUV extends MTEVoidMinerBase<VMUV> {

        private static final String STRUCTURE_PIECE_MAIN = "main";

        private static final IStructureDefinition<VMUV> STRUCTURE_DEFINITION = StructureDefinition.<VMUV>builder()
            // spotless:off
            .addShape(
                STRUCTURE_PIECE_MAIN,
                transpose(
                    new String[][]{
                        {"         ","         ","         ","         ","    C    ","         ","         ","         ","         "},
                        {"         ","         ","         ","         ","    C    ","         ","         ","         ","         "},
                        {"         ","         ","         ","         ","    C    ","         ","         ","         ","         "},
                        {"         ","         ","         ","         ","    C    ","         ","         ","         ","         "},
                        {"         ","         ","         ","    C    ","   CDC   ","    C    ","         ","         ","         "},
                        {"         ","         ","         ","    C    ","   CDC   ","    C    ","         ","         ","         "},
                        {"         ","         ","         ","    C    ","   CDC   ","    C    ","         ","         ","         "},
                        {"         ","         ","         ","   DCD   ","   CDC   ","   DCD   ","         ","         ","         "},
                        {"         ","         ","         ","   DCD   ","   CDC   ","   DCD   ","         ","         ","         "},
                        {"         ","         ","         ","   DCD   ","   CDC   ","   DCD   ","         ","         ","         "},
                        {"         ","         ","         ","   DCD   ","   CDC   ","   DCD   ","         ","         ","         "},
                        {"         ","         ","  C   C  ","   DDD   ","   DDD   ","   DDD   ","  C   C  ","         ","         "},
                        {"         ","         ","  CEEEC  ","  EAAAE  ","  EADAE  ","  EAAAE  ","  CEEEC  ","         ","         "},
                        {"   E E   ","   B B   ","  CB~BC  ","EBB   BBE","  B D B  ","EBB   BBE","  CBBBC  ","   B B   ","   E E   "},
                        {"   F F   ","         ","  CBBBC  ","F B   B F","  B D B  ","F B   B F","  CBBBC  ","         ","   F F   "},
                        {"   F F   ","  C   C  "," CCCCCCC ","F C   C F","  C D C  ","F C   C F"," CCCCCCC ","  C   C  ","   F F   "}}))
            // spotless:on
            .addElement('A', Casings.BlackPlutoniumItemPipeCasing.asElement())
            .addElement('B', Casings.MiningNeutroniumCasing.asElement())
            .addElement('C', ofFrame(Materials.Adamantium))
            .addElement('D', Casings.ReboltedIridiumCasing.asElement())
            .addElement('E', Casings.BoltedIridiumCasing.asElement())
            .addElement(
                'F',
                buildHatchAdder(VMUV.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .hint(1)
                    .casingIndex(Casings.MiningNeutroniumCasing.getTextureId())
                    .buildAndChain(Casings.MiningNeutroniumCasing.asElement()))
            .build();

        @Override
        public IStructureDefinition<VMUV> getStructureDefinition() {
            return STRUCTURE_DEFINITION;
        }

        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addMachineType("Miner")
                .addInfo("Consumes " + numberFormat.format(GTValues.V[this.getMinTier()]) + " EU/t")
                .addInfo(
                    "Can be supplied with " + EnumChatFormatting.AQUA
                        + "2 L/s"
                        + EnumChatFormatting.GRAY
                        + " of Noble gases to boost "
                        + EnumChatFormatting.GOLD
                        + "output")
                .addInfo(createGasString(EnumChatFormatting.LIGHT_PURPLE, "Neon", 4))
                .addInfo(createGasString(EnumChatFormatting.AQUA, "Krypton", 8))
                .addInfo(createGasString(EnumChatFormatting.DARK_AQUA, "Xenon", 16))
                .addInfo(createGasString(EnumChatFormatting.BLUE, "Oganesson", 64))
                .addInfo(
                    "Will output " + 2 * this.TIER_MULTIPLIER
                        + " Ores per Second depending on the Dimension it is build in")
                .addInfo("Put the Ore into the input bus to set the Whitelist/Blacklist")
                .addInfo("Use a screwdriver to toggle Whitelist/Blacklist")
                .addInfo(
                    "Blacklist or non Whitelist Ore will be " + EnumChatFormatting.DARK_RED
                        + "VOIDED"
                        + EnumChatFormatting.RESET
                        + ".")
                .beginStructureBlock(9, 16, 9, false)
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "47x" + EnumChatFormatting.GRAY + " Mining Neutronium Casing")
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "8x" + EnumChatFormatting.GRAY + " Black Plutonium Item Pipe Casing")
                .addStructureInfo(EnumChatFormatting.GOLD + "72x" + EnumChatFormatting.GRAY + " Adamantium Frame Box")
                .addStructureInfo(EnumChatFormatting.GOLD + "20x" + EnumChatFormatting.GRAY + " Bolted Iridium Casing")
                .addStructureInfo(
                    EnumChatFormatting.GOLD + "36x" + EnumChatFormatting.GRAY + " Rebolted Iridium Casing")
                .addEnergyHatch(VN[this.getMinTier()] + "+, Any base casing")
                .addMaintenanceHatch("Any base casing")
                .addInputBus("Ores, optional, any base casing")
                .addInputHatch("Optional noble gas, any base casing")
                .addOutputBus("Any base casing")
                .toolTipFinisher();
            return tt;
        }

        public void construct(ItemStack stackSize, boolean hintsOnly) {
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 13, 2);
        }

        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            if (mMachine) return -1;
            return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 13, 2, elementBudget, env, false, true);
        }

        @Override
        public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
            return (checkPiece(STRUCTURE_PIECE_MAIN, 4, 13, 2)) && checkHatches()
                && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
                && mMaintenanceHatches.size() == 1;
        }

        public VMUV(String aName, int tier) {
            super(aName, tier);
            casingTextureIndex = Casings.MiningNeutroniumCasing.getTextureId();
        }

        public VMUV(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 3);
        }

        @Override
        protected int getControllerTextureIndex() {
            return Casings.MiningNeutroniumCasing.getTextureId();
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMUV(this.mName, this.TIER_MULTIPLIER);
        }
    }
}
