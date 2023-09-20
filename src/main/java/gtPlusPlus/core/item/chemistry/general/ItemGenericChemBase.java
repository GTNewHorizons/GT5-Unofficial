package gtPlusPlus.core.item.chemistry.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class ItemGenericChemBase extends Item {

    protected final IIcon base[];

    private final int aMetaSize = 34;

    /*
     * 0 - Red Metal Catalyst //FeCu 1 - Yellow Metal Catalyst //WNi 2 - Blue Metal Catalyst //CoTi 3 - Orange Metal
     * Catalyst //Vanadium Pd 4 - Purple Metal Catalyst //IrIdium Ruthenium 5 - Brown Metal Catalyst //NiAl 6 - Pink
     * Metal Catalyst //PtRh 7 - Alumina Grinding Ball 8 - Soapstone Grinding Ball 9 - Sodium Ethoxide // 2 Sodium + 1
     * Ethanol | 2 C2H5OH + 2 Na → 2 C2H5ONa + H2 10 - Sodium Ethyl Xanthate //CH3CH2ONa + CS2 → CH3CH2OCS2Na 11 -
     * Potassium Ethyl Xanthate //CH3CH2OH + CS2 + KOH → CH3CH2OCS2K + H2O 12 - Potassium Hydroxide // KOH 13 -
     * Formaldehyde Catalyst //Fe16V1 14 - Solid Acid Catalyst //H2SO4 15 - Infinite Mutation Catalyst (for Mutated
     * Living Solder) 16 - Platinum Group Catalyst (for platline skip) 17 - Plastic Polymer Catalyst (for early plastics
     * skip) 18 - Rubber Polymer Catalyst (for early rubbers skip) 19 - Adhesion Promoter Catalyst (for glue/solder
     * skip) 20 - Tita-Tungsten Indium Catalyst (for titanium/tungsten/indium skip) 21 - Radioactivity Catalyst (for
     * thorium/uranium/plutonium skip) 22 - Rare-Earth Group Catalyst (for monaline skip) 23 - Simple Naquadah Catalyst
     * (for early naqline skip) 24 - Advanced Naquadah Catalyst (for late naqline skip) 25 - Raw Intelligence Catalyst
     * (for stem cells skip) 26 - Ultimate Plasticizer Catalyst (for late plastics skip) 27 - Biological Intelligence
     * Catalyst (for bio cells skip) 28 - Temporal Harmonizer Catalyst (for Eternity processing) 29 - Limpid Water
     * Catalyst (for early waterline skip) 30 - Flawless Water Catalyst (for advanced waterline skip) 33 - Algagenic
     * Growth Promoter Catalyst (for seaweed skip)
     */

    public ItemGenericChemBase() {
        this.setHasSubtypes(true);
        this.setNoRepair();
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        base = new IIcon[aMetaSize];
        this.setUnlocalizedName("BasicGenericChemItem");
        GameRegistry.registerItem(this, this.getUnlocalizedName());
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        if (ItemUtils.isMillingBall(stack)) {
            return 16;
        }
        return super.getItemStackLimit(stack);
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

    @Override
    public void getSubItems(Item aItem, CreativeTabs p_150895_2_, List aList) {
        for (int i = 0; i < aMetaSize; i++) {
            aList.add(ItemUtils.simpleMetaStack(aItem, i, 1));
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
        for (int i = 0; i < this.aMetaSize; i++) {
            String aPath = GTPlusPlus.ID + ":" + "science/general/MetaItem1/" + i;
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
        if (ItemUtils.isMillingBall(aStack)) {
            if (aStack.getTagCompound() == null || aStack.getTagCompound().hasNoTags()) {
                createMillingBallNBT(aStack);
            }
            double currentDamage = getMillingBallDamage(aStack);
            return currentDamage / getMaxBallDurability(aStack);
        } else if (ItemUtils.isCatalyst(aStack)) {
            if (aStack.getTagCompound() == null || aStack.getTagCompound().hasNoTags()) {
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
        if (ItemUtils.isMillingBall(aStack)) {
            list.add(EnumChatFormatting.GRAY + "Tumble Tumble Tumble");
            aMaxDamage = getMillingBallMaxDamage(aStack);
            aDamageSegment = aMaxDamage / 5;
            aDam = aMaxDamage - getMillingBallDamage(aStack);
            aHasSpecialTooltips = true;
        }
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
        if (ItemUtils.isMillingBall(aStack)) {
            int aDam = getMillingBallDamage(aStack);
            if (aDam > 0) {
                return true;
            }
        } else if (ItemUtils.isCatalyst(aStack)) {
            int aDam = getCatalystDamage(aStack);
            if (aDam > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean createMillingBallNBT(ItemStack rStack) {
        final NBTTagCompound tagMain = new NBTTagCompound();
        final NBTTagCompound tagNBT = new NBTTagCompound();
        tagNBT.setLong("Damage", 0);
        tagNBT.setLong("MaxDamage", getMaxBallDurability(rStack));
        tagMain.setTag("MillingBall", tagNBT);
        rStack.setTagCompound(tagMain);
        return true;
    }

    public static int getMillingBallDamage(ItemStack aStack) {
        if (aStack.getTagCompound() == null || aStack.getTagCompound().hasNoTags()) {
            createMillingBallNBT(aStack);
        }
        NBTTagCompound aNBT = aStack.getTagCompound();
        return aNBT.getCompoundTag("MillingBall").getInteger("Damage");
    }

    public static int getMillingBallMaxDamage(ItemStack aStack) {
        if (aStack.getTagCompound() == null || aStack.getTagCompound().hasNoTags()) {
            createMillingBallNBT(aStack);
        }
        NBTTagCompound aNBT = aStack.getTagCompound();
        return aNBT.getCompoundTag("MillingBall").getInteger("MaxDamage");
    }

    public static void setMillingBallDamage(ItemStack aStack, int aAmount) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        aNBT = aNBT.getCompoundTag("MillingBall");
        aNBT.setInteger("Damage", aAmount);
    }

    public static int getMaxBallDurability(ItemStack aStack) {
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mMillingBallAlumina, true)) {
            return 100;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mMillingBallSoapstone, true)) {
            return 50;
        }
        return 0;
    }

    public static boolean createCatalystNBT(ItemStack rStack) {
        final NBTTagCompound tagMain = new NBTTagCompound();
        final NBTTagCompound tagNBT = new NBTTagCompound();
        tagNBT.setLong("Damage", 0);
        tagNBT.setLong("MaxDamage", getMaxCatalystDurability(rStack));
        tagMain.setTag("catalyst", tagNBT);
        rStack.setTagCompound(tagMain);
        return true;
    }

    public static int getCatalystDamage(ItemStack aStack) {
        if (aStack.getTagCompound() == null || aStack.getTagCompound().hasNoTags()) {
            createCatalystNBT(aStack);
        }
        NBTTagCompound aNBT = aStack.getTagCompound();
        return aNBT.getCompoundTag("catalyst").getInteger("Damage");
    }

    public static int getCatalystMaxDamage(ItemStack aStack) {
        if (aStack.getTagCompound() == null || aStack.getTagCompound().hasNoTags()) {
            createCatalystNBT(aStack);
        }
        NBTTagCompound aNBT = aStack.getTagCompound();
        return aNBT.getCompoundTag("catalyst").getInteger("MaxDamage");
    }

    public static void setCatalystDamage(ItemStack aStack, int aAmount) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        aNBT = aNBT.getCompoundTag("catalyst");
        aNBT.setInteger("Damage", aAmount);
    }

    public static int getMaxCatalystDurability(ItemStack aStack) {
        return 50;
    }
}
