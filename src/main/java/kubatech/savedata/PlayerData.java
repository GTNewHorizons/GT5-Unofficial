package kubatech.savedata;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerData {
    public long teaAmount = 0L;
    public boolean autoRegen = false;

    PlayerData(NBTTagCompound NBTData) {
        teaAmount = NBTData.getLong("teaAmount");
        autoRegen = NBTData.getBoolean("autoRegen");
    }

    PlayerData() {}

    public NBTTagCompound toNBTData() {
        NBTTagCompound NBTData = new NBTTagCompound();
        NBTData.setLong("teaAmount", teaAmount);
        NBTData.setBoolean("autoRegen", autoRegen);
        return NBTData;
    }

    public void markDirty() {
        PlayerDataManager.Instance.markDirty();
    }
}
