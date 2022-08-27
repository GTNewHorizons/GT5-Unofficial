/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import static com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry.BW_BLOCKS;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GT_TileEntity_ManualTrafo extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_ManualTrafo> {

    private byte mode;
    private int mTiers;
    private boolean upstep = true;

    public GT_TileEntity_ManualTrafo(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_ManualTrafo(String aName) {
        super(aName);
    }

    private static final int CASING_INDEX = 2;
    private static final String STRUCTURE_PIECE_BASE = "base";
    private static final String STRUCTURE_PIECE_LAYER = "layer";
    private static final String STRUCTURE_PIECE_TOP = "top";
    private static final String STRUCTURE_PIECE_TAP_LAYER = "taplayer";
    private static final IStructureDefinition<GT_TileEntity_ManualTrafo> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_TileEntity_ManualTrafo>builder()
                    .addShape(STRUCTURE_PIECE_BASE, transpose(new String[][] {{"b~b", "bbb", "bbb"}}))
                    .addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][] {{"ttt", "tft", "ttt"}}))
                    .addShape(STRUCTURE_PIECE_TOP, transpose(new String[][] {{"ooo", "ooo", "ooo"}}))
                    .addShape(
                            STRUCTURE_PIECE_TAP_LAYER,
                            transpose(new String[][] {{" TTT ", "TtttT", "TtftT", "TtttT", " TTT "}}))
                    .addElement(
                            'b',
                            ofChain(
                                    ofHatchAdder(
                                            GT_TileEntity_ManualTrafo::addEnergyInputToMachineList, CASING_INDEX, 1),
                                    ofHatchAdder(
                                            GT_TileEntity_ManualTrafo::addMaintenanceToMachineList, CASING_INDEX, 1),
                                    ofBlock(GregTech_API.sBlockCasings1, 2)))
                    .addElement(
                            'o',
                            ofHatchAdderOptional(
                                    GT_TileEntity_ManualTrafo::addDynamoToMachineList,
                                    CASING_INDEX,
                                    2,
                                    GregTech_API.sBlockCasings1,
                                    2))
                    .addElement('t', ofBlock(BW_BLOCKS[2], 1))
                    .addElement('f', ofBlock(BW_BLOCKS[2], 0))
                    .addElement('T', new IStructureElementNoPlacement<GT_TileEntity_ManualTrafo>() {
                        @Override
                        public boolean check(GT_TileEntity_ManualTrafo te, World world, int x, int y, int z) {
                            if (world.isAirBlock(x, y, z)) return true;
                            TileEntity tileEntity = world.getTileEntity(x, y, z);
                            if (tileEntity == null) return true;
                            if (!(tileEntity instanceof IGregTechTileEntity)) return true;
                            IMetaTileEntity mte = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
                            if (mte instanceof GT_MetaTileEntity_Hatch_Dynamo
                                    || mte instanceof GT_MetaTileEntity_Hatch_Energy) {
                                int intier = te.mEnergyHatches.get(0).mTier;
                                if (((GT_MetaTileEntity_TieredMachineBlock) mte).mTier
                                        == intier + (te.upstep ? te.mTiers : -te.mTiers)) {
                                    te.addToMachineList((IGregTechTileEntity) tileEntity, CASING_INDEX);
                                    return true;
                                } else return false;
                            }
                            return true;
                        }

                        @Override
                        public boolean spawnHint(
                                GT_TileEntity_ManualTrafo te, World world, int x, int y, int z, ItemStack itemStack) {
                            StructureLibAPI.hintParticle(
                                    world, x, y, z, StructureLibAPI.getBlockHint(), 2 /* aDots: 3 */);
                            return true;
                        }
                    })
                    .build();

    @Override
    public IStructureDefinition<GT_TileEntity_ManualTrafo> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Transformer")
                .addInfo("Controller block for the Manual Trafo")
                .addInfo("Operates in 4 diffrent modes:")
                .addInfo("Mode 1: Circuit 0 in controller: Direct-Upstep")
                .addInfo("Mode 2: Circuit 1 in controller: Direct-Downstep")
                .addInfo("Mode 3: Circuit 2 in controller: Tapped-Upstep (currently disabled)")
                .addInfo("Mode 4: Circuit 2 in controller: Tapped-Downstep (currently disabled)")
                .addSeparator()
                .beginVariableStructureBlock(3, 3, 3, 10, 3, 3, false)
                .addController("Front bottom center")
                .addCasingInfo("MV Machine Casing", 0)
                .addOtherStructurePart("Transformer-Winding Blocks", "1 Layer for each tier transformed")
                .addOtherStructurePart("Nickel-Zinc-Ferrite Blocks", "Middle of Transformer-Winding Blocks")
                .addMaintenanceHatch("Any bottom layer casing", 1)
                .addEnergyHatch("Any bottom layer casing", 1)
                .addDynamoHatch("Any top layer casing", 2)
                .addStructureInfo("---------TAPPED MODE---------")
                .addEnergyHatch("Touching Transformer-Winding Blocks", 3)
                .addDynamoHatch("Touching Transformer-Winding Blocks", 3)
                .addStructureInfo("Hatches touching Transformer-Winding Blocks must be tiered from bottom to top")
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (!this.getBaseMetaTileEntity().isAllowedToWork()) this.stopMachine();
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!this.getBaseMetaTileEntity().isAllowedToWork()) {
            this.stopMachine();
            return false;
        }

        this.mProgresstime = 0;
        this.mMaxProgresstime = 1;

        if (this.getBaseMetaTileEntity().getTimer() % 40 == 0)
            if (this.mEfficiency < this.getMaxEfficiency(null)) this.mEfficiency += 100;
            else this.mEfficiency = this.getMaxEfficiency(null);

        if (this.mode > 1) {
            return false; // this.onRunningTickTabbedMode(); Tapped mode is disable
        }

        return this.drainEnergyInput(this.getInputTier() * 2 * this.mEnergyHatches.size())
                && this.addEnergyOutput(this.getInputTier()
                        * 2
                        * this.mEnergyHatches.size()
                        * (long) this.mEfficiency
                        / this.getMaxEfficiency(null));
    }

    public boolean onRunningTickTabbedMode() {
        boolean ret = false;
        for (GT_MetaTileEntity_Hatch_Dynamo E : this.mDynamoHatches) {
            for (GT_MetaTileEntity_Hatch_Energy I : this.mEnergyHatches) {

                long vtt = I.getEUVar() >= (V[E.mTier] / 2) && E.getEUVar() < E.maxEUStore() ? I.getEUVar() : 0;

                if (vtt == 0) continue;

                long vtp = E.getEUVar() + (vtt);
                long avt = Math.min(vtp, E.maxEUStore());
                E.setEUVar(avt);
                I.setEUVar(I.getEUVar() - vtt);
                ret = true;
            }
        }
        return ret;
    }

    public long getInputTier() {
        if (this.mEnergyHatches.size() > 0)
            return GT_Utility.getTier(
                    this.mEnergyHatches.get(0).getBaseMetaTileEntity().getInputVoltage());
        else return 0L;
    }

    public long getOutputTier() {
        if (this.mDynamoHatches.size() > 0)
            return GT_Utility.getTier(
                    this.mDynamoHatches.get(0).getBaseMetaTileEntity().getOutputVoltage());
        else return 0L;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {

        if (!this.getBaseMetaTileEntity().isAllowedToWork()) {
            this.stopMachine();
            return false;
        }
        if (itemStack == null || !itemStack.getUnlocalizedName().startsWith("gt.integrated_circuit")) this.mode = 0;
        else this.mode = (byte) Math.min(3, itemStack.getItemDamage());
        this.upstep = this.mode % 2 == 0;
        this.mProgresstime = 0;
        this.mMaxProgresstime = 1;
        this.mEfficiency = Math.max(this.mEfficiency, 100);
        return this.upstep
                ? this.getOutputTier() - this.getInputTier() == this.mTiers
                : this.getInputTier() - this.getOutputTier() == this.mTiers;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {

        if (itemStack == null || !itemStack.getUnlocalizedName().startsWith("gt.integrated_circuit")) this.mode = 0;
        else this.mode = (byte) Math.min(3, itemStack.getItemDamage());

        this.upstep = this.mode % 2 == 0;
        boolean tapmode = this.mode > 1;

        if (!checkPiece(STRUCTURE_PIECE_BASE, 1, 0, 0)) return false;

        if (this.mEnergyHatches.size() == 0) return false;

        byte intier = this.mEnergyHatches.get(0).mTier;
        for (GT_MetaTileEntity_Hatch_Energy in : this.mEnergyHatches) if (in.mTier != intier) return false;

        int mHeight;
        for (mHeight = 1; mHeight <= 8; mHeight++) {
            if (tapmode) {
                this.mTiers = mHeight;
                if (!checkPiece(STRUCTURE_PIECE_TAP_LAYER, 2, mHeight, 1)) break;
            } else if (!checkPiece(STRUCTURE_PIECE_LAYER, 1, mHeight, 0)) break;
        }
        if (!checkPiece(STRUCTURE_PIECE_TOP, 1, mHeight, 0)) return false;
        this.mTiers = mHeight - 1;

        if (this.mDynamoHatches.size() == 0 || mMaintenanceHatches.size() != 1 || this.mTiers == 0) return false;

        byte outtier = this.mDynamoHatches.get(0).mTier;
        for (GT_MetaTileEntity_Hatch_Dynamo out : this.mDynamoHatches) {
            if (out.mTier != outtier) return false;
        }

        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_ManualTrafo(this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound ntag) {
        super.saveNBTData(ntag);
        ntag.setInteger("mTiers", this.mTiers);
        ntag.setByte("mMode", this.mode);
        ntag.setBoolean("upstep", this.upstep);
    }

    @Override
    public void loadNBTData(NBTTagCompound ntag) {
        super.loadNBTData(ntag);
        this.mTiers = ntag.getInteger("mTiers");
        this.mode = ntag.getByte("mMode");
        this.upstep = ntag.getBoolean("upstep");
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(CASING_INDEX)};
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        if (this.mInventory[1] == null
                || !this.mInventory[1].getUnlocalizedName().startsWith("gt.integrated_circuit")) this.mode = 0;
        else this.mode = (byte) Math.min(3, this.mInventory[1].getItemDamage());
        int mHeight = Math.min(itemStack.stackSize, 8);
        boolean tapmode = this.mode > 1;
        buildPiece(STRUCTURE_PIECE_BASE, itemStack, b, 1, 0, 0);
        for (int i = 0; i < mHeight; i++) {
            if (tapmode) buildPiece(STRUCTURE_PIECE_TAP_LAYER, itemStack, b, 2, i + 1, 1);
            else buildPiece(STRUCTURE_PIECE_LAYER, itemStack, b, 1, i + 1, 0);
        }
        buildPiece(STRUCTURE_PIECE_TOP, itemStack, b, 1, mHeight + 1, 0);
    }
}
