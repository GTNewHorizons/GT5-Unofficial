package gregtech.common.tileentities.storage;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;

public class MTESuperTank extends MTEDigitalTankBase {

    public MTESuperTank(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTESuperTank(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public MTESuperTank(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESuperTank(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getInfoData() {

        if (mFluid == null) {
            return new String[] { EnumChatFormatting.BLUE + "Super Tank" + EnumChatFormatting.RESET, "Stored Fluid:",
                EnumChatFormatting.GOLD + "No Fluid" + EnumChatFormatting.RESET,
                EnumChatFormatting.GREEN + "0 L"
                    + EnumChatFormatting.RESET
                    + " "
                    + EnumChatFormatting.YELLOW
                    + GTUtility.formatNumbers(getCapacity())
                    + " L"
                    + EnumChatFormatting.RESET };
        }
        return new String[] { EnumChatFormatting.BLUE + "Super Tank" + EnumChatFormatting.RESET, "Stored Fluid:",
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
