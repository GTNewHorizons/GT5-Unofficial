package gtPlusPlus.core.item.general;

import java.util.List;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.events.CustomMovementHandler;
import gtPlusPlus.core.handler.events.SneakManager;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;

@Optional.InterfaceList(value = {@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles"), @Optional.Interface(iface = "baubles.api.BaubleType", modid = "Baubles")})
public class ItemSlowBuildingRing extends Item implements IBauble{

	private final String unlocalizedName = "SlowBuildingRing";
	CustomMovementHandler x;

	public ItemSlowBuildingRing(){
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setUnlocalizedName(this.unlocalizedName);
		this.setMaxStackSize(1);
		this.setTextureName(CORE.MODID + ":" + "itemSlowBuildersRing");
		ItemUtils.getSimpleStack(this);
		GameRegistry.registerItem(this, this.unlocalizedName);
	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World worldObj, final Entity player, final int p_77663_4_, final boolean p_77663_5_) {
		if (worldObj.isRemote) {
			return;
		}
		if (player instanceof EntityPlayer){
			for (final ItemStack is : ((EntityPlayer) player).inventory.mainInventory) {
				if (is == itemStack) {
					continue;
				}
				if (is != null) {


				}
			}
		}
		super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		return (EnumChatFormatting.YELLOW+"Slow Building Ring"+EnumChatFormatting.GRAY);
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack)
	{
		return false;
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add("");
		list.add(EnumChatFormatting.GREEN+"Worn as a Ring within Baubles. Prevents you from sprinting."+EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GREEN+"Movement speed reduced to crouch speed."+EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GREEN+"Press shift while worn to toggle crouch on/off."+EnumChatFormatting.GRAY);
		list.add("");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public boolean canEquip(final ItemStack arg0, final EntityLivingBase arg1) {
		return true;
	}

	@Override
	public boolean canUnequip(final ItemStack arg0, final EntityLivingBase arg1) {
		return true;
	}

	@Override
	public BaubleType getBaubleType(final ItemStack arg0) {
		return BaubleType.RING;
	}

	@Override //TODO
	public void onEquipped(final ItemStack arg0, final EntityLivingBase arg1) {
		doEffect(arg1);
	}

	@Override //TODO
	public void onUnequipped(final ItemStack arg0, final EntityLivingBase arg1) {
		SneakManager.setSprintingStateON();
		SneakManager.setCrouchingStateOFF();
	}

	@Override //TODO
	public void onWornTick(final ItemStack arg0, final EntityLivingBase arg1) {
		doEffect(arg1);
	}

	private static void doEffect(final EntityLivingBase arg1){
		if (arg1.worldObj.isRemote){
			if (!arg1.isSneaking()){
				arg1.setSneaking(true);
				Minecraft.getMinecraft().thePlayer.setSneaking(true);
				SneakManager.setSprintingStateOFF();
				SneakManager.setCrouchingStateON();
			}
			else if (arg1.isSneaking()){
				arg1.setSprinting(false);
				Minecraft.getMinecraft().thePlayer.setSprinting(true);
				SneakManager.setSprintingStateOFF();
				SneakManager.setCrouchingStateON();
			}
		}
	}

}
