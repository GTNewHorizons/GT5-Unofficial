package gregtech.api.structure;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;

/**
 * An object which manages a multi's structure. Usually implemented by the multi itself.
 */
public interface IStructureProvider<MTE extends MTEMultiBlockBase & IAlignment & IStructureProvider<MTE>> {

    /**
     * Gets the minimal (default trigger w/ stackSize = 1) structure definition.
     * Override {@link #getMaxDefinition()} if your structure has a variable size.
     * Used primarily to determine the controller offset and casing counts, but it is also passed to
     * {@link #compile(String[][])}.
     */
    String[][] getDefinition();

    /**
     * Returns the max-sized structure definition.
     */
    default String[][] getMaxDefinition() {
        return getDefinition();
    }

    /**
     * Compiles a structure definition. Usually configures a {@link StructureWrapper}, then calls
     * {@link StructureWrapper#buildStructure(String[][])}.
     * 
     * @param definition The return value of {@link #getDefinition()}
     * @return A valid structure definition
     */
    IStructureDefinition<MTE> compile(String[][] definition);

    /**
     * Gets the structure instance. Return is undefined/null for prototype MTEs, but should be well-defined for instance
     * MTEs.
     */
    IStructureInstance getStructureInstance();
}
