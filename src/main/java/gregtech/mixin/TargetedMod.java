package gregtech.mixin;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhmixins.builders.ITargetMod;
import com.gtnewhorizon.gtnhmixins.builders.TargetModBuilder;

public enum TargetedMod implements ITargetMod {

    ADVANCED_SOLAR_PANELS(null, "AdvancedSolarPanel"),
    ANGELICA("com.gtnewhorizons.angelica.loading.AngelicaTweaker", "angelica"),
    BIOMESOPLENTY(null, "BiomesOPlenty"),
    EFR(null, "etfuturum"),
    GALACTICRAFT_CORE("micdoodle8.mods.galacticraft.core.asm.GCLoadingPlugin", "GalacticraftCore"),
    GALAXYSPACE("galaxyspace.GalaxySpace", "GalaxySpace"),
    GT6("gregtech.asm.GT_ASM", "gregapi"),
    HEE("chylex.hee.HEECore", "HardcoreEnderExpansion"),
    IC2("ic2.core.coremod.IC2core", "IC2"),
    NATURA(null, "Natura"),
    OPTIFINE("optifine.OptiFineForgeTweaker", "Optifine"),
    RAILCRAFT(null, "Railcraft"),
    THAUMCRAFT(null, "Thaumcraft"),
    TINKERSCONSTRUCT(null, "TConstruct");

    private final TargetModBuilder builder;

    TargetedMod(String coreModClass, String modId) {
        this.builder = new TargetModBuilder().setCoreModClass(coreModClass)
            .setModId(modId);
    }

    @NotNull
    @Override
    public TargetModBuilder getBuilder() {
        return builder;
    }
}
