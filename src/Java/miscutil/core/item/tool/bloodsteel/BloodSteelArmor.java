package miscutil.core.item.tool.bloodsteel;

import miscutil.core.item.ModItems;
import miscutil.core.lib.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class BloodSteelArmor extends ItemArmor {

	public BloodSteelArmor(ArmorMaterial p_i45325_1_, int p_i45325_2_, int p_i45325_3_) {
		super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
		// TODO Auto-generated constructor stub
	}

	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		if (stack.getItem() == ModItems.tutHelmet || stack.getItem() == ModItems.tutPlate || stack.getItem() == ModItems.tutBoots) {
			return Strings.MODID + ":textures/armor/BloodSteelArmor1.png";
		} else if (stack.getItem() == ModItems.tutPants) {
			return Strings.MODID + ":textures/armor/BloodSteelArmor2.png";
		} else {
			return null;
		}
	}

	/**
	 * Called to tick armor in the armor slot. Override to do something
	 * 
	 * @param world
	 * @param player
	 * @param itemStack
	 */
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if(player.getCurrentArmor(3) != null && player.getCurrentArmor(2) != null && player.getCurrentArmor(1) != null){
			ItemStack helmet = player.getCurrentArmor(3);
			ItemStack plate = player.getCurrentArmor(2);
			ItemStack pants = player.getCurrentArmor(1);
			if(helmet.getItem() == ModItems.tutHelmet && plate.getItem() == ModItems.tutPlate && pants.getItem() == ModItems.tutPants){
				//player.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 100, 1));
				player.capabilities.allowFlying = true;
			}
		}
		
		if(player.getCurrentArmor(0) != null){
			ItemStack boots = player.getCurrentArmor(0);
			if(boots.getItem() == ModItems.tutBoots){
				player.addPotionEffect(new PotionEffect(Potion.jump.getId(), 100, 1));
			}
		}
	}

}
