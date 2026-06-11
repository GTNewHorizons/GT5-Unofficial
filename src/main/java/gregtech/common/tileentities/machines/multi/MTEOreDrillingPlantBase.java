package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.block.Block;
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
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetricsExporter;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTEOreDrillingPlantBaseGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.WorkAreaChunk;
import gregtech.common.misc.workarea.IWorkAreaProvider;
import gregtech.common.misc.workarea.WorkAreaProviderRegistry;
import gregtech.common.ores.OreManager;
import gregtech.crossmod.visualprospecting.VisualProspectingDatabase;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

public abstract class MTEOreDrillingPlantBase extends MTEDrillerBase implements IMetricsExporter, IWorkAreaProvider {

    protected int mTier = 1;

    private static final String NBT_VEIN_NAME = "veinName";
    private static final String NBT_REPLACE_WITH_COBBLESTONE = "replaceWithCobblestone";
    private static final String NBT_CHUNK_RADIUS_CONFIG = "chunkRadiusConfig";
    private static final String NBT_SHOW_WORK_AREA = "showWorkArea";
    private static final String NBT_WORK_STATE = "workState";
    private static final String NBT_HAS_CURRENT_WORK_CHUNK = "hasCurrentWorkChunk";
    private static final String NBT_CURRENT_WORK_CHUNK_X = "currentWorkChunkX";
    private static final String NBT_CURRENT_WORK_CHUNK_Z = "currentWorkChunkZ";

    private final LongList oreBlockPositions = new LongArrayList();
    private final LongSet oreBlockSet = new LongOpenHashSet();
    private int chunkRadiusConfig = getRadiusInChunks();
    private boolean replaceWithCobblestone = true;
    private boolean showWorkArea = false;

    private @Nullable WorkAreaBounds cachedWorkAreaBounds = null;
    private List<WorkAreaChunk> cachedWorkAreaChunks = Collections.emptyList();

    private @Nullable WorkAreaBounds cachedBounds = null;
    private int cachedBoundsXDrill = Integer.MIN_VALUE;
    private int cachedBoundsZDrill = Integer.MIN_VALUE;
    private int cachedBoundsRadius = Integer.MIN_VALUE;

    private int clientOreListSize = 0;
    private int clientCurrentChunk = 0;

    private int clientTotalChunks = 0;
    private int clientYHead = 0;
    private String veinName = null;
    private final XSTR random = new XSTR();

    @Desugar
    private record WorkAreaBounds(int minChunkX, int minChunkZ, int maxChunkX, int maxChunkZ, int centerChunkX,
        int centerChunkZ) {}

    MTEOreDrillingPlantBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    MTEOreDrillingPlantBase(String aName) {
        super(aName);
    }

    /********************************************************
     * Parent overrides
     *******************************************************/
    @Override
    public boolean isWorkAreaShown() {
        return showWorkArea;
    }

    @Override
    public int calculateMaxProgressTime(int tier, boolean simulateWorking) {
        return (int) Math.max(
            1,
            ((workState == WorkState.DOWNWARD || workState == WorkState.AT_BOTTOM || simulateWorking)
                ? getBaseProgressTime()
                : 80) / GTUtility.powInt(2, tier));
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger(NBT_CHUNK_RADIUS_CONFIG, chunkRadiusConfig);
        aNBT.setBoolean(NBT_REPLACE_WITH_COBBLESTONE, replaceWithCobblestone);
        aNBT.setBoolean(NBT_SHOW_WORK_AREA, showWorkArea);

        if (veinName != null) {
            aNBT.setString(NBT_VEIN_NAME, veinName);
        } else if (aNBT.hasKey(NBT_VEIN_NAME)) {
            aNBT.removeTag(NBT_VEIN_NAME);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        if (aNBT.hasKey(NBT_CHUNK_RADIUS_CONFIG)) {
            chunkRadiusConfig = aNBT.getInteger(NBT_CHUNK_RADIUS_CONFIG);
        }

        if (aNBT.hasKey(NBT_SHOW_WORK_AREA)) {
            showWorkArea = aNBT.getBoolean(NBT_SHOW_WORK_AREA);
        }

        if (aNBT.hasKey(NBT_REPLACE_WITH_COBBLESTONE)) {
            replaceWithCobblestone = aNBT.getBoolean(NBT_REPLACE_WITH_COBBLESTONE);
        }

        veinName = aNBT.hasKey(NBT_VEIN_NAME) ? aNBT.getString(NBT_VEIN_NAME) : null;
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = new NBTTagCompound();

        data.setInteger(NBT_CHUNK_RADIUS_CONFIG, chunkRadiusConfig);
        data.setBoolean(NBT_SHOW_WORK_AREA, showWorkArea);

        data.setInteger(NBT_WORK_STATE, workState.ordinal());
        data.setBoolean(NBT_HAS_CURRENT_WORK_CHUNK, mCurrentChunk != null);

        if (mCurrentChunk != null) {
            data.setInteger(NBT_CURRENT_WORK_CHUNK_X, mCurrentChunk.chunkXPos);
            data.setInteger(NBT_CURRENT_WORK_CHUNK_Z, mCurrentChunk.chunkZPos);
        }

        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        if (data == null) {
            return;
        }

        if (data.hasKey(NBT_CHUNK_RADIUS_CONFIG)) {
            chunkRadiusConfig = data.getInteger(NBT_CHUNK_RADIUS_CONFIG);
        }

        if (data.hasKey(NBT_SHOW_WORK_AREA)) {
            showWorkArea = data.getBoolean(NBT_SHOW_WORK_AREA);
        }

        if (data.hasKey(NBT_WORK_STATE)) {
            setWorkState(WorkState.fromOrdinal(data.getInteger(NBT_WORK_STATE)));
        }

        if (data.getBoolean(NBT_HAS_CURRENT_WORK_CHUNK)) {
            mCurrentChunk = new ChunkCoordIntPair(
                data.getInteger(NBT_CURRENT_WORK_CHUNK_X),
                data.getInteger(NBT_CURRENT_WORK_CHUNK_Z));
        } else {
            mCurrentChunk = null;
        }

        updateWorkAreaRendererRegistration();
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isActive()) {
            GTUtility.sendChatTrans(aPlayer, "GT5U.machines.workarea_fail");
        } else {
            adjustChunkRadius(!aPlayer.isSneaking());
            final String chunkDiameter = formatNumber(chunkRadiusConfig * 2L);
            final String blockDiameter = formatNumber(chunkRadiusConfig * 32L);
            GTUtility.sendChatTrans(
                aPlayer,
                "GT5U.machines.workareaset.blocks",
                chunkDiameter,
                chunkDiameter,
                blockDiameter,
                blockDiameter);
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        replaceWithCobblestone = !replaceWithCobblestone;
        GTUtility.sendChatTrans(
            aPlayer,
            "GT5U.chat.ore_drilling_plant_base.replace_with_cobblestone",
            replaceWithCobblestone);
        return true;
    }

    @Override
    public String[] getInfoData() {
        final String diameter = formatNumber(chunkRadiusConfig * 2L);
        return new String[] { "GT5U.infodata.ore_drill.title",
            IGregTechDeviceInformation.encode("GT5U.infodata.ore_drill.work_area", diameter, diameter) };
    }

    @Override
    protected void setElectricityStats() {
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(1, GTUtility.getTier(getMaxInputVoltage()));
        this.mEUt = -3 * (1 << (tier << 1));
        this.mMaxProgresstime = calculateMaxProgressTime(tier);
    }

    @Override
    protected boolean workingDownward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        if (yHead != oldYHead) {
            oreBlockPositions.clear();
            oreBlockSet.clear();
        }

        if (mWorkChunkNeedsReload && mChunkLoadingEnabled) { // ask to load machine itself
            GTChunkManager.requestChunkLoad((TileEntity) getBaseMetaTileEntity(), null);
            mWorkChunkNeedsReload = false;
        }
        fillMineListIfEmpty(xDrill, yDrill, zDrill, xPipe, zPipe, yHead);
        if (oreBlockPositions.isEmpty()) {
            if (veinName == null) {
                updateVeinNameFromVP(getDrillCoords());
            }

            switch (tryLowerPipeState()) {
                case NO_PIPE -> {
                    mMaxProgresstime = 0;
                    setRuntimeFailureReason(CheckRecipeResultRegistry.MISSING_MINING_PIPE);
                    return false;
                }
                case CANCELED -> {
                    workState = WorkState.UPWARD;
                    return true;
                }
                case INVALID_BLOCK -> {
                    workState = WorkState.AT_BOTTOM;
                    return true;
                }
            }
            // new layer - fill again
            fillMineListIfEmpty(xDrill, yDrill, zDrill, xPipe, zPipe, yHead);
        }
        return tryProcessOreList();
    }

    @Override
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        if (!mChunkLoadingEnabled)
            return super.workingAtBottom(aStack, xDrill, yDrill, zDrill, xPipe, zPipe, yHead, oldYHead);

        if (mCurrentChunk == null) {
            createInitialWorkingChunk();
            return true;
        }

        if (mWorkChunkNeedsReload) {
            GTChunkManager.requestChunkLoad((TileEntity) getBaseMetaTileEntity(), mCurrentChunk);
            mWorkChunkNeedsReload = false;
            return true;
        }
        if (oreBlockPositions.isEmpty()) {
            fillChunkMineList(yHead, yDrill);
            if (oreBlockPositions.isEmpty()) {
                GTChunkManager.releaseChunk((TileEntity) getBaseMetaTileEntity(), mCurrentChunk);
                if (!moveToNextChunk(xDrill >> 4, zDrill >> 4)) {
                    workState = WorkState.UPWARD;
                    updateVeinNameFromVP(mCurrentChunk);
                    syncWorkAreaData();
                }
                return true;
            }
        }
        return tryProcessOreList();
    }

    @Override
    protected boolean workingUpward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        boolean result;
        if (!mChunkLoadingEnabled || oreBlockPositions.isEmpty()) {
            result = super.workingUpward(aStack, xDrill, yDrill, zDrill, xPipe, zPipe, yHead, oldYHead);
        } else {
            result = tryProcessOreList();
            if (oreBlockPositions.isEmpty()) GTChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        }

        if (!result) {
            setShutdownReason(StatCollector.translateToLocal("GT5U.gui.text.drill_exhausted"));
        }

        return result;
    }

    @Override
    protected void onAbort() {
        oreBlockPositions.clear();

        if (mCurrentChunk != null) {
            GTChunkManager.releaseChunk((TileEntity) getBaseMetaTileEntity(), mCurrentChunk);
        }

        mCurrentChunk = null;
        updateVeinNameFromVP(mCurrentChunk);

        syncWorkAreaData();
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_MINER;
    }

    @Override
    protected List<IHatchElement<? super MTEDrillerBase>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, InputBus, OutputBus, Maintenance, Energy);
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEOreDrillingPlantBaseGui(this);
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

        if (!base.isActive()) {
            return ImmutableList.of(
                getFailureReason()
                    .map(
                        reason -> StatCollector.translateToLocalFormatted("GT5U.gui.text.drill_offline_reason", reason))
                    .orElseGet(() -> StatCollector.translateToLocalFormatted("GT5U.gui.text.drill_offline_generic")));
        }

        return switch (workState) {
            case AT_BOTTOM -> ImmutableList.of(
                StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.drill_ores_left_chunk",
                    formatNumber(oreBlockPositions.size())),
                StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.drill_chunks_left",
                    formatNumber(getChunkNumber()),
                    formatNumber(getTotalChunkCount())),
                veinName == null ? ""
                    : StatCollector.translateToLocalFormatted("GT5U.gui.text.drill_current_vein", veinName));
            case DOWNWARD -> ImmutableList.of(
                StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.drill_ores_left_layer",
                    getYHead(),
                    formatNumber(oreBlockPositions.size())),
                veinName == null ? ""
                    : StatCollector.translateToLocalFormatted("GT5U.gui.text.drill_current_vein", veinName));
            case UPWARD, ABORT -> ImmutableList.of(StatCollector.translateToLocal("GT5U.gui.text.retracting_pipe"));
        };
    }

    /********************************************************
     * Implemented IWorkAreaProvider
     *******************************************************/
    @Override
    public @Nullable World getWorkAreaWorld() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        return base == null ? null : base.getWorld();
    }

    public int getCurrentWorkAreaOrder() {
        WorkAreaBounds bounds = getWorkAreaBounds();
        if (bounds == null) {
            return 0;
        }

        if (workState == WorkState.DOWNWARD) {
            return 1;
        }

        if (workState == WorkState.AT_BOTTOM && mCurrentChunk != null) {
            return getWorkOrderForChunk(bounds, mCurrentChunk.chunkXPos, mCurrentChunk.chunkZPos);
        }

        if (workState == WorkState.UPWARD && mCurrentChunk == null) {
            return getTotalWorkAreaChunkCount(bounds) + 1;
        }

        return 0;
    }

    public AxisAlignedBB getWorkAreaAABB() {
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

    public List<WorkAreaChunk> getWorkAreaChunksInWorkOrder() {
        WorkAreaBounds bounds = getWorkAreaBounds();
        if (bounds == null) {
            return Collections.emptyList();
        }

        if (bounds.equals(cachedWorkAreaBounds)) {
            return cachedWorkAreaChunks;
        }

        cachedWorkAreaBounds = bounds;
        cachedWorkAreaChunks = Collections.unmodifiableList(getWorkAreaChunksInWorkOrder(bounds));

        return cachedWorkAreaChunks;
    }

    /********************************************************
     * Protected helpers
     *******************************************************/
    protected abstract int getRadiusInChunks();

    protected abstract int getBaseProgressTime();

    protected MultiblockTooltipBuilder createTooltip() {
        String casings = getCasingBlockItem().get(0)
            .getDisplayName();

        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        final int baseCycleTime = calculateMaxProgressTime(getMinTier(), true);
        final String chunkDiameter = formatNumber(chunkRadiusConfig * 2L);
        final String blockDiameter = formatNumber(chunkRadiusConfig * 32L);
        tt.addMachineType("Miner, MBM")
            .addInfo("Use a Screwdriver to configure working area")
            .addInfo(
                "Maximum area is " + chunkDiameter
                    + "x"
                    + chunkDiameter
                    + " chunks ("
                    + blockDiameter
                    + "x"
                    + blockDiameter
                    + " blocks)")
            .addInfo("In chunk mode, working area center is the chunk corner nearest to the drill")
            .addInfo("Use Soldering iron to turn off chunk mode")
            .addInfo("Use Wire Cutter to toggle replacing mined blocks with cobblestone")
            .addInfo("Requires Drilling Fluid to operate")
            .addInfo("Gives ~3x as much crushed ore vs normal processing")
            .addInfo("Fortune bonus of " + formatNumber(mTier + 3) + ". Only works on small ores")
            .addInfo("Minimum energy hatch tier: " + GTUtility.getColoredTierNameFromTier((byte) getMinTier()))
            .addInfo(
                "Base cycle time: " + (baseCycleTime < 20 ? formatNumber(baseCycleTime) + " ticks"
                    : formatNumber(baseCycleTime / 20.0) + " seconds"))
            .beginStructureBlock(3, 7, 3, false)
            .addController("Front bottom center")
            .addOtherStructurePart(casings, "form the 3x1x3 Base")
            .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
            .addOtherStructurePart(getFrameMaterial().mName + " Frame Box", "Each pillar's side and 1x3x1 on top")
            .addEnergyHatch(VN[getMinTier()] + "+, any base Casing", 1)
            .addMaintenanceHatch("Any base Casing", 1)
            .addInputBus("Mining Pipes, optional, any base Casing", 1)
            .addInputHatch("Drilling Fluid, any base Casing", 1)
            .addOutputBus("Any base Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    /********************************************************
     * Getters and setters
     *******************************************************/
    public int getTotalChunkCount() {
        final ChunkCoordIntPair topLeft = getTopLeftChunkCoords();
        final ChunkCoordIntPair bottomRight = getBottomRightChunkCoords();
        return (bottomRight.chunkXPos - topLeft.chunkXPos) * (bottomRight.chunkZPos - topLeft.chunkZPos);
    }

    public int getOreBlockCount() {
        return oreBlockPositions.size();
    }

    public int getChunkRadiusConfig() {
        return chunkRadiusConfig;
    }

    public void setChunkRadiusConfig(int val) {
        chunkRadiusConfig = val;
    }

    public boolean isReplaceWithCobblestone() {
        return replaceWithCobblestone;
    }

    public void setReplaceWithCobblestone(boolean val) {
        replaceWithCobblestone = val;
    }

    public boolean isShowWorkArea() {
        return showWorkArea;
    }

    public void setShowWorkArea(boolean val) {
        showWorkArea = val;
        updateWorkAreaRendererRegistration();
    }

    public String getVeinName() {
        return veinName;
    }

    public void setVeinName(String val) {
        veinName = val;
    }

    public void setClientOreListSize(int val) {
        clientOreListSize = val;
    }

    public void setClientCurrentChunk(int val) {
        clientCurrentChunk = val;
    }

    public void setClientTotalChunks(int val) {
        clientTotalChunks = val;
    }

    public void setClientYHead(int val) {
        clientYHead = val;
    }

    @NotNull
    private ChunkCoordIntPair getTopLeftChunkCoords() {
        return getCornerCoords(-1, -1);
    }

    @NotNull
    private ChunkCoordIntPair getBottomRightChunkCoords() {
        return getCornerCoords(1, 1);
    }

    @NotNull
    private ChunkCoordIntPair getCornerCoords(int xMultiplier, int zMultiplier) {
        final ChunkCoordIntPair drillPos = getDrillCoords();
        // use corner closest to the drill as mining area center
        return new ChunkCoordIntPair(
            drillPos.chunkXPos + xMultiplier * chunkRadiusConfig
                + ((getXDrill() - (drillPos.chunkXPos << 4)) < 8 ? 0 : 1),
            drillPos.chunkZPos + zMultiplier * chunkRadiusConfig
                + ((getZDrill() - (drillPos.chunkZPos << 4)) < 8 ? 0 : 1));
    }

    @NotNull
    private ChunkCoordIntPair getDrillCoords() {
        return new ChunkCoordIntPair(getXDrill() >> 4, getZDrill() >> 4);
    }

    private ItemStack @NotNull [] getOutputByDrops(@NotNull List<ItemStack> oreBlockDrops) {
        long voltage = getMaxInputVoltage();
        List<ItemStack> outputItems = new ArrayList<>();

        for (ItemStack currentItem : oreBlockDrops) {
            if (!doUseMaceratorRecipe(currentItem)) {
                outputItems.add(getMultipliedStackSize(currentItem));
                continue;
            }
            GTRecipe tRecipe = RecipeMaps.maceratorRecipes.findRecipeQuery()
                .caching(true)
                .items(currentItem)
                .voltage(voltage)
                .find();

            if (tRecipe == null) {
                outputItems.add(currentItem);
                continue;
            }

            for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                ItemStack recipeOutput = tRecipe.mOutputs[i].copy();
                if (random.nextInt(10000) < tRecipe.getOutputChance(i)) {
                    getMultipliedStackSize(recipeOutput);
                }
                outputItems.add(recipeOutput);
            }
        }

        return outputItems.toArray(new ItemStack[0]);
    }

    /**
     * Returns a number corresponding to which chunk the drill is operating on. Only really useful for driving outputs
     * in the controller UI.
     *
     * @return 0 if the miner is not in operation, positive integer corresponding to the chunk currently being drilled
     */
    @SuppressWarnings("ExtractMethodRecommender")
    public int getChunkNumber() {
        if (mCurrentChunk == null) {
            return 0;
        }

        final ChunkCoordIntPair topLeft = getTopLeftChunkCoords();
        final ChunkCoordIntPair drillPos = getDrillCoords();

        if (workState == WorkState.DOWNWARD) {
            return 1;
        } else if (workState == WorkState.UPWARD) {
            // Technically, the miner isn't mining anything now; it's retracting the pipes in preparation to end
            // operation.
            return 0;
        }

        int chunkNumber = (chunkRadiusConfig * 2) * (mCurrentChunk.chunkZPos - topLeft.chunkZPos)
            + mCurrentChunk.chunkXPos
            - topLeft.chunkXPos
            + 1;

        // Drills mine the chunk they're in first, so if we're not there yet, bump the number to indicate that it
        // was already mined.
        if (mCurrentChunk.chunkZPos < drillPos.chunkZPos
            || (mCurrentChunk.chunkZPos == drillPos.chunkZPos && mCurrentChunk.chunkXPos < drillPos.chunkXPos)) {
            chunkNumber += 1;
        }
        return chunkNumber;
    }

    private @Nullable WorkAreaBounds getWorkAreaBounds() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return null;
        }

        int xDrill = base.getXCoord();
        int zDrill = base.getZCoord();
        int radius = Math.max(1, chunkRadiusConfig);

        if (cachedBounds != null && cachedBoundsXDrill == xDrill
            && cachedBoundsZDrill == zDrill
            && cachedBoundsRadius == radius) {
            return cachedBounds;
        }

        int centerChunkX = xDrill >> 4;
        int centerChunkZ = zDrill >> 4;

        int xOffset = (xDrill - (centerChunkX << 4)) < 8 ? 0 : 1;
        int zOffset = (zDrill - (centerChunkZ << 4)) < 8 ? 0 : 1;

        int minChunkX = centerChunkX - radius + xOffset;
        int minChunkZ = centerChunkZ - radius + zOffset;
        int maxChunkX = centerChunkX + radius + xOffset;
        int maxChunkZ = centerChunkZ + radius + zOffset;

        cachedBoundsXDrill = xDrill;
        cachedBoundsZDrill = zDrill;
        cachedBoundsRadius = radius;
        cachedBounds = new WorkAreaBounds(minChunkX, minChunkZ, maxChunkX, maxChunkZ, centerChunkX, centerChunkZ);

        return cachedBounds;
    }

    private int getWorkOrderForChunk(@NotNull WorkAreaBounds bounds, int targetChunkX, int targetChunkZ) {
        if (targetChunkX == bounds.centerChunkX() && targetChunkZ == bounds.centerChunkZ()) {
            return 1;
        }

        int order = 2;

        for (int chunkZ = bounds.minChunkZ(); chunkZ < bounds.maxChunkZ(); chunkZ++) {
            for (int chunkX = bounds.minChunkX(); chunkX < bounds.maxChunkX(); chunkX++) {
                if (chunkX == bounds.centerChunkX() && chunkZ == bounds.centerChunkZ()) {
                    continue;
                }

                if (chunkX == targetChunkX && chunkZ == targetChunkZ) {
                    return order;
                }

                order++;
            }
        }

        return 0;
    }

    private int getTotalWorkAreaChunkCount(@NotNull WorkAreaBounds bounds) {
        return (bounds.maxChunkX() - bounds.minChunkX()) * (bounds.maxChunkZ() - bounds.minChunkZ());
    }

    private @NotNull List<WorkAreaChunk> getWorkAreaChunksInWorkOrder(@NotNull WorkAreaBounds bounds) {
        int totalChunkCount = getTotalWorkAreaChunkCount(bounds);
        List<WorkAreaChunk> chunks = new ArrayList<>(totalChunkCount);

        int order = 1;

        // Chunk with controller is treated in first, during the DOWNWARD phase
        chunks.add(new WorkAreaChunk(bounds.centerChunkX(), bounds.centerChunkZ(), order++));

        for (int chunkZ = bounds.minChunkZ(); chunkZ < bounds.maxChunkZ(); chunkZ++) {
            for (int chunkX = bounds.minChunkX(); chunkX < bounds.maxChunkX(); chunkX++) {
                if (chunkX == bounds.centerChunkX() && chunkZ == bounds.centerChunkZ()) {
                    continue;
                }

                chunks.add(new WorkAreaChunk(chunkX, chunkZ, order++));
            }
        }

        return chunks;
    }

    private @NotNull ItemStack getMultipliedStackSize(@NotNull ItemStack itemStack) {
        IGregTechTileEntity base = Objects
            .requireNonNull(getBaseMetaTileEntity(), "Ore drill base tile entity is null on getMultipliedStackSize");

        itemStack.stackSize *= base.getRandomNumber(4) + 1;

        return itemStack;
    }

    /********************************************************
     * Helpers
     *******************************************************/
    private boolean tryProcessOreList() {
        // Even though it works fine without this check,
        // it can save tiny amount of CPU time when void protection is disabled
        if (protectsExcessItem()) {
            // Store the XSTR's internal state, then restore it after to make sure the results of processOreList are
            // deterministic
            long seed = random.getSeed();
            boolean simulateResult = processOreList(true);
            random.setSeed(seed);

            if (!simulateResult) {
                mEUt = 0;
                mMaxProgresstime = 0;
                return false;
            }
        }

        boolean result = processOreList(false);

        if (!result) {
            mEUt = 0;
            mMaxProgresstime = 0;
            return false;
        }
        return true;
    }

    private boolean processOreList(boolean simulate) {
        IGregTechTileEntity base = Objects
            .requireNonNull(getBaseMetaTileEntity(), "Ore drill base tile entity is null while process ore list");

        World world = base.getWorld();
        LongIterator iter = oreBlockPositions.iterator();

        if (!tryConsumeDrillingFluid(simulate)) {
            setRuntimeFailureReason(CheckRecipeResultRegistry.NO_DRILLING_FLUID);
            return false;
        }

        while (iter.hasNext()) {
            long pos = iter.nextLong();

            int x = CoordinatePacker.unpackX(pos);
            int y = CoordinatePacker.unpackY(pos);
            int z = CoordinatePacker.unpackZ(pos);

            Block block = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);

            if (!GTUtility.isOre(block, meta)) {
                // Always remove non-ore blocks, even if we're simulating
                iter.remove();
                continue;
            }

            if (!world.canMineBlock(getFakePlayer(base), x, y, z)) {
                // Always remove protected blocks - if the fake player can't remove it now, they won't be able to remove
                // it later
                iter.remove();
                continue;
            }

            List<ItemStack> oreBlockDrops = OreManager
                .mineBlock(random, world, x, y, z, false, mTier + 3, simulate, replaceWithCobblestone);

            ItemStack[] toOutput = getOutputByDrops(oreBlockDrops);

            if (simulate && !canOutputAll(toOutput)) {
                setRuntimeFailureReason(CheckRecipeResultRegistry.ITEM_OUTPUT_FULL);
                return false;
            }

            if (!simulate) iter.remove();

            mOutputItems = toOutput;

            return true;
        }

        return true;
    }

    private void createInitialWorkingChunk() {
        mCurrentChunk = getTopLeftChunkCoords();

        updateVeinNameFromVP(mCurrentChunk);

        if (mChunkLoadingEnabled) {
            GTChunkManager.requestChunkLoad((TileEntity) getBaseMetaTileEntity(), mCurrentChunk);
            mWorkChunkNeedsReload = false;
        }

        syncWorkAreaData();
    }

    private boolean moveToNextChunk(int centerX, int centerZ) {
        if (mCurrentChunk == null) return false;
        // use corner closest to the drill as mining area center
        final int left = centerX - chunkRadiusConfig + ((getXDrill() - (centerX << 4)) < 8 ? 0 : 1);
        final int right = left + chunkRadiusConfig * 2;
        final int bottom = centerZ + chunkRadiusConfig + ((getZDrill() - (centerZ << 4)) < 8 ? 0 : 1);

        int nextChunkX = mCurrentChunk.chunkXPos + 1;
        int nextChunkZ = mCurrentChunk.chunkZPos;

        // step to the next chunk
        if (nextChunkX >= right) {
            nextChunkX = left;
            ++nextChunkZ;
        }
        // skip center chunk - dug in workingDownward()
        if (nextChunkX == centerX && nextChunkZ == centerZ) {
            ++nextChunkX;

            if (nextChunkX >= right) {
                nextChunkX = left;
                ++nextChunkZ;
            }
        }

        if (nextChunkZ >= bottom) {
            mCurrentChunk = null;
            return false;
        }

        mCurrentChunk = new ChunkCoordIntPair(nextChunkX, nextChunkZ);
        updateVeinNameFromVP(mCurrentChunk);

        GTChunkManager
            .requestChunkLoad((TileEntity) getBaseMetaTileEntity(), new ChunkCoordIntPair(nextChunkX, nextChunkZ));

        syncWorkAreaData();

        return true;
    }

    private void updateVeinNameFromVP(ChunkCoordIntPair coords) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || coords == null) {
            veinName = null;
        } else {
            veinName = VisualProspectingDatabase.getVeinName(base.getWorld().provider.dimensionId, coords)
                .orElse(null);
        }
    }

    @Override
    protected void checkHatches(List<StructureError> errors) {
        checkHasInputHatch(errors);
        checkHasOutputBus(errors);
        checkHasMaintenanceHatch(errors);
        checkHasEnergyHatch(errors);
    }

    private boolean doUseMaceratorRecipe(ItemStack currentItem) {
        ItemData itemData = GTOreDictUnificator.getItemData(currentItem);
        return itemData == null || itemData.mPrefix != OrePrefixes.crushed && itemData.mPrefix != OrePrefixes.dustImpure
            && itemData.mPrefix != OrePrefixes.dust
            && itemData.mPrefix != OrePrefixes.gem
            && itemData.mPrefix != OrePrefixes.gemChipped
            && itemData.mPrefix != OrePrefixes.gemExquisite
            && itemData.mPrefix != OrePrefixes.gemFlawed
            && itemData.mPrefix != OrePrefixes.gemFlawless
            && itemData.mMaterial.mMaterial != Materials.Oilsands
            && !itemData.mMaterial.mMaterial.contains(SubTag.ICE_ORE);
    }

    private boolean tryConsumeDrillingFluid(boolean simulate) {
        return depleteInput(new FluidStack(ItemList.sDrillingFluid, 2000), simulate);
    }

    private void fillChunkMineList(int yHead, int yDrill) {
        if (mCurrentChunk == null || !oreBlockPositions.isEmpty()) return;

        oreBlockSet.clear();

        final int minX = mCurrentChunk.chunkXPos << 4;
        final int maxX = minX + 16;
        final int minZ = mCurrentChunk.chunkZPos << 4;
        final int maxZ = minZ + 16;

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                for (int y = yHead; y < yDrill; ++y) {
                    tryAddOreBlockToMineList(x, y, z);
                }
            }
        }
    }

    private void fillMineListIfEmpty(int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead) {
        if (!oreBlockPositions.isEmpty()) return;

        oreBlockSet.clear();

        tryAddOreBlockToMineList(xPipe, yHead - 1, zPipe);
        if (yHead == yDrill) return; // skip controller block layer

        if (mChunkLoadingEnabled) {
            int startX = (xDrill >> 4) << 4;
            int startZ = (zDrill >> 4) << 4;
            for (int x = startX; x < (startX + 16); ++x) {
                for (int z = startZ; z < (startZ + 16); ++z) {
                    tryAddOreBlockToMineList(x, yHead, z);
                }
            }
        } else {
            int radius = chunkRadiusConfig << 4;
            for (int xOff = -radius; xOff <= radius; xOff++) {
                for (int zOff = -radius; zOff <= radius; zOff++) {
                    tryAddOreBlockToMineList(xDrill + xOff, yHead, zDrill + zOff);
                }
            }
        }
    }

    private void tryAddOreBlockToMineList(int x, int y, int z) {
        IGregTechTileEntity base = Objects
            .requireNonNull(getBaseMetaTileEntity(), "Ore drill base tile entity is null while scanning ore blocks");

        Block block = base.getBlock(x, y, z);
        int blockMeta = base.getMetaID(x, y, z);

        long pos = CoordinatePacker.pack(x, y, z);

        if (GTUtility.isOre(block, blockMeta) && oreBlockSet.add(pos)) {
            oreBlockPositions.add(pos);
        }
    }

    public void adjustChunkRadius(boolean increase) {
        if (increase) {
            if (chunkRadiusConfig <= getRadiusInChunks()) {
                chunkRadiusConfig++;
            }
            if (chunkRadiusConfig > getRadiusInChunks()) chunkRadiusConfig = 1;
        } else {
            if (chunkRadiusConfig > 0) {
                chunkRadiusConfig--;
            }
            if (chunkRadiusConfig == 0) chunkRadiusConfig = getRadiusInChunks();
        }

        if (mCurrentChunk != null && mChunkLoadingEnabled) {
            GTChunkManager.releaseChunk((TileEntity) getBaseMetaTileEntity(), mCurrentChunk);
        }

        oreBlockPositions.clear();
        createInitialWorkingChunk();
    }

    public void toggleWorkArea() {
        showWorkArea = !showWorkArea;
        updateWorkAreaRendererRegistration();
        syncWorkAreaData();
    }

    public void syncWorkAreaData() {
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

    public void updateWorkAreaRendererRegistration() {
        WorkAreaProviderRegistry.setActive(this, showWorkArea);
    }
}
