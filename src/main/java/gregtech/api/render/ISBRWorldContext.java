package gregtech.api.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
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

    @SuppressWarnings("MethodWithTooManyParameters")
    ISBRWorldContext setup(int x, int y, int z, @NotNull Block block, int modelId, @NotNull RenderBlocks renderBlocks);

    @Override
    ISBRWorldContext reset();

    @Override
    ISBRWorldContext setupColor(ForgeDirection side, int hexColor);

    ISBRWorldContext setupLighting(ForgeDirection facing);
}
