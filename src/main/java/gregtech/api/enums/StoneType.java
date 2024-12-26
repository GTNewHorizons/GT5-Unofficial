package gregtech.api.enums;

import static gregtech.api.enums.Mods.GalacticraftAmunRa;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.registry.GameRegistry;
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
import gregtech.api.util.GTUtility;
import gregtech.api.util.Lazy;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;

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
    AlphaCentauri(new StoneBuilder()
        .setStoneNoCobble(GalaxySpace, "acentauribbsubgrunt", 1)
        .setCoremodDust("CentauriA")),
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
        .addOtherStone(GalacticraftAmunRa, "tile.baseFalling", 0)
        .addAllowedDimensions(DimNames.HORUS)
        .setDust(Materials.Obsidian)),
    Anubis(new StoneBuilder()
        .setCobble(GalacticraftAmunRa, "tile.baseBlockRock", 0)
        .setMainStone(GalacticraftAmunRa, "tile.baseBlockRock", 1)
        .setDust(Materials.Basalt)),
    PackedIce(new StoneBuilder()
        .setStoneNoCobble(() -> Blocks.packed_ice, 0)
        .setDust(Materials.Ice)
        .setCategory(StoneCategory.Ice)),

    ;
    // spotless:on

    public static final ImmutableList<StoneType> STONE_TYPES = ImmutableList.copyOf(values());
    public static final ImmutableList<StoneType> VISUAL_STONE_TYPES = ImmutableList.copyOf(
        Arrays.stream(values())
            .filter(s -> s.builder.enabled && !s.isExtraneous())
            .toArray(StoneType[]::new));

    public static final ImmutableList<IStoneType> STONE_ONLY = ImmutableList.of(StoneType.Stone);
    public static final ImmutableList<IStoneType> STONES = ImmutableList.copyOf(
        StoneType.STONE_TYPES.stream()
            .filter(s -> s.getCategory() == StoneCategory.Stone)
            .toArray(StoneType[]::new));
    public static final ImmutableList<IStoneType> ICES = ImmutableList.copyOf(
        StoneType.STONE_TYPES.stream()
            .filter(s -> s.getCategory() == StoneCategory.Ice)
            .toArray(StoneType[]::new));

    private final StoneBuilder builder;

    private StoneType(StoneBuilder builder) {
        this.builder = builder;
    }

    public static void init() {
        for (StoneType stoneType : STONE_TYPES) {
            StoneBuilder builder = stoneType.builder;

            builder.cobble.get();
            builder.mainStone.get();

            if (builder.otherStones != null) builder.otherStones.forEach(Lazy::get);

            ItemStack pure = builder.pureDust.get();
            if (builder.impureDust.get() == null) {
                builder.impureDust.set(pure.copy());
            }
        }
    }

    @Override
    public ItemStack getDust(boolean pure, int amount) {
        return GTUtility.copyAmount(amount, pure ? builder.pureDust.get() : builder.impureDust.get());
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
    public ObjectIntPair<Block> getCobblestone() {
        return builder.cobble.get()
            .toPair();
    }

    @Override
    public ObjectIntPair<Block> getStone() {
        return builder.mainStone.get()
            .toPair();
    }

    @Override
    public ITexture getTexture(int side) {
        IIconContainer container = switch (this) {
            case BlackGranite -> BlockIcons.GRANITE_BLACK_STONE;
            case RedGranite -> BlockIcons.GRANITE_RED_STONE;
            case Marble -> BlockIcons.MARBLE_STONE;
            case Basalt -> BlockIcons.BASALT_STONE;
            default -> new IIconContainer() {

                @Override
                public IIcon getIcon() {
                    return StoneType.this.getIcon(side);
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

        return TextureFactory.builder()
            .addIcon(container)
            .stdOrient()
            .build();
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
            default -> {
                BlockMeta main = builder.mainStone.get();

                yield main.block.getIcon(side, main.meta);
            }
        };
    }

    @Override
    public boolean isRich() {
        return switch (this) {
            case Netherrack -> GTMod.gregtechproxy.mNetherOreYieldMultiplier;
            case Endstone -> GTMod.gregtechproxy.mEndOreYieldMultiplier;
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
        return this.ordinal() > Endstone.ordinal() && this != PackedIce;
    }

    @Override
    public boolean isEnabled() {
        return builder.enabled;
    }

    @Override
    public boolean contains(Block block, int meta) {
        BlockMeta main = builder.mainStone.get();

        if (block == main.block && meta == main.meta) return true;

        if (builder.otherStones != null) {
            for (Lazy<BlockMeta> other : builder.otherStones) {
                BlockMeta other2 = other.get();

                if (block == other2.block && meta == other2.meta) return true;
            }
        }

        return false;
    }

    @Override
    public boolean canGenerateInWorld(World world) {
        return builder.allowedDimensions == null
            || builder.allowedDimensions.contains(world.provider.getDimensionName());
    }

    public static StoneType findStoneType(World world, int x, int y, int z) {
        return findStoneType(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }

    public static StoneType findStoneType(Block block, int meta) {
        for (StoneType stoneType : STONE_TYPES) {
            if (stoneType.builder.enabled && stoneType.contains(block, meta)) {
                return stoneType;
            }
        }

        return null;
    }

    private static class BlockMeta {

        public Block block;
        public int meta;

        public BlockMeta(Mods mod, String blockName, int meta) {
            this.block = GameRegistry.findBlock(mod.ID, blockName);
            this.meta = meta;

            Objects.requireNonNull(block, "Could not find block " + mod.ID + ":" + blockName);
        }

        public BlockMeta(Block block, int meta) {
            this.block = block;
            this.meta = meta;
        }

        public ObjectIntPair<Block> toPair() {
            return ObjectIntPair.of(block, meta);
        }
    }

    private static class StoneBuilder {

        public boolean enabled = true;
        public Lazy<BlockMeta> cobble = new Lazy<>(() -> new BlockMeta(Blocks.air, 0));
        public Lazy<BlockMeta> mainStone = new Lazy<>(() -> new BlockMeta(Blocks.air, 0));
        public List<Lazy<BlockMeta>> otherStones;
        public OrePrefixes oreBlockPrefix = OrePrefixes.ore;
        public Lazy<ItemStack> pureDust = new Lazy<>(
            () -> GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1));
        public Lazy<ItemStack> impureDust = new Lazy<>(
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

        public StoneBuilder setCobble(Supplier<Block> block, int meta) {
            Objects.requireNonNull(block);

            cobble = new Lazy<>(() -> new BlockMeta(block.get(), meta));

            return this;
        }

        public StoneBuilder setCobble(Mods owner, String blockName, int meta) {
            enabled &= owner.isModLoaded();

            if (enabled) {
                cobble = new Lazy<>(() -> new BlockMeta(owner, blockName, meta));
            }

            return this;
        }

        public StoneBuilder setStoneNoCobble(Mods owner, String blockName, int meta) {
            enabled &= owner.isModLoaded();

            if (enabled) {
                cobble = mainStone = new Lazy<>(() -> new BlockMeta(owner, blockName, meta));
            }

            return this;
        }

        public StoneBuilder setStoneNoCobble(Supplier<Block> block, int meta) {
            cobble = mainStone = new Lazy<>(() -> new BlockMeta(block.get(), meta));

            return this;
        }

        public StoneBuilder setMainStone(Mods owner, String blockName, int meta) {
            enabled &= owner.isModLoaded();

            if (enabled) {
                mainStone = new Lazy<>(() -> new BlockMeta(owner, blockName, meta));
            }

            return this;
        }

        public StoneBuilder setMainStone(Supplier<Block> block, int meta) {
            mainStone = new Lazy<>(() -> new BlockMeta(block.get(), meta));

            return this;
        }

        public StoneBuilder addOtherStone(Mods owner, String blockName, int meta) {
            if (owner.isModLoaded()) {
                if (otherStones == null) otherStones = new ArrayList<>();
                otherStones.add(new Lazy<>(() -> new BlockMeta(owner, blockName, meta)));
            }

            return this;
        }

        public StoneBuilder setCoremodDust(String name) {
            if (NewHorizonsCoreMod.isModLoaded()) {
                pureDust = impureDust = new Lazy<>(() -> {
                    Item dust = GameRegistry.findItem(NewHorizonsCoreMod.ID, "item." + name + "StoneDust");
                    Objects.requireNonNull(dust, "Could not find " + "item." + name + "StoneDust");
                    return new ItemStack(dust, 1);
                });
            }

            return this;
        }

        public StoneBuilder setDust(Materials mat) {
            pureDust = new Lazy<>(() -> mat.getDust(1));
            impureDust = new Lazy<>(() -> GTOreDictUnificator.get(OrePrefixes.dustImpure, mat, 1));

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
