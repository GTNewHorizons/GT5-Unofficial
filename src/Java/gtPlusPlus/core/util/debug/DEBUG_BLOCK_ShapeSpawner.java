package gtPlusPlus.core.util.debug;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class DEBUG_BLOCK_ShapeSpawner extends DEBUG_MULTIBLOCK_ShapeSpawner {

	private static boolean controller;

	public DEBUG_BLOCK_ShapeSpawner(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public DEBUG_BLOCK_ShapeSpawner(final String aName) {
		super(aName);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {

		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}

		int stepX = aBaseMetaTileEntity.getXCoord();
		final int stepY = aBaseMetaTileEntity.getYCoord();
		int stepZ = aBaseMetaTileEntity.getZCoord();
		final int temp = 0;

		Utils.LOG_INFO("Starting Block located @ " + "[X:" + stepX + "][Y:" + stepY + "][Z:" + stepZ + "]");

		final int tAmount = 0;
		switch (xDir) {
			case -1:
				stepX++;
				Utils.LOG_INFO("Modifying stepX + accomodate a " + xDir + " xDir - [X:" + stepX + "][Y:" + stepY
						+ "][Z:" + stepZ + "]");
				break;

			case 1:
				stepX--;
				Utils.LOG_INFO("Modifying stepX - accomodate a " + xDir + " xDir - [X:" + stepX + "][Y:" + stepY
						+ "][Z:" + stepZ + "]");
				break;
		}
		switch (zDir) {
			case -1:
				stepZ++;
				Utils.LOG_INFO("Modifying stepZ + accomodate a " + zDir + " zDir - [X:" + stepX + "][Y:" + stepY
						+ "][Z:" + stepZ + "]");
				break;

			case 1:
				stepZ--;
				Utils.LOG_INFO("Modifying stepZ - accomodate a " + zDir + " zDir - [X:" + stepX + "][Y:" + stepY
						+ "][Z:" + stepZ + "]");
				break;
		}

		for (int i = stepX - 1; i <= stepX + 1; i++) {
			for (int j = stepZ - 1; j <= stepZ + 1; j++) {
				for (int h = stepY - 1; h <= stepY + 1; h++) {

					Utils.LOG_INFO("Block Facing - X:" + xDir + "    Z:" + zDir);
					Utils.LOG_INFO("(h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))");
					Utils.LOG_INFO("  " + (h != 0) + "   ||       " + (xDir + i != 0) + "       ||       "
							+ (zDir + j != 0) + "       &&    " + (i != 0) + "   ||   " + (j != 0));
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getAmountOfOutputs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Controller Block for the Testing", "Create the shapes for Multiblocks.",
		};
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity arg0, final byte arg1, final byte arg2, final byte arg3,
			final boolean arg4, final boolean arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new DEBUG_BLOCK_ShapeSpawner(this.mName);
	}

}
