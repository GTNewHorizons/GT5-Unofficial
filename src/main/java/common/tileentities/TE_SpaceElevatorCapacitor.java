package common.tileentities;

import net.minecraft.tileentity.TileEntity;

public class TE_SpaceElevatorCapacitor extends TileEntity {

    private float chargeLevel = 0.0F;
    private boolean isDamaged = true;

    /**
     * Called by {@link GTMTE_SpaceElevator} while charging
     * @param charge
     *              Current elevator charge
     * @param maxCharge
     *              Charge level it is trying to reach
     */
    public void updateChargeLevel(int charge, int maxCharge) {
        chargeLevel = ((float) charge) / ((float) maxCharge);
    }

    /**
     * Called by {@link client.renderer.TESR_SECapacitor} to calculate the block's colour saturation
     * @return
     *          Charge level from 0.0F to 1.0F
     */
    public float getChargeLevel() {
        return chargeLevel;
    }

    /**
     * Called by {@link GTMTE_SpaceElevator} in case of power loss
     */
    public void resetChargeLevel() {
        chargeLevel = 0.0F;
    }

    /**
     * Called by {@link GTMTE_SpaceElevator} in case of maintenance issues
     * @param isDamaged
     *            has maintenance issue
     */
    public void setIsDamaged(boolean isDamaged) {
        this.isDamaged = isDamaged;
    }

    /**
     * Called by {@link client.renderer.TESR_SECapacitor} to check whether the block should be rendered red
     * @return
     *          should be rendered red
     */
    public boolean isDamaged() {
        return isDamaged;
    }
}
