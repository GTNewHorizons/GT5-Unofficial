package com.elisis.gtnhlanth.common.tileentity;

import static com.elisis.gtnhlanth.util.DescTextLocalization.BLUEPRINT_INFO;
import static com.elisis.gtnhlanth.util.DescTextLocalization.addDotText;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.elisis.gtnhlanth.common.beamline.BeamInformation;
import com.elisis.gtnhlanth.common.beamline.BeamLinePacket;
import com.elisis.gtnhlanth.common.beamline.Particle;
import com.elisis.gtnhlanth.common.hatch.TileHatchOutputBeamline;
import com.elisis.gtnhlanth.common.register.LanthItemList;
import com.elisis.gtnhlanth.common.tileentity.recipe.beamline.BeamlineRecipeAdder2;
import com.elisis.gtnhlanth.common.tileentity.recipe.beamline.RecipeSC;
import com.elisis.gtnhlanth.util.DescTextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.SimpleShutDownReason;

public class SourceChamber extends GT_MetaTileEntity_EnhancedMultiBlockBase<SourceChamber>
    implements ISurvivalConstructable {

    private static final IStructureDefinition<SourceChamber> STRUCTURE_DEFINITION;

    private ArrayList<TileHatchOutputBeamline> mOutputBeamline = new ArrayList<>();

    private static final int CASING_INDEX = GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings5, 14);

    private float outputEnergy;
    private int outputRate;
    private int outputParticle;
    private float outputFocus;

    static {
        STRUCTURE_DEFINITION = StructureDefinition.<SourceChamber>builder()
            .addShape(
                "sc",
                new String[][] { { "ccccc", "ckkkc", "ckikc", "ckkkc", "dd~dd" },
                    { "ckkkc", "keeek", "ke-ek", "keeek", "ccocc" }, { "ckkkc", "k---k", "k---k", "k---k", "ccccc" },
                    { "ckkkc", "k---k", "k---k", "k---k", "ccccc" }, { "ckkkc", "keeek", "ke-ek", "keeek", "ccccc" },
                    { "ccccc", "ckkkc", "ckbkc", "ckkkc", "ccccc" } })
            .addElement('c', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
            .addElement('k', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
            .addElement('e', ofBlock(LanthItemList.ELECTRODE_CASING, 0))
            .addElement(
                'b',
                buildHatchAdder(SourceChamber.class).hatchClass(TileHatchOutputBeamline.class)
                    .casingIndex(CASING_INDEX)
                    .dot(4)
                    .adder(SourceChamber::addBeamLineOutputHatch)
                    .build())
            .addElement(
                'i',
                buildHatchAdder(SourceChamber.class).atLeast(InputBus)
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .build())
            .addElement(
                'o',
                buildHatchAdder(SourceChamber.class).atLeast(OutputBus)
                    .casingIndex(CASING_INDEX)
                    .dot(2)
                    .build())
            .addElement(
                'd',
                buildHatchAdder(SourceChamber.class).atLeast(Maintenance, Energy)
                    .casingIndex(CASING_INDEX)
                    .dot(3)
                    .buildAndChain(ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0)))

            .build();
    }

    public SourceChamber(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public SourceChamber(String name) {
        super(name);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece("sc", stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity te) {
        return new SourceChamber(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Particle Source")
            .addInfo("Controller block for the Source Chamber")
            .addInfo(BLUEPRINT_INFO)
            .addInfo(DescTextLocalization.BEAMLINE_SCANNER_INFO)
            .addSeparator()
            .beginStructureBlock(5, 5, 6, true)
            .addController("Front bottom")
            .addCasingInfoExactly(LanthItemList.SHIELDED_ACCELERATOR_CASING.getLocalizedName(), 56, false)
            .addCasingInfoExactly(LanthItemList.SHIELDED_ACCELERATOR_GLASS.getLocalizedName(), 52, false)
            .addCasingInfoExactly(LanthItemList.ELECTRODE_CASING.getLocalizedName(), 16, false)
            .addOtherStructurePart("Beamline Output Hatch", addDotText(4))
            .addEnergyHatch(addDotText(3))
            .addMaintenanceHatch(addDotText(3))
            .addInputBus(addDotText(1))
            .addOutputBus(addDotText(2))
            .toolTipFinisher("GTNH: Lanthanides");
        return tt;
    }

    private boolean addBeamLineOutputHatch(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();

        if (mte == null) return false;

        if (mte instanceof TileHatchOutputBeamline) {
            return this.mOutputBeamline.add((TileHatchOutputBeamline) mte);
        }

        return false;
    }
    /*
     * protected OverclockDescriber createOverclockDescriber() { return new EUNoTotalOverclockDescriber((byte) 4, 1); }
     */

    @Override
    public boolean checkRecipe(ItemStack itemStack) {

        // GT_Log.out.print("In checkRecipe");

        // No input particle, so no input quantities

        outputFocus = 0;
        outputEnergy = 0;
        outputParticle = 0;
        outputRate = 0;

        ItemStack[] tItems = this.getStoredInputs()
            .toArray(new ItemStack[0]);
        // GT_Log.out.print(Arrays.toString(tItems));
        long tVoltageMaxTier = this.getMaxInputVoltage(); // Used to keep old math the same
        long tVoltageActual = GT_Values.VP[(int) this.getInputVoltageTier()];

        RecipeSC tRecipe = (RecipeSC) BeamlineRecipeAdder2.instance.SourceChamberRecipes
            .findRecipe(this.getBaseMetaTileEntity(), false, tVoltageActual, new FluidStack[] {}, tItems);

        if (tRecipe == null || !tRecipe.isRecipeInputEqual(true, new FluidStack[] {}, tItems)) return false; // Consumes
                                                                                                             // input
                                                                                                             // item

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        this.mMaxProgresstime = tRecipe.mDuration;
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1) return false;

        mEUt = (int) -tVoltageActual;
        if (this.mEUt > 0) this.mEUt = (-this.mEUt);

        outputParticle = tRecipe.particleId;
        float maxParticleEnergy = Particle.getParticleFromId(outputParticle)
            .maxSourceEnergy(); // The maximum energy a
                                // particle can possess
                                // when produced by this
                                // multiblock
        float maxMaterialEnergy = tRecipe.maxEnergy; // The maximum energy for the recipe processed
        outputEnergy = (float) Math.min(
            (-maxMaterialEnergy) * Math.pow(1.001, -(tRecipe.energyRatio) * (tVoltageMaxTier - tRecipe.mEUt))
                + maxMaterialEnergy,
            maxParticleEnergy);

        if (outputEnergy <= 0) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.scerror"));
            return false;
        }

        outputFocus = tRecipe.focus;
        outputRate = tRecipe.rate;

        this.mOutputItems = tRecipe.mOutputs;
        this.updateSlots();

        outputAfterRecipe();

        return true;
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("SourceChamber.hint", 7); // Generate 7 localised hint strings in structure
                                                                      // description
    }

    private void outputAfterRecipe() {

        if (!mOutputBeamline.isEmpty()) {

            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(outputEnergy, outputRate, outputParticle, outputFocus));

            for (TileHatchOutputBeamline o : mOutputBeamline) {

                o.q = packet;
            }
        }
    }

    @Override
    public void stopMachine() {
        outputFocus = 0;
        outputEnergy = 0;
        outputParticle = 0;
        outputRate = 0;
        super.stopMachine();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return BeamlineRecipeAdder2.instance.SourceChamberRecipes;
    }

    @Override
    public String[] getInfoData() {

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (tHatch.isValid()) {
                storedEnergy += tHatch.getBaseMetaTileEntity()
                    .getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity()
                    .getEUCapacity();
            }
        }

        return new String[] {
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GT_Utility.getTier(getMaxInputVoltage())]
                + EnumChatFormatting.RESET,
            /* 5 */ StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + Float.toString(mEfficiency / 100.0F)
                + EnumChatFormatting.RESET
                + " %",
            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.out_pre")
                + ": "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.particle") + ": "
                + EnumChatFormatting.GOLD
                + Particle.getParticleFromId(this.outputParticle)
                    .getLocalisedName()
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.energy") + ": "
                + EnumChatFormatting.DARK_RED
                + this.outputEnergy
                + EnumChatFormatting.RESET
                + " keV",
            StatCollector.translateToLocal(
                "beamline.focus") + ": " + EnumChatFormatting.BLUE + this.outputFocus + " " + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.amount") + ": "
                + EnumChatFormatting.LIGHT_PURPLE
                + this.outputRate, };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("sc", stackSize, hintsOnly, 2, 4, 0);
    }

    @Override
    public IStructureDefinition<SourceChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        this.mOutputBeamline.clear(); // Necessary due to the nature of the beamline hatch adder

        return checkPiece("sc", 2, 4, 0) && this.mMaintenanceHatches.size() == 1
            && this.mInputBusses.size() == 1
            && this.mOutputBusses.size() == 1
            && this.mOutputBeamline.size() == 1
            && this.mEnergyHatches.size() == 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {

        // Placeholder
        if (side == facing) {
            if (active) return new ITexture[] { casingTexturePages[1][14], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][14], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][14] };
    }
}
