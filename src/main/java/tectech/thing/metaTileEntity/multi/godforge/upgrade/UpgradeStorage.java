package tectech.thing.metaTileEntity.multi.godforge.upgrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;

import gregtech.api.util.GTUtility;

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

    public short[] getPaidCosts(ForgeOfGodsUpgrade upgrade) {
        return getData(upgrade).amountsPaid;
    }

    /** Handles consuming items and updating state if successful. Does NOT handle graviton shards! */
    public void payCost(ForgeOfGodsUpgrade upgrade, ItemStack[] inputStacks) {
        UpgradeData data = getData(upgrade);

        if (!upgrade.hasExtraCost()) {
            data.costPaid = true;
            return;
        }

        ItemStack[] extraCost = upgrade.getExtraCost();
        for (int i = 0; i < inputStacks.length; i++) {
            ItemStack inputStack = inputStacks[i];
            if (inputStack == null) continue;

            for (int j = 0; j < extraCost.length; j++) {
                ItemStack costStack = extraCost[j];
                if (costStack == null) continue;
                int alreadyPaid = data.amountsPaid[j];
                if (alreadyPaid >= costStack.stackSize) continue;

                if (GTUtility.areStacksEqual(inputStack, costStack)) {
                    int maxExtract = costStack.stackSize - alreadyPaid;
                    int extractAmount = Math.min(maxExtract, inputStack.stackSize);
                    if (extractAmount > 0) {
                        data.amountsPaid[j] += (short) extractAmount;
                        inputStack.stackSize -= extractAmount;
                        if (inputStack.stackSize == 0) {
                            inputStacks[i] = null;
                        }
                    }
                }
            }
        }

        // Check if all costs are paid
        for (int i = 0; i < extraCost.length; i++) {
            ItemStack costStack = extraCost[i];
            if (costStack == null) continue;
            if (data.amountsPaid[i] < costStack.stackSize) {
                return;
            }
        }
        data.costPaid = true;
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
        ForgeOfGodsUpgrade[] prereqs = upgrade.getPrerequisites();
        if (prereqs.length == 0) return true;

        Stream<UpgradeData> prereqStream = Arrays.stream(prereqs)
            .map(unlockedUpgrades::get);

        if (upgrade.requiresAllPrerequisites()) {
            return prereqStream.allMatch(UpgradeData::isActive);
        }
        return prereqStream.anyMatch(UpgradeData::isActive);
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

    private boolean hasAnyProgress() {
        if (isUpgradeActive(ForgeOfGodsUpgrade.START)) return true;

        // Check if any costs have been paid in any upgrades
        for (var entry : unlockedUpgrades.entrySet()) {
            ForgeOfGodsUpgrade upgrade = entry.getKey();
            if (upgrade.hasExtraCost()) {
                UpgradeData data = entry.getValue();
                if (data.isCostPaid()) return true;
                for (int i = 0; i < data.amountsPaid.length; i++) {
                    if (data.amountsPaid[i] != 0) return true;
                }
            }
        }

        return false;
    }

    public int getTotalActiveUpgrades() {
        return (int) unlockedUpgrades.values()
            .stream()
            .filter(UpgradeData::isActive)
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
        if (!force && !hasAnyProgress()) return;

        NBTTagCompound upgradeTag = new NBTTagCompound();
        for (ForgeOfGodsUpgrade upgrade : ForgeOfGodsUpgrade.VALUES) {
            UpgradeData data = unlockedUpgrades.get(upgrade);
            upgradeTag.setBoolean("upgrade" + upgrade.ordinal(), data.isActive());
            if (upgrade.hasExtraCost()) {
                NBTTagCompound costTag = new NBTTagCompound();
                costTag.setBoolean("paid", data.isCostPaid());
                for (int i = 0; i < data.amountsPaid.length; i++) {
                    costTag.setShort("costPaid" + i, data.amountsPaid[i]);
                }
                upgradeTag.setTag("extraCost" + upgrade.ordinal(), costTag);
            }
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
            if (upgrade.hasExtraCost() && upgradeTag.hasKey("extraCost" + upgrade.ordinal())) {
                NBTTagCompound costTag = upgradeTag.getCompoundTag("extraCost" + upgrade.ordinal());
                data.costPaid = costTag.getBoolean("paid");
                for (int j = 0; j < data.amountsPaid.length; j++) {
                    data.amountsPaid[j] = costTag.getShort("costPaid" + j);
                }
            }
        }
    }

    /** Sync the full upgrade tree. */
    public GenericListSyncHandler<?> getFullSyncer() {
        return GenericListSyncHandler.<UpgradeData>builder()
            .getter(() -> new ArrayList<>(unlockedUpgrades.values()))
            .setter(val -> {
                for (int i = 0; i < val.size(); i++) {
                    unlockedUpgrades.put(ForgeOfGodsUpgrade.VALUES[i], val.get(i));
                }
            })
            .deserializer(UpgradeData::readFromBuffer)
            .serializer(UpgradeData::writeToBuffer)
            .build();
    }

    private static class UpgradeData {

        private boolean active;
        private boolean costPaid;
        private final short[] amountsPaid = new short[12];

        public boolean isActive() {
            return active;
        }

        public boolean isCostPaid() {
            return costPaid;
        }

        private static void writeToBuffer(PacketBuffer buf, UpgradeData data) {
            buf.writeBoolean(data.isActive());
            buf.writeBoolean(data.isCostPaid());
            for (int i = 0; i < data.amountsPaid.length; i++) {
                buf.writeShort(data.amountsPaid[i]);
            }
        }

        private static UpgradeData readFromBuffer(PacketBuffer buf) {
            UpgradeData data = new UpgradeData();
            data.active = buf.readBoolean();
            data.costPaid = buf.readBoolean();
            for (int i = 0; i < data.amountsPaid.length; i++) {
                data.amountsPaid[i] = buf.readShort();
            }
            return data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UpgradeData that = (UpgradeData) o;

            if (active != that.active) return false;
            if (costPaid != that.costPaid) return false;
            return Arrays.equals(amountsPaid, that.amountsPaid);
        }
    }
}
