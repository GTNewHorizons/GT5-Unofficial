package tectech.thing.metaTileEntity.multi.base.parameter;

import net.minecraft.nbt.NBTTagCompound;

public interface IParametrized {

    void initParameters();

    void saveParameters(NBTTagCompound nbt);

    void loadParameters(NBTTagCompound nbt);
}
