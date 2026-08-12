package gregtech.common.tileentities.storage;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatFluid;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTESuperTank extends MTEDigitalTankBase {

    public MTESuperTank(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
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
            return new String[] { "GT5U.infodata.super_tank.name", "GT5U.infodata.digital_tank.stored_fluid",
                "GT5U.infodata.digital_tank.stored_fluid.empty",
                EnumChatFormatting.GREEN + formatFluid(0)
                    + EnumChatFormatting.RESET
                    + " "
                    + EnumChatFormatting.YELLOW
                    + formatFluid(getCapacity())
                    + EnumChatFormatting.RESET };
        }
        return new String[] { "GT5U.infodata.super_tank.name", "GT5U.infodata.digital_tank.stored_fluid",
            EnumChatFormatting.GOLD + mFluid.getLocalizedName() + EnumChatFormatting.RESET,
            EnumChatFormatting.GREEN + formatFluid(mFluid.amount)
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.YELLOW
                + formatFluid(getCapacity())
                + EnumChatFormatting.RESET };
    }
}
