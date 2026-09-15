package gregtech.api.util;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IGTHatchAdder<T> {

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
    default <T2 extends T> IGTHatchAdder<T2> rebrand() {
        return (IGTHatchAdder<T2>) this;
    }

    default IGTHatchAdder<T> orElse(IGTHatchAdder<? super T> fallback) {
        return (t, iGregTechTileEntity, aShort) -> IGTHatchAdder.this.apply(t, iGregTechTileEntity, aShort)
            || fallback.apply(t, iGregTechTileEntity, aShort);
    }
}
