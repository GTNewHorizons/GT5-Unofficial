package common.tileentities;

import net.minecraft.tileentity.TileEntity;

public class TE_SpaceElevatorCapacitor extends TileEntity {

    private float chargeLevel = 0.0F;

    /**
     * Called by the space elevator controller while charging
     * @param charge
     *              Current elevator charge
     * @param maxCharge
     *              Charge level it is trying to reach
     */
    public void updateChargeLevel(int charge, int maxCharge) {
        chargeLevel = ((float) charge) / ((float) maxCharge);
    }

    /**
     * Called by this block's renderer to calculate the block's colour saturation
     * @return
     *          Charge level from 0.0F to 1.0F
     */
    public float getChargeLevel() {
        return chargeLevel;
    }

    /**
     * Called by the space elevator in case of power loss
     */
    public void resetChargeLevel() {
        chargeLevel = 0.0F;
    }

    long tickCounter = 0;
    @Override
    public void updateEntity() {
        if(tickCounter == 20){
            chargeLevel = Float.compare(chargeLevel, 0.0F) == 0 ? 1.0F : 0.0F;
            tickCounter = 0;
        }
        tickCounter++;
    }
}
