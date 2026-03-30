package gregtech.common.tileentities.machines.multi.beamcrafting;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAMCRAFTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAMCRAFTER_ACTIVE;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.gui.modularui.multiblock.MTEBeamCrafterGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.loaders.postload.recipes.beamcrafter.BeamCrafterMetadata;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;

public class MTEBeamCrafter extends MTEBeamMultiBase<MTEBeamCrafter> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int ShieldedAccCasingTextureID = Casings.ShieldedAcceleratorCasing.getTextureId();

    private int currentRecipeCurrentAmountA = 0;
    private int currentRecipeCurrentAmountB = 0;
    private int currentRecipeMaxAmountA = 0;
    private int currentRecipeMaxAmountB = 0;

    private int currentRecipeParticleIDA;
    private int currentRecipeParticleIDB;

    private GTRecipe lastRecipe;

    private static final IStructureDefinition<MTEBeamCrafter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEBeamCrafter>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "                 ",
                " DDD         DDD ",
                " DCD         DCD ",
                " DDD         DDD ",
                "                 "
            },{
                " BBB         BBB ",
                "B   B       B   B",
                "B   B       B   B",
                "B   B       B   B",
                " BBB         BBB "
            },{
                " BBB         BBB ",
                "B   B       B   B",
                "B   A       A   B",
                "B   B       B   B",
                " BBB         BBB "
            },{
                " BBB         BBB ",
                "B   B       B   B",
                "B   A       A   B",
                "B   B       B   B",
                " BBB         BBB "
            },{
                " BBB         BBB ",
                "B   BB     BB   B",
                "B   AA     AA   B",
                "B   BB     BB   B",
                " BBB         BBB "
            },{
                "  BBB       BBB  ",
                " B   BB   BB   B ",
                " A   AA   AA   A ",
                " B   BB   BB   B ",
                "  BBB       BBB  "
            },{
                "  BBBB     BBBB  ",
                " B    BBBBB    B ",
                " A    AB~BA    A ",
                " B    BBBBB    B ",
                "  BBBB     BBBB  "
            },{
                "   BBBBBBBBBBB   ",
                "  B           B  ",
                "  A           A  ",
                "  B           B  ",
                "   BBBBBBBBBBB   "
            },{
                "    BBBBBBBBB    ",
                "   B         B   ",
                "   A         A   ",
                "   B         B   ",
                "    BBBBBBBBB    "
            },{
                "      BBBBB      ",
                "    BB     BB    ",
                "    AA     AA    ",
                "    BB     BB    ",
                "      BBBBB      "
            },{
                "                 ",
                "      BBBBB      ",
                "      BBBBB      ",
                "      BBBBB      ",
                "                 "
            }})
        //spotless:on
        .addElement(
            'B',
            buildHatchAdder(MTEBeamCrafter.class)
                .atLeast(Energy, ExoticEnergy, Maintenance, InputBus, InputHatch, OutputBus, OutputHatch)
                .casingIndex(ShieldedAccCasingTextureID)
                .hint(1)
                .buildAndChain(Casings.ShieldedAcceleratorCasing.asElement()))
        .addElement('A', chainAllGlasses())
        .addElement(
            'C',
            buildHatchAdder(MTEBeamCrafter.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(ShieldedAccCasingTextureID)
                .hint(2)
                .adder(MTEBeamCrafter::addBeamLineInputHatch)
                .build()) // beamline input hatch
        .addElement('D', Casings.GrateMachineCasing.asElement())
        .build();

    public MTEBeamCrafter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBeamCrafter(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEBeamCrafter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBeamCrafter(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAMCRAFTER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAMCRAFTER_ACTIVE)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAMCRAFTER)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture() };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(
            StatCollector
                .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.machinetype"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip1"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip2"))
            .addSeparator()
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip3"))
            .addSeparator()
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip4"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip5"))
            .addSeparator()
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip6"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip7"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip8"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip9"))
            .addSeparator()
            .beginStructureBlock(17, 5, 11, false)
            .addController(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttcontroller"))
            .addCasingInfoMin(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttcasing"),
                224,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanyglass"),
                26,
                false)
            .addInputBus(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanycasing"),
                1)
            .addOutputBus(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanycasing"),
                1)
            .addInputHatch(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanycasing"),
                1)
            .addOutputHatch(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanycasing"),
                1)
            .addEnergyHatch(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanycasing"),
                1)
            .addMaintenanceHatch(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanycasing"),
                1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addTecTechHatchInfo()
            .toolTipFinisher(GTAuthors.AuthorHamCorp);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 8, 2, 6);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 8, 2, 6, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 8, 2, 6);
    }

    private boolean isInputParticleInRecipe(BeamInformation inputParticle_A, BeamInformation inputParticle_B,
        BeamCrafterMetadata metadata) {

        int particleID_x = metadata.particleID_A;
        int particleID_y = metadata.particleID_B;
        float minEnergy_x = metadata.minEnergy_A;
        float minEnergy_y = metadata.minEnergy_B;

        int inputParticleID_A = inputParticle_A.getParticleId();
        int inputParticleID_B = inputParticle_B.getParticleId();
        float inputEnergy_A = inputParticle_A.getEnergy();
        float inputEnergy_B = inputParticle_B.getEnergy();

        // possibilities: (A = x, B = y); (A = y, B = x)

        return ((inputParticleID_A == particleID_x && inputParticleID_B == particleID_y
            && inputEnergy_A > minEnergy_x
            && inputEnergy_B > minEnergy_y)
            || (inputParticleID_A == particleID_y && inputParticleID_B == particleID_x
                && inputEnergy_A > minEnergy_y
                && inputEnergy_B > minEnergy_x));

    }

    private int progressContribution(int currentAmount, int maxAmount, int rate) {
        if (currentAmount <= maxAmount) {
            return rate;
        } else {
            return (currentAmount - maxAmount);
        }
    }

    @Override
    protected void incrementProgressTime() {

        BeamInformation inputParticle_A = this.getNthInputParticle(0);
        BeamInformation inputParticle_B = this.getNthInputParticle(1);

        if (inputParticle_A != null && inputParticle_B != null) {
            int inputParticleRateA = inputParticle_A.getRate();
            int inputParticleRateB = inputParticle_B.getRate();
            int inputParticleIDA = inputParticle_A.getParticleId();
            int inputParticleIDB = inputParticle_B.getParticleId();

            // beamline input hatch array matches order of particles from recipe
            if (inputParticleIDA == this.currentRecipeParticleIDA) {
                this.currentRecipeCurrentAmountA += inputParticleRateA;
                mProgresstime += this.progressContribution(
                    this.currentRecipeCurrentAmountA,
                    this.currentRecipeMaxAmountA,
                    inputParticleRateA);

                this.currentRecipeCurrentAmountB += inputParticleRateB;
                mProgresstime += this.progressContribution(
                    this.currentRecipeCurrentAmountB,
                    this.currentRecipeMaxAmountB,
                    inputParticleRateB);
            }
            // beamline input hatch array does NOT match order of particles from recipe
            else {
                this.currentRecipeCurrentAmountA += inputParticleRateB;
                mProgresstime += this.progressContribution(
                    this.currentRecipeCurrentAmountA,
                    this.currentRecipeMaxAmountA,
                    inputParticleRateB);

                this.currentRecipeCurrentAmountB += inputParticleRateA;
                mProgresstime += this.progressContribution(
                    this.currentRecipeCurrentAmountB,
                    this.currentRecipeMaxAmountB,
                    inputParticleRateA);
            }
        }
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {

        this.currentRecipeCurrentAmountA = 0;
        this.currentRecipeCurrentAmountB = 0;
        this.currentRecipeMaxAmountA = 0;
        this.currentRecipeMaxAmountB = 0;

        ArrayList<ItemStack> tItems = this.getStoredInputs();
        ItemStack[] inputItems = tItems.toArray(new ItemStack[0]);
        ArrayList<FluidStack> tFluids = this.getStoredFluids();
        FluidStack[] inputFluids = tFluids.toArray(new FluidStack[0]);

        long tVoltageActual = GTValues.VP[(int) this.getInputVoltageTier()];

        GTRecipe tRecipe = RecipeMaps.beamcrafterRecipes.findRecipeQuery()
            .items(inputItems)
            .fluids(inputFluids)
            .voltage(tVoltageActual)
            .filter((GTRecipe recipe) -> {
                BeamCrafterMetadata metadata = recipe.getMetadata(BEAMCRAFTER_METADATA);
                if (metadata == null) return false;

                BeamInformation inputParticle_A = this.getNthInputParticle(0);
                BeamInformation inputParticle_B = this.getNthInputParticle(1);

                if ((inputParticle_A != null) && (inputParticle_B != null)) {
                    return isInputParticleInRecipe(inputParticle_A, inputParticle_B, metadata);
                }
                return false;
            })
            .cachedRecipe(this.lastRecipe)
            .find();
        if (tRecipe == null) return CheckRecipeResultRegistry.NO_RECIPE;

        BeamCrafterMetadata metadata = tRecipe.getMetadata(BEAMCRAFTER_METADATA);
        if (metadata == null) return CheckRecipeResultRegistry.NO_RECIPE;

        BeamInformation inputParticle_A = this.getNthInputParticle(0);
        BeamInformation inputParticle_B = this.getNthInputParticle(1);
        if (inputParticle_A == null || inputParticle_B == null) return CheckRecipeResultRegistry.NO_RECIPE;

        if (!isInputParticleInRecipe(inputParticle_A, inputParticle_B, metadata))
            return CheckRecipeResultRegistry.NO_RECIPE;

        this.currentRecipeParticleIDA = metadata.particleID_A;
        this.currentRecipeParticleIDB = metadata.particleID_B;

        this.currentRecipeMaxAmountA = metadata.amount_A;
        this.currentRecipeMaxAmountB = metadata.amount_B;
        // total time to finish recipe in ticks is the sum of the required Amounts
        this.mMaxProgresstime = this.currentRecipeMaxAmountA + this.currentRecipeMaxAmountB;

        if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1)
            return CheckRecipeResultRegistry.NO_RECIPE;

        if (!tRecipe.equals(this.lastRecipe)) this.lastRecipe = tRecipe;

        tRecipe.consumeInput(1, inputFluids, inputItems);

        if (tRecipe.mOutputChances != null && tRecipe.mOutputs != null) {
            for (int i = 0; i < tRecipe.mOutputChances.length; i++) {
                if (XSTR_INSTANCE.nextInt(10000) < tRecipe.mOutputChances[i]) {
                    this.mOutputItems[i] = tRecipe.mOutputs[i].copy();
                }
            }
        } else {
            this.mOutputItems = ArrayExt.copyItemsIfNonEmpty(tRecipe.mOutputs);
        }

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        mEUt = (int) -tVoltageActual;

        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.beamcrafterRecipes;
    }

    public int getCurrentRecipeCurrentAmountA() {
        return this.currentRecipeCurrentAmountA;
    }

    public int getCurrentRecipeCurrentAmountB() {
        return this.currentRecipeCurrentAmountB;
    }

    public int getCurrentRecipeMaxAmountA() {
        return this.currentRecipeMaxAmountA;
    }

    public int getCurrentRecipeMaxAmountB() {
        return this.currentRecipeMaxAmountB;
    }

    public int getCurrentRecipeParticleIDA() {
        return this.currentRecipeParticleIDA;
    }

    public int getCurrentRecipeParticleIDB() {
        return this.currentRecipeParticleIDB;
    }

    @Override
    protected @NotNull MTEBeamCrafterGui getGui() {
        return new MTEBeamCrafterGui(this);
    }
}
