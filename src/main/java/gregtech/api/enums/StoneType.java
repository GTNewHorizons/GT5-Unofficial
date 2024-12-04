package gregtech.api.enums;

import com.google.common.collect.ImmutableList;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public enum StoneType implements IStoneType {
    Stone(OrePrefixes.ore, Materials.Stone) {
        @Override
        public boolean contains(Block block, int meta) {
            return block == Blocks.stone;
        }
    },
    Netherrack(OrePrefixes.oreNetherrack, Materials.Netherrack) {
        @Override
        public boolean contains(Block block, int meta) {
            return block == Blocks.netherrack;
        }
    },
    Endstone(OrePrefixes.oreEndstone, Materials.Endstone) {
        @Override
        public boolean contains(Block block, int meta) {
            return block == Blocks.end_stone;
        }
    },
    BlackGranite(OrePrefixes.oreBlackgranite, Materials.GraniteBlack) {
        @Override
        public boolean contains(Block block, int meta) {
            return block == GregTechAPI.sBlockGranites && meta < 8;
        }
    },
    RedGranite(OrePrefixes.oreRedgranite, Materials.GraniteRed) {
        @Override
        public boolean contains(Block block, int meta) {
            return block == GregTechAPI.sBlockGranites && meta >= 8;
        }
    },
    Marble(OrePrefixes.oreMarble, Materials.Marble) {
        @Override
        public boolean contains(Block block, int meta) {
            return block == GregTechAPI.sBlockStones && meta < 8;
        }
    },
    Basalt(OrePrefixes.oreBasalt, Materials.Basalt) {
        @Override
        public boolean contains(Block block, int meta) {
            return block == GregTechAPI.sBlockStones && meta >= 8;
        }
    };

    public static final ImmutableList<StoneType> STONE_TYPES = ImmutableList.copyOf(values());

    public final OrePrefixes prefix;
    public final Materials material;

    private StoneType(OrePrefixes prefix, Materials material) {
        this.prefix = prefix;
        this.material = material;
    }

    @Override
    public Materials getMaterial() {
        return material;
    }

    @Override
    public OrePrefixes getPrefix() {
        return prefix;
    }

    @Override
    public ITexture getTexture() {
        return switch (this) {
            case Stone -> TextureFactory.of(Blocks.stone);
            case Netherrack -> TextureFactory.of(Blocks.netherrack);
            case Endstone -> TextureFactory.of(Blocks.end_stone);
            case BlackGranite -> TextureFactory.builder().addIcon(BlockIcons.GRANITE_BLACK_STONE).stdOrient().build();
            case RedGranite -> TextureFactory.builder().addIcon(BlockIcons.GRANITE_RED_STONE).stdOrient().build();
            case Marble -> TextureFactory.builder().addIcon(BlockIcons.MARBLE_STONE).stdOrient().build();
            case Basalt -> TextureFactory.builder().addIcon(BlockIcons.BASALT_STONE).stdOrient().build();
        };
    }

    public static StoneType findStoneType(Block block, int meta) {
        for (StoneType stoneType : STONE_TYPES) {
            if (stoneType.contains(block, meta)) {
                return stoneType;
            }
        }

        return null;
    }
}
