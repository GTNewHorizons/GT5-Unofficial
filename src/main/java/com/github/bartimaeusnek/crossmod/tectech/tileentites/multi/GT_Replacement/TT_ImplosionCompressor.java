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

import com.github.bartimaeusnek.crossmod.tectech.helper.StructureDefinitions;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.google.common.collect.ArrayListMultimap;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADV_STR_CHECK;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.TT_BLUEPRINT;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class TT_ImplosionCompressor extends TT_Abstract_GT_Replacement {

    public TT_ImplosionCompressor(Object unused, Object unused2) {
        super(1001, "multimachine.implosioncompressor", "Implosion Compressor");
    }

    private TT_ImplosionCompressor(String aName) {
        super(aName);
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
                            onElementPass(
                                    x -> ++x.blocks,
                                    ofBlocksMap(
                                            BLOCKS.asMap(),GregTech_API.sBlockCasings2,
                                            0
                                    )
                            ),
                            ofHatchAdder(
                                TT_ImplosionCompressor::addImplosionHatches,
                                TEXTURE_INDEX,
                                1
                            )
                    )
            ).build();

    public final boolean addImplosionHatches(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
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
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo)
            return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti)
            return this.eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti)
            return this.eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
            return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
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

        setInputFilters();
        return ret;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sImplosionRecipes;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 1,1,0, b, itemStack);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new TT_ImplosionCompressor(this.mName);
    }

    private static final int pollutionPerTick = 500;

    private final static String[] desc = new String[]{
            "Implosion Compressor",
            "Explosions are fun",
            "Controller block for the Implosion Compressor",
            "Creates up to: " + 20 * pollutionPerTick + " Pollution per Second",
            ADV_STR_CHECK,
            TT_BLUEPRINT
    };

    @Override
    public String[] getDescription() {
        return desc;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][TEXTURE_INDEX], new TT_RenderedExtendedFacingTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][TEXTURE_INDEX]};
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
                if (!GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
                    continue;
                }
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
        ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);
        if (tInputList.size() <= 0) {
            return false;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sImplosionRecipes.findRecipe(getBaseMetaTileEntity(), false, 9223372036854775807L, null, tInputs);
        if ((tRecipe == null) || (!tRecipe.isRecipeInputEqual(true, null, tInputs))) {
            return false;
        }
        setEfficiencyAndOc(tRecipe);
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

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == SOUND_INDEX) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(5), 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return pollutionPerTick;
    }

    private static final String[] sfStructureDescription = new String[] {
            "0 - Air",
            "Required: Muffler Hatch, Output Bus, Input Bus, Energy Hatch, Maintenance Hatch",
            "16 Casings at least!"
    };

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return sfStructureDescription;
    }

}
