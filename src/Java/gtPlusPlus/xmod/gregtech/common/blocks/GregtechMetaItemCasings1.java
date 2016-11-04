package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GregtechMetaItemCasings1 extends GregtechMetaItemCasingsAbstract {
	public GregtechMetaItemCasings1(final Block par1) {
		super(par1);
	}

	@Override
	public void addInformation(final ItemStack aStack, final EntityPlayer aPlayer, final List aList,
			final boolean aF3_H) {
		super.addInformation(aStack, aPlayer, aList, aF3_H);
		switch (this.getDamage(aStack)) {
			case 0:
				aList.add(this.mCasing_Centrifuge);
				break;
			case 1:
				aList.add(this.mCasing_CokeOven);
				break;
			case 2:
				aList.add(this.mCasing_CokeCoil1);
				break;
			case 3:
				aList.add(this.mCasing_CokeCoil2);
				break;
			default:
				aList.add(this.mCasing_CokeCoil2);
				break;
		}
	}
}
