package gregtech.api.enums;

import static gregtech.api.enums.Mods.GalacticraftAmunRa;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.util.data.BlockSupplier;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.LazyBlock;
import com.gtnewhorizon.gtnhlib.util.data.LazyItem;

import galacticgreg.api.enums.DimensionDef;
import galacticgreg.api.enums.DimensionDef.DimNames;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IStoneCategory;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;

public enum StoneType implements IStoneType {

    // spotless:off
    Stone(new StoneBuilder()
        .setCobble(() -> Blocks.cobblestone, 0)
        .setMainStone(() -> Blocks.stone, 0)
        .setDust(Materials.Stone)),
    Netherrack(new StoneBuilder()
        .setPrefix(OrePrefixes.oreNetherrack)
        .setStoneNoCobble(() -> Blocks.netherrack, 0)
        .setDust(Materials.Netherrack)),
    Endstone(new StoneBuilder()
        .setPrefix(OrePrefixes.oreEndstone)
        .setStoneNoCobble(() -> Blocks.end_stone, 0)
        .setDust(Materials.Endstone)),
    BlackGranite(new StoneBuilder()
        .setPrefix(OrePrefixes.oreBlackgranite)
        .setCobble(() -> GregTechAPI.sBlockGranites, 1)
        .setMainStone(() -> GregTechAPI.sBlockGranites, 0)
        .setDust(Materials.GraniteBlack)),
    RedGranite(new StoneBuilder()
        .setPrefix(OrePrefixes.oreRedgranite)
        .setCobble(() -> GregTechAPI.sBlockGranites, 9)
        .setMainStone(() -> GregTechAPI.sBlockGranites, 8)
        .setDust(Materials.GraniteRed)),
    Marble(new StoneBuilder()
        .setPrefix(OrePrefixes.oreMarble)
        .setCobble(() -> GregTechAPI.sBlockStones, 1)
        .setMainStone(() -> GregTechAPI.sBlockStones, 0)
        .setDust(Materials.Marble)),
    Basalt(new StoneBuilder()
        .setPrefix(OrePrefixes.oreBasalt)
        .setCobble(() -> GregTechAPI.sBlockStones, 9)
        .setMainStone(() -> GregTechAPI.sBlockStones, 8)
        .setDust(Materials.Basalt)),
    Moon(new StoneBuilder()
        .setStoneNoCobble(GalacticraftCore, "tile.moonBlock", 4)
        .setCoremodDust("Moon")),
    Mars(new StoneBuilder()
        .setCobble(GalacticraftMars, "tile.mars", 4)
        .setMainStone(GalacticraftMars, "tile.mars", 9)
        .addOtherStone(GalacticraftMars, "tile.mars", 6)
        .setCoremodDust("Mars")),
    Asteroid(new StoneBuilder()
        .setStoneNoCobble(GalacticraftMars, "tile.asteroidsBlock", 1)
        .setCoremodDust("Asteroids")),
    Phobos(StoneBuilder.galaxySpace("Phobos", 2, 1)),
    Deimos(StoneBuilder.galaxySpace("Deimos", 1)),
    Ceres(StoneBuilder.galaxySpace("Ceres", 1)),
    Io(StoneBuilder.galaxySpace("Io", 2)),
    Europa(StoneBuilder.galaxySpace("Europa", "grunt", 1)),
    Ganymede(StoneBuilder.galaxySpace("Ganymede", 1)),
    Callisto(StoneBuilder.galaxySpace("Callisto", 1)),
    Enceladus(StoneBuilder.galaxySpace("Enceladus", 1, 3)),
    Titan(StoneBuilder.galaxySpace("Titan", 2, 1)),
    Miranda(StoneBuilder.galaxySpace("Miranda", 2, 1)),
    Oberon(StoneBuilder.galaxySpace("Oberon", 2, 1)),
    Proteus(StoneBuilder.galaxySpace("Proteus", 2, 1)),
    Triton(StoneBuilder.galaxySpace("Triton", 2, 1)),
    Pluto(StoneBuilder.galaxySpace("Pluto", 5, 4)),
    Haumea(StoneBuilder.galaxySpace("Haumea", 0)),
    MakeMake(StoneBuilder.galaxySpace("MakeMake", "grunt", 1)),
    Venus(StoneBuilder.galaxySpace("Venus", 1)),
    Mercury(StoneBuilder.galaxySpace("Mercury", 2, 1)),
    AlphaCentauri(new StoneBuilder()
        .setStoneNoCobble(GalaxySpace, "acentauribbsubgrunt", 0)
        .setDust(NewHorizonsCoreMod, "CentauriASurfaceDust")),
    TCetiE(StoneBuilder.galaxySpace("TCetiE", 2, 1)),
    VegaB(StoneBuilder.galaxySpace("VegaB", "subgrunt", 0)),
    BarnardaE(new StoneBuilder()
        .setStoneNoCobble(GalaxySpace, "barnardaEsubgrunt", 0)
        .setCoremodDust("BarnardaE")),
    BarnardaF(new StoneBuilder()
        .setStoneNoCobble(GalaxySpace, "barnardaFsubgrunt", 0)
        .setCoremodDust("BarnardaF")),
    Horus(new StoneBuilder()
        .setStoneNoCobble(() -> Blocks.obsidian, 0)
        .addOtherStone(GalacticraftAmunRa, "tile.baseFalling", 1)
        .addAllowedDimensions(DimNames.HORUS)
        .setDust(Materials.Obsidian)),
    AnubisAndMaahes(new StoneBuilder()
        .setCobble(GalacticraftAmunRa, "tile.baseBlockRock", 0)
        .setMainStone(GalacticraftAmunRa, "tile.baseBlockRock", 1)
        .setDust(Materials.Basalt)),
    PackedIce(new StoneBuilder()
        .setStoneNoCobble(() -> Blocks.packed_ice, 0)
        .setDust(Materials.Ice)
        .setCategory(StoneCategory.Ice)
        .addAllowedDimensions(DimNames.ASTEROIDS, DimNames.KUIPERBELT, DimNames.MEHENBELT)),
    SethIce(new StoneBuilder()
        .setStoneNoCobble(() -> Blocks.packed_ice, 0)
        .addOtherStone(Mods.Minecraft, () -> Blocks.ice, 0)
        .setDust(Materials.Ice)
        .setCategory(StoneCategory.Stone)
        .addAllowedDimensions(DimNames.SETH)),
    SethClay(new StoneBuilder()
        .setStoneNoCobble(() -> Blocks.hardened_clay, WILDCARD)
        .addOtherStone(Mods.Minecraft, () -> Blocks.clay, 0)
        .setDust(Materials.Clay)
        .setCategory(StoneCategory.Stone)
        .addAllowedDimensions(DimNames.SETH)),

    Deepslate(new StoneBuilder()
        .setStoneNoCobble(Mods.EtFuturumRequiem, "deepslate", WILDCARD)
        .setDust(NewHorizonsCoreMod, "DeepslateDust")
        .setCategory(StoneCategory.Stone)
        .addAllowedDimensions(DimNames.OW)),

    Tuff(new StoneBuilder()
        .setStoneNoCobble(Mods.EtFuturumRequiem, "tuff", 0)
        .setDust(NewHorizonsCoreMod, "TuffDust")
        .setCategory(StoneCategory.Stone)
        .addAllowedDimensions(DimNames.OW)),

    BlueIce(new StoneBuilder()
        .setStoneNoCobble(Mods.EtFuturumRequiem, "blue_ice", 0)
        .setDust(Materials.Ice)
        .setCategory(StoneCategory.Ice)
        .addAllowedDimensions(DimNames.ASTEROIDS, DimNames.KUIPERBELT, DimNames.MEHENBELT)),

    ;
    // spotless:on

    public static final List<StoneType> STONE_TYPES = ImmutableList.copyOf(values());
    public static final List<StoneType> VISUAL_STONE_TYPES = ImmutableList.copyOf(
        Arrays.stream(values())
            .filter(s -> s.builder.enabled && !s.isExtraneous())
            .toArray(StoneType[]::new));

    public static final List<IStoneType> STONE_ONLY = ImmutableList.of(StoneType.Stone);
    public static final List<IStoneType> STONES = ImmutableList.copyOf(
        StoneType.STONE_TYPES.stream()
            .filter(s -> s.getCategory() == StoneCategory.Stone)
            .toArray(StoneType[]::new));
    public static final List<IStoneType> ICES = ImmutableList.copyOf(
        StoneType.STONE_TYPES.stream()
            .filter(s -> s.getCategory() == StoneCategory.Ice)
            .toArray(StoneType[]::new));

    private final StoneBuilder builder;

    StoneType(StoneBuilder builder) {
        this.builder = builder;
    }

    public static void init() {
        for (StoneType stoneType : STONE_TYPES) {
            StoneBuilder builder = stoneType.builder;

            builder.cobble.get();
            builder.mainStone.get();

            if (builder.otherStones != null) builder.otherStones.forEach(LazyBlock::get);

            builder.pureDust.get();

            if (builder.impureDust == null || builder.impureDust.get() == null) {
                builder.impureDust = builder.pureDust;
            }
        }
    }

    @Override
    public ItemStack getDust(boolean pure, int amount) {
        if (pure) {
            return builder.pureDust.toStack(amount);
        } else {
            return builder.impureDust.toStack(amount);
        }
    }

    @Override
    public OrePrefixes getPrefix() {
        return builder.oreBlockPrefix;
    }

    @Override
    public IStoneCategory getCategory() {
        return builder.category;
    }

    @Override
    public ImmutableBlockMeta getCobblestone() {
        return builder.cobble;
    }

    @Override
    public ImmutableBlockMeta getStone() {
        return builder.mainStone;
    }

    private volatile ITexture[] textures;

    private void populateTextures() {
        // This might be called in a render thread in Angelica, but there shouldn't be any side effects from that as
        // long as the textures field is always valid and never partially initialized.
        ITexture[] texturesTemp = new ITexture[6];

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            IIconContainer container = switch (this) {
                case BlackGranite -> BlockIcons.GRANITE_BLACK_STONE;
                case RedGranite -> BlockIcons.GRANITE_RED_STONE;
                case Marble -> BlockIcons.MARBLE_STONE;
                case Basalt -> BlockIcons.BASALT_STONE;
                default -> new IIconContainer() {

                    @Override
                    public IIcon getIcon() {
                        return StoneType.this.getIcon(side.ordinal());
                    }

                    @Override
                    public IIcon getOverlayIcon() {
                        return null;
                    }

                    @Override
                    public ResourceLocation getTextureFile() {
                        return TextureMap.locationBlocksTexture;
                    }
                };
            };

            texturesTemp[side.ordinal()] = TextureFactory.builder()
                .addIcon(container)
                .stdOrient()
                .build();
        }

        // Atomically initialize the field to prevent race conditions
        textures = texturesTemp;
    }

    @Override
    public ITexture getTexture(int side) {
        if (textures == null) populateTextures();

        // Multithreaded reads here are fine because the field will always be valid by this point, regardless of the
        // thread
        return textures[side];
    }

    @Override
    public IIcon getIcon(int side) {
        if (!builder.enabled) {
            return Blocks.stone.getIcon(side, 0);
        }

        return switch (this) {
            case Stone -> Blocks.stone.getIcon(side, 0);
            case Netherrack -> Blocks.netherrack.getIcon(side, 0);
            case Endstone -> Blocks.end_stone.getIcon(side, 0);
            case BlackGranite -> BlockIcons.GRANITE_BLACK_STONE.getIcon();
            case RedGranite -> BlockIcons.GRANITE_RED_STONE.getIcon();
            case Marble -> BlockIcons.MARBLE_STONE.getIcon();
            case Basalt -> BlockIcons.BASALT_STONE.getIcon();
            default -> builder.mainStone.getBlock()
                .getIcon(side, builder.mainStone.getBlockMeta());
        };
    }

    @Override
    public boolean isRich() {
        return switch (this) {
            case Netherrack -> GTMod.proxy.mNetherOreYieldMultiplier;
            case Endstone -> GTMod.proxy.mEndOreYieldMultiplier;
            default -> false;
        };
    }

    @Override
    public boolean isDimensionSpecific() {
        return switch (this) {
            case Stone -> false;
            case BlackGranite -> false;
            case RedGranite -> false;
            case Marble -> false;
            case Basalt -> false;
            default -> true;
        };
    }

    @Override
    public boolean isExtraneous() {
        return switch (this) {
            case Stone -> false;
            case Netherrack -> false;
            case Endstone -> false;
            case PackedIce -> false;
            default -> true;
        };
    }

    @Override
    public boolean isEnabled() {
        return builder.enabled;
    }

    @Override
    public boolean contains(Block block, int meta) {
        if (builder.mainStone.matches(block, meta)) return true;

        if (builder.otherStones != null) {
            for (ImmutableBlockMeta other : builder.otherStones) {
                if (other.matches(block, meta)) return true;
            }
        }

        return false;
    }

    @Override
    public boolean canGenerateInWorld(World world) {
        return builder.allowedDimensions == null
            || builder.allowedDimensions.contains(DimensionDef.getDimensionName(world));
    }

    @Nullable
    public static StoneType findStoneType(World world, int x, int y, int z) {
        return findStoneType(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }

    @Nullable
    public static StoneType findStoneType(Block block, int meta) {
        if (block == Blocks.air) return null;

        for (int i = 0, stoneTypesSize = STONE_TYPES.size(); i < stoneTypesSize; i++) {
            StoneType stoneType = STONE_TYPES.get(i);
            if (stoneType.builder.enabled && stoneType.contains(block, meta)) {
                return stoneType;
            }
        }

        return null;
    }

    private static class StoneBuilder {

        private static final LazyBlock AIR = new LazyBlock(Mods.Minecraft, "air");

        public boolean enabled = true;

        public LazyBlock cobble = AIR;
        public LazyBlock mainStone = AIR;
        public List<LazyBlock> otherStones;

        public OrePrefixes oreBlockPrefix = OrePrefixes.ore;

        public LazyItem pureDust = new LazyItem(
            Mods.GregTech,
            () -> GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1));
        public LazyItem impureDust = new LazyItem(
            Mods.GregTech,
            () -> GTOreDictUnificator.get(OrePrefixes.dustImpure, Materials.Stone, 1));

        public StoneCategory category = StoneCategory.Stone;
        public HashSet<String> allowedDimensions = null;

        public static StoneBuilder galaxySpace(String name, int mainStone, int... extraStones) {
            return galaxySpace(name, "blocks", mainStone, extraStones);
        }

        public static StoneBuilder galaxySpace(String name, String suffix, int mainStone, int... extraStones) {
            StoneBuilder builder = new StoneBuilder()
                .setStoneNoCobble(GalaxySpace, name.toLowerCase() + suffix, mainStone)
                .setCoremodDust(name);

            for (int extra : extraStones) {
                builder.addOtherStone(GalaxySpace, name.toLowerCase() + suffix, extra);
            }

            return builder;
        }

        public StoneBuilder setCobble(BlockSupplier block, int meta) {
            Objects.requireNonNull(block);

            cobble = new LazyBlock(Mods.Minecraft, block, meta);

            return this;
        }

        public StoneBuilder setCobble(Mods owner, String blockName, int meta) {
            enabled &= owner.isModLoaded();

            if (enabled) {
                cobble = new LazyBlock(owner, blockName, meta);
            }

            return this;
        }

        public StoneBuilder setStoneNoCobble(Mods owner, String blockName, int meta) {
            enabled &= owner.isModLoaded();

            if (enabled) {
                cobble = mainStone = new LazyBlock(owner, blockName, meta);
            }

            return this;
        }

        public StoneBuilder setStoneNoCobble(BlockSupplier block, int meta) {
            cobble = mainStone = new LazyBlock(Mods.Minecraft, block, meta);

            return this;
        }

        public StoneBuilder setMainStone(Mods owner, String blockName, int meta) {
            enabled &= owner.isModLoaded();

            if (enabled) {
                mainStone = new LazyBlock(owner, blockName, meta);
            }

            return this;
        }

        public StoneBuilder setMainStone(BlockSupplier block, int meta) {
            mainStone = new LazyBlock(Mods.Minecraft, block, meta);

            return this;
        }

        public StoneBuilder addOtherStone(Mods owner, String blockName, int meta) {
            if (owner.isModLoaded()) {
                if (otherStones == null) otherStones = new ArrayList<>();
                otherStones.add(new LazyBlock(owner, blockName, meta));
            }

            return this;
        }

        public StoneBuilder addOtherStone(Mods owner, BlockSupplier block, int meta) {
            if (owner.isModLoaded()) {
                if (otherStones == null) otherStones = new ArrayList<>();
                otherStones.add(new LazyBlock(owner, block, meta));
            }

            return this;
        }

        public StoneBuilder setCoremodDust(String name) {
            if (NewHorizonsCoreMod.isModLoaded()) {
                pureDust = impureDust = new LazyItem(NewHorizonsCoreMod, name + "StoneDust");
            }

            return this;
        }

        public StoneBuilder setDust(Mods mod, String name) {
            if (mod.isModLoaded()) {
                pureDust = impureDust = new LazyItem(mod, name);
            }

            return this;
        }

        public StoneBuilder setDust(Materials mat) {
            pureDust = new LazyItem(Mods.GregTech, () -> mat.getDust(1));
            impureDust = new LazyItem(Mods.GregTech, () -> GTOreDictUnificator.get(OrePrefixes.dustImpure, mat, 1));

            return this;
        }

        public StoneBuilder setPrefix(OrePrefixes prefix) {
            this.oreBlockPrefix = prefix;

            return this;
        }

        public StoneBuilder setCategory(StoneCategory cat) {
            this.category = cat;

            return this;
        }

        public StoneBuilder addAllowedDimensions(String... dimNames) {
            if (allowedDimensions == null) allowedDimensions = new HashSet<>();

            allowedDimensions.addAll(Arrays.asList(dimNames));

            return this;
        }
    }
}
