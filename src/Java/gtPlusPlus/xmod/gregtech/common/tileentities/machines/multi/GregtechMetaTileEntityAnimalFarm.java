package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;

public abstract class GregtechMetaTileEntityAnimalFarm extends GT_MetaTileEntity_MultiBlockBase {

	public GregtechMetaTileEntityAnimalFarm(String aName) {
		super(aName);
		// TODO Auto-generated constructor stub
	}
	
	public GregtechMetaTileEntityAnimalFarm(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity p0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity p0, byte p1, byte p2, byte p3, boolean p4, boolean p5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack p0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkRecipe(ItemStack p0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity p0, ItemStack p1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMaxEfficiency(ItemStack p0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPollutionPerTick(ItemStack p0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack p0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack p0) {
		// TODO Auto-generated method stub
		return false;
	}


}