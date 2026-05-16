package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTLanguageManager;

public class CoreItem extends Item {

    private final EnumRarity rarity;
    private final boolean hasEffect;
    // Replace Item - What does this item turn into when held.
    private final ItemStack turnsInto;

    public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, String string) {
        this(unlocalizedName, creativeTab, stackSize, 0, new String[] { string });
    }

    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
        final String[] description) {
        this(unlocalizedName, creativeTab, stackSize, maxDmg, description, EnumRarity.common, false, null); // Calls 4.5
    }

    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
        final String[] description, final EnumRarity regRarity, final boolean Effect, final ItemStack OverrideItem) {
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        this.setCreativeTab(creativeTab);
        this.setMaxStackSize(stackSize);
        this.setMaxDamage(maxDmg);
        this.rarity = regRarity;
        this.setItemDescription(description);
        this.hasEffect = Effect;
        this.turnsInto = OverrideItem;
        GameRegistry.registerItem(this, unlocalizedName);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        for (int i = 0;; i++) {
            String tooltip = GTLanguageManager
                .getTranslation("gtplusplus." + this.getUnlocalizedName() + ".tooltip" + "." + i);
            if (!("gtplusplus." + this.getUnlocalizedName() + ".tooltip" + "." + i).equals(tooltip)) {
                list.add(tooltip);
            } else break;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack par1ItemStack) {
        return this.rarity;
    }

    @Override
    public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
        return this.hasEffect;
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        if (this.turnsInto != null) {
            if (entityHolding instanceof EntityPlayer) {

                final ItemStack tempTransform = this.turnsInto;
                if (iStack.stackSize == 64) {
                    tempTransform.stackSize = 64;
                    ((EntityPlayer) entityHolding).inventory.addItemStackToInventory((tempTransform));
                    for (int l = 0; l < 64; l++) {
                        ((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
                    }

                } else {
                    tempTransform.stackSize = 1;
                    ((EntityPlayer) entityHolding).inventory.addItemStackToInventory((tempTransform));
                    ((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
                }
            }
        }
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    public void setItemDescription(String[] description) {
        for (int i = 0; i < description.length; i++) {
            GTLanguageManager.addStringLocalization(
                "gtplusplus." + this.getUnlocalizedName() + ".tooltip" + "." + i,
                description[i]);
        }
    }
}
