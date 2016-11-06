package gtPlusPlus.core.util.debug;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class DEBUG_BLOCK_ShapeSpawner extends DEBUG_MULTIBLOCK_ShapeSpawner {

	private static boolean controller;

	public DEBUG_BLOCK_ShapeSpawner(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public DEBUG_BLOCK_ShapeSpawner(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new DEBUG_BLOCK_ShapeSpawner(this.mName);
	}

	
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Testing",
				"Create the shapes for Multiblocks.",};
	}


	@Override
	public ITexture[] getTexture(IGregTechTileEntity arg0, byte arg1,
			byte arg2, byte arg3, boolean arg4, boolean arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		
		int stepX = aBaseMetaTileEntity.getXCoord();
		int stepY = aBaseMetaTileEntity.getYCoord();
		int stepZ = aBaseMetaTileEntity.getZCoord();
		int temp = 0;

		Utils.LOG_INFO("Starting Block located @ "+"[X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	

		int tAmount = 0;
		switch (xDir) {
		case -1:
			stepX++;
			Utils.LOG_INFO("Modifying stepX + accomodate a "+xDir+" xDir - [X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	
			break;

		case 1:
			stepX--;
			Utils.LOG_INFO("Modifying stepX - accomodate a "+xDir+" xDir - [X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	
			break;
		}
		switch (zDir) {
		case -1:
			stepZ++;
			Utils.LOG_INFO("Modifying stepZ + accomodate a "+zDir+" zDir - [X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	
			break;

		case 1:
			stepZ--;
			Utils.LOG_INFO("Modifying stepZ - accomodate a "+zDir+" zDir - [X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	
			break;
		}

		for (int i = stepX-1; i <= stepX+1; i++){
			for (int j = stepZ-1; j <= stepZ+1; j++){
				for (int h = stepY-1; h <= stepY+1; h++){	


					Utils.LOG_INFO("Block Facing - X:"+xDir+"    Z:"+zDir);
					Utils.LOG_INFO("(h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))");
					Utils.LOG_INFO("  "+(h != 0)+"   ||       "+(((xDir + i != 0)+"       ||       "+(zDir + j != 0))+"       &&    "+((i != 0)+"   ||   "+(j != 0))));
				}
			}
		}
		return false;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		// TODO Auto-generated method stub
		return false;
	}

}
