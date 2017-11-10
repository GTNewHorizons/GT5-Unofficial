package gtPlusPlus.core.item.bauble;

import java.util.List;

import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.nbt.ModularArmourUtils;
import gtPlusPlus.core.util.nbt.ModularArmourUtils.BT;
import gtPlusPlus.core.util.nbt.ModularArmourUtils.Modifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;

public class ModularBauble extends BaseBauble{

	
	public ModularBauble() {
		super(BaubleType.AMULET, "Does Fancy Things.");
		addDamageNegation(DamageSource.wither);
		this.setTextureName(CORE.MODID + ":" + "itemKeyGold");
	}

	@Override
	void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "Bauble modifier", 40, 0));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		//Bauble Type
		if (ModularArmourUtils.getBaubleType(stack) == BaubleType.AMULET){
			list.add(EnumChatFormatting.GRAY+"Current Form: "+EnumChatFormatting.RED+"Amulet"+EnumChatFormatting.GRAY+".");	
			list.add(EnumChatFormatting.GRAY+"You can change this into a Ring or a Belt.");		
		}
		else if (ModularArmourUtils.getBaubleType(stack) == BaubleType.RING){
			list.add(EnumChatFormatting.GRAY+"Current Form: "+EnumChatFormatting.RED+"Ring"+EnumChatFormatting.GRAY+".");	
			list.add(EnumChatFormatting.GRAY+"You can change this into an Amulet or a Belt.");		
		}
		else if (ModularArmourUtils.getBaubleType(stack) == BaubleType.BELT){
			list.add(EnumChatFormatting.GRAY+"Current Form: "+EnumChatFormatting.RED+"Belt"+EnumChatFormatting.GRAY+".");	
			list.add(EnumChatFormatting.GRAY+"You can change this into a Ring or an Amulet.");		
		}
		
		//Get Stats
		int mStatlevel = 0;
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_DAMAGE)) > 0){
			list.add(EnumChatFormatting.GRAY+"Damage Boost: "+EnumChatFormatting.DARK_RED+mStatlevel+EnumChatFormatting.GRAY+".");	
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_DEF)) > 0){
			list.add(EnumChatFormatting.GRAY+"Defence Boost: "+EnumChatFormatting.BLUE+mStatlevel+EnumChatFormatting.GRAY+".");	
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_HP)) > 0){
			list.add(EnumChatFormatting.GRAY+"Health Boost: "+EnumChatFormatting.RED+mStatlevel+EnumChatFormatting.GRAY+".");	
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_SPEED)) > 0){
			list.add(EnumChatFormatting.GRAY+"Speed Boost: "+EnumChatFormatting.WHITE+mStatlevel+EnumChatFormatting.GRAY+".");	
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_MINING)) > 0){
			list.add(EnumChatFormatting.GRAY+"Mining Boost: "+EnumChatFormatting.DARK_GRAY+mStatlevel+EnumChatFormatting.GRAY+".");	
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_HOLY)) > 0){
			list.add(EnumChatFormatting.GRAY+"Holy Boost: "+EnumChatFormatting.GOLD+mStatlevel+EnumChatFormatting.GRAY+".");	
		}
		
		super.addInformation(stack, player, list, bool);
	}

	@Override
	public boolean addDamageNegation(DamageSource damageSource) {
		// TODO Auto-generated method stub
		return super.addDamageNegation(damageSource);
	}

	@Override
	public boolean canEquip(ItemStack arg0, EntityLivingBase arg1) {
		if (ModularArmourUtils.getBaubleTypeID(arg0) == BT.TYPE_AMULET.getID()){
			this.SetBaubleType(BT.TYPE_AMULET);
		}
		else if (ModularArmourUtils.getBaubleTypeID(arg0) == BT.TYPE_RING.getID()){
			this.SetBaubleType(BT.TYPE_RING);
		}
		else if (ModularArmourUtils.getBaubleTypeID(arg0) == BT.TYPE_BELT.getID()){
			this.SetBaubleType(BT.TYPE_BELT);
		}
		else {
			this.SetBaubleType(BT.TYPE_RING);
		}
		return super.canEquip(arg0, arg1);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return ModularArmourUtils.getBaubleType(arg0);
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase entity) {
		// TODO Auto-generated method stub
		super.onEquipped(stack, entity);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		// TODO Auto-generated method stub
		super.onUnequipped(stack, player);
	}
	
	
	
	
}
