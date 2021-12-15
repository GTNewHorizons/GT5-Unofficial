package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.fluids.BlockFluidBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class FluidItemBlock extends ItemBlock{

	protected final int blockColour;
	final BlockFluidBase baseBlock;
	String name;

	public FluidItemBlock(final Block block) {
		super(block);
		this.baseBlock = (BlockFluidBase) block;
		this.blockColour = this.baseBlock.getRenderColor(1);
		this.name = this.baseBlock.getLocalizedName().replace("tile", "").replace("fluid", "").replace("name", "").replace("block", "").replace(".", "");
		//GT_OreDictUnificator.registerOre("frameGt"+block.getUnlocalizedName().replace("tile.", "").replace("tile.BlockGtFrame", "").replace("-", "").replace("_", "").replace(" ", "").replace("FrameBox", ""), UtilsItems.getSimpleStack(this));
	}

	public int getRenderColor(final int aMeta) {
		return this.blockColour;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack iStack) {
		/*if (this.thisFluid != null){
			this.name = "Molten "+this.thisFluid.getLocalizedName();
			return this.name;
		}*/
		this.name = "Molten "+this.baseBlock.getLocalizedName().replace("tile", "").replace("fluid", "").replace("name", "").replace("block", "").replace(".", "");
		return this.name;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.blockColour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.blockColour;
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		/*list.add("Temperature: "+MathUtils.celsiusToKelvin(this.thisFluid.getMeltingPointC())+"K");
		if (this.sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}*/
		super.addInformation(stack, aPlayer, list, bool);
	}

}
