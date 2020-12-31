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

package com.github.bartimaeusnek.crossmod.tectech.tileentites.multi.GT_Replacement;

import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.crossmod.tectech.helper.StructureDefinitions;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_Container_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.google.common.collect.ArrayListMultimap;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.List;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.*;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class TT_ImplosionCompressor extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    public TT_ImplosionCompressor(Object unused, Object unused2) {
        super(32765, "multimachine.implosioncompressor", "Implosion Compressor");
        GregTech_API.METATILEENTITIES[32765] = null;
        GregTech_API.METATILEENTITIES[1001] = this;
    }

    private TT_ImplosionCompressor(String aName) {
        super(aName);
    }

    @Override
    protected boolean cyclicUpdate_EM() {
        return false;
    }

    private byte blocks = 0;

    private static final byte TEXTURE_INDEX = 16;
    private static final byte SOUND_INDEX = 20;

    private static final ArrayListMultimap<Block, Integer> BLOCKS = ArrayListMultimap.create();

    static {
        BLOCKS.put(GregTech_API.sBlockCasings2,0);
        BLOCKS.put(GregTech_API.sBlockCasings3,4);
    }

    private static final IStructureDefinition<TT_ImplosionCompressor> STRUCTURE_DEFINITION =
            StructureDefinition.<TT_ImplosionCompressor>builder().addShape("main",
                    StructureDefinitions.CUBE_NO_MUFFLER.getDefinition()
            ).addElement(
                    'V',
                    ofChain(
                            ofHatchAdder(
                                TT_ImplosionCompressor::addImplosionHatches,
                                TEXTURE_INDEX,
                                1
                            ),
                            onElementPass(
                                    x -> ++x.blocks,
                                    ofBlocksMap(
                                            BLOCKS.asMap(),GregTech_API.sBlockCasings2,
                                            0
                                    )
                            )
                    )
            ).build();

    public final boolean addImplosionHatches(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch)
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            else
                return false;

            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus)
                return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
            else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
                return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
            else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy)
                return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
            else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
                return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
            else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti)
                return this.eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
            else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti)
                return this.eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
            else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
                return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        return false;
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.blocks = 0;
        boolean ret = this.structureCheck_EM("main", 1,1,0)
                && this.blocks >= 16;
        this.mMufflerHatches.forEach(x -> x.setInValidFacings(this.getExtendedFacing().getRelativeUpInWorld().getOpposite()));

        if (this.mMufflerHatches.stream()
                .map(MetaTileEntity::getBaseMetaTileEntity)
                .mapToInt(ITurnable::getFrontFacing)
                .noneMatch(x -> x == this.getExtendedFacing().getRelativeUpInWorld().ordinal()))
            return false;

        return ret;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 1,1,0, b, itemStack);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new TT_ImplosionCompressor(this.mName);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Implosion Compressor",
                "Explosions are fun",
                "Controller block for the Implosion Compressor",
                "Creates up to: " + 20 * getPollutionPerTick(null) + " Pollution per Second",
                ADV_STR_CHECK,
                TT_BLUEPRINT
        };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][TEXTURE_INDEX], new TT_RenderedExtendedFacingTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][TEXTURE_INDEX]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "EMDisplay.png",false,false,true);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, false, false, true);
    }

    @Override
    public IStructureDefinition<TT_ImplosionCompressor> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        List<ItemStack> tInputList = getStoredInputs();
        int tInputList_sS=tInputList.size();
        for (int i = 0; i < tInputList_sS - 1; i++) {
            for (int j = i + 1; j < tInputList_sS; j++) {
                if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
                    if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
                        tInputList.remove(j--);
                        tInputList_sS=tInputList.size();
                    } else {
                        tInputList.remove(i--);
                        tInputList_sS=tInputList.size();
                        break;
                    }
                }
            }
        }
        ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);
        if (tInputList.size() > 0) {
            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sImplosionRecipes.findRecipe(getBaseMetaTileEntity(), false, 9223372036854775807L, null, tInputs);
            if ((tRecipe != null) && (tRecipe.isRecipeInputEqual(true, null, tInputs))) {
                this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;
                //OC THAT EXPLOSIVE SHIT!!!
                calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, getMaxInputVoltage());
                //In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                    return false;
                if (this.mEUt > 0) {
                    this.mEUt = (-this.mEUt);
                }
                this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0), tRecipe.getOutput(1)};
                sendLoopStart(SOUND_INDEX);
                updateSlots();
                return true;
            }
        }
        return false;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == SOUND_INDEX) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(5), 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 500;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
