package GoodGenerator.Items;

import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MyItems extends Item {

        private final String tex;

        public MyItems(String name, CreativeTabs Tab){
                this.setUnlocalizedName(name);
                this.setCreativeTab(Tab);
                this.tex = name;
        }

        @SideOnly(Side.CLIENT)
        public void registerIcons(IIconRegister iconRegister) {
                this.itemIcon = iconRegister.registerIcon(GoodGenerator.MOD_ID + ":" + this.tex);
        }
}
