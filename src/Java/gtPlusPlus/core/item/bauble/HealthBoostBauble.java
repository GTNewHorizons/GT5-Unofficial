package gtPlusPlus.core.item.bauble;

import java.util.List;

import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;

public class HealthBoostBauble extends BaseBauble{

	
	public HealthBoostBauble() {
		super(BaubleType.AMULET, "The Key to the City");
		addDamageNegation(DamageSource.wither);
		this.setTextureName(CORE.MODID + ":" + "itemKeyGold");
	}

	@Override
	void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "Bauble modifier", 40, 0));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		list.add(EnumChatFormatting.GRAY+"You feel like you're on top of the world.");
		list.add(EnumChatFormatting.GRAY+"Wear it around your neck like an amulet.");
		super.addInformation(stack, player, list, bool);
	}
	
	
}
