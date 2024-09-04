package gregtech.common.tileentities.machines.multi;

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
import java.util.List;

import javax.annotation.Nonnegative;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.SoundResource;
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

public abstract class MTEOilDrillBase extends MTEDrillerBase implements IMetricsExporter {

    private final ArrayList<Chunk> mOilFieldChunks = new ArrayList<>();
    private int mOilId = 0;
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
        aNBT.setInteger("mOilId", mOilId);
        aNBT.setInteger("chunkRangeConfig", chunkRangeConfig);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mOilId = aNBT.getInteger("mOilId");
        if (aNBT.hasKey("chunkRangeConfig")) chunkRangeConfig = aNBT.getInteger("chunkRangeConfig");
    }

    protected MultiblockTooltipBuilder createTooltip(String tierSuffix) {
        String casings = getCasingBlockItem().get(0)
            .getDisplayName();

        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Pump")
            .addInfo("Controller Block for the Oil/Gas/Fluid Drilling Rig " + (tierSuffix != null ? tierSuffix : ""))
            .addInfo("Works on " + getRangeInChunks() + "x" + getRangeInChunks() + " chunks")
            .addInfo("Use a Screwdriver to configure range")
            .addInfo("Use Programmed Circuits to ignore near exhausted oil field")
            .addInfo("If total circuit # is greater than output amount it will halt. If it worked right.") // doesn't
            // work
            .addSeparator()
            .beginStructureBlock(3, 7, 3, false)
            .addController("Front bottom")
            .addOtherStructurePart(casings, "form the 3x1x3 Base")
            .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
            .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
            .addEnergyHatch("1x " + VN[getMinTier()] + "+, Any base casing", 1)
            .addMaintenanceHatch("Any base casing", 1)
            .addInputBus("Mining Pipes or Circuits, optional, any base casing", 1)
            .addOutputHatch("Any base casing", 1)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    protected abstract int getRangeInChunks();

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
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
        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal("GT5U.machines.workareaset") + " "
                + chunkRangeConfig
                + "x"
                + chunkRangeConfig
                + StatCollector.translateToLocal("GT5U.machines.chunks")); // TODO Add translation support
    }

    @Override
    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mOutputHatches.isEmpty() && mEnergyHatches.size() == 1;
    }

    @Override
    protected List<IHatchElement<? super MTEDrillerBase>> getAllowedHatches() {
        return ImmutableList.of(InputBus, OutputHatch, Maintenance, Energy);
    }

    @Override
    protected void setElectricityStats() {
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(0, GTUtility.getTier(getMaxInputVoltage()));
        this.mEUt = -7 << (tier << 1); // (1/4) A of current tier when at bottom (7/8) A of current tier while mining
        this.mMaxProgresstime = Math.max(
            1,
            (workState == STATE_AT_BOTTOM ? (64 * (chunkRangeConfig * chunkRangeConfig)) >> (getMinTier() - 1) : 120)
                >> tier);
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
        setShutdownReason(StatCollector.translateToLocal("GT5U.gui.text.drill_exhausted"));
        return true;
    }

    private boolean tryFillChunkList() {
        FluidStack tFluid, tOil;
        if (mOilId <= 0) {
            tFluid = undergroundOilReadInformation(getBaseMetaTileEntity());
            if (tFluid == null) return false;
            mOilId = tFluid.getFluidID();
        }
        if (debugDriller) {
            GTLog.out.println(" Driller on  fluid = " + mOilId);
        }

        tOil = new FluidStack(FluidRegistry.getFluid(mOilId), 0);

        if (mOilFieldChunks.isEmpty()) {
            Chunk tChunk = getBaseMetaTileEntity().getWorld()
                .getChunkFromBlockCoords(getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getZCoord());
            int range = chunkRangeConfig;
            int xChunk = Math.floorDiv(tChunk.xPosition, range) * range; // Java was written by idiots. For negative
                                                                         // values, / returns rounded towards zero.
            // Fucking morons.
            int zChunk = Math.floorDiv(tChunk.zPosition, range) * range;
            if (debugDriller) {
                GTLog.out.println(
                    "tChunk.xPosition = " + tChunk.xPosition
                        + " tChunk.zPosition = "
                        + tChunk.zPosition
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
                    tChunk = getBaseMetaTileEntity().getWorld()
                        .getChunkFromChunkCoords(xChunk + i, zChunk + j);
                    tFluid = undergroundOilReadInformation(tChunk);
                    if (debugDriller) {
                        GTLog.out.println(
                            " Fluid in chunk = " + tFluid.getFluid()
                                .getID());
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
        if (mOilId <= 0) return null;
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

        ArrayList<Chunk> emptyChunks = new ArrayList<>();
        FluidStack returnOil = new FluidStack(FluidRegistry.getFluid(mOilId), 0);

        for (Chunk tChunk : mOilFieldChunks) {
            FluidStack pumped = undergroundOil(tChunk, simulate ? -speed : speed);
            if (debugDriller) {
                GTLog.out.println(
                    " chunkX = " + tChunk.getChunkCoordIntPair().chunkXPos
                        + " chunkZ = "
                        + tChunk.getChunkCoordIntPair().chunkZPos);
                if (pumped != null) {
                    GTLog.out.println("     Fluid pumped = " + pumped.amount);
                } else {
                    GTLog.out.println("     No fluid pumped ");
                }
            }
            if (pumped == null || pumped.amount < 1) {
                emptyChunks.add(tChunk);
                continue;
            }
            if (returnOil.isFluidEqual(pumped)) {
                returnOil.amount += pumped.amount;
            }
        }
        for (Chunk tChunk : emptyChunks) {
            mOilFieldChunks.remove(tChunk);
        }
        return returnOil;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_PUMP_OP;
    }

    @Override
    public String[] getInfoData() {
        List<String> l = new ArrayList<>(
            Arrays.asList(
                EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.machines.oilfluidpump")
                    + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.machines.workarea") + ": "
                    + EnumChatFormatting.GREEN
                    + GTUtility.formatNumbers(chunkRangeConfig)
                    + " x "
                    + GTUtility.formatNumbers(chunkRangeConfig)
                    + EnumChatFormatting.RESET
                    + " "
                    + StatCollector.translateToLocal("GT5U.machines.chunks"),
                "Drilling fluid: " + EnumChatFormatting.GREEN + getFluidName() + EnumChatFormatting.RESET,
                "Drilling flow: " + EnumChatFormatting.GREEN
                    + getFlowRatePerTick()
                    + EnumChatFormatting.RESET
                    + " L/t"));
        l.addAll(Arrays.asList(super.getInfoData()));
        return l.toArray(new String[0]);
    }

    @Override
    public @NotNull List<String> reportMetrics() {
        final boolean machineIsActive = getBaseMetaTileEntity().isActive();
        final String failureReason = getFailureReason()
            .map(reason -> StatCollector.translateToLocalFormatted("GT5U.gui.text.drill_offline_reason", reason))
            .orElseGet(() -> StatCollector.translateToLocalFormatted("GT5U.gui.text.drill_offline_generic"));

        if (workState == STATE_AT_BOTTOM) {
            final ImmutableList.Builder<String> builder = ImmutableList.builder();
            builder.add(StatCollector.translateToLocalFormatted("GT5U.gui.text.pump_fluid_type", getFluidName()));

            if (machineIsActive) {
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

        if (machineIsActive) {
            return switch (workState) {
                case STATE_DOWNWARD -> ImmutableList.of(StatCollector.translateToLocal("GT5U.gui.text.deploying_pipe"));
                case STATE_UPWARD, STATE_ABORT -> ImmutableList
                    .of(StatCollector.translateToLocal("GT5U.gui.text.retracting_pipe"));
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
        if (mOilId > 0) {
            final Fluid fluid = FluidRegistry.getFluid(mOilId);
            return fluid.getLocalizedName(new FluidStack(fluid, 0));
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
                            + StatCollector.translateToLocalFormatted("GT5U.gui.text.pump_fluid_type", clientFluidType))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive() && workState == STATE_AT_BOTTOM))
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
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive() && workState == STATE_AT_BOTTOM))
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
}
