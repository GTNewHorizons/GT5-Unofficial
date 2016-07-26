package miscutil.core.item.base;

import java.util.List;

import miscutil.core.item.base.foods.BaseItemFood;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
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

	public BaseItemHotFood(String unlocalizedName, int healAmount, float healSaturation, String foodName, int timeToCoolInSeconds, Item cooledFood) {
		super(unlocalizedName, healAmount, healSaturation, false);
		this.unlocalName = unlocalizedName;
		this.cooldownTime = timeToCoolInSeconds * 20;
		this.materialName = foodName;
		this.output = cooledFood;
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onEaten(ItemStack iStack, World world, EntityPlayer player) {
		return super.onEaten(iStack, world, player);
	}

	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		//Utils.LOG_INFO("Item Damage: "+iStack.getItemDamage()+" Max Damage: "+iStack.getMaxDamage());
		if (!world.isRemote){
		if(iStack.getItemDamage() == cooldownTime)	{
			if (entityHolding instanceof EntityPlayer){
				Utils.LOG_INFO("Foods Done.");
				((EntityPlayer) entityHolding).inventory.addItemStackToInventory(UtilsItems.getSimpleStack(output));
				((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
			}
		}else if(iStack.getItemDamage() < cooldownTime){
			iStack.setItemDamage(iStack.getItemDamage() + 1);
		}	
		if(Utils.divideXintoY(iStack.getItemDamage(), 150)){
			entityHolding.attackEntityFrom(DamageSource.onFire, 1);
		}	

		
		}
		super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"Warning: Very hot! Avoid direct handling..");	
			list.add(EnumChatFormatting.GRAY+"This food has "+(((int) stack.getMaxDamage()-(int) stack.getItemDamage())/20)+" seconds left.");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		return Utils.rgbtoHexValue(255, 128, 128);

	}
}
