package gregtech.codegen;

import gregtech.generated.GeneratedMaterialProperties;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;

/**
 * Provides bootstrapping methods needed by the codegen-generated classes to set up materials.
 */
public class MaterialsBackend {
    private static final HashMap<String, Material> registeredMaterials = new HashMap<>();

    /**
     * invokedynamic bootstrap method used to lazily get registered materials in the generated MaterialsAccess class.
     * It is invoked once by the JVM, when it returns it's replaced with the constant return method handle this method returns.
     * @param lookup Implicit invokedynamic argument
     * @param name Name of the material looked up
     * @param factoryType Implicit invokedynamic argument
     * @return The CallSite needed for invokedynamic to work
     */
    public static CallSite accessBootstrap(final MethodHandles.Lookup lookup, final String name, final MethodType factoryType) {
        Material mat = registeredMaterials.get(name);
        if (mat == null) {
            throw new IllegalStateException("Trying to access material " + name + " before it has been registered");
        }
        return new ConstantCallSite(MethodHandles.constant(Material.class, mat));
    }

    public static void loadMaterials() {
        if (!registeredMaterials.isEmpty()) {
            throw new IllegalStateException("Cannot load materials twice");
        }
    }
}
