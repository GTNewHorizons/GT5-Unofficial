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

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockCasings4;

public class MTEVoidMiners {

    public static class VMLUV extends MTEVoidMinerBase<VMLUV> {

        private static final String STRUCTURE_PIECE_MAIN = "main";

        private static final IStructureDefinition<VMLUV> STRUCTURE_DEFINITION = StructureDefinition.<VMLUV>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                transpose(
                    new String[][] { { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", " B   B ", "  DAD  ", "  ACA  ", "  DAD  ", " B   B ", "       " },
                        { "  D D  ", " BA~AB ", " A   A ", " B C B ", " A   A ", " BABAB ", "       " },
                        { "  E E  ", " BBBBB ", "EB   BE", " B C B ", "EB   BE", " BBBBB ", "  E E  " } }))
            .addElement('A', ofBlock(GregTechAPI.sBlockCasings4, 14))
            .addElement('B', ofFrame(Materials.Osmiridium))
            .addElement('C', ofFrame(Materials.Iridium))
            .addElement('D', ofFrame(Materials.Bronze))
            .addElement(
                'E',
                buildHatchAdder(VMLUV.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .dot(1)
                    .casingIndex(((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(14))
                    .buildAndChain(GregTechAPI.sBlockCasings4, 14))
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
            return checkPiece(STRUCTURE_PIECE_MAIN, 3, 7, 1) && checkHatches()
                && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
                && mMaintenanceHatches.size() == 1;
        }

        public VMLUV(String aName, int tier) {
            super(aName, tier);
        }

        public VMLUV(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 1);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMLUV(this.mName, this.TIER_MULTIPLIER);
        }
    }

    public static class VMZPM extends MTEVoidMinerBase<VMZPM> {

        private static final String STRUCTURE_PIECE_MAIN = "main";

        private static final IStructureDefinition<VMZPM> STRUCTURE_DEFINITION = StructureDefinition.<VMZPM>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                transpose(
                    new String[][] { { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", " B   B ", "  DAD  ", "  ACA  ", "  DAD  ", " B   B ", "       " },
                        { "  D D  ", " BA~AB ", " A   A ", " B C B ", " A   A ", " BABAB ", "       " },
                        { "  E E  ", " BBBBB ", "EB   BE", " B C B ", "EB   BE", " BBBBB ", "  E E  " } }))
            .addElement('A', ofBlock(GregTechAPI.sBlockCasings4, 14))
            .addElement('B', ofFrame(Materials.Osmiridium))
            .addElement('C', ofFrame(Materials.Iridium))
            .addElement('D', ofFrame(Materials.Bronze))
            .addElement(
                'E',
                buildHatchAdder(VMZPM.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .dot(1)
                    .casingIndex(((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(14))
                    .buildAndChain(GregTechAPI.sBlockCasings4, 14))
            .build();

        @Override
        public IStructureDefinition<VMZPM> getStructureDefinition() {
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
            return checkPiece(STRUCTURE_PIECE_MAIN, 3, 7, 1) && checkHatches()
                && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
                && mMaintenanceHatches.size() == 1;
        }

        public VMZPM(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 2);
        }

        public VMZPM(String aName, int tier) {
            super(aName, tier);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMZPM(this.mName, this.TIER_MULTIPLIER);
        }
    }

    public static class VMUV extends MTEVoidMinerBase<VMUV> {

        private static final String STRUCTURE_PIECE_MAIN = "main";

        private static final IStructureDefinition<VMUV> STRUCTURE_DEFINITION = StructureDefinition.<VMUV>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                transpose(
                    new String[][] { { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "       ", "   B   ", "       ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", "       ", "   B   ", "  BCB  ", "   B   ", "       ", "       " },
                        { "       ", " B   B ", "  DAD  ", "  ACA  ", "  DAD  ", " B   B ", "       " },
                        { "  D D  ", " BA~AB ", " A   A ", " B C B ", " A   A ", " BABAB ", "       " },
                        { "  E E  ", " BBBBB ", "EB   BE", " B C B ", "EB   BE", " BBBBB ", "  E E  " } }))
            .addElement('A', ofBlock(GregTechAPI.sBlockCasings4, 14))
            .addElement('B', ofFrame(Materials.Osmiridium))
            .addElement('C', ofFrame(Materials.Iridium))
            .addElement('D', ofFrame(Materials.Bronze))
            .addElement(
                'E',
                buildHatchAdder(VMUV.class).atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy)
                    .dot(1)
                    .casingIndex(((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(14))
                    .buildAndChain(GregTechAPI.sBlockCasings4, 14))
            .build();

        @Override
        public IStructureDefinition<VMUV> getStructureDefinition() {
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
            return checkPiece(STRUCTURE_PIECE_MAIN, 3, 7, 1) && checkHatches()
                && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
                && mMaintenanceHatches.size() == 1;
        }

        public VMUV(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 3);
        }

        public VMUV(String aName, int tier) {
            super(aName, tier);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMUV(this.mName, this.TIER_MULTIPLIER);
        }
    }
}
