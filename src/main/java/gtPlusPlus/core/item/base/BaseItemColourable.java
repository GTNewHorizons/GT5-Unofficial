package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTLanguageManager;

public class BaseItemColourable extends Item {

    private final EnumRarity rarity;
    private final EnumChatFormatting descColour;
    private final boolean hasEffect;
    public final int componentColour;

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
        return this.componentColour;
    }

    // 5
    /*
     * Name, Tab, Stack, Dmg, Description, Rarity, Text Colour, Effect
     */
    public BaseItemColourable(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize,
        final int maxDmg, final String description, final EnumRarity regRarity, final EnumChatFormatting colour,
        final boolean Effect, int rgb) {
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        this.setCreativeTab(creativeTab);
        this.setMaxStackSize(stackSize);
        this.setMaxDamage(maxDmg);
        this.setHasSubtypes(true);
        this.rarity = regRarity;
        GTLanguageManager.addStringLocalization("gtplusplus." + this.getUnlocalizedName() + ".tooltip", description);
        this.descColour = colour;
        this.hasEffect = Effect;
        this.componentColour = rgb;
        GameRegistry.registerItem(this, unlocalizedName);
    }

    // 6
    /*
     * Name, Tab, Stack, Dmg, Description, Rarity, Text Colour, Effect
     */
    public BaseItemColourable(final String unlocalizedName, final String displayName, final CreativeTabs creativeTab,
        final int stackSize, final int maxDmg, final String description, final EnumRarity regRarity,
        final EnumChatFormatting colour, final boolean Effect, int rgb) {
        this.setUnlocalizedName(unlocalizedName);
        GTLanguageManager.addStringLocalization("gtplusplus." + this.getUnlocalizedName() + ".name", displayName);
        this.setTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        this.setCreativeTab(creativeTab);
        this.setMaxStackSize(stackSize);
        this.setMaxDamage(maxDmg);
        this.rarity = regRarity;
        GTLanguageManager.addStringLocalization("gtplusplus." + this.getUnlocalizedName() + ".tooltip", description);
        this.descColour = colour;
        this.hasEffect = Effect;
        this.componentColour = rgb;
        GameRegistry.registerItem(this, unlocalizedName);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        list.add(
            this.descColour + GTLanguageManager.getTranslation("gtplusplus." + this.getUnlocalizedName() + ".tooltip"));
        // super.addInformation(stack, aPlayer, list, bool);
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
    public String getItemStackDisplayName(final ItemStack tItem) {
        if (!("gtplusplus." + this.getUnlocalizedName() + ".name")
            .equals(GTLanguageManager.getTranslation("gtplusplus." + this.getUnlocalizedName() + ".name"))) {
            return GTLanguageManager.getTranslation("gtplusplus." + this.getUnlocalizedName() + ".name");
        } else return super.getItemStackDisplayName(tItem);
    }
}
