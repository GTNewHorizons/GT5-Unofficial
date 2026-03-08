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
import static gregtech.common.UndergroundOil.undergroundOil;
import static gregtech.common.UndergroundOil.undergroundOilReadInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetricsExporter;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ValidationResult;
import gregtech.api.util.ValidationType;

public abstract class MTEOilDrillBase extends MTEDrillerBase implements IMetricsExporter {

    private final ArrayList<ChunkCoordIntPair> mOilFieldChunks = new ArrayList<>();
    private Fluid mOil = null;
    private int mOilFlow = 0;

    private int chunkRangeConfig = getRangeInChunks();

    public MTEOilDrillBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEOilDrillBase(String aName) {
        super(aName);
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
        aNBT.setInteger("chunkRangeConfig", chunkRangeConfig);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("chunkRangeConfig")) chunkRangeConfig = aNBT.getInteger("chunkRangeConfig");
    }

    protected MultiblockTooltipBuilder createTooltip(String tierSuffix) {
        String casings = getCasingBlockItem().get(0)
            .getDisplayName();

        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        final int baseCycleTime = calculateMaxProgressTime(getMinTier(), true);
        final String side = formatNumber((long) getRangeInChunks() << 4);

        final String timeUnit = baseCycleTime < 20
            ? GTUtility.translate(
                "gt.time.ticks",
                EnumChatFormatting.WHITE + formatNumber(baseCycleTime) + EnumChatFormatting.GRAY)
            : GTUtility.translate(
                "gt.time.seconds",
                EnumChatFormatting.WHITE + formatNumber(baseCycleTime / 20.0) + EnumChatFormatting.GRAY);

        final ItemStack frameStack = GTOreDictUnificator.get(OrePrefixes.frameGt, getFrameMaterial(), 1);
        final String frameBoxes = frameStack != null ? frameStack.getDisplayName()
            : getFrameMaterial().getLocalizedName();

        tt.addMachineType(GTUtility.translate("gt.multiblock.oil_drill.machine_type"))
            .addInfo(
                GTUtility.translate(
                    "gt.multiblock.oil_drill.desc1",
                    EnumChatFormatting.WHITE + side + "x" + side + EnumChatFormatting.GRAY))
            .addInfo(GTUtility.translate("gt.multiblock.oil_drill.desc2"))
            .addInfo(GTUtility.translate("gt.multiblock.oil_drill.desc3"))
            .addInfo(GTUtility.translate("gt.multiblock.oil_drill.desc4")) // doesn't
            // work
            .addInfo(
                GTUtility.translate(
                    "gt.multiblock.min_energy_hatch_tier",
                    GTUtility.getColoredTierNameFromTier((byte) getMinTier())))
            .addInfo(GTUtility.translate("gt.multiblock.base_cycle_time", timeUnit))
            .beginStructureBlock(3, 7, 3, false)
            .addController(GTUtility.translate("gt.structure.controller.front_bottom"))
            .addOtherStructurePart(casings, GTUtility.translate("gt.structure.base.3x1x3"))
            .addOtherStructurePart(casings, GTUtility.translate("gt.structure.pillar.1x3x1.center"))
            .addOtherStructurePart(frameBoxes, GTUtility.translate("gt.structure.pillar.side_and_top"))
            .addEnergyHatch(
                GTUtility.translate(
                    "gt.structure.energy_hatch.tier_base",
                    VN[getMinTier()],
                    GTUtility.translate("gt.structure.any_base_casing")),
                1)
            .addMaintenanceHatch(GTUtility.translate("gt.structure.any_base_casing"), 1)
            .addInputBus(GTUtility.translate("gt.structure.oil_mining_pipes_optional"), 1)
            .addOutputHatch(GTUtility.translate("gt.structure.any_base_casing"), 1)
            .toolTipFinisher();
        return tt;
    }

    protected abstract int getRangeInChunks();

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
        int oldChunkRange = chunkRangeConfig;
        if (aPlayer.isSneaking()) {
            if (chunkRangeConfig > 0) {
                chunkRangeConfig--;
            }
            if (chunkRangeConfig == 0) chunkRangeConfig = getRangeInChunks();
        } else {
            if (chunkRangeConfig <= getRangeInChunks()) {
                chunkRangeConfig++;
            }
            if (chunkRangeConfig > getRangeInChunks()) chunkRangeConfig = 1;
        }
        if (oldChunkRange != chunkRangeConfig) mOilFieldChunks.clear();
        GTUtility.sendChatTrans(aPlayer, "gt.chat.drill_oil.workarea_set.chunks", chunkRangeConfig, chunkRangeConfig);
    }

    @Override
    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mOutputHatches.isEmpty() && mEnergyHatches.size() == 1;
    }

    @Override
    protected List<IHatchElement<? super MTEDrillerBase>> getAllowedHatches() {
        return ImmutableList.of(InputBus, OutputHatch, Maintenance, Energy);
    }

    int batchMultiplier = 1;

    @Override
    protected void setElectricityStats() {
        // for a 6.4 second beautiful batch
        batchMultiplier = batchMode ? 128 : 1;
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(0, GTUtility.getTier(getMaxInputVoltage()));
        this.mEUt = -7 << (tier << 1); // (1/4) A of current tier when at bottom (7/8) A of current tier while mining
        this.mMaxProgresstime = calculateMaxProgressTime(tier) * batchMultiplier;
    }

    @Override
    public int calculateMaxProgressTime(int tier, boolean simulateWorking) {
        return (int) Math.max(
            1,
            (workState == STATE_AT_BOTTOM || simulateWorking
                ? (64 * (chunkRangeConfig * chunkRangeConfig)) >> (getMinTier() - 1)
                : 120) / GTUtility.powInt(2, tier));
    }

    protected float computeSpeed() {
        return .5F + (GTUtility.getTier(getMaxInputVoltage()) - getMinTier()) * .25F;
    }

    @Override
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        switch (tryLowerPipeState(true)) {
            case 0 -> {
                workState = STATE_DOWNWARD;
                setElectricityStats();
                return true;
            }
            case 3 -> {
                workState = STATE_UPWARD;
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
        workState = STATE_UPWARD;
        setShutdownReason(GTUtility.translate("gt.gui.text.ore_drill_exhausted"));
        return true;
    }

    private boolean tryFillChunkList() {
        FluidStack tFluid, tOil;
        if (mOil == null) {
            tFluid = undergroundOilReadInformation(getBaseMetaTileEntity());
            if (tFluid == null) return false;
            mOil = tFluid.getFluid();
        }
        if (debugDriller) {
            GTLog.out.println(" Driller on  fluid = " + mOil == null ? null : mOil.getName());
        }

        tOil = new FluidStack(mOil, 0);

        if (mOilFieldChunks.isEmpty()) {
            ChunkCoordIntPair tChunk = new ChunkCoordIntPair(
                getBaseMetaTileEntity().getXCoord() >> 4,
                getBaseMetaTileEntity().getZCoord() >> 4);
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
                    tFluid = undergroundOil(getBaseMetaTileEntity().getWorld(), xChunk + i, zChunk + j, -1);
                    if (debugDriller) {
                        String fluidName = tFluid == null ? tFluid.getFluid()
                            .getName() : null;
                        GTLog.out.println(" Fluid in chunk = " + fluidName);
                    }
                    if (tOil.isFluidEqual(tFluid) && tFluid.amount > 0) {
                        mOilFieldChunks.add(tChunk);
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
    protected ValidationResult<FluidStack> tryPumpOil(float speed) {
        if (mOil == null) return null;
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
        mOilFlow = pumpedOil.amount * batchMultiplier;
        // Multiblock base already includes 1 parallel
        recipesDone += batchMultiplier - 1;
        return ValidationResult.of(ValidationType.VALID, pumpedOil.amount == 0 ? null : pumpedOil);
    }

    /**
     * @param speed    Speed to pump oil
     * @param simulate If true, it actually does not consume vein
     * @return Fluid pumped
     */
    protected FluidStack pumpOil(@Nonnegative float speed, boolean simulate) {
        if (speed < 0) {
            throw new IllegalArgumentException("Don't pass negative speed");
        }

        FluidStack returnOil = new FluidStack(mOil, 0);
        World world = getBaseMetaTileEntity().getWorld();
        float coefficient = simulate ? -speed : speed;

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
                continue;
            }
            if (returnOil.isFluidEqual(pumped)) {
                returnOil.amount += pumped.amount;
            }
        }
        return returnOil;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_PUMP;
    }

    @Override
    public String[] getInfoData() {
        List<String> l = new ArrayList<>(
            Arrays.asList(
                EnumChatFormatting.BLUE + GTUtility.translate("GT5U.machines.oilfluidpump") + EnumChatFormatting.RESET,
                GTUtility.translate("GT5U.machines.workarea") + ": "
                    + EnumChatFormatting.GREEN
                    + formatNumber(chunkRangeConfig)
                    + " x "
                    + formatNumber(chunkRangeConfig)
                    + EnumChatFormatting.RESET
                    + " "
                    + GTUtility.translate("GT5U.machines.chunks"),
                GTUtility.translate(
                    "GT5U.infodata.oil_drill.drilling_fluid",
                    EnumChatFormatting.GREEN + getFluidName() + EnumChatFormatting.RESET),
                GTUtility.translate(
                    "GT5U.infodata.oil_drill.drilling_flow",
                    EnumChatFormatting.GREEN + formatNumber(getFlowRatePerTick()) + EnumChatFormatting.RESET)));
        l.addAll(Arrays.asList(super.getInfoData()));
        return l.toArray(new String[0]);
    }

    @Override
    public @NotNull List<String> reportMetrics() {
        final boolean machineIsActive = getBaseMetaTileEntity().isActive();
        final String failureReason = getFailureReason()
            .map(reason -> GTUtility.translate("GT5U.gui.text.drill_offline_reason", reason))
            .orElseGet(() -> GTUtility.translate("GT5U.gui.text.drill_offline_generic"));

        if (workState == STATE_AT_BOTTOM) {
            final ImmutableList.Builder<String> builder = ImmutableList.builder();
            builder.add(GTUtility.translate("GT5U.gui.text.pump_fluid_type", getFluidName()));

            if (machineIsActive) {
                builder.add(
                    GTUtility.translate(
                        "GT5U.gui.text.pump_rate.1",
                        EnumChatFormatting.AQUA + numberFormat.format(getFlowRatePerTick()))
                        + GTUtility.translate("GT5U.gui.text.pump_rate.2"),
                    mOilFlow + GTUtility.translate("GT5U.gui.text.pump_recovery.2"));
            } else {
                builder.add(failureReason);
            }

            return builder.build();
        }

        if (machineIsActive) {
            return switch (workState) {
                case STATE_DOWNWARD -> ImmutableList.of(GTUtility.translate("GT5U.gui.text.deploying_pipe"));
                case STATE_UPWARD, STATE_ABORT -> ImmutableList
                    .of(GTUtility.translate("GT5U.gui.text.retracting_pipe"));
                default -> ImmutableList.of();
            };
        }

        return ImmutableList.of(failureReason);
    }

    protected int getFlowRatePerTick() {
        return this.mMaxProgresstime > 0 ? (mOilFlow / this.mMaxProgresstime) : 0;
    }

    @NotNull
    private String getFluidName() {
        if (mOil != null) {
            return mOil.getLocalizedName(new FluidStack(mOil, 0));
        }
        return "None";
    }

    private @NotNull String clientFluidType = "";
    private int clientFlowPerTick = 0;
    private int clientFlowPerOperation = 0;

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> EnumChatFormatting.GRAY
                            + GTUtility.translate("GT5U.gui.text.pump_fluid_type", clientFluidType))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive() && workState == STATE_AT_BOTTOM))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> EnumChatFormatting.GRAY
                            + GTUtility.translate(
                                "GT5U.gui.text.pump_rate.1",
                                EnumChatFormatting.AQUA + numberFormat.format(clientFlowPerTick))
                            + EnumChatFormatting.GRAY
                            + GTUtility.translate("GT5U.gui.text.pump_rate.2"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive() && workState == STATE_AT_BOTTOM))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> EnumChatFormatting.GRAY
                            + GTUtility.translate(
                                "GT5U.gui.text.pump_recovery.1",
                                EnumChatFormatting.AQUA + numberFormat.format(clientFlowPerOperation))
                            + EnumChatFormatting.GRAY
                            + GTUtility.translate("GT5U.gui.text.pump_recovery.2"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive() && workState == STATE_AT_BOTTOM))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> workState, newInt -> workState = newInt))
            .widget(new FakeSyncWidget.StringSyncer(this::getFluidName, newString -> clientFluidType = newString))
            .widget(new FakeSyncWidget.IntegerSyncer(this::getFlowRatePerTick, newInt -> clientFlowPerTick = newInt))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> mOilFlow, newInt -> clientFlowPerOperation = newInt));
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_OIL_DRILL_LOOP;
    }
}
