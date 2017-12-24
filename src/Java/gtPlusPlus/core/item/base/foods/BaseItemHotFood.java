package gtPlusPlus.core.item.base.foods;

import java.util.List;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BaseItemHotFood extends BaseItemFood{

	protected String materialName;
	protected String unlocalName;
	protected int cooldownTime;
	protected Item output;

	public BaseItemHotFood(final String unlocalizedName, final int healAmount, final float healSaturation, final String foodName, final int timeToCoolInSeconds, final Item cooledFood) {
		super(unlocalizedName, "Hot "+foodName, healAmount, healSaturation, false);
		this.unlocalName = unlocalizedName;
		this.cooldownTime = timeToCoolInSeconds * 20;
		this.materialName = foodName;
		this.output = cooledFood;
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onEaten(final ItemStack iStack, final World world, final EntityPlayer player) {
		return super.onEaten(iStack, world, player);
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		//Utils.LOG_INFO("Item Damage: "+iStack.getItemDamage()+" Max Damage: "+iStack.getMaxDamage());
		if (!world.isRemote){
			if(iStack.getItemDamage() == this.cooldownTime)	{
				if (entityHolding instanceof EntityPlayer){
					Logger.INFO("Foods Done.");
					((EntityPlayer) entityHolding).inventory.addItemStackToInventory(ItemUtils.getSimpleStack(this.output));
					((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
				}
			}else if(iStack.getItemDamage() < this.cooldownTime){
				iStack.setItemDamage(iStack.getItemDamage() + 1);
			}
			if(MathUtils.divideXintoY(iStack.getItemDamage(), 150)){
				entityHolding.attackEntityFrom(DamageSource.onFire, 1);
			}


		}
		super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if ((this.materialName != null) && (this.materialName != "") && !this.materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.RED+"Very hot!"+EnumChatFormatting.GRAY+" Avoid direct handling..");
			list.add(EnumChatFormatting.GRAY+"This food has "+((this.cooldownTime-stack.getItemDamage())/20)+" seconds left, until it is cool.");
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return Utils.rgbtoHexValue(230, 96, 96);

	}
}
