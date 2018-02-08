package gtPlusPlus.plugin.fishing.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.plugin.fishing.misc.BaseFishTypes;

import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BaseFish extends ItemFood
{
    private final boolean isCooked;

    public BaseFish(boolean cooked)
    {
        super(0, 0.0F, false);
        this.isCooked = cooked;
    }

    public int func_150905_g(ItemStack p_150905_1_)
    {
        BaseFishTypes fishtype = BaseFishTypes.getFishTypeFromStackDamage(p_150905_1_);
        return this.isCooked && fishtype.isCooked() ? fishtype.func_150970_e() : fishtype.func_150975_c();
    }

    public float func_150906_h(ItemStack p_150906_1_)
    {
        BaseFishTypes fishtype = BaseFishTypes.getFishTypeFromStackDamage(p_150906_1_);
        return this.isCooked && fishtype.isCooked() ? fishtype.func_150977_f() : fishtype.func_150967_d();
    }

    /**
     * Returns a string representing what this item does to a potion.
     */
    public String getPotionEffect(ItemStack p_150896_1_)
    {
        return BaseFishTypes.getFishTypeFromStackDamage(p_150896_1_) == BaseFishTypes.PUFFERFISH ? PotionHelper.field_151423_m : null;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        BaseFishTypes[] afishtype = BaseFishTypes.values();
        int i = afishtype.length;

        for (int j = 0; j < i; ++j)
        {
            BaseFishTypes fishtype = afishtype[j];
            fishtype.func_150968_a(p_94581_1_);
        }
    }

    protected void onFoodEaten(ItemStack fish, World world, EntityPlayer player)
    {
        BaseFishTypes fishtype = BaseFishTypes.getFishTypeFromStackDamage(fish);

        if (fishtype == BaseFishTypes.PUFFERFISH)
        {
            player.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
            player.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
        }

        super.onFoodEaten(fish, world, player);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dmg)
    {
        BaseFishTypes fishtype = BaseFishTypes.getFishTypeFromDamageValue(dmg);
        return this.isCooked && fishtype.isCooked() ? fishtype.func_150979_h() : fishtype.func_150971_g();
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        BaseFishTypes[] afishtype = BaseFishTypes.values();
        int i = afishtype.length;

        for (int j = 0; j < i; ++j)
        {
            BaseFishTypes fishtype = afishtype[j];

            if (!this.isCooked || fishtype.isCooked())
            {
                p_150895_3_.add(new ItemStack(this, 1, fishtype.getFishID()));
            }
        }
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        BaseFishTypes fishtype = BaseFishTypes.getFishTypeFromStackDamage(p_77667_1_);
        return this.getUnlocalizedName() + "." + fishtype.getFishName() + "." + (this.isCooked && fishtype.isCooked() ? "cooked" : "raw");
    }

}