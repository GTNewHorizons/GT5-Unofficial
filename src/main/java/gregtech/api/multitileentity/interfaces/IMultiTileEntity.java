package gregtech.api.multitileentity.interfaces;

import java.util.List;

import javax.annotation.Nonnull;

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
    ForgeDirection getFrontFacing();

    void initFromNBT(@Nonnull NBTTagCompound nbt, final int registryId, final int metaId);

    @Nonnull
    ChunkCoordinates getCoords();

    @Nonnull
    String getTileEntityName();

    void addToolTip(@Nonnull final List<String> toolTips);
}
