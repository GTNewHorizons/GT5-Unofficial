package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.BeamlineOutput;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.SOURCE_CHAMBER_METADATA;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gtnhlanth.api.recipe.LanthanidesRecipeMaps;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.tileentity.recipe.beamline.SourceChamberMetadata;
import gtnhlanth.util.DescTextLocalization;

public class MTESourceChamber extends MTEEnhancedMultiBlockBase<MTESourceChamber> implements ISurvivalConstructable {

    private static final IStructureDefinition<MTESourceChamber> STRUCTURE_DEFINITION;

    private static final int ShieldedAccCasingTextureID = Casings.ShieldedAcceleratorCasing.getTextureId();

    private float outputEnergy;
    private int outputRate;
    private int outputParticle;
    private float outputFocus;

    static {
        STRUCTURE_DEFINITION = StructureDefinition.<MTESourceChamber>builder()
            .addShape(
                "sc",
                new String[][] { { "ccccc", "ckkkc", "ckikc", "ckkkc", "dd~dd" },
                    { "ckkkc", "keeek", "ke-ek", "keeek", "ccocc" }, { "ckkkc", "k---k", "k---k", "k---k", "ccccc" },
                    { "ckkkc", "k---k", "k---k", "k---k", "ccccc" }, { "ckkkc", "keeek", "ke-ek", "keeek", "ccccc" },
                    { "ccccc", "ckkkc", "ckbkc", "ckkkc", "ccccc" } })
            .addElement('c', Casings.ShieldedAcceleratorCasing.asElement())
            .addElement('k', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
            .addElement('e', ofBlock(LanthItemList.ELECTRODE_CASING, 0))
            .addElement(
                'b',
                buildHatchAdder(MTESourceChamber.class).atLeast(BeamlineOutput)
                    .casingIndex(ShieldedAccCasingTextureID)
                    .hint(4)
                    .build())
            .addElement(
                'i',
                buildHatchAdder(MTESourceChamber.class).atLeast(InputBus, InputHatch)
                    .casingIndex(ShieldedAccCasingTextureID)
                    .hint(1)
                    .build())
            .addElement(
                'o',
                buildHatchAdder(MTESourceChamber.class).atLeast(OutputBus, OutputHatch)
                    .casingIndex(ShieldedAccCasingTextureID)
                    .hint(2)
                    .build())
            .addElement(
                'd',
                buildHatchAdder(MTESourceChamber.class).atLeast(Maintenance, Energy)
                    .casingIndex(ShieldedAccCasingTextureID)
                    .hint(3)
                    .buildAndChain(Casings.ShieldedAcceleratorCasing.asElement()))

            .build();
    }

    public MTESourceChamber(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTESourceChamber(String name) {
        super(name);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return survivalBuildPiece("sc", stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity te) {
        return new MTESourceChamber(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gtnhlanth.tt.sc.machinetype"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info1"))
            .addInfo(DescTextLocalization.BEAMLINE_SCANNER_INFO)
            .addSeparator()
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info2"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info3"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info4"))
            .addSeparator()
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info5"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info6"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info7"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info8"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info9"))
            .addSeparator()
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info10"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.sc.info11"))
            .beginStructureBlock(6, 5, 5, true)
            .addController("Front bottom center")
            .addCasing("56", Casings.ShieldedAcceleratorCasing.getLocalizedName(), false)
            .addCasing("52", LanthItemList.SHIELDED_ACCELERATOR_GLASS.getLocalizedName(), false)
            .addCasing("16", LanthItemList.ELECTRODE_CASING.getLocalizedName(), false)
            .addMiscHatch("1", StatCollector.translateToLocal("gtnhlanth.tt.hatch.beamoutput"), "Back center casing", 4)
            .addEnergyHatch("1", "Any front bottom casing", 3)
            .addMaintenanceHatch("1", "Any front bottom casing", 3)
            .addInputAny("1", "Front center casing", 1)
            .addOutputAny("1", "Behind controller", 2)
            .addAir("Interior of the structure")
            .toolTipFinisher();
        return tt;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        ItemStack[] tItems = this.getStoredInputs()
            .toArray(new ItemStack[0]);
        FluidStack[] tFluids = this.getStoredFluids()
            .toArray(new FluidStack[0]);

        long tVoltageMaxTier = this.getMaxInputVoltage(); // Used to keep old math the same
        long tVoltageActual = GTValues.VP[(int) this.getInputVoltageTier()];

        GTRecipe tRecipe = LanthanidesRecipeMaps.sourceChamberRecipes.findRecipeQuery()
            .caching(false)
            .items(tItems)
            .fluids(tFluids)
            .voltage(tVoltageActual)
            .find();
        if (tRecipe == null) return CheckRecipeResultRegistry.NO_RECIPE;

        SourceChamberMetadata metadata = tRecipe.getMetadata(SOURCE_CHAMBER_METADATA);
        if (metadata == null) return CheckRecipeResultRegistry.NO_RECIPE;

        if (!tRecipe.isRecipeInputEqual(true, tFluids, tItems)) {
            return CheckRecipeResultRegistry.NO_RECIPE; // Consumes input item
        }

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        this.mMaxProgresstime = tRecipe.mDuration;
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        this.mEUt = (int) -tVoltageActual;
        if (this.mEUt > 0) this.mEUt = (-this.mEUt);

        outputParticle = metadata.particleID;
        float maxParticleEnergy = Particle.getParticleFromId(outputParticle)
            .maxSourceEnergy();
        float maxMaterialEnergy = metadata.maxEnergy; // The maximum energy for the recipe processed

        this.outputEnergy = (float) Math.min(
            maxMaterialEnergy * (1 - Math.pow(1.001, -(metadata.energyRatio) * (tVoltageMaxTier - tRecipe.mEUt))),
            maxParticleEnergy);
        if (outputEnergy <= 0) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.scerror"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        this.outputFocus = metadata.focus;
        this.outputRate = metadata.rate;
        this.mOutputItems = tRecipe.mOutputs;
        this.updateSlots();

        outputPacketAfterRecipe();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void outputPacketAfterRecipe() {
        if (!mBeamlineOutputHatches.isEmpty()) {
            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(outputEnergy, outputRate, outputParticle, outputFocus));

            for (MTEHatchOutputBeamline o : mBeamlineOutputHatches) {
                o.dataPacket = packet;
            }
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        this.outputFocus = 0;
        this.outputEnergy = 0;
        this.outputParticle = 0;
        this.outputRate = 0;
        super.stopMachine(reason);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return LanthanidesRecipeMaps.sourceChamberRecipes;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchEnergy tHatch : mEnergyHatches) {
            if (tHatch.isValid()) {
                storedEnergy += tHatch.getBaseMetaTileEntity()
                    .getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity()
                    .getEUCapacity();
            }
        }

        return new String[] {
            // from super()
            /* 1 */ IGregTechDeviceInformation.encode(
                "GT5U.multiblock.Progress.fmt.s",
                formatNumber(mProgresstime / 20),
                formatNumber(mMaxProgresstime / 20)),
            /* 2 */ IGregTechDeviceInformation
                .encode("GT5U.multiblock.energy.fmt", formatNumber(storedEnergy), formatNumber(maxEnergy)),
            /* 3 */ IGregTechDeviceInformation
                .encode("GT5U.multiblock.usage.fmt", formatNumber(getActualEnergyUsage())),
            /* 4 */ IGregTechDeviceInformation.encode(
                "GT5U.multiblock.mei.fmt.2A",
                formatNumber(getMaxInputVoltage()),
                VN[GTUtility.getTier(getMaxInputVoltage())]),
            /* 5 */ IGregTechDeviceInformation.encode(
                "GT5U.multiblock.problems.efficiency.fmt",
                getIdealStatus() - getRepairStatus(),
                mEfficiency / 100.0F + " %"),
            /* 6 Pollution not included */
            /* 7 */ IGregTechDeviceInformation.encode("GT5U.multiblock.recipesDone.fmt", formatNumber(recipesDone)),
            // Beamline-specific
            "beamline.out_pre.hdr", Particle.getParticleFromId(this.outputParticle)
                .encodeInfoData(),
            IGregTechDeviceInformation.encode("beamline.energy.eV.fmt", this.outputEnergy * 1000),
            IGregTechDeviceInformation.encode("beamline.focus.fmt", this.outputFocus),
            IGregTechDeviceInformation.encode("beamline.amount.fmt", this.outputRate) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("sc", stackSize, hintsOnly, 2, 4, 0);
    }

    @Override
    public IStructureDefinition<MTESourceChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece("sc", 2, 4, 0, errors)) return;
        checkOneEnergyHatch(errors);
        checkOneMaintenanceHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {

        // Placeholder
        if (side == facing) {
            if (active) return new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture() };
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    private String particleText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.GOLD, text, EnumChatFormatting.GRAY);
    }

    private String energyText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.GREEN, text, EnumChatFormatting.GRAY);
    }

    private String particleLine(String particle, String energy) {
        return String.format("%s : %s", particleText(particle), energyText(energy + "keV"));
    }

    private String sourceText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.LIGHT_PURPLE, text, EnumChatFormatting.GRAY);
    }

    private String ratioText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.BLUE, text, EnumChatFormatting.GRAY);
    }

    private String sourceLine(String source, String ratio) {
        return String.format("%s : %s", sourceText(source), ratioText(ratio));
    }
}
