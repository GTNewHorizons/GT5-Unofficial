package gregtech.api.interfaces.tileentity;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public interface IGregtechWailaProvider {

    default ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    default void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {}

    default boolean hasWailaAdvancedBody(ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return false;
    }

    default void getWailaAdvancedBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {}

    default void getWailaNBTData(final EntityPlayerMP player, final TileEntity tile, final NBTTagCompound tag,
        final World world, int x, int y, int z) {}
}
