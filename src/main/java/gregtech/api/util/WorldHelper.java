package gregtech.api.util;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldHelper {

    private WorldHelper() {}

    @Nullable
    public static TileEntity getTileEntityAtSide(@Nonnull final ForgeDirection side, @Nonnull final IBlockAccess world,
        final int x, final int y, final int z) {
        Objects.requireNonNull(side);
        Objects.requireNonNull(world);

        final int newX = x + side.offsetX;
        final int newY = y + side.offsetY;
        final int newZ = z + side.offsetZ;

        return world.getTileEntity(newX, newY, newZ);
    }

    @Nullable
    public static IInventory getIInventoryAtSide(@Nonnull final ForgeDirection side, @Nonnull final IBlockAccess world,
        final int x, final int y, final int z) {
        final TileEntity te = getTileEntityAtSide(side, world, x, y, z);
        if (te instanceof IInventory teii) return teii;
        return null;
    }

}
