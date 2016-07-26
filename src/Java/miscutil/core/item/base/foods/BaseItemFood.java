package miscutil.core.item.base.foods;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemFood extends ItemFood {

	private PotionEffect[] effects;

	public BaseItemFood(String unlocalizedName, int healAmount, float saturationModifier, boolean wolvesFavorite, PotionEffect... effects) {
		super(healAmount, saturationModifier, wolvesFavorite);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.effects = effects;
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		super.onFoodEaten(stack, world, player);

		for (int i = 0; i < effects.length; i ++) {
			if (!world.isRemote && effects[i] != null && effects[i].getPotionID() > 0)
				player.addPotionEffect(new PotionEffect(this.effects[i].getPotionID(),
						this.effects[i].getDuration(), this.effects[i].getAmplifier(),
						this.effects[i].getIsAmbient()
						)
						);
		}
	}

}