package gtPlusPlus.core.item.base.foods;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemFood extends ItemFood {

	private PotionEffect[] effects;
	protected String localName;

	public BaseItemFood(String unlocalizedName, String localizedName, int healAmount, float saturationModifier, boolean wolvesFavorite, PotionEffect... effects) {
		super(healAmount, saturationModifier, wolvesFavorite);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName.replace("Hot", ""));
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.effects = effects;
		this.localName = localizedName;
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
	
	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return ("A Serving of "+localName);
	}

}