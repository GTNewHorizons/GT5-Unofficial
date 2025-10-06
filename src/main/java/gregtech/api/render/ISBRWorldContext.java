package gregtech.api.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISBRWorldContext extends ISBRContext {

    @NotNull
    IBlockAccess getBlockAccess();

    @Nullable
    TileEntity getTileEntity();

    @Override
    ISBRWorldContext reset();

    @Override
    ISBRWorldContext setupColor(ForgeDirection side, int hexColor);

    ISBRWorldContext setupLighting(ForgeDirection facing);
}
