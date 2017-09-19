package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityCable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.util.EnumChatFormatting;

import static gregtech.api.enums.GT_Values.VN;

public class GregtechMetaPipeEntity_Cable extends GT_MetaPipeEntity_Cable implements IMetaTileEntityCable {
	public GregtechMetaPipeEntity_Cable(final int aID, final String aName, final String aNameRegional, final float aThickNess, final Materials aMaterial, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock) {
		super(aID, aName, aNameRegional, aThickNess, aMaterial, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
	}

	public GregtechMetaPipeEntity_Cable(final String aName, final float aThickNess, final Materials aMaterial, final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated, final boolean aCanShock) {
		super(aName, aThickNess, aMaterial, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaPipeEntity_Cable(this.mName, this.mThickNess, this.mMaterial, this.mCableLossPerMeter, this.mAmperage, this.mVoltage, this.mInsulated, this.mCanShock);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Max Voltage: %%%" + EnumChatFormatting.GREEN + mVoltage + " (" + VN[GT_Utility.getTier(mVoltage)] + ")" + EnumChatFormatting.GRAY,
				"Max Amperage: %%%" + EnumChatFormatting.YELLOW + mAmperage + EnumChatFormatting.GRAY,
				"Loss/Meter/Ampere: %%%" + EnumChatFormatting.RED + mCableLossPerMeter + EnumChatFormatting.GRAY + "%%% EU-Volt",
				CORE.GT_Tooltip
		};
	}
}