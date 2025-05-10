package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.IConnectsToVacuumConveyor;

public class MTEHatchVacuumConveyorInput extends MTEHatchVacuumConveyor implements IConnectsToVacuumConveyor {

    public MTEHatchVacuumConveyorInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[] {});
    }

    public MTEHatchVacuumConveyorInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchVacuumConveyorInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isComponentsInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public IConnectsToVacuumConveyor getNext(IConnectsToVacuumConveyor source) {
        return null;
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {}

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
}
