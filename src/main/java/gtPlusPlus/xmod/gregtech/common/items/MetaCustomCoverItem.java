package gtPlusPlus.xmod.gregtech.common.items;

import static codechicken.nei.api.API.hideItem;
import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.common.covers.CoverToggleVisual;

public class MetaCustomCoverItem extends Item {

    protected final IIcon[] icons;
    private final String mModID;
    private final String mTextureSetName;
    protected final IIconContainer[] mTextures;
    private final short[][] mRGB;

    public MetaCustomCoverItem(String aModId, int aTextureCount, String aTextureSetName, IIconContainer[] aTextures,
        short[][] aRGB) {
        super();
        icons = new IIcon[aTextureCount];
        mModID = aModId;
        mTextureSetName = Utils.sanitizeString(aTextureSetName);
        mTextures = aTextures;
        mRGB = aRGB;
        this.setTextureName(GTPlusPlus.ID + ":" + "itemPlate");
        this.setHasSubtypes(true);
        String unlocalizedName = "itemCustomMetaCover." + mModID + "." + mTextureSetName;
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setMaxStackSize(1);
        GameRegistry.registerItem(this, unlocalizedName);
        registerCover();
        Logger.INFO(
            "[Covers] Generated Custom covers for " + mModID
                + " using "
                + aTextureCount
                + " textures from "
                + mTextureSetName
                + ".");
    }

    public boolean hide() {
        return true;
    }

    private void registerCover() {
        for (int i = 0; i < icons.length; i++) {
            ItemStack thisStack = new ItemStack(this, 1, i);
            if (i > 0 && hide()) {
                hideItem(thisStack);
            }
            CoverRegistry.registerCover(thisStack, TextureFactory.of(mTextures[i]), CoverToggleVisual::new);
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < icons.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + stack.getItemDamage();
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {
        return StringUtils.capitalize(mTextureSetName) + " (" + tItem.getItemDamage() + ")"; // super.getItemStackDisplayName(tItem);
    }

    private static boolean createNBT(ItemStack rStack) {
        final NBTTagCompound tagMain = new NBTTagCompound();
        final NBTTagCompound tagNBT = new NBTTagCompound();
        tagNBT.setLong("Damage", 0);
        tagNBT.setBoolean("AllowConnections", false);
        tagMain.setTag("CustomCoverMeta", tagNBT);
        rStack.setTagCompound(tagMain);
        return true;
    }

    public static long getCoverDamage(final ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("CustomCoverMeta");
            if (aNBT != null) {
                return aNBT.getLong("Damage");
            }
        } else {
            createNBT(aStack);
        }
        return 0L;
    }

    public static boolean setCoverDamage(final ItemStack aStack, final long aDamage) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("CustomCoverMeta");
            if (aNBT != null) {
                aNBT.setLong("Damage", aDamage);
                return true;
            }
        }
        return false;
    }

    public static boolean getCoverConnections(final ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("CustomCoverMeta");
            if (aNBT != null) {
                return aNBT.getBoolean("AllowConnections");
            }
        } else {
            createNBT(aStack);
        }
        return false;
    }

    public static boolean setCoverConnections(final ItemStack aStack, final boolean aConnections) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("CustomCoverMeta");
            if (aNBT != null) {
                aNBT.setBoolean("AllowConnections", aConnections);
                return true;
            }
        }
        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            createNBT(stack);
        }
        double currentDamage = getCoverDamage(stack);
        double meta = stack.getItemDamage() == 0 ? 50 : 2500;
        return currentDamage / meta;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (KeyboardUtils.isShiftKeyDown()) {
            boolean con = getCoverConnections(stack);
            setCoverConnections(stack, !con);
        }
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        list.add(
            EnumChatFormatting.GRAY + (getCoverConnections(stack)
                ? StatCollector.translateToLocal("gtpp.tooltip.cover_item.connection.allow")
                : StatCollector.translateToLocal("gtpp.tooltip.cover_item.connection.deny")));
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gtpp.tooltip.cover_item.change_state"));
        super.addInformation(stack, player, list, bool);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public Item setFull3D() {
        // TODO Auto-generated method stub
        return super.setFull3D();
    }

    @Override
    public boolean isFull3D() {
        // TODO Auto-generated method stub
        return super.isFull3D();
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
        if (this.mRGB == null) {
            return super.getColorFromItemStack(stack, HEX_OxFFFFFF);
        }
        int aMeta = stack.getItemDamage();
        return Utils.rgbtoHexValue(mRGB[aMeta][0], mRGB[aMeta][1], mRGB[aMeta][2]);
    }
}
