package gtPlusPlus.core.item.base.foods;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;

public class BaseItemFood extends ItemFood {

	private final PotionEffect[] effects;
	protected String localName;

	public BaseItemFood(final String unlocalizedName, final String localizedName, final int healAmount, final float saturationModifier, final boolean wolvesFavorite, final PotionEffect... effects) {
		super(healAmount, saturationModifier, wolvesFavorite);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName.replace("Hot", ""));
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.effects = effects;
		this.localName = localizedName;
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@Override
	protected void onFoodEaten(final ItemStack stack, final World world, final EntityPlayer player) {
		super.onFoodEaten(stack, world, player);

		for (int i = 0; i < this.effects.length; i ++) {
			if (!world.isRemote && (this.effects[i] != null) && (this.effects[i].getPotionID() > 0)) {
				player.addPotionEffect(new PotionEffect(this.effects[i].getPotionID(),
						this.effects[i].getDuration(), this.effects[i].getAmplifier(),
						this.effects[i].getIsAmbient()
						)
						);
			}
		}
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		return ("A Serving of "+this.localName);
	}

}