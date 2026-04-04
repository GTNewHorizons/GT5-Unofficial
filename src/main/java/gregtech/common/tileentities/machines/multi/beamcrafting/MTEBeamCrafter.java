package gregtech.common.tileentities.machines.multi.beamcrafting;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAMCRAFTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAMCRAFTER_ACTIVE;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;
import java.util.HashMap;
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
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;

public class MTEBeamCrafter extends MTEBeamMultiBase<MTEBeamCrafter> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int ShieldedAccCasingTextureID = Casings.ShieldedAcceleratorCasing.getTextureId();

    private static final int MAX_BUFFER = 2_000_000_000;
    private static final int MAX_PARALLEL = 1024;
    private static final int BEAM_AMOUNT_TO_BUFFER_FACTOR = 1;

    private static final String NBT_KEY_DESCRIPTOR = "KEY";
    private static final String NBT_VALUE_DESCRIPTOR = "VALUE";

    private int currentRecipeCurrentAmountA = 0;
    private int currentRecipeCurrentAmountB = 0;
    private int currentRecipeMaxAmountA = 0;
    private int currentRecipeMaxAmountB = 0;
    private int currentRecipeParticleIDA;
    private int currentRecipeParticleIDB;

    private int activeParallel = 1;

    public Map<Integer, Integer> bufferMap = new HashMap<>();
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
        for (int i = 0; i < Particle.VALUES.length; i++) {
            bufferMap.put(i, 0);
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

            aNBT.setInteger(NBT_KEY_DESCRIPTOR + particleID, particleID);
            aNBT.setInteger(NBT_VALUE_DESCRIPTOR + particleID, particleBuffer);
        }
    }

    public void loadInputMapFromNBT(NBTTagCompound aNBT, Map<Integer, Integer> map) {
        for (Particle p : Particle.VALUES) {
            final int particleID = p.getId();
            if (!aNBT.hasKey(NBT_KEY_DESCRIPTOR + particleID)) continue;
            // assume that if key exists, value exists as well
            // can skip the key nbt mapping, as the particle is in NBT
            map.put(particleID, aNBT.getInteger(NBT_VALUE_DESCRIPTOR + particleID));

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
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip4"))
            .addSeparator()
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip5"))
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip6",
                    BEAM_AMOUNT_TO_BUFFER_FACTOR))
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip7",
                    MAX_BUFFER))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip8"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip9"))
            .addSeparator()
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "gt.blockmachines.multimachine.beamcrafting.beamcrafter.tooltip10",
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 8, 2, 6);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return true;
    }

    @Override
    protected void incrementProgressTime() {

        for (int n = 0; n < this.mInputBeamline.size(); n++) {
            BeamInformation inputParticle = this.getNthInputParticle(n);
            int id = inputParticle.getParticleId();
            int rate = inputParticle.getRate();

            int newAmount = bufferMap.getOrDefault(id, 0) + BEAM_AMOUNT_TO_BUFFER_FACTOR * rate;
            bufferMap.put(id, Math.min(newAmount, MAX_BUFFER));
            this.mInputBeamline.get(n)
                .setContents(null);
        }

        contributeToProgress(this.currentRecipeParticleIDA, this.currentRecipeParticleIDB);
        if (mProgresstime >= mMaxProgresstime) {
            currentRecipeCurrentAmountA = 0;
            currentRecipeCurrentAmountB = 0;
        }
    }

    private void contributeToProgress(int recipeParticleIDA, int recipeParticleIDB) {
        int availableA = bufferMap.getOrDefault(recipeParticleIDA, 0);
        int availableB = bufferMap.getOrDefault(recipeParticleIDB, 0);

        int neededA = this.activeParallel * (currentRecipeMaxAmountA - currentRecipeCurrentAmountA);
        int neededB = this.activeParallel * (currentRecipeMaxAmountB - currentRecipeCurrentAmountB);

        int consumedA = Math.min(availableA, neededA);
        int consumedB = Math.min(availableB, neededB);

        bufferMap.put(recipeParticleIDA, availableA - consumedA);
        bufferMap.put(recipeParticleIDB, availableB - consumedB);

        currentRecipeCurrentAmountA += consumedA;
        currentRecipeCurrentAmountB += consumedB;

        mProgresstime += consumedA + consumedB;

    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        this.currentRecipeMaxAmountA = 0;
        this.currentRecipeMaxAmountB = 0;
        this.activeParallel = 1;

        ArrayList<ItemStack> tItems = this.getStoredInputs();
        ItemStack[] inputItems = tItems.toArray(new ItemStack[0]);
        ArrayList<FluidStack> tFluids = this.getStoredFluids();
        FluidStack[] inputFluids = tFluids.toArray(new FluidStack[0]);

        long tVoltageActual = GTValues.VP[(int) this.getInputVoltageTier()];

        GTRecipe tRecipe = RecipeMaps.beamcrafterRecipes.findRecipeQuery()
            .items(inputItems)
            .fluids(inputFluids)
            .voltage(tVoltageActual)
            .filter((GTRecipe recipe) -> (recipe.getMetadata(BEAMCRAFTER_METADATA) != null))
            .cachedRecipe(this.lastRecipe)
            .find();
        if (tRecipe == null) return CheckRecipeResultRegistry.NO_RECIPE;

        BeamCrafterMetadata metadata = tRecipe.getMetadata(BEAMCRAFTER_METADATA);
        if (metadata == null) return CheckRecipeResultRegistry.NO_RECIPE;

        this.currentRecipeParticleIDA = metadata.particleID_A;
        this.currentRecipeParticleIDB = metadata.particleID_B;

        this.currentRecipeMaxAmountA = metadata.amount_A;
        this.currentRecipeMaxAmountB = metadata.amount_B;

        // total time to finish recipe in ticks is the sum of the required Amounts
        this.mMaxProgresstime = this.currentRecipeMaxAmountA + this.currentRecipeMaxAmountB;

        if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1)
            return CheckRecipeResultRegistry.NO_RECIPE;

        if (!tRecipe.equals(this.lastRecipe)) this.lastRecipe = tRecipe;

        // --- parallels ---
        int parallel = MAX_PARALLEL;

        // limit by available items and fluids
        parallel = (int) (tRecipe.maxParallelCalculatedByInputs(parallel, inputFluids, inputItems));

        // limit by beam buffer
        int availableA = bufferMap.getOrDefault(this.currentRecipeParticleIDA, 0);
        int availableB = bufferMap.getOrDefault(this.currentRecipeParticleIDB, 0);

        parallel = Math.min(parallel, availableA / this.currentRecipeMaxAmountA);
        parallel = Math.min(parallel, availableB / this.currentRecipeMaxAmountB);

        parallel = Math.max(1, parallel); // so it can't be 0
        this.activeParallel = parallel;

        tRecipe.consumeInput(this.activeParallel, inputFluids, inputItems);

        if (tRecipe.mOutputs != null) {
            this.mOutputItems = new ItemStack[tRecipe.mOutputs.length];
            if (tRecipe.mOutputChances != null) {
                for (int i = 0; i < tRecipe.mOutputChances.length; i++) {
                    if (XSTR_INSTANCE.nextInt(10000) < tRecipe.mOutputChances[i]) {
                        this.mOutputItems[i] = tRecipe.mOutputs[i].copy();
                    }
                }
            } else {
                this.mOutputItems = ArrayExt.copyItemsIfNonEmpty(tRecipe.mOutputs);
            }

            // multiply everything by parallel count
            for (ItemStack mOutputItem : this.mOutputItems) {
                if (mOutputItem != null) {
                    mOutputItem.stackSize *= this.activeParallel;
                }
            }

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

    @Override
    protected @NotNull MTEBeamCrafterGui getGui() {
        return new MTEBeamCrafterGui(this);
    }

    public Map<Integer, Integer> getBufferMap() {
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
