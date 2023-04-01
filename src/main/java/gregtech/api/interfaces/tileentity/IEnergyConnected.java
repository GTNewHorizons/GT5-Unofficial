package gregtech.api.interfaces.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyReceiver;
import gregtech.api.GregTech_API;
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
     * @param aSide 0 - 5 = Vanilla Directions of YOUR Block the Energy gets inserted to. 6 = No specific Side (don't do
     *              Side checks for this Side)
     * @return amount of used Amperes. 0 if not accepted anything.
     */
    long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage);

    /**
     * Sided Energy Input
     */
    boolean inputEnergyFrom(byte aSide);

    default boolean inputEnergyFrom(byte aSide, boolean waitForActive) {
        return inputEnergyFrom(aSide);
    }

    /**
     * Sided Energy Output
     */
    boolean outputsEnergyTo(byte aSide);

    default boolean outputsEnergyTo(byte aSide, boolean waitForActive) {
        return outputsEnergyTo(aSide);
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
            if (aEmitter instanceof IHasWorldObjectAndCoords) {
                IHasWorldObjectAndCoords emitterTile = (IHasWorldObjectAndCoords) aEmitter;
                for (byte i = 0, j = 0; i < 6 && aAmperage > rUsedAmperes; i++) {
                    if (aEmitter.outputsEnergyTo(i)) {
                        j = GT_Utility.getOppositeSide(i);
                        final TileEntity tTileEntity = emitterTile.getTileEntityAtSide(i);
                        if (tTileEntity instanceof IEnergyConnected) {
                            if (aEmitter.getColorization() >= 0) {
                                final byte tColor = ((IEnergyConnected) tTileEntity).getColorization();
                                if (tColor >= 0 && tColor != aEmitter.getColorization()) continue;
                            }
                            rUsedAmperes += ((IEnergyConnected) tTileEntity).injectEnergyUnits(
                                    j,
                                    aVoltage,
                                    aAmperage - rUsedAmperes);

                        } else if (tTileEntity instanceof IEnergySink) {
                            if (((IEnergySink) tTileEntity).acceptsEnergyFrom(
                                    (TileEntity) aEmitter,
                                    ForgeDirection.getOrientation(j))) {
                                while (aAmperage > rUsedAmperes && ((IEnergySink) tTileEntity).getDemandedEnergy() > 0
                                        && ((IEnergySink) tTileEntity).injectEnergy(
                                                ForgeDirection.getOrientation(j),
                                                aVoltage,
                                                aVoltage) < aVoltage)
                                    rUsedAmperes++;
                            }
                        } else if (GregTech_API.mOutputRF && tTileEntity instanceof IEnergyReceiver) {
                            final ForgeDirection tDirection = ForgeDirection.getOrientation(i)
                                                                            .getOpposite();
                            final int rfOut = GT_Utility.safeInt(aVoltage * GregTech_API.mEUtoRF / 100);
                            if (((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, rfOut, true) == rfOut) {
                                ((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, rfOut, false);
                                rUsedAmperes++;
                            }
                        }
                    }
                }
            }
            return rUsedAmperes;
        }
    }
}
