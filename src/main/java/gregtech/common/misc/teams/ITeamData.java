package gregtech.common.misc.teams;

import net.minecraft.nbt.NBTTagCompound;

public interface ITeamData {

    void writeToNBT(NBTTagCompound NBT);

    void readFromNBT(NBTTagCompound NBT);
}
