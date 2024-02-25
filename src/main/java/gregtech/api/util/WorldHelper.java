package gregtech.api.util;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldHelper {

    private WorldHelper() {}

    @Nullable
    public static TileEntity getTileEntityAtSide(@Nonnull final ForgeDirection side, @Nonnull final IBlockAccess world, @Nonnull final ChunkCoordinates origin) {
        Objects.requireNonNull(side);
        Objects.requireNonNull(world);
        Objects.requireNonNull(origin);

        final int x = origin.posX + side.offsetX;
        final int y = origin.posY + side.offsetY;
        final int z = origin.posZ + side.offsetZ;

        return world.getTileEntity(x, y, z);
    }

    @Nullable
    public static IInventory getIInventoryAtSide(@Nonnull final ForgeDirection side, @Nonnull final IBlockAccess world, @Nonnull final ChunkCoordinates origin) {
        TileEntity te = getTileEntityAtSide(side, world, origin);
        if (te instanceof IInventory teii) return teii;
        return null;
    }

}
