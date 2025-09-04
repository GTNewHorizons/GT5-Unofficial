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

package bartworks.common.tileentities.multis.mega;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;

public class MTEMegaBlastFurnace extends MTEExtendedPowerMultiBlockBase<MTEMegaBlastFurnace>
    implements ISurvivalConstructable {

    private int mCasingAmount;
    private int mHeatingCapacity;
    private HeatingCoilLevel mCoilLevel = HeatingCoilLevel.None;
    private byte glassTier = -1;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMegaBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMegaBlastFurnace>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{
                {"BBBBBBBBBBBBBBB", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAA~AAAAAAA", "AAAAAAAAAAAAAAA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBMBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "AC           CA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "ACCCCCCCCCCCCCA", "BBBBBBBBBBBBBBB"},
                {"BBBBBBBBBBBBBBB", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "BBBBBBBBBBBBBBB"}})
        //spotless:on
        .addElement(
            'B',
            buildHatchAdder(MTEMegaBlastFurnace.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(Casings.HeatProofCasing.textureId)
                .dot(1)
                .buildAndChain(onElementPass(MTEMegaBlastFurnace::onCasingAdded, Casings.HeatProofCasing.asElement())))
        .addElement(
            'C',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTEMegaBlastFurnace::setCoilLevel, MTEMegaBlastFurnace::getCoilLevel))))
        .addElement('A', chainAllGlasses())
        .addElement('M', HatchElement.Muffler.newAny(Casings.HeatProofCasing.textureId, 2))
        .build();

    public MTEMegaBlastFurnace(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEMegaBlastFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaBlastFurnace(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Casings.HeatProofCasing.getCasingTexture(), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Casings.HeatProofCasing.getCasingTexture(), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Casings.HeatProofCasing.getCasingTexture() };
        }
        return rTexture;
    }

    // spotless:off
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Blast Furnace, MEBF, MBF")
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addInfo(
                "Increases Heat by " + EnumChatFormatting.RED
                    + "100K"
                    + EnumChatFormatting.GRAY
                    + " for every "
                    + TooltipHelper.tierText("Voltage")
                    + " tier past "
                    + EnumChatFormatting.AQUA
                    + "MV")
            .addInfo(
                "Reduces " + TooltipHelper.effText("EU Usage")
                    + " by "
                    + EnumChatFormatting.WHITE
                    + "5%"
                    + EnumChatFormatting.GRAY
                    + " every "
                    + EnumChatFormatting.RED
                    + "900K"
                    + EnumChatFormatting.GRAY
                    + " above the recipe requirement")
            .addInfo(
                "Every " + EnumChatFormatting.RED
                    + "1800K"
                    + EnumChatFormatting.GRAY
                    + " over the recipe requirement grants 1 "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Perfect Overclock")
            .addTecTechHatchInfo()
            .addGlassEnergyLimitInfo()
            .addMinGlassForLaser(VoltageIndex.UV)
            .addUnlimitedTierSkips()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(15, 20, 15, true)
            .addController("3rd layer center")
            .addCasingInfoRange("Heat Proof Machine Casing", 0, 447, false)
            .addCasingInfoExactly("Heating Coils", 864, true)
            .addCasingInfoExactly("Any Tiered Glass", 1007, true)
            .addStructureInfo("The glass tier limits the Energy Input tier")
            .addEnergyHatch("Any bottom layer casing")
            .addMaintenanceHatch("Any bottom layer casing")
            .addMufflerHatch("Top middle")
            .addInputBus("Any bottom layer casing")
            .addInputHatch("Any bottom layer casing")
            .addOutputBus("Any bottom layer casing")
            .addOutputHatch("Any Heat Proof Machine Casing")
            .addStructureHint("This Mega Multiblock is too big to have its structure hologram displayed fully.")
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        return tt;
    }
    // spotless:on

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 17, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        this.glassTier = -1;
        this.setCoilLevel(HeatingCoilLevel.None);
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 17, 0, realBudget, env, false, true);
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        this.mCoilLevel = aCoilLevel;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.mCoilLevel;
    }

    public void setGlassTier(byte tier) {
        this.glassTier = tier;
    }

    public byte getGlassTier() {
        return this.glassTier;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mHeatingCapacity = 0;
        glassTier = -1;
        setCoilLevel(HeatingCoilLevel.None);
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 7, 17, 0)) return false;
        if (mMaintenanceHatches.size() != 1) return false;
        if (getCoilLevel() == HeatingCoilLevel.None) return false;
        if (mCasingAmount < 48) return false;

        for (MTEHatchEnergy hatch : mEnergyHatches) {
            if (hatch.mTier < glassTier) {
                return false;
            }
        }
        if (glassTier < VoltageIndex.UV) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
                if (hatch.getConnectionType() == MTEHatch.ConnectionType.LASER) {
                    return false;
                }
            }
        }
        mHeatingCapacity = (int) getCoilLevel().getHeat() + 100 * (GTUtility.getTier(getMaxInputEu()) - 2);
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setRecipeHeat(recipe.mSpecialValue)
                    .setMachineHeat(MTEMegaBlastFurnace.this.mHeatingCapacity)
                    .setHeatOC(true)
                    .setHeatDiscount(true);
            }

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                return recipe.mSpecialValue <= MTEMegaBlastFurnace.this.mHeatingCapacity
                    ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }
        }.setMaxParallelSupplier(this::getTrueParallel)
            .setAvailableVoltage(this.getMaxInputEu())
            .setAvailableAmperage(1)
            .setUnlimitedTierSkips();
    }

    @Override
    public String[] getInfoData() {
        super.getInfoData();
        return new String[] { StatCollector.translateToLocal("GT5U.EBF.heat") + ": "
            + EnumChatFormatting.GREEN
            + GTUtility.formatNumbers(this.mHeatingCapacity)
            + EnumChatFormatting.RESET
            + " K" };
    }

    @Override
    public int getMaxParallelRecipes() {
        return Configuration.Multiblocks.megaMachinesMax;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.blastFurnaceRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -2;
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MEGA_BLAST_FURNACE_LOOP;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.basePollutionMBFSecond;
    }
}
