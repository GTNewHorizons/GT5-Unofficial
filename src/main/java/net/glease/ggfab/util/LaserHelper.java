package net.glease.ggfab.util;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;

import java.lang.reflect.Field;

public class LaserHelper {
    private static final Class<?> GT_MetaTileEntity_Hatch_EnergyMulti;
    private static final Field fieldAmperes;

    static {
        Class<?> tmp1;
        Field tmp2;
        try {
            tmp1 = Class.forName("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti");
        } catch (ClassNotFoundException e) {
            tmp1 = null;
        }
        if (tmp1 != null) {
            try {
                tmp2 = tmp1.getField("Amperes");
                tmp2.setAccessible(true);
            } catch (ReflectiveOperationException e) {
                tmp1 = null;
                tmp2 = null;
            }
        } else {
            tmp2 = null;
        }
        GT_MetaTileEntity_Hatch_EnergyMulti = tmp1;
        fieldAmperes = tmp2;
    }

    public static long getAmperes(GT_MetaTileEntity_Hatch hatch) {
        if (GT_MetaTileEntity_Hatch_EnergyMulti.isInstance(hatch)) {
            try {
                // target field is int, not long
                return (int) fieldAmperes.get(hatch);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return hatch.maxAmperesIn();
    }
}
