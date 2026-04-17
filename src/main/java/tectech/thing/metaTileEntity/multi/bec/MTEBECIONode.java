package tectech.thing.metaTileEntity.multi.bec;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.WHITE;
import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTDataUtils.oneshot;
import static gregtech.api.util.GTDataUtils.zip;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import appeng.api.storage.data.IAEFluidStack;
import appeng.util.item.AEFluidStack;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.OCMethod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ReasonMissingCondensate;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import gregtech.common.gui.modularui.adapter.CondensateListAdapter;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import gregtech.common.modularui2.sync.NaniteTierSyncValue;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntIterator;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.boseEinsteinCondensate.CondensateList;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchIONodeController;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchIONodeController.Mode;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchNaniteDetector;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECIONode extends MTEBECMultiblockBase<MTEBECIONode> implements IDataCopyable {

    private int assemblerX, assemblerY, assemblerZ;
    private @Nullable MTEBECAssembler assembler;

    private @Nullable NaniteTier[] requiredNanites;
    private @Nullable CondensateList requiredCondensate, consumedCondensate;
    private List<RecipeStep> recipeSteps;

    private @Nullable NaniteTier providedTier, requiredTier;
    private int availableNanites;
    private int subtickCounter, slowdowns, parallelRecipesInProgress;
    private long assemblerEUt;
    private boolean powered;
    private NodeState state = NodeState.Idle;

    private int minParallel = 1, manualSlowdown = 0;

    private final List<MTEHatchNaniteDetector> naniteDetectors = new ArrayList<>();
    private final List<MTEHatchIONodeController> controllerHatches = new ArrayList<>();

    private enum NodeState {
        Idle,
        Unpowered,
        AssemblerOffline,
        NaniteTierTooLow,
        PausedStep,
        PausedImmediate,
        Crafting
    }

    @Desugar
    public record RecipeStep(NaniteTier nanite, int start, int end, int index) {

    }

    public MTEBECIONode(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEBECIONode(MTEBECIONode prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBECIONode(this);
    }

    @Override
    public String[][] getDefinition() {
        return BECStructureDefinitions.BEC_IO_NODE;
    }

    @Override
    public IStructureDefinition<MTEBECIONode> compile(String[][] definition) {
        structure.addCasing('A', SuperconductivePlasmaEnergyConduit);
        structure.addCasing('B', ElectromagneticallyIsolatedCasing)
            .withHatches(1, 64, Arrays.asList(InputBus, OutputBus, NaniteHatch.INSTANCE, ControllerHatch.INSTANCE));
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', AdvancedFusionCoilII);
        structure.addCasing('E', ElectromagneticWaveguide);

        return structure.buildStructure(definition);
    }

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();

        naniteDetectors.clear();
        controllerHatches.clear();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECIONode> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC I/O Node, Input bus, Output bus")
            .addMarkdown(new ResourceLocation("gregtech", "bec-ionode"));

        tt.beginStructureBlock();
        tt.addAllCasingInfo();

        tt.toolTipFinisher(GTAuthors.AuthorPineapple);

        return tt;
    }

    @Override
    protected ITexture getCasingTexture() {
        return SuperconductivePlasmaEnergyConduit.getCasingTexture();
    }

    @Override
    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(BlockIcons.BEC_IONODE_ACTIVE)
            .extFacing()
            .glow()
            .build();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.condensateAssemblingRecipes;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                setCurrentRecipe(recipe);

                return super.onRecipeStart(recipe);
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setNoOverclock(true);
            }

            private static final CheckRecipeResult NOT_ENOUGH_PARALLELS = SimpleCheckRecipeResult
                .ofFailure("not_enough_parallels");

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                double parallels = recipe
                    .maxParallelCalculatedByInputs(MTEBECIONode.this.maxParallel, this.inputFluids, this.inputItems);

                if (parallels >= minParallel) {
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                } else {
                    return NOT_ENOUGH_PARALLELS;
                }
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAmperageOC(false);
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(assembler == null ? 0 : assembler.getMaxInputVoltage()));
        logic.setAvailableAmperage(1);
        logic.setMaxParallel(this.maxParallel);
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);

        clearCurrentRecipe();
    }

    @Override
    public void outputAfterRecipe_EM() {
        super.outputAfterRecipe_EM();

        clearCurrentRecipe();
    }

    @Override
    protected void notAllowedToWork_stopMachine_EM() {

    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    private @Nullable MTEBECAssembler getAssembler() {
        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte == null || igte.isDead()) return null;

        if (!GTUtility.isServer()) return null;

        if (!(igte.getTileEntity(assemblerX, assemblerY, assemblerZ) instanceof IGregTechTileEntity other)) return null;

        if (!(other.getMetaTileEntity() instanceof MTEBECAssembler assembler2)) return null;

        return assembler2;
    }

    private static final CheckRecipeResult NO_ASSEMBLER = SimpleCheckRecipeResult.ofFailure("no_bec_assembler");

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        if (assembler == null) {
            connect(getAssembler());
        }

        if (assembler == null) return NO_ASSEMBLER;

        return super.checkProcessing_EM();
    }

    @Override
    protected void setEnergyUsage(ProcessingLogic processingLogic) {
        super.setEnergyUsage(processingLogic);

        // Defer the EU/t consumption to the assembler. We allow the ProcessingLogic to set this multi's EU/t, then
        // reset it and pass the number along to the assembler.
        assemblerEUt = -lEUt;
        lEUt = 0;
    }

    private void setCurrentRecipe(@Nullable GTRecipe recipe) {
        this.requiredCondensate = new CondensateList();
        this.consumedCondensate = new CondensateList();

        this.parallelRecipesInProgress = processingLogic.getCurrentParallels();

        // noinspection DataFlowIssue
        for (FluidStack stack : recipe.getMetadata(GTRecipeConstants.CONDENSATE_INPUT)) {
            this.requiredCondensate.addTo(stack.getFluid(), stack.amount * (long) this.parallelRecipesInProgress);
        }

        this.requiredNanites = recipe.getMetadata(GTRecipeConstants.NANITE_TIERS);
        assert this.requiredNanites != null;
        loadRequiredNanites(recipe.mDuration, Arrays.asList(this.requiredNanites));

        state = NodeState.Crafting;
    }

    private void clearCurrentRecipe() {
        requiredCondensate = null;
        requiredNanites = null;
        recipeSteps = null;
        parallelRecipesInProgress = 0;
        assemblerEUt = 0;
        state = NodeState.Idle;
        setRequiredTier(null);
    }

    @Nonnegative
    public long getAssemblerEUt() {
        return this.assemblerEUt;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public void setNaniteShare(NaniteTier providedTier, int nanites) {
        this.providedTier = providedTier;
        this.availableNanites = nanites;
    }

    private void setRequiredTier(NaniteTier tier) {
        this.requiredTier = tier;

        Iterator<MTEHatchNaniteDetector> iter = this.naniteDetectors.iterator();

        while (iter.hasNext()) {
            MTEHatchNaniteDetector naniteDetector = iter.next();

            if (naniteDetector == null || !naniteDetector.isValid()) {
                iter.remove();
                continue;
            }

            naniteDetector.setRequiredTier(this.requiredTier);
        }
    }

    /// Divides the duration into roughly equal chunks so that we can easily determine which step we're in, given a
    /// specific tick (see [#getCurrentStep()])
    private void loadRequiredNanites(int duration, List<NaniteTier> tiers) {
        IntDivisionIterator iter = new IntDivisionIterator(duration, tiers.size());
        Iterator<NaniteTier> nanites = tiers.iterator();

        this.recipeSteps = new ArrayList<>();

        int current = 0;
        int index = 0;

        for (Pair<Integer, NaniteTier> step : oneshot(zip(iter, nanites))) {
            this.recipeSteps.add(new RecipeStep(step.right(), current, current + step.left(), index++));

            current += step.left();
        }

        setRequiredTier(this.recipeSteps.get(0).nanite);
    }

    private RecipeStep getCurrentStep() {
        if (this.recipeSteps == null) return null;

        for (RecipeStep step : this.recipeSteps) {
            if (step.start <= this.mProgresstime && this.mProgresstime < step.end) return step;
        }

        return null;
    }

    private RecipeStep getNextProgressGate() {
        RecipeStep step = getCurrentStep();

        if (step == null || this.providedTier == null) return null;

        int index = step.index;

        while (this.recipeSteps.get(index).nanite.tier <= this.providedTier.tier) {
            index++;

            if (index >= this.recipeSteps.size()) return null;
        }

        return this.recipeSteps.get(index);
    }

    private static final ShutDownReason CLOGGED = SimpleShutDownReason.ofCritical("bec_clogged");

    @Override
    protected void incrementProgressTime() {
        // Assembler can't deliver enough power; stall crafting
        if (!this.powered) {
            state = NodeState.Unpowered;
            return;
        }

        RecipeStep step = getCurrentStep();

        if (step == null) {
            throw new IllegalStateException("current step was null");
        }

        this.requiredTier = step.nanite;
        setRequiredTier(this.requiredTier);

        // sanity check, this should never happen
        if (this.requiredTier == null) return;
        if (this.assembler == null) return;
        if (this.requiredCondensate == null) return;
        if (this.consumedCondensate == null) return;

        // if the provided tier is insufficient, do nothing
        if (this.providedTier == null || this.providedTier.tier < this.requiredTier.tier) {
            state = NodeState.NaniteTierTooLow;
            return;
        }

        if (this.assembler.mMaxProgresstime <= 0) {
            state = NodeState.AssemblerOffline;
            return;
        }

        // if any controllers want the multi to pause immediately, then pause
        for (var controller : this.controllerHatches) {
            if (controller.getMode() == Mode.PAUSE_INSTANT) {
                if (controller.receivingSignal()) {
                    state = NodeState.PausedImmediate;
                    return;
                }
            }
        }

        this.slowdowns = assembler.getSlowdowns(requiredCondensate.keySet());

        if (this.slowdowns > 3) {
            this.stopMachine(CLOGGED);
            return;
        }

        int divisor = this.parallelRecipesInProgress * (1 + Math.max(this.slowdowns, this.manualSlowdown));

        this.subtickCounter += availableNanites;

        int fullTicks = this.subtickCounter / divisor;

        this.subtickCounter -= fullTicks * divisor;

        // If we don't use nextProgress we're wasting the ticks acrued from subtickCounter, but there's no good way to
        // prevent that. It just means the process will be slower by a tick or two if it gets paused.
        int nextProgress = this.mProgresstime + fullTicks;

        RecipeStep nextGate = getNextProgressGate();

        // if one of the succeeding steps cannot run with the current nanite tier, the multi cannot proceed past it
        if (nextGate != null) {
            if (nextProgress == nextGate.start) {
                state = NodeState.NaniteTierTooLow;
            }

            nextProgress = Math.min(nextProgress, nextGate.start);
        }

        boolean shouldPauseStep = false;

        for (var controller : this.controllerHatches) {
            if (controller.getMode() == Mode.PAUSE_STEP) {
                if (controller.receivingSignal()) {
                    shouldPauseStep = true;
                    break;
                }
            }
        }

        // Check if the multi should pause on the next step.
        // This means that the multi should either:
        // 1. jump to the next step's t=0 time, or
        // 2. do nothing if we're already at a t=0 time
        if (shouldPauseStep) {
            if (this.mProgresstime == step.start) {
                state = NodeState.PausedStep;
                return;
            }

            RecipeStep nextStep = GTDataUtils.getIndexSafe(recipeSteps, step.index + 1);

            // nextStep == null when we're in the last step
            if (nextStep != null) {
                nextProgress = Math.min(nextProgress, nextStep.start);
            }
        }

        for (var required : this.requiredCondensate.object2LongEntrySet()) {
            long remainingCondensate = required.getLongValue() - this.consumedCondensate.getLong(required.getKey());

            if (remainingCondensate == 0) continue;

            long initial = required.getLongValue();

            IAEFluidStack toConsume = AEFluidStack.create(new FluidStack(required.getKey(), 1))
                .setStackSize(initial);

            this.assembler.drainCondensate(toConsume);

            long consumed = initial - toConsume.getStackSize();

            this.consumedCondensate.addTo(required.getKey(), consumed);
        }

        this.mProgresstime = nextProgress;
        this.state = NodeState.Crafting;

        if (this.mProgresstime >= this.mMaxProgresstime) {
            // Recipe has finished, check if there's any missing condensate and fail if so
            for (var required : this.requiredCondensate.object2LongEntrySet()) {
                long remainingCondensate = required.getLongValue() - this.consumedCondensate.getLong(required.getKey());

                if (remainingCondensate > 0) {
                    stopMachine(
                        new ReasonMissingCondensate(
                            AEFluidStack.create(new FluidStack(required.getKey(), 1))
                                .setStackSize(remainingCondensate)));
                    return;
                }
            }
        }
    }

    private void connect(MTEBECAssembler assembler) {
        if (!GTUtility.isServer()) return;

        disconnect();

        if (assembler != null) {
            this.assembler = assembler;
            assembler.addIONode(this);
        }
    }

    public void disconnect() {
        if (!GTUtility.isServer()) return;

        if (assembler != null) {
            assembler.removeIONode(this);
            assembler = null;
        }

        setNaniteShare(null, 0);
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity igte) {
        super.onFirstTick_EM(igte);

        if (GTUtility.isServer()) {
            connect(getAssembler());
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();

        if (GTUtility.isServer()) {
            disconnect();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity igte, long aTick) {
        super.onPostTick(igte, aTick);

        if (GTUtility.isServer()) {
            // periodically try to reconnect to the assembler if we're supposed to be running but the assembler isn't
            // loaded
            if (assembler == null && mMaxProgresstime > 0 && aTick % 200 == 0) {
                connect(getAssembler());
            }
        }
    }

    @Override
    public void onLeftclick(IGregTechTileEntity igte, EntityPlayer player) {
        if (!(player instanceof EntityPlayerMP)) return;

        ItemStack heldItem = player.getHeldItem();
        if (!ItemList.Tool_DataStick.isStackEqual(heldItem, false, true)) return;

        heldItem.setTagCompound(getCopiedData(player));
        heldItem.setStackDisplayName(
            MessageFormat.format(
                "{0} Link Data Stick ({1}, {2}, {3})",
                getStackForm(1).getDisplayName(),
                igte.getXCoord(),
                igte.getYCoord(),
                igte.getZCoord()));
        player.addChatMessage(new ChatComponentText("Saved Link Data to Data Stick"));
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity igte, EntityPlayer player) {
        ItemStack heldItem = player.getHeldItem();
        if (!ItemList.Tool_DataStick.isStackEqual(heldItem, false, true)) {
            return super.onRightclick(igte, player);
        }

        // intentionally run on the client so that the player's arm swings
        if (pasteCopiedData(player, heldItem.getTagCompound())) {
            if (GTUtility.isServer()) {
                player.addChatMessage(
                    new ChatComponentText(
                        "Successfully connected to " + CustomItemList.Machine_Multi_BECAssembler.getDisplayName()));
            }

            return true;
        } else {
            if (GTUtility.isServer()) {
                player.addChatMessage(
                    new ChatComponentText(
                        "Could not connect to " + CustomItemList.Machine_Multi_BECAssembler.getDisplayName()));
            }

            return false;
        }
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("type", getCopiedDataIdentifier(player));
        tag.setInteger("x", assemblerX);
        tag.setInteger("y", assemblerY);
        tag.setInteger("z", assemblerZ);

        return tag;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (!nbt.getString("type")
            .equals(getCopiedDataIdentifier(player))) return false;

        assemblerX = nbt.getInteger("x");
        assemblerY = nbt.getInteger("y");
        assemblerZ = nbt.getInteger("z");

        disconnect();
        connect(getAssembler());

        return true;
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return "bec-assembler";
    }

    private float getProcessingSpeed() {
        if (parallelRecipesInProgress == 0) return 0;

        return availableNanites / (float) parallelRecipesInProgress / (1f + slowdowns);
    }

    @OCMethod
    public @Nullable CondensateList getRequiredCondensate() {
        return requiredCondensate != null ? requiredCondensate.clone() : null;
    }

    @OCMethod
    public @Nullable CondensateList getConsumedCondensate() {
        return consumedCondensate != null ? consumedCondensate.clone() : null;
    }

    @OCMethod
    public @Nullable NaniteTier getProvidedTier() {
        return providedTier;
    }

    @OCMethod
    public @Nullable NaniteTier getRequiredTier() {
        return requiredTier;
    }

    @OCMethod
    public int getAvailableNanites() {
        return availableNanites;
    }

    @OCMethod
    public int getSlowdowns() {
        return slowdowns;
    }

    @OCMethod
    public int getParallelRecipesInProgress() {
        return parallelRecipesInProgress;
    }

    @OCMethod
    public int getMinParallel() {
        return minParallel;
    }

    @OCMethod
    public int getMaxParallel() {
        return maxParallel;
    }

    @OCMethod
    public int getManualSlowdown() {
        return manualSlowdown;
    }

    @OCMethod
    public List<RecipeStep> getRecipeSteps() {
        return new ArrayList<>(recipeSteps);
    }

    @OCMethod
    public String getState() {
        return switch (state) {
            case Idle -> "idle";
            case Unpowered -> "unpowered";
            case AssemblerOffline -> "assembler-offline";
            case NaniteTierTooLow -> "nanite-tier-too-low";
            case PausedStep -> "paused-step";
            case PausedImmediate -> "paused-immediate";
            case Crafting -> "crafting";
        };
    }

    @OCMethod
    public void setMinParallel(int minParallel) {
        this.minParallel = minParallel;
    }

    @OCMethod
    public void setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
    }

    @OCMethod
    public void setManualSlowdown(int manualSlowdown) {
        this.manualSlowdown = manualSlowdown;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new Gui();
    }

    private class Gui extends TTMultiblockBaseGui<MTEBECIONode> {

        public Gui() {
            super(MTEBECIONode.this);
        }

        @Override
        protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
            syncManager
                .syncValue("availableNanites", new IntSyncValue(() -> availableNanites, i -> availableNanites = i));
            syncManager.syncValue("state", new EnumSyncValue<>(NodeState.class, () -> state, s -> state = s));
            syncManager.syncValue("providedTier", new NaniteTierSyncValue(() -> providedTier, t -> providedTier = t));
            syncManager.syncValue("requiredTier", new NaniteTierSyncValue(() -> requiredTier, t -> requiredTier = t));

            syncManager.syncValue(
                "requiredCondensate",
                GenericSyncValue.builder(CondensateList.class)
                    .getter(() -> requiredCondensate)
                    .setter(c -> requiredCondensate = c)
                    .adapter(new CondensateListAdapter())
                    .build());

            syncManager.syncValue(
                "consumedCondensate",
                GenericSyncValue.builder(CondensateList.class)
                    .getter(() -> consumedCondensate)
                    .setter(c -> consumedCondensate = c)
                    .adapter(new CondensateListAdapter())
                    .build());

            TextWidget<?> contentsWidget = IKey.dynamic(() -> {
                StringBuilder ret = new StringBuilder();

                ret.append(
                    translate("GT5U.gui.text.ionode_status", translate("GT5U.gui.text.ionode_status." + state.name())));
                ret.append("\n");

                ret.append(
                    translate(
                        "GT5U.gui.text.provided_nanite",
                        providedTier == null ? translate("GT5U.gui.text.nil")
                            : translate("GT5U.gui.text.nanite_desc", availableNanites, providedTier.describe())));
                ret.append("\n");

                ret.append(
                    translate(
                        "GT5U.gui.text.required_nanite",
                        requiredTier == null ? translate("GT5U.gui.text.nil")
                            : GOLD + requiredTier.describe() + WHITE));
                ret.append("\n");

                boolean hasAny = false;

                ret.append(translate("GT5U.gui.text.required_condensate"));
                ret.append("\n");

                if (requiredCondensate != null && consumedCondensate != null && mMaxProgresstime > 0) {
                    for (var e : requiredCondensate.object2LongEntrySet()) {
                        hasAny = true;

                        long consumed = consumedCondensate.getLong(e.getKey());

                        ret.append(
                            translate(
                                "GT5U.gui.text.remaining_condensate",
                                CondensateType.getCondensateName(e.getKey()),
                                consumed,
                                e.getLongValue()));
                    }
                }

                if (!hasAny) {
                    ret.append(translate("GT5U.gui.text.nil"));
                }

                return ret.toString();
            })
                .asWidget()
                .widthRel(1);

            return super.createTerminalTextWidget(syncManager, parent).child(contentsWidget);
        }

        @Override
        protected boolean isParametrized() {
            return true;
        }

        @Override
        public boolean showMaxParallelRow() {
            // Handled by custom parallel system.
            // It's less confusing to put the existing max parallel option in the parameters window.
            return false;
        }

        @Override
        protected boolean showOutputRates() {
            return false;
        }

        @Override
        protected Widget<?> getParameterEditor(ModularPanel panel, PanelSyncManager syncManager) {
            return SettingsPanel.builder()
                .setDividerPosition(75)
                .addHeader(IKey.str("Parameters"))
                .addIntEditor(
                    IKey.str("Min Parallels"),
                    () -> minParallel,
                    f -> minParallel = f,
                    (panel2, sync, widget) -> {
                        widget.setNumbers(1, Integer.MAX_VALUE);
                        widget.tooltip(
                            t -> t.addStringLines(
                                MarkdownTooltipLoader.STANDARD.loadStandardPath(
                                    new ResourceLocation("gregtech", "bec-ionode/min-parallels"),
                                    Collections.emptyMap())));
                    })
                .addIntEditor(
                    IKey.str("Max Parallels"),
                    () -> maxParallel,
                    f -> maxParallel = f,
                    (panel2, sync, widget) -> {
                        widget.setNumbers(1, Integer.MAX_VALUE);
                        widget.tooltip(
                            t -> t.addStringLines(
                                MarkdownTooltipLoader.STANDARD.loadStandardPath(
                                    new ResourceLocation("gregtech", "bec-ionode/max-parallels"),
                                    Collections.emptyMap())));
                    })
                .addIntEditor(
                    IKey.str("Speed Divisor"),
                    () -> manualSlowdown,
                    i -> manualSlowdown = i,
                    (panel2, sync, widget) -> {
                        widget.setNumbers(0, Integer.MAX_VALUE);
                        widget.tooltip(
                            t -> t.addStringLines(
                                MarkdownTooltipLoader.STANDARD.loadStandardPath(
                                    new ResourceLocation("gregtech", "bec-ionode/speed-divisor"),
                                    Collections.emptyMap())));
                    })
                .build(panel, syncManager)
                .size(150, 90);
        }
    }

    @Override
    public boolean hasRunningText() {
        return false;
    }

    @Override
    public String generateCurrentRecipeInfoString() {
        StringBuffer ret = new StringBuffer(GTUtility.translate("GT5U.gui.text.progress"));
        ret.append(" ");
        ret.append(mProgresstime);
        ret.append(" / ");
        ret.append(mMaxProgresstime);
        ret.append(" (");

        numberFormat.setMinimumFractionDigits(1);
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.format((double) mProgresstime / mMaxProgresstime * 100, ret);
        ret.append("%");
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);

        ret.append(")");

        return ret.toString();
    }

    private static int saveNanite(NaniteTier tier) {
        return tier == null ? -1 : tier.getMaterial().mMetaItemSubID;
    }

    private static NaniteTier loadNanite(int id) {
        return NaniteTier.fromMaterial(GTDataUtils.getIndexSafe(GregTechAPI.sGeneratedMaterials, id));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setFloat("speed", getProcessingSpeed());
        tag.setInteger("slowdowns", slowdowns);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        currenttip.add(MessageFormat.format("Processing Speed: x{0}", tag.getFloat("speed")));
        currenttip.add(MessageFormat.format("Slowdowns: {0}", tag.getInteger("slowdowns")));
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("assemblerX", assemblerX);
        aNBT.setInteger("assemblerY", assemblerY);
        aNBT.setInteger("assemblerZ", assemblerZ);

        aNBT.setInteger("minParallels", minParallel);
        aNBT.setInteger("maxParallels", maxParallel);

        if (requiredNanites != null) {
            aNBT.setInteger("naniteCount", requiredNanites.length);

            for (int i = 0; i < requiredNanites.length; i++) {
                aNBT.setInteger("nanite" + i, saveNanite(requiredNanites[i]));
            }
        }

        if (requiredCondensate != null) {
            aNBT.setTag("required", requiredCondensate.saveToNBT());
        }

        if (consumedCondensate != null) {
            aNBT.setTag("consumed", consumedCondensate.saveToNBT());
        }

        if (recipeSteps != null) {
            NBTTagList steps = new NBTTagList();
            aNBT.setTag("steps", steps);

            for (RecipeStep step : recipeSteps) {
                NBTTagCompound tag = new NBTTagCompound();
                steps.appendTag(tag);

                tag.setInteger("nanite", saveNanite(step.nanite));
            }
        }

        aNBT.setInteger("parallels", parallelRecipesInProgress);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        assemblerX = aNBT.getInteger("assemblerX");
        assemblerY = aNBT.getInteger("assemblerY");
        assemblerZ = aNBT.getInteger("assemblerZ");

        minParallel = aNBT.getInteger("minParallels");
        maxParallel = aNBT.getInteger("maxParallels");

        int count = aNBT.getInteger("naniteCount");

        requiredNanites = new NaniteTier[count];

        for (int i = 0; i < count; i++) {
            requiredNanites[i] = loadNanite(aNBT.getInteger("nanite" + i));
        }

        if (aNBT.hasKey("required")) {
            requiredCondensate = new CondensateList();
            requiredCondensate.loadFromNBT(aNBT.getCompoundTag("required"));
        } else {
            requiredCondensate = null;
        }

        if (aNBT.hasKey("consumed")) {
            consumedCondensate = new CondensateList();
            consumedCondensate.loadFromNBT(aNBT.getCompoundTag("consumed"));
        } else {
            consumedCondensate = null;
        }

        if (aNBT.hasKey("steps")) {
            NBTTagList steps = aNBT.getTagList("steps", NBT.TAG_COMPOUND);

            recipeSteps = new ArrayList<>();

            List<NaniteTier> tiers = new ArrayList<>();

            // noinspection unchecked
            for (NBTTagCompound tag : (List<NBTTagCompound>) steps.tagList) {
                tiers.add(loadNanite(tag.getInteger("nanite")));
            }

            loadRequiredNanites(mMaxProgresstime, tiers);
        } else {
            recipeSteps = null;
        }

        parallelRecipesInProgress = aNBT.getInteger("parallels");
    }

    /**
     * A deterministic division algorithm that splits an int up into properly rounded chunks, such that the sum of each
     * chunk equals the original number. Rounding will be performed as equally as possible.
     */
    public static class IntDivisionIterator implements IntIterator {

        public int remaining, divisor, counter, sum;

        public IntDivisionIterator(int total, int divisor) {
            this.remaining = total;
            this.divisor = divisor;
            this.counter = 0;
            this.sum = 0;
        }

        @Override
        public int nextInt() {
            if (remaining == 0) return 0;

            int value = peek();

            remaining -= value;
            sum += value;
            counter++;

            return value;
        }

        public int peek() {
            int bucketsLeft = divisor - counter;

            if (bucketsLeft == 0) return 0;

            if (bucketsLeft == 1) {
                return remaining;
            } else {
                return Math.round(remaining / (float) (divisor - counter));
            }
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }
    }

    public enum NaniteHatch implements IHatchElement<MTEBECIONode> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchNaniteDetector.class);
        }

        @Override
        public String getDisplayName() {
            return CustomItemList.Hatch_BEC_Nanites.getDisplayName();
        }

        @Override
        public long count(MTEBECIONode self) {
            return self.naniteDetectors.size();
        }

        @Override
        public IGTHatchAdder<MTEBECIONode> adder() {
            return (self, igtme, id) -> {
                IMetaTileEntity imte = igtme.getMetaTileEntity();

                if (imte instanceof MTEHatchNaniteDetector hatch) {
                    hatch.updateTexture(id);
                    hatch.updateCraftingIcon(self.getMachineCraftingIcon());

                    self.naniteDetectors.add(hatch);
                    hatch.setRequiredTier(self.requiredTier);

                    return true;
                } else {
                    return false;
                }
            };
        }
    }

    public enum ControllerHatch implements IHatchElement<MTEBECIONode> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchIONodeController.class);
        }

        @Override
        public String getDisplayName() {
            return CustomItemList.Hatch_BEC_IOController.getDisplayName();
        }

        @Override
        public long count(MTEBECIONode self) {
            return self.controllerHatches.size();
        }

        @Override
        public IGTHatchAdder<MTEBECIONode> adder() {
            return (self, igtme, id) -> {
                IMetaTileEntity imte = igtme.getMetaTileEntity();

                if (imte instanceof MTEHatchIONodeController hatch) {
                    hatch.updateTexture(id);
                    hatch.updateCraftingIcon(self.getMachineCraftingIcon());

                    self.controllerHatches.add(hatch);

                    return true;
                } else {
                    return false;
                }
            };
        }
    }
}
