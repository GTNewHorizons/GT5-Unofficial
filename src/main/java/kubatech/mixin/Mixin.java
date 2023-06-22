package kubatech.mixin;

import static kubatech.mixin.TargetedMod.VANILLA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import cpw.mods.fml.relauncher.FMLLaunchHandler;

public enum Mixin {

    // Minecraft
    WorldMixin("minecraft.WorldMixin", VANILLA),
    EntityAccessor("minecraft.EntityAccessor", VANILLA),
    EntityLivingAccessor("minecraft.EntityLivingAccessor", VANILLA),
    EntityLivingBaseAccessor("minecraft.EntityLivingBaseAccessor", VANILLA),
    EntitySlimeAccessor("minecraft.EntitySlimeAccessor", VANILLA),
    RendererLivingEntityAccessor("minecraft.RendererLivingEntityAccessor", VANILLA),
    StringTranslateMixin("minecraft.StringTranslateMixin", VANILLA),
    LanguageRegistryMixin("minecraft.LanguageRegistryMixin", VANILLA),
    LocaleMixin("minecraft.LocaleMixin", Side.CLIENT, VANILLA),

    ;

    public final String mixinClass;
    public final List<TargetedMod> targetedMods;
    private final Side side;

    Mixin(String mixinClass, Side side, TargetedMod... targetedMods) {
        this.mixinClass = mixinClass;
        this.targetedMods = Arrays.asList(targetedMods);
        this.side = side;
    }

    Mixin(String mixinClass, TargetedMod... targetedMods) {
        this.mixinClass = mixinClass;
        this.targetedMods = Arrays.asList(targetedMods);
        this.side = Side.BOTH;
    }

    public boolean shouldLoad(List<TargetedMod> loadedMods) {
        return (side == Side.BOTH || side == Side.SERVER && FMLLaunchHandler.side()
            .isServer()
            || side == Side.CLIENT && FMLLaunchHandler.side()
                .isClient())
            && new HashSet<>(loadedMods).containsAll(targetedMods);
    }

    enum Side {
        BOTH,
        CLIENT,
        SERVER
    }
}
