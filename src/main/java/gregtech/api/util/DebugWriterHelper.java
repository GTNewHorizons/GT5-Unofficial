package gregtech.api.util;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.iterate;
import static gregtech.GTMod.GT_FML_LOGGER;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.tileentities.debug.MTEDebugStructureWriter;

public class DebugWriterHelper {

    private static final String NICE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz=|!@#$%&()[]{};:<>/?_,.*^'`";
    private static final List<BlockAssociation> blockAssociations = new ArrayList<>();
    private static final List<TileEntityAssociation> tileEntityAssociations = new ArrayList<>();
    private static final List<BlockSymbolOverride> symbolOverrides = new ArrayList<>();

    private static final Map<Block, String> VANILLA_BLOCK_NAMES = new IdentityHashMap<>();
    private static final Casings[] CASINGS_VALUES = Casings.values();
    private static final Materials[] MATERIALS_VALUES = Materials.values();

    static {
        // VanillaBlocks
        for (Field field : Blocks.class.getDeclaredFields()) {
            if (Block.class.isAssignableFrom(field.getType())) {
                try {
                    VANILLA_BLOCK_NAMES.put((Block) field.get(null), field.getName());
                } catch (IllegalAccessException ignored) {}
            }
        }

        registerBlockAssociation((block, meta, symbol) -> {
            String name = VANILLA_BLOCK_NAMES.get(block);
            if (name != null) {
                return symbol + "-> " + "addElement('" + symbol + "', ofBlocks(Blocks." + name + ", " + meta + ")";
            }
            return null;
        });

        // Casings
        registerBlockAssociation((block, meta, symbol) -> {
            for (Casings casing : CASINGS_VALUES) {
                try {
                    if (casing.getBlock() == block && casing.getBlockMeta() == meta) {
                        return symbol + "-> "
                            + "addElement('"
                            + symbol
                            + "', Casings."
                            + casing.name()
                            + ".asElement()";
                    }
                } catch (Exception ignored) {}
            }
            return null;
        });

        // Frames
        registerBlockAssociation((block, meta, symbol) -> {
            if (block instanceof BlockFrameBox) {
                try {
                    Materials material = BlockFrameBox.getMaterial(meta);
                    if (material != null) {
                        return symbol + "-> "
                            + "addElement('"
                            + symbol
                            + "', ofFrame(Materials."
                            + material.getName()
                            + ")";
                    }
                } catch (Exception ignored) {}
            }
            return null;
        });

        // Sheet Metal
        registerBlockAssociation((block, meta, symbol) -> {
            if (block == GregTechAPI.sBlockSheetmetalGT) {
                for (Materials material : MATERIALS_VALUES) {
                    if (material.mMetaItemSubID == meta) {
                        return symbol + "-> "
                            + "addElement('"
                            + symbol
                            + "', ofSheetMetal(Materials."
                            + material.getName()
                            + ")";
                    }
                }
            }
            return null;
        });

        // DebugStructureWriter
        registerSymbolOverride(((world, x, y, z, block, tileEntity) -> {
            if (tileEntity instanceof IGregTechTileEntity gte) {
                IMetaTileEntity mte = gte.getMetaTileEntity();

                if (mte instanceof MTEDebugStructureWriter) {
                    return '~';
                }
            }
            return null;
        }));
    }

    public interface BlockClassifier {

        Character classify(World world, int x, int y, int z, Block block, TileEntity tileEntity);
    }

    public interface BlockAssociation {

        String getExpression(Block block, int meta, Character symbol);
    }

    public static void registerBlockAssociation(BlockAssociation association) {
        blockAssociations.add(association);
    }

    public interface TileEntityAssociation {

        String getExpression(Class<? extends TileEntity> tileClass, Character symbol);
    }

    public static void registerTileEntityAssociation(TileEntityAssociation association) {
        tileEntityAssociations.add(association);
    }

    public interface BlockSymbolOverride {

        Character getSymbol(World world, int x, int y, int z, Block block, TileEntity tileEntity);
    }

    public static void registerSymbolOverride(BlockSymbolOverride override) {
        symbolOverrides.add(override);
    }

    public static String findBlockExpression(Block block, int meta, Character symbol) {
        for (BlockAssociation association : blockAssociations) {
            try {
                String expression = association.getExpression(block, meta, symbol);
                if (expression != null) {
                    return expression;
                }
            } catch (Exception e) {
                GT_FML_LOGGER.error("Error in block association", e);
            }
        }
        return null;
    }

    public static String findTileEntityExpression(Class<? extends TileEntity> tileClass, Character symbol) {
        for (TileEntityAssociation association : tileEntityAssociations) {
            try {
                String expression = association.getExpression(tileClass, symbol);
                if (expression != null) {
                    return expression;
                }
            } catch (Exception e) {
                GT_FML_LOGGER.error("Error in tile entity association", e);
            }
        }
        return null;
    }

    public static Character findSymbolOverride(World world, int x, int y, int z, Block block, TileEntity tileEntity) {
        for (BlockSymbolOverride override : symbolOverrides) {
            try {
                Character symbol = override.getSymbol(world, x, y, z, block, tileEntity);
                if (symbol != null) {
                    return symbol;
                }
            } catch (Exception e) {
                GT_FML_LOGGER.error("Error in symbol override", e);
            }
        }
        return null;
    }

    private static Map<String, Character> createSymbolMapping(Map<Block, Set<Integer>> blocks,
        Set<Class<? extends TileEntity>> tiles, Set<String> specialTiles) {

        Map<String, Character> mapping = new HashMap<>();
        int charIndex = 0;

        for (Map.Entry<Block, Set<Integer>> entry : blocks.entrySet()) {
            Block block = entry.getKey();
            for (Integer meta : entry.getValue()) {
                String key = block.getUnlocalizedName() + '\0' + meta;
                Character symbol = getNextSymbol(charIndex++);
                if (symbol == null) return null;
                mapping.put(key, symbol);
            }
        }

        for (Class<? extends TileEntity> tile : tiles) {
            Character symbol = getNextSymbol(charIndex++);
            if (symbol == null) return null;
            mapping.put(tile.getCanonicalName(), symbol);
        }

        for (String tile : specialTiles) {
            Character symbol = getNextSymbol(charIndex++);
            if (symbol == null) return null;
            mapping.put(tile, symbol);
        }

        return mapping;
    }

    private static Character getNextSymbol(int index) {
        return index < NICE_CHARS.length() ? NICE_CHARS.charAt(index) : null;
    }

    private static void appendLegend(StringBuilder builder, Map<String, Character> mapping,
        Map<Block, Set<Integer>> blocks, Set<Class<? extends TileEntity>> tiles, Set<String> specialTiles) {

        if (!blocks.isEmpty()) {
            builder.append("\n\nStructure:\n\nBlocks:\n");
        }

        for (Map.Entry<Block, Set<Integer>> entry : blocks.entrySet()) {
            Block block = entry.getKey();
            for (Integer meta : entry.getValue()) {
                String key = block.getUnlocalizedName() + '\0' + meta;
                Character symbol = mapping.get(key);

                String expression = DebugWriterHelper.findBlockExpression(block, meta, symbol);

                if (expression != null) {
                    builder.append(expression)
                        .append("\n");
                } else {
                    builder.append(symbol)
                        .append(" -> ofBlock(")
                        .append(block.getUnlocalizedName())
                        .append(", ")
                        .append(meta)
                        .append(");\n");
                }
            }
        }

        if (!tiles.isEmpty()) {
            builder.append("\nTiles:\n");
        }

        for (Class<? extends TileEntity> tile : tiles) {
            Character symbol = mapping.get(tile.getCanonicalName());

            String expression = DebugWriterHelper.findTileEntityExpression(tile, symbol);

            if (expression != null) {
                builder.append(expression)
                    .append("\n");
            } else {
                builder.append(symbol)
                    .append(" -> ofTileAdder(")
                    .append(tile.getSimpleName())
                    .append(");\n");
            }
        }

        if (!specialTiles.isEmpty()) {
            builder.append("\nSpecial Tiles:\n");
        }

        for (String tile : specialTiles) {
            Character symbol = mapping.get(tile);
            builder.append(symbol)
                .append(" -> ofSpecialTileAdder(")
                .append(tile)
                .append(");\n");
        }
    }

    public static String getPseudoJavaCode(World world, ExtendedFacing extendedFacing, int basePositionX,
        int basePositionY, int basePositionZ, int basePositionA, int basePositionB, int basePositionC,
        Function<? super TileEntity, String> tileEntityClassifier, int sizeA, int sizeB, int sizeC, boolean transpose,
        BlockClassifier customClassifier) {

        Map<Block, Set<Integer>> blocks = new TreeMap<>(Comparator.comparing(Block::getUnlocalizedName));
        Set<Class<? extends TileEntity>> tiles = new HashSet<>();
        Set<String> specialTiles = new HashSet<>();
        Map<String, Character> customMapping = new HashMap<>();

        iterate(
            world,
            extendedFacing,
            basePositionX,
            basePositionY,
            basePositionZ,
            basePositionA,
            basePositionB,
            basePositionC,
            sizeA,
            sizeB,
            sizeC,
            (w, x, y, z) -> {
                TileEntity tileEntity = w.getTileEntity(x, y, z);
                Block block = tileEntity == null ? w.getBlock(x, y, z) : null;

                Character customSymbol = customClassifier.classify(w, x, y, z, block, tileEntity);
                if (customSymbol != null) {
                    String key = "custom_" + x + "_" + y + "_" + z;
                    customMapping.put(key, customSymbol);
                    return;
                }

                if (tileEntity == null) {
                    if (block != null && block != Blocks.air) {
                        blocks.computeIfAbsent(block, k -> new TreeSet<>())
                            .add(block.getDamageValue(w, x, y, z));
                    }
                } else {
                    String classification = tileEntityClassifier.apply(tileEntity);
                    if (classification == null) {
                        tiles.add(tileEntity.getClass());
                    } else {
                        specialTiles.add(classification);
                    }
                }
            });

        Map<String, Character> symbolMapping = createSymbolMapping(blocks, tiles, specialTiles);
        if (symbolMapping == null) {
            return "Too complicated for nice chars";
        }
        symbolMapping.putAll(customMapping);

        StringBuilder builder = new StringBuilder();
        appendLegend(builder, symbolMapping, blocks, tiles, specialTiles);

        builder.append("\nOffsets:\n")
            .append(basePositionA)
            .append(", ")
            .append(basePositionB)
            .append(", ")
            .append(basePositionC)
            .append("\n");

        builder.append("\nDimensions (Width, Height, Length)\n")
            .append(sizeA)
            .append(", ")
            .append(sizeB)
            .append(", ")
            .append(sizeC)
            .append("\n");

        appendStructureScanWithCustom(
            builder,
            symbolMapping,
            transpose,
            world,
            extendedFacing,
            basePositionX,
            basePositionY,
            basePositionZ,
            basePositionA,
            basePositionB,
            basePositionC,
            sizeA,
            sizeB,
            sizeC,
            tileEntityClassifier,
            customClassifier);

        return builder.toString()
            .replaceAll("\"\"", "E");
    }

    private static void appendStructureScanWithCustom(StringBuilder builder, Map<String, Character> mapping,
        boolean transpose, World world, ExtendedFacing extendedFacing, int baseX, int baseY, int baseZ, int baseA,
        int baseB, int baseC, int sizeA, int sizeB, int sizeC, Function<? super TileEntity, String> classifier,
        BlockClassifier customClassifier) {

        String scanType = transpose ? "Transposed" : "Normal";
        builder.append("\n")
            .append(scanType)
            .append(" Scan:\n")
            .append("\n")
            .append("//spotless:off\n")
            .append("new String[][]{\n    {\"");

        Runnable nextRow = transpose ? () -> builder.append("\",\"") : () -> builder.append("\",\n    \"");

        Runnable nextLayer = transpose ? () -> {
            builder.setLength(builder.length() - 2);
            builder.append("},\n    {\"");
        } : () -> {
            builder.setLength(builder.length() - 7);
            builder.append("\n},{\n    \"");
        };

        iterate(
            world,
            extendedFacing,
            baseX,
            baseY,
            baseZ,
            baseA,
            baseB,
            baseC,
            transpose,
            sizeA,
            sizeB,
            sizeC,
            (w, x, y, z) -> {
                TileEntity tileEntity = w.getTileEntity(x, y, z);
                Block block = tileEntity == null ? w.getBlock(x, y, z) : null;

                Character customSymbol = customClassifier.classify(w, x, y, z, block, tileEntity);
                if (customSymbol != null) {
                    builder.append(customSymbol);
                    return;
                }

                String key;
                if (tileEntity == null) {
                    if (block != null && block != Blocks.air) {
                        key = block.getUnlocalizedName() + '\0' + block.getDamageValue(w, x, y, z);
                    } else {
                        key = null;
                    }
                } else {
                    String classification = classifier.apply(tileEntity);
                    key = classification != null ? classification
                        : tileEntity.getClass()
                            .getCanonicalName();
                }

                Character symbol = key != null ? mapping.get(key) : null;
                builder.append(symbol != null ? symbol : ' ');
            },
            nextRow,
            nextLayer);

        int trimLength = transpose ? 2 : 8;
        builder.setLength(builder.length() - trimLength);
        builder.append(transpose ? "\n}\n" : "}\n");
        builder.append("//spotless:on\n");
    }
}
