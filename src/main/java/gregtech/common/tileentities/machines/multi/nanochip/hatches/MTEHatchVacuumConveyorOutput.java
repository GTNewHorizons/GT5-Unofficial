package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTEVacuumConveyorPipe;
import gregtech.common.tileentities.machines.multi.nanochip.util.IConnectsToVacuumConveyor;

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
    public boolean isComponentsInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        IConnectsToVacuumConveyor current = this, source = this, next;
        int range = 0;
        while ((next = current.getNext(source)) != null && range++ < 1000) {
            if (next instanceof MTEHatchVacuumConveyorInput) {
                ((MTEHatchVacuumConveyorInput) next).unifyPacket(contents);
                // Only clear contents if we actually moved the packet
                contents = null;
                break;
            }
            source = current;
            current = next;
        }
    }

    @Override
    public IConnectsToVacuumConveyor getNext(IConnectsToVacuumConveyor source) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        byte color = base.getColorization();
        if (color < 0) {
            return null;
        }
        IGregTechTileEntity next = base.getIGregTechTileEntityAtSide(base.getFrontFacing());
        if (next == null) {
            return null;
        }
        IMetaTileEntity meta = next.getMetaTileEntity();
        if (meta instanceof MTEVacuumConveyorPipe) {
            ((MTEVacuumConveyorPipe) meta).markUsed();
            return (IConnectsToVacuumConveyor) meta;
        } else if (meta instanceof MTEHatchVacuumConveyorInput
            && ((MTEHatchVacuumConveyorInput) meta).getColorization() == color
            && ((MTEHatchVacuumConveyorInput) meta).canConnect(
                base.getFrontFacing()
                    .getOpposite())) {
                        return (IConnectsToVacuumConveyor) meta;
                    }
        return null;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return isOutputFacing(side);
    }
}
