package gregtech.common.tileentities.storage;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;

public class MTEQuantumTank extends MTEDigitalTankBase {

    public MTEQuantumTank(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEQuantumTank(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public MTEQuantumTank(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEQuantumTank(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getInfoData() {

        if (mFluid == null) {
            return new String[] { EnumChatFormatting.BLUE + "Quantum Tank" + EnumChatFormatting.RESET, "Stored Fluid:",
                EnumChatFormatting.GOLD + "No Fluid" + EnumChatFormatting.RESET,
                EnumChatFormatting.GREEN + Integer.toString(0)
                    + " L"
                    + EnumChatFormatting.RESET
                    + " "
                    + EnumChatFormatting.YELLOW
                    + GTUtility.formatNumbers(getCapacity())
                    + " L"
                    + EnumChatFormatting.RESET };
        }
        return new String[] { EnumChatFormatting.BLUE + "Quantum Tank" + EnumChatFormatting.RESET, "Stored Fluid:",
            EnumChatFormatting.GOLD + mFluid.getLocalizedName() + EnumChatFormatting.RESET,
            EnumChatFormatting.GREEN + GTUtility.formatNumbers(mFluid.amount)
                + " L"
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(getCapacity())
                + " L"
                + EnumChatFormatting.RESET };
    }
}
