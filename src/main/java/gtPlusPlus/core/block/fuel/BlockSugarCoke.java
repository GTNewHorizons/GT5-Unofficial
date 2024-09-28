package gtPlusPlus.core.block.fuel;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.base.itemblock.ItemBlockMeta;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BlockSugarCoke extends Block {

    private final IIcon[] textureArray = new IIcon[6];

    public BlockSugarCoke() {
        super(Material.rock);
        this.setBlockName("blockSugarCoke");
        this.setStepSound(soundTypeStone);
        GameRegistry.registerBlock(this, ItemBlockMeta.class, "blockSugarCoke");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        for (int i = 0; i < textureArray.length; i++) {
            this.textureArray[i] = iconRegister.registerIcon(GTPlusPlus.ID + ":fuel/blockSugarCoke" + i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.textureArray[meta];
    }

    @Override
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List<ItemStack> list) {
        for (int i = 0; i < textureArray.length; i++) {
            ItemStack is = new ItemStack(item, 1, i);
            ItemUtils.registerFuel(is, 8000 * (int) Math.pow(9, i));
            list.add(is);
        }
    }

}
