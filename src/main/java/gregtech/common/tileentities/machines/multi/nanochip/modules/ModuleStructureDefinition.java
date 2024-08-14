package gregtech.common.tileentities.machines.multi.nanochip.modules;

import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.common.tileentities.machines.multi.nanochip.GT_MetaTileEntity_NanochipAssemblyModuleBase;

/**
 * Utility class that implicitly adds the base structure piece of a nanochip assembly module to
 * a structure definition.
 */
public class ModuleStructureDefinition {

    public static <B> StructureDefinition.Builder<B> builder() {
        return GT_MetaTileEntity_NanochipAssemblyModuleBase.addBaseStructure(StructureDefinition.builder());
    }
}
