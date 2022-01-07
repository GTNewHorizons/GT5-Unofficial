package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_LanguageManager;

public abstract class GregtechMetaItemCasingsAbstract
extends ItemBlock {

	protected final String mCasing_Centrifuge = GT_LanguageManager.addStringLocalization("mu.centrifugecasing", "Warning! Standing in the Centrifuge not recommended");
	protected final String mCasing_CokeOven = GT_LanguageManager.addStringLocalization("mu.cokeoven", "Sturdy and Strong");
	protected final String mCasing_CokeCoil1 = GT_LanguageManager.addStringLocalization("mu.coil01tooltip", "Base Heating Capacity = 1350 Kelvin");
	protected final String mCasing_CokeCoil2 = GT_LanguageManager.addStringLocalization("mu.coil02tooltip", "Base Heating Capacity = 2275 Kelvin");
	protected final String mNoMobsToolTip = GT_LanguageManager.addStringLocalization("gt.nomobspawnsonthisblock", "Mobs cannot Spawn on this Block");
	protected final String mNoTileEntityToolTip = GT_LanguageManager.addStringLocalization("gt.notileentityinthisblock", "This is NOT a TileEntity!");

	public GregtechMetaItemCasingsAbstract(final Block par1) {
		super(par1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		//setCreativeTab(AddToCreativeTab.tabMachines);
	}

	@Override
	public int getMetadata(final int aMeta) {
		return aMeta;
	}

	@Override
	public String getUnlocalizedName(final ItemStack aStack) {
		return this.field_150939_a.getUnlocalizedName() + "." + this.getDamage(aStack);
	}

	@Override
	public void addInformation(final ItemStack aStack, final EntityPlayer aPlayer, final List aList, final boolean aF3_H) {
		super.addInformation(aStack, aPlayer, aList, aF3_H);
		switch (this.getDamage(aStack)) {
		case 0:
			//aList.add(this.mCasing_Centrifuge);
			break;
		case 1:
			//aList.add(this.mCasing_CokeOven);
			break;
		case 2:
			//aList.add(this.mCasing_CokeCoil1);
			break;
		case 3:
			//aList.add(this.mCasing_CokeCoil2);
			break;
		default:
			break;
		}
		aList.add(this.mNoMobsToolTip);
		aList.add(this.mNoTileEntityToolTip);
	}
}
