package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;

public class ItemGenericToken extends CoreItem {

    public final HashMap<Integer, String> mLocalNames;
    public final HashMap<Integer, Integer> mMaxStackSizes;
    public final HashMap<Integer, String[]> mDescriptionArrays;
    public final HashMap<Integer, EnumRarity> mRarities;
    public final HashMap<Integer, EnumChatFormatting> mCustomNameColours;
    public final HashMap<Integer, IIcon> mIcons;
    private final String mTextureDir;

    public ItemGenericToken() {
        this("itemGenericToken", "Token", new String[] { "Can be reclaimed in some way, shape or form" }, "token");
    }

    public ItemGenericToken(String aUnlocalName, String aInternalName, String[] aBaseTooltip, String aTextureDir) {
        super(
                aUnlocalName,
                aInternalName,
                AddToCreativeTab.tabMisc,
                64,
                1000,
                aBaseTooltip,
                EnumRarity.common,
                EnumChatFormatting.RESET,
                false,
                null);
        mLocalNames = new HashMap<>();
        mMaxStackSizes = new HashMap<>();
        mDescriptionArrays = new HashMap<>();
        mRarities = new HashMap<>();
        mCustomNameColours = new HashMap<>();
        mIcons = new HashMap<>();
        mTextureDir = aTextureDir;
        setMaxStackSize(64);
    }

    public boolean register(int id, String aLocalName, int aMaxStack, String aDescript) {
        return register(id, aLocalName, aMaxStack, new String[] { aDescript });
    }

    public boolean register(int id, String aLocalName, int aMaxStack, String[] aDescript) {
        return register(id, aLocalName, aMaxStack, aDescript, EnumRarity.common, EnumChatFormatting.RESET);
    }

    public boolean register(int id, String aLocalName, int aMaxStack, String[] aDescript, EnumRarity aRarity,
            EnumChatFormatting aCustomNameColour) {
        int[][] sizes = new int[2][6];
        sizes[0][0] = mLocalNames.size();
        sizes[0][1] = mMaxStackSizes.size();
        sizes[0][2] = mDescriptionArrays.size();
        sizes[0][3] = mRarities.size();
        sizes[0][4] = mCustomNameColours.size();
        // sizes[0][5] = mIcons.size();
        mLocalNames.put(id, aLocalName);
        GT_LanguageManager
                .addStringLocalization("gtplusplus." + this.getUnlocalizedName() + "." + id + ".name", aLocalName);
        mMaxStackSizes.put(id, aMaxStack);
        mDescriptionArrays.put(id, aDescript);
        for (int i = 0; i < aDescript.length; i++) {
            GT_LanguageManager.addStringLocalization(
                    "gtplusplus." + this.getUnlocalizedName() + "." + id + ".tooltip." + i,
                    aDescript[i]);
        }
        mRarities.put(id, aRarity);
        mCustomNameColours.put(id, aCustomNameColour);
        sizes[1][0] = mLocalNames.size();
        sizes[1][1] = mMaxStackSizes.size();
        sizes[1][2] = mDescriptionArrays.size();
        sizes[1][3] = mRarities.size();
        sizes[1][4] = mCustomNameColours.size();
        // sizes[1][5] = mIcons.size();
        boolean b = sizes[0][0] > sizes[1][0] && sizes[0][1] > sizes[1][1]
                && sizes[0][2] > sizes[1][2]
                && sizes[0][3] > sizes[1][3]
                && sizes[0][4] > sizes[1][4];
        return b;
    }

    // Handle Sub items
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item var1, final CreativeTabs aCreativeTab, final List aList) {
        for (int i = 0, j = mIcons.size(); i < j; i++) {
            final ItemStack tStack = new ItemStack(this, 1, i);
            aList.add(tStack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
        super.addInformation(stack, aPlayer, list, bool);
        for (int i = 0;; i++) {
            String tooltip = GT_LanguageManager.getTranslation(
                    "gtplusplus." + this
                            .getUnlocalizedNameInefficiently(stack) + "." + stack.getItemDamage() + ".tooltip." + i);
            if (!("gtplusplus." + this
                    .getUnlocalizedNameInefficiently(stack) + "." + stack.getItemDamage() + ".tooltip." + i)
                            .equals(tooltip)) {
                list.add(tooltip);
            } else break;
        }
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {
        String ret = GT_LanguageManager.getTranslation(
                "gtplusplus." + this.getUnlocalizedNameInefficiently(tItem) + "." + tItem.getItemDamage() + ".name");
        EnumChatFormatting format = mCustomNameColours.get(tItem.getItemDamage());
        if (format != null) {
            ret = format + ret;
        }
        return ret;
    }

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return mRarities.get(par1ItemStack.getItemDamage());
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, final int pass) {
        return false;
    }

    @Override
    public int getMetadata(int p_77647_1_) {
        return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 0D;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        if (aStack == null) {
            return maxStackSize;
        } else {
            Integer aSize = mMaxStackSizes.get(aStack.getItemDamage());
            if (aSize != null) {
                return aSize;
            } else {
                return maxStackSize;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister aIconRegister) {
        for (int i = 0, j = mLocalNames.size(); i < j; i++) {
            mIcons.put(i, aIconRegister.registerIcon(GTPlusPlus.ID + ":" + mTextureDir + "/" + i));
        }
    }

    @Override
    public final IIcon getIconFromDamage(final int aMetaData) {
        if (aMetaData < 0) {
            return null;
        }
        return mIcons.get(aMetaData);
    }
}
