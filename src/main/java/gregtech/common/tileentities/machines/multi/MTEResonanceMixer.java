package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCREEN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCREEN_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsElements;

public class MTEResonanceMixer extends MTEExtendedPowerMultiBlockBase<MTEResonanceMixer>
    implements ISurvivalConstructable {

    private Byte mSolenoidLevel = null;
    private final static int STRUCTURE_OFFSET_X = 5;
    private final static int STRUCTURE_OFFSET_Y = 13;
    private final static int STRUCTURE_OFFSET_Z = 1;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final ArrayList<TieredPlasma> TIERED_PLASMAS = new ArrayList<>(
        Arrays.asList(
            new TieredPlasma(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN, 30, 2.25f, 1.1f, 0),
            new TieredPlasma(MaterialsElements.getInstance().NEPTUNIUM, 15, 4f, 1.5f, 0),
            new TieredPlasma(MaterialsUEVplus.QuarkGluonPlasma, 5, 6f, 2.5f, 1)));

    private static final IStructureDefinition<MTEResonanceMixer> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEResonanceMixer>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{
                {"   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   "},
                {"           ", " CCCCCCCCC ", " CCCCCCCCC ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", " CCCC~CCCC ", " CCCCCCCCC "},
                {"   SSSSS   ", " CCCCCCCCC ", " CG     GC ", "  G     G  ", "  G     G  ", "  G  C  G  ", "  G  C  G  ", "  G  S  G  ", "  G  S  G  ", "  G  S  G  ", "  G  S  G  ", "  G  S  G  ", "  G  C  G  ", " CG  C  GC ", " CCSSCSSCC "},
                {"S SS   SS S", "SCCCCCCCCCS", "SC       CS", "SC       CS", "SC       CS", "SC CCCCC CS", "SC   B   CS", "SC BBBBB CS", "SC   B   CS", "SC   B   CS", "SC   B   CS", "SC BBBBB CS", "SC   B   CS", "SC   C   CS", "SCSSCCCSSCS"},
                {"  S     S  ", " CCCCCCCCC ", " C       C ", " C       C ", " C       C ", " C C   C C ", " C       C ", " C B   B C ", " C       C ", " C       C ", " C       C ", " C B   B C ", " C       C ", " C       C ", " CSCCCCCSC "},
                {"  S     S  ", " CCCCCCCCC ", " C       C ", " C       C ", " C       C ", " CCC   CCC ", " CCB   BCC ", " CSB   BSC ", " CSB   BSC ", " CSB   BSC ", " CSB   BSC ", " CSB   BSC ", " CCB   BCC ", " CCC   CCC ", " CCCCCCCCC "},
                {"  S     S  ", " CCCCCCCCC ", " C       C ", " C       C ", " C       C ", " C C   C C ", " C       C ", " C B   B C ", " C       C ", " C       C ", " C       C ", " C B   B C ", " C       C ", " C       C ", " CSCCCCCSC "},
                {"S SS   SS S", "SCCCCCCCCCS", "SC       CS", "SC       CS", "SC       CS", "SC CCCCC CS", "SC   B   CS", "SC BBBBB CS", "SC   B   CS", "SC   B   CS", "SC   B   CS", "SC BBBBB CS", "SC   B   CS", "SC   C   CS", "SCSSCCCSSCS"},
                {"   SSSSS   ", " CCCCCCCCC ", " CG     GC ", "  G     G  ", "  G     G  ", "  G  C  G  ", "  G  C  G  ", "  G  S  G  ", "  G  S  G  ", "  G  S  G  ", "  G  S  G  ", "  G  S  G  ", "  G  C  G  ", " CG  C  GC ", " CCSSCSSCC "},
                {"           ", " CCCCCCCCC ", " CCCCCCCCC ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", "   CCCCC   ", " CCCCCCCCC ", " CCCCCCCCC "},
                {"   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   ", "   S   S   "}})
        //spotless:on
        .addElement(
            'C',
            buildHatchAdder(MTEResonanceMixer.class).atLeast(InputBus, OutputBus, InputHatch, OutputHatch, ExoticEnergy)
                .casingIndex(Casings.ResonanceMixerCasing.textureId)
                .dot(1)
                .buildAndChain(
                    onElementPass(MTEResonanceMixer::onCasingAdded, Casings.ResonanceMixerCasing.asElement())))
        .addElement('S', Casings.SoundProofCasing.asElement())
        // Maybe keep this? While it is restrictive, this would allow any of the fluids in this hatch to NOT clash with
        // existing recipes inputs, which could be good. - chrom.
        /*
         * .addElement(
         * 'H',
         * buildHatchAdder(MTEResonanceMixer.class).hatchClass(MTEHatchInput.class)
         * .adder(MTEResonanceMixer::addPlasmaInputToMachineList)
         * .casingIndex(Casings.ResonanceMixerCasing.textureId)
         * .dot(2)
         * .buildAndChain(Casings.ResonanceMixerCasing.asElement()))
         */
        .addElement(
            'B',
            GTStructureChannels.SOLENOID
                .use(ofSolenoidCoil(MTEResonanceMixer::setSolenoidLevel, MTEResonanceMixer::getSolenoidLevel)))
        .addElement('G', chainAllGlasses())
        .build();

    private Byte getSolenoidLevel() {
        return mSolenoidLevel;
    }

    private void setSolenoidLevel(byte level) {
        mSolenoidLevel = level;
    }

    public MTEResonanceMixer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEResonanceMixer(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEResonanceMixer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEResonanceMixer(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Casings.ResonanceMixerCasing.getCasingTexture(), TextureFactory.builder()
                    .addIcon(OVERLAY_SCREEN)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_SCREEN_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Casings.ResonanceMixerCasing.getCasingTexture(), TextureFactory.builder()
                    .addIcon(OVERLAY_FUSION1)
                    .extFacing()
                    .build() };
            }
        } else {
            rTexture = new ITexture[] { Casings.ResonanceMixerCasing.getCasingTexture() };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("poopshittium")
            .addBulkMachineInfo(64, 4.5f, 1.25f)
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("pepe peepo", 14, false)
            .addCasingInfoExactly("pepe peepo", 6, false)
            .addCasingInfoExactly("pepe peepo", 4, false)
            .addInputBus("pepe peepo", 1)
            .addOutputBus("pepe peepo", 1)
            .addInputHatch("pepe peepo", 1)
            .addOutputHatch("pepe peepo", 1)
            .addEnergyHatch("pepe peepo", 1)
            .addMaintenanceHatch("pepe peepo", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mSolenoidLevel = null;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_OFFSET_X, STRUCTURE_OFFSET_Y, STRUCTURE_OFFSET_Z)) {
            return false;
        }

        if (mCasingAmount < 14) return false;
        return true;
    }

    private float speed = 1f;
    private int gainedOC = 0;
    private float parallelX = 1f;
    private TieredPlasma currentPlasma = null;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                currentPlasma = findTieredPlasma();
                if (currentPlasma == null) return SimpleCheckRecipeResult.ofFailure("invalidfluidsup");
                setStats(currentPlasma);
                setSpeedBonus(1f / speed);
                return super.validateRecipe(recipe);
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setMaxOverclocks(
                    gainedOC + (GTUtility.getTier(getAverageInputVoltage()) - GTUtility.getTier(recipe.mEUt)));
            }
        }.setEuModifier(1.25);
    }

    public void setStats(TieredPlasma plasma) {
        if (plasma != null) {
            speed = plasma.speed;
            gainedOC = plasma.overclocks;
            parallelX = plasma.parallelX;
        }
    }

    @Override
    public int getMaxParallelRecipes() {
        return (Math.round(parallelX * 4 * GTUtility.getTier(this.getMaxInputVoltage())));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.mixerRecipes;
    }

    public TieredPlasma findTieredPlasma() {
        // Loop over all hatches and find the first match with a valid fluid
        for (MTEHatchInput hatch : mInputHatches) {
            Optional<TieredPlasma> fluid = TIERED_PLASMAS.stream()
                .filter(candidate -> drain(hatch, candidate.getPlasma(), false))
                .findFirst();
            if (fluid.isPresent()) return fluid.get();
        }
        return null;
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);
        // try to drain plasma every second. if cant for some reason, crash machine.
        if (mMaxProgresstime > 0 && aTick % 20 == 0) {

            if (this.currentPlasma != null) {
                FluidStack plasma = this.currentPlasma.getPlasma();
                for (MTEHatchInput hatch : mInputHatches) {
                    if (drain(hatch, plasma, false)) {
                        drain(hatch, plasma, true);
                        return;
                    }
                }
                stopMachine(ShutDownReasonRegistry.outOfFluid(plasma));
            }

        }
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
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    private static class TieredPlasma {

        public Materials gtMat;
        public Material gtppMat;
        public int amount;
        public float speed;
        public float parallelX;
        public int overclocks;

        // dont you just love the gt5u codebase :D
        public TieredPlasma(Materials gtMat, int amount, float speed, float parallelX, int overclocks) {
            this.gtMat = gtMat;
            this.amount = amount;
            this.speed = speed;
            this.parallelX = parallelX;
            this.overclocks = overclocks;
            gtppMat = null;
        }

        public TieredPlasma(Material gtppMat, int amount, float speed, float parallelX, int overclocks) {
            this.gtppMat = gtppMat;
            this.amount = amount;
            this.speed = speed;
            this.parallelX = parallelX;
            this.overclocks = overclocks;
            gtMat = null;
        }

        public FluidStack getPlasma() {
            if (gtMat != null) {
                return gtMat.getFluid(amount); // we know this is only QGP, so this is fine.
            }
            if (gtppMat != null) {
                return new FluidStack(gtppMat.getPlasma(), amount);
            }
            return null;
        }

    }
}
