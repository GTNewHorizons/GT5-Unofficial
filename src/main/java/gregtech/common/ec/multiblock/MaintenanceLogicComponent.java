package gregtech.common.ec.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.ec.OrderedSectionBuilder;
import gregtech.api.ec.component.IGuiSyncableComponent;
import gregtech.api.ec.component.IOnRecipeTickComponent;
import gregtech.api.ec.component.IOnServerPostTickComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.ISavableComponent;
import gregtech.api.ec.component.IScannerInfoComponent;
import gregtech.api.ec.component.IWailaDataComponent;
import gregtech.api.ec.component.gui.IGuiProviderComponent;
import gregtech.api.ec.component.gui.MultiblockGuiBuilder;
import gregtech.api.ec.component.multiblock.IMaintenanceLogicComponent;
import gregtech.api.enums.MaintenanceIssues;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.config.MachineStats;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;

public class MaintenanceLogicComponent extends BaseHatchContainerComponent
    implements IMaintenanceLogicComponent, ISavableComponent, IOnRecipeTickComponent, IOnServerPostTickComponent,
    IScannerInfoComponent, IWailaDataComponent, IGuiSyncableComponent, IGuiProviderComponent {

    private final List<MTEHatchMaintenance> mMaintenanceHatches = new ArrayList<>();
    private int state;
    private int allowMasks;
    private int errorAmount;
    private boolean randomMaintenanceDamage = true;

    public MaintenanceLogicComponent() {
        this(MaintenanceIssues.ALL_ISSUES);
    }

    public MaintenanceLogicComponent(int allowMasks) {
        if (allowMasks == 0) {
            throw new IllegalArgumentException("allowMasks can't be 0");
        }
        this.allowMasks = allowMasks;
        this.state = MachineStats.machines.disableMaintenanceChecks ? 0 : allowMasks;
        this.errorAmount = Integer.bitCount(allowMasks);
    }

    public MaintenanceLogicComponent setAllowMasks(int allowMasks) {
        if (allowMasks == 0) {
            throw new IllegalArgumentException("allowMasks can't be 0");
        }
        this.allowMasks = allowMasks;
        this.state = MachineStats.machines.disableMaintenanceChecks ? 0 : allowMasks;
        this.errorAmount = Integer.bitCount(allowMasks);
        return this;
    }

    public MaintenanceLogicComponent setRandomMaintenanceDamage(boolean enabled) {
        this.randomMaintenanceDamage = enabled;
        return this;
    }

    public boolean tryAttachMaintenance(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchMaintenance hatch) {
            if (!mMaintenanceHatches.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                mMaintenanceHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mMaintenanceHatches);
        mMaintenanceHatches.clear();
    }

    @Override
    public void explodeAllHatches(long power) {
        for (MetaTileEntity tTileEntity : getValidMaintenanceHatches()) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(power);
        }
    }

    @Override
    public @NotNull List<MTEHatchMaintenance> getMaintenanceHatches() {
        return mMaintenanceHatches;
    }

    @Override
    public boolean supportIssue(int mask) {
        return (allowMasks & mask) != 0;
    }

    @Override
    public boolean hasIssue(int mask) {
        return (state & mask) != 0;
    }

    @Override
    public void setIssue(int mask, boolean hasIssue) {
        mask &= allowMasks;
        if (mask == 0) {
            throw new IllegalArgumentException("Unsupported mask");
        }
        if (hasIssue) {
            state |= mask;
        } else {
            state &= ~mask;
        }
    }

    @Override
    public int getMaintenanceErrorBits() {
        return state;
    }

    @Override
    public int getMaintenanceErrorCount() {
        return Integer.bitCount(state);
    }

    @Override
    public int getRepairStatus() {
        return errorAmount - getMaintenanceErrorCount();
    }

    @Override
    public int getIdealStatus() {
        return errorAmount;
    }

    @Override
    public void fixAllIssues() {
        state = 0;
    }

    @Override
    public void causeAllIssues() {
        state = allowMasks;
    }

    @Override
    public void causeRandomIssue() {
        int index = getMachine().getRandomNumber(errorAmount);
        int mask = allowMasks;
        for (int i = 0; i < index; i++) {
            mask &= mask - 1;
        }
        mask = Integer.lowestOneBit(mask);
        state |= mask;
    }

    @Override
    public void checkMaintenance() {
        if (MachineStats.machines.disableMaintenanceChecks) {
            fixAllIssues();
            return;
        }
        if (state == 0) return;
        for (MTEHatchMaintenance tHatch : getValidMaintenanceHatches()) {
            boolean tDidRepair = false;

            if (tHatch.mAuto) tDidRepair = tHatch.autoMaintainance();

            // For each tool, only if needed, collect the tool flags
            // that this Maintenance Hatch has provided
            if (tHatch.mWrench && hasIssue(MaintenanceIssues.WRENCH)) {
                setIssue(MaintenanceIssues.WRENCH, false);
                tDidRepair = true;
            }
            if (tHatch.mScrewdriver && hasIssue(MaintenanceIssues.SCREWDRIVER)) {
                setIssue(MaintenanceIssues.SCREWDRIVER, false);
                tDidRepair = true;
            }
            if (tHatch.mSoftMallet && hasIssue(MaintenanceIssues.SOFT_MALLET)) {
                setIssue(MaintenanceIssues.SOFT_MALLET, false);
                tDidRepair = true;
            }
            if (tHatch.mHardHammer && hasIssue(MaintenanceIssues.HARD_HAMMER)) {
                setIssue(MaintenanceIssues.HARD_HAMMER, false);
                tDidRepair = true;
            }
            if (tHatch.mSolderingTool && hasIssue(MaintenanceIssues.SOLDERING_TOOL)) {
                setIssue(MaintenanceIssues.SOLDERING_TOOL, false);
                tDidRepair = true;
            }
            if (tHatch.mCrowbar && hasIssue(MaintenanceIssues.CROWBAR)) {
                setIssue(MaintenanceIssues.CROWBAR, false);
                tDidRepair = true;
            }

            tHatch.mWrench = false;
            tHatch.mScrewdriver = false;
            tHatch.mSoftMallet = false;
            tHatch.mHardHammer = false;
            tHatch.mSolderingTool = false;
            tHatch.mCrowbar = false;

            if (tDidRepair) tHatch.onMaintenancePerformed(getMachine());
        }
    }

    @Override
    public int getMaintenanceEfficiencyPenalty() {
        return (getIdealStatus() - getRepairStatus()) * 1000;
    }

    @Override
    public @NotNull String getSaveKey() {
        return "maint";
    }

    @Override
    public @Nullable NBTTagCompound saveComponentData() {
        var nbt = new NBTTagCompound();
        nbt.setInteger("state", state);
        return nbt;
    }

    @Override
    public void loadLegacyNBTData(@NotNull NBTTagCompound aNBT) {
        if (MachineStats.machines.disableMaintenanceChecks) {
            fixAllIssues();
            return;
        }
        if (aNBT.hasKey("mWrench") && supportIssue(MaintenanceIssues.WRENCH)) {
            setIssue(MaintenanceIssues.WRENCH, !aNBT.getBoolean("mWrench"));
        }
        if (aNBT.hasKey("mScrewdriver") && supportIssue(MaintenanceIssues.SCREWDRIVER)) {
            setIssue(MaintenanceIssues.SCREWDRIVER, !aNBT.getBoolean("mScrewdriver"));
        }
        if ((aNBT.hasKey("mSoftMallet") || aNBT.hasKey("mSoftHammer")) && supportIssue(MaintenanceIssues.SOFT_MALLET)) {
            setIssue(
                MaintenanceIssues.SOFT_MALLET,
                !(aNBT.getBoolean("mSoftMallet") || aNBT.getBoolean("mSoftHammer")));
        }
        if (aNBT.hasKey("mHardHammer") && supportIssue(MaintenanceIssues.HARD_HAMMER)) {
            setIssue(MaintenanceIssues.HARD_HAMMER, !aNBT.getBoolean("mHardHammer"));
        }
        if (aNBT.hasKey("mSolderingTool") && supportIssue(MaintenanceIssues.SOLDERING_TOOL)) {
            setIssue(MaintenanceIssues.SOLDERING_TOOL, !aNBT.getBoolean("mSolderingTool"));
        }
        if (aNBT.hasKey("mCrowbar") && supportIssue(MaintenanceIssues.CROWBAR)) {
            setIssue(MaintenanceIssues.CROWBAR, !aNBT.getBoolean("mCrowbar"));
        }
    }

    @Override
    public void loadComponentData(@NotNull NBTTagCompound nbt) {
        if (MachineStats.machines.disableMaintenanceChecks) {
            fixAllIssues();
            return;
        }
        state = nbt.getInteger("state") & allowMasks;
    }

    @Override
    public void onServerPostTick(IGregTechTileEntity te, long aTick) {
        if (isMachineFormed()) {
            checkMaintenance();
            if (getRepairStatus() <= 0 && te.isAllowedToWork()) {
                stopMachine(ShutDownReasonRegistry.NO_REPAIR);
            }
        }
    }

    @Override
    public int getServerTickPriority() {
        return ServerTickPriority.MAINTAIN.priority;
    }

    @Override
    public boolean onRecipeTick(@NotNull IRecipeLogicComponent recipeLogicComponent) {
        if (!randomMaintenanceDamage || MachineStats.machines.disableMaintenanceChecks) return true;
        if (recipeLogicComponent.getRuntime() == 1000) {
            if (getMachine().getRandomNumber(6000) == 0) {
                causeRandomIssue();
            }
        }
        return true;
    }

    @Override
    public int getRecipeTickPriority() {
        return RecipeTickPriority.RANDOM_MAINTAIN_ISSUE.priority;
    }

    @Override
    public void appendInfoData(@NotNull OrderedSectionBuilder<Consumer<List<String>>> builder) {
        builder.add(Section.MAINTAIN, data -> {
            data.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.multiblock.scanner.problems",
                    formatNumber(getIdealStatus() - getRepairStatus())));
        });
    }

    @Override
    public void appendInfoMap(@NotNull Map<String, String> map) {
        map.put("maintenanceIssues", Integer.toString(getIdealStatus() - getRepairStatus()));
    }

    @Override
    public void appendWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x,
        int y, int z) {
        tag.setBoolean("hasProblems", (getIdealStatus() - getRepairStatus()) > 0);
    }

    @Override
    public void registerGuiSyncValues(@NotNull PanelSyncManager syncManager) {
        syncManager.syncValue("maintState", new IntSyncValue(() -> state, val -> state = val));
    }

    @Override
    public void configureGui(@NotNull MultiblockGuiBuilder builder) {
        builder.rightCorner()
            .add(RightCornerSection.MAINTAIN, ((panel, syncManager) -> new DynamicDrawable(() -> {
                int count = getMaintenanceErrorCount();
                if (count == 0) return GTGuiTextures.OVERLAY_NO_MAINTENANCE_ISSUES;
                if (count >= getIdealStatus()) return GTGuiTextures.OVERLAY_ALL_MAINTENANCE_ISSUES;
                return GTGuiTextures.OVERLAY_SOME_MAINTENANCE_ISSUES;
            }

            ).asWidget()
                .size(18, 18)
                .marginTop(4)
                .tooltipBuilder(this::makeMaintenanceHoverableTooltip)
                .tooltipAutoUpdate(true)));
    }

    protected void makeMaintenanceHoverableTooltip(RichTooltip t) {
        if (getMaintenanceErrorCount() == 0) {
            t.addLine(
                IKey.lang("GT5U.gui.text.multi.no_maintenance_issue")
                    .style(EnumChatFormatting.GREEN));
            return;
        }
        if (hasIssue(MaintenanceIssues.CROWBAR)) t.add(
            GTGuiTextures.OVERLAY_NEEDS_CROWBAR.asIcon()
                .size(16, 16))
            .add(" ");
        if (hasIssue(MaintenanceIssues.HARD_HAMMER)) t.add(
            GTGuiTextures.OVERLAY_NEEDS_HARDHAMMER.asIcon()
                .size(16, 16))
            .add(" ");
        if (hasIssue(MaintenanceIssues.SCREWDRIVER)) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SCREWDRIVER.asIcon()
                .size(16, 16))
            .add(" ");
        if (hasIssue(MaintenanceIssues.SOFT_MALLET)) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SOFTHAMMER.asIcon()
                .size(16, 16))
            .add(" ");
        if (hasIssue(MaintenanceIssues.SOLDERING_TOOL)) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SOLDERING.asIcon()
                .size(16, 16))
            .add(" ");
        if (hasIssue(MaintenanceIssues.WRENCH)) t.add(
            GTGuiTextures.OVERLAY_NEEDS_WRENCH.asIcon()
                .size(16, 16))
            .add(" ");
    }
}
