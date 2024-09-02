package gtPlusPlus.xmod.gregtech.common.items;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GTMultiTexture;
import gregtech.api.objects.GTRenderedTexture;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
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
        // CommonProxy.registerItemRendererGlobal(this, new CustomItemBlockRenderer());
        for (int i = 0; i < icons.length; i++) {
            ItemStack thisStack = ItemUtils.simpleMetaStack(this, i, 1);
            if (i > 0 && hide()) {
                ItemUtils.hideItemFromNEI(thisStack);
            }
            GregTechAPI.registerCover(
                thisStack,
                new GTMultiTexture(new ITexture[] { new GTRenderedTexture(mTextures[i]) }),
                new CoverToggleVisual());
        }
    }

    /*
     * @Override public void registerIcons(IIconRegister reg) { for (int i = 0; i < icons.length; i++) { this.icons[i] =
     * mTextures[i].getIcon(); } }
     * @Override public IIcon getIconFromDamage(int meta) { return this.icons[meta]; }
     */

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

    public static final long getCoverDamage(final ItemStack aStack) {
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

    public static final boolean setCoverDamage(final ItemStack aStack, final long aDamage) {
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

    public static final boolean getCoverConnections(final ItemStack aStack) {
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

    public static final boolean setCoverConnections(final ItemStack aStack, final boolean aConnections) {
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
        double durabilitypercent = currentDamage / meta;
        return durabilitypercent;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (KeyboardUtils.isShiftKeyDown()) {
            boolean con = getCoverConnections(stack);
            if (con) {
                setCoverConnections(stack, false);
            } else {
                setCoverConnections(stack, true);
            }
        }
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        boolean cons = getCoverConnections(stack);
        list.add(EnumChatFormatting.GRAY + "Allows Connections: " + cons);
        list.add(EnumChatFormatting.GRAY + "Shift Rmb to change state before applying");
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
