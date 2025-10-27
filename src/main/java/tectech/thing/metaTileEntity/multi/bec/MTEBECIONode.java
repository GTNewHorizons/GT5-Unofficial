package tectech.thing.metaTileEntity.multi.bec;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.WHITE;
import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTBECRecipe;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ReasonBEC;
import gregtech.api.util.shutdown.ShutDownReason;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.boseEinsteinCondensate.CondensateStack;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.hatch.MTEHatchBECIONodeController;
import tectech.thing.metaTileEntity.hatch.MTEHatchNaniteDetector;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECIONode extends MTEBECMultiblockBase<MTEBECIONode> implements IDataCopyable {

    private int assemblerX, assemblerY, assemblerZ;
    private @Nullable MTEBECAssembler assembler;

    private @Nullable NaniteTier[] requiredNanites;
    private @Nullable Object2LongOpenHashMap<Object> requiredCondensate, consumedCondensate;

    private @Nullable NaniteTier providedTier, requiredTier;
    private int availableNanites;
    private int subtickCounter, slowdowns, parallels;

    @Desugar
    record RecipeStep(NaniteTier nanite, int start, int end, int index) {}

    private List<RecipeStep> recipeSteps;

    private final List<MTEHatchNaniteDetector> naniteDetectors = new ArrayList<>();
    private final List<MTEHatchBECIONodeController> controllers = new ArrayList<>();

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
        structure.addCasing('B', ElectromagneticallyIsolatedCasing).withHatches(1, 16, Arrays.asList(Energy, ExoticEnergy, InputBus, OutputBus, NaniteHatch.INSTANCE, ControllerHatch.INSTANCE));
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', AdvancedFusionCoilII);
        structure.addCasing('E', ElectromagneticWaveguide);

        return structure.buildStructure(definition);
    }

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();

        naniteDetectors.clear();
        controllers.clear();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECIONode> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("Input bus, Output bus")
            .addInfo("Teleports stuff");

        tt.beginStructureBlock();
        tt.addAllCasingInfo();

        tt.toolTipFinisher(EnumChatFormatting.WHITE, 0, GTValues.AuthorPineapple);

        return tt;
    }

    @Override
    protected ITexture getCasingTexture() {
        return SuperconductivePlasmaEnergyConduit.getCasingTexture();
    }

    @Override
    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.BEC_IONODE_ACTIVE)
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
                setCurrentRecipe((GTBECRecipe) recipe);

                return super.onRecipeStart(recipe);
            }
        };
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);

        setCurrentRecipe(null);
    }

    @Override
    public void outputAfterRecipe_EM() {
        super.outputAfterRecipe_EM();

        setCurrentRecipe(null);
    }

    @Override
    protected void notAllowedToWork_stopMachine_EM() {

    }

    private @Nullable MTEBECAssembler getAssembler() {
        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte.isDead()) return null;

        if (!isServerSide()) return null;

        if (!(igte.getTileEntity(assemblerX, assemblerY, assemblerZ) instanceof IGregTechTileEntity other)) return null;

        if (!(other.getMetaTileEntity() instanceof MTEBECAssembler assembler2)) return null;

        return assembler2;
    }

    // @Override
    // public long getEUVar() {
    //     if (assembler == null) return 0;

    //     return assembler.getEUVar();
    // }

    // @Override
    // public void setEUVar(long aEnergy) {
    //     if (assembler == null) return;

    //     assembler.setEUVar(aEnergy);
    // }

    // @Override
    // public long maxEUStore() {
    //     return super.maxEUStore();
    // }

    // @Override
    // public long getMaxInputVoltage() {
    //     return super.getMaxInputVoltage();
    // }

    // @Override
    // public long getAverageInputVoltage() {
    //     return super.getAverageInputVoltage();
    // }

    // @Override
    // public long getMaxInputAmps() {
    //     return super.getMaxInputAmps();
    // }

    // @Override
    // public long getMaxInputEu() {
    //     return super.getMaxInputEu();
    // }

    // @Override
    // public boolean drainEnergyInput_EM(long EUtTierVoltage, long EUtEffective, long Amperes) {
    //     return super.drainEnergyInput_EM(EUtTierVoltage, EUtEffective, Amperes);
    // }

    // @Override
    // public boolean drainEnergyInput(long EUtEffective, long Amperes) {
    //     return super.drainEnergyInput(EUtEffective, Amperes);
    // }

    private static final CheckRecipeResult NO_ASSEMBLER = SimpleCheckRecipeResult.ofFailure("no_bec_assembler");

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        if (assembler == null) {
            connect(getAssembler());
        }

        if (assembler == null) return NO_ASSEMBLER;

        return super.checkProcessing_EM();
    }

    private void setCurrentRecipe(@Nullable GTBECRecipe recipe) {
        requiredNanites = recipe == null ? null : recipe.mInputTiers;

        if (recipe == null) {
            requiredCondensate = null;
            recipeSteps = null;
            parallels = 0;
            setRequiredTier(null);
        } else {
            requiredCondensate = new Object2LongOpenHashMap<>();

            for (CondensateStack stack : recipe.mCInput) {
                requiredCondensate.put(stack.material, stack.amount);
            }

            loadRequiredNanites(recipe.mDuration, Arrays.asList(recipe.mInputTiers));

            parallels = processingLogic.getCurrentParallels();
        }

        consumedCondensate = new Object2LongOpenHashMap<>();
    }

    public void setNaniteShare(NaniteTier providedTier, int nanites) {
        this.providedTier = providedTier;
        availableNanites = nanites;
    }

    private void setRequiredTier(NaniteTier tier) {
        requiredTier = tier;

        Iterator<MTEHatchNaniteDetector> iter = naniteDetectors.iterator();

        while (iter.hasNext()) {
            MTEHatchNaniteDetector naniteDetector = iter.next();

            if (naniteDetector == null || !naniteDetector.isValid()) {
                iter.remove();
                continue;
            }

            naniteDetector.setRequiredTier(requiredTier);
        }
    }

    private void loadRequiredNanites(int duration, List<NaniteTier> tiers) {
        IntDivisionIterator iter = new IntDivisionIterator(duration, tiers.size());
        Iterator<NaniteTier> nanites = tiers.iterator();

        recipeSteps = new ArrayList<>();

        int current = 0;
        int index = 0;

        for (Pair<Integer, NaniteTier> step : oneshot(zip(iter, nanites))) {
            recipeSteps.add(new RecipeStep(step.right(), current, current + step.left(), index++));

            current += step.left();
        }

        setRequiredTier(recipeSteps.get(0).nanite);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAmperageOC(false);
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1);
        logic.setMaxParallel(Math.max(1, availableNanites));
    }

    private RecipeStep getCurrentStep() {
        if (recipeSteps == null) return null;

        for (RecipeStep step : recipeSteps) {
            if (step.start <= mProgresstime && mProgresstime < step.end) return step;
        }

        return null;
    }

    private RecipeStep getNextProgressGate() {
        RecipeStep step = getCurrentStep();

        if (step == null || providedTier == null) return null;

        int index = step.index;

        while (recipeSteps.get(index).nanite.tier <= providedTier.tier) {
            index++;

            if (index >= recipeSteps.size()) return null;
        }

        return recipeSteps.get(index);
    }

    @Override
    protected void incrementProgressTime() {
        RecipeStep step = getCurrentStep();

        if (step == null) {
            throw new IllegalStateException("current step was null");
        }

        this.requiredTier = step.nanite;
        setRequiredTier(requiredTier);

        // sanity check, this should never happen
        if (requiredTier == null || assembler == null) return;
        if (requiredCondensate == null || consumedCondensate == null) return;

        // if the provided tier is insufficient, do nothing
        if (providedTier == null || providedTier.tier < requiredTier.tier) return;

        // if any controllers want the multi to pause immediately, then pause
        for (var controller : controllers) {
            if (controller.getMode() == MTEHatchBECIONodeController.Mode.PAUSE_INSTANT) {
                if (controller.receivingSignal()) {
                    return;
                }
            }
        }

        int nextProgress = mProgresstime + availableNanites;

        RecipeStep nextGate = getNextProgressGate();

        // if one of the succeeding steps cannot run with the current nanite tier, the multi cannot proceed past it
        if (nextGate != null) {
            nextProgress = Math.min(nextProgress, nextGate.start);
        }

        boolean shouldPauseStep = false;

        for (var controller : controllers) {
            if (controller.getMode() == MTEHatchBECIONodeController.Mode.PAUSE_STEP) {
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
            if (mProgresstime == step.start) return;

            RecipeStep nextStep = GTDataUtils.getIndexSafe(recipeSteps, step.index + 1);

            // nextStep == null when we're in the last step
            if (nextStep != null) {
                nextProgress = Math.min(nextProgress, nextStep.start);
            }
        }

        for (var required : requiredCondensate.object2LongEntrySet()) {
            long remainingCondensate = required.getLongValue() - consumedCondensate.getLong(required.getKey());

            if (remainingCondensate == 0) continue;

            long initial = required.getLongValue();

            CondensateStack toConsume = new CondensateStack(
                required.getKey(),
                initial);

            assembler.drainCondensate(toConsume);

            long consumed = initial - toConsume.amount;

            consumedCondensate.addTo(required.getKey(), consumed);
        }

        this.slowdowns = assembler.getSlowdowns(requiredCondensate.keySet());

        int divisor = parallels + slowdowns;

        subtickCounter += nextProgress - mProgresstime;

        int fullTicks = subtickCounter / divisor;

        subtickCounter -= fullTicks * divisor;
        mProgresstime += fullTicks;

        if (mProgresstime >= mMaxProgresstime) {
            for (var required : requiredCondensate.object2LongEntrySet()) {
                long remainingCondensate = required.getLongValue() - consumedCondensate.getLong(required.getKey());

                if (remainingCondensate > 0) {
                    stopMachine(ReasonBEC.noCondensate(new CondensateStack(required.getKey(), remainingCondensate)));
                    return;
                }
            }
        }
    }

    private void connect(MTEBECAssembler assembler) {
        if (!isServerSide()) return;

        disconnect();

        if (assembler != null) {
            this.assembler = assembler;
            assembler.addIONode(this);
        }
    }

    public void disconnect() {
        if (!isServerSide()) return;

        if (assembler != null) {
            assembler.removeIONode(this);
            assembler = null;
        }

        setNaniteShare(null, 0);
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity igte) {
        super.onFirstTick_EM(igte);

        if (isServerSide()) {
            connect(getAssembler());
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();

        if (isServerSide()) {
            disconnect();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity igte, long aTick) {
        super.onPostTick(igte, aTick);

        if (isServerSide()) {
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
            if (isServerSide()) {
                player.addChatMessage(
                    new ChatComponentText(
                        "Successfully connected to " + CustomItemList.Machine_Multi_BECAssembler.getDisplayName()));
            }

            return true;
        } else {
            if (isServerSide()) {
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
        if (parallels == 0) return 0;

        float slowdownMult = slowdowns == 0 ? 1f : 1f / slowdowns;

        return availableNanites / (float) parallels * slowdownMult;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements.widget(
            new FakeSyncWidget.IntegerSyncer(
                () -> saveNanite(providedTier),
                matId -> providedTier = loadNanite(matId)));

        screenElements
            .widget(new FakeSyncWidget.IntegerSyncer(() -> availableNanites, amount -> availableNanites = amount));

        screenElements.widget(
            new FakeSyncWidget.IntegerSyncer(
                () -> saveNanite(requiredTier),
                matId -> requiredTier = loadNanite(matId)));

        screenElements.widget(TextWidget.dynamicString(() -> {
            StringBuffer ret = new StringBuffer();

            ret.append(translate("GT5U.gui.text.provided", providedTier == null ? translate("GT5U.gui.text.nil") : translate("GT5U.gui.text.nanite_desc", availableNanites, providedTier.describe())));
            ret.append("\n");

            ret.append(translate("GT5U.gui.text.required", requiredTier == null ? translate("GT5U.gui.text.nil") : (GOLD + requiredTier.describe() + WHITE)));
            ret.append("\n");

        ret.append(translate("GT5U.gui.text.required_condensate"));
            ret.append("\n");

            boolean hasAny = false;

            if (requiredCondensate != null && consumedCondensate != null && mMaxProgresstime > 0) {
                for (var e : requiredCondensate.object2LongEntrySet()) {
                    hasAny = true;

                    long consumed = consumedCondensate.getLong(e.getKey());

                    String matName = new CondensateStack(e.getKey(), 0).getDisplayName();

                    ret.append(translate("GT5U.gui.text.remaining_condensate", matName, consumed, e.getLongValue()));
                }
            }

            if (!hasAny) {
                ret.append("None");
            }

            return ret.toString();
        }).setSynced(true).setTextAlignment(Alignment.CenterLeft).setDefaultColor(EnumChatFormatting.WHITE));
    }

    @Override
    protected String generateCurrentProgress() {
        StringBuffer ret = new StringBuffer(StatCollector.translateToLocal("GT5U.gui.text.progress"));
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

    protected boolean showRecipeOutputTooltips() {
        return false;
    }

    @Override
    protected String appendRate(boolean isLiquid, Long amount, boolean isFormatShortened) {
        return "";
    }

    protected void generateRate(StringBuffer ret, double amount) {
        // do nothing, rates can't be calculated easily
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

        tag.setInteger("x", assemblerX);
        tag.setInteger("y", assemblerY);
        tag.setInteger("z", assemblerZ);
        tag.setBoolean("con", assembler != null);
        tag.setInteger("nanite", saveNanite(requiredTier));
        tag.setInteger("provided", saveNanite(providedTier));
        tag.setFloat("speed", getProcessingSpeed());
        tag.setInteger("slowdowns", slowdowns);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        currenttip.add(
            MessageFormat
                .format("Assembler: {0},{1},{2}", tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z")));
        currenttip.add(MessageFormat.format("Connected: {0}", tag.getBoolean("con")));

        NaniteTier required = loadNanite(tag.getInteger("nanite"));
        NaniteTier provided = loadNanite(tag.getInteger("provided"));

        currenttip.add(MessageFormat.format("Required Tier: {0}", required == null ? "None" : required.describe()));
        currenttip.add(MessageFormat.format("Provided Tier: {0}", provided == null ? "None" : provided.describe()));
        currenttip.add(MessageFormat.format("Processing Speed: x{0}", tag.getFloat("speed")));
        currenttip.add(MessageFormat.format("Slowdowns: {0}", tag.getInteger("slowdowns") > 0 ? tag.getInteger("slowdowns") - 1 : 0));
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("assemblerX", assemblerX);
        aNBT.setInteger("assemblerY", assemblerY);
        aNBT.setInteger("assemblerZ", assemblerZ);

        if (requiredNanites != null) {
            aNBT.setInteger("naniteCount", requiredNanites.length);

            for (int i = 0; i < requiredNanites.length; i++) {
                aNBT.setInteger("nanite" + i, saveNanite(requiredNanites[i]));
            }
        }

        if (requiredCondensate != null) {
            aNBT.setTag("required", CondensateStack.save(requiredCondensate));
        }

        if (consumedCondensate != null) {
            aNBT.setTag("consumed", CondensateStack.save(consumedCondensate));
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

        aNBT.setInteger("parallels", parallels);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        assemblerX = aNBT.getInteger("assemblerX");
        assemblerY = aNBT.getInteger("assemblerY");
        assemblerZ = aNBT.getInteger("assemblerZ");

        int count = aNBT.getInteger("naniteCount");

        requiredNanites = new NaniteTier[count];

        for (int i = 0; i < count; i++) {
            requiredNanites[i] = loadNanite(aNBT.getInteger("nanite" + i));
        }

        requiredCondensate = CondensateStack.loadMap(aNBT.getTagList("required", Constants.NBT.TAG_COMPOUND));
        consumedCondensate = CondensateStack.loadMap(aNBT.getTagList("consumed", Constants.NBT.TAG_COMPOUND));

        if (aNBT.hasKey("steps")) {
            NBTTagList steps = aNBT.getTagList("steps", Constants.NBT.TAG_COMPOUND);

            recipeSteps = new ArrayList<>();

            List<NaniteTier> tiers = new ArrayList<>();

            //noinspection unchecked
            for (NBTTagCompound tag : (List<NBTTagCompound>) steps.tagList) {
                tiers.add(loadNanite(tag.getInteger("nanite")));
            }

            loadRequiredNanites(mMaxProgresstime, tiers);
        } else {
            recipeSteps = null;
        }

        parallels = aNBT.getInteger("parallels");
    }

    /**
     * A deterministic division algorithm that splits an int up into properly rounded chunks, such that the sum of each
     * chunk equals the original number.
     * Rounding will be performed as equally as possible.
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
            return Collections.singletonList(MTEHatchBECIONodeController.class);
        }

        @Override
        public String getDisplayName() {
            return CustomItemList.Hatch_BEC_IOController.getDisplayName();
        }

        @Override
        public long count(MTEBECIONode self) {
            return self.controllers.size();
        }

        @Override
        public IGTHatchAdder<MTEBECIONode> adder() {
            return (self, igtme, id) -> {
                IMetaTileEntity imte = igtme.getMetaTileEntity();

                if (imte instanceof MTEHatchBECIONodeController hatch) {
                    hatch.updateTexture(id);
                    hatch.updateCraftingIcon(self.getMachineCraftingIcon());

                    self.controllers.add(hatch);

                    return true;
                } else {
                    return false;
                }
            };
        }
    }
}
