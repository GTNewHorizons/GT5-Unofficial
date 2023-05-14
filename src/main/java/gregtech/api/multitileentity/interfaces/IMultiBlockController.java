package gregtech.api.multitileentity.interfaces;

import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.logic.PowerLogic;
import gregtech.api.multitileentity.enums.MultiTileCasingPurpose;

public interface IMultiBlockController
    extends IMultiTileEntity, IMultiBlockFluidHandler, IMultiBlockInventory, UpgradableMuTE {

    boolean checkStructure(boolean aForceReset);

    /** Set the structure as having changed, and trigger an update */
    void onStructureChange();

    @Override
    ChunkCoordinates getCoords();

    FluidStack getDrainableFluid(ForgeDirection side);

    FluidStack getDrainableFluid(ForgeDirection side, Fluid fluid);

    boolean isLiquidInput(ForgeDirection side);

    boolean isLiquidOutput(ForgeDirection side);

    void registerCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart part);

    void unregisterCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart part);

    void registerCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part);

    void unregisterCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part);

    void registerInventory(String aName, String aID, int aInventorySize, int aType);

    void unregisterInventory(String aName, String aID, int aType);

    void changeInventoryName(String aName, String aID, int aType);

    PowerLogic getPowerLogic(IMultiBlockPart part, ForgeDirection side);

    ModularWindow createWindowGUI(UIBuildContext buildContext);
}
