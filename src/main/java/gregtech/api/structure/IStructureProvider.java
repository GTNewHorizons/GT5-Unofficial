package gregtech.api.structure;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

/**
 * An object which manages a multi's structure. Usually implemented by the multi itself.
 */
public interface IStructureProvider<MTE> {

    /**
     * Gets the minimal (default trigger w/ stackSize = 1) structure definition.
     * Override {@link #getMaxDefinition()} if your structure has a variable size.
     * This is primarily used to determine the controller offset, casing counts, and the structure dimensions, but it is
     * also passed to {@link #compile(String[][])}.
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
    IStructureInstance<MTE> getStructureInstance();
}
