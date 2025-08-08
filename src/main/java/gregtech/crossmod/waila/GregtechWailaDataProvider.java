package gregtech.crossmod.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class GregtechWailaDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final TileEntity tile = accessor.getTileEntity();
        if (tile instanceof IGregtechWailaProvider) {
            try {
                ((IGregtechWailaProvider) tile).getWailaBody(itemStack, currenttip, accessor, config);
            } catch (Throwable t) {
                // waila doesn't print a useful stacktrace, so catch the error and rethrow it
                GTMod.GT_FML_LOGGER.error("Could not call getWailaBody on " + tile, t);
                throw t;
            }
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(final EntityPlayerMP player, final TileEntity tile, final NBTTagCompound tag,
        final World world, int x, int y, int z) {
        if (tile instanceof IGregtechWailaProvider) {
            try {
                ((IGregtechWailaProvider) tile).getWailaNBTData(player, tile, tag, world, x, y, z);
            } catch (Throwable t) {
                GTMod.GT_FML_LOGGER.error("Could not call getWailaNBTData on " + tile, t);
                throw t;
            }
        }

        return tag;
    }
}
