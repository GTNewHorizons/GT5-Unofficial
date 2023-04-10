package kubatech.tileentity;

import java.math.BigInteger;
import java.util.UUID;

import kubatech.api.tea.TeaNetwork;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TeaStorageTile extends TileEntity {

    public TeaStorageTile() {
        super();
    }

    private UUID tileOwner = null;
    private TeaNetwork teaNetwork = null;

    public void setTeaOwner(UUID teaOwner) {
        if (tileOwner == null) {
            tileOwner = teaOwner;
            teaNetwork = TeaNetwork.getNetwork(tileOwner);
            markDirty();
            teaNetwork.registerTeaStorageExtender(this);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound NBTData) {
        super.readFromNBT(NBTData);
        try {
            tileOwner = UUID.fromString(NBTData.getString("tileOwner"));
            teaNetwork = TeaNetwork.getNetwork(tileOwner);
            teaNetwork.registerTeaStorageExtender(this);
        } catch (Exception ignored) {}
    }

    @Override
    public void writeToNBT(NBTTagCompound NBTData) {
        super.writeToNBT(NBTData);
        NBTData.setString("tileOwner", tileOwner.toString());
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    public BigInteger teaExtendAmount() {
        return BigInteger.valueOf(Long.MAX_VALUE);
    }

    @Override
    public void onChunkUnload() {
        if (teaNetwork != null) teaNetwork.unregisterTeaStorageExtender(this);
    }

    @Override
    public void invalidate() {
        if (teaNetwork != null) teaNetwork.unregisterTeaStorageExtender(this);
    }
}
