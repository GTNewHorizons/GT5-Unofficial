package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import java.util.Collections;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gregtech.common.tileentities.machines.multi.nanochip.factory.IVacuumStorage;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryNetwork;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import it.unimi.dsi.fastutil.Pair;

public class MTEHatchVacuumConveyorOutput extends MTEHatchVacuumConveyor {

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
        return new String[] { "Must be " + MTENanochipAssemblyComplexGui.coloredString() + " to work",
            "Can be installed in the " + EnumChatFormatting.GREEN + "Nanochip Assembly Complex",
            "Provides" + EnumChatFormatting.YELLOW
                + " Circuit Component "
                + EnumChatFormatting.GRAY
                + "output for NAC modules",
            EnumChatFormatting.STRIKETHROUGH
                + "------------------------------------------------------------------------",
            EnumChatFormatting.YELLOW + "Outputs from recipes with inputs from a colored Vacuum Conveyor Input",
            EnumChatFormatting.YELLOW + "will be placed in a Vacuum Conveyor Output of the corresponding color", };
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public VacuumFactoryNetwork getNetwork() {
        return network;
    }

    // SAVE ME
    @Override
    public CircuitComponentPacket getcomponentPacket() {
        return this.contents;
    }

    @Override
    public List<Pair<Class<?>, Object>> getComponents() {
        return Collections.singletonList(Pair.of(IVacuumStorage.class, this));
    }

    @Override
    public MTENanochipAssemblyComplex getAssemblyComplex() {
        return this.getMainController();
    }

    @Override
    public void setNetwork(VacuumFactoryNetwork network) {
        this.network = network;
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }
}
