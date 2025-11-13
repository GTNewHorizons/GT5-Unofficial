package gregtech.common.gui.modularui.multiblock.godforge.data;

import static tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade.END;

import java.util.function.BiConsumer;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public final class SyncActions<T> {

    // spotless:off

    public static SyncActions<ForgeOfGodsUpgrade> RESPEC_UPGRADE = new SyncActions<>(
        "fog.sync_action.respec_upgrade",
        (buf, upgrade) -> buf.writeByte(upgrade.ordinal()),
        (buf, data) -> {
            ForgeOfGodsUpgrade upgrade = ForgeOfGodsUpgrade.VALUES[buf.readByte()];

            if (!data.isUpgradeActive(upgrade)) return;
            if (!data.getUpgrades().checkDependents(upgrade)) return;

            data.getUpgrades().respecUpgrade(upgrade);
            data.setGravitonShardsAvailable(data.getGravitonShardsAvailable() + upgrade.getShardCost());
            data.setGravitonShardsSpent(data.getGravitonShardsSpent() - upgrade.getShardCost());

            if (upgrade == END) {
                data.setGravitonShardEjection(false);
            }
        },
        Side.SERVER);

    public static SyncActions<ForgeOfGodsUpgrade> COMPLETE_UPGRADE = new SyncActions<>(
        "fog.sync_action.complete_upgrade",
        (buf, upgrade) -> buf.writeByte(upgrade.ordinal()),
        (buf, data) -> {
            ForgeOfGodsUpgrade upgrade = ForgeOfGodsUpgrade.VALUES[buf.readByte()];

            if (data.isUpgradeActive(upgrade)) return;
            if (!data.getUpgrades().checkPrerequisites(upgrade)) return;
            if (!data.getUpgrades().checkSplit(upgrade, data.getRingAmount())) return;
            if (!data.getUpgrades().checkCost(upgrade, data.getGravitonShardsAvailable())) return;

            data.unlockUpgrade(upgrade);
            data.setGravitonShardsAvailable(data.getGravitonShardsAvailable() - upgrade.getShardCost());
            data.setGravitonShardsSpent(data.getGravitonShardsSpent() + upgrade.getShardCost());
        },
        Side.SERVER);

    // spotless:on

    private final String syncId;
    private final BiConsumer<PacketBuffer, T> writer;
    private final BiConsumer<PacketBuffer, ForgeOfGodsData> action;
    private final Side executeSide;

    private SyncActions(String syncId, BiConsumer<PacketBuffer, T> writer,
        BiConsumer<PacketBuffer, ForgeOfGodsData> action, Side executeSide) {
        this.syncId = syncId;
        this.writer = writer;
        this.action = action;
        this.executeSide = executeSide;
    }

    public void registerFor(Panels forPanel, SyncHypervisor hypervisor) {
        PanelSyncManager syncManager = hypervisor.getSyncManager(forPanel);
        syncManager.registerSyncedAction(syncId, buf -> {
            ForgeOfGodsData data = hypervisor.getData();
            switch (executeSide) {
                case SERVER -> {
                    if (!syncManager.isClient()) {
                        action.accept(buf, data);
                    }
                }
                case CLIENT -> {
                    if (syncManager.isClient()) {
                        action.accept(buf, data);
                    }
                }
                case BOTH -> action.accept(buf, data);
            }
        });
    }

    public void callFrom(Panels fromPanel, SyncHypervisor hypervisor, T data) {
        PanelSyncManager syncManager = hypervisor.getSyncManager(fromPanel);
        syncManager.callSyncedAction(syncId, buf -> writer.accept(buf, data));
    }

    private enum Side {
        SERVER,
        CLIENT,
        BOTH
    }
}
