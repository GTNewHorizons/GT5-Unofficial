package gregtech.common.ec.multiblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;

import gregtech.api.ec.OrderedSectionBuilder;
import gregtech.api.ec.component.IGuiSyncableComponent;
import gregtech.api.ec.component.IMachineModeComponent;
import gregtech.api.ec.component.IOnScrewdriverClickComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.ISavableComponent;
import gregtech.api.ec.component.IWailaBodyProviderComponent;
import gregtech.api.ec.component.IWailaDataComponent;
import gregtech.api.ec.component.gui.IGuiProviderComponent;
import gregtech.api.ec.component.gui.MultiblockGuiBuilder;
import gregtech.api.ec.component.multiblock.IRecipeLockingComponent;
import gregtech.api.util.GTUtility;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;
import gregtech.common.tileentities.machines.RecipeCheckReason;

public class MachineModeComponent extends BaseMultiblockComponent
    implements IMachineModeComponent, IOnScrewdriverClickComponent, IWailaDataComponent, ISavableComponent,
    IWailaBodyProviderComponent, IGuiProviderComponent, IGuiSyncableComponent {

    @Override
    public void appendWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x,
        int y, int z) {
        tag.setString("mode", getMachineModeName());
    }

    @Override
    public void appendWailaBody(@NotNull OrderedSectionBuilder<WailaBodyProvider> builder) {
        builder.add(IWailaBodyProviderComponent.Section.MODE, (ctx, currentTip) -> {
            var tag = ctx.tag();
            if (tag.hasKey("mode")) {
                currentTip.add(
                    StatCollector.translateToLocal("GT5U.multiblock.runningMode") + " "
                        + EnumChatFormatting.WHITE
                        + tag.getString("mode")
                        + EnumChatFormatting.RESET);
            }
        });
    }

    @Override
    public void configureGui(@NotNull MultiblockGuiBuilder builder) {
        builder.terminalTexts()
            .add(
                TerminalSection.MACHINE_MODE,
                (panel, syncManager) -> IKey
                    .dynamic(
                        () -> StatCollector.translateToLocalFormatted("gt.interact.desc.mb.mode", getMachineModeName()))
                    .asWidget()
                    .marginBottom(2)
                    .fullWidth());
        builder.leftGap()
            .addIf(!machineModeIcons.isEmpty(), LeftGapSection.MACHINE_MODE, (panel, syncManager) -> {
                IntSyncValue machineModeSyncer = syncManager.findSyncHandler("machineMode", IntSyncValue.class);
                return new CycleButtonWidget() {

                    @Override
                    public void next() {
                        setState(nextMachineMode(), true);
                    }

                    @Override
                    public void prev() {
                        setState(prevMachineMode(), true);
                    }
                }.value(machineModeSyncer)
                    .length(machineModeIcons.size())
                    .overlay(new DynamicDrawable(() -> getMachineModeIcon(machineModeSyncer.getValue())))
                    .tooltipBuilder(
                        t -> t
                            .addLine(IKey.dynamic(() -> StatCollector.translateToLocal("GT5U.gui.button.mode_switch")))
                            .addLine(IKey.dynamic(() -> GTUtility.getColoredSecondaryTooltip(getMachineModeName()))));
            });
    }

    @Override
    public void registerGuiSyncValues(@NotNull PanelSyncManager syncManager) {
        IntSyncValue machineModeSyncer = new IntSyncValue(this::getMachineMode, this::setMachineMode).allowC2S();
        syncManager.syncValue("machineMode", machineModeSyncer);
    }

    @FunctionalInterface
    public interface ScrewdriverClickCallback {

        boolean onRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ, ItemStack aTool);
    }

    private int machineMode = 0;
    private final int modeCount;
    private final List<UITexture> machineModeIcons = new ArrayList<>(4);
    private final @NotNull IntFunction<String> modeKeyProvider;
    private final @NotNull IntUnaryOperator nextModeOperator;
    private final @NotNull IntUnaryOperator prevModeOperator;
    private @Nullable ScrewdriverClickCallback onScrewdriverRightClick = null;
    private @Nullable Runnable onModeSwitchClickCallback = null;

    public MachineModeComponent(@NotNull String... modeKeys) {
        int modeCount = modeKeys.length;
        this(
            modeCount,
            index -> (index >= 0 && index < modeKeys.length) ? modeKeys[index] : "GT5U.MULTI_MACHINE_MODE.unknown",
            mode -> Math.floorMod(mode + 1, modeCount),
            mode -> Math.floorMod(mode - 1, modeCount));
    }

    public MachineModeComponent(int modeCount, @NotNull IntFunction<String> modeKeyProvider,
        @NotNull IntUnaryOperator nextModeOperator, @NotNull IntUnaryOperator prevModeOperator) {
        if (modeCount <= 0) {
            throw new IllegalArgumentException("MachineModeComponent must have at least one mode!");
        }
        this.modeCount = modeCount;
        this.modeKeyProvider = modeKeyProvider;
        this.nextModeOperator = nextModeOperator;
        this.prevModeOperator = prevModeOperator;
    }

    @Override
    public int getMachineModeCount() {
        return modeCount;
    }

    @Override
    public int getMachineMode() {
        return machineMode;
    }

    @Override
    public void setMachineMode(int index) {
        int normalized = Math.floorMod(index, modeCount);
        if (this.machineMode == normalized) return;
        machineMode = normalized;

        onMachineModeSwitchClick();
        var recipeLocking = tryGetComponent(IRecipeLockingComponent.KEY);
        if (recipeLocking != null) {
            recipeLocking.setSingleRecipeCheck(null);
        }
        var recipeLogic = tryGetComponent(IRecipeLogicComponent.KEY);
        if (recipeLogic != null) {
            recipeLogic.scheduleRecipeCheck(RecipeCheckReason.IMMEDIATE);
        }
    }

    @Override
    public int nextMachineMode() {
        return nextModeOperator.applyAsInt(machineMode);
    }

    @Override
    public int prevMachineMode() {
        return prevModeOperator.applyAsInt(machineMode);
    }

    @Override
    public @Nullable UITexture getMachineModeIcon(int index) {
        if (index < 0 || index >= machineModeIcons.size()) return null;
        return machineModeIcons.get(index);
    }

    @Override
    public @NotNull MachineModeComponent addMachineModeIcons(@NotNull UITexture... icons) {
        Collections.addAll(machineModeIcons, icons);
        return this;
    }

    @Override
    public @NotNull String getMachineModeKey(int index) {
        return modeKeyProvider.apply(index);
    }

    @Override
    public void onMachineModeSwitchClick() {
        if (onModeSwitchClickCallback != null) onModeSwitchClickCallback.run();
    }

    @Override
    public @NotNull String getSaveKey() {
        return "mode";
    }

    @Override
    public @Nullable NBTTagCompound saveComponentData() {
        var nbt = new NBTTagCompound();
        nbt.setInteger("machineMode", machineMode);
        return nbt;
    }

    @Override
    public void loadLegacyNBTData(@NotNull NBTTagCompound nbt) {
        if (nbt.hasKey("machineMode")) {
            machineMode = Math.floorMod(nbt.getInteger("machineMode"), modeCount);
        }
    }

    @Override
    public void loadComponentData(@NotNull NBTTagCompound nbt) {
        loadLegacyNBTData(nbt);
    }

    @Override
    public boolean onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (onScrewdriverRightClick != null) {
            return onScrewdriverRightClick.onRightClick(side, aPlayer, aX, aY, aZ, aTool);
        }
        return false;
    }

    @Override
    public int getOnScrewdriverClickPriority() {
        return OnScrewdriverClickPriority.MACHINE_MODE.priority;
    }

    public @NotNull MachineModeComponent setOnScrewdriverClick(@NotNull ScrewdriverClickCallback callback) {
        onScrewdriverRightClick = callback;
        return this;
    }

    public @NotNull MachineModeComponent setOnModeSwitchClick(@NotNull Runnable callback) {
        onModeSwitchClickCallback = callback;
        return this;
    }
}
