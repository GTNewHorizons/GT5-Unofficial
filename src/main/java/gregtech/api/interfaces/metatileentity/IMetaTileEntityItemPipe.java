package gregtech.api.interfaces.metatileentity;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.util.GT_Utility;
import java.util.Map;

public interface IMetaTileEntityItemPipe extends IMetaTileEntity {
    /**
     * @return if this Pipe can still be used.
     */
    boolean pipeCapacityCheck();

    /**
     * @return if this Pipe can still be used.
     */
    boolean incrementTransferCounter(int aIncrement);

    /**
     * Sends an ItemStack from aSender to the adjacent Blocks.
     *
     * @param aSender the BaseMetaTileEntity sending the Stack.
     * @return if it was able to send something
     */
    boolean sendItemStack(Object aSender);

    /**
     * Executes the Sending Code for inserting Stacks into the TileEntities.
     *
     * @param aSender the BaseMetaTileEntity sending the Stack.
     * @param aSide   the Side of the PIPE facing the TileEntity.
     * @return if this Side was allowed to Output into the Block.
     */
    boolean insertItemStackIntoTileEntity(Object aSender, byte aSide);

    /**
     * Can be used to make flow control Pipes, like Redpowers Restriction Tubes.
     * Every normal Pipe returns a Value of 32768, so you can easily insert lower Numbers to set Routing priorities.
     * Negative Numbers to "suck" Items into a certain direction are also possible.
     */
    int getStepSize();

    /**
     * Utility for the Item Network
     */
    class Util {
        /**
         * @return a List of connected Item Pipes
         */
        public static Map<IMetaTileEntityItemPipe, Long> scanPipes(
                IMetaTileEntityItemPipe aMetaTileEntity,
                Map<IMetaTileEntityItemPipe, Long> aMap,
                long aStep,
                boolean aSuckItems,
                boolean aIgnoreCapacity) {
            aStep += aMetaTileEntity.getStepSize();
            if (aIgnoreCapacity || aMetaTileEntity.pipeCapacityCheck())
                if (aMap.get(aMetaTileEntity) == null || aMap.get(aMetaTileEntity) > aStep) {
                    final IGregTechTileEntity aBaseMetaTileEntity = aMetaTileEntity.getBaseMetaTileEntity();
                    aMap.put(aMetaTileEntity, aStep);
                    for (byte i = 0, j = 0; i < 6; i++) {
                        if (aMetaTileEntity instanceof IConnectable
                                && !((IConnectable) aMetaTileEntity).isConnectedAtSide(i)) continue;
                        j = GT_Utility.getOppositeSide(i);
                        if (aSuckItems) {
                            if (aBaseMetaTileEntity.getCoverInfoAtSide(i).letsItemsIn(-2)) {
                                final IGregTechTileEntity tItemPipe =
                                        aBaseMetaTileEntity.getIGregTechTileEntityAtSide(i);
                                if (aBaseMetaTileEntity.getColorization() >= 0) {
                                    final byte tColor = tItemPipe.getColorization();
                                    if (tColor >= 0 && tColor != aBaseMetaTileEntity.getColorization()) {
                                        continue;
                                    }
                                }
                                if (tItemPipe instanceof BaseMetaPipeEntity) {
                                    final IMetaTileEntity tMetaTileEntity = tItemPipe.getMetaTileEntity();
                                    if (tMetaTileEntity instanceof IMetaTileEntityItemPipe
                                            && tItemPipe.getCoverInfoAtSide(j).letsItemsOut(-2)) {
                                        scanPipes(
                                                (IMetaTileEntityItemPipe) tMetaTileEntity,
                                                aMap,
                                                aStep,
                                                aSuckItems,
                                                aIgnoreCapacity);
                                    }
                                }
                            }
                        } else {
                            if (aBaseMetaTileEntity.getCoverInfoAtSide(i).letsItemsOut(-2)) {
                                final IGregTechTileEntity tItemPipe =
                                        aBaseMetaTileEntity.getIGregTechTileEntityAtSide(i);
                                if (tItemPipe != null) {
                                    if (aBaseMetaTileEntity.getColorization() >= 0) {
                                        final byte tColor = tItemPipe.getColorization();
                                        if (tColor >= 0 && tColor != aBaseMetaTileEntity.getColorization()) {
                                            continue;
                                        }
                                    }
                                    if (tItemPipe instanceof BaseMetaPipeEntity) {
                                        final IMetaTileEntity tMetaTileEntity = tItemPipe.getMetaTileEntity();
                                        if (tMetaTileEntity instanceof IMetaTileEntityItemPipe
                                                && tItemPipe
                                                        .getCoverInfoAtSide(i)
                                                        .letsItemsIn(-2)) {
                                            scanPipes(
                                                    (IMetaTileEntityItemPipe) tMetaTileEntity,
                                                    aMap,
                                                    aStep,
                                                    aSuckItems,
                                                    aIgnoreCapacity);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            return aMap;
        }
    }
}
