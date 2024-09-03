package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gtnhlanth.util.DescTextLocalization.BLUEPRINT_INFO;
import static gtnhlanth.util.DescTextLocalization.addDotText;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.tileentity.recipe.beamline.BeamlineRecipeAdder2;
import gtnhlanth.common.tileentity.recipe.beamline.RecipeSC;
import gtnhlanth.util.DescTextLocalization;

public class MTESourceChamber extends MTEEnhancedMultiBlockBase<MTESourceChamber> implements ISurvivalConstructable {

    private static final IStructureDefinition<MTESourceChamber> STRUCTURE_DEFINITION;

    private ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();

    private static final int CASING_INDEX = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings5, 14);

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
                    .dot(4)
                    .adder(MTESourceChamber::addBeamLineOutputHatch)
                    .build())
            .addElement(
                'i',
                buildHatchAdder(MTESourceChamber.class).atLeast(InputBus)
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .build())
            .addElement(
                'o',
                buildHatchAdder(MTESourceChamber.class).atLeast(OutputBus)
                    .casingIndex(CASING_INDEX)
                    .dot(2)
                    .build())
            .addElement(
                'd',
                buildHatchAdder(MTESourceChamber.class).atLeast(Maintenance, Energy)
                    .casingIndex(CASING_INDEX)
                    .dot(3)
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
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece("sc", stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity te) {
        return new MTESourceChamber(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
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

        if (mte instanceof MTEHatchOutputBeamline) {
            return this.mOutputBeamline.add((MTEHatchOutputBeamline) mte);
        }

        return false;
    }
    /*
     * protected OverclockDescriber createOverclockDescriber() { return new EUNoTotalOverclockDescriber((byte) 4, 1); }
     */

    @Override
    public boolean checkRecipe(ItemStack itemStack) {

        // GTLog.out.print("In checkRecipe");

        // No input particle, so no input quantities

        outputFocus = 0;
        outputEnergy = 0;
        outputParticle = 0;
        outputRate = 0;

        ItemStack[] tItems = this.getStoredInputs()
            .toArray(new ItemStack[0]);
        // GTLog.out.print(Arrays.toString(tItems));
        long tVoltageMaxTier = this.getMaxInputVoltage(); // Used to keep old math the same
        long tVoltageActual = GTValues.VP[(int) this.getInputVoltageTier()];

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

            for (MTEHatchOutputBeamline o : mOutputBeamline) {

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
        for (MTEHatchEnergy tHatch : mEnergyHatches) {
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
    public IStructureDefinition<MTESourceChamber> getStructureDefinition() {
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
