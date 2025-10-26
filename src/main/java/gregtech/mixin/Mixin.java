package gregtech.mixin;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

import gregtech.common.config.Gregtech;
import gregtech.common.pollution.PollutionConfig;

public enum Mixin implements IMixins {

    // spotless:off
    // Minecraft
    GregtechCapes(new MixinBuilder()
        .addClientMixins("minecraft.AbstractClientPlayerMixin")
        .setPhase(Phase.EARLY)),
    SoundManagerMixin(new MixinBuilder("Seeking sound playback")
        .addClientMixins(
            "minecraft.SoundManagerMixin",
            "minecraft.SoundManagerInnerMixin")
        .setPhase(Phase.EARLY)),
    WorldMixin(new MixinBuilder("Block update detection")
        .addCommonMixins("minecraft.WorldMixin")
        .setPhase(Phase.EARLY)),
    StringTranslateMixin(new MixinBuilder("Keep track of currently translating mods")
        .addCommonMixins(
            "minecraft.StringTranslateMixin",
            "minecraft.LanguageRegistryMixin")
        .setPhase(Phase.EARLY)),
    LocaleMixin(new MixinBuilder("Keep track of currently translating client mods")
        .addClientMixins("minecraft.LocaleMixin")
        .setPhase(Phase.EARLY)),
    VANILLA_ACCESSORS(new MixinBuilder()
        .addCommonMixins(
            "minecraft.accessors.BlockStemMixin",
            "minecraft.accessors.ChunkCacheMixin",
            "minecraft.accessors.VanillaShapedRecipeMixin",
            "minecraft.accessors.VanillaShapelessRecipeMixin",
            "minecraft.accessors.ForgeShapedRecipeMixin",
            "minecraft.accessors.ForgeShapelessRecipeMixin",
            "minecraft.accessors.ItemArmorMixin",
            "minecraft.accessors.PotionMixin",
            "minecraft.accessors.EntityPlayerMPMixin",
            "minecraft.accessors.WeightedRandomFishableMixin",
            "minecraft.accessors.EntityMixin",
            "minecraft.accessors.LanguageRegistryMixin",
            "minecraft.accessors.EntityItemMixin")
        .addClientMixins(
            "minecraft.accessors.GuiTextFieldMixin",
            "minecraft.accessors.TessellatorMixin")
        .setPhase(Phase.EARLY)),
    RemoveItemStack(new MixinBuilder()
        .addCommonMixins(
            "minecraft.ItemStackMixin_MetaItemRemover"
        )
        .setPhase(Phase.EARLY)
    ),
    ItemMixinCoverFix(new MixinBuilder("Allow cover items to bypass sneak checks")
        .addCommonMixins("minecraft.ItemMixin")
        .setPhase(Phase.EARLY)),
    SplitPhysicRenderingPipeBoundingBox(
        new MixinBuilder("Allows pipes to have different bounding box for the rendering, physics and player pickblock")
            .addClientMixins("minecraft.MinecraftMixin_MouseOver")
            .setPhase(Phase.EARLY)),
    VanillaToolChanges(
        new MixinBuilder("Changes wooden tools to be a little faster")
            .addCommonMixins("minecraft.ItemToolMaterialMixin")
            .setApplyIf(() -> Gregtech.general.changedWoodenVanillaTools)
            .setPhase(Phase.EARLY)),

    GTWorldgenSortingFix(new MixinBuilder("Forces GTWorldgenerator to the end of the world gen list")
        .addCommonMixins("forge.GameRegistryMixin")
        .setPhase(Phase.EARLY)),

    HEEAccessors(new MixinBuilder("Various accessors for Hardcore Ender Expansion")
        .addCommonMixins("hee.ChunkProviderHardcoreEndMixin", "hee.MapGenIslandMixin")
        .addRequiredMod(TargetedMod.HEE)
        .setPhase(Phase.LATE)),

    ForgeHooksMixin(new MixinBuilder("Adds missing hooks in ForgeHooks")
        .addCommonMixins("forge.ForgeHooksMixin")
        .setPhase(Phase.EARLY)),
    IC2_MACHINE_WRENCHING(new MixinBuilder("Changes the behavior of the wrenching mechanic for IC2 machines")
        .addCommonMixins(
            "ic2.MixinDamageDropped",
            "ic2.MixinHarvestTool",
            "ic2.MixinItemDropped")
        .addRequiredMod(TargetedMod.IC2)
        .setPhase(Phase.LATE)),
    IC2_REINFORCED_GLASS_SILK(
        new MixinBuilder("Lets Reinforced Glass be harvested by silk touch")
            .addCommonMixins("ic2.MixinIc2ReinforcedGlass")
            .addRequiredMod(TargetedMod.IC2)
            .setPhase(Phase.LATE)),
    IC2_REMOVE_FISSION_FUELS(new MixinBuilder()
        .addCommonMixins("ic2.MixinIc2FissionFuelRemoval")
        .addRequiredMod(TargetedMod.IC2)
        .setPhase(Phase.LATE)),
    // Hazmat armors
    IC2_HAZMAT(new MixinBuilder()
        .setPhase(Phase.LATE)
        .addCommonMixins(
            "ic2.MixinIc2Hazmat",
            "ic2.MixinIc2Nano",
            "ic2.MixinIc2Quantum")
        .addRequiredMod(TargetedMod.IC2)
        .addExcludedMod(TargetedMod.GT6)),
    ADV_SOLAR_HAZMAT(new MixinBuilder("Applies Hazmat API to Advanced Solar helmets")
        .setPhase(Phase.LATE)
        .addCommonMixins("advanced_solar_panels.MixinAdvancedSolarHelmet")
        .addRequiredMod(TargetedMod.ADVANCED_SOLAR_PANELS)),

    VANILLA_TRADING(new MixinBuilder()
        .setPhase(Phase.EARLY)
        .addCommonMixins("minecraft.VanillaTradingMixin")),

    // Pollution
    POLLUTION_RENDER_BLOCKS(new MixinBuilder()
        .addClientMixins("minecraft.pollution.MixinRenderBlocks_PollutionWithoutOptifine")
        .addExcludedMod(TargetedMod.OPTIFINE)
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.pollutionBlockRecolor)
        .setPhase(Phase.EARLY)),
    POLLUTION_RENDER_BLOCKS_OPTIFINE(new MixinBuilder()
        .addClientMixins("minecraft.pollution.MixinRenderBlocks_PollutionWithOptifine")
        .addRequiredMod(TargetedMod.OPTIFINE)
        .addExcludedMod(TargetedMod.ANGELICA)
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.pollutionBlockRecolor)
        .setPhase(Phase.EARLY)),
    POLLUTION_RENDER_BLOCKS_BOP(new MixinBuilder()
        .addClientMixins("biomesoplenty.MixinFoliageRendererPollution")
        .addRequiredMod(TargetedMod.BIOMESOPLENTY)
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.pollutionBlockRecolor)
        .setPhase(Phase.LATE)),
    POLLUTION_MINECRAFT_FURNACE(new MixinBuilder()
        .setPhase(Phase.EARLY)
        .addCommonMixins("minecraft.pollution.MixinTileEntityFurnacePollution")
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.furnacesPollute)),
    POLLUTION_MINECRAFT_EXPLOSION(new MixinBuilder()
        .setPhase(Phase.EARLY)
        .addCommonMixins("minecraft.pollution.MixinExplosionPollution")
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.explosionPollutionAmount != 0F)),
    POLLUTION_IC2_IRON_FURNACE(
        new MixinBuilder()
            .addCommonMixins("ic2.MixinIC2IronFurnacePollution")
            .setPhase(Phase.LATE)
            .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.furnacesPollute)
            .addRequiredMod(TargetedMod.IC2)),
    POLLUTION_THAUMCRAFT_ALCHEMICAL_FURNACE(new MixinBuilder("Thaumcraft Alchemical Construct Pollutes")
        .addCommonMixins("thaumcraft.MixinThaumcraftAlchemyFurnacePollution")
        .setPhase(Phase.LATE)
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.furnacesPollute)
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    POLLUTION_TICON_SLAB_FURNACE(new MixinBuilder("Tinker's Contruct Slab Furnace Pollutes")
        .addCommonMixins("tinkersconstruct.MixinFurnaceLogicPollution")
        .setPhase(Phase.LATE)
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.furnacesPollute)
        .addRequiredMod(TargetedMod.TINKERSCONSTRUCT)),
    POLLUTION_EFR_FURNACE(new MixinBuilder("Et Futurum Requiem Blast Furnace and Smoker Pollutes")
        .addCommonMixins("efr.MixinTileEntityBlastFurnacePollution", "efr.MixinTileEntitySmokerPollution")
        .setPhase(Phase.LATE)
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.furnacesPollute)
        .addRequiredMod(TargetedMod.EFR)),
    POLLUTION_NATURA_NETHER_FURNACE(new MixinBuilder("Natura Nether Furnace Pollutes")
        .addCommonMixins("natura.MixinNetherrackFurnacePollution")
        .setPhase(Phase.LATE)
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.furnacesPollute)
        .addRequiredMod(TargetedMod.NATURA)),
    POLLUTION_RAILCRAFT(new MixinBuilder("Make Railcraft Pollute")
        .addCommonMixins(
            "railcraft.MixinRailcraftBoilerPollution",
            "railcraft.MixinRailcraftCokeOvenPollution",
            "railcraft.MixinRailcraftTunnelBorePollution")
        .setPhase(Phase.LATE)
        .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.railcraftPollutes)
        .addRequiredMod(TargetedMod.RAILCRAFT)),
    POLLUTION_ROCKET(
        new MixinBuilder()
            .addCommonMixins("galacticraftcore.MixinGalacticraftRocketPollution")
            .setPhase(Phase.LATE)
            .setApplyIf(() -> PollutionConfig.pollution && PollutionConfig.rocketsPollute)
            .addRequiredMod(TargetedMod.GALACTICRAFT_CORE));
    // spotless:on

    private final MixinBuilder builder;

    Mixin(MixinBuilder builder) {
        this.builder = builder;
    }

    @NotNull
    @Override
    public MixinBuilder getBuilder() {
        return this.builder;
    }
}
