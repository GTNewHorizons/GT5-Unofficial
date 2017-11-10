package gtPlusPlus.core.item.bauble;

import java.util.List;

import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.nbt.ModularArmourUtils;
import gtPlusPlus.core.util.nbt.ModularArmourUtils.BT;
import gtPlusPlus.core.util.nbt.ModularArmourUtils.Modifiers;
import gtPlusPlus.core.util.nbt.NBTUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class ModularBauble extends BaseBauble{


	public ModularBauble() {
		super(BaubleType.AMULET, "Modular Bauble");
		this.setTextureName(CORE.MODID + ":" + "itemKeyGold");
	}

	@Override
	void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {

		//Get Stats
		int mStatlevel = 0;
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_DAMAGE)) > 0){
			if (mStatlevel == 1){
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "AD"+mStatlevel, 1, 0));
			}
			else if (mStatlevel == 2){
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "AD"+mStatlevel, 2, 0));
			}
			else if (mStatlevel == 3){
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "AD"+mStatlevel, 4, 0));
			}
			else if (mStatlevel == 4){
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "AD"+mStatlevel, 8, 0));
			}
			else if (mStatlevel == 5){
				attributes.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "AD"+mStatlevel, 16, 0));
			}
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_DEF)) > 0){
			if (mStatlevel == 1){
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "BD"+mStatlevel, 1, 0));
			}
			else if (mStatlevel == 2){
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "BD"+mStatlevel, 2, 0));
			}
			else if (mStatlevel == 3){
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "BD"+mStatlevel, 3, 0));
			}
			else if (mStatlevel == 4){
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "BD"+mStatlevel, 6, 0));
			}
			else if (mStatlevel == 5){
				attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "BD"+mStatlevel, 10, 0));
			}
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_HP)) > 0){
			if (mStatlevel == 1){
				attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "HP"+mStatlevel, 15, 0));
			}
			else if (mStatlevel == 2){
				attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "HP"+mStatlevel, 20, 0));
			}
			else if (mStatlevel == 3){
				attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "HP"+mStatlevel, 25, 0));
			}
			else if (mStatlevel == 4){
				attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "HP"+mStatlevel, 30, 0));
			}
			else if (mStatlevel == 5){
				attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "HP"+mStatlevel, 40, 0));
			}
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_SPEED)) > 0){
			if (mStatlevel == 1){
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "SP"+mStatlevel, 1, 0));
			}
			else if (mStatlevel == 2){
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "SP"+mStatlevel, 2, 0));
			}
			else if (mStatlevel == 3){
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "SP"+mStatlevel, 3, 0));
			}
			else if (mStatlevel == 4){
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "SP"+mStatlevel, 4, 0));
			}
			else if (mStatlevel == 5){
				attributes.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "SP"+mStatlevel, 5, 0));
			}
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_MINING)) > 0){
		}
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(stack, Modifiers.BOOST_HOLY)) > 0){
		}

	}

	@SuppressWarnings({ "unchecked"})
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
		if (NBTUtils.getBotanicaSoulboundOwner(stack) != null){
			if (!NBTUtils.getBotanicaSoulboundOwner(stack).equals("")){
				list.add(EnumChatFormatting.GRAY+"Relic Owner: "+EnumChatFormatting.GREEN+NBTUtils.getBotanicaSoulboundOwner(stack)+EnumChatFormatting.GRAY+".");	
			}
		}

		super.addInformation(stack, player, list, bool);
	}

	@Override
	public boolean addDamageNegation(DamageSource damageSource,ItemStack aStack) {	

		this.clearDamageNegation();
		int mStatlevel = 0;
		if ((mStatlevel = ModularArmourUtils.getModifierLevel(aStack, Modifiers.BOOST_HOLY)) > 0){
			addDamageNegation(DamageSource.cactus);
			if (mStatlevel == 1){
				addDamageNegation(DamageSource.inWall);
			}
			else if (mStatlevel == 2){
				addDamageNegation(DamageSource.drown);
			}
			else if (mStatlevel == 3){
				addDamageNegation(DamageSource.starve);
			}
			else if (mStatlevel == 4){
				addDamageNegation(DamageSource.fall);
			}
			else if (mStatlevel == 5){
				addDamageNegation(DamageSource.lava);
			}
		}		
		return super.addDamageNegation(damageSource, null);
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

		String mOwner;
		if (NBTUtils.getBotanicaSoulboundOwner(arg0) == null || NBTUtils.getBotanicaSoulboundOwner(arg0).equals("")){
			return true;
		}
		else if ((mOwner = NBTUtils.getBotanicaSoulboundOwner(arg0)) != null){
			String mPlayerName = arg1.getCommandSenderName();
			if (mOwner.toLowerCase().equals(mPlayerName.toLowerCase())){
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}

	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return ModularArmourUtils.getBaubleType(arg0);
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer){
			if (NBTUtils.getBotanicaSoulboundOwner(stack) == null || NBTUtils.getBotanicaSoulboundOwner(stack).equals("")){
				NBTUtils.setBotanicaSoulboundOwner(stack, entity.getCommandSenderName());
			}	
		}
		super.onEquipped(stack, entity);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		// TODO Auto-generated method stub
		super.onUnequipped(stack, player);
	}


	@SideOnly(Side.CLIENT)
	private IIcon mTextureAmulet;
	@SideOnly(Side.CLIENT)
	private IIcon mTextureRing;
	@SideOnly(Side.CLIENT)
	private IIcon mTextureBelt;

	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		// TODO Auto-generated method stub
		return super.getIconFromDamage(p_77617_1_);
	}

	@Override
	public IIcon getIconIndex(ItemStack p_77650_1_) {
		// TODO Auto-generated method stub
		return super.getIconIndex(p_77650_1_);
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_) {
		// TODO Auto-generated method stub
		return super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
	}

	@Override
	protected String getIconString() {
		// TODO Auto-generated method stub
		return super.getIconString();
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		// TODO Auto-generated method stub
		return super.getIcon(stack, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		if (usingItem == null) { return itemIcon; }
		try {
			if (ModularArmourUtils.getBaubleType(stack) == BaubleType.AMULET){
				return iconArray[0];
			}
			if (ModularArmourUtils.getBaubleType(stack) == BaubleType.RING){
				return iconArray[1];
			}
			if (ModularArmourUtils.getBaubleType(stack) == BaubleType.BELT){
				return iconArray[2];
			}
			else {
				return iconArray[0];
			}
		}
		catch (Throwable t){
			return itemIcon;
		}
	}


	private IIcon iconArray[] = new IIcon[3];

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		// you cannot initialize iconArray when declared nor in the constructor, as it is client-side only, so do it here:
		if (LoadedMods.Thaumcraft){
			iconArray[0] = register.registerIcon("thaumcraft" + ":" + "bauble_amulet");
			iconArray[1] = register.registerIcon("thaumcraft" + ":" + "bauble_ring");
			iconArray[2] = register.registerIcon("thaumcraft" + ":" + "bauble_belt");
		}
		else {
			iconArray[0] = register.registerIcon("miscutils" + ":" + "itemHeavyPlate");
			iconArray[1] = register.registerIcon("baubles" + ":" + "ring");
			iconArray[2] = register.registerIcon("miscutils" + ":" + "itemPineapple");
		}	
	}


}
