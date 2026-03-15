package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gtnhlanth.util.DescTextLocalization.addHintNumber;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.StructureLib;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TickTime;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.tileentity.recipe.beamline.BeamlineRecipeLoader;
import gtnhlanth.util.DescTextLocalization;
import gtnhlanth.util.Util;

public class MTELINAC extends MTEEnhancedMultiBlockBase<MTELINAC> implements ISurvivalConstructable {

    private static final IStructureDefinition<MTELINAC> STRUCTURE_DEFINITION;

    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String STRUCTURE_PIECE_LAYER = "layer";
    protected static final String STRUCTURE_PIECE_END = "end"; // Coolant temperature

    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    private final ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();

    private static final int CASING_INDEX = 1662;

    private int glassTier = -1;
    private int machineTemp = 0;
    private int length;

    private float outputEnergy;
    private int outputParticleID;
    private int outputRate;
    private float outputFocus;

    /*
     * g: Grate Machine Casing b: Borosilicate glass c: Shielded accelerator casing v: Vacuum k: Shielded glass d:
     * Coolant Delivery casing y: Superconducting coil
     */

    static {
        STRUCTURE_DEFINITION = StructureDefinition.<MTELINAC>builder()
            .addShape(
                STRUCTURE_PIECE_BASE,
                new String[][] { { "ggggggg", "gbbbbbg", "gbbbbbg", "gbbibbg", "gbbbbbg", "gbbbbbg", "ggg~ggg" },
                    { "ggggggg", "gcccccg", "gcccccg", "gcc-ccg", "gcccccg", "gcccccg", "ggggggg" },
                    { "ccccccc", "cvvvvvc", "kvvvvvk", "kvv-vvk", "kvvvvvk", "cvvvvvc", "jcccccj" },
                    { "cckkkcc", "cdddddc", "kdyyydk", "kdy-ydk", "kdyyydk", "cdddddc", "jcccccj" },
                    { "cckkkcc", "cdvvvdc", "kvvvvvk", "kdv-vdk", "kvvvvvk", "cdvvvdc", "jcccccj" },
                    { "cckkkcc", "cdddddc", "kdyyydk", "kdy-ydk", "kdyyydk", "cdddddc", "jcccccj" },
                    { "cckkkcc", "cdvvvdc", "kvvvvvk", "kdv-vdk", "kvvvvvk", "cdvvvdc", "jcccccj" },
                    { "cckhkcc", "cdddddc", "kdyyydk", "kdy-ydk", "kdyyydk", "cdddddc", "jcccccj" }, })
            .addShape(
                STRUCTURE_PIECE_LAYER,
                new String[][] { { "cckkkcc", "cdvvvdc", "kvvvvvk", "kdv-vdk", "kvvvvvk", "cdvvvdc", "ccccccc" },
                    { "cckkkcc", "cdddddc", "kdyyydk", "kdy-ydk", "kdyyydk", "cdddddc", "ccccccc" } })
            .addShape(
                STRUCTURE_PIECE_END,
                new String[][] { { "cckkkcc", "cdvvvdc", "kvvvvvk", "kdv-vdk", "kvvvvvk", "cdvvvdc", "ccccccc" },
                    { "cckhkcc", "cdddddc", "kdyyydk", "kdy-ydk", "kdyyydk", "cdddddc", "ccccccc" },
                    { "cckkkcc", "cdvvvdc", "kvvvvvk", "kdv-vdk", "kvvvvvk", "cdvvvdc", "ccccccc" },
                    { "cckkkcc", "cdddddc", "kdyyydk", "kdy-ydk", "kdyyydk", "cdddddc", "ccccccc" },
                    { "cckkkcc", "cdvvvdc", "kvvvvvk", "kdv-vdk", "kvvvvvk", "cdvvvdc", "ccccccc" },
                    { "cckkkcc", "cdddddc", "kdyyydk", "kdy-ydk", "kdyyydk", "cdddddc", "ccccccc" },
                    { "ccccccc", "cvvvvvc", "kvvvvvk", "kvv-vvk", "kvvvvvk", "cvvvvvc", "ccccccc" },
                    { "ccccccc", "ccccccc", "ccccccc", "ccc-ccc", "ccccccc", "ccccccc", "ccccccc" },
                    { "ccccccc", "cbbbbbc", "cbbbbbc", "cbbobbc", "cbbbbbc", "cbbbbbc", "ccccccc" } })
            .addElement('c', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
            .addElement('g', ofBlock(GregTechAPI.sBlockCasings3, 10)) // Grate Machine Casing
            .addElement('b', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
            .addElement(
                'i',
                buildHatchAdder(MTELINAC.class).hatchClass(MTEHatchInputBeamline.class)
                    .casingIndex(CASING_INDEX)
                    .hint(3)
                    .adder(MTELINAC::addBeamLineInputHatch)
                    .build())
            .addElement(
                'o',
                buildHatchAdder(MTELINAC.class).hatchClass(MTEHatchOutputBeamline.class)
                    .casingIndex(CASING_INDEX)
                    .hint(4)
                    .adder(MTELINAC::addBeamLineOutputHatch)
                    .build())
            .addElement('v', ofBlock(LanthItemList.ELECTRODE_CASING, 0))
            .addElement('k', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
            .addElement('d', ofBlock(LanthItemList.COOLANT_DELIVERY_CASING, 0))
            .addElement('y', ofBlock(GregTechAPI.sBlockCasings1, 15)) // Superconducting coil
            .addElement(
                'h',
                buildHatchAdder(MTELINAC.class).atLeast(InputHatch, OutputHatch)
                    .casingIndex(CASING_INDEX)
                    .hint(2)
                    .build())
            .addElement(
                'j',
                buildHatchAdder(MTELINAC.class).atLeast(Maintenance, Energy)
                    .casingIndex(CASING_INDEX)
                    .hint(1)
                    .buildAndChain(ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0)))
            .build();
    }

    public MTELINAC(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTELINAC(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity te) {
        return new MTELINAC(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType("Particle Accelerator, LINAC")
            .addInfo("Accelerates charged particles to higher energies by running them through an electric field")
            .addInfo("Electrically neutral particles are therefore unaffected")
            .addInfo(DescTextLocalization.BEAMLINE_SCANNER_INFO)
            .addSeparator()
            .addInfo("Increasing Structure Length increases output "+createEnergyText("Beam Energy")+" but decreases "+createFocusText("Beam Focus"))
            .addInfo("Requires " + EnumChatFormatting.WHITE + "Length * kL/s " + EnumChatFormatting.GRAY + "of " + EnumChatFormatting.AQUA + "coolant" + EnumChatFormatting.GRAY + " for operation")
            .addInfo(createFocusText("Beam Focus") + " loss can be mitigated more effectively with lower temperature " + EnumChatFormatting.AQUA+"coolant")
            .addInfo("Valid coolants:")
            .addInfo(coolantLine("Liquid Nitrogen",90))
            .addInfo(coolantLine("Liquid Oxygen",90))
            .addInfo(coolantLine("Coolant",60))
            .addInfo(coolantLine("Super Coolant",1))
            .addSeparator()
            .addInfo(createEnergyText("Output Beam Energy") + EnumChatFormatting.WHITE + " = max("+EnumChatFormatting.YELLOW+"V"+EnumChatFormatting.WHITE+", 50) * 10^"+EnumChatFormatting.RED+"IE")
            .addInfo("where " + EnumChatFormatting.YELLOW + "V" + EnumChatFormatting.WHITE + " = (Length - 1) * cbrt(" + EnumChatFormatting.DARK_GREEN + "Machine Voltage" + EnumChatFormatting.WHITE + ") / 4")
            .addInfo("and " + EnumChatFormatting.RED + "IE " + EnumChatFormatting.WHITE + "= 1 + min(" + EnumChatFormatting.LIGHT_PURPLE + "Input Beam Energy" + EnumChatFormatting.WHITE + ", 7500) / " + EnumChatFormatting.GREEN + "Maximum Particle Energy")
            .addInfo(EnumChatFormatting.RED + "Input Beam Energies" + EnumChatFormatting.GRAY + " higher than 7500keV are treated as if they are 7500keV")
            .addInfo(EnumChatFormatting.GREEN +"Maximum Particle Energy"+EnumChatFormatting.GRAY + " refers to values in the Source Chamber Tooltip")
            .addSeparator()
            .addInfo(createFocusText("Output Beam Focus") + EnumChatFormatting.WHITE + "depends on the relation between " + EnumChatFormatting.YELLOW + "Input Beam Focus" + EnumChatFormatting.WHITE + " and " + EnumChatFormatting.DARK_AQUA + "Machine Focus" + EnumChatFormatting.WHITE)
            .addInfo("where " + EnumChatFormatting.DARK_AQUA + "Machine Focus" + EnumChatFormatting.WHITE + " = min(max((-0.9 * (Length-1) * 1.1^(0.2 * " + EnumChatFormatting.GOLD + "Coolant Temperature" + EnumChatFormatting.WHITE + ") + 110), 5), 90)")
            .addInfo("If " + EnumChatFormatting.YELLOW + "Input Beam Focus" + EnumChatFormatting.WHITE + " > " + EnumChatFormatting.DARK_AQUA + "Machine Focus" + EnumChatFormatting.WHITE + ", " + createFocusText("Output Beam Focus") + EnumChatFormatting.WHITE + " = (" + EnumChatFormatting.YELLOW + "Input Beam Focus" + EnumChatFormatting.WHITE + " + " + EnumChatFormatting.DARK_AQUA + "Machine Focus" + EnumChatFormatting.WHITE + ")/2")
            .addInfo("If " + EnumChatFormatting.YELLOW + "Input Beam Focus" + EnumChatFormatting.WHITE + " <= " + EnumChatFormatting.DARK_AQUA + "Machine Focus" + EnumChatFormatting.WHITE + ", " + createFocusText("Output Beam Focus") + EnumChatFormatting.WHITE + " = " + EnumChatFormatting.YELLOW + "Input Beam Focus" + EnumChatFormatting.WHITE + " * " + EnumChatFormatting.DARK_AQUA + "Machine Focus" + EnumChatFormatting.WHITE + "/100")
            .beginVariableStructureBlock(7, 7, 7, 7, 19, 83, false)
            .addController("Front bottom")
            .addCasingInfoRange(LanthItemList.SHIELDED_ACCELERATOR_CASING.getLocalizedName(), 325, 1285, false)
            .addCasingInfoRange(LanthItemList.COOLANT_DELIVERY_CASING.getLocalizedName(), 148, 852, false)
            .addCasingInfoRange(LanthItemList.SHIELDED_ACCELERATOR_GLASS.getLocalizedName(), 127, 703, false)
            .addCasingInfoRange("Superconducting Coil Block", 56, 312, false)
            .addCasingInfoRange(LanthItemList.ELECTRODE_CASING.getLocalizedName(), 156, 732, false)
            .addCasingInfoExactly("Grate Machine Casing", 47, false)
            .addCasingInfoExactly("Any Tiered Glass (LuV+)", 48, false)
            .addEnergyHatch(addHintNumber(1))
            .addMaintenanceHatch(addHintNumber(1))
            .addInputHatch(addHintNumber(2))
            .addOutputHatch(addHintNumber(2))
            .addOtherStructurePart("Beamline Input Hatch", addHintNumber(3))
            .addOtherStructurePart("Beamline Output Hatch", addHintNumber(4))
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        //spotless:on
        return tt;
    }

    private boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }

    private boolean addBeamLineOutputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchOutputBeamline) {
            return this.mOutputBeamline.add((MTEHatchOutputBeamline) mte);
        }

        return false;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        ArrayList<FluidStack> tFluidInputs = this.getStoredFluids();
        if (tFluidInputs.isEmpty()) {
            this.doRandomMaintenanceDamage(); // Penalise letting coolant run dry
            this.stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.nocoolant"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        FluidStack fluidCoolant = tFluidInputs.get(0);

        BeamInformation inputInfo = this.getInputInformation();
        if (inputInfo == null) return CheckRecipeResultRegistry.NO_RECIPE;

        float inputEnergy = inputInfo.getEnergy();
        int inputParticleID = inputInfo.getParticleId();
        Particle inputParticle = Particle.getParticleFromId(inputParticleID);
        float inputFocus = inputInfo.getFocus();
        int inputRate = inputInfo.getRate();
        if (inputEnergy == 0) return CheckRecipeResultRegistry.NO_RECIPE;

        if (!inputParticle.canAccelerate()) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.noaccel"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        this.machineTemp = Util.coolantFluidTemperature(fluidCoolant); // Used for tricorder too
        float tempFactor = calculateTemperatureFactor(this.machineTemp);

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = TickTime.SECOND;
        this.mEUt = (int) ((this.mEnergyHatches.size() == 1) ? -GTValues.VP[(int) this.getInputVoltageTier()]
            : (int) (-this.getMaxInputAmps() * GTValues.VP[(int) this.getInputVoltageTier()]));

        // 1A of full power if one energy hatch, 4A if two
        long voltage = (this.mEnergyHatches.size() == 1) ? this.getMaxInputVoltage() : this.getMaxInputPower();
        float machineEnergy = (float) Math.max((this.length - 1) / 4.0 * Math.pow(voltage, 1.0 / 3.0), 50);
        inputEnergy = Math.min(inputEnergy, 7500); // Does not scale past 7500 keV, prevents double LINAC issue
        this.outputEnergy = (float) Math.pow(10, 1 + inputEnergy / inputParticle.maxSourceEnergy()) * machineEnergy;

        this.outputParticleID = inputParticleID;

        float machineFocus = ((-0.9f) * (this.length - 1) * tempFactor) + 110;
        machineFocus = Math.min(Math.max(machineFocus, 5), 90);
        this.outputFocus = (inputFocus > machineFocus) ? ((inputFocus + machineFocus) / 2)
            : inputFocus * (machineFocus / 100);

        this.outputRate = inputRate;

        final int fluidConsumed = 1000 * (this.length);
        if (Util.coolantFluidCheck(fluidCoolant, fluidConsumed)) {
            this.stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.inscoolant"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        Fluid fluidOutput = BeamlineRecipeLoader.coolantMap.get(
            fluidCoolant.getFluid()
                .getName());
        if (Objects.isNull(fluidOutput)) return CheckRecipeResultRegistry.NO_RECIPE;

        fluidCoolant.amount -= fluidConsumed;
        FluidStack fluidOutputStack = new FluidStack(fluidOutput, fluidConsumed);
        this.addFluidOutputs(new FluidStack[] { fluidOutputStack });

        outputPacketAfterRecipe();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void outputPacketAfterRecipe() {
        if (!mOutputBeamline.isEmpty()) {
            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(outputEnergy, outputRate, outputParticleID, outputFocus));

            for (MTEHatchOutputBeamline o : mOutputBeamline) {
                o.dataPacket = packet;
            }
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        outputFocus = 0;
        outputEnergy = 0;
        outputParticleID = 0;
        outputRate = 0;
        machineTemp = 0;
        super.stopMachine(reason);
    }

    @Nullable
    private BeamInformation getInputInformation() {
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
            return in.dataPacket.getContent();
        }
        return null;
    }

    private static float calculateTemperatureFactor(int fluidTemp) {
        return (float) Math.pow(1.1, 0.2 * fluidTemp);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity mte, ItemStack stack) {
        mInputBeamline.clear();
        mOutputBeamline.clear();

        this.outputEnergy = 0;
        this.outputRate = 0;
        this.outputParticleID = 0;
        this.outputFocus = 0;

        this.glassTier = -1;

        length = 8; // Base piece length

        if (!checkPiece(STRUCTURE_PIECE_BASE, 3, 6, 0)) return false;

        while (length < 128) {
            if (!checkPiece(STRUCTURE_PIECE_LAYER, 3, 6, -length)) {
                if (!checkPiece(STRUCTURE_PIECE_END, 3, 6, -length)) {
                    return false;
                }
                break;
            }
            length += 2;
        }

        length += 9;

        return this.mInputBeamline.size() == 1 && this.mOutputBeamline.size() == 1
            && this.mEnergyHatches.size() <= 2
            && this.glassTier >= VoltageIndex.LuV;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 3, 6, 0);

        int lLength = Math.max(stackSize.stackSize + 7, 8); // !!
        if (!(lLength % 2 == 0)) {
            lLength++; // Otherwise you get gaps at the end
        }

        for (int i = -8; i > -lLength - 1; i -= 2) {
            buildPiece(STRUCTURE_PIECE_LAYER, stackSize, hintsOnly, 3, 6, i);
        }

        buildPiece(STRUCTURE_PIECE_END, stackSize, hintsOnly, 3, 6, -(lLength + 2));

        StructureLib.addClientSideChatMessages("Length: " + (11 + lLength) + " blocks.");
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        elementBudget = 200;

        if (this.mMachine) return -1;

        int build = survivalBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 3, 6, 0, elementBudget, env, false, true);
        if (build >= 0) return build; // Incomplete

        int lLength = Math.max(stackSize.stackSize + 7, 8); // !!
        if (!(lLength % 2 == 0)) {
            lLength++; // Otherwise you get gaps at the end
        }

        for (int i = -8; i > -lLength - 1; i -= 2) {
            build = survivalBuildPiece(STRUCTURE_PIECE_LAYER, stackSize, 3, 6, i, elementBudget, env, false, true);
            if (build >= 0) return build;
        }

        return survivalBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            3,
            6,
            -(lLength + 2),
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean active, boolean aRedstone) {

        // Placeholder
        if (side == facing) {
            if (active) return new ITexture[] { casingTexturePages[0][47], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][47], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][47] };
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("LINAC.hint", 11);
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

        BeamInformation information = this.getInputInformation();
        if (information == null) {
            information = new BeamInformation(0, 0, 0, 0);
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
                + " EU/t(*"
                + getMaxInputAmps()
                + "A)"
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
            // Beamline-specific
            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.info") + ": " + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.temperature") + ": " // Temperature:
                + EnumChatFormatting.DARK_RED
                + machineTemp
                + EnumChatFormatting.RESET
                + " K", // e.g. "137 K"
            StatCollector.translateToLocal("beamline.coolusage") + ": " // Coolant usage:
                + EnumChatFormatting.AQUA
                + (length)
                + EnumChatFormatting.RESET
                + " kL/s", // e.g. "24 kL/s
            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.in_pre")
                + ": "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.particle") + ": " // "Multiblock Beamline Input:"
                + EnumChatFormatting.GOLD
                + Particle.getParticleFromId(information.getParticleId())
                    .getLocalisedName() // e.g. "Electron
                                        // (e-)"
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.energy") + ": " // "Energy:"
                + EnumChatFormatting.DARK_RED
                + information.getEnergy()
                + EnumChatFormatting.RESET
                + " keV", // e.g. "10240 keV"
            StatCollector.translateToLocal("beamline.focus") + ": " // "Focus:"
                + EnumChatFormatting.BLUE
                + information.getFocus()
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.amount") + ": " // "Amount:"
                + EnumChatFormatting.LIGHT_PURPLE
                + information.getRate(),

            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.out_pre")
                + ": "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.particle") + ": " // "Multiblock Beamline Output:"
                + EnumChatFormatting.GOLD
                + Particle.getParticleFromId(this.outputParticleID)
                    .getLocalisedName()
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.energy") + ": " // "Energy:"
                + EnumChatFormatting.DARK_RED
                + this.outputEnergy
                + EnumChatFormatting.RESET
                + " keV",
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
    public IStructureDefinition<MTELINAC> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public boolean addInputHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addInputHatchToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    private String createFocusText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.RED, text, EnumChatFormatting.GRAY);
    }

    private String createEnergyText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.BLUE, text, EnumChatFormatting.GRAY);
    }

    private String coolantLine(String coolant, int kelvin) {
        return "  " + EnumChatFormatting.AQUA
            + coolant
            + EnumChatFormatting.GRAY
            + " : "
            + EnumChatFormatting.GOLD
            + kelvin
            + "K";
    }
}
