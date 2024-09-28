package gtPlusPlus.plugin.agrichem.item.algae;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.OreDictUtils;

public class ItemAgrichemBase extends Item {

    protected final IIcon[] base;

    /*
     * 0 - Algae Biomass 1 - Green Algae Biomass 2 - Brown Algae Biomass 3 - Golden-Brown Algae Biomass 4 - Red Algae
     * Biomass 5 - Cellulose Fiber 6 - Golden-Brown Cellulose Fiber 7 - Red Cellulose Fiber 8 - Compost 9 - Wood Pellet
     * 10 - Wood Brick 11 - Cellulose Pulp 12 - Raw Bio Resin 13 - Catalyst Carrier 14 - Green Metal Catalyst 15 -
     * Alginic Acid 16 - Alumina 17 - Aluminium Pellet 18 - Sodium Aluminate 19 - Sodium Hydroxide // Exists in Newer GT
     * 20 - Sodium Carbonate 21 - Lithium Chloride 22 - Pellet Mold 23 - Clean Aluminium Mix 24 - Pinecone 25 - Crushed
     * Pine
     */

    public ItemAgrichemBase() {
        this.setHasSubtypes(true);
        this.setNoRepair();
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        base = new IIcon[26];
        this.setUnlocalizedName("BasicAgrichemItem");
        GameRegistry.registerItem(this, this.getUnlocalizedName());
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return super.shouldRotateAroundWhenRendering();
    }

    @Override
    public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_,
        boolean p_77663_5_) {
        super.onUpdate(p_77663_1_, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        return super.getItemStackDisplayName(aStack);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return EnumRarity.common;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    private static boolean mHasCheckedForSodiumHydroxide = false;
    private static boolean mShowSodiumHydroxide = true;

    private static boolean checkSodiumHydroxide() {
        if (mHasCheckedForSodiumHydroxide) {
            return mShowSodiumHydroxide;
        } else {
            if (OreDictUtils.containsValidEntries("dustSodiumHydroxide_GT5U")
                || OreDictUtils.containsValidEntries("dustSodiumHydroxide")) {
                List<ItemStack> aTest = OreDictionary.getOres("dustSodiumHydroxide", false);
                if (aTest.isEmpty()) {
                    aTest = OreDictionary.getOres("dustSodiumHydroxide_GT5U", false);
                    if (!aTest.isEmpty()) {
                        mShowSodiumHydroxide = false;
                    }
                } else {
                    mShowSodiumHydroxide = false;
                }
            }
        }
        mHasCheckedForSodiumHydroxide = true;
        return mShowSodiumHydroxide;
    }

    @Override
    public void getSubItems(Item aItem, CreativeTabs p_150895_2_, List aList) {
        for (int i = 0; i < base.length; i++) {
            if (i == 19) {
                // Only show if it doesn't exist.
                if (checkSodiumHydroxide()) {
                    aList.add(ItemUtils.simpleMetaStack(aItem, i, 1));
                }
            } else {
                aList.add(ItemUtils.simpleMetaStack(aItem, i, 1));
            }
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
        return false;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 0;
    }

    @Override
    public void registerIcons(final IIconRegister u) {
        for (int i = 0; i < this.base.length; i++) {
            String aPath = GTPlusPlus.ID + ":" + "bioscience/MetaItem1/" + i;
            this.base[i] = u.registerIcon(aPath);
        }
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        return this.base[damage];
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return this.base[damage];
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return this.base[stack.getItemDamage()];
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return this.base[stack.getItemDamage()];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack aStack) {
        if (ItemUtils.isCatalyst(aStack)) {
            if (aStack.getTagCompound() == null || aStack.getTagCompound()
                .hasNoTags()) {
                createCatalystNBT(aStack);
            }
            double currentDamage = getCatalystDamage(aStack);
            return currentDamage / getCatalystMaxDamage(aStack);
        } else {
            return 1D;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack aStack, EntityPlayer player, List list, boolean bool) {
        boolean aHasSpecialTooltips = false;
        int aMaxDamage = 0;
        int aDamageSegment = 0;
        int aDam = 0;
        EnumChatFormatting durability = EnumChatFormatting.GRAY;
        if (ItemUtils.isCatalyst(aStack)) {
            list.add(EnumChatFormatting.GRAY + "Active Reaction Agent");
            aMaxDamage = getCatalystMaxDamage(aStack);
            aDamageSegment = aMaxDamage / 5;
            aDam = aMaxDamage - getCatalystDamage(aStack);
            aHasSpecialTooltips = true;
        }
        if (aHasSpecialTooltips) {
            if (aDam > aDamageSegment * 3) {
                durability = EnumChatFormatting.GREEN;
            } else if (aDam > aDamageSegment * 2) {
                durability = EnumChatFormatting.YELLOW;
            } else if (aDam > aDamageSegment) {
                durability = EnumChatFormatting.GOLD;
            } else if (aDam >= 0) {
                durability = EnumChatFormatting.RED;
            }
            list.add(durability + "" + (aDam) + EnumChatFormatting.GRAY + " / " + aMaxDamage);
        }
        super.addInformation(aStack, player, list, bool);
    }

    @Override
    public boolean showDurabilityBar(ItemStack aStack) {
        if (ItemUtils.isCatalyst(aStack)) {
            int aDam = getCatalystDamage(aStack);
            return aDam > 0;
        }
        return false;
    }

    public static boolean createCatalystNBT(ItemStack rStack) {
        return ItemGenericChemBase.createCatalystNBT(rStack);
    }

    public static int getCatalystDamage(ItemStack aStack) {
        return ItemGenericChemBase.getCatalystDamage(aStack);
    }

    public static int getCatalystMaxDamage(ItemStack aStack) {
        return ItemGenericChemBase.getCatalystMaxDamage(aStack);
    }

    public static void setCatalystDamage(ItemStack aStack, int aAmount) {
        ItemGenericChemBase.setCatalystDamage(aStack, aAmount);
    }

    public static int getMaxCatalystDurability(ItemStack aStack) {
        return ItemGenericChemBase.getMaxCatalystDurability(aStack);
    }
}
