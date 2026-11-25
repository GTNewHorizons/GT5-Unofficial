package gregtech.crossmod.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gregtech.common.hydrodam.ClientDamLevels;
import gregtech.common.hydrodam.ClientDamStats;
import gregtech.common.hydrodam.DamLevel;
import gregtech.common.hydrodam.DamWailaLines;
import gregtech.common.hydrodam.DamWaterMeta;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

/// Shows which volume a dam water block belongs to, its water level, and its reserve and net change. WAILA can't fetch
/// server data for blocks without a tile entity, so the reserve and net change are requested separately through
/// {@link ClientDamStats}, and appear once the server answers.
public class DamWaterWailaDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currentTip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        int volumeId = DamWaterMeta.getVolumeId(accessor.getMetadata());

        if (volumeId == DamWaterMeta.UNASSIGNED_ID) return currentTip;

        currentTip.add(StatCollector.translateToLocalFormatted("GT5U.waila.hydrodam.volume", volumeId));

        World world = accessor.getWorld();
        int dimension = world.provider.dimensionId;
        DamLevel level = ClientDamLevels.get(dimension, volumeId);

        if (level != null) {
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.hydrodam.level",
                    String.format("%.2f", level.topY + level.topLayerFill)));
        }

        ClientDamStats.request(
            dimension,
            volumeId,
            accessor.getPosition().blockX,
            accessor.getPosition().blockY,
            accessor.getPosition().blockZ,
            world.getTotalWorldTime());

        ClientDamStats.Stats stats = ClientDamStats.get(dimension, volumeId);

        if (stats != null) {
            currentTip.add(DamWailaLines.reserve(stats.litres));
            currentTip.add(DamWailaLines.netChange(stats.netLitresPerTick));
        }

        return currentTip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currentTip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x,
        int y, int z) {
        return tag;
    }
}
