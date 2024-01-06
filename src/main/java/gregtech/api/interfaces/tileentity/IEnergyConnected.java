package gregtech.api.interfaces.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyReceiver;
import gregtech.api.GregTech_API;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.util.GT_Utility;
import ic2.api.energy.tile.IEnergySink;

/**
 * Interface for getting Connected to the GregTech Energy Network.
 * <p/>
 * This is all you need to connect to the GT Network. IColoredTileEntity is needed for not connecting differently
 * coloured Blocks to each other. IHasWorldObjectAndCoords is needed for the InWorld related Stuff. @BaseTileEntity does
 * implement most of that Interface.
 */
public interface IEnergyConnected extends IColoredTileEntity {

    /**
     * Inject Energy Call for Electricity. Gets called by EnergyEmitters to inject Energy into your Block
     * <p/>
     * Note: you have to check for @inputEnergyFrom because the Network won't check for that by itself.
     *
     * @param side 0 - 5 = Vanilla Directions of YOUR Block the Energy gets inserted to. 6 = No specific Side (don't do
     *             Side checks for this Side)
     * @return amount of used Amperes. 0 if not accepted anything.
     */
    long injectEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage);

    /**
     * Sided Energy Input
     */
    boolean inputEnergyFrom(ForgeDirection side);

    default boolean inputEnergyFrom(ForgeDirection side, boolean waitForActive) {
        return inputEnergyFrom(side);
    }

    /**
     * Sided Energy Output
     */
    boolean outputsEnergyTo(ForgeDirection side);

    default boolean outputsEnergyTo(ForgeDirection side, boolean waitForActive) {
        return outputsEnergyTo(side);
    }

    /**
     * Utility for the Network
     */
    final class Util {

        /**
         * Emits Energy to the E-net. Also compatible with adjacent IC2 TileEntities.
         *
         * @return the used Amperage.
         */
        public static long emitEnergyToNetwork(long aVoltage, long aAmperage, IEnergyConnected aEmitter) {
            long rUsedAmperes = 0;
            if (!(aEmitter instanceof IHasWorldObjectAndCoords emitterTile)) {
                return 0;
            }

            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (rUsedAmperes > aAmperage) break;
                if (!aEmitter.outputsEnergyTo(side)) {
                    continue;
                }

                final ForgeDirection oppositeSide = side.getOpposite();
                final TileEntity tTileEntity = emitterTile.getTileEntityAtSide(side);
                if (tTileEntity instanceof PowerLogicHost host) {

                    final PowerLogic logic = host.getPowerLogic(oppositeSide);
                    if (logic == null || logic.isEnergyReceiver()) {
                        continue;
                    }

                    rUsedAmperes += logic.injectEnergy(aVoltage, aAmperage - rUsedAmperes);
                } else if (tTileEntity instanceof IEnergyConnected energyConnected) {
                    if (aEmitter.getColorization() >= 0) {
                        final byte tColor = energyConnected.getColorization();
                        if (tColor >= 0 && tColor != aEmitter.getColorization()) continue;
                    }
                    rUsedAmperes += energyConnected.injectEnergyUnits(oppositeSide, aVoltage, aAmperage - rUsedAmperes);

                } else if (tTileEntity instanceof IEnergySink sink) {
                    if (sink.acceptsEnergyFrom((TileEntity) aEmitter, oppositeSide)) {
                        while (aAmperage > rUsedAmperes && sink.getDemandedEnergy() > 0
                            && sink.injectEnergy(oppositeSide, aVoltage, aVoltage) < aVoltage) rUsedAmperes++;
                    }
                } else if (GregTech_API.mOutputRF && tTileEntity instanceof IEnergyReceiver receiver) {
                    final int rfOut = GT_Utility.safeInt(aVoltage * GregTech_API.mEUtoRF / 100);
                    if (receiver.receiveEnergy(oppositeSide, rfOut, true) == rfOut) {
                        receiver.receiveEnergy(oppositeSide, rfOut, false);
                        rUsedAmperes++;
                    }
                }
            }
            return rUsedAmperes;
        }
    }
}
