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

    /**
     * hack to work around java generic issues.
     */
    @SuppressWarnings("unchecked")
    default <T2 extends T> IGT_HatchAdder<T2> rebrand() {
        return (IGT_HatchAdder<T2>) this;
    }

    default IGT_HatchAdder<T> orElse(IGT_HatchAdder<? super T> fallback) {
        return (t, iGregTechTileEntity, aShort) -> IGT_HatchAdder.this.apply(t, iGregTechTileEntity, aShort)
            || fallback.apply(t, iGregTechTileEntity, aShort);
    }
}
