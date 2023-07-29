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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis.mega;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_TileEntity_MegaBlastFurnace extends GT_TileEntity_MegaMultiBlockBase<GT_TileEntity_MegaBlastFurnace>
        implements ISurvivalConstructable {

    private static final int CASING_INDEX = 11;
    private static final IStructureDefinition<GT_TileEntity_MegaBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition
            .<GT_TileEntity_MegaBlastFurnace>builder().addShape("main", createShape())
            .addElement('=', StructureElementAirNoHint.getInstance())
            .addElement(
                    't',
                    buildHatchAdder(GT_TileEntity_MegaBlastFurnace.class)
                            .atLeast(
                                    OutputHatch.withAdder(GT_TileEntity_MegaBlastFurnace::addOutputHatchToTopList)
                                            .withCount(t -> t.mPollutionOutputHatches.size()))
                            .casingIndex(CASING_INDEX).dot(1).buildAndChain(GregTech_API.sBlockCasings1, CASING_INDEX))
            .addElement('m', Muffler.newAny(CASING_INDEX, 2))
            .addElement(
                    'C',
                    withChannel(
                            "coil",
                            ofCoil(
                                    GT_TileEntity_MegaBlastFurnace::setCoilLevel,
                                    GT_TileEntity_MegaBlastFurnace::getCoilLevel)))
            .addElement(
                    'g',
                    withChannel(
                            "glass",
                            BorosilicateGlass.ofBoroGlass(
                                    (byte) 0,
                                    (byte) 1,
                                    Byte.MAX_VALUE,
                                    (te, t) -> te.glassTier = t,
                                    te -> te.glassTier)))
            .addElement(
                    'b',
                    buildHatchAdder(GT_TileEntity_MegaBlastFurnace.class)
                            .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                            .casingIndex(CASING_INDEX).dot(1).buildAndChain(GregTech_API.sBlockCasings1, CASING_INDEX))
            .build();

    private static String[][] createShape() {
        String[][] raw = new String[20][];

        raw[0] = new String[15];
        String topCasing = "ttttttttttttttt";
        String middleTopCasing = "tttttttmttttttt";
        raw[0][0] = topCasing;
        for (int i = 1; i < 15; i++) {
            raw[0][i] = topCasing;
        }
        raw[0][7] = middleTopCasing;

        raw[1] = new String[15];
        String allGlass = "ggggggggggggggg";
        String allCoil = "gCCCCCCCCCCCCCg";
        String middleLine = "gC===========Cg";
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
    protected final FluidStack[] pollutionFluidStacks = { Materials.CarbonDioxide.getGas(1000),
            Materials.CarbonMonoxide.getGas(1000), Materials.SulfurDioxide.getGas(1000) };
    private int mHeatingCapacity;
    private byte glassTier;
    private final static int polPtick = ConfigHandler.basePollutionMBFSecond / 20 * ConfigHandler.megaMachinesMax;

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
        tt.addMachineType("Blast Furnace").addInfo("Controller block for the Mega Blast Furnace")
                .addInfo("You can use some fluids to reduce recipe time. Place the circuit in the Input Bus")
                .addInfo("Each 900K over the min. Heat required reduces power consumption by 5% (multiplicatively)")
                .addInfo("Each 1800K over the min. Heat required grants one perfect overclock")
                .addInfo(
                        "For each perfect overclock the EBF will reduce recipe time 4 times (instead of 2) (100% efficiency)")
                .addInfo("Additionally gives +100K for every tier past MV")
                .addPollutionAmount(20 * getPollutionPerTick(null)).addSeparator().beginStructureBlock(15, 20, 15, true)
                .addController("3rd layer center").addCasingInfoRange("Heat Proof Machine Casing", 0, 279, false)
                .addOtherStructurePart("864x Heating Coils", "Inner 13x18x13 (Hollow)")
                .addOtherStructurePart("1007x Borosilicate Glass", "Outer 15x18x15")
                .addStructureInfo("The glass tier limits the Energy Input tier")
                .addEnergyHatch("Any bottom layer casing").addMaintenanceHatch("Any bottom layer casing")
                .addMufflerHatch("Top middle").addInputBus("Any bottom layer casing")
                .addInputHatch("Any bottom layer casing").addOutputBus("Any bottom layer casing")
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
        this.glassTier = aNBT.getByte("glasTier");
        if (!aNBT.hasKey(INPUT_SEPARATION_NBT_KEY)) {
            inputSeparation = aNBT.getBoolean("isBussesSeparate");
        }
        if (!aNBT.hasKey(BATCH_MODE_NBT_KEY)) {
            batchMode = aNBT.getBoolean("mUseMultiparallelMode");
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
            float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        } else {
            inputSeparation = !inputSeparation;
            GT_Utility.sendChatToPlayer(
                    aPlayer,
                    StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
            return true;
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][CASING_INDEX],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW).extFacing()
                            .glow().build() };
            return new ITexture[] { casingTexturePages[0][CASING_INDEX],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW).extFacing().glow()
                            .build() };
        }
        return new ITexture[] { casingTexturePages[0][CASING_INDEX] };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("glasTier", glassTier);
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return polPtick;
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

    @Override
    protected String[] getExtendedInfoData() {
        return new String[] { StatCollector.translateToLocal("GT5U.EBF.heat") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mHeatingCapacity)
                + EnumChatFormatting.RESET
                + " K" };
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe).setRecipeHeat(recipe.mSpecialValue)
                        .setMultiHeat(mHeatingCapacity).setHeatOC(true).setHeatDiscount(true);
            }

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
                return recipe.mSpecialValue <= mHeatingCapacity ? CheckRecipeResultRegistry.SUCCESSFUL
                        : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }
        }.setMaxParallel(ConfigHandler.megaMachinesMax);
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

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        glassTier = 0;
        setCoilLevel(HeatingCoilLevel.None);
        return survivialBuildPiece("main", stackSize, 7, 17, 0, realBudget, source, actor, false, true);
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mCoilLevel = aCoilLevel;
    }

    public HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        FluidStack tLiquid = aLiquid.copy();
        boolean isOutputPollution = false;
        for (FluidStack pollutionFluidStack : pollutionFluidStacks) {
            if (!tLiquid.isFluidEqual(pollutionFluidStack)) continue;

            isOutputPollution = true;
            break;
        }
        ArrayList<GT_MetaTileEntity_Hatch_Output> tOutputHatches;
        if (isOutputPollution) {
            tOutputHatches = this.mPollutionOutputHatches;
            int pollutionReduction = 0;
            for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
                if (!isValidMetaTileEntity(tHatch)) continue;
                pollutionReduction = 100 - tHatch.calculatePollutionReduction(100);
                break;
            }
            tLiquid.amount = tLiquid.amount * (pollutionReduction + 5) / 100;
        } else {
            tOutputHatches = this.mOutputHatches;
        }
        return dumpFluid(tOutputHatches, tLiquid, true) || dumpFluid(tOutputHatches, tLiquid, false);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.mHeatingCapacity = 0;
        glassTier = 0;

        setCoilLevel(HeatingCoilLevel.None);

        this.mPollutionOutputHatches.clear();

        if (!checkPiece("main", 7, 17, 0)) return false;

        if (getCoilLevel() == HeatingCoilLevel.None) return false;

        if (mMaintenanceHatches.size() != 1) return false;

        if (glassTier < 8) {
            for (GT_MetaTileEntity_Hatch hatch : mExoticEnergyHatches) {
                if (hatch.getConnectionType() == GT_MetaTileEntity_Hatch.ConnectionType.LASER) {
                    return false;
                }
                if (glassTier < hatch.mTier) {
                    return false;
                }
            }
        }

        this.mHeatingCapacity = (int) getCoilLevel().getHeat() + 100 * (BW_Util.getTier(getMaxInputEu()) - 2);

        return true;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sBlastRecipes;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
