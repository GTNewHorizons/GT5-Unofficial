package gtPlusPlus.core.item.base;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.GuiHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseItemBackpack extends Item{

	protected final int colourValue;
	protected final String unlocalName;


	public BaseItemBackpack(final String unlocalizedName, final int colour){
		this.unlocalName = unlocalizedName;
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + "itemBackpack");
		this.colourValue = colour;
		GameRegistry.registerItem(this, unlocalizedName);
		GT_OreDictUnificator.registerOre("storageBackpack", ItemUtils.getSimpleStack(this));
		this.setMaxStackSize(1);
		this.setCreativeTab(AddToCreativeTab.tabOther);
	}

	// Without this method, your inventory will NOT work!!!
	@Override
	public int getMaxItemUseDuration(final ItemStack stack) {
		return 1; // return any value greater than zero
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer player)
	{
		if (!world.isRemote)
		{
			// If player not sneaking, open the inventory gui
			if (!player.isSneaking()) {
				player.openGui(GTplusplus.instance, GuiHandler.GUI3, world, 0, 0, 0);
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
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colourValue == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colourValue;

	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		//Name Formatting.
		String temp = this.unlocalName.replace("backpack", "");
		//Lets find the colour.
		if (temp.toLowerCase().contains("dark")){
			temp = this.unlocalName.substring(12, this.unlocalName.length());
			temp = "Dark "+ temp;
		}
		return (temp+" Backpack");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(CORE.MODID + ":" + "itemBackpack");
	}
}
