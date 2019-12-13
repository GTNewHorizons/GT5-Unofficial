package gtPlusPlus.xmod.ic2.item;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import ic2.api.item.IKineticRotor;
import ic2.core.IC2;
import ic2.core.block.kineticgenerator.gui.GuiWaterKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class CustomKineticRotor extends Item implements IKineticRotor {

	private final int mTier;
	
	@SideOnly(Side.CLIENT)
	private final IIcon[] mTextures;
	
	private static final String[] mRegistrationNames = new String[] {
			"itemwoodrotor", "itemironrotor", "itemsteelrotor", "itemwcarbonrotor"
	};
	
	private static final String[] mUnlocalNames = new String[] {
			"itemEnergeticRotor",
			"itemTungstenSteelRotor",
			"itemVibrantRotor",
			"itemIridiumRotor",
			"itemMagnaliumRotor",
			"itemUltimetRotor",			
	};
	private static final int[] mMaxDurability = new int[] {
			512000, 809600, 1600000, 3200000
	};
	private static final int[] mRadius = new int[] {
			9, 11, 13, 15
	};
	private static final float[] mEfficiency = new float[] {
			0.9f, 1.0f, 1.2f, 1.5f
	};
	private static final int[] mMinWindStrength = new int[] {
			12, 14, 16, 18
	};
	private static final int[] mMaxWindStrength = new int[] {
			80, 120, 160, 320
	};
	
	private static final ResourceLocation[] mResourceLocations = new ResourceLocation[] {
			new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorEnergeticModel.png"),
			new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorTungstenSteelModel.png"),
			new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorVibrantModel.png"),
			new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorIridiumModel.png"),
			new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorMagnaliumModel.png"),
			new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorUltimetModel.png"),
	};	

	private final int maxWindStrength;
	private final int minWindStrength;
	private final int radius;
	private final float efficiency;
	private final ResourceLocation renderTexture;
	
	public CustomKineticRotor(int aTier) {
		mTier = aTier;
		if (Utils.isClient()) {
			mTextures = new IIcon[6];
		}
		else {
			mTextures = null;
		}
		this.setMaxStackSize(1);
		// Handle Differences if EIO is not loaded
		if (!LoadedMods.EnderIO && (aTier == 0 || aTier == 2)) {
			this.renderTexture = mResourceLocations[(aTier == 0 ? 4 : 5)];	
			this.setUnlocalizedName(mUnlocalNames[(aTier == 0 ? 4 : 5)]);		
		}
		else {
			this.renderTexture = mResourceLocations[aTier];
			this.setUnlocalizedName(mUnlocalNames[aTier]);
		}	
		this.setMaxDamage(mMaxDurability[aTier]);
		this.radius = mRadius[aTier];
		this.efficiency = mEfficiency[aTier];
		this.minWindStrength = mMinWindStrength[aTier];
		this.maxWindStrength = mMaxWindStrength[aTier];
		this.setNoRepair();
		this.setCreativeTab(IC2.tabIC2);
		GameRegistry.registerItem(this, mRegistrationNames[aTier]);	
	}
	
	@Override
	public void setDamage(final ItemStack stack, final int damage) {
		if (mTier < 3) {
			super.setDamage(stack, damage);
		}
	}

	@Override
	public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List info, final boolean b) {

		info.add(StatCollector.translateToLocalFormatted("ic2.itemrotor.wind.info",	new Object[]{this.minWindStrength, this.maxWindStrength}));
		
		GearboxType type = null;
		if (Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiWaterKineticGenerator) {
			type = GearboxType.WATER;
		} 
		else if (Minecraft.getMinecraft().currentScreen != null	&& Minecraft.getMinecraft().currentScreen instanceof GuiWindKineticGenerator) {
			type = GearboxType.WIND;
		}
		
		if (type != null) {
			info.add(StatCollector.translateToLocal("ic2.itemrotor.fitsin." + this.isAcceptedType(itemStack, type)));
		}
		
	}

	@Override
	public int getDiameter(final ItemStack stack)
	{
		return this.radius;
	}

	@Override
	public ResourceLocation getRotorRenderTexture(final ItemStack stack)
	{
		return this.renderTexture;
	}

	@Override
	public float getEfficiency(final ItemStack stack)
	{
		return this.efficiency;
	}

	@Override
	public int getMinWindStrength(final ItemStack stack)
	{
		return this.minWindStrength;
	}

	@Override
	public int getMaxWindStrength(final ItemStack stack)
	{
		return this.maxWindStrength;
	}

	@Override
	public boolean isAcceptedType(final ItemStack stack, final IKineticRotor.GearboxType type){
		return (type == IKineticRotor.GearboxType.WIND) || (type == IKineticRotor.GearboxType.WATER);
	}

	public String getUnlocalizedName() {
		return "ic2." + super.getUnlocalizedName().substring(5);
	}

	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName();
	}

	public String getItemStackDisplayName(ItemStack itemStack) {
		return StatCollector.translateToLocal(this.getUnlocalizedName(itemStack));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return mTier < 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {		
		if (!LoadedMods.EnderIO && (mTier == 0 || mTier == 2)) {
			if (mTier == 0) {
				return mTextures[4];				
			}
			else {
				return mTextures[5];				
			}
		}
		else {
			return mTextures[mTier];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack aIndex) {
		if (!LoadedMods.EnderIO && (mTier == 0 || mTier == 2)) {
			if (mTier == 0) {
				return mTextures[4];				
			}
			else {
				return mTextures[5];				
			}
		}
		else {
			return mTextures[mTier];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int aDmg, int aPass) {
		if (!LoadedMods.EnderIO && (mTier == 0 || mTier == 2)) {
			if (mTier == 0) {
				return mTextures[4];				
			}
			else {
				return mTextures[5];				
			}
		}
		else {
			return mTextures[mTier];
		}
	}

	@Override
	protected String getIconString() {
		return super.getIconString();
	}

	@Override
	public int getDisplayDamage(ItemStack stack) {
		return super.getDisplayDamage(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return super.getDurabilityForDisplay(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		if (!LoadedMods.EnderIO && (mTier == 0 || mTier == 2)) {
			if (mTier == 0) {
				return mTextures[4];				
			}
			else {
				return mTextures[5];				
			}
		}
		else {
			return mTextures[mTier];
		}		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		if (!LoadedMods.EnderIO && (mTier == 0 || mTier == 2)) {
			if (mTier == 0) {
				return mTextures[4];				
			}
			else {
				return mTextures[5];				
			}
		}
		else {
			return mTextures[mTier];
		}
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {		
		int aIndex = 0;
		for (String y : mUnlocalNames) {
			mTextures[aIndex++] = iconRegister.registerIcon(IC2.textureDomain + ":" + "rotors/" + y);		
		}		
	}
}