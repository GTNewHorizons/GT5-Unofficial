package gregtech.api.ec.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.metatileentity.implementations.MTEMultiBlockBase.INTERRUPT_SOUND_INDEX;
import static gregtech.api.util.GTUtility.*;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;

import javax.annotation.Nonnegative;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.collect.Lists;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureWalker;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.ec.IECMultiblock;
import gregtech.api.ec.OrderedSectionBuilder;
import gregtech.api.ec.component.IActivitySoundLoopComponent;
import gregtech.api.ec.component.IOnMachineStopComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.IScannerInfoComponent;
import gregtech.api.ec.component.IWailaBodyProviderComponent;
import gregtech.api.ec.component.IWailaDataComponent;
import gregtech.api.ec.component.gui.MultiblockGuiBuilder;
import gregtech.api.ec.component.multiblock.IControllerPartComponent;
import gregtech.api.ec.component.multiblock.IDualInputLogicComponent;
import gregtech.api.ec.component.multiblock.IHatchContainerComponent;
import gregtech.api.ec.component.multiblock.IMaintenanceLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IPostStructureCheckComponent;
import gregtech.api.ec.component.multiblock.IRecipeLockingComponent;
import gregtech.api.ec.component.multiblock.IStructureCheckHandlerComponent;
import gregtech.api.ec.component.multiblock.IStructureLogicComponent;
import gregtech.api.ec.component.multiblock.IVoidingConfigComponent;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.IOutputHatch;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMultiblockController;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.structure.StructureChecker;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtil;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.ec.multiblock.SmartHatchLogicComponent;
import gregtech.common.ec.multiblock.StructureLogicComponent;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockECBaseGui;
import gregtech.common.pollution.Pollution;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;
import gregtech.common.tileentities.machines.RecipeCheckReason;
import io.netty.buffer.ByteBuf;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTEMultiBlockECBase<T extends MTEMultiBlockECBase<T>> extends MetaTileEntityECBase
    implements IECMultiblock, IMultiblockController, IAlignment, ISurvivalConstructable, ISecondaryDescribable {

    protected static final int SURVIVAL_BUILD_DONE = -1;
    protected static final int SURVIVAL_BUILD_UNSUPPORTED = -2;
    static {
        if (GTUtility.isClient()) {
            addTooltipResetListener();
        }
    }

    private ExtendedFacing mExtendedFacing = ExtendedFacing.DEFAULT;
    private IAlignmentLimits mLimits = getInitialAlignmentLimits();
    private final StructureCenterWalker centerWalker = new StructureCenterWalker();
    private final Vector3f center = new Vector3f();
    private int structureRadius;
    protected long mTotalRunTime = 0;

    private static final AtomicReferenceArray<MultiblockTooltipBuilder> tooltips = new AtomicReferenceArray<>(
        GregTechAPI.METATILEENTITIES.length);

    public MTEMultiBlockECBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 2);
    }

    public MTEMultiBlockECBase(String aName) {
        super(aName, 2);
    }

    protected IStructureLogicComponent structureLogic;
    protected @Nullable IMaintenanceLogicComponent maintenanceLogic;
    protected @Nullable IRecipeLogicComponent recipeLogic;

    protected void onComponentsReady() {
        super.onComponentsReady();
        structureLogic = getComponent(IStructureLogicComponent.KEY);
        maintenanceLogic = tryGetComponent(IMaintenanceLogicComponent.KEY);
        recipeLogic = tryGetComponent(IRecipeLogicComponent.KEY);
    }

    private class ScanHandler extends BaseMultiblockComponent
        implements IStructureCheckHandlerComponent, IPostStructureCheckComponent {

        @Override
        public void checkMachine(@NotNull IGregTechTileEntity igte, @NotNull List<StructureError> errors) {
            MTEMultiBlockECBase.this.checkMachine(igte, getControllerSlot(), errors);
        }

        @Override
        public StructureCheckPhase getStructureCheckPhase() {
            return StructureCheckPhase.SCAN;
        }

        @Override
        public int getStructureCheckPriority() {
            return StructureCheckPriority.MACHINE_CHECK.priority;
        }

        @Override
        public void onPostStructureCheck(@NotNull IGregTechTileEntity igte, @NotNull List<StructureError> errors) {
            StructureSize size = centerWalker.finish();

            if (size != null) {
                center.set(size.centerX, size.centerY, size.centerZ);
                structureRadius = size.radius;
            } else {
                center.set(igte.getXCoord(), igte.getYCoord(), igte.getZCoord());
                structureRadius = 1;
            }
        }

        @Override
        public int getPostStructureCheckPriority() {
            return PostStructureCheckPriority.STRUCTURE_BOUNDS.priority;
        }
    }

    private class CheckHandler extends BaseMultiblockComponent implements IStructureCheckHandlerComponent {

        @Override
        public void checkMachine(@NotNull IGregTechTileEntity igte, @NotNull List<StructureError> errors) {
            MTEMultiBlockECBase.this.verifyMachine(igte, getControllerSlot(), errors);
        }

        @Override
        public StructureCheckPhase getStructureCheckPhase() {
            return StructureCheckPhase.VERIFY;
        }

        @Override
        public int getStructureCheckPriority() {
            return StructureCheckPriority.MACHINE_CHECK.priority;
        }
    }

    protected void registerComponents() {
        addComponent(new StructureLogicComponent());
        addComponent(new ScanHandler());
        addComponent(new CheckHandler());
        addComponent(new SmartHatchLogicComponent());
        MultiblockComponentBuilder builder = new MultiblockComponentBuilder();
        configureComponents(builder);
        builder.build(this, this::createProcessingLogic, this::getRecipeMap);
    }

    protected void configureComponents(@NotNull MultiblockComponentBuilder builder) {}

    @SideOnly(Side.CLIENT)
    private static void addTooltipResetListener() {
        SimpleReloadableResourceManager manager = (SimpleReloadableResourceManager) Minecraft.getMinecraft()
            .getResourceManager();

        manager.reloadListeners.add(resourceManager -> {
            final int len = tooltips.length();

            for (int i = 0; i < len; i++) {
                tooltips.set(i, null);
            }
        });
    }

    protected abstract MultiblockTooltipBuilder createTooltip();

    protected final MultiblockTooltipBuilder getTooltip() {
        int tId = getBaseMetaTileEntity().getMetaTileID();
        MultiblockTooltipBuilder tooltip = tooltips.get(tId);
        if (tooltip == null) {
            tooltip = createTooltip();
            tooltips.set(tId, tooltip);
        }
        return tooltip;
    }

    @Override
    public final String[] getDescription() {
        return getCurrentDescription();
    }

    @Override
    public String getLocalName() {
        return super.getLocalName();
    }

    @Override
    public final String[] getPrimaryDescription() {
        return getTooltip().getInformation();
    }

    @Override
    public final String[] getSecondaryDescription() {
        return getTooltip().getStructureInformation();
    }

    @Override
    public final boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ITexture[][] getInventoryTextures() {
        return getOrCreateInventoryTextures();
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex > 0;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("mTotalRunTime", mTotalRunTime);
        aNBT.setBoolean("makePowerfailEvents", makePowerfailEvents);
        aNBT.setByte(
            "eRotation",
            (byte) mExtendedFacing.getRotation()
                .getIndex());
        aNBT.setByte(
            "eFlip",
            (byte) mExtendedFacing.getFlip()
                .getIndex());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mTotalRunTime = aNBT.getLong("mTotalRunTime");
        makePowerfailEvents = !aNBT.hasKey("makePowerfailEvents") || aNBT.getBoolean("makePowerfailEvents");
        mExtendedFacing = ExtendedFacing.of(
            getBaseMetaTileEntity().getFrontFacing(),
            Rotation.byIndex(aNBT.getByte("eRotation")),
            Flip.byIndex(aNBT.getByte("eFlip")));
        if (!getAlignmentLimits().isNewExtendedFacingValid(mExtendedFacing)) {
            mExtendedFacing = getCorrectedAlignment(mExtendedFacing);
        }
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isServerSide() && GTUtil.saveMultiblockInputConfiguration(this, aPlayer)) {
            aPlayer.addChatComponentMessage(new ChatComponentTranslation("GT5U.MULTI_MACHINE_CONFIG.SAVE"));
            return;
        }
        super.onLeftclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (GTUtil.hasMultiblockInputConfiguration(aPlayer.getHeldItem())) {
            if (aBaseMetaTileEntity.isServerSide()) {
                if (GTUtil.loadMultiblockInputConfiguration(this, aPlayer)) {
                    aPlayer.addChatComponentMessage(new ChatComponentTranslation("GT5U.MULTI_MACHINE_CONFIG.LOAD"));
                } else {
                    aPlayer
                        .addChatComponentMessage(new ChatComponentTranslation("GT5U.MULTI_MACHINE_CONFIG.LOAD.FAIL"));
                }
            }
            return true;
        }
        openGui(aPlayer);
        return true;
    }

    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.WrenchLevel2.toTileEntityBaseType();
    }

    @Override
    public void onMachineBlockUpdate() {
        structureLogic.markStructureDirty();
    }

    @Override
    public long getTotalRunTime() {
        return mTotalRunTime;
    }

    public void setTotalRunTime(long mTotalRunTime) {
        this.mTotalRunTime = mTotalRunTime;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // Time Counter
            mTotalRunTime++;
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean isFormed() {
        return structureLogic.isFormed();
    }

    @Override
    public void scheduleRecipeCheck(RecipeCheckReason reason) {
        if (recipeLogic != null) recipeLogic.scheduleRecipeCheck(reason);
    }

    @Override
    protected void onUnhandledDoSound(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == INTERRUPT_SOUND_INDEX) {
            GTUtility.doSoundAtClient(SoundResource.IC2_MACHINES_INTERRUPT_ONE, 100, 1.0F, aX, aY, aZ);
        }
    }

    /**
     * Check the ItemStack in the controller slot.
     *
     * @param aStack the ItemStack in the controller slot.
     * @return {@code true} if the item is valid for this machine or the machine doesn't have restrictions on the
     *         controller slot.
     */
    public boolean isCorrectMachinePart(@Nullable ItemStack aStack) {
        for (var comp : getComponents(IControllerPartComponent.KEY)) {
            if (!comp.isCorrectMachinePart(aStack)) return false;
        }
        return true;
    }

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     * Any error must be added to the error list.
     */
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (errors.isEmpty()) {
            // Only add this if we do not emit any diagnostics.
            errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        }
    }

    /**
     * Verifies structure requirements.
     * Any error must be added to the error list.
     */
    public void verifyMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {}

    /**
     * @deprecated Use {@link #stopMachine(ShutDownReason)}
     */
    @Deprecated
    public void stopMachine() {
        stopMachine(ShutDownReasonRegistry.NONE);
    }

    /**
     * @deprecated Use {@link #stopMachine(ShutDownReason)}
     */
    @Deprecated
    public void criticalStopMachine() {
        stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
    }

    public void stopMachine(@NotNull ShutDownReason reason) {
        if (!ShutDownReasonRegistry.isRegistered(reason.getID())) {
            throw new RuntimeException(String.format("Reason %s is not registered for registry", reason.getID()));
        }
        for (var component : getComponents(IOnMachineStopComponent.KEY)) {
            component.onMachineStop(reason);
        }
        IGregTechTileEntity igte = getBaseMetaTileEntity();
        if (igte != null) {
            igte.setShutDownReason(reason);
            igte.setShutdownStatus(true);
            igte.disableWorking();
            igte.setActive(false);
        }
        if (reason.wasCritical()) {
            sendSound(INTERRUPT_SOUND_INDEX);
        }
        if (makePowerfailEvents && reason == ShutDownReasonRegistry.POWER_LOSS) {
            GTMod.proxy.powerfailTracker.createPowerfailEvent(igte);
        }
    }

    public int getRepairStatus() {
        return maintenanceLogic != null ? maintenanceLogic.getRepairStatus() : 6;
    }

    public int getIdealStatus() {
        return maintenanceLogic != null ? maintenanceLogic.getIdealStatus() : 6;
    }

    public void explodeMultiblock() {
        GTLog.writeExplosionLog(this, "MultiBlockExplosion");

        Pollution.addPollution(getBaseMetaTileEntity(), GTMod.proxy.mPollutionOnExplosion);
        mInventory[1] = null;
        for (var component : getComponents(IHatchContainerComponent.KEY)) {
            component.explodeAllHatches(V[8]);
        }
        getBaseMetaTileEntity().doExplosion(V[8]);
    }

    /**
     * Drains fluid from the given hatch, including {@link IDualInputHatch}.
     * Note that you should not modify any hatch content during recipe check as it will
     * lead to possible failed extraction when the recipe processing tries to extract
     * the recipe from the hatch. Doing simulate check is fine, however.
     *
     * @param doDrain If false, fluid will not actually be consumed
     * @return Whether the hatch contains enough fluid to drain
     */
    public boolean drain(MTEHatch hatch, FluidStack fluid, boolean doDrain) {
        if (fluid == null || hatch == null) return false;
        if (hasComponent(IDualInputLogicComponent.KEY) && hatch instanceof IDualInputHatch tHatch
            && tHatch.supportsFluids()) {
            Optional<IDualInputInventory> inventory = tHatch.getFirstNonEmptyInventory();
            if (inventory.isPresent()) {
                for (FluidStack storedFluid : Lists.newArrayList(
                    inventory.get()
                        .getFluidInputs())) {
                    if (fluid.isFluidEqual(storedFluid)) {
                        if (doDrain) {
                            long storedFluidAmount = GTUtility.getFluidAmount(storedFluid);
                            long fluidAmount = GTUtility.getFluidAmount(fluid);
                            GTUtility.setFluidAmount(storedFluid, Math.max(storedFluidAmount - fluidAmount, 0));
                        }
                        return GTUtility.getFluidAmount(storedFluid) >= GTUtility.getFluidAmount(fluid);
                    }
                }
            }
        }

        if (hatch instanceof MTEHatchInput tHatch && tHatch.isValid()) {
            FluidStack tFluid = tHatch.drain(ForgeDirection.UNKNOWN, fluid, doDrain);
            return tFluid != null && GTUtility.getFluidAmount(tFluid) >= GTUtility.getFluidAmount(fluid);
        }

        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    /**
     * Creates logic to run recipe check based on recipemap. This runs only once, on class instantiation.
     * <p>
     * If this machine doesn't use recipemap or does some complex things, override @link #checkProcessing().
     */
    @ApiStatus.OverrideOnly
    protected @NotNull ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic();
    };

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    protected void registerDefaultScannerInfo(@NotNull OrderedSectionBuilder<Consumer<List<String>>> builder) {
        builder.add(IScannerInfoComponent.Section.OWNER, infoData -> {
            IGregTechTileEntity igte = getBaseMetaTileEntity();
            if (igte != null) {
                infoData.add(
                    StatCollector.translateToLocalFormatted("GT5U.multiblock.scanner.owned_by", igte.getOwnerName()));

            }
        })
            .add(IScannerInfoComponent.Section.MTE_ID, infoData -> {
                IGregTechTileEntity igte = getBaseMetaTileEntity();
                if (igte == null) return;
                if (igte.getMetaTileEntity() != null) {
                    infoData.add(
                        StatCollector
                            .translateToLocalFormatted("GT5U.multiblock.scanner.meta_tile_entity", igte.getMetaTileID())
                            + " "
                            + StatCollector.translateToLocal(
                                igte.canAccessData() ? "GT5U.multiblock.scanner.valid"
                                    : "GT5U.multiblock.scanner.invalid"));
                } else {
                    infoData.add(StatCollector.translateToLocal("GT5U.multiblock.scanner.is_meta_tile_entity"));
                }
            })
            .add(IScannerInfoComponent.Section.TOTAL_RUN_TIME, infoData -> {
                long seconds = (this.mTotalRunTime / 20);
                long minutes = TimeUnit.SECONDS.toMinutes(seconds);
                long hours = TimeUnit.SECONDS.toHours(seconds);
                // spotless:off
            String timeValue =
                hours > 0 ? String.valueOf(hours)
                    : minutes > 0 ? String.valueOf(minutes)
                      : seconds > 0 ? String.valueOf(seconds)
                        : String.valueOf(this.mTotalRunTime);
            String timeKey =
                hours > 0 ? "GT5U.multiblock.scanner.totalRunHours"
                    : minutes > 0 ? "GT5U.multiblock.scanner.totalRunMinutes"
                      : seconds > 0 ? "GT5U.multiblock.scanner.totalRunSeconds"
                        : "GT5U.multiblock.scanner.totalRunTicks";
            // spotless:on
                infoData.add(StatCollector.translateToLocalFormatted(timeKey, timeValue));
            });
    }

    @Override
    public String[] getInfoData() {
        OrderedSectionBuilder<Consumer<List<String>>> builder = new OrderedSectionBuilder<>();
        registerDefaultScannerInfo(builder);
        for (var component : getComponents(IScannerInfoComponent.KEY)) {
            component.appendInfoData(builder);
        }
        appendInfoData(builder);
        List<String> infoData = new ArrayList<>();
        builder.forEach(consumer -> consumer.accept(infoData));
        return infoData.toArray(new String[0]);
    }

    protected void appendInfoData(@NotNull OrderedSectionBuilder<Consumer<List<String>>> builder) {}

    @Override
    public Map<String, String> getInfoMap() {
        Map<String, String> infoMap = new HashMap<>();
        for (var component : getComponents(IScannerInfoComponent.KEY)) {
            component.appendInfoMap(infoMap);
        }
        return infoMap;
    }

    public void configureGui(@NotNull MultiblockGuiBuilder builder) {}

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return supportsSlotAutomation(aIndex);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return supportsSlotAutomation(aIndex);
    }

    protected boolean showProgressInWaila() {
        return recipeLogic != null;
    }

    protected void registerDefaultWailaBody(
        @NotNull OrderedSectionBuilder<IWailaBodyProviderComponent.WailaBodyProvider> builder) {
        builder.add(IWailaBodyProviderComponent.Section.STATUS, (ctx, currentTip) -> {
            var tag = ctx.tag();
            String efficiency = tag.hasKey("efficiency")
                ? RESET
                    + translateToLocalFormatted("GT5U.waila.multiblock.status.efficiency", tag.getFloat("efficiency"))
                : "";
            if (tag.getBoolean("hasProblems")) {
                currentTip.add(RED + translateToLocal("GT5U.waila.multiblock.status.has_problem") + efficiency);
            } else if (!tag.getBoolean("incompleteStructure")) {
                currentTip.add(GREEN + translateToLocal("GT5U.waila.multiblock.status.running_fine") + efficiency);
            }
        })
            .add(IWailaBodyProviderComponent.Section.ENERGY, (ctx, currentTip) -> {
                var tag = ctx.tag();
                boolean isActive = tag.getBoolean("isActive");
                if (isActive) {
                    long energyTier = tag.getLong("energyTier");
                    long actualEnergyUsage = tag.getLong("energyUsage");
                    if (energyTier > 0) {
                        if (actualEnergyUsage > 0) {
                            currentTip.add(
                                translateToLocalFormatted(
                                    "GT5U.waila.energy.use_with_amperage",
                                    formatNumber(actualEnergyUsage),
                                    GTUtility.getAmperageForTier(actualEnergyUsage, (byte) energyTier),
                                    GTUtility.getColoredTierNameFromTier((byte) energyTier)));
                        } else if (actualEnergyUsage < 0) {
                            currentTip.add(
                                translateToLocalFormatted(
                                    "GT5U.waila.energy.produce_with_amperage",
                                    formatNumber(-actualEnergyUsage),
                                    GTUtility.getAmperageForTier(-actualEnergyUsage, (byte) energyTier),
                                    GTUtility.getColoredTierNameFromTier((byte) energyTier)));
                        }
                    } else {
                        if (actualEnergyUsage > 0) {
                            currentTip.add(
                                translateToLocalFormatted(
                                    "GT5U.waila.energy.use",
                                    formatNumber(actualEnergyUsage),
                                    GTUtility.getColoredTierNameFromVoltage(actualEnergyUsage)));
                        } else if (actualEnergyUsage < 0) {
                            currentTip.add(
                                translateToLocalFormatted(
                                    "GT5U.waila.energy.produce",
                                    formatNumber(-actualEnergyUsage),
                                    GTUtility.getColoredTierNameFromVoltage(-actualEnergyUsage)));
                        }
                    }
                }
            })
            .add(IWailaBodyProviderComponent.Section.OUTPUTS, (ctx, currentTip) -> {
                var tag = ctx.tag();
                boolean isActive = tag.getBoolean("isActive");
                boolean isLockedToRecipe = tag.getBoolean("isLockedToRecipe");
                String lockedRecipe = tag.getString("lockedRecipeName");

                if (!isActive && isLockedToRecipe && !lockedRecipe.isEmpty()) {
                    // Display locked recipe when the machine is idle
                    currentTip.add(translateToLocal("GT5U.waila.multiblock.status.locked_recipe"));
                    String[] lines = lockedRecipe.split("\n");
                    currentTip.addAll(Arrays.asList(lines));
                } else if (isActive) {
                    int outputItemLength = tag.getInteger("outputItemLength");
                    int outputFluidLength = tag.getInteger("outputFluidLength");
                    int totalOutputs = outputItemLength + outputFluidLength;

                    if (totalOutputs > 0) {
                        currentTip.add(translateToLocal("GT5U.waila.producing"));
                        if (isLockedToRecipe) {
                            currentTip.add(translateToLocal("GT5U.waila.multiblock.status.locked_recipe"));
                        }
                        for (int i = 0; i < min(3, outputItemLength); i++) {
                            // Localize on the client: the NBT holds the raw stack, not the display name
                            ItemStack outputStack = ItemStack
                                .loadItemStackFromNBT(tag.getCompoundTag("outputItemStack" + i));
                            String itemName = outputStack != null ? outputStack.getDisplayName() : "";
                            currentTip.add(
                                "  " + tag.getString("outputItemIcon" + i)
                                    + EnumChatFormatting.AQUA
                                    + itemName
                                    + EnumChatFormatting.RESET
                                    + " x "
                                    + EnumChatFormatting.GOLD
                                    + formatNumber(tag.getInteger("outputItemCount" + i)));
                        }
                        for (int i = 0; i < min(3 - outputItemLength, outputFluidLength); i++) {
                            // Localize on the client: the NBT holds the internal fluid name, not the display name
                            String internalName = tag.getString("outputFluidName" + i);
                            Fluid fluid = FluidRegistry.getFluid(internalName);
                            String fluidName = fluid != null ? new FluidStack(fluid, 1).getLocalizedName()
                                : internalName;
                            currentTip.add(
                                "  " + tag.getString("outputFluidIcon" + i)
                                    + EnumChatFormatting.AQUA
                                    + fluidName
                                    + EnumChatFormatting.RESET
                                    + " x "
                                    + EnumChatFormatting.GOLD
                                    + formatNumber(tag.getLong("outputFluidCount" + i))
                                    + "L");
                        }
                        if (totalOutputs > 3) {
                            currentTip.add(
                                translateToLocalFormatted(
                                    "GT5U.waila.producing.andmore",
                                    formatNumber((totalOutputs - 3))));
                        }
                    }
                }
            })
            .add(IWailaBodyProviderComponent.Section.PROGRESS, (ctx, currentTip) -> {
                var tag = ctx.tag();
                boolean isActive = tag.getBoolean("isActive");
                // disables progress time display if recipe text is hidden
                if (showProgressInWaila()) {
                    currentTip.add(
                        GTWaila.getMachineProgressString(
                            isActive,
                            tag.getBoolean("isAllowedToWork"),
                            tag.getInteger("maxProgress"),
                            tag.getInteger("progress")));
                }
            })
            .add(IWailaBodyProviderComponent.Section.CPU_LOAD, (ctx, currentTip) -> {
                var tag = ctx.tag();
                // Show ns on the tooltip
                if (GTMod.proxy.wailaAverageNS && tag.hasKey("averageNS")) {
                    int tAverageTime = tag.getInteger("averageNS");
                    currentTip.add(
                        translateToLocalFormatted("GT5U.waila.multiblock.status.cpu_load", formatNumber(tAverageTime)));
                }
            })
            .add(IWailaBodyProviderComponent.Section.PARALLEL, (ctx, currentTip) -> {
                var tag = ctx.tag();
                final int localMaxParallels = tag.getInteger("maxParallelRecipes");
                if (localMaxParallels > 1) {
                    String tooltip;
                    if (tag.hasKey("powerPanelMaxParallel")) {
                        tooltip = translateToLocalFormatted(
                            "GT5U.multiblock.parallelism_override",
                            tag.getInteger("powerPanelMaxParallel"),
                            localMaxParallels);
                    } else {
                        tooltip = translateToLocal("GT5U.multiblock.parallelism") + ": "
                            + EnumChatFormatting.WHITE
                            + localMaxParallels;
                    }

                    currentTip.add(tooltip);
                }
            });
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();

        if (tag.getBoolean("incompleteStructure")) {
            currentTip.add(RED + translateToLocalFormatted("GT5U.waila.multiblock.status.incomplete") + RESET);
            super.getWailaBody(itemStack, currentTip, accessor, config);
            return;
        }

        OrderedSectionBuilder<IWailaBodyProviderComponent.WailaBodyProvider> builder = new OrderedSectionBuilder<>();
        registerDefaultWailaBody(builder);
        for (var component : getComponents(IWailaBodyProviderComponent.KEY)) {
            component.appendWailaBody(builder);
        }
        appendWailaBody(builder);
        var ctx = new IWailaBodyProviderComponent.WailaBodyContext(itemStack, tag, accessor, config);
        builder.forEach(provider -> provider.append(ctx, currentTip));
        getExtraWailaBody(itemStack, currentTip, tag, accessor, config);
        super.getWailaBody(itemStack, currentTip, accessor, config);
    }

    protected void appendWailaBody(
        @NotNull OrderedSectionBuilder<IWailaBodyProviderComponent.WailaBodyProvider> builder) {}

    public final void getMTEWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setBoolean("incompleteStructure", !isFormed());

        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setBoolean("isActive", tileEntity.isActive());
            tag.setBoolean("isAllowedToWork", tileEntity.isAllowedToWork());
        }

        final GTClientPreference preference = GTMod.proxy.getClientPreference(player.getUniqueID());
        if (preference != null && preference.isWailaAverageNSEnabled()) {
            getBaseMetaTileEntity().startTimeStatistics();
            int tAverageTime = 0;
            int amountOfZero = 0;
            for (int tTime : this.getBaseMetaTileEntity()
                .getTimeStatistics()) {
                tAverageTime += tTime;
                if (tTime == 0) {
                    amountOfZero += 1;
                }
            }

            // tick time zero means it has not been updated yet
            int samples = getBaseMetaTileEntity().getTimeStatistics().length - amountOfZero;
            if (samples > 0) {
                tag.setInteger("averageNS", tAverageTime / samples);
            }
        }

        for (var component : getComponents(IWailaDataComponent.KEY)) {
            component.appendWailaNBTData(player, tile, tag, world, x, y, z);
        }

        getExtraWailaNBT(player, tile, tag, world, x, y, z);
    }

    public void getExtraWailaNBT(EntityPlayerMP playerMP, TileEntity tileEntity, NBTTagCompound tag, World world, int x,
        int y, int z) {}

    @Deprecated
    public void getExtraWailaBody(ItemStack itemStack, List<String> list, NBTTagCompound tag,
        IWailaDataAccessor accessor, IWailaConfigHandler config) {}

    @Override
    public final boolean isDeprecated() {
        return getTooltip().isDeprecated();
    }

    @Override
    protected void onGuiOpened(EntityPlayer player) {
        super.onGuiOpened(player);

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte != null && igte.isServerSide()) {
            GTMod.proxy.powerfailTracker.removePowerfailEvents(igte);
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        IGregTechTileEntity igte = getBaseMetaTileEntity();
        if (igte != null && igte.isServerSide()) {
            GTMod.proxy.powerfailTracker.removePowerfailEvents(igte);
        }
    }

    /**
     * Checks if all the item / fluid outputs of the recipe can be outputted to the buses / hatches. If void protection
     * is enabled, it also checks for {@link #protectsExcessItem()} and {@link #protectsExcessFluid()}, so you don't
     * need to call them along with this method.
     * <p>
     * If you're using {@link ParallelHelper}, it will handle void protection and return 0 parallel if all the output
     * cannot be dumped into buses / hatches. In that case you won't use this method.
     */
    protected boolean canOutputAll(@NotNull GTRecipe recipe) {
        return canOutputAll(recipe.mOutputs, recipe.mFluidOutputs);
    }

    /**
     * Checks if all the items can be outputted to the output buses. If void protection is enabled, it also checks for
     * {@link #protectsExcessItem()}, so you don't need to call it along with this method.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean canOutputAll(ItemStack[] items) {
        return canOutputAll(items, null);
    }

    /**
     * Checks if all the fluids can be outputted to the output hatches. If void protection is enabled, it also checks
     * for {@link #protectsExcessFluid()}, so you don't need to call it along with this method.
     */
    protected boolean canOutputAll(FluidStack[] fluids) {
        return canOutputAll(null, fluids);
    }

    /**
     * Checks if all the items / fluids can be outputted to output buses / hatches. If void protection is enabled, it
     * also checks for {@link #protectsExcessItem()} and {@link #protectsExcessFluid()}, so you don't need to call them
     * along with this method.
     */
    protected boolean canOutputAll(@Nullable ItemStack[] items, @Nullable FluidStack[] fluids) {
        if (!protectsExcessItem() && !protectsExcessFluid()) {
            return true;
        }

        VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper().setMachine(this)
            .setItemOutputs(items)
            .setFluidOutputs(fluids)
            .build();
        return voidProtectionHelper.getMaxParallel() > 0;
    }

    @Override
    public boolean isAllowedToWork() {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        return baseMetaTileEntity != null && baseMetaTileEntity.isAllowedToWork();
    }

    public boolean isMuffled() {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity != null) {
            return baseMetaTileEntity.isMuffled();
        }
        return false;
    }

    public void setMuffled(boolean value) {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity != null) {
            baseMetaTileEntity.setMuffler(value);
        }
    }

    public ItemStack getControllerSlot() {
        return mInventory[getControllerSlotIndex()];
    }

    public final int getControllerSlotIndex() {
        return 1;
    }

    // True if the slot with index aSlot may be interacted with through automation
    protected boolean supportsSlotAutomation(int aSlot) {
        return false;
    }

    public int getStructureUpdateTime() {
        return structureLogic.getStructureUpdateTime();
    }

    public void setStructureUpdateTime(int time) {
        structureLogic.setStructureUpdateTime(time);
    }

    @Override
    public boolean isRecipeLockingEnabled() {
        var recipeLocking = tryGetComponent(IRecipeLockingComponent.KEY);
        return recipeLocking != null && recipeLocking.isRecipeLockingEnabled();
    }

    @Override
    public void setRecipeLocking(boolean enabled) {
        var recipeLocking = tryGetComponent(IRecipeLockingComponent.KEY);
        if (recipeLocking != null) recipeLocking.setRecipeLocking(enabled);
    }

    @Override
    public void setSingleRecipeCheck(SingleRecipeCheck recipeCheck) {
        var recipeLocking = tryGetComponent(IRecipeLockingComponent.KEY);
        if (recipeLocking != null) recipeLocking.setSingleRecipeCheck(recipeCheck);
    }

    @Override
    public SingleRecipeCheck getSingleRecipeCheck() {
        var recipeLocking = tryGetComponent(IRecipeLockingComponent.KEY);
        return recipeLocking != null ? recipeLocking.getSingleRecipeCheck() : null;
    }

    @Override
    public int getGUIWidth() {
        return 198;
    }

    @Override
    public int getGUIHeight() {
        return 192;
    }

    // Until other features are implemented, this will be the same as supporting parallel.
    public boolean supportsPowerPanel() {
        return true;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    public boolean supportsInventoryRow() {
        return this.doesBindPlayerInventory();
    }

    public boolean shouldDisplayShutDownReason() {
        return true;
    }

    @Override
    public int getStartUpCheckTimer() {
        return structureLogic.getStartUpCheckTimer();
    }

    @Override
    public final ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return getGui().build(guiData, syncManager, uiSettings);
    }

    protected @NotNull MTEMultiBlockECBaseGui<?> getGui() {
        return new MTEMultiBlockECBaseGui<>((T) this);
    }

    // todo remove it
    public boolean usesTurbines() {
        return false;
    }

    public boolean canBeMuffled() {
        return true;
    }

    private boolean makePowerfailEvents = true;

    public boolean makesPowerfailEvents() {
        return makePowerfailEvents;
    }

    public void setPowerfailEventCreationStatus(boolean status) {
        makePowerfailEvents = status;
    }

    @Override
    public void setControllerSlot(@Nullable ItemStack item) {
        mInventory[getControllerSlotIndex()] = item;
    }

    @Nullable
    public Vector3f getCenter() {
        return center;
    }

    @Nonnegative
    public int getApproximateRadius() {
        return structureRadius;
    }

    @Override
    public void setExtendedFacing(ExtendedFacing newExtendedFacing) {
        if (mExtendedFacing != newExtendedFacing) {
            mExtendedFacing = newExtendedFacing;
            final IGregTechTileEntity base = getBaseMetaTileEntity();
            structureLogic.markStructureDirty();
            if (base.isServerSide() && !GregTechAPI.isDummyWorld(base.getWorld())) {
                StructureLibAPI.sendAlignment(
                    (IAlignmentProvider) base,
                    new NetworkRegistry.TargetPoint(
                        base.getWorld().provider.dimensionId,
                        base.getXCoord(),
                        base.getYCoord(),
                        base.getZCoord(),
                        512));
            } else {
                base.issueTextureUpdate();
            }
        }
    }

    @Override
    public final boolean isFacingValid(ForgeDirection facing) {
        return canSetToDirectionAny(facing);
    }

    @Override
    public boolean onUnhandledWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer entityPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        if (wrenchingSide != getBaseMetaTileEntity().getFrontFacing())
            return super.onUnhandledWrenchRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool);
        if (entityPlayer.isSneaking()) {
            if (isFlipChangeAllowed()) {
                toolSetFlip(getFlip().isHorizontallyFlipped() ? Flip.NONE : Flip.HORIZONTAL);
            } else {
                return false;
            }
        } else {
            if (isRotationChangeAllowed()) {
                toolSetRotation(null);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public Set<VoidingMode> getAllowedVoidingModes() {
        var voidingConfig = tryGetComponent(IVoidingConfigComponent.KEY);
        return voidingConfig != null ? voidingConfig.getAllowedVoidingModes()
            : IECMultiblock.super.getAllowedVoidingModes();
    }

    @Override
    public VoidingMode getVoidingMode() {
        var voidingConfig = tryGetComponent(IVoidingConfigComponent.KEY);
        return voidingConfig != null ? voidingConfig.getVoidingMode() : getDefaultVoidingMode();
    }

    @Override
    public void setVoidingMode(VoidingMode mode) {
        var voidingConfig = tryGetComponent(IVoidingConfigComponent.KEY);
        if (voidingConfig != null) voidingConfig.setVoidingMode(mode);
    }

    @Override
    public List<IOutputBus> getOutputBusses() {
        var itemOutputLogic = tryGetComponent(IMultiblockItemOutputLogicComponent.KEY);
        return itemOutputLogic != null ? itemOutputLogic.getOutputBusses() : Collections.emptyList();
    }

    @Override
    public List<IOutputHatch> getOutputHatches(@NotNull List<FluidStack> toOutput) {
        var fluidOutputLogic = tryGetComponent(IMultiblockFluidOutputLogicComponent.KEY);
        return fluidOutputLogic != null ? fluidOutputLogic.getOutputHatches(toOutput) : Collections.emptyList();
    }

    @Override
    public int getItemOutputLimit() {
        var itemOutputLogic = tryGetComponent(IMultiblockItemOutputLogicComponent.KEY);
        return itemOutputLogic != null ? itemOutputLogic.getItemOutputLimit()
            : IECMultiblock.super.getItemOutputLimit();
    }

    @Override
    public int getFluidOutputLimit() {
        var fluidOutputLogic = tryGetComponent(IMultiblockFluidOutputLogicComponent.KEY);
        return fluidOutputLogic != null ? fluidOutputLogic.getFluidOutputLimit()
            : IECMultiblock.super.getFluidOutputLimit();
    }

    @Override
    public boolean canDumpItemToME(List<GTUtility.ItemId> outputs) {
        var itemOutputLogic = tryGetComponent(IMultiblockItemOutputLogicComponent.KEY);
        return itemOutputLogic != null && itemOutputLogic.canDumpItemToME(outputs);
    }

    @Override
    public boolean canDumpFluidToME(List<GTUtility.FluidId> outputs) {
        var fluidOutputLogic = tryGetComponent(IMultiblockFluidOutputLogicComponent.KEY);
        return fluidOutputLogic != null && fluidOutputLogic.canDumpFluidToME(outputs);
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    public void onFacingChange() {
        toolSetDirection(getBaseMetaTileEntity().getFrontFacing());
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return mLimits;
    }

    protected void setAlignmentLimits(IAlignmentLimits mLimits) {
        this.mLimits = mLimits;
    }

    @Override
    public ExtendedFacing getExtendedFacing() {
        return mExtendedFacing;
    }

    /**
     * Due to limitation of Java type system, you might need to do an unchecked cast. HOWEVER, the returned
     * IStructureDefinition is expected to be evaluated against current instance only, and should not be used against
     * other instances, even for those of the same class.
     */
    @Override
    public abstract IStructureDefinition<T> getStructureDefinition();

    @Override
    public abstract void construct(ItemStack stackSize, boolean hintsOnly);

    @Override
    public abstract int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env);

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return getTooltip().getStructureHint();
    }

    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> !f.isVerticallyFliped();
    }

    /**
     * When an old {@link ExtendedFacing} is loaded upon MTE deserialization, and the loaded facing cannot pass test of
     * current {@link #getAlignmentLimits()}, this method will be called to retrieve a corrected version. This method is
     * currently only intended to be used as a mean to migrate alignment limits, so if you never change the alignment
     * limit then you can probably just use the default implementation.
     * <p>
     * The returned new facing must be able to pass the test of {@link #isNewExtendedFacingValid(ExtendedFacing)}
     */
    protected ExtendedFacing getCorrectedAlignment(ExtendedFacing aOldFacing) {
        if (isNewExtendedFacingValid(ExtendedFacing.DEFAULT)) {
            return ExtendedFacing.DEFAULT;
        }
        for (ExtendedFacing facing : ExtendedFacing.VALUES) {
            if (facing.getFlip()
                .isVerticallyFliped()) {
                continue;
            }
            if (isNewExtendedFacingValid(facing)) {
                return facing;
            }
        }
        throw new AssertionError("No ExtendedFacing can pass the test of isNewExtendedFacingValid");
    }

    /**
     * Explanation of the world coordinate these offset means:
     * <p>
     * Imagine you stand in front of the controller, with controller facing towards you not rotated or flipped.
     * <p>
     * The horizontalOffset would be the number of blocks on the left side of the controller, not counting controller
     * itself. The verticalOffset would be the number of blocks on the top side of the controller, not counting
     * controller itself. The depthOffset would be the number of blocks between you and controller, not counting
     * controller itself.
     * <p>
     * All these offsets can be negative.
     */
    public final boolean checkPiece(String piece, int horizontalOffset, int verticalOffset, int depthOffset,
        @Nullable List<StructureError> errors) {
        final IGregTechTileEntity tTile = getBaseMetaTileEntity();
        StructureChecker<T> checker = new StructureChecker<>((T) this, !isFormed(), errors);
        getStructureDefinition().iterate(
            piece,
            tTile.getWorld(),
            getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            checker);

        if (checker.success) {
            getStructureDefinition().iterate(
                piece,
                tTile.getWorld(),
                getExtendedFacing(),
                tTile.getXCoord(),
                tTile.getYCoord(),
                tTile.getZCoord(),
                horizontalOffset,
                verticalOffset,
                depthOffset,
                centerWalker);
        }

        return checker.success;
    }

    protected final boolean buildPiece(String piece, ItemStack trigger, boolean hintOnly, int horizontalOffset,
        int verticalOffset, int depthOffset) {
        final IGregTechTileEntity tTile = getBaseMetaTileEntity();
        return getStructureDefinition().buildOrHints(
            (T) this,
            trigger,
            piece,
            tTile.getWorld(),
            getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            hintOnly);
    }

    protected final int survivalBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, ISurvivalBuildEnvironment env, boolean check) {
        final IGregTechTileEntity tTile = getBaseMetaTileEntity();
        return getStructureDefinition().survivalBuild(
            (T) this,
            trigger,
            piece,
            tTile.getWorld(),
            getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            env,
            check);
    }

    protected final int survivalBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, ISurvivalBuildEnvironment env, boolean check, boolean checkIfPlaced) {
        int built = survivalBuildPiece(
            piece,
            trigger,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            env,
            check);
        if (checkIfPlaced && built > 0) structureLogic.checkStructure(getBaseMetaTileEntity());
        return built;
    }

    @Override
    public void writeToStream(ByteBuf buffer) {
        super.writeToStream(buffer);
        buffer.writeFloat(center.x);
        buffer.writeFloat(center.y);
        buffer.writeFloat(center.z);
        buffer.writeInt(structureRadius);
        buffer.writeByte(mExtendedFacing.getIndex());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void readFromStream(ByteBuf buffer) {
        super.readFromStream(buffer);
        center.x = buffer.readFloat();
        center.y = buffer.readFloat();
        center.z = buffer.readFloat();

        int oldRadius = structureRadius;
        structureRadius = buffer.readInt();
        var activitySoundLoop = tryGetComponent(IActivitySoundLoopComponent.KEY);
        if (oldRadius != structureRadius && activitySoundLoop != null) {
            activitySoundLoop.restartActivitySound();
        }

        mExtendedFacing = ExtendedFacing.byIndex(buffer.readByte());
    }

    public static class StructureSize {

        public int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        public int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

        public float centerX, centerY, centerZ;
        public int radius;
    }

    private class StructureCenterWalker implements IStructureWalker<T> {

        private StructureSize size;

        public StructureSize finish() {
            // When `size` is null, for whatever reason the piece method was never called, which never called [#visit].
            // We'll just fall back to the default behaviour in this case - play the sound at the center with the
            // default radius.
            // This usually happens when a multi has its own structure check logic.
            if (this.size == null) return null;
            var result = size;
            this.size = null;

            result.centerX = (result.minX + result.maxX) * 0.5f;
            result.centerY = (result.minY + result.maxY) * 0.5f;
            result.centerZ = (result.minZ + result.maxZ) * 0.5f;

            result.radius = GTUtility
                .max(result.maxX - result.minX, result.maxY - result.minY, result.maxZ - result.minZ) / 2;

            return result;
        }

        @Override
        public boolean visit(IStructureElement<T> element, World world, int x, int y, int z, int a, int b, int c) {
            if (size == null) {
                size = new StructureSize();
            }

            size.minX = Math.min(size.minX, x);
            size.minY = Math.min(size.minY, y);
            size.minZ = Math.min(size.minZ, z);

            size.maxX = Math.max(size.maxX, x);
            size.maxY = Math.max(size.maxY, y);
            size.maxZ = Math.max(size.maxZ, z);

            return true;
        }
    }
}
