package gregtech.common.blocks;

import static gregtech.api.enums.Textures.BlockIcons.BASALT_STONE;
import static gregtech.api.enums.Textures.BlockIcons.GRANITE_BLACK_STONE;
import static gregtech.api.enums.Textures.BlockIcons.GRANITE_RED_STONE;
import static gregtech.api.enums.Textures.BlockIcons.MARBLE_STONE;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOresOld extends BlockOresAbstractOld {

    private static final String UNLOCALIZED_NAME = "gt.blockores";

    public BlockOresOld() {
        super(UNLOCALIZED_NAME, 7, false, Material.rock);
    }

    @Override
    public String getUnlocalizedName() {
        return UNLOCALIZED_NAME;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
        return (side == ForgeDirection.UP && getDamageValue(world, x, y, z) / 1000 % 16 == 1);
    }

    /**
     * @inheritDoc
     */
    @Override
    public MapColor getMapColor(int meta) {
        return meta == 1 ? MapColor.netherrackColor : MapColor.stoneColor;
    }
}
