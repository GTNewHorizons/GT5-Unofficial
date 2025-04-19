package gtnhintergalactic.spaceprojects;

import gregtech.common.misc.spaceprojects.base.SpaceProject;

/**
 * Space project that boosts the mining module
 *
 * @author BlueWeabo
 */
public class ProjectAsteroidOutpost extends SpaceProject {

    /** Computation reduction per installed upgrade */
    private static final float COMPUTATION_DISCOUNT_PER_UPGRADE = 0.10f;
    /** Plasma reduction per installed upgrade */
    private static final float PLASMA_DISCOUNT_PER_UPGRADE = 0.10f;

    /**
     * @return Total computation discount of this project
     */
    public float getComputationDiscount() {
        return COMPUTATION_DISCOUNT_PER_UPGRADE + COMPUTATION_DISCOUNT_PER_UPGRADE * upgradesInstalled.size();
    }

    /**
     * @return Total plasma discount of this project
     */
    public float getPlasmaDiscount() {
        return PLASMA_DISCOUNT_PER_UPGRADE + PLASMA_DISCOUNT_PER_UPGRADE * upgradesInstalled.size();
    }
}
