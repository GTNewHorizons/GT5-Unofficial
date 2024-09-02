package goodgenerator.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import gregtech.api.util.GTUtility;
import ic2.core.IC2Potion;

public class RadioactiveItem extends GGItem {

    protected final int mRadio;

    public RadioactiveItem(String name, CreativeTabs Tab, int Rad) {
        super(name, Tab);
        this.mRadio = Rad;
    }

    public RadioactiveItem(String name, String[] tooltip, CreativeTabs Tab, int Rad) {
        super(name, tooltip, Tab);
        this.mRadio = Rad;
    }

    public RadioactiveItem(String name, String tooltip, CreativeTabs Tab, int Rad) {
        super(name, tooltip, Tab);
        this.mRadio = Rad;
    }

    @Override
    public void onUpdate(ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand) {
        super.onUpdate(aStack, aWorld, aPlayer, aTimer, aIsInHand);
        EntityLivingBase tPlayer = (EntityPlayer) aPlayer;
        if (!GTUtility.isWearingFullRadioHazmat(tPlayer))
            tPlayer.addPotionEffect(new PotionEffect(IC2Potion.radiation.id, mRadio, 4));
    }
}
