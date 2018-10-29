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

public class BaseItemTickable extends CoreItem {

	public final String[] descriptionString;
	public final int itemColour;
	public final int maxTicks;
	public final boolean twoRenderPasses;
	public final boolean ticksInContainers;

	public IIcon[] mIcon = new IIcon[2];

	public BaseItemTickable(boolean twoPass, final String unlocalName, final int colour, final int maxTicks) {
		this(false, twoPass, unlocalName, colour, maxTicks, new String[] {});
	}
	
	public BaseItemTickable(boolean containerTick, boolean twoPass, final String unlocalName, final int colour, final int maxTicks) {
		this(containerTick, twoPass, unlocalName, colour, maxTicks, new String[] {});
	}

	public BaseItemTickable(boolean containerTick, boolean twoPass, final String unlocalName, final int colour, final int maxTicks, final String[] Description) {
		super(unlocalName, AddToCreativeTab.tabMisc, 1, 999999999, Description, EnumRarity.epic, EnumChatFormatting.DARK_RED, true, null);
		this.itemColour = colour;
		this.descriptionString = Description;
		this.maxTicks = maxTicks;
		this.twoRenderPasses = twoPass;
		this.ticksInContainers = containerTick;
		//setGregtechItemList();
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		if (world == null || iStack == null) {
			return;
		}	
		

		boolean active = getIsActive(world, iStack);
		if (active) {
			tickItemTag(world, iStack);
		}
		
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


	private boolean createNBT(World world, ItemStack rStack){
		final NBTTagCompound tagMain = new NBTTagCompound();
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setLong("Tick", 0);
		tagNBT.setLong("maxTick", this.maxTicks);
		tagNBT.setBoolean("isActive", true);
		
		//Try set world time
		if (world != null) {
			tagNBT.setLong("CreationDate", world.getTotalWorldTime());
		}
		
		tagMain.setTag("TickableItem", tagNBT);		
		rStack.setTagCompound(tagMain);		
		return true;
	}

	public final long getFilterDamage(World world, final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				return aNBT.getLong("Tick");
			}
		}
		else {
			createNBT(world, aStack);
		}
		return 0L;
	}

	public final boolean setFilterDamage(World world, final ItemStack aStack, final long aDamage) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				aNBT.setLong("Tick", aDamage);
				return true;
			}
		}
		else {
			createNBT(world, aStack);
		}
		return false;
	}
	
	public final boolean getIsActive(World world, final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				return aNBT.getBoolean("isActive");
			}
		}
		else {
			return createNBT(world, aStack);
		}
		return true;
	}

	public final boolean setIsActive(World world, final ItemStack aStack, final boolean active) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				aNBT.setBoolean("isActive", active);
				return true;
			}
		}
		else {
			createNBT(world, aStack);
		}
		return false;
	}
	
	public final boolean getTicksInContainer(World world, final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				return aNBT.getBoolean("ticksInContainer");
			}
		}
		else {
			createNBT(world, aStack);
		}
		return false;
	}

	public final boolean setTicksInContainer(World world, final ItemStack aStack, final boolean active) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				aNBT.setBoolean("ticksInContainer", active);
				return true;
			}
		}
		else {
			createNBT(world, aStack);
		}
		return false;
	}
	
	public final long getDifferenceInWorldTimeToCreationTime(World world, final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				return (world.getTotalWorldTime() - aNBT.getLong("CreationDate"));
			}
		}
		else {
			createNBT(world, aStack);
		}
		return 0L;
	}

	public final boolean setItemStackCreationTime(final ItemStack aStack, World world) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("TickableItem");
			if (aNBT != null) {
				aNBT.setLong("CreationDate", world.getTotalWorldTime());
				return true;
			}
		}
		else {
			createNBT(world, aStack);
		}
		return false;
	}

	public final boolean tickItemTag(World world, ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			if (aNBT.hasKey("TickableItem")) {
				aNBT = aNBT.getCompoundTag("TickableItem");
				
				if (!aNBT.hasKey("CreationDate") && world != null) {
					aNBT.setLong("CreationDate", world.getTotalWorldTime());
				}
				
				//Done Ticking
				if (maxTicks-getFilterDamage(world, aStack) <= 0) {
					setIsActive(world, aStack, false);
				}			
				if (getIsActive(world, aStack)) {
					if (aNBT != null) {
						
						//if ((world.getTotalWorldTime()-))
						
						// Just tick once
						aNBT.setLong("Tick", getFilterDamage(world, aStack)+1);
						
						
						return true;
					}
				}				
			}					
		}
		return createNBT(world, aStack);		
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.getTagCompound() == null){
			createNBT(null, stack);
		}
		double currentDamage = getFilterDamage(null, stack);
		double durabilitypercent = currentDamage / maxTicks;		
		return  durabilitypercent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		World world = player.getEntityWorld();
		if (this.descriptionString.length > 0) {
			list.add(EnumChatFormatting.GRAY+this.descriptionString[0]);
		}
		EnumChatFormatting durability = EnumChatFormatting.GRAY;
		if (maxTicks-getFilterDamage(world, stack) > (maxTicks*0.8)){
			durability = EnumChatFormatting.GRAY;
		}
		else if (maxTicks-getFilterDamage(world, stack) > (maxTicks*0.6)){
			durability = EnumChatFormatting.GREEN;
		}
		else if (maxTicks-getFilterDamage(world, stack) > (maxTicks*0.4)){
			durability = EnumChatFormatting.YELLOW;
		}
		else if (maxTicks-getFilterDamage(world, stack) > (maxTicks*0.2)){
			durability = EnumChatFormatting.GOLD;
		}
		else if (maxTicks-getFilterDamage(world, stack) > 0){
			durability = EnumChatFormatting.RED;
		}
		list.add(durability+""+((maxTicks-getFilterDamage(world, stack))/20)+EnumChatFormatting.GRAY+" seconds until decay");

		if (this.descriptionString.length > 1) {
			for (int h=1;h<this.descriptionString.length;h++) {
				list.add(EnumChatFormatting.GRAY+this.descriptionString[h]);
			}
		}

		//super.addInformation(stack, player, list, bool);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}


}


