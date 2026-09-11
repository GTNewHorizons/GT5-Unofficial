package gregtech.crossmod.waila;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import gregtech.common.blocks.BlockGhostSpace;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class GregtechBlockWailaDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();

        if (block instanceof IGregtechWailaProvider providerBlock) {
            return providerBlock.getWailaStack(accessor, config);
        }

        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        // Ghost space blocks have no meaningful name of their own - show whatever block is below it instead
        if (accessor.getBlock() instanceof BlockGhostSpace && !currenttip.isEmpty()) {
            final ItemStack parent = BlockGhostSpace.getParentPickBlock(accessor.getWorld(), accessor.getPosition());
            if (parent != null) currenttip.set(0, EnumChatFormatting.WHITE + parent.getDisplayName());
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
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
        return tag;
    }
}
