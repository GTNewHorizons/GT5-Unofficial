package gregtech.common.tileentities.machines.multi.beamcrafting;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAMCRAFTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAMCRAFTER_ACTIVE;
import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.common.gui.modularui.multiblock.MTEBeamCrafterGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.loaders.postload.recipes.beamcrafter.BeamCrafterMetadata;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.Particle;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public class MTEBeamCrafter extends MTEBeamMultiBase<MTEBeamCrafter> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int ShieldedAccCasingTextureID = Casings.ShieldedAcceleratorCasing.getTextureId();

    private static final int MAX_BUFFER = 2_000_000_000;
    private static final int MAX_PARALLEL = 1024;

    private static final String NBT_KEY_DESCRIPTOR = "KEY";
    private static final String NBT_VALUE_DESCRIPTOR = "VALUE";

    private int currentRecipeCurrentAmountA = 0;
    private int currentRecipeCurrentAmountB = 0;
    private int currentRecipeMaxAmountA = 0;
    private int currentRecipeMaxAmountB = 0;
    private int currentRecipeParticleIDA;
    private int currentRecipeParticleIDB;

    public Int2IntOpenHashMap bufferMap = new Int2IntOpenHashMap();
    private boolean bufferMapInitialized = false;

    private GTRecipe lastRecipe;

    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("currentRecipeCurrentAmountA", this.currentRecipeCurrentAmountA);
        aNBT.setInteger("currentRecipeCurrentAmountB", this.currentRecipeCurrentAmountB);
        aNBT.setInteger("currentRecipeMaxAmountA", this.currentRecipeMaxAmountA);
        aNBT.setInteger("currentRecipeMaxAmountB", this.currentRecipeMaxAmountB);
        aNBT.setInteger("currentRecipeParticleIDA", this.currentRecipeParticleIDA);
        aNBT.setInteger("currentRecipeParticleIDB", this.currentRecipeParticleIDB);

        aNBT.setBoolean("bufferMapInitialized", this.bufferMapInitialized);
        saveInputMapToNBT(aNBT, bufferMap);
    }

    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.currentRecipeCurrentAmountA = aNBT.getInteger("currentRecipeCurrentAmountA");
        this.currentRecipeCurrentAmountB = aNBT.getInteger("currentRecipeCurrentAmountB");
        this.currentRecipeMaxAmountA = aNBT.getInteger("currentRecipeMaxAmountA");
        this.currentRecipeMaxAmountB = aNBT.getInteger("currentRecipeMaxAmountB");
        this.currentRecipeParticleIDA = aNBT.getInteger("currentRecipeParticleIDA");
        this.currentRecipeParticleIDB = aNBT.getInteger("currentRecipeParticleIDB");

        this.bufferMapInitialized = aNBT.getBoolean("bufferMapInitialized");
        initBufferMapIfNeeded();
        loadInputMapFromNBT(aNBT, bufferMap);
    }

    private void initBufferMapIfNeeded() {
        if (bufferMapInitialized) return;
        for (Particle particle : Particle.VALUES) {
            bufferMap.put(particle.getId(), 0);
        }
        bufferMapInitialized = true;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity baseMetaTileEntity) {
        super.onFirstTick(baseMetaTileEntity);
        initBufferMapIfNeeded();
    }

    public void saveInputMapToNBT(NBTTagCompound aNBT, Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int particleID = entry.getKey();
            int particleBuffer = entry.getValue();

            aNBT.setInteger(NBT_VALUE_DESCRIPTOR + particleID, particleBuffer);
        }
    }

    public void loadInputMapFromNBT(NBTTagCompound aNBT, Map<Integer, Integer> map) {
        for (Particle p : Particle.VALUES) {
            int particleID = p.getId();

            String valueKey = NBT_VALUE_DESCRIPTOR + particleID;

            if (!aNBT.hasKey(valueKey)) continue;
            map.put(particleID, aNBT.getInteger(valueKey));

        }
    }

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
                .atLeast(Energy, ExoticEnergy, InputBus, InputHatch, OutputBus, OutputHatch)
                .casingIndex(ShieldedAccCasingTextureID)
                .hint(1)
                .buildAndChain(Casings.ShieldedAcceleratorCasing.asElement()))
        .addElement('A', chainAllGlasses())
        .addElement('C', buildBeamlineInputHatch(MTEBeamCrafter.class, ShieldedAccCasingTextureID, 2))
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
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip4"))
            .addSeparator()
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip5",
                    MAX_BUFFER))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip6"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip7"))
            .addSeparator()
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip8",
                    MAX_PARALLEL))
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
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttgratecasing"),
                16,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttbeaminhatch"),
                2,
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 8, 2, 6, errors)) return;
        checkHasAnyEnergy(errors);
        checkHasAnyInput(errors);
        checkHasOutputBus(errors);
    }

    @Override
    protected void incrementProgressTime() {

        for (int n = 0; n < this.mInputBeamline.size(); n++) {
            BeamInformation inputParticle = this.getNthInputParticle(n);
            int id = inputParticle.getParticleId();
            int rate = inputParticle.getRate();

            int newAmount = bufferMap.getOrDefault(id, 0) + rate;
            bufferMap.put(id, Math.min(newAmount, MAX_BUFFER));
            this.mInputBeamline.get(n)
                .setContents(null);
        }

        contributeToProgress();
        if (mProgresstime >= mMaxProgresstime) {
            currentRecipeCurrentAmountA = 0;
            currentRecipeCurrentAmountB = 0;
        }
    }

    private void contributeToProgress() {
        int availableA = bufferMap.getOrDefault(currentRecipeParticleIDA, 0);
        int neededA = currentRecipeMaxAmountA - currentRecipeCurrentAmountA;
        int consumeA = Math.min(availableA, neededA);
        bufferMap.put(currentRecipeParticleIDA, availableA - consumeA);

        int availableB = bufferMap.getOrDefault(currentRecipeParticleIDB, 0);
        int neededB = currentRecipeMaxAmountB - currentRecipeCurrentAmountB;
        int consumeB = Math.min(availableB, neededB);
        bufferMap.put(currentRecipeParticleIDB, availableB - consumeB);

        currentRecipeCurrentAmountA += consumeA;
        currentRecipeCurrentAmountB += consumeB;
        mProgresstime += consumeA + consumeB;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (availableAmperage * availableVoltage < recipe.mEUt) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                }
                return super.validateRecipe(recipe);
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setNoOverclock(true);
            }

            @Override
            protected @NotNull ParallelHelper createParallelHelper(@NotNull GTRecipe recipe1) {
                return super.createParallelHelper(recipe1).setMaxParallelCalculator(
                    (GTRecipe recipe, int maxParallel, FluidStack[] fluids, ItemStack[] items) -> {
                        BeamCrafterMetadata metadata = recipe.getMetadata(BEAMCRAFTER_METADATA);
                        if (metadata == null) return 0;
                        double parallel = recipe.maxParallelCalculatedByInputs(maxParallel, fluids, items);
                        if (parallel < 1) return parallel;

                        if (metadata.particleID_A == metadata.particleID_B) {
                            double available = bufferMap.getOrDefault(metadata.particleID_A, 0);
                            parallel = Math.min(parallel, available / (metadata.amount_A + metadata.amount_B));
                        } else {
                            double availableA = bufferMap.getOrDefault(metadata.particleID_A, 0);
                            double availableB = bufferMap.getOrDefault(metadata.particleID_B, 0);
                            parallel = Math.min(parallel, availableA / metadata.amount_A);
                            parallel = Math.min(parallel, availableB / metadata.amount_B);
                        }

                        parallel = Math.max(1, parallel); // allow starting when item and fluid are enough but beam is
                                                          // not
                        return parallel;
                    });
            }

            @Override
            protected @NotNull CheckRecipeResult applyRecipe(@NotNull GTRecipe recipe, @NotNull ParallelHelper helper,
                @NotNull OverclockCalculator calculator, @NotNull CheckRecipeResult result) {
                BeamCrafterMetadata metadata = recipe.getMetadata(BEAMCRAFTER_METADATA);
                if (metadata == null) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                result = super.applyRecipe(recipe, helper, calculator, result);
                if (result.wasSuccessful()) {
                    currentRecipeParticleIDA = metadata.particleID_A;
                    currentRecipeParticleIDB = metadata.particleID_B;
                    currentRecipeCurrentAmountA = 0;
                    currentRecipeCurrentAmountB = 0;
                    int activeParallel = helper.getCurrentParallel();
                    currentRecipeMaxAmountA = metadata.amount_A * activeParallel;
                    currentRecipeMaxAmountB = metadata.amount_B * activeParallel;
                    duration = currentRecipeMaxAmountA + currentRecipeMaxAmountB;
                    calculatedEut = recipe.mEUt; // Set the real eu/t usage or else it will not consume power
                }
                return result;
            }
        }.setEuModifier(0) // Set eu/t to 0 for parallel calculation
            .setMaxParallel(MAX_PARALLEL)
            .setUnlimitedTierSkips();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.beamcrafterRecipes;
    }

    @Override
    protected @NotNull MTEBeamCrafterGui getGui() {
        return new MTEBeamCrafterGui(this);
    }

    public Int2IntOpenHashMap getBufferMap() {
        return bufferMap;
    }

    public void setBufferToZeroForParticle(int id) {
        bufferMap.put(id, 0);
    }

    @Override
    public int getMaxParallelRecipes() {
        return MAX_PARALLEL;
    }

    public int getCurrentRecipeParticleIDA() {
        return currentRecipeParticleIDA;
    }

    public int getCurrentRecipeParticleIDB() {
        return currentRecipeParticleIDB;
    }

}
