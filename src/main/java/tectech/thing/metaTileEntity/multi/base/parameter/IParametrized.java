package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public interface IParametrized {

    void initParameters();

    void loadLegacyParameters(NBTTagCompound nbt);

    List<Parameter<?>> getParameters();
}
