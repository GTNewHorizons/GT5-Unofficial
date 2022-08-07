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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis.mega;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.crossmod.tectech.helper.TecTechUtils;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.util.StatCollector;


import java.util.*;
import java.util.stream.Collectors;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.getMultiOutput;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.handleParallelRecipe;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;

@Optional.Interface(iface = "com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti", modid = "tectech", striprefs = true)
public class GT_TileEntity_MegaBlastFurnace extends GT_TileEntity_MegaMultiBlockBase<GT_TileEntity_MegaBlastFurnace> {

    private static final int CASING_INDEX = 11;
    private static final IStructureDefinition<GT_TileEntity_MegaBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MegaBlastFurnace>builder()
            .addShape("main", createShape())
            .addElement('t', ofHatchAdderOptional(GT_TileEntity_MegaBlastFurnace::addOutputHatchToTopList, CASING_INDEX, 1, GregTech_API.sBlockCasings1, CASING_INDEX))
            .addElement('m', ofHatchAdder(GT_TileEntity_MegaBlastFurnace::addMufflerToMachineList, CASING_INDEX, 2))
            .addElement('C', ofCoil(GT_TileEntity_MegaBlastFurnace::setCoilLevel, GT_TileEntity_MegaBlastFurnace::getCoilLevel))
            .addElement('g', BorosilicateGlass.ofBoroGlass((byte) 0, (byte) 1, Byte.MAX_VALUE, (te, t) -> te.glasTier = t, te -> te.glasTier))
            .addElement('b', ofHatchAdderOptional(GT_TileEntity_MegaBlastFurnace::addBottomHatch, CASING_INDEX, 3, GregTech_API.sBlockCasings1, CASING_INDEX))
            .build();

    private static String[][] createShape() {
        String[][] raw = new String[20][];

        raw[0] = new String[15];
        String topCasing   = "ttttttttttttttt";
        String mufflerLine = "tmmmmmmmmmmmmmt";
        raw[0][0] = topCasing;
        for (int i = 1; i < 14; i++) {
            raw[0][i] = mufflerLine;
        }
        raw[0][14] = topCasing;

        raw[1] = new String[15];
        String allGlass   = "ggggggggggggggg";
        String allCoil    = "gCCCCCCCCCCCCCg";
        String middleLine = "gC-----------Cg";
        raw[1][0] = allGlass;
        raw[1][1] = allCoil;
        raw[1][13] = allCoil;
        raw[1][14] = allGlass;
        for (int i = 2; i < 13; i++) {
            raw[1][i] = middleLine;
        }
        for (int i = 2; i < 19; i++) {
            raw[i] = raw[1];
        }
        String bottomCasing = "bbbbbbbbbbbbbbb";
        raw[19] = new String[15];
        for (int i = 0; i < 15; i++) {
            raw[19][i] = bottomCasing;
        }

        raw[17] = Arrays.copyOf(raw[17], raw[17].length);
        raw[17][0] = "ggggggg~ggggggg";

        return transpose(raw);
    }

    private HeatingCoilLevel mCoilLevel;
    protected final ArrayList<GT_MetaTileEntity_Hatch_Output> mPollutionOutputHatches = new ArrayList<>();
    protected final FluidStack[] pollutionFluidStacks = {Materials.CarbonDioxide.getGas(1000),
        Materials.CarbonMonoxide.getGas(1000), Materials.SulfurDioxide.getGas(1000)};
    private int mHeatingCapacity;
    private byte glasTier;
    private int polPtick = ConfigHandler.basePollutionMBFSecond / 20 * ConfigHandler.megaMachinesMax;
    private int mufflerTier = -1;

    public GT_TileEntity_MegaBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_MegaBlastFurnace(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Blast Furnace")
                .addInfo("Controller block for the Mega Blast Furnace")
                .addInfo("You can use some fluids to reduce recipe time. Place the circuit in the Input Bus")
                .addInfo("Each 900K over the min. Heat required reduces power consumption by 5% (multiplicatively)")
                .addInfo("Each 1800K over the min. Heat required grants one perfect overclock")
                .addInfo("For each perfect overclock the EBF will reduce recipe time 4 times (instead of 2) (100% efficiency)")
                .addInfo("Additionally gives +100K for every tier past MV")
                .addPollutionAmount(20 * getPollutionPerTick(null))
                .addSeparator()
                .beginStructureBlock(15, 20, 15, true)
                .addController("3rd layer center")
                .addCasingInfo("Heat Proof Machine Casing", 0)
                .addOtherStructurePart("864x Heating Coils", "Inner 13x18x13 (Hollow)")
                .addOtherStructurePart("1007x Borosilicate Glass", "Outer 15x18x15")
                .addStructureInfo("The glass tier limits the Energy Input tier")
                .addEnergyHatch("Any bottom layer casing")
                .addMaintenanceHatch("Any bottom layer casing")
                .addMufflerHatch("Top middle 13x13")
                .addInputBus("Any bottom layer casing")
                .addInputHatch("Any bottom layer casing")
                .addOutputBus("Any bottom layer casing")
                .addOutputHatch("Gasses, Any top layer casing")
                .addStructureInfo("Recovery amount scales with Muffler Hatch tier")
                .addOutputHatch("Platline fluids, Any bottom layer casing")
                .addStructureHint("This Mega Multiblock is too big to have its structure hologram displayed fully.")
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS);
        return tt;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.circuitMode = aNBT.getByte("circuitMode");
        this.glasTier = aNBT.getByte("glasTier");
        this.isBussesSeparate = aNBT.getBoolean("isBussesSeparate");
    }

    private byte circuitMode = 0;
    private boolean isBussesSeparate = false;

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            --circuitMode;
            if (circuitMode < 0)
                circuitMode = 24;
        } else {
            ++circuitMode;
            if (circuitMode > 24)
                circuitMode = 0;
        }

        GT_Utility.sendChatToPlayer(aPlayer, circuitMode > 0 ? "MEBF will prioritise circuit: " + circuitMode : "Circuit prioritisation disabled.");
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        isBussesSeparate = !isBussesSeparate;
        GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + isBussesSeparate);
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[]{
                    casingTexturePages[0][CASING_INDEX],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                casingTexturePages[0][CASING_INDEX],
                TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{casingTexturePages[0][CASING_INDEX]};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("glasTier", glasTier);
        aNBT.setByte("circuitMode", circuitMode);
        aNBT.setBoolean("isBussesSeparate", isBussesSeparate);
    }

    @Override
    public boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if(super.addMufflerToMachineList(aTileEntity, aBaseCasingIndex))
        {
            if(mufflerTier == -1)
                mufflerTier = this.mMufflerHatches.get(this.mMufflerHatches.size() - 1).mTier;
            return mufflerTier == this.mMufflerHatches.get(this.mMufflerHatches.size() - 1).mTier;
        }
        return false;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return this.polPtick;
    }

    public boolean addOutputHatchToTopList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mPollutionOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        return false;
    }

    protected boolean addBottomHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addMaintenanceToMachineList(aTileEntity, aBaseCasingIndex) ||
            addInputToMachineList(aTileEntity, aBaseCasingIndex) ||
            addOutputToMachineList(aTileEntity, aBaseCasingIndex) ||
            addEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ElectricBlastFurnace.png");
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        ItemStack[] tInputs = null;
        FluidStack[] tFluids = this.getStoredFluids().toArray(new FluidStack[0]);
        long nominalV = LoaderReference.tectech ? TecTechUtils.getnominalVoltageTT(this) : BW_Util.getnominalVoltage(this);

        byte tTier = (byte) Math.max(1, Math.min(GT_Utility.getTier(nominalV), V.length - 1));
        GT_Recipe tRecipe = null;

        if (isBussesSeparate) {
            for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
                ArrayList<ItemStack> tInputList = new ArrayList<>();
                tBus.mRecipeMap = getRecipeMap();

                if (isValidMetaTileEntity(tBus)) {
                    for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                        if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null) {
                            tInputList.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
                        }
                    }
                }
                tInputs = Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, tInputList.size());
                tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(this.getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
                if ((tRecipe != null) && (tRecipe.isRecipeInputEqual(false, tFluids, tInputs))) {
                    break;
                }
            }
        } else {
            tInputs = this.getStoredInputs().toArray(new ItemStack[0]);
            if (circuitMode > 0 && Arrays.stream(tInputs).anyMatch(e -> GT_Utility.areStacksEqual(e, GT_Utility.getIntegratedCircuit(circuitMode), true))) {
                List<ItemStack> modInputs = Arrays.stream(tInputs).filter(Objects::nonNull).filter(e -> !e.getItem().equals(GT_Utility.getIntegratedCircuit(circuitMode).getItem())).collect(Collectors.toList());
                modInputs.add(GT_Utility.getIntegratedCircuit(circuitMode));
                tInputs = modInputs.toArray(new ItemStack[0]);
            }
            tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(this.getBaseMetaTileEntity(), false, V[tTier], tFluids, tInputs);
            if (tRecipe == null) {
                if (circuitMode == 0)
                    return false;
                tInputs = this.getStoredInputs().toArray(new ItemStack[0]);
                tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(this.getBaseMetaTileEntity(), false, V[tTier], tFluids, tInputs);
                if (tRecipe == null)
                    return false;
            }
        }

        ArrayList<ItemStack> outputItems = new ArrayList<>();
        ArrayList<FluidStack> outputFluids = new ArrayList<>();

        boolean found_Recipe = false;
        int processed = 0;

        int tHeatCapacityDivTiers = (this.mHeatingCapacity - tRecipe.mSpecialValue) / 900;
        long precutRecipeVoltage = (long) (tRecipe.mEUt * Math.pow(0.95, tHeatCapacityDivTiers));

        long tMaxPara = Math.min(ConfigHandler.megaMachinesMax, nominalV / precutRecipeVoltage);

        if (this.mHeatingCapacity >= tRecipe.mSpecialValue) {
            int tCurrentPara = handleParallelRecipe(tRecipe, tFluids, tInputs, (int) tMaxPara);
            this.updateSlots();
            if (tCurrentPara <= 0) return false;
            processed = tCurrentPara;
            found_Recipe = true;
            Pair<ArrayList<FluidStack>, ArrayList<ItemStack>> Outputs = getMultiOutput(tRecipe, tCurrentPara);
            outputFluids = Outputs.getKey();
            outputItems = Outputs.getValue();
        }

        if (found_Recipe) {
            this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;

            long actualEUT = precutRecipeVoltage * processed;
            byte overclockCount = this.calculateOverclockedNessMultiInternal(actualEUT, tRecipe.mDuration, nominalV, false);

            //In case recipe is too OP for that machine
            if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.lEUt == Integer.MAX_VALUE - 1)
                return false;

            if (this.lEUt > 0)
                this.lEUt = (-this.lEUt);

            if (tHeatCapacityDivTiers > 0) {
                this.mMaxProgresstime >>= Math.min(tHeatCapacityDivTiers / 2, overclockCount); //extra free overclocking if possible
                if (this.mMaxProgresstime < 1)
                    this.mMaxProgresstime = 1; //no eu efficiency correction
            }

            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

            this.polPtick = ConfigHandler.basePollutionMBFSecond / 20 * processed;
            this.mOutputItems = new ItemStack[outputItems.size()];
            this.mOutputItems = outputItems.toArray(this.mOutputItems);
            this.mOutputFluids = new FluidStack[outputFluids.size()];
            this.mOutputFluids = outputFluids.toArray(this.mOutputFluids);
            return true;
        }
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    public IStructureDefinition<GT_TileEntity_MegaBlastFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 7, 17, 0);
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mCoilLevel = aCoilLevel;
    }

    public HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null)
            return false;
        FluidStack tLiquid = aLiquid.copy();
        boolean isOutputPollution = false;
        for (FluidStack pollutionFluidStack : pollutionFluidStacks) {
            if (!tLiquid.isFluidEqual(pollutionFluidStack))
                continue;

            isOutputPollution = true;
            break;
        }
        ArrayList<GT_MetaTileEntity_Hatch_Output> tOutputHatches;
        if (isOutputPollution) {
            tOutputHatches = this.mPollutionOutputHatches;
            int pollutionReduction = 0;
            for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
                if (!isValidMetaTileEntity(tHatch))
                    continue;
                pollutionReduction = 100 - tHatch.calculatePollutionReduction(100);
                break;
            }
            tLiquid.amount = tLiquid.amount * (pollutionReduction + 5) / 100;
        } else {
            tOutputHatches = this.mOutputHatches;
        }
        return dumpFluid(tOutputHatches, tLiquid, true) ||
            dumpFluid(tOutputHatches, tLiquid, false);
    }


    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if (LoaderReference.tectech) {
            this.getTecTechEnergyMultis().clear();
            this.getTecTechEnergyTunnels().clear();
        }

        this.mHeatingCapacity = 0;
        glasTier = 0;
        mufflerTier = -1;

        setCoilLevel(HeatingCoilLevel.None);

        this.mPollutionOutputHatches.clear();

        if (!checkPiece("main", 7, 17, 0))
            return false;

        if (getCoilLevel() == HeatingCoilLevel.None)
            return false;

        if (mMaintenanceHatches.size() != 1)
            return false;

        if (LoaderReference.tectech && this.glasTier < 8)
            if (!areLazorsLowPowa() || areThingsNotProperlyTiered(this.getTecTechEnergyTunnels()) || areThingsNotProperlyTiered(this.getTecTechEnergyMultis()))
                return false;

        if (this.glasTier < 8 && !this.mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches)
                if (this.glasTier < hatchEnergy.mTier)
                    return false;

        this.mHeatingCapacity = (int) getCoilLevel().getHeat() + 100 * (GT_Utility.getTier(getMaxInputVoltage()) - 2);

        return true;
    }

    @SuppressWarnings("rawtypes")
    @Optional.Method(modid = "tectech")
    private boolean areThingsNotProperlyTiered(Collection collection) {
        if (!collection.isEmpty())
            for (Object tecTechEnergyMulti : collection)
                if (((GT_MetaTileEntity_TieredMachineBlock) tecTechEnergyMulti).mTier > this.glasTier)
                    return true;
        return false;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sBlastRecipes;
    }


}
