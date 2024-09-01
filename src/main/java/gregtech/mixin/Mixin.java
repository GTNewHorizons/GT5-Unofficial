package gregtech.mixin;

import static gregtech.mixin.TargetedMod.VANILLA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bartworks.common.configs.ConfigHandler;
import cpw.mods.fml.relauncher.FMLLaunchHandler;

public enum Mixin {

    // Minecraft
    SoundManagerMixin(new Builder("Seeking sound playback")
        .addMixinClasses("minecraft.SoundManagerMixin", "minecraft.SoundManagerInnerMixin")
        .addTargetedMod(VANILLA)
        .setApplyIf(() -> true)
        .setPhase(Phase.EARLY)
        .setSide(Side.CLIENT)),
    WorldMixin(new Builder("Block update detection").addMixinClasses("minecraft.WorldMixin")
        .addTargetedMod(VANILLA)
        .setApplyIf(() -> true)
        .setPhase(Phase.EARLY)
        .setSide(Side.BOTH)),
    StringTranslateMixin(new Builder("Keep track of currently translating mods")
        .addMixinClasses("minecraft.StringTranslateMixin", "minecraft.LanguageRegistryMixin")
        .addTargetedMod(VANILLA)
        .setApplyIf(() -> true)
        .setPhase(Phase.EARLY)
        .setSide(Side.BOTH)),
    LocaleMixin(new Builder("Keep track of currently translating client mods").addMixinClasses("minecraft.LocaleMixin")
        .addTargetedMod(VANILLA)
        .setApplyIf(() -> true)
        .setPhase(Phase.EARLY)
        .setSide(Side.CLIENT)),
    CacheCraftingManagerRecipes(
        new Builder("Cache CraftingManager recipes").addMixinClasses("minecraft.CraftingManagerMixin")
            .addTargetedMod(VANILLA)
            .setApplyIf(() -> ConfigHandler.enabledPatches[3])
            .setPhase(Phase.EARLY)
            .setSide(Side.BOTH)),
    CraftingRecipeAccessorMixin(new Builder("Add accessors to crafting recipe types")
        .addMixinClasses(
            "minecraft.VanillaShapedRecipeMixin",
            "minecraft.VanillaShapelessRecipeMixin",
            "minecraft.ForgeShapedRecipeMixin",
            "minecraft.ForgeShapelessRecipeMixin")
        .addTargetedMod(VANILLA)
        .setApplyIf(() -> true)
        .setPhase(Phase.EARLY)
        .setSide(Side.BOTH)),
    BlockStemMixin(new Builder("Stem Crop Block Accessor").addMixinClasses("minecraft.BlockStemMixin")
        .addTargetedMod(VANILLA)
        .setApplyIf(() -> true)
        .setPhase(Phase.EARLY)
        .setSide(Side.BOTH));

    public static final Logger LOGGER = LogManager.getLogger("GregTech-Mixin");

    private final List<String> mixinClasses;
    private final List<TargetedMod> targetedMods;
    private final List<TargetedMod> excludedMods;
    private final Supplier<Boolean> applyIf;
    private final Phase phase;
    private final Side side;

    Mixin(Builder builder) {
        this.mixinClasses = builder.mixinClasses;
        this.targetedMods = builder.targetedMods;
        this.excludedMods = builder.excludedMods;
        this.applyIf = builder.applyIf;
        this.phase = builder.phase;
        this.side = builder.side;
        if (this.mixinClasses.isEmpty()) {
            throw new RuntimeException("No mixin class specified for Mixin : " + this.name());
        }
        if (this.targetedMods.isEmpty()) {
            throw new RuntimeException("No targeted mods specified for Mixin : " + this.name());
        }
        if (this.applyIf == null) {
            throw new RuntimeException("No ApplyIf function specified for Mixin : " + this.name());
        }
        if (this.phase == null) {
            throw new RuntimeException("No Phase specified for Mixin : " + this.name());
        }
        if (this.side == null) {
            throw new RuntimeException("No Side function specified for Mixin : " + this.name());
        }
    }

    public static List<String> getEarlyMixins(Set<String> loadedCoreMods) {
        final List<String> mixins = new ArrayList<>();
        final List<String> notLoading = new ArrayList<>();
        for (Mixin mixin : Mixin.values()) {
            if (mixin.phase == Phase.EARLY) {
                if (mixin.shouldLoad(loadedCoreMods, Collections.emptySet())) {
                    mixins.addAll(mixin.mixinClasses);
                } else {
                    notLoading.addAll(mixin.mixinClasses);
                }
            }
        }
        LOGGER.info("Not loading the following EARLY mixins: {}", notLoading.toString());
        return mixins;
    }

    public static List<String> getLateMixins(Set<String> loadedMods) {
        // NOTE: Any targetmod here needs a modid, not a coremod id
        final List<String> mixins = new ArrayList<>();
        final List<String> notLoading = new ArrayList<>();
        for (Mixin mixin : Mixin.values()) {
            if (mixin.phase == Phase.LATE) {
                if (mixin.shouldLoad(Collections.emptySet(), loadedMods)) {
                    mixins.addAll(mixin.mixinClasses);
                } else {
                    notLoading.addAll(mixin.mixinClasses);
                }
            }
        }
        LOGGER.info("Not loading the following LATE mixins: {}", notLoading.toString());
        return mixins;
    }

    private boolean shouldLoadSide() {
        return side == Side.BOTH || (side == Side.SERVER && FMLLaunchHandler.side()
            .isServer())
            || (side == Side.CLIENT && FMLLaunchHandler.side()
                .isClient());
    }

    private boolean allModsLoaded(List<TargetedMod> targetedMods, Set<String> loadedCoreMods, Set<String> loadedMods) {
        if (targetedMods.isEmpty()) return false;

        for (TargetedMod target : targetedMods) {
            if (target == TargetedMod.VANILLA) continue;

            // Check coremod first
            if (!loadedCoreMods.isEmpty() && target.coreModClass != null
                && !loadedCoreMods.contains(target.coreModClass)) return false;
            else if (!loadedMods.isEmpty() && target.modId != null && !loadedMods.contains(target.modId)) return false;
        }

        return true;
    }

    private boolean noModsLoaded(List<TargetedMod> targetedMods, Set<String> loadedCoreMods, Set<String> loadedMods) {
        if (targetedMods.isEmpty()) return true;

        for (TargetedMod target : targetedMods) {
            if (target == TargetedMod.VANILLA) continue;

            // Check coremod first
            if (!loadedCoreMods.isEmpty() && target.coreModClass != null
                && loadedCoreMods.contains(target.coreModClass)) return false;
            else if (!loadedMods.isEmpty() && target.modId != null && loadedMods.contains(target.modId)) return false;
        }

        return true;
    }

    private boolean shouldLoad(Set<String> loadedCoreMods, Set<String> loadedMods) {
        return (shouldLoadSide() && applyIf.get()
            && allModsLoaded(targetedMods, loadedCoreMods, loadedMods)
            && noModsLoaded(excludedMods, loadedCoreMods, loadedMods));
    }

    private static class Builder {

        private final String name;
        private final List<String> mixinClasses = new ArrayList<>();
        private final List<TargetedMod> targetedMods = new ArrayList<>();
        private final List<TargetedMod> excludedMods = new ArrayList<>();
        private Supplier<Boolean> applyIf = null;
        private Phase phase = null;
        private Side side = null;

        public Builder(String name) {
            this.name = name;
        }

        public Builder addMixinClasses(String... mixinClasses) {
            this.mixinClasses.addAll(Arrays.asList(mixinClasses));
            return this;
        }

        public Builder setPhase(Phase phase) {
            if (this.phase != null) {
                throw new RuntimeException("Trying to define Phase twice for " + this.name);
            }
            this.phase = phase;
            return this;
        }

        public Builder setSide(Side side) {
            if (this.side != null) {
                throw new RuntimeException("Trying to define Side twice for " + this.name);
            }
            this.side = side;
            return this;
        }

        public Builder setApplyIf(Supplier<Boolean> applyIf) {
            this.applyIf = applyIf;
            return this;
        }

        public Builder addTargetedMod(TargetedMod mod) {
            this.targetedMods.add(mod);
            return this;
        }

        public Builder addExcludedMod(TargetedMod mod) {
            this.excludedMods.add(mod);
            return this;
        }
    }

    private enum Side {
        BOTH,
        CLIENT,
        SERVER
    }

    private enum Phase {
        EARLY,
        LATE,
    }
}
