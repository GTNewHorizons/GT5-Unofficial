package gtPlusPlus.core.item.general;

import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

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
		super(aUnlocalName, aInternalName, AddToCreativeTab.tabMisc, 64, 1000, aBaseTooltip, EnumRarity.common,	EnumChatFormatting.RESET, false, null);
		mLocalNames = new HashMap<Integer, String>();
		mMaxStackSizes = new HashMap<Integer, Integer>();
		mDescriptionArrays = new HashMap<Integer, String[]>();
		mRarities = new HashMap<Integer, EnumRarity>();
		mCustomNameColours = new HashMap<Integer, EnumChatFormatting>();
		mIcons = new HashMap<Integer, IIcon>();
		mTextureDir = aTextureDir;
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
		//sizes[0][5] = mIcons.size();
		mLocalNames.put(id, aLocalName);
		mMaxStackSizes.put(id, aMaxStack);
		mDescriptionArrays.put(id, aDescript);
		mRarities.put(id, aRarity);
		mCustomNameColours.put(id, aCustomNameColour);
		sizes[1][0] = mLocalNames.size();
		sizes[1][1] = mMaxStackSizes.size();
		sizes[1][2] = mDescriptionArrays.size();
		sizes[1][3] = mRarities.size();
		sizes[1][4] = mCustomNameColours.size();
		//sizes[1][5] = mIcons.size();
		boolean b = sizes[0][0] > sizes[1][0] && sizes[0][1] > sizes[1][1] && sizes[0][2] > sizes[1][2]
				&& sizes[0][3] > sizes[1][3] && sizes[0][4] > sizes[1][4];
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
		for (String s : mDescriptionArrays.get(stack.getItemDamage())) {
			list.add(s);
		}
	}

	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {

		String s = "" + mCustomNameColours.get(tItem.getItemDamage());
		String parent = super.getItemStackDisplayName(tItem);
		if (mLocalNames.get(tItem.getItemDamage()).length() > 0 && parent.toLowerCase().contains(".name")) {
			s = s + mLocalNames.get(tItem.getItemDamage());
		} else {
			s = s + parent;
		}
		return s;
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return mRarities.get(par1ItemStack.getItemDamage());
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return false;
	}

	@Override
	public int getMetadata(int p_77647_1_) {
		return 0;
	}

	@Override
	public int getDisplayDamage(ItemStack stack) {
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
	public int getItemStackLimit() {
		return 64;
	}

	@Override
	public int getItemStackLimit(ItemStack aStack) {
		if (aStack == null) {
			return getItemStackLimit();
		}
		else {
			Integer aSize = mMaxStackSizes.get(aStack.getItemDamage());
			if (aSize != null) {
				return aSize;
			}
			else {
				return getItemStackLimit();
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister aIconRegister) {
		for (int i = 0, j = mLocalNames.size(); i < j; i++) {
			mIcons.put(i, aIconRegister.registerIcon(CORE.MODID + ":" + mTextureDir + "/" + i));
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
