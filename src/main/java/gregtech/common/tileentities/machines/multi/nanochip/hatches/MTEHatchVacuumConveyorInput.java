package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryGrid;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryNetwork;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public class MTEHatchVacuumConveyorInput extends MTEHatchVacuumConveyor {

    public MTEHatchVacuumConveyorInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[] {});
    }

    public MTEHatchVacuumConveyorInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        VacuumFactoryGrid.INSTANCE.removeElement(this);
    }


    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchVacuumConveyorInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Must be " + MTENanochipAssemblyComplexGui.coloredString() + " to work",
            "Can be installed in the " + EnumChatFormatting.GREEN + "Nanochip Assembly Complex",
            "Provides" + EnumChatFormatting.YELLOW
                + " Circuit Component "
                + EnumChatFormatting.GRAY
                + "input for NAC modules",
            EnumChatFormatting.STRIKETHROUGH
                + "------------------------------------------------------------------------",
            EnumChatFormatting.YELLOW + "Outputs from recipes with inputs from a colored Vacuum Conveyor Input",
            EnumChatFormatting.YELLOW + "will be placed in a Vacuum Conveyor Output of the corresponding color", };
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void onFirstTick(IGregTechTileEntity base) {
        VacuumFactoryGrid.INSTANCE.addElement(this);
        super.onFirstTick(base);
    }

    // Try to consume a stack of fake input items from this hatch. Returns the amount of items consumed.
    public int tryConsume(ItemStack stack) {
        if (contents == null) return 0;
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(stack);
        Map<CircuitComponent, Long> inventory = contents.getComponents();
        // Find this component in the inventory
        Long amount = inventory.get(component);
        if (amount != null) {
            // If found, consume as much as possible
            int toConsume = Math.min((int) Math.min(Integer.MAX_VALUE, amount), stack.stackSize);
            amount -= toConsume;
            if (amount > 0) {
                inventory.put(component, amount);
            } else {
                // Remove component from inventory if it is fully drained
                inventory.remove(component);
            }
            return toConsume;
        }
        return 0;
    }

    @Override
    public VacuumFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(VacuumFactoryNetwork network) {
        this.network = network;
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        contents = this.getNetwork()
            .getCircuitComponentPacket();
        super.onPostTick(aBaseMetaTileEntity, aTick);

    }
}
