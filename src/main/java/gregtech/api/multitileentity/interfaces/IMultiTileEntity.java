package gregtech.api.multitileentity.interfaces;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

/*
 * Heavily inspired by GT6
 */
public interface IMultiTileEntity {

    int getMetaId();

    int getRegistryId();

    @Nonnull
    ForgeDirection getFacing();

    void setFacing(ForgeDirection facing);

    void initFromNBT(@Nonnull NBTTagCompound nbt, final int registryId, final int metaId);

    @Nonnull
    ChunkCoordinates getCoords();

    @Nonnull
    String getTileEntityName();

    void addToolTip(@Nonnull final List<String> toolTips);

    default void onNeighborBlockChange(@Nonnull final Block block) {
        onBlockPlaced();
    }

    default boolean onBlockActivated(@Nonnull EntityPlayer player, ForgeDirection side, float subX, float subY,
        float subZ) {
        return false;
    }

    default void onBlockPlaced() {}

    default void onBlockBroken() {}

    default void setOwnder(EntityPlayer player) {}
}
