package common.itemBlocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class IB_LapotronicEnergyUnit extends ItemBlock {
	
	public IB_LapotronicEnergyUnit(Block block) {
		super(block);
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getItemDamage();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
		lines.add("Part of the Lapotronic Super Capacitor");
		switch(stack.getItemDamage()) {
		case 1: lines.add("Capacity: 100,000,000 EU"); lines.add("Voltage: 8192"); break;
		case 2: lines.add("Capacity: 1,000,000,000 EU"); lines.add("Voltage: 32,768"); break;
		case 3: lines.add("Capacity: 10,00,000,000 EU"); lines.add("Voltage: 131,072"); break;
		case 4: lines.add("Capacity: 100,000,000,000 EU"); lines.add("Voltage: 524,288"); break;
		case 5: lines.add("Capacity: 9,223,372,036,854,775,807 EU"); lines.add("Voltage: 524,288"); break;
		case 6: lines.add("Capacity: 9,223,372,036,854,775,807 EU"); lines.add("Voltage: 134,217,728"); break;
		}
	}
}
