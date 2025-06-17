package gregtech.api.metatileentity;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class MTEOverrideCharacteristics {

    /**
     * True if this MTE overrides {@link MetaTileEntity#getEUVar()} or {@link MetaTileEntity#setEUVar(long)}.
     * Used to optimize EU draining.
     */
    public final boolean hasCustomEUStorage;

    public MTEOverrideCharacteristics(Class<? extends MetaTileEntity> clazz) {
        try {
            boolean overridesSetEUVar = clazz.getMethod("setEUVar", long.class).getDeclaringClass() != MetaTileEntity.class;
            boolean overridesGetEUVar = clazz.getMethod("getEUVar").getDeclaringClass() != MetaTileEntity.class;

            hasCustomEUStorage = overridesSetEUVar || overridesGetEUVar;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Map<Class<? extends MetaTileEntity>, MTEOverrideCharacteristics> OVERRIDE_CACHE = new Object2ObjectOpenHashMap<>();

    public static MTEOverrideCharacteristics getCharacteristics(Class<? extends MetaTileEntity> clazz) {
        return OVERRIDE_CACHE.computeIfAbsent(clazz, MTEOverrideCharacteristics::new);
    }
}
