package gtPlusPlus.xmod.rftools.blocks;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import mcjty.rftools.blocks.dimlets.DimensionBuilderBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class Block_RFT_AdvancedWorldBuilder extends DimensionBuilderBlock{

	public Block_RFT_AdvancedWorldBuilder(boolean creative, String blockName) {
		super(creative, blockName);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThis) {
		//super.addInformation(itemStack, player, list, whatIsThis);

		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54))) {
			list.add(EnumChatFormatting.WHITE + "This builds a dimension and powers it when");
			list.add(EnumChatFormatting.WHITE + "the dimension is ready.");
			list.add(EnumChatFormatting.WHITE + "Use a dialing device to connect this to");
			list.add(EnumChatFormatting.WHITE + "your new dimension and then teleport.");
			list.add(EnumChatFormatting.YELLOW + "Infusing bonus: reduced power consumption and");
			list.add(EnumChatFormatting.YELLOW + "faster dimension creation speed.");
		} else {
			list.add(EnumChatFormatting.WHITE + "<Press Shift>");
		}
	}

	/*@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return null;
	}*/

}
