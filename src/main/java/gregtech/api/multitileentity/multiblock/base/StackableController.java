package gregtech.api.multitileentity.multiblock.base;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

public abstract class StackableController<T extends StackableController<T>> extends PowerController<T> {

    protected static String STACKABLE_TOP = "STACKABLE_TOP";
    protected static String STACKABLE_MIDDLE = "STACKABLE_MIDDLE";
    protected static String STACKABLE_BOTTOM = "STACKABLE_BOTTOM";
    protected int stackCount = 0;

    /**
     * construct implementation for stackable multi-blocks
     */
    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        final int blueprintCount = (trigger.stackSize - 1) + getMinStacks();
        final int stackCount = Math.min(blueprintCount, getMaxStacks());

        buildState.startBuilding(getStartingStructureOffset());
        buildPiece(getStackableBottom(), trigger, hintsOnly, buildState.getCurrentOffset());
        buildState.addOffset(getStartingStackOffset());

        for (int i = 0; i < stackCount; i++) {
            buildPiece(getStackableMiddle(i), trigger, hintsOnly, buildState.getCurrentOffset());
            buildState.addOffset(getPerStackOffset());
        }
        if (hasTop()) {
            buildState.addOffset(getAfterLastStackOffset());
            buildPiece(getStackableTop(), trigger, hintsOnly, buildState.stopBuilding());
        } else {
            buildState.stopBuilding();
        }
    }

    /**
     * Stackable
     *
     * @return The minimum number of stacks required for this multi-block to form
     */
    public abstract int getMinStacks();

    /**
     * Stackable
     *
     * @return The maximum number of stacks allowed for this multi-block to form
     */
    public abstract int getMaxStacks();

    /**
     * Stackable
     *
     * @return The starting offset for the first stack
     */
    public abstract Vec3Impl getStartingStackOffset();

    /**
     * Stackable
     *
     * @return The per stack offset
     */
    public abstract Vec3Impl getPerStackOffset();

    /**
     * Stackable
     *
     * @return Whether this structure has a Top/Cap. Defaults to true.
     */
    public boolean hasTop() {
        return true;
    }

    /**
     * Stackable
     *
     * @return Any offset needed after the last stack
     */
    public Vec3Impl getAfterLastStackOffset() {
        return new Vec3Impl(0, 0, 0);
    }

    /**
     * checkMachine implementation for stackable multi-blocks
     */
    @Override
    public boolean checkMachine() {
        stackCount = 0;

        buildState.startBuilding(getStartingStructureOffset());
        if (!checkPiece(getStackableBottom(), buildState.getCurrentOffset())) return buildState.failBuilding();

        buildState.addOffset(getStartingStackOffset());

        for (int i = 0; i < getMaxStacks(); i++) {
            if (checkPiece(getStackableMiddle(i), buildState.getCurrentOffset())) {
                buildState.addOffset(getPerStackOffset());
                stackCount++;
            } else {
                break;
            }
        }
        if (stackCount < getMinStacks()) return buildState.failBuilding();

        buildState.addOffset(getAfterLastStackOffset());
        if (!checkPiece(getStackableTop(), buildState.stopBuilding())) {
            return buildState.failBuilding();
        }
        return super.checkMachine();
    }

    protected String getStackableTop() {
        return STACKABLE_TOP;
    }

    protected String getStackableMiddle(int stackIndex) {
        return STACKABLE_MIDDLE;
    }

    protected String getStackableBottom() {
        return STACKABLE_BOTTOM;
    }
}
