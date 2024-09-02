package gtnhlanth.common.tileentity;

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
import static gtnhlanth.util.DescTextLocalization.addDotText;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.StructureLib;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.SimpleShutDownReason;
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
    protected static final String STRUCTURE_PIECE_END = "end";

    private byte glassTier;

    private boolean onEndInnerLayer = false;

    private int machineTemp = 0; // Coolant temperature

    private ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    private ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();

    private static final int CASING_INDEX = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings5, 14);

    private static final byte MIN_GLASS_TIER = 6;

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
            .addElement(
                'b',
                BorosilicateGlass.ofBoroGlass(
                    (byte) 0,
                    MIN_GLASS_TIER,
                    Byte.MAX_VALUE,
                    (te, t) -> te.glassTier = t,
                    te -> te.glassTier))
            .addElement(
                'i',
                buildHatchAdder(MTELINAC.class).hatchClass(MTEHatchInputBeamline.class)
                    .casingIndex(CASING_INDEX)
                    .dot(3)
                    .adder(MTELINAC::addBeamLineInputHatch)
                    .build())
            .addElement(
                'o',
                buildHatchAdder(MTELINAC.class).hatchClass(MTEHatchOutputBeamline.class)
                    .casingIndex(CASING_INDEX)
                    .dot(4)
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
                    .dot(2)
                    .build())

            .addElement(
                'j',
                buildHatchAdder(MTELINAC.class).atLeast(Maintenance, Energy)
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .buildAndChain(ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0)))

            .build();
    }

    private float outputEnergy;
    private int outputRate;
    private int outputParticle;
    private float outputFocus;

    private int length;

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
        tt.addMachineType("Particle Accelerator")
            .addInfo("Controller block for the LINAC")
            .addInfo("Accelerates charged particles to higher energies")
            .addInfo("Increasing length increases output energy, but decreases focus")
            .addInfo("Use a lower temperature coolant to improve focus")
            // .addInfo("Extendable, with a minimum length of 18 blocks")
            .addInfo(DescTextLocalization.BLUEPRINT_INFO)
            .addInfo(DescTextLocalization.BEAMLINE_SCANNER_INFO)
            .addInfo("Valid Coolants:");

        // Valid coolant list
        for (String fluidName : BeamlineRecipeLoader.coolantMap.keySet()) {

            tt.addInfo(
                "- " + FluidRegistry.getFluid(fluidName)
                    .getLocalizedName(null));

        }

        tt.addInfo("Requires (length + 1)kL/s of coolant")
            .addSeparator()
            .beginVariableStructureBlock(7, 7, 7, 7, 19, 83, false)
            .addController("Front bottom")
            .addCasingInfoRange(LanthItemList.SHIELDED_ACCELERATOR_CASING.getLocalizedName(), 325, 1285, false)
            .addCasingInfoRange(LanthItemList.COOLANT_DELIVERY_CASING.getLocalizedName(), 148, 852, false)
            .addCasingInfoRange(LanthItemList.SHIELDED_ACCELERATOR_GLASS.getLocalizedName(), 127, 703, false)
            .addCasingInfoRange("Superconducting Coil Block", 56, 312, false)
            .addCasingInfoRange(LanthItemList.ELECTRODE_CASING.getLocalizedName(), 156, 732, false)
            .addCasingInfoExactly("Grate Machine Casing", 47, false)
            .addCasingInfoExactly("Borosilicate Glass (LuV+)", 48, false)
            .addEnergyHatch(addDotText(1))
            .addMaintenanceHatch(addDotText(1))
            .addInputHatch(addDotText(2))
            .addOutputHatch(addDotText(2))
            .addOtherStructurePart("Beamline Input Hatch", addDotText(3))
            .addOtherStructurePart("Beamline Output Hatch", addDotText(4))

            .toolTipFinisher("GTNH: Lanthanides");;
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

    @Override
    public boolean checkRecipe(ItemStack itemStack) {

        float tempFactor = 0;
        // Focus as determined by multi properties
        float machineFocus = 0;
        // Input particle focus
        float inputFocus = 0;

        // Output focus to be set
        outputFocus = 0;

        float voltageFactor = 0;
        float inputEnergy = 0;

        machineTemp = 0;

        // Energy quantity determined by multi
        float machineEnergy = 0;
        outputEnergy = 0;

        int particleId = 0;
        outputParticle = 0;

        int inputRate = 0;
        outputRate = 0;

        ArrayList<FluidStack> tFluidInputs = this.getStoredFluids();
        if (tFluidInputs.size() == 0) {
            this.doRandomMaintenanceDamage(); // Penalise letting coolant run dry
            this.stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.nocoolant"));
            return false;
        }

        // Coolant input
        FluidStack primFluid = tFluidInputs.get(0);

        // 1b (1000L)/m/operation
        final int fluidConsumed = 1000 * length;

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        if (this.getInputInformation() == null) {
            return false;
        }

        if (this.getInputInformation()
            .getEnergy() == 0) {
            return false;
        }

        particleId = this.getInputInformation()
            .getParticleId();
        Particle inputParticle = Particle.getParticleFromId(particleId);

        if (!inputParticle.canAccelerate()) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.noaccel"));
            return false;
        }

        mMaxProgresstime = 1 * TickTime.SECOND;
        // Consume the input tier's corresponding practical voltage instead of the maximum suggested by the logic
        mEUt = (int) -GTValues.VP[(int) this.getInputVoltageTier()];

        // Particle stays the same with this multiblock
        outputParticle = particleId;

        if (primFluid.isFluidEqual(new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1))) {
            tempFactor = calculateTemperatureFactor(60); // Default temp of 300 is unreasonable
            machineTemp = 60; // Solely for tricorder use
        } else {
            tempFactor = calculateTemperatureFactor(
                primFluid.getFluid()
                    .getTemperature());
            machineTemp = primFluid.getFluid()
                .getTemperature(); // Solely for tricorder use
        }

        machineFocus = Math.max(((-0.9f) * this.length * tempFactor) + 110, 5); // Min of 5
        if (machineFocus > 90) { // Max of 90
            machineFocus = 90;
        }

        inputFocus = this.getInputInformation()
            .getFocus();

        outputFocus = (inputFocus > machineFocus) ? ((inputFocus + machineFocus) / 2)
            : inputFocus * (machineFocus / 100); // If input focus > machine focus, take the average of both, else
                                                 // weigh the former by the latter

        long voltage = this.getMaxInputVoltage();
        // voltageFactor = calculateVoltageFactor(voltage);

        // machineEnergy = Math.max(-((60) / this.length) * voltageFactor + 60_000, 2000); // Minimum of 2000keV

        machineEnergy = (float) Math.max(length / 4 * Math.pow(voltage, 1.0 / 3.0), 50); // Minimum of 50keV

        inputEnergy = this.getInputInformation()
            .getEnergy();
        /*
         * outputEnergy = Math.min(
         * (1 + inputEnergy / Particle.getParticleFromId(outputParticle)
         * .maxSourceEnergy()) * machineEnergy,
         * 120_000); // TODO more complex calculation than just
         * // addition
         */

        outputEnergy = (float) Math.pow(
            10,
            1 + inputEnergy / Particle.getParticleFromId(outputParticle)
                .maxSourceEnergy())
            * machineEnergy;

        inputRate = this.getInputInformation()
            .getRate();
        outputRate = inputRate; // Cannot increase rate with this multiblock

        if (Util.coolantFluidCheck(primFluid, fluidConsumed)) {

            this.stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.inscoolant"));
            return false;

        }

        primFluid.amount -= fluidConsumed;

        Fluid fluidOutput = BeamlineRecipeLoader.coolantMap.get(
            primFluid.getFluid()
                .getName());

        if (Objects.isNull(fluidOutput)) return false;

        FluidStack fluidOutputStack = new FluidStack(fluidOutput, fluidConsumed);

        if (Objects.isNull(fluidOutputStack)) return false;

        this.addFluidOutputs(new FluidStack[] { fluidOutputStack });

        outputAfterRecipe();

        return true;
    }

    private void outputAfterRecipe() {

        if (!mOutputBeamline.isEmpty()) {

            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(outputEnergy, outputRate, outputParticle, outputFocus));

            for (MTEHatchOutputBeamline o : mOutputBeamline) {

                o.q = packet;
            }
        }
    }

    @Override
    public void stopMachine() {

        // GTLog.out.print("Machine stopped");
        outputFocus = 0;
        outputEnergy = 0;
        outputParticle = 0;
        outputRate = 0;
        machineTemp = 0;
        super.stopMachine();
    }

    @Override
    public void stopMachine(ShutDownReason reason) {

        outputFocus = 0;
        outputEnergy = 0;
        outputParticle = 0;
        outputRate = 0;
        machineTemp = 0;
        super.stopMachine(reason);

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

        return new String[] {
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(getMaxInputVoltage())
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
                + Float.toString(mEfficiency / 100.0F)
                + EnumChatFormatting.RESET
                + " %",

            /* 7 */ EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.info")
                + ": "
                + EnumChatFormatting.RESET,

            StatCollector.translateToLocal("beamline.temperature") + ": " // Temperature:
                + EnumChatFormatting.DARK_RED
                + machineTemp
                + EnumChatFormatting.RESET
                + " K", // e.g. "137 K"

            StatCollector.translateToLocal("beamline.coolusage") + ": " // Coolant usage:
                + EnumChatFormatting.AQUA
                + length
                + EnumChatFormatting.RESET
                + " kL/s", // e.g. "24 kL/s

            /* 8 */ EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.in_pre")
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
            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.out_pre") // "Multiblock Beamline
                                                                                         // Output:"
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

    private BeamInformation getInputInformation() {

        for (MTEHatchInputBeamline in : this.mInputBeamline) { // Easy way to find the desired input. Efficient? No.
                                                               // Will it matter? No :boubs_glasses:

            if (in.q == null) return new BeamInformation(0, 0, 0, 0);
            // if (in.q == null) return new BeamInformation(10000, 10, 0, 90); // temporary for testing purposes

            return in.q.getContent();
        }
        return null;
    }

    private static float calculateTemperatureFactor(int fluidTemp) {

        float factor = (float) Math.pow(1.1, 0.2 * fluidTemp);
        return factor;
    }

    /*
     * private static float calculateVoltageFactor(long voltage) {
     * float factor = (float) Math.pow(1.00009, -(0.1 * voltage - 114000));
     * return factor;
     * }
     */

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("LINAC.hint", 11);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity mte, ItemStack stack) {

        mInputBeamline.clear();
        mOutputBeamline.clear();

        this.outputEnergy = 0;
        this.outputRate = 0;
        this.outputParticle = 0;
        this.outputFocus = 0;

        this.glassTier = 0;

        this.onEndInnerLayer = false;

        length = 8; // Base piece length

        if (!checkPiece(STRUCTURE_PIECE_BASE, 3, 6, 0)) return false;

        while (length < 128) {

            if (!checkPiece(STRUCTURE_PIECE_LAYER, 3, 6, -length)) {
                if (!checkPiece(STRUCTURE_PIECE_END, 3, 6, -length)) {
                    return false;
                }
                break;
            } ;

            length += 2;
        }

        // if (!checkPiece(STRUCTURE_PIECE_END, 3, 6, -length)) return false;

        // Likely off by one or two, not visible to player however so doesn't particularly matter
        length += 8;

        return this.mInputBeamline.size() == 1 && this.mOutputBeamline.size() == 1
            && this.mMaintenanceHatches.size() == 1
            && this.mEnergyHatches.size() <= 2
            && this.glassTier >= MIN_GLASS_TIER;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 3, 6, 0);

        int lLength = Math.max(stackSize.stackSize + 7, 8); // !!

        if (!(lLength % 2 == 0)) {
            lLength++; // Otherwise you get gaps at the end
        }

        for (int i = -8; i > -lLength - 1; i -= 2) {

            // GTLog.out.print("Building inner piece! i = " + i);

            buildPiece(STRUCTURE_PIECE_LAYER, stackSize, hintsOnly, 3, 6, i);
        }

        buildPiece(STRUCTURE_PIECE_END, stackSize, hintsOnly, 3, 6, -(lLength + 2));

        StructureLib.addClientSideChatMessages("Length: " + (11 + lLength) + " blocks.");
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {

        elementBudget = 200; // Maybe make a config

        if (mMachine) return -1;

        int build = 0;

        build = survivialBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 3, 6, 0, elementBudget, env, false, true);

        if (build >= 0) return build; // Incomplete

        int lLength = Math.max(stackSize.stackSize + 7, 8); // !!

        if (!(lLength % 2 == 0)) {
            lLength++; // Otherwise you get gaps at the end
        }

        for (int i = -8; i > -lLength - 1; i -= 2) {

            build = survivialBuildPiece(STRUCTURE_PIECE_LAYER, stackSize, 3, 6, i, elementBudget, env, false, true);

            if (build >= 0) return build;

        }

        int finalOutput = survivialBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            3,
            6,
            -(lLength + 2),
            elementBudget,
            env,
            false,
            true);

        return finalOutput;
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
    public IStructureDefinition<MTELINAC> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public boolean addInputHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        this.onEndInnerLayer = true;
        return super.addInputHatchToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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
}
