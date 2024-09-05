package goodgenerator.blocks.regularBlock;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockComplexTextureCasing extends BlockCasing {

    @SideOnly(Side.CLIENT)
    protected IIcon[] texture1, texture2;
    String[] textureSide;
    String[] textureTopAndDown;

    public BlockComplexTextureCasing(String name, String[] textureSide, String[] textureTopAndDown) {
        super(name);
        this.textureSide = textureSide;
        this.textureTopAndDown = textureTopAndDown;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side < 2) {
            return meta < this.texture2.length ? this.texture2[meta] : this.texture2[0];
        } else {
            return meta < this.texture1.length ? this.texture1[meta] : this.texture1[0];
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.texture1 = new IIcon[this.textureSide.length];
        for (int i = 0; i < this.textureSide.length; i++) {
            this.texture1[i] = par1IconRegister.registerIcon(this.textureSide[i]);
        }
        this.texture2 = new IIcon[this.textureTopAndDown.length];
        for (int i = 0; i < this.textureTopAndDown.length; i++) {
            this.texture2[i] = par1IconRegister.registerIcon(this.textureTopAndDown[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < Math.max(this.textureSide.length, this.textureTopAndDown.length); i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
