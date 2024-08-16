package gregtech.common.tileentities.machines.multi.nanochip.util;

import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.common.tileentities.machines.multi.nanochip.GT_MetaTileEntity_NanochipAssemblyModuleBase;

/**
 * Utility class that implicitly adds the base structure piece of a nanochip assembly module to
 * a structure definition.
 */
public class ModuleStructureDefinition {

    public static <B extends GT_MetaTileEntity_NanochipAssemblyModuleBase<B>> StructureDefinition.Builder<B> builder() {
        return GT_MetaTileEntity_NanochipAssemblyModuleBase.addBaseStructure(StructureDefinition.<B>builder());
    }
}
