package common.itemBlocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class IB_LapotronicEnergyUnit extends ItemBlock {
	
	public IB_LapotronicEnergyUnit(Block block) {
		super(block);
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public boolean getHasSubtypes() {
		return true;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getItemDamage();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
		lines.add(StatCollector.translateToLocal("tile.kekztech_lapotronicenergyunit_block.desc"));
		switch(stack.getItemDamage()) {
		case 1: lines.add("Capacity: 100,000,000 EU"); break;
		case 2: lines.add("Capacity: 1,000,000,000 EU"); break;
		case 3: lines.add("Capacity: 10,000,000,000 EU"); break;
		case 4: lines.add("Capacity: 100,000,000,000 EU"); break;
		case 5: lines.add("Capacity: 9,223,372,036,854,775,807 EU"); break;
		case 6: lines.add("Capacity: None"); break;
		case 7: lines.add("Capacity: 10,000,000 EU"); break;
		}
	}
}
