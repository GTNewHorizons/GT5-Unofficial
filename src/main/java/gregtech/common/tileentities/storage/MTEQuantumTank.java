package gregtech.common.tileentities.storage;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTEQuantumTank extends MTEDigitalTankBase {

    public MTEQuantumTank(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
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
            return new String[] {
                EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.infodata.quantum_tank.name")
                    + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.infodata.digital_tank.stored_fluid"),
                EnumChatFormatting.GOLD
                    + StatCollector.translateToLocal("GT5U.infodata.digital_tank.stored_fluid.empty")
                    + EnumChatFormatting.RESET,
                EnumChatFormatting.GREEN + Integer.toString(0)
                    + " L"
                    + EnumChatFormatting.RESET
                    + " "
                    + EnumChatFormatting.YELLOW
                    + formatNumber(getCapacity())
                    + " L"
                    + EnumChatFormatting.RESET };
        }
        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.infodata.quantum_tank.name")
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.infodata.digital_tank.stored_fluid"),
            EnumChatFormatting.GOLD + mFluid.getLocalizedName() + EnumChatFormatting.RESET,
            EnumChatFormatting.GREEN + formatNumber(mFluid.amount)
                + " L"
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.YELLOW
                + formatNumber(getCapacity())
                + " L"
                + EnumChatFormatting.RESET };
    }
}
