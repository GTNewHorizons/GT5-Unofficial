package gregtech.api.multitileentity.interfaces;

import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.fluids.FluidStack;

public interface IMultiBlockController
        extends IMultiTileEntity, IMultiBlockFluidHandler, IMultiBlockInventory, IMultiBlockEnergy {

    boolean checkStructure(boolean aForceReset);

    /** Set the structure as having changed, and trigger an update */
    void onStructureChange();

    @Override
    ChunkCoordinates getCoords();

    FluidStack getDrainableFluid(byte aSide);

    boolean isLiquidInput(byte aSide);

    boolean isLiquidOutput(byte aSide);

    void registerCoveredPartOnSide(final int aSide, IMultiBlockPart part);

    void unregisterCoveredPartOnSide(final int aSide, IMultiBlockPart part);

    void registerInventory(String aName, String aID, int aInventorySize, int aType);

    void unregisterInventory(String aName, String aID, int aType);

    void changeInventoryName(String aName, String aID, int aType);
}
