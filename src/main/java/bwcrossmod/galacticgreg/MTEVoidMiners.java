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

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockCasings4;
import gregtech.common.blocks.BlockCasings8;
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
            // old structure (to be deprecated on future version)
            .addShape(
                STRUCTURE_PIECE_OLD,
                transpose(
                    new String[][] {
                        { "   ", " G ", "   " },
                        { "   ", " G ", "   " },
                        { "   ", " G ", "   " },
                        { " G ", "GFG", " G " },
                        { " G ", "GFG", " G " },
                        { " G ", "GFG", " G " },
                        { "H~H", "HHH", "HHH" } }))
            // spotless:on
            .addElement('A', ofBlock(GregTechAPI.sBlockCasings4, 14))
            .addElement('B', ofFrame(Materials.Osmiridium))
            .addElement('C', ofBlock(WerkstoffLoader.BWBlockCasingsAdvanced, 32083))
            .addElement('D', ofBlock(WerkstoffLoader.BWBlockCasings, 32083))
            .addElement(
                'E',
                buildHatchAdder(VMLUV.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .dot(1)
                    .casingIndex(((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(14))
                    .buildAndChain(GregTechAPI.sBlockCasings4, 14))
            // for compatibility with the old structure
            .addElement(
                'F',
                ofBlock(
                    ItemList.Casing_UV.getBlock(),
                    ItemList.Casing_UV.get(0)
                        .getItemDamage()))
            .addElement('G', ofFrame(Materials.Europium))
            .addElement(
                'H',
                buildHatchAdder(VMLUV.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .dot(1)
                    .casingIndex(8)
                    .buildAndChain(
                        ItemList.Casing_UV.getBlock(),
                        ItemList.Casing_UV.get(0)
                            .getItemDamage()))
            .build();

        @Override
        public IStructureDefinition<VMLUV> getStructureDefinition() {
            return STRUCTURE_DEFINITION;
        }

        public void construct(ItemStack stackSize, boolean hintsOnly) {
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 7, 1);
        }

        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            if (mMachine) return -1;
            return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 7, 1, elementBudget, env, false, true);
        }

        @Override
        public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
            return (checkPiece(STRUCTURE_PIECE_MAIN, 3, 7, 1)
                // old structure (to be deprecated on future version)
                || (checkPiece(STRUCTURE_PIECE_OLD, 1, 6, 0))) && checkHatches()
                && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
                && mMaintenanceHatches.size() == 1;
        }

        public VMLUV(String aName, int tier) {
            super(aName, tier);
            casingTextureIndex = 8;
        }

        public VMLUV(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 1);
        }

        @Override
        protected int getControllerTextureIndex() {
            return 8;
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
            // old structure (to be deprecated on future version)
            .addShape(
                STRUCTURE_PIECE_OLD,
                transpose(
                    new String[][] {
                        { "   ", " G ", "   " },
                        { "   ", " G ", "   " },
                        { "   ", " G ", "   " },
                        { " G ", "GIG", " G " },
                        { " G ", "GIG", " G " },
                        { " G ", "GIG", " G " },
                        { "H~H", "HHH", "HHH" } }))
            // spotless:on
            .addElement('A', ofBlock(GregTechAPI.sBlockCasings11, 7))
            .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 3))
            .addElement('C', ofFrame(Materials.NaquadahAlloy))
            .addElement('D', ofBlock(WerkstoffLoader.BWBlockCasings, 32091))
            .addElement('E', ofBlock(WerkstoffLoader.BWBlockCasingsAdvanced, 32091))
            .addElement(
                'F',
                buildHatchAdder(VMZPM.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .dot(1)
                    .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(3))
                    .buildAndChain(GregTechAPI.sBlockCasings8, 3))
            // for compatibility with the old structure
            .addElement(
                'I',
                ofBlock(
                    ItemList.Casing_MiningBlackPlutonium.getBlock(),
                    ItemList.Casing_MiningBlackPlutonium.get(0)
                        .getItemDamage()))
            .addElement('G', ofFrame(Materials.BlackPlutonium))
            .addElement(
                'H',
                buildHatchAdder(VMZPM.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .dot(1)
                    .casingIndex(179)
                    .buildAndChain(
                        ItemList.Casing_MiningBlackPlutonium.getBlock(),
                        ItemList.Casing_MiningBlackPlutonium.get(0)
                            .getItemDamage()))
            .build();

        @Override
        public IStructureDefinition<VMZPM> getStructureDefinition() {
            return STRUCTURE_DEFINITION;
        }

        public void construct(ItemStack stackSize, boolean hintsOnly) {
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 10, 1);
        }

        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            if (mMachine) return -1;
            return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 10, 1, elementBudget, env, false, true);
        }

        @Override
        public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
            return (checkPiece(STRUCTURE_PIECE_MAIN, 4, 10, 1)
                // old structure (to be deprecated on future version)
                || (checkPiece(STRUCTURE_PIECE_OLD, 1, 6, 0))) && checkHatches()
                && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
                && mMaintenanceHatches.size() == 1;
        }

        public VMZPM(String aName, int tier) {
            super(aName, tier);
            casingTextureIndex = 179;
        }

        public VMZPM(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 2);

        }

        @Override
        protected int getControllerTextureIndex() {
            return 179;
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMZPM(this.mName, this.TIER_MULTIPLIER);
        }
    }

    public static class VMUV extends MTEVoidMinerBase<VMUV> {

        private static final String STRUCTURE_PIECE_MAIN = "main";
        private static final String STRUCTURE_PIECE_OLD = "old";

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
            // old structure (to be deprecated on future version)
            .addShape(
                STRUCTURE_PIECE_OLD,
                transpose(
                    new String[][] {
                        { "   ", " G ", "   " },
                        { "   ", " G ", "   " },
                        { "   ", " G ", "   " },
                        { " G ", "GIG", " G " },
                        { " G ", "GIG", " G " },
                        { " G ", "GIG", " G " },
                        { "H~H", "HHH", "HHH" } }))
            // spotless:on
            .addElement('A', ofBlock(GregTechAPI.sBlockCasings11, 7))
            .addElement('B', ofBlock(GregTechAPI.sBlockCasings8,2))
            .addElement('C', ofFrame(Materials.Adamantium))
            .addElement('D', ofBlock(WerkstoffLoader.BWBlockCasingsAdvanced,31850))
            .addElement('E', ofBlock(WerkstoffLoader.BWBlockCasings,31850))
            .addElement(
                'F',
                buildHatchAdder(VMUV.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .dot(1)
                    .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                    .buildAndChain(GregTechAPI.sBlockCasings8, 2))
            // for compatibility with the old structure
            .addElement(
                'I',
                ofBlock(
                    ItemList.Casing_MiningNeutronium.getBlock(),
                    ItemList.Casing_MiningNeutronium.get(0)
                        .getItemDamage()))
            .addElement('G', ofFrame(Materials.Neutronium))
            .addElement(
                'H',
                buildHatchAdder(VMUV.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .dot(1)
                    .casingIndex(178)
                    .buildAndChain(
                        ItemList.Casing_MiningNeutronium.getBlock(),
                        ItemList.Casing_MiningNeutronium.get(0)
                            .getItemDamage()))
            .build();

        @Override
        public IStructureDefinition<VMUV> getStructureDefinition() {
            return STRUCTURE_DEFINITION;
        }

        public void construct(ItemStack stackSize, boolean hintsOnly) {
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 13, 2);
        }

        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            if (mMachine) return -1;
            return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 13, 2, elementBudget, env, false, true);
        }

        @Override
        public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
            return (checkPiece(STRUCTURE_PIECE_MAIN, 4, 13, 2)
                // old structure (to be deprecated on future version)
                || (checkPiece(STRUCTURE_PIECE_OLD, 1, 6, 0))) && checkHatches()
                && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
                && mMaintenanceHatches.size() == 1;
        }

        public VMUV(String aName, int tier) {
            super(aName, tier);
            casingTextureIndex = 178;
        }

        public VMUV(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 3);
        }

        @Override
        protected int getControllerTextureIndex() {
            return 178;
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMUV(this.mName, this.TIER_MULTIPLIER);
        }
    }
}
