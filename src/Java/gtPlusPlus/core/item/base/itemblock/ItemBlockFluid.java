package gtPlusPlus.core.item.base.itemblock;

import gtPlusPlus.core.fluids.BlockFluidBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockFluid extends ItemBlock{

	protected final int blockColour;
	protected final int sRadiation;
	protected Material thisFluid;
	final BlockFluidBase baseBlock;
	String name;

	public ItemBlockFluid(Block block) {
		super(block);
		this.baseBlock = (BlockFluidBase) block;
		this.blockColour = baseBlock.getRenderColor(1);
		this.thisFluid = baseBlock.getFluidMaterial();
		this.sRadiation=ItemUtils.getRadioactivityLevel(baseBlock.getUnlocalizedName());
		this.name = baseBlock.getLocalizedName().replace("tile", "").replace("fluid", "").replace("name", "").replace("block", "").replace(".", "");
		//GT_OreDictUnificator.registerOre("frameGt"+block.getUnlocalizedName().replace("tile.", "").replace("tile.BlockGtFrame", "").replace("-", "").replace("_", "").replace(" ", "").replace("FrameBox", ""), UtilsItems.getSimpleStack(this));
	}
	
	public final Material setFluidMaterial(Material M){
		return thisFluid=M;
	}

	public int getRenderColor(int aMeta) {
		return blockColour;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack iStack) {
		if (thisFluid != null){
		this.name = "Molten "+thisFluid.getLocalizedName();
		return name;
		}
		this.name = "Molten "+baseBlock.getLocalizedName().replace("tile", "").replace("fluid", "").replace("name", "").replace("block", "").replace(".", "");
		return name;
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (blockColour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return blockColour;

	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		list.add("Temperature: "+MathUtils.celsiusToKelvin(thisFluid.getMeltingPointC())+"K");
		if (sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public String GetProperName() {
		String tempIngot;	

		tempIngot = "Molten "+baseBlock.getLocalizedName();

		return tempIngot;
	}

}
