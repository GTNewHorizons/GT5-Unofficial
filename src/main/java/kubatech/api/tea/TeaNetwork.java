package kubatech.api.tea;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kubatech.savedata.PlayerData;
import kubatech.savedata.PlayerDataManager;
import kubatech.tileentity.TeaStorageTile;

public class TeaNetwork {

    // TODO: Optimize later :P
    public @NotNull BigInteger teaAmount = BigInteger.ZERO;
    public @NotNull BigInteger teaLimit = BigInteger.valueOf(Long.MAX_VALUE);
    PlayerData owner;
    private final HashSet<TeaStorageTile> teaStorageExtenders = new HashSet<>();

    public static @Nullable TeaNetwork getNetwork(UUID player) {
        PlayerData p = PlayerDataManager.getPlayer(player);
        if (p == null) return null;
        TeaNetwork n = p.teaNetwork;
        if (n == null) {
            p.teaNetwork = new TeaNetwork();
            p.teaNetwork.owner = p;
            return p.teaNetwork;
        }
        n.owner = p;
        return n;
    }

    public boolean canAfford(long price, boolean take) {
        return canAfford(BigInteger.valueOf(price), take);
    }

    public boolean canAfford(BigInteger price, boolean take) {
        if (teaAmount.compareTo(price) >= 0) {
            if (take) {
                teaAmount = teaAmount.subtract(price);
                markDirty();
            }
            return true;
        }
        return false;
    }

    public boolean addTea(long toAdd) {
        return addTea(BigInteger.valueOf(toAdd));
    }

    public boolean addTea(BigInteger toAdd) {
        BigInteger newValue = teaAmount.add(toAdd);
        if (newValue.compareTo(teaLimit) > 0) return false;
        teaAmount = teaAmount.add(toAdd);
        markDirty();
        return true;
    }

    public boolean canAdd(long toAdd) {
        return canAdd(BigInteger.valueOf(toAdd));
    }

    public boolean canAdd(BigInteger toAdd) {
        return teaAmount.add(toAdd)
            .compareTo(teaLimit) <= 0;
    }

    public void registerTeaStorageExtender(@NotNull TeaStorageTile storageTile) {
        if (teaStorageExtenders.add(storageTile)) teaLimit = teaLimit.add(storageTile.teaExtendAmount());
    }

    public void unregisterTeaStorageExtender(@NotNull TeaStorageTile storageTile) {
        if (teaStorageExtenders.remove(storageTile)) teaLimit = teaLimit.subtract(storageTile.teaExtendAmount());
    }

    public void markDirty() {
        owner.markDirty();
    }

    public @NotNull NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByteArray("teaAmount", teaAmount.toByteArray());
        return nbt;
    }

    public static @NotNull TeaNetwork fromNBT(@NotNull NBTTagCompound nbt) {
        TeaNetwork teaNetwork = new TeaNetwork();
        teaNetwork.teaAmount = new BigInteger(nbt.getByteArray("teaAmount"));
        return teaNetwork;
    }
}
