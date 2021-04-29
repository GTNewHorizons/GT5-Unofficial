package common.blocks;

import java.util.List;

import common.itemBlocks.IB_LapotronicEnergyUnit;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class Block_LapotronicEnergyUnit extends BaseGTUpdateableBlock {
	
	private static final Block_LapotronicEnergyUnit INSTANCE = new Block_LapotronicEnergyUnit();
	
	private IIcon iconBaseSide;
	private IIcon iconBaseTop;
	
	private IIcon iconLapoEmptySide;
	private IIcon iconLapoEmptyTop;
	private IIcon iconLapoEVSide;
	private IIcon iconLapoEVTop;
	private IIcon iconLapoIVSide;
	private IIcon iconLapoIVTop;
	private IIcon iconLapoLuVSide;
	private IIcon iconLapoLuVTop;
	private IIcon iconLapoZPMSide;
	private IIcon iconLapoZPMTop;
	private IIcon iconLapoUVSide;
	private IIcon iconLapoUVTop;
	private IIcon iconUltimateSide;
	private IIcon iconUltimateTop;
	
	private Block_LapotronicEnergyUnit() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_lapotronicenergyunit_block";
		INSTANCE.setBlockName(blockName);
		INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
		INSTANCE.setHardness(5.0f);
		INSTANCE.setResistance(6.0f);
		GameRegistry.registerBlock(INSTANCE, IB_LapotronicEnergyUnit.class, blockName);

		return INSTANCE;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister ir) {
		iconBaseSide = ir.registerIcon("kekztech:LSCBase_side");
		iconBaseTop = ir.registerIcon("kekztech:LSCBase_top");
		
		
		iconLapoEmptySide = ir.registerIcon("kekztech:LapotronicEnergyUnit6_side");
		iconLapoEmptyTop = ir.registerIcon("kekztech:LapotronicEnergyUnit6_top");
		iconLapoEVSide = ir.registerIcon("kekztech:LapotronicEnergyUnit7_side");
		iconLapoEVTop = ir.registerIcon("kekztech:LapotronicEnergyUnit7_top");
		iconLapoIVSide = ir.registerIcon("kekztech:LapotronicEnergyUnit1_side");
		iconLapoIVTop = ir.registerIcon("kekztech:LapotronicEnergyUnit1_top");
		iconLapoLuVSide = ir.registerIcon("kekztech:LapotronicEnergyUnit2_side");
		iconLapoLuVTop = ir.registerIcon("kekztech:LapotronicEnergyUnit2_top");
		iconLapoZPMSide = ir.registerIcon("kekztech:LapotronicEnergyUnit3_side");
		iconLapoZPMTop = ir.registerIcon("kekztech:LapotronicEnergyUnit3_top");
		iconLapoUVSide = ir.registerIcon("kekztech:LapotronicEnergyUnit4_side");
		iconLapoUVTop = ir.registerIcon("kekztech:LapotronicEnergyUnit4_top");
		
		iconUltimateSide = ir.registerIcon("kekztech:UltimateEnergyUnit_side");
		iconUltimateTop = ir.registerIcon("kekztech:UltimateEnergyUnit_top");
	}
	
	@Override
	@SuppressWarnings({"unchecked" })
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		// Multi casing
		par3List.add(new ItemStack(par1, 1, 0));
		// Empty capacitor
		par3List.add(new ItemStack(par1, 1, 6));
		// Lapo capacitors EV - UV
		par3List.add(new ItemStack(par1, 1, 7));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
		par3List.add(new ItemStack(par1, 1, 4));
		// Ultimate battery
		par3List.add(new ItemStack(par1, 1, 5));
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		switch(meta) {
		case 0: return (side < 2) ? iconBaseTop : iconBaseSide;
		case 1: return (side < 2) ? iconLapoIVTop : iconLapoIVSide;
		case 2: return (side < 2) ? iconLapoLuVTop : iconLapoLuVSide;
		case 3: return (side < 2) ? iconLapoZPMTop : iconLapoZPMSide;
		case 4: return (side < 2) ? iconLapoUVTop : iconLapoUVSide;
		case 5: return (side < 2) ? iconUltimateTop : iconUltimateSide;
		case 6: return (side < 2) ? iconLapoEmptyTop : iconLapoEmptySide;
		case 7: return (side < 2) ? iconLapoEVTop : iconLapoEVSide;
		default: return iconUltimateTop;
		}
	}
	
}
