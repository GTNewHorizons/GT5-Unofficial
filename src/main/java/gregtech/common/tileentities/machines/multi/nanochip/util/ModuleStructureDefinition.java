package gregtech.common.tileentities.machines.multi.nanochip.util;

import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;

/**
 * Utility class that implicitly adds the base structure piece of a nanochip assembly module to
 * a structure definition.
 */
public class ModuleStructureDefinition {

    public static <B extends MTENanochipAssemblyModuleBase<B>> StructureDefinition.Builder<B> builder() {
        return MTENanochipAssemblyModuleBase.addBaseStructure(StructureDefinition.<B>builder());
    }
}
