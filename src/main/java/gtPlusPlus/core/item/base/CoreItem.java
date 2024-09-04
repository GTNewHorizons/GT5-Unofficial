package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTLanguageManager;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class CoreItem extends Item {

    private final EnumRarity rarity;
    private final EnumChatFormatting descColour;
    protected String itemName;
    private final boolean hasEffect;

    // Replace Item - What does this item turn into when held.
    private final ItemStack turnsInto;

    // 0
    /*
     * Name, Tab - 64 Stack, 0 Dmg
     */
    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab) {
        this(unlocalizedName, creativeTab, 64, 0); // Calls 3
    }

    // 0
    /*
     * Name, Tab - 64 Stack, 0 Dmg
     */
    public CoreItem(final String unlocalizedName, final String displayName, final CreativeTabs creativeTab) {
        this(unlocalizedName, creativeTab, 64, 0); // Calls 3
        this.itemName = displayName;
    }

    // 0.1
    /*
     * Name, Tab - 64 Stack, 0 Dmg
     */
    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final ItemStack OverrideItem) {
        this(
            unlocalizedName,
            creativeTab,
            64,
            0,
            new String[] {
                "This item will be replaced by another when held by a player, it is old and should not be used in recipes." },
            EnumRarity.uncommon,
            EnumChatFormatting.UNDERLINE,
            false,
            OverrideItem); // Calls 5
    }

    // 0.1
    /*
     * Name, Tab - 64 Stack, 0 Dmg
     */
    public CoreItem(final String unlocalizedName, final String displayName, final CreativeTabs creativeTab,
        final ItemStack OverrideItem) {
        this(
            unlocalizedName,
            creativeTab,
            64,
            0,
            new String[] {
                "This item will be replaced by another when held by a player, it is old and should not be used in recipes." },
            EnumRarity.uncommon,
            EnumChatFormatting.UNDERLINE,
            false,
            OverrideItem); // Calls 5
        this.itemName = displayName;
    }

    // 1
    /*
     * Name, Tab, Stack - 0 Dmg
     */
    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize) {
        this(unlocalizedName, creativeTab, stackSize, 0); // Calls 3
    }

    // 2
    /*
     * Name, Tab, Stack, Description - 0 Dmg
     */
    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize,
        final String[] description) {
        this(unlocalizedName, creativeTab, stackSize, 0, description); // Calls 4
    }

    // 3
    /*
     * Name, Tab, Stack, Dmg - Description
     */
    public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, String string) {
        this(unlocalizedName, creativeTab, stackSize, new String[] { string });
    }

    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize,
        final int maxDmg) {
        this(unlocalizedName, creativeTab, stackSize, maxDmg, new String[] {}); // Calls 4
    }

    // 4 //Not Rare + basic tooltip
    /*
     * Name, Tab, Stack, Dmg, Description
     */
    public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg, String string) {
        this(unlocalizedName, creativeTab, stackSize, maxDmg, new String[] { string });
    }

    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
        final String[] description) {
        this(
            unlocalizedName,
            creativeTab,
            stackSize,
            maxDmg,
            description,
            EnumRarity.common,
            EnumChatFormatting.GRAY,
            false,
            null); // Calls 4.5
    }

    // 4.5
    /*
     * Name, Tab, Stack, Dmg, Description, Text Colour - Common
     */
    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
        final String[] description, final EnumChatFormatting colour) {
        this(unlocalizedName, creativeTab, stackSize, maxDmg, description, EnumRarity.common, colour, false, null); // Calls
                                                                                                                    // 5
    }

    // 4.75
    /*
     * Name, Tab, Stack, Dmg, Description, Rarity - Gray text
     */
    public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg, String string,
        EnumRarity uncommon) {
        this(unlocalizedName, creativeTab, stackSize, maxDmg, new String[] { string }, uncommon);
    }

    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
        final String[] description, final EnumRarity rarity) {
        this(
            unlocalizedName,
            creativeTab,
            stackSize,
            maxDmg,
            description,
            rarity,
            EnumChatFormatting.GRAY,
            false,
            null); // Calls 5
    }

    // 5
    /*
     * Name, Tab, Stack, Dmg, Description, Rarity, Text Colour, Effect
     */
    public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
        final String[] description, final EnumRarity regRarity, final EnumChatFormatting colour, final boolean Effect,
        final ItemStack OverrideItem) {
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        this.setCreativeTab(creativeTab);
        this.setMaxStackSize(stackSize);
        this.setMaxDamage(maxDmg);
        this.rarity = regRarity;
        this.setItemDescription(description);
        this.descColour = colour != null ? colour : EnumChatFormatting.RESET;
        this.hasEffect = Effect;
        this.turnsInto = OverrideItem;
        GameRegistry.registerItem(this, unlocalizedName);
    }

    // 6
    /*
     * Name, Tab, Stack, Dmg, Description, Rarity, Text Colour, Effect
     */
    public CoreItem(final String unlocalizedName, final String displayName, final CreativeTabs creativeTab,
        final int stackSize, final int maxDmg, final String[] description, final EnumRarity regRarity,
        final EnumChatFormatting colour, final boolean Effect, final ItemStack OverrideItem) {
        this.setUnlocalizedName(unlocalizedName);
        this.itemName = displayName;
        this.setTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        this.setCreativeTab(creativeTab);
        this.setMaxStackSize(stackSize);
        this.setMaxDamage(maxDmg);
        this.rarity = regRarity;
        this.setItemDescription(description);
        this.descColour = colour != null ? colour : EnumChatFormatting.RESET;
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

                Logger.INFO("Replacing " + iStack.getDisplayName() + " with " + this.turnsInto.getDisplayName() + ".");
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

    public ItemStack getStack() {
        return ItemUtils.getSimpleStack(this);
    }

    public void setItemDescription(String[] description) {
        for (int i = 0; i < description.length; i++) {
            GTLanguageManager.addStringLocalization(
                "gtplusplus." + this.getUnlocalizedName() + ".tooltip" + "." + i,
                description[i]);
        }
    }
    /*
     * @Override public String getItemStackDisplayName(final ItemStack tItem) { if ((this.itemName == null) ||
     * this.itemName.equals("")) { return super.getItemStackDisplayName(tItem); } return this.itemName; }
     */
}
