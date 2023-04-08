package gtPlusPlus.core.item.bauble;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import baubles.api.BaubleType;

import com.google.common.collect.Multimap;

public class HealthBoostBauble extends BaseBauble {

    public HealthBoostBauble() {
        super(BaubleType.AMULET, "The Key to the City");
        addDamageNegation(DamageSource.wither);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemKeyGold");
    }

    @Override
    void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
        attributes.put(
                SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(),
                new AttributeModifier(getBaubleUUID(stack), "Bauble modifier", 40, 0));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.thekeytothecity.tooltip.0"));
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.thekeytothecity.tooltip.1"));
        super.addInformation(stack, player, list, bool);
    }
}
