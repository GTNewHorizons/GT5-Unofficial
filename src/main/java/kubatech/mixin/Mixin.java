package kubatech.mixin;

import static kubatech.mixin.TargetedMod.*;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public enum Mixin {
    // Minecraft
    EnchantmentHelperMixin("minecraft.EnchantmentHelperMixin", VANILLA);

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
        return (side == Side.BOTH
                        || side == Side.SERVER && FMLLaunchHandler.side().isServer()
                        || side == Side.CLIENT && FMLLaunchHandler.side().isClient())
                && new HashSet<>(loadedMods).containsAll(targetedMods);
    }
}

enum Side {
    BOTH,
    CLIENT,
    SERVER
}
