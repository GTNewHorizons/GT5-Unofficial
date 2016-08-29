package miscutil.core.item.base;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.MiscUtils;
import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.handler.GuiHandler;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.math.MathUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseItemBackpack extends Item{
	
	protected final int colourValue;
	
	
	public BaseItemBackpack(String unlocalizedName, int colour){
	
	this.setUnlocalizedName(unlocalizedName);
	this.setTextureName(CORE.MODID + ":" + "itemBackpack");
	this.colourValue = colour;
	GameRegistry.registerItem(this, unlocalizedName);
	GT_OreDictUnificator.registerOre(unlocalizedName.replace("itemB", "b"), UtilsItems.getSimpleStack(this));
	setMaxStackSize(1);
	setCreativeTab(AddToCreativeTab.tabOther);
	}

	// Without this method, your inventory will NOT work!!!
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1; // return any value greater than zero
	}
   
    	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			Utils.LOG_INFO("Tried to use a Backpack.");
			// If player not sneaking, open the inventory gui
			if (!player.isSneaking()) {
				Utils.LOG_INFO("Player is not sneaking.");
				player.openGui(MiscUtils.instance, GuiHandler.GUI3, world, 0, 0, 0);
			}
			
			// Otherwise, stealthily place some diamonds in there for a nice surprise next time you open it up :)
			else {
			//	Utils.LOG_INFO("Player is Sneaking, giving them sneaky diamonds.");
			//	new BaseInventoryBackpack(player.getHeldItem()).setInventorySlotContents(0, new ItemStack(Items.diamond,4));
			}
		}
		
		return itemstack;
	}
    	
    	@Override
    	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
    		if (colourValue == 0){
    			return MathUtils.generateSingularRandomHexValue();
    		}
    		return colourValue;

    	}
    	
    @Override
    	public String getItemStackDisplayName(ItemStack p_77653_1_) {

    		return ("Backpack");
    	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(CORE.MODID + ":" + "itemBackpack");
	}
}
