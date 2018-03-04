package gtPlusPlus.core.item.base;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class BaseItemTickable extends CoreItem {

	public final String descriptionString;
	public final String descriptionString2;
	public final int itemColour;
	public final int maxTicks;
	public final boolean twoRenderPasses;

	public IIcon[] mIcon = new IIcon[2];

	public BaseItemTickable(boolean twoPass, final String unlocalName, final int colour, final int maxTicks) {
		this(twoPass, unlocalName, colour, maxTicks, "");
	}

	public BaseItemTickable(boolean twoPass, final String unlocalName, final int colour, final int maxTicks, final String Description) {
		this(twoPass, unlocalName, colour, maxTicks, "", Description);
	}

	public BaseItemTickable(boolean twoPass, final String unlocalName, final int colour, final int maxTicks, final String Description, final String Description2) {
		super(unlocalName, AddToCreativeTab.tabMisc, 1, 999999999, Description, EnumRarity.epic, EnumChatFormatting.DARK_RED, true, null);
		this.itemColour = colour;
		this.descriptionString = Description;
		this.descriptionString2 = Description2;
		this.maxTicks = maxTicks;
		this.twoRenderPasses = twoPass;
		//setGregtechItemList();
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		if (world == null || iStack == null) {
			return;
		}	
		tickItemTag(iStack);
	}

	/*private final boolean setGregtechItemList() {
		ItemList.Component_LavaFilter.set(this);
		return ItemList.Component_LavaFilter.get(1) != null ? true : false;
	}*/

	/**
	 * 
	 * Handle Custom Rendering
	 *
	 */

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return this.twoRenderPasses;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
		if (renderPass == 1 && this.twoRenderPasses){
			return Utils.rgbtoHexValue(255, 255, 255);
		}
		return this.itemColour;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		if (this.twoRenderPasses) {
			if(pass == 0) {
				return this.mIcon[0];
			}
			return this.mIcon[1];			
		}
		return this.mIcon[0];
	}

	@Override
	public void registerIcons(final IIconRegister i) {

		if (this.twoRenderPasses){
			this.mIcon[0] = i.registerIcon(CORE.MODID + ":" + this.getUnlocalizedName());
			this.mIcon[1] = i.registerIcon(CORE.MODID + ":" + this.getUnlocalizedName() + "_OVERLAY");
		}
		else {
			this.mIcon[0] = i.registerIcon(CORE.MODID + ":" + this.getUnlocalizedName());
			//this.overlay = i.registerIcon(getCorrectTextures() + "_OVERLAY");
		}
	}


	private boolean createNBT(ItemStack rStack){
		final NBTTagCompound tagMain = new NBTTagCompound();
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setLong("Tick", 0);
		tagNBT.setLong("maxTick", this.maxTicks);
		tagNBT.setBoolean("isActive", true);
		tagMain.setTag("TickableItem", tagNBT);		
		rStack.setTagCompound(tagMain);		
		return true;
	}

	public final long getFilterDamage(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				return aNBT.getLong("Tick");
			}
		}
		else {
			createNBT(aStack);
		}
		return 0L;
	}

	public final boolean setFilterDamage(final ItemStack aStack, final long aDamage) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				aNBT.setLong("Tick", aDamage);
				return true;
			}
		}
		else {
			createNBT(aStack);
		}
		return false;
	}
	
	public final boolean getIsActive(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				return aNBT.getBoolean("isActive");
			}
		}
		else {
			createNBT(aStack);
		}
		return true;
	}

	public final boolean setIsActive(final ItemStack aStack, final boolean active) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				aNBT.setBoolean("isActive", active);
				return true;
			}
		}
		else {
			createNBT(aStack);
		}
		return false;
	}

	public final boolean tickItemTag(ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			//Done Ticking
			if (maxTicks-getFilterDamage(aStack) <= 0) {
				setIsActive(aStack, false);
			}			
			if (getIsActive(aStack)) {
				aNBT = aNBT.getCompoundTag("TickableItem");
				if (aNBT != null) {
					aNBT.setLong("Tick", getFilterDamage(aStack)+1);
					return true;
				}
			}			
		}
		else {
			createNBT(aStack);
		}
		return false;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.getTagCompound() == null){
			createNBT(stack);
		}
		double currentDamage = getFilterDamage(stack);
		double durabilitypercent = currentDamage / maxTicks;		
		return  durabilitypercent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		EnumChatFormatting durability = EnumChatFormatting.GRAY;
		if (maxTicks-getFilterDamage(stack) > (maxTicks*0.8)){
			durability = EnumChatFormatting.GRAY;
		}
		else if (maxTicks-getFilterDamage(stack) > (maxTicks*0.6)){
			durability = EnumChatFormatting.GREEN;
		}
		else if (maxTicks-getFilterDamage(stack) > (maxTicks*0.4)){
			durability = EnumChatFormatting.YELLOW;
		}
		else if (maxTicks-getFilterDamage(stack) > (maxTicks*0.2)){
			durability = EnumChatFormatting.GOLD;
		}
		else if (maxTicks-getFilterDamage(stack) > 0){
			durability = EnumChatFormatting.RED;
		}
		list.add(durability+""+((maxTicks-getFilterDamage(stack))/20)+EnumChatFormatting.GRAY+" seconds until decay");

		if ((this.descriptionString != "") || !this.descriptionString.equals("")){
			list.add(EnumChatFormatting.GRAY+this.descriptionString);
		}
		if ((this.descriptionString2 != "") || !this.descriptionString2.equals("")){
			list.add(EnumChatFormatting.GRAY+this.descriptionString2);
		}

		//super.addInformation(stack, player, list, bool);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}


}


