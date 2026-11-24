package gregtech.api.ec.component;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IWailaDataComponent extends IComponent {

    ComponentKey.ListKey<IWailaDataComponent> KEY = ComponentKey.list();

    void appendWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z);
}
