package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.GTValues.debugDriller;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_DRILL;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_DRILL_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_DRILL_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_DRILL_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.common.UndergroundOil.undergroundOil;
import static gregtech.common.UndergroundOil.undergroundOilReadInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnegative;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.widgets.LockedWhileActiveButton;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetricsExporter;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ValidationResult;
import gregtech.api.util.ValidationType;
import gregtech.common.misc.IWorkAreaProvider;
import gregtech.common.misc.WorkAreaChunk;

public abstract class MTEOilDrillBase extends MTEDrillerBase implements IMetricsExporter, IWorkAreaProvider {

    protected int batchMultiplier = 1;
    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    private static final String NBT_SHOW_WORK_AREA = "showWorkArea";
    private static final String NBT_CHUNK_RANGE_CONFIG = "chunkRangeConfig";
    private static final String NBT_ACTIVE_OIL_FIELD_CHUNKS = "activeOilFieldChunks";

    private final ArrayList<ChunkCoordIntPair> mOilFieldChunks = new ArrayList<>();
    private final Set<Long> activeOilFieldChunkKeys = new HashSet<>();
    private Fluid mOil = null;
    private int mOilFlow = 0;
    private @NotNull String clientFluidType = "";
    private int clientFlowPerTick = 0;
    private int clientFlowPerOperation = 0;

    private int chunkRangeConfig = getRangeInChunks();
    private boolean showWorkArea = false;

    private @Nullable WorkAreaBounds cachedBounds = null;
    private int cachedBoundsXDrill = Integer.MIN_VALUE;
    private int cachedBoundsZDrill = Integer.MIN_VALUE;
    private int cachedBoundsRange = Integer.MIN_VALUE;

    private @Nullable WorkAreaBounds cachedWorkAreaBounds = null;
    private List<WorkAreaChunk> cachedWorkAreaChunks = Collections.emptyList();

    @Desugar
    private record WorkAreaBounds(int minChunkX, int minChunkZ, int maxChunkX, int maxChunkZ) {}

    public MTEOilDrillBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEOilDrillBase(String aName) {
        super(aName);
    }

    /********************************************************
     * Parent overrides
     *******************************************************/
    @Override
    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mOutputHatches.isEmpty() && mEnergyHatches.size() == 1;
    }

    @Override
    protected void setElectricityStats() {
        // for a 6.4 second beautiful batch
        batchMultiplier = (batchMode && reachingVoidOrBedrock()) ? 128 : 1;
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(0, GTUtility.getTier(getMaxInputVoltage()));
        this.mEUt = -7 << (tier << 1); // (1/4) A of current tier when at bottom (7/8) A of current tier while mining
        this.mMaxProgresstime = calculateMaxProgressTime(tier);
    }

    @Override
    public int calculateMaxProgressTime(int tier, boolean simulateWorking) {
        return (int) Math.max(
            1,
            (workState == WorkState.AT_BOTTOM || simulateWorking
                ? (64 * (chunkRangeConfig * chunkRangeConfig)) >> (getMinTier() - 1)
                : 120) / GTUtility.powInt(2, tier))
            * batchMultiplier;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { getCasingTextureForId(casingTextureIndex), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_DRILL_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_DRILL_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { getCasingTextureForId(casingTextureIndex), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_DRILL)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_DRILL_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { getCasingTextureForId(casingTextureIndex) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger(NBT_CHUNK_RANGE_CONFIG, chunkRangeConfig);
        aNBT.setBoolean(NBT_SHOW_WORK_AREA, showWorkArea);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        if (aNBT.hasKey(NBT_CHUNK_RANGE_CONFIG)) {
            chunkRangeConfig = aNBT.getInteger(NBT_CHUNK_RANGE_CONFIG);
        }

        if (aNBT.hasKey(NBT_SHOW_WORK_AREA)) {
            showWorkArea = aNBT.getBoolean(NBT_SHOW_WORK_AREA);
        }
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = new NBTTagCompound();

        data.setInteger(NBT_CHUNK_RANGE_CONFIG, chunkRangeConfig);
        data.setBoolean(NBT_SHOW_WORK_AREA, showWorkArea);

        int[] activeChunks = new int[activeOilFieldChunkKeys.size() * 2];
        int index = 0;

        for (long chunkKey : activeOilFieldChunkKeys) {
            activeChunks[index++] = (int) (chunkKey >> 32); // chunkX
            activeChunks[index++] = (int) chunkKey; // chunkZ
        }

        data.setIntArray(NBT_ACTIVE_OIL_FIELD_CHUNKS, activeChunks);

        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        if (data == null) {
            return;
        }

        if (data.hasKey(NBT_CHUNK_RANGE_CONFIG)) {
            chunkRangeConfig = data.getInteger(NBT_CHUNK_RANGE_CONFIG);
            invalidateWorkAreaCache();
        }

        if (data.hasKey(NBT_SHOW_WORK_AREA)) {
            showWorkArea = data.getBoolean(NBT_SHOW_WORK_AREA);
        }

        if (data.hasKey(NBT_ACTIVE_OIL_FIELD_CHUNKS)) {
            activeOilFieldChunkKeys.clear();

            int[] activeChunks = data.getIntArray(NBT_ACTIVE_OIL_FIELD_CHUNKS);

            for (int i = 0; i + 1 < activeChunks.length; i += 2) {
                int chunkX = activeChunks[i];
                int chunkZ = activeChunks[i + 1];

                activeOilFieldChunkKeys.add(packChunkKey(chunkX, chunkZ));
            }
        }
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }

        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> EnumChatFormatting.GRAY
                            + StatCollector.translateToLocalFormatted("GT5U.gui.text.pump_fluid_type", clientFluidType))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> base.isActive() && workState == WorkState.AT_BOTTOM))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> EnumChatFormatting.GRAY
                            + StatCollector.translateToLocalFormatted(
                                "GT5U.gui.text.pump_rate.1",
                                EnumChatFormatting.AQUA + numberFormat.format(clientFlowPerTick))
                            + EnumChatFormatting.GRAY
                            + StatCollector.translateToLocal("GT5U.gui.text.pump_rate.2"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> base.isActive() && workState == WorkState.AT_BOTTOM))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> EnumChatFormatting.GRAY
                            + StatCollector.translateToLocalFormatted(
                                "GT5U.gui.text.pump_recovery.1",
                                EnumChatFormatting.AQUA + numberFormat.format(clientFlowPerOperation))
                            + EnumChatFormatting.GRAY
                            + StatCollector.translateToLocal("GT5U.gui.text.pump_recovery.2"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> base.isActive() && workState == WorkState.AT_BOTTOM))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> workState.ordinal(), this::setWorkState))
            .widget(new FakeSyncWidget.StringSyncer(this::getFluidName, newString -> clientFluidType = newString))
            .widget(new FakeSyncWidget.IntegerSyncer(this::getFlowRatePerTick, newInt -> clientFlowPerTick = newInt))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> mOilFlow, newInt -> clientFlowPerOperation = newInt));
    }

    @Override
    protected List<ButtonWidget> getAdditionalButtons(ModularWindow.Builder builder, UIBuildContext buildContext) {
        return ImmutableList.of(createChunkRangeButton(builder), createWorkAreaToggleButton(builder));
    }

    @Override
    protected List<IHatchElement<? super MTEDrillerBase>> getAllowedHatches() {
        return ImmutableList.of(InputBus, OutputHatch, Maintenance, Energy);
    }

    @Override
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        switch (tryLowerPipeState(true)) {
            case SUCCESS -> {
                workState = WorkState.DOWNWARD;
                setElectricityStats();
                return true;
            }
            case CANCELED -> {
                workState = WorkState.UPWARD;
                return true;
            }
        }

        if (reachingVoidOrBedrock() && tryFillChunkList()) {
            if (mWorkChunkNeedsReload) {
                mCurrentChunk = new ChunkCoordIntPair(xDrill >> 4, zDrill >> 4);
                GTChunkManager.requestChunkLoad((TileEntity) getBaseMetaTileEntity(), null);
                mWorkChunkNeedsReload = false;
            }
            float speed = computeSpeed();
            ValidationResult<FluidStack> pumpResult = tryPumpOil(speed);
            if (pumpResult.getType() != ValidationType.VALID) {
                mEUt = 0;
                mMaxProgresstime = 0;
                setRuntimeFailureReason(CheckRecipeResultRegistry.FLUID_OUTPUT_FULL);
                return false;
            }
            FluidStack tFluid = pumpResult.getResult();
            if (tFluid != null && tFluid.amount > getTotalConfigValue()) {
                this.mOutputFluids = new FluidStack[] { tFluid };
                return true;
            }
        }
        GTChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        workState = WorkState.UPWARD;
        setShutdownReason(StatCollector.translateToLocal("GT5U.gui.text.drill_exhausted"));
        return true;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        String casings = getCasingBlockItem().get(0)
            .getDisplayName();

        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        final int baseCycleTime = calculateMaxProgressTime(getMinTier(), true);
        tt.addMachineType("Pump, FDR")
            .addInfo("Works on " + getRangeInChunks() + "x" + getRangeInChunks() + " chunks")
            .addInfo("Use a Screwdriver to configure range")
            .addInfo("Use Programmed Circuits to ignore near exhausted oil field")
            .addInfo("If total circuit # is greater than output per operation, the machine will halt.") // doesn't
            // work
            .addInfo("Minimum energy hatch tier: " + GTUtility.getColoredTierNameFromTier((byte) getMinTier()))
            .addInfo(
                "Base cycle time: " + (baseCycleTime < 20 ? formatNumber(baseCycleTime) + " ticks"
                    : formatNumber(baseCycleTime / 20.0) + " seconds"))
            .beginStructureBlock(3, 7, 3, false)
            .addController("Front bottom center")
            .addOtherStructurePart(casings, "form the 3x1x3 Base")
            .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base")
            .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
            .addEnergyHatch("1x " + VN[getMinTier()] + "+, Any base casing", 1)
            .addMaintenanceHatch("Any base casing", 1)
            .addInputBus("Mining Pipes or Circuits, optional, any base casing", 1)
            .addOutputHatch("Any base casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isActive()) {
            GTUtility.sendChatTrans(aPlayer, "GT5U.machines.workarea_fail");
        } else {
            adjustChunkRange(!aPlayer.isSneaking());

            GTUtility.sendChatTrans(aPlayer, "GT5U.machines.workareaset.chunks", chunkRangeConfig, chunkRangeConfig);
        }
    }

    @Override
    public String[] getInfoData() {
        List<String> l = new ArrayList<>(
            Arrays.asList(
                EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.machines.oilfluidpump")
                    + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.machines.workarea") + ": "
                    + EnumChatFormatting.GREEN
                    + formatNumber(chunkRangeConfig)
                    + " x "
                    + formatNumber(chunkRangeConfig)
                    + EnumChatFormatting.RESET
                    + " "
                    + StatCollector.translateToLocal("GT5U.machines.chunks"),
                StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.oil_drill.drilling_fluid",
                    EnumChatFormatting.GREEN + getFluidName() + EnumChatFormatting.RESET),
                StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.oil_drill.drilling_flow",
                    EnumChatFormatting.GREEN + formatNumber(getFlowRatePerTick()) + EnumChatFormatting.RESET)));
        l.addAll(Arrays.asList(super.getInfoData()));
        return l.toArray(new String[0]);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_PUMP;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_OIL_DRILL_LOOP;
    }

    /********************************************************
     * Implemented IMetricsExporter
     *******************************************************/
    @Override
    public @NotNull List<String> reportMetrics() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return ImmutableList.of();
        }

        final String failureReason = getFailureReason()
            .map(reason -> StatCollector.translateToLocalFormatted("GT5U.gui.text.drill_offline_reason", reason))
            .orElseGet(() -> StatCollector.translateToLocalFormatted("GT5U.gui.text.drill_offline_generic"));

        if (workState == WorkState.AT_BOTTOM) {
            final ImmutableList.Builder<String> builder = ImmutableList.builder();
            builder.add(StatCollector.translateToLocalFormatted("GT5U.gui.text.pump_fluid_type", getFluidName()));

            if (base.isActive()) {
                builder.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.pump_rate.1",
                        EnumChatFormatting.AQUA + numberFormat.format(getFlowRatePerTick()))
                        + StatCollector.translateToLocal("GT5U.gui.text.pump_rate.2"),
                    mOilFlow + StatCollector.translateToLocal("GT5U.gui.text.pump_recovery.2"));
            } else {
                builder.add(failureReason);
            }

            return builder.build();
        }

        if (base.isActive()) {
            return switch (workState) {
                case DOWNWARD -> ImmutableList.of(StatCollector.translateToLocal("GT5U.gui.text.deploying_pipe"));
                case UPWARD, ABORT -> ImmutableList.of(StatCollector.translateToLocal("GT5U.gui.text.retracting_pipe"));
                default -> ImmutableList.of();
            };
        }

        return ImmutableList.of(failureReason);
    }

    /********************************************************
     * Implemented IWorkAreaProvider
     *******************************************************/
    @Override
    public boolean isWorkAreaShown() {
        return showWorkArea;
    }

    @Override
    public int getCurrentWorkAreaOrder() {
        return 0;
    }

    @Override
    public @Nullable AxisAlignedBB getWorkAreaAABB() {
        WorkAreaBounds bounds = getWorkAreaBounds();
        if (bounds == null) {
            return null;
        }

        return AxisAlignedBB.getBoundingBox(
            bounds.minChunkX() << 4,
            0,
            bounds.minChunkZ() << 4,
            bounds.maxChunkX() << 4,
            256,
            bounds.maxChunkZ() << 4);
    }

    @Override
    public List<WorkAreaChunk> getWorkAreaChunksInWorkOrder() {
        WorkAreaBounds bounds = getWorkAreaBounds();
        if (bounds == null) {
            return Collections.emptyList();
        }

        if (bounds.equals(cachedWorkAreaBounds)) {
            return cachedWorkAreaChunks;
        }

        cachedWorkAreaBounds = bounds;
        cachedWorkAreaChunks = Collections.unmodifiableList(buildWorkAreaChunksInWorkOrder(bounds));

        return cachedWorkAreaChunks;
    }

    @Override
    public WorkAreaChunkState getWorkAreaChunkState(@NotNull WorkAreaChunk chunk) {
        long key = packChunkKey(chunk.chunkX(), chunk.chunkZ());

        if (activeOilFieldChunkKeys.contains(key)) {
            return WorkAreaChunkState.ACTIVE;
        }

        return WorkAreaChunkState.INACTIVE;
    }

    /********************************************************
     * Protected helpers
     *******************************************************/
    protected abstract int getRangeInChunks();

    protected float computeSpeed() {
        return .5F + (GTUtility.getTier(getMaxInputVoltage()) - getMinTier()) * .25F;
    }

    /**
     * Pump the oil. Takes batch mode into account.
     *
     * @param speed    Speed to pump oil
     * @param simulate If true, it actually does not consume vein
     * @return Fluid pumped
     */
    protected FluidStack pumpOil(@Nonnegative float speed, boolean simulate) {
        if (speed < 0) {
            throw new IllegalArgumentException("Don't pass negative speed");
        }

        FluidStack returnOil = new FluidStack(mOil, 0);
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return returnOil;
        }

        World world = base.getWorld();
        final float coefficient = (simulate ? -speed : speed) * batchMultiplier;

        boolean workAreaChanged = false;

        for (Iterator<ChunkCoordIntPair> iterator = mOilFieldChunks.iterator(); iterator.hasNext();) {
            ChunkCoordIntPair tChunk = iterator.next();
            FluidStack pumped = undergroundOil(world, tChunk.chunkXPos, tChunk.chunkZPos, coefficient);
            if (debugDriller) {
                GTLog.out.println(" chunkX = " + tChunk.chunkXPos + " chunkZ = " + tChunk.chunkZPos);
                if (pumped != null) {
                    GTLog.out.println("     Fluid pumped = " + pumped.amount);
                } else {
                    GTLog.out.println("     No fluid pumped ");
                }
            }
            if (pumped == null || pumped.amount < 1) {
                iterator.remove();
                activeOilFieldChunkKeys.remove(packChunkKey(tChunk.chunkXPos, tChunk.chunkZPos));
                workAreaChanged = true;
                continue;
            }
            if (returnOil.isFluidEqual(pumped)) {
                returnOil.amount += pumped.amount;
            }
        }

        if (workAreaChanged) {
            syncWorkAreaData();
        }

        return returnOil;
    }

    /********************************************************
     * Private getters
     *******************************************************/
    private int getFlowRatePerTick() {
        return this.mMaxProgresstime > 0 ? (mOilFlow / this.mMaxProgresstime) : 0;
    }

    private @Nullable WorkAreaBounds getWorkAreaBounds() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return null;
        }

        int xDrill = base.getXCoord();
        int zDrill = base.getZCoord();
        int range = Math.max(1, chunkRangeConfig);

        if (cachedBounds != null && cachedBoundsXDrill == xDrill
            && cachedBoundsZDrill == zDrill
            && cachedBoundsRange == range) {
            return cachedBounds;
        }

        int controllerChunkX = xDrill >> 4;
        int controllerChunkZ = zDrill >> 4;

        int minChunkX = Math.floorDiv(controllerChunkX, range) * range;
        int minChunkZ = Math.floorDiv(controllerChunkZ, range) * range;
        int maxChunkX = minChunkX + range;
        int maxChunkZ = minChunkZ + range;

        cachedBoundsXDrill = xDrill;
        cachedBoundsZDrill = zDrill;
        cachedBoundsRange = range;
        cachedBounds = new WorkAreaBounds(minChunkX, minChunkZ, maxChunkX, maxChunkZ);

        return cachedBounds;
    }

    private @NotNull List<WorkAreaChunk> buildWorkAreaChunksInWorkOrder(@NotNull WorkAreaBounds bounds) {
        int totalChunkCount = getTotalWorkAreaChunkCount(bounds);
        List<WorkAreaChunk> chunks = new ArrayList<>(totalChunkCount);

        int order = 1;

        for (int chunkX = bounds.minChunkX(); chunkX < bounds.maxChunkX(); chunkX++) {
            for (int chunkZ = bounds.minChunkZ(); chunkZ < bounds.maxChunkZ(); chunkZ++) {
                chunks.add(new WorkAreaChunk(chunkX, chunkZ, order++));
            }
        }

        return chunks;
    }

    private @NotNull String getFluidName() {
        if (mOil != null) {
            return mOil.getLocalizedName(new FluidStack(mOil, 0));
        }
        return "None";
    }

    private int getTotalWorkAreaChunkCount(@NotNull WorkAreaBounds bounds) {
        return (bounds.maxChunkX() - bounds.minChunkX()) * (bounds.maxChunkZ() - bounds.minChunkZ());
    }

    private static long packChunkKey(int chunkX, int chunkZ) {
        return (((long) chunkX) << 32) ^ (chunkZ & 0xffffffffL);
    }

    private boolean tryFillChunkList() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return false;
        }

        FluidStack tFluid, tOil;
        if (mOil == null) {
            tFluid = undergroundOilReadInformation(base);
            if (tFluid == null) return false;
            mOil = tFluid.getFluid();
        }
        if (debugDriller) {
            GTLog.out.println(mOil == null ? null : " Driller on  fluid = " + mOil.getName());
        }

        tOil = new FluidStack(mOil, 0);

        if (mOilFieldChunks.isEmpty()) {
            ChunkCoordIntPair tChunk = new ChunkCoordIntPair(base.getXCoord() >> 4, base.getZCoord() >> 4);
            int range = chunkRangeConfig;
            int xChunk = Math.floorDiv(tChunk.chunkXPos, range) * range; // For negative values, / returns rounded
            // towards zero.
            int zChunk = Math.floorDiv(tChunk.chunkZPos, range) * range;
            if (debugDriller) {
                GTLog.out.println(
                    "tChunk.chunkXPos = " + tChunk.chunkXPos
                        + " tChunk.chunkZPos = "
                        + tChunk.chunkZPos
                        + " xChunk = "
                        + xChunk
                        + " zChunk = "
                        + zChunk);
            }

            for (int i = 0; i < range; i++) {
                for (int j = 0; j < range; j++) {
                    if (debugDriller) {
                        GTLog.out.println(" getChunkX = " + (xChunk + i) + " getChunkZ = " + (zChunk + j));
                    }
                    tChunk = new ChunkCoordIntPair(xChunk + i, zChunk + j);
                    tFluid = undergroundOil(base.getWorld(), xChunk + i, zChunk + j, -1);
                    if (debugDriller) {
                        String fluidName = tFluid != null ? tFluid.getFluid()
                            .getName() : null;
                        GTLog.out.println(" Fluid in chunk = " + fluidName);
                    }
                    if (tFluid != null && tOil.isFluidEqual(tFluid) && tFluid.amount > 0) {
                        mOilFieldChunks.add(tChunk);
                        activeOilFieldChunkKeys.add(packChunkKey(tChunk.chunkXPos, tChunk.chunkZPos));
                        if (debugDriller) {
                            GTLog.out.println(" Matching fluid, quantity = " + tFluid.amount);
                        }
                    }
                }
            }
        }

        if (debugDriller) {
            GTLog.out.println("mOilFieldChunks.size = " + mOilFieldChunks.size());
        }

        return !mOilFieldChunks.isEmpty();
    }

    /**
     * Tries to pump oil, accounting for output space if void protection is enabled.
     * <p>
     * If pumped fluid will not fit in output hatches, it returns a result with INVALID.
     * <p>
     * If vein is depleted, it returns a result with VALID and null fluid.
     */
    private @NotNull ValidationResult<FluidStack> tryPumpOil(float speed) {
        if (mOil == null) {
            return ValidationResult.of(ValidationType.VALID, null);
        }

        if (debugDriller) {
            GTLog.out.println(" pump speed = " + speed);
        }

        // Even though it works fine without this check,
        // it can save tiny amount of CPU time when void protection is disabled
        if (protectsExcessFluid()) {
            FluidStack simulatedOil = pumpOil(speed, true);
            if (!canOutputAll(new FluidStack[] { simulatedOil })) {
                return ValidationResult.of(ValidationType.INVALID, null);
            }
        }

        FluidStack pumpedOil = pumpOil(speed, false);
        mOilFlow = pumpedOil.amount;
        // Multiblock base already includes 1 parallel
        recipesDone += batchMultiplier - 1;

        return ValidationResult.of(ValidationType.VALID, pumpedOil.amount == 0 ? null : pumpedOil);
    }

    /********************************************************
     * Private helpers
     *******************************************************/
    private void adjustChunkRange(boolean increase) {
        int oldChunkRange = chunkRangeConfig;

        if (increase) {
            if (chunkRangeConfig <= getRangeInChunks()) {
                chunkRangeConfig++;
            }

            if (chunkRangeConfig > getRangeInChunks()) {
                chunkRangeConfig = 1;
            }
        } else {
            if (chunkRangeConfig > 0) {
                chunkRangeConfig--;
            }

            if (chunkRangeConfig == 0) {
                chunkRangeConfig = getRangeInChunks();
            }
        }

        if (oldChunkRange != chunkRangeConfig) {
            mOilFieldChunks.clear();

            activeOilFieldChunkKeys.clear();

            invalidateWorkAreaCache();
            syncWorkAreaData();
        }
    }

    private void toggleWorkArea() {
        showWorkArea = !showWorkArea;
        syncWorkAreaData();
    }

    private void syncWorkAreaData() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }

        TileEntity tile = (TileEntity) base;

        if (!tile.getWorldObj().isRemote) {
            tile.markDirty();
            base.issueTileUpdate();
        }
    }

    private void invalidateWorkAreaCache() {
        cachedBounds = null;
        cachedBoundsXDrill = Integer.MIN_VALUE;
        cachedBoundsZDrill = Integer.MIN_VALUE;
        cachedBoundsRange = Integer.MIN_VALUE;

        cachedWorkAreaBounds = null;
        cachedWorkAreaChunks = Collections.emptyList();
    }

    /********************************************************
     * UI Buttons
     *******************************************************/
    private ButtonWidget createChunkRangeButton(ModularWindow.Builder builder) {
        IGregTechTileEntity base = Objects.requireNonNull(
            getBaseMetaTileEntity(),
            "Oil drill base meta tile entity is null while creating range button");

        return (ButtonWidget) new LockedWhileActiveButton(base, builder)
            .setOnClick((clickData, widget) -> adjustChunkRange(clickData.mouseButton == 0))
            .setPlayClickSound(true)
            .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_WORK_AREA)
            .attachSyncer(
                new FakeSyncWidget.IntegerSyncer(() -> chunkRangeConfig, this::setChunkRangeConfigFromSync),
                builder,
                (widget, val) -> widget.notifyTooltipChange())
            .dynamicTooltip(
                () -> ImmutableList.of(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.gui.button.oil_drill_radius_1",
                        formatNumber((long) chunkRangeConfig << 4)),
                    StatCollector.translateToLocal("GT5U.gui.button.oil_drill_radius_2")))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(16, 16);
    }

    private ButtonWidget createWorkAreaToggleButton(ModularWindow.Builder builder) {
        return (ButtonWidget) new ButtonWidget().setOnClick((clickData, widget) -> toggleWorkArea())
            .setPlayClickSound(true)
            .setBackground(() -> {
                if (showWorkArea) {
                    return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                        GTUITextures.OVERLAY_BUTTON_WORK_AREA };
                }

                return new IDrawable[] { GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_WORK_AREA };
            })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> showWorkArea, val -> showWorkArea = val),
                builder,
                (widget, val) -> widget.notifyTooltipChange())
            .dynamicTooltip(
                () -> ImmutableList.of(
                    StatCollector.translateToLocal(
                        showWorkArea ? "GT5U.gui.button.work_area_preview_on"
                            : "GT5U.gui.button.work_area_preview_off")))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(16, 16);
    }

    private void setChunkRangeConfigFromSync(int value) {
        if (chunkRangeConfig == value) {
            return;
        }

        chunkRangeConfig = value;
        invalidateWorkAreaCache();
    }
}
