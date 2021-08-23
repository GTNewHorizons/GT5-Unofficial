package GoodGenerator.Items;

import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyItems extends Item {

        private final String tex;
        private List<String> tooltips = new ArrayList<>();

        public MyItems(String name, CreativeTabs Tab){
                this.setUnlocalizedName(name);
                this.setCreativeTab(Tab);
                this.tex = name;
        }

        public MyItems(String name, String tooltip, CreativeTabs Tab) {
                this.setUnlocalizedName(name);
                this.setCreativeTab(Tab);
                this.tex = name;
                this.tooltips.add(tooltip);
        }

        public MyItems(String name, String[] tooltip, CreativeTabs Tab) {
                this.setUnlocalizedName(name);
                this.setCreativeTab(Tab);
                this.tex = name;
                this.tooltips = Arrays.asList(tooltip);
        }

        @SideOnly(Side.CLIENT)
        public void registerIcons(IIconRegister iconRegister) {
                this.itemIcon = iconRegister.registerIcon(GoodGenerator.MOD_ID + ":" + this.tex);
        }

        @SideOnly(Side.CLIENT)
        @SuppressWarnings({"unchecked"})
        public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
                if (tooltips.size() > 0) {
                        p_77624_3_.addAll(tooltips);
                }
        }
}
