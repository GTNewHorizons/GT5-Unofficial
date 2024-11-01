package tectech.thing.metaTileEntity.multi.godforge.upgrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.stream.Stream;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

public class UpgradeStorage {

    private final EnumMap<ForgeOfGodsUpgrade, UpgradeData> unlockedUpgrades = new EnumMap<>(ForgeOfGodsUpgrade.class);

    public UpgradeStorage() {
        for (ForgeOfGodsUpgrade upgrade : ForgeOfGodsUpgrade.VALUES) {
            unlockedUpgrades.put(upgrade, new UpgradeData());
        }
    }

    /** Whether the passed upgrade is already unlocked (purchased). */
    public boolean isUpgradeActive(ForgeOfGodsUpgrade upgrade) {
        return getData(upgrade).isActive();
    }

    public boolean isCostPaid(ForgeOfGodsUpgrade upgrade) {
        return getData(upgrade).isCostPaid();
    }

    public void paidCost(ForgeOfGodsUpgrade upgrade) {
        getData(upgrade).costPaid = true;
    }

    public void unlockUpgrade(ForgeOfGodsUpgrade upgrade) {
        getData(upgrade).active = true;
    }

    public void respecUpgrade(ForgeOfGodsUpgrade upgrade) {
        getData(upgrade).active = false;
    }

    /**
     * Whether the passed upgrade can be unlocked, checking that the prerequisites are satisfied.
     */
    public boolean checkPrerequisites(ForgeOfGodsUpgrade upgrade) {
        Stream<UpgradeData> prereqs = Arrays.stream(upgrade.getPrerequisites())
            .map(unlockedUpgrades::get);

        if (upgrade.requiresAllPrerequisites()) {
            return prereqs.allMatch(UpgradeData::isActive);
        }
        return prereqs.anyMatch(UpgradeData::isActive);
    }

    public boolean checkSplit(ForgeOfGodsUpgrade upgrade, int maxSplitUpgrades) {
        if (ForgeOfGodsUpgrade.SPLIT_UPGRADES.contains(upgrade)) {
            return ForgeOfGodsUpgrade.SPLIT_UPGRADES.stream()
                .map(unlockedUpgrades::get)
                .filter(UpgradeData::isActive)
                .count() < maxSplitUpgrades;
        }
        return true;
    }

    public boolean checkCost(ForgeOfGodsUpgrade upgrade, int availableShards) {
        if (upgrade.getShardCost() > availableShards) return false;
        return !upgrade.hasExtraCost() || isCostPaid(upgrade);
    }

    /** @return true if any dependent upgrades are currently unlocked. */
    public boolean checkDependents(ForgeOfGodsUpgrade upgrade) {
        for (ForgeOfGodsUpgrade dependent : upgrade.getDependents()) {
            if (!isUpgradeActive(dependent)) continue;

            // Check failed, this dependent strictly requires the passed upgrade
            if (dependent.requiresAllPrerequisites()) return false;

            // Check for some other prerequisite upgrade of the dependent to make sure that
            // if the passed upgrade is removed, that upgrade is still valid
            if (Arrays.stream(dependent.getPrerequisites())
                .map(unlockedUpgrades::get)
                .filter(UpgradeData::isActive)
                .count() <= 1) {
                return false;
            }
        }
        return true;
    }

    private UpgradeData getData(ForgeOfGodsUpgrade upgrade) {
        return unlockedUpgrades.computeIfAbsent(upgrade, $ -> new UpgradeData());
    }

    /**  */
    public boolean hasAnyUpgrade() {
        return isUpgradeActive(ForgeOfGodsUpgrade.START);
    }

    public int getTotalActiveUpgrades() {
        return (int) unlockedUpgrades.values()
            .stream()
            .map(UpgradeData::isActive)
            .count();
    }

    public Collection<ForgeOfGodsUpgrade> getAllUpgrades() {
        return unlockedUpgrades.keySet();
    }

    public void resetAll() {
        for (UpgradeData data : unlockedUpgrades.values()) {
            data.active = false;
            data.costPaid = false;
        }
    }

    public void unlockAll() {
        for (UpgradeData data : unlockedUpgrades.values()) {
            data.active = true;
        }
    }

    public void serializeToNBT(NBTTagCompound NBT, boolean force) {
        if (!force && !hasAnyUpgrade()) return;

        NBTTagCompound upgradeTag = new NBTTagCompound();
        for (ForgeOfGodsUpgrade upgrade : ForgeOfGodsUpgrade.VALUES) {
            UpgradeData data = unlockedUpgrades.get(upgrade);
            upgradeTag.setBoolean("upgrade" + upgrade.ordinal(), data.isActive());
            upgradeTag.setBoolean("upgrade" + upgrade.ordinal() + "cost", data.isCostPaid());
        }
        NBT.setTag("upgrades", upgradeTag);
    }

    public void rebuildFromNBT(NBTTagCompound NBT) {
        if (!NBT.hasKey("upgrades")) return;

        NBTTagCompound upgradeTag = NBT.getCompoundTag("upgrades");
        for (int i = 0; i < ForgeOfGodsUpgrade.VALUES.length; i++) {
            ForgeOfGodsUpgrade upgrade = ForgeOfGodsUpgrade.VALUES[i];
            UpgradeData data = unlockedUpgrades.get(upgrade);
            data.active = upgradeTag.getBoolean("upgrade" + upgrade.ordinal());
            data.costPaid = upgradeTag.getBoolean("upgrade" + upgrade.ordinal() + "cost");
        }
    }

    /** Sync widget to sync the full upgrade tree. */
    public FakeSyncWidget<?> getFullSyncer() {
        return new FakeSyncWidget.ListSyncer<>(() -> new ArrayList<>(unlockedUpgrades.values()), val -> {
            for (int i = 0; i < val.size(); i++) {
                unlockedUpgrades.put(ForgeOfGodsUpgrade.VALUES[i], val.get(i));
            }
        }, UpgradeData::writeToBuffer, UpgradeData::readFromBuffer);
    }

    /** Sync widget to sync a single upgrade. */
    public FakeSyncWidget<?> getSingleSyncer(ForgeOfGodsUpgrade upgrade) {
        return new FakeSyncWidget<>(
            () -> unlockedUpgrades.get(upgrade),
            val -> unlockedUpgrades.put(upgrade, val),
            UpgradeData::writeToBuffer,
            UpgradeData::readFromBuffer);
    }

    private static class UpgradeData {

        private boolean active;
        private boolean costPaid;

        public boolean isActive() {
            return active;
        }

        public boolean isCostPaid() {
            return costPaid;
        }

        private static void writeToBuffer(PacketBuffer buf, UpgradeData data) {
            buf.writeBoolean(data.isActive());
            buf.writeBoolean(data.isCostPaid());
        }

        private static UpgradeData readFromBuffer(PacketBuffer buf) {
            UpgradeData data = new UpgradeData();
            data.active = buf.readBoolean();
            data.costPaid = buf.readBoolean();
            return data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UpgradeData that = (UpgradeData) o;

            return active == that.active && costPaid == that.costPaid;
        }
    }
}
