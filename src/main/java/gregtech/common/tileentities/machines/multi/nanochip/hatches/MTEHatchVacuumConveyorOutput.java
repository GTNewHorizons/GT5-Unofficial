package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Collections;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gregtech.common.tileentities.machines.multi.nanochip.factory.IVacuumStorage;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import it.unimi.dsi.fastutil.Pair;

public class MTEHatchVacuumConveyorOutput extends MTEHatchVacuumConveyor implements IVacuumStorage {

    public MTEHatchVacuumConveyorOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[] {});
    }

    public MTEHatchVacuumConveyorOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchVacuumConveyorOutput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { translateToLocalFormatted("GT5U.tooltip.nac.hatch.vc.base.1", TOOLTIP_COLORED),
            translateToLocal("GT5U.tooltip.nac.hatch.vc.base.2"),
            translateToLocal("GT5U.tooltip.nac.hatch.vc.output.base.1"),
            EnumChatFormatting.STRIKETHROUGH
                + "------------------------------------------------------------------------",
            translateToLocal("GT5U.tooltip.nac.hatch.vc.base.3"),
            translateToLocal("GT5U.tooltip.nac.hatch.vc.base.4") };
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public CircuitComponentPacket extractPacket() {
        CircuitComponentPacket outputPacket = this.contents;
        this.contents = null;
        return outputPacket;
    }

    @Override
    public MTENanochipAssemblyComplex getMainController() {
        return this.mainController;
    }

    @Override
    public List<Pair<Class<?>, Object>> getComponents() {
        return Collections.singletonList(Pair.of(IVacuumStorage.class, this));
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }
}
