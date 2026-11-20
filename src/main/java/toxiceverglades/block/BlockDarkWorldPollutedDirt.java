package toxiceverglades.block;

import static gregtech.api.enums.Mods.Minecraft;

import net.minecraft.block.BlockDirt;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class BlockDarkWorldPollutedDirt extends BlockDirt implements ITileTooltip {

    public BlockDarkWorldPollutedDirt() {
        this.setCreativeTab(AddToCreativeTab.tabBOP);
        this.setBlockName("blockDarkWorldGround2");
        this.setHardness(0.5F);
        this.setBlockTextureName(Minecraft.ID + ":dirt");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int metadata) {
        return getBlockColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return world.getBiomeGenForCoords(x, z)
            .getBiomeGrassColor(x, y, z);
    }

    @Override
    public int getTooltipID() {
        return 3;
    }
}
