package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class GT4Entity_Shelf_Iron extends GT4Entity_Shelf {
	public GT4Entity_Shelf_Iron(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GT4Entity_Shelf_Iron(String mName) {
		super(mName);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf_Iron(this.mName);
	}

	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return 216 + this.mType;
		}
		if (aSide == 0) {
			return 32;
		}
		if (aSide == 1) {
			return 29;
		}
		return 40;
	}
}
