package gregtech.api.util;


import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IGT_HatchAdder<T> {
    /**
     * Callback to add hatch, needs to check if hatch is valid (and add it)
     *
     * @param iGregTechTileEntity hatch
     * @param aShort              requested texture index, or null if not...
     * @return managed to add hatch (structure still valid)
     */
    boolean apply(T t, IGregTechTileEntity iGregTechTileEntity, Short aShort);
}
