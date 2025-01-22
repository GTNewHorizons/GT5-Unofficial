package gregtech.common.misc.teams;

import net.minecraft.nbt.NBTTagCompound;

public interface ITeamData {

    void writeToNBT(NBTTagCompound NBT);

    void readFromNBT(NBTTagCompound NBT);

    ITeamData UNIMPLEMENTED = new ITeamData() {

        @Override
        public void writeToNBT(NBTTagCompound NBT) {}

        @Override
        public void readFromNBT(NBTTagCompound NBT) {}
    };
}
