package gregtech.common.tileentities.machines;

public interface ISmartInputHatch {

    /*
     * An interface to allow advanced interaction between hatches and a multiblock controller
     * Adapted from the Crafting Input Buffer functionality
     */

    // Have the contents of the hatch changed since the last check?
    boolean justUpdated();

    public boolean doFastRecipeCheck();

}
