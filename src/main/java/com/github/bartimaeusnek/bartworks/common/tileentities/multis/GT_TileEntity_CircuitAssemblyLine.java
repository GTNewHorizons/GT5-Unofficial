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

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.BW_Meta_Items;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;

public class GT_TileEntity_CircuitAssemblyLine extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_CircuitAssemblyLine> {

    private static final IStructureDefinition<GT_TileEntity_CircuitAssemblyLine> STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_CircuitAssemblyLine>builder()
            .addShape("first", transpose(new String[][] {
                    {"~", "G", "G"},
                    {"g", "l", "g"},
                    {"b", "i", "b"},
            }))
            .addShape("next", transpose(new String[][] {
                    {"G", "G", "G"},
                    {"g", "l", "g"},
                    {"b", "I", "b"},
            }))
            .addElement('G', ofChain(
                    ofHatchAdder(GT_TileEntity_CircuitAssemblyLine::addEnergyInputToMachineList, 16, 1), //grate machine casings
                    ofBlock(GregTech_API.sBlockCasings3, 10)))
            .addElement('g', ofChain(
                    ofBlockAnyMeta(GameRegistry.findBlock("IC2", "blockAlloyGlass")),
                    //Forgive me for I have sinned. But it works...
                    ofBlock(ItemRegistry.bw_realglas, 1),
                    ofBlock(ItemRegistry.bw_realglas, 2),
                    ofBlock(ItemRegistry.bw_realglas, 3),
                    ofBlock(ItemRegistry.bw_realglas, 4),
                    ofBlock(ItemRegistry.bw_realglas, 5),
                    ofBlock(ItemRegistry.bw_realglas, 12),
                    ofBlock(ItemRegistry.bw_realglas, 13),
                    ofBlock(ItemRegistry.bw_realglas, 14)
            ))
            .addElement('l', ofBlock(GregTech_API.sBlockCasings2, 5)) //assembling line casings
            .addElement('b', ofChain(
                    ofHatchAdder(GT_TileEntity_CircuitAssemblyLine::addMaintenanceToMachineList, 16, 2),
                    ofHatchAdder(GT_TileEntity_CircuitAssemblyLine::addInputHatchToMachineList, 16, 2),
                    ofBlock(GregTech_API.sBlockCasings2, 0)
            ))
            .addElement('i', ofHatchAdder(GT_TileEntity_CircuitAssemblyLine::addInputToMachineList, 16, 3))
            .addElement('I', ofChain(
                    ofHatchAdder(GT_TileEntity_CircuitAssemblyLine::addInputToMachineList, 16, 4),
                    ofHatchAdder(GT_TileEntity_CircuitAssemblyLine::addOutputToMachineList, 16, 4)
            ))
            .build();

    @Override
    public IStructureDefinition<GT_TileEntity_CircuitAssemblyLine> getStructureDefinition() { return STRUCTURE_DEFINITION; }

    //I can't get this to work properly...
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return new GT_Multiblock_Tooltip_Builder();
    }

    public String getTypeForDisplay() {
        if (this.type.equals(new NBTTagCompound()))
            return "";
        return GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(CircuitImprintLoader.getStackFromTag(this.type)));
    }

    private NBTTagCompound type = new NBTTagCompound();
    private GT_Recipe bufferedRecipe;

    public GT_TileEntity_CircuitAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private GT_TileEntity_CircuitAssemblyLine(String aName) {
        super(aName);
    }

    private boolean imprintMachine(ItemStack itemStack){
        if (!this.type.equals(new NBTTagCompound()))
            return true;
        if (!GT_Utility.isStackValid(itemStack))
            return false;
        if (itemStack.getItem() instanceof BW_Meta_Items.BW_GT_MetaGenCircuits && itemStack.getItemDamage() == 0 && itemStack.getTagCompound() != null && this.type.equals(new NBTTagCompound())){
            this.type = itemStack.getTagCompound();
            this.mInventory[1] = null;
            this.getBaseMetaTileEntity().issueBlockUpdate();
            return true;
        }
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 20) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(212), 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.type = aNBT.getCompoundTag("Type");
        super.loadNBTData(aNBT);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (!this.type.equals(new NBTTagCompound()))
            aNBT.setTag("Type", this.type);
        super.saveNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (!this.type.equals(new NBTTagCompound()))
            aNBT.setTag("Type", this.type);
        super.saveNBTData(aNBT);
    }

    private final Collection<GT_Recipe> GT_RECIPE_COLLECTION = new HashSet<>();

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        if (this.type.equals(new NBTTagCompound()))
            if (!this.imprintMachine(itemStack))
                return false;

        if (this.bufferedRecipe != null && this.bufferedRecipe.isRecipeInputEqual(true,false, BW_Util.getFluidsFromInputHatches(this), getStoredInputs().toArray(new ItemStack[0]))) {
            setRecipeStats();
            return true;
        }

        ItemStack stack = ItemStack.loadItemStackFromNBT(this.type);

        if (stack == null)
            return false;

        if (this.GT_RECIPE_COLLECTION.isEmpty()) {
            for (GT_Recipe recipe : BWRecipes.instance.getMappingsFor((byte) 3).mRecipeList) {
                if (GT_Utility.areStacksEqual(recipe.mOutputs[0], stack, true)) {
                    this.GT_RECIPE_COLLECTION.add(recipe);
                }
            }
        }

        for (GT_Recipe recipe : this.GT_RECIPE_COLLECTION) {
            if (recipe.isRecipeInputEqual(true,false, BW_Util.getFluidsFromInputHatches(this), getStoredInputs().toArray(new ItemStack[0])))
                this.bufferedRecipe = recipe;
            else
                continue;

            this.setRecipeStats();
            return true;
        }
        return false;
    }

    private void setRecipeStats() {
        calculatePerfectOverclockedNessMulti(this.bufferedRecipe.mEUt, this.bufferedRecipe.mDuration, 1, this.getMaxInputVoltage());
        if (this.mEUt > 0)
            this.mEUt = -this.mEUt;
        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
        this.mOutputItems = this.bufferedRecipe.mOutputs;
        this.mOutputFluids = this.bufferedRecipe.mFluidOutputs;
        sendLoopStart((byte) 20);
        this.updateSlots();
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = 0; i < tHatch.getBaseMetaTileEntity().getSizeInventory(); i++) {
                    if (tHatch.getBaseMetaTileEntity().getStackInSlot(i) != null) {
                        rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
                        break;
                    }
                }
            }
        }
        return rList;
    }

    @Override
    public boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                ((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity).mRecipeMap = this.getRecipeMap();
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                ((GT_MetaTileEntity_Hatch_InputBus)aMetaTileEntity).mRecipeMap = this.getRecipeMap();
                return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus)aMetaTileEntity);
            } else {
                return false;
            }
        }
    }

    private boolean addInputHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                ((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity).mRecipeMap = this.getRecipeMap();
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity);
            } else {
                return false;
            }
        }
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
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_CircuitAssemblyLine(this.mName);
    }

    private static final String[] DESCRIPTION = new String[]{
            "Circuit Assembly Line", "Size(WxHxD): (2-7)x3x3, variable length",
            "Bottom: Steel Machine Casing(or 1x Maintenance or Input Hatch),",
            "Input Bus (Last Output Bus), Steel Machine Casing",
            "Middle: EV+ Tier Glass, Assembling Line Casing, EV+ Tier Glass",
            "Top: Grate Machine Casing (or Controller or 1x Energy Hatch)",
            "Up to 7 repeating slices, last is Output Bus",
            "Imprint this machine with a Circuit Imprint,",
            "by putting the imprint in the controller.",
            "Every Circuit Assembly Line can only be imprinted ONCE.",
            ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get()
    };

    @Override
    public String[] getDescription() {
        return DESCRIPTION;
    }

    private String[] infoDataBuffer;
    @Override
    public String[] getInfoData() {
        if (infoDataBuffer != null)
            return infoDataBuffer;

        String[] oldInfo = super.getInfoData();
        infoDataBuffer = new String[oldInfo.length+1];
        System.arraycopy(oldInfo,0,infoDataBuffer,0, oldInfo.length);
        infoDataBuffer[oldInfo.length] = "Imprinted with: "+ GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(CircuitImprintLoader.getStackFromTag(this.type)));
        return infoDataBuffer;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[]{
                    Textures.BlockIcons.getCasingTextureForId(16),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                Textures.BlockIcons.getCasingTextureForId(16),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_ASSEMBLY_LINE).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(16)};
    }

    private static final String[] description = new String[] {
            translateToLocal("BW.keyphrase.Hint_Details") + ":",
            translateToLocal("BW.tile.CircuitAssemblyLine.hint.0"), //1 - Energy Input Hatch
            translateToLocal("BW.tile.CircuitAssemblyLine.hint.1"), //2 - Maintenance Hatch, Input Hatch
            translateToLocal("BW.tile.CircuitAssemblyLine.hint.2"), //3 - Input Bus
            translateToLocal("BW.tile.CircuitAssemblyLine.hint.3"), //4 - Input Bus, Output Bus
    };

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!this.checkPiece("first", 0, 0, 0)) {
            return false;
        } else {
            return this.checkMachine(true) || this.checkMachine(false);
        }
    }

    private boolean checkMachine(boolean leftToRight) {
        for(int i = 1; i < 7; ++i) {
            if (!this.checkPiece("next", leftToRight ? -i : i, 0, 0)) {
                return false;
            }

            if (!this.mOutputBusses.isEmpty()) {
                return this.mEnergyHatches.size() == 1 && this.mMaintenanceHatches.size() == 1;
            }
        }

        return false;
    }

    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece("first", stackSize, hintsOnly, 0, 0, 0);
        int tLength = Math.min(stackSize.stackSize + 1, 7);

        for(int i = 1; i < tLength; ++i) {
            this.buildPiece("next", stackSize, hintsOnly, -i, 0, 0);
        }

    }
}
