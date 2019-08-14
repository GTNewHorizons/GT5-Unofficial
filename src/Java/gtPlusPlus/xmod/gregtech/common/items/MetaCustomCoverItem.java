package gtPlusPlus.xmod.gregtech.common.items;

import java.util.List;

import cofh.core.render.IconRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_MultiTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.client.renderer.CustomItemBlockRenderer;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.covers.GTPP_Cover_ToggleVisual;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class MetaCustomCoverItem extends Item {

	private final IIcon[] icons;
	private final String mModID;
	private final String mTextureSetName;
	private final CustomIcon[] mTextures;

	public MetaCustomCoverItem(String aModId, int aTextureCount, String aTextureSetName, CustomIcon[] aTextures) {
		super();
		icons = new IIcon[aTextureCount];
		mModID = aModId;
		mTextureSetName = aTextureSetName;
		mTextures = aTextures;
		this.setHasSubtypes(true);
		String unlocalizedName = "itemCustomMetaCover." + mModID + "." + mTextureSetName + "." + aTextureCount;
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, unlocalizedName);
		registerCover();
		Logger.INFO("[Covers] Generated Custom covers for "+mModID+" using "+aTextureCount+" textures from "+mTextureSetName+".");
	}

	private final void registerCover() {		
		//CommonProxy.registerItemRendererGlobal(this, new CustomItemBlockRenderer());		
		for (int i=0;i<icons.length;i++) {
			GregTech_API.registerCover(
					ItemUtils.simpleMetaStack(this, i, 1),
					new GT_MultiTexture(
							new ITexture[]{
									new GT_RenderedTexture(mTextures[i])
							}),
					new GTPP_Cover_ToggleVisual());
		}
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < icons.length; i++) {
			this.icons[i] = mTextures[i].getIcon();
		}
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.icons[meta];
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
		return EnumChatFormatting.LIGHT_PURPLE + super.getItemStackDisplayName(tItem);
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
			}
			else {
				setCoverConnections(stack, true);				
			}
		}
		return stack;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		boolean cons = getCoverConnections(stack);
		list.add(EnumChatFormatting.GRAY + "Allows Connections: "+cons);
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

}