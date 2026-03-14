package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.SOURCE_CHAMBER_METADATA;
import static gtnhlanth.util.DescTextLocalization.addHintNumber;

import java.util.ArrayList;

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

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.tileentities.machines.multi.beamcrafting.MTEHatchAdvancedOutputBeamline;
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

    private final ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();

    private static final int CASING_INDEX = 1662;

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
            .addElement('c', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
            .addElement('k', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
            .addElement('e', ofBlock(LanthItemList.ELECTRODE_CASING, 0))
            .addElement(
                'b',
                buildHatchAdder(MTESourceChamber.class).hatchClass(MTEHatchOutputBeamline.class)
                    .casingIndex(CASING_INDEX)
                    .hint(4)
                    .adder(MTESourceChamber::addBeamLineOutputHatch)
                    .build())
            .addElement(
                'i',
                buildHatchAdder(MTESourceChamber.class).atLeast(InputBus, InputHatch)
                    .casingIndex(CASING_INDEX)
                    .hint(1)
                    .build())
            .addElement(
                'o',
                buildHatchAdder(MTESourceChamber.class).atLeast(OutputBus)
                    .casingIndex(CASING_INDEX)
                    .hint(2)
                    .build())
            .addElement(
                'd',
                buildHatchAdder(MTESourceChamber.class).atLeast(Maintenance, Energy)
                    .casingIndex(CASING_INDEX)
                    .hint(3)
                    .buildAndChain(ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0)))

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
        // spotless:off
        tt.addMachineType("Particle Source")
            .addInfo("Creates beams of Particles")
            .addInfo(DescTextLocalization.BEAMLINE_SCANNER_INFO)
            .addSeparator()
            .addInfo(EnumChatFormatting.RED + "Takes 1 input bus OR 1 input hatch")
            .addInfo("NEI shows the minimum " + EnumChatFormatting.AQUA + "EU/t requirement" + EnumChatFormatting.GRAY + ", Beam Focus, Beam Rate, and " + EnumChatFormatting.YELLOW + "Maximum Beam Energy")
            .addInfo("All recipes last for one second")
            .addInfo("This upper limit is approached asymptotically as operating power increases")
            .addSeparator()
            .addInfo("Each " + particleText("Particle Type") + " has a " + energyText("Maximum Particle Energy"))
            .addInfo(particleLine("Alpha", "8000"))
            .addInfo(particleLine("Proton", "15000"))
            .addInfo(particleLine("Neutron", "15000"))
            .addInfo(particleLine("Electron", "5000"))
            .addSeparator()
            .addInfo("Each " + sourceText("Source input") + " has an Associated " + ratioText("Energy Ratio"))
            .addInfo(sourceLine("U-238 Dust", "999"))
            .addInfo(sourceLine("Californium Dust", "999"))
            .addInfo(sourceLine("Tungsten Rods", "0.1"))
            .addInfo(sourceLine("Long LaB6 Rods", "0.3"))
            .addSeparator()
            .addInfo(EnumChatFormatting.WHITE + "Actual Beam Energy = min(" + energyText("Max Particle Energy") + ", " + EnumChatFormatting.GOLD + "aeV" + EnumChatFormatting.GRAY + ")")
            .addInfo("Where " + EnumChatFormatting.GOLD + "aeV" + EnumChatFormatting.WHITE + " =  "+EnumChatFormatting.YELLOW+"Max Beam Energy"+EnumChatFormatting.WHITE+" * (1 - 1.001^(" + ratioText("-Energy Ratio") + " * (" + EnumChatFormatting.RED + "EU/t provided"+EnumChatFormatting.WHITE+" - "+EnumChatFormatting.AQUA+"EU/t required"+EnumChatFormatting.WHITE+")))")
            .addInfo("In short, the " + particleText("particle beam's") + " energy is capped to the " + energyText("Max Particle Energy"))
            .addInfo("Any one recipe can only provide up to its own " + EnumChatFormatting.YELLOW + "Maximum Beam Energy")
            .beginStructureBlock(5, 5, 6, true)
            .addController("Front bottom")
            .addCasingInfoExactly(LanthItemList.SHIELDED_ACCELERATOR_CASING.getLocalizedName(), 56, false)
            .addCasingInfoExactly(LanthItemList.SHIELDED_ACCELERATOR_GLASS.getLocalizedName(), 52, false)
            .addCasingInfoExactly(LanthItemList.ELECTRODE_CASING.getLocalizedName(), 16, false)
            .addOtherStructurePart("Beamline Output Hatch", addHintNumber(4))
            .addEnergyHatch(addHintNumber(3))
            .addMaintenanceHatch(addHintNumber(3))
            .addInputBus(addHintNumber(1))
            .addInputHatch(addHintNumber(1))
            .addOutputBus(addHintNumber(2))
            .toolTipFinisher();
        return tt;
        //spotless:on
    }

    private boolean addBeamLineOutputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchAdvancedOutputBeamline) {
            return false;
        }

        if (mte instanceof MTEHatchOutputBeamline) {
            return this.mOutputBeamline.add((MTEHatchOutputBeamline) mte);
        }

        return false;
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

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("SourceChamber.hint", 7);
    }

    private void outputPacketAfterRecipe() {
        if (!mOutputBeamline.isEmpty()) {
            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(outputEnergy, outputRate, outputParticle, outputFocus));

            for (MTEHatchOutputBeamline o : mOutputBeamline) {
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
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + formatNumber(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + formatNumber(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GTUtility.getTier(getMaxInputVoltage())]
                + EnumChatFormatting.RESET,
            /* 5 */ StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            /* 6 Pollution not included */
            /* 7 */ StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET,
            // Beamline-specific
            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.out_pre")
                + ": "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.particle") + ": " // "Multiblock Beamline Output:"
                + EnumChatFormatting.GOLD
                + Particle.getParticleFromId(this.outputParticle)
                    .getLocalisedName()
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.energy") + ": " // "Energy:"
                + EnumChatFormatting.DARK_RED
                + this.outputEnergy * 1000
                + EnumChatFormatting.RESET
                + " eV",
            StatCollector.translateToLocal("beamline.focus") + ": " // "Focus:"
                + EnumChatFormatting.BLUE
                + this.outputFocus
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.amount") + ": " // "Amount:"
                + EnumChatFormatting.LIGHT_PURPLE
                + this.outputRate };
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mOutputBeamline.clear(); // Necessary due to the nature of the beamline hatch adder

        return checkPiece("sc", 2, 4, 0) && this.mMaintenanceHatches.size() == 1
            && (this.mInputBusses.size() == 1 || this.mInputHatches.size() == 1)
            && this.mOutputBusses.size() == 1
            && this.mOutputBeamline.size() == 1
            && this.mEnergyHatches.size() == 1;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {

        // Placeholder
        if (side == facing) {
            if (active) return new ITexture[] { casingTexturePages[12][126], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[12][126], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[12][126] };
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
