package com.github.technus.tectech.mechanics.structure;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import com.github.technus.tectech.mechanics.structure.adders.IBlockAdder;
import com.github.technus.tectech.mechanics.structure.adders.IHatchAdder;
import com.github.technus.tectech.mechanics.structure.adders.ITileAdder;
import com.github.technus.tectech.util.Vec3Impl;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sHintCasingsTT;

public class StructureUtility {
    private static final String NICE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz=|!@#$%&()[]{};:<>/?_,.*^'`";
    @SuppressWarnings("rawtypes")
    private static final Map<Vec3Impl, IStructureNavigate> STEP = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private static final IStructureElement AIR = new IStructureElement() {
        @Override
        public boolean check(Object t, World world, int x, int y, int z) {
            return world.getBlock(x, y, z).getMaterial() == Material.air;
        }

        @Override
        public boolean spawnHint(Object o, World world, int x, int y, int z, ItemStack trigger) {
            TecTech.proxy.hint_particle(world, x, y, z, sHintCasingsTT, 13);
            return true;
        }

        @Override
        public boolean placeBlock(Object o, World world, int x, int y, int z, ItemStack trigger) {
            world.setBlock(x, y, z, Blocks.air, 0, 2);
            return false;
        }
    };
    @SuppressWarnings("rawtypes")
    private static final IStructureElement NOT_AIR = new IStructureElement() {
        @Override
        public boolean check(Object t, World world, int x, int y, int z) {
            return world.getBlock(x, y, z).getMaterial() != Material.air;
        }

        @Override
        public boolean spawnHint(Object o, World world, int x, int y, int z, ItemStack trigger) {
            TecTech.proxy.hint_particle(world, x, y, z, sHintCasingsTT, 14);
            return true;
        }

        @Override
        public boolean placeBlock(Object o, World world, int x, int y, int z, ItemStack trigger) {
            world.setBlock(x, y, z, sHintCasingsTT, 14, 2);
            return true;
        }
    };
    @SuppressWarnings("rawtypes")
    private static final IStructureElement ERROR = new IStructureElement() {
        @Override
        public boolean check(Object t, World world, int x, int y, int z) {
            return false;
        }

        @Override
        public boolean spawnHint(Object o, World world, int x, int y, int z, ItemStack trigger) {
            TecTech.proxy.hint_particle(world, x, y, z, sHintCasingsTT, 15);
            return true;
        }

        @Override
        public boolean placeBlock(Object o, World world, int x, int y, int z, ItemStack trigger) {
            return true;
        }
    };

    private StructureUtility() {

    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureElement<T> isAir() {
        return AIR;
    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureElement<T> notAir() {
        return NOT_AIR;
    }

    /**
     * Check returns false.
     * Placement is always handled by this and does nothing.
     * Makes little to no use it in  fallback chain.
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> IStructureElement<T> error() {
        return ERROR;
    }

    /**
     * Check always returns: true.
     *
     * @param dots
     * @param <T>
     * @return
     */
    public static <T> IStructureElementNoPlacement<T> ofHint(int dots) {
        int meta = dots - 1;
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, sHintCasingsTT, meta);
                return false;
            }
        };
    }

    /**
     * Check always returns: true.
     *
     * @param icons
     * @param <T>
     * @return
     */
    public static <T> IStructureElementNoPlacement<T> ofHint(IIcon[] icons) {
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, icons);
                return false;
            }
        };
    }

    /**
     * Check always returns: true.
     *
     * @param icons
     * @param RGBa
     * @param <T>
     * @return
     */
    public static <T> IStructureElementNoPlacement<T> ofHint(IIcon[] icons,short[] RGBa) {
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle_tinted(world, x, y, z, icons,RGBa);
                return false;
            }
        };
    }

    /**
     * Does not allow Block duplicates (with different meta)
     */
    public static <T> IStructureElementNoPlacement<T> ofBlocksFlatHint(Map<Block, Integer> blocsMap, Block hintBlock, int hintMeta) {
        if (blocsMap == null || blocsMap.isEmpty() || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return blocsMap.getOrDefault(world.getBlock(x, y, z), -1) == world.getBlockMetadata(x, y, z);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    /**
     * Allows block duplicates (with different meta)
     */
    public static <T> IStructureElementNoPlacement<T> ofBlocksMapHint(Map<Block, Set<Integer>> blocsMap, Block hintBlock, int hintMeta) {
        if (blocsMap == null || blocsMap.isEmpty() || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        for (Set<Integer> value : blocsMap.values()) {
            if (value.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return blocsMap.getOrDefault(world.getBlock(x, y, z), Collections.emptySet()).contains(world.getBlockMetadata(x, y, z));
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElementNoPlacement<T> ofBlockHint(Block block, int meta, Block hintBlock, int hintMeta) {
        if (block == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return block == world.getBlock(x, y, z) && meta == world.getBlockMetadata(x, y, z);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElementNoPlacement<T> ofBlockHint(Block block, int meta) {
        return ofBlockHint(block, meta, block, meta);
    }

    public static <T> IStructureElementNoPlacement<T> ofBlockAdderHint(IBlockAdder<T> iBlockAdder, Block hintBlock, int hintMeta) {
        if (iBlockAdder == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return iBlockAdder.apply(t, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    /**
     * Does not allow Block duplicates (with different meta)
     */
    public static <T> IStructureElement<T> ofBlocksFlat(Map<Block, Integer> blocsMap, Block defaultBlock, int defaultMeta) {
        if (blocsMap == null || blocsMap.isEmpty() || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return blocsMap.getOrDefault(world.getBlock(x, y, z), -1) == world.getBlockMetadata(x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                return true;
            }
        };
    }

    /**
     * Allows block duplicates (with different meta)
     */
    public static <T> IStructureElement<T> ofBlocksMap(Map<Block, Set<Integer>> blocsMap, Block defaultBlock, int defaultMeta) {
        if (blocsMap == null || blocsMap.isEmpty() || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        for (Set<Integer> value : blocsMap.values()) {
            if (value.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return blocsMap.getOrDefault(world.getBlock(x, y, z), Collections.emptySet()).contains(world.getBlockMetadata(x, y, z));
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofBlock(Block block, int meta, Block defaultBlock, int defaultMeta) {
        if (block == null || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return block == world.getBlock(x, y, z) && meta == world.getBlockMetadata(x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofBlock(Block block, int meta) {
        return ofBlock(block, meta, block, meta);
    }

    public static <T> IStructureElement<T> ofBlockAdder(IBlockAdder<T> iBlockAdder, Block defaultBlock, int defaultMeta) {
        if (iBlockAdder == null || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return iBlockAdder.apply(t, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofBlockAdder(IBlockAdder<T> iBlockAdder, int dots) {
        return ofBlockAdder(iBlockAdder, sHintCasingsTT, dots - 1);
    }

    public static <T> IStructureElementNoPlacement<T> ofTileAdder(ITileAdder<T> iTileAdder, Block hintBlock, int hintMeta) {
        if (iTileAdder == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity && iTileAdder.apply(t, tileEntity);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IHatchAdder<T> iHatchAdder, int textureIndex, int dots) {
        return ofHatchAdder(iHatchAdder, textureIndex, sHintCasingsTT, dots - 1);
    }

    public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IHatchAdder<T> iHatchAdder, int textureIndex, Block hintBlock, int hintMeta) {
        if (iHatchAdder == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity && iHatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) textureIndex);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofHatchAdderOptional(IHatchAdder<T> iHatchAdder, int textureIndex, int dots, Block placeCasing, int placeCasingMeta) {
        return ofHatchAdderOptional(iHatchAdder, textureIndex, sHintCasingsTT, dots - 1, placeCasing, placeCasingMeta);
    }

    public static <T> IStructureElement<T> ofHatchAdderOptional(IHatchAdder<T> iHatchAdder, int textureIndex, Block hintBlock, int hintMeta, Block placeCasing, int placeCasingMeta) {
        if (iHatchAdder == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity &&
                        (iHatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) textureIndex) ||
                                (world.getBlock(x, y, z) == placeCasing && world.getBlockMetadata(x, y, z) == placeCasingMeta));
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, placeCasing, placeCasingMeta, 2);
                return true;
            }
        };
    }

    public static <B extends IStructureElement<T>, T> IStructureElement<T> onElementPass(Consumer<T> onCheckPass, B element) {
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                boolean check = element.check(t, world, x, y, z);
                if (check) {
                    onCheckPass.accept(t);
                }
                return check;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return element.placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return element.spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    public static <B extends IStructureElement<T>, T> IStructureElement<T> onElementFail(Consumer<T> onFail, B element) {
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                boolean check = element.check(t, world, x, y, z);
                if (!check) {
                    onFail.accept(t);
                }
                return check;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return element.placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return element.spawnHint(t, world, x, y, z, trigger);
            }
        };
    }


    /**
     * Take care while chaining, as it will try to call every structure element until it returns true.
     * If none does it will finally return false.
     *
     * @param elementChain
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> IStructureElementChain<T> ofChain(IStructureElement<T>... elementChain) {
        if (elementChain == null || elementChain.length == 0) {
            throw new IllegalArgumentException();
        }
        for (IStructureElement<T> iStructureElement : elementChain) {
            if (iStructureElement == null) {
                throw new IllegalArgumentException();
            }
        }
        return () -> elementChain;
    }

    /**
     * Take care while chaining, as it will try to call every structure element until it returns true.
     * If none does it will finally return false.
     *
     * @param elementChain
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> IStructureElementChain<T> ofChain(List<IStructureElement<T>> elementChain) {
        return ofChain(elementChain.toArray(new IStructureElement[0]));
    }

    public static <T> IStructureElementDeferred<T> defer(Supplier<IStructureElement<T>> to) {
        if (to == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return to.get().check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return to.get().placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return to.get().spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    public static <T> IStructureElementDeferred<T> defer(Function<T, IStructureElement<T>> to) {
        if (to == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return to.apply(t).check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(t).placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(t).spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    public static <T, K> IStructureElementDeferred<T> defer(Function<T, K> keyExtractor, Map<K, IStructureElement<T>> map) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return map.get(keyExtractor.apply(t)).check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(t)).placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(t)).spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    public static <T, K> IStructureElementDeferred<T> defer(Function<T, K> keyExtractor, Map<K, IStructureElement<T>> map, IStructureElement<T> defaultElem) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return map.getOrDefault(keyExtractor.apply(t), defaultElem).check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(t), defaultElem).placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(t), defaultElem).spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    @SafeVarargs
    public static <T> IStructureElementDeferred<T> defer(Function<T, Integer> keyExtractor, IStructureElement<T>... array) {
        if (keyExtractor == null || array == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return array[keyExtractor.apply(t)].check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(t)].placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(t)].spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureElementDeferred<T> defer(Function<T, Integer> keyExtractor, List<IStructureElement<T>> array) {
        return defer(keyExtractor, array.toArray(new IStructureElement[0]));
    }

    public static <T> IStructureElementDeferred<T> defer(BiFunction<T, ItemStack, IStructureElement<T>> to) {
        if (to == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return to.apply(t, null).check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(t, trigger).placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(t, trigger).spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    public static <T, K> IStructureElementDeferred<T> defer(BiFunction<T, ItemStack, K> keyExtractor, Map<K, IStructureElement<T>> map) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return map.get(keyExtractor.apply(t, null)).check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(t, trigger)).placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(t, trigger)).spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    public static <T, K> IStructureElementDeferred<T> defer(BiFunction<T, ItemStack, K> keyExtractor, Map<K, IStructureElement<T>> map, IStructureElement<T> defaultElem) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return map.getOrDefault(keyExtractor.apply(t, null), defaultElem).check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(t, trigger), defaultElem).placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(t, trigger), defaultElem).spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    @SafeVarargs
    public static <T> IStructureElementDeferred<T> defer(BiFunction<T, ItemStack, Integer> keyExtractor, IStructureElement<T>... array) {
        if (keyExtractor == null || array == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return array[keyExtractor.apply(t, null)].check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(t, trigger)].placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(t, trigger)].spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureElementDeferred<T> defer(BiFunction<T, ItemStack, Integer> keyExtractor, List<IStructureElement<T>> array) {
        return defer(keyExtractor, array.toArray(new IStructureElement[0]));
    }

    public static <T> IStructureElementDeferred<T> defer(Function<T, IStructureElement<T>> toCheck, BiFunction<T, ItemStack, IStructureElement<T>> to) {
        if (to == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return toCheck.apply(t).check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(t, trigger).placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(t, trigger).spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    public static <T, K> IStructureElementDeferred<T> defer(Function<T, K> keyExtractorCheck, BiFunction<T, ItemStack, K> keyExtractor, Map<K, IStructureElement<T>> map) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return map.get(keyExtractorCheck.apply(t)).check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(t, trigger)).placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(t, trigger)).spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    public static <T, K> IStructureElementDeferred<T> defer(Function<T, K> keyExtractorCheck, BiFunction<T, ItemStack, K> keyExtractor, Map<K, IStructureElement<T>> map, IStructureElement<T> defaultElem) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return map.getOrDefault(keyExtractorCheck.apply(t), defaultElem).check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(t, trigger), defaultElem).placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(t, trigger), defaultElem).spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    @SafeVarargs
    public static <T> IStructureElementDeferred<T> defer(Function<T, Integer> keyExtractorCheck, BiFunction<T, ItemStack, Integer> keyExtractor, IStructureElement<T>... array) {
        if (keyExtractor == null || array == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return array[keyExtractorCheck.apply(t)].check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(t, trigger)].placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(t, trigger)].spawnHint(t, world, x, y, z, trigger);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureElementDeferred<T> defer(Function<T, Integer> keyExtractorCheck, BiFunction<T, ItemStack, Integer> keyExtractor, List<IStructureElement<T>> array) {
        return defer(keyExtractorCheck, keyExtractor, array.toArray(new IStructureElement[0]));
    }

    public static <T> IStructureNavigate<T> step(int a, int b, int c) {
        return step(new Vec3Impl(a, b, c));
    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureNavigate<T> step(Vec3Impl step) {
        if (step == null || step.get0() < 0 || step.get1() < 0 || step.get2() < 0) {
            throw new IllegalArgumentException();
        }
        return STEP.computeIfAbsent(step, vec3 -> {
            if (vec3.get2() > 0) {
                return stepC(vec3.get0(), vec3.get1(), vec3.get2());
            } else if (vec3.get1() > 0) {
                return stepB(vec3.get0(), vec3.get1(), vec3.get2());
            } else {
                return stepA(vec3.get0(), vec3.get1(), vec3.get2());
            }
        });
    }

    private static <T> IStructureNavigate<T> stepA(int a, int b, int c) {
        return new IStructureNavigate<T>() {
            @Override
            public int getStepA() {
                return a;
            }

            @Override
            public int getStepB() {
                return b;
            }

            @Override
            public int getStepC() {
                return c;
            }
        };
    }

    private static <T> IStructureNavigate<T> stepB(int a, int b, int c) {
        return new IStructureNavigate<T>() {
            @Override
            public int getStepA() {
                return a;
            }

            @Override
            public int getStepB() {
                return b;
            }

            @Override
            public int getStepC() {
                return c;
            }

            @Override
            public boolean resetA() {
                return true;
            }
        };
    }

    private static <T> IStructureNavigate<T> stepC(int a, int b, int c) {
        return new IStructureNavigate<T>() {
            @Override
            public int getStepA() {
                return a;
            }

            @Override
            public int getStepB() {
                return b;
            }

            @Override
            public int getStepC() {
                return c;
            }

            @Override
            public boolean resetA() {
                return true;
            }

            @Override
            public boolean resetB() {
                return true;
            }
        };
    }

    /**
     * Used only to get pseudo code in structure writer...
     *
     * @param world
     * @return
     */
    public static String getPseudoJavaCode(World world, ExtendedFacing extendedFacing,
                                           int basePositionX, int basePositionY, int basePositionZ,
                                           int basePositionA, int basePositionB, int basePositionC,
                                           int sizeA, int sizeB, int sizeC, boolean transpose) {
        Map<Block, Set<Integer>> blocks = new TreeMap<>(Comparator.comparing(Block::getUnlocalizedName));
        Set<Class<? extends TileEntity>> tiles = new TreeSet<>(Comparator.comparing(Class::getCanonicalName));
        Set<Class<? extends IMetaTileEntity>> gtTiles = new TreeSet<>(Comparator.comparing(Class::getCanonicalName));
        iterate(world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,
                sizeA, sizeB, sizeC, ((w, x, y, z) -> {
                    TileEntity tileEntity = w.getTileEntity(x, y, z);
                    if (tileEntity == null) {
                        Block block = w.getBlock(x, y, z);
                        if (block != null && block != Blocks.air) {
                            blocks.compute(block, (b, set) -> {
                                if (set == null) {
                                    set = new TreeSet<>();
                                }
                                set.add(world.getBlockMetadata(x, y, z));
                                return set;
                            });
                        }
                    } else {
                        if (tileEntity instanceof IGregTechTileEntity) {
                            IMetaTileEntity meta = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
                            if (meta != null) {
                                gtTiles.add(meta.getClass());
                            } else {
                                tiles.add(tileEntity.getClass());
                            }
                        } else {
                            tiles.add(tileEntity.getClass());
                        }
                    }
                }));
        Map<String, Character> map = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        {
            int i = 0;
            char c;
            builder.append("\n\nStructure:\n")
                    .append("\nBlocks:\n");
            for (Map.Entry<Block, Set<Integer>> entry : blocks.entrySet()) {
                Block block = entry.getKey();
                Set<Integer> set = entry.getValue();
                for (Integer meta : set) {
                    c = NICE_CHARS.charAt(i++);
                    if (i > NICE_CHARS.length()) {
                        return "Too complicated for nice chars";
                    }
                    map.put(block.getUnlocalizedName() + '\0' + meta, c);
                    builder.append(c).append(" -> ofBlock...(")
                            .append(block.getUnlocalizedName()).append(", ").append(meta).append(", ...);\n");
                }
            }
            builder.append("\nTiles:\n");
            for (Class<? extends TileEntity> tile : tiles) {
                c = NICE_CHARS.charAt(i++);
                if (i > NICE_CHARS.length()) {
                    return "Too complicated for nice chars";
                }
                map.put(tile.getCanonicalName(), c);
                builder.append(c).append(" -> ofTileAdder(")
                        .append(tile.getCanonicalName()).append(", ...);\n");
            }
            builder.append("\nMeta:\n");
            for (Class<? extends IMetaTileEntity> gtTile : gtTiles) {
                c = NICE_CHARS.charAt(i++);
                if (i > NICE_CHARS.length()) {
                    return "Too complicated for nice chars";
                }
                map.put(gtTile.getCanonicalName(), c);
                builder.append(c).append(" -> ofHatchAdder(")
                        .append(gtTile.getCanonicalName()).append(", textureId, ...);\n");
            }
        }
        builder.append("\nOffsets:\n")
                .append(basePositionA).append(' ').append(basePositionB).append(' ').append(basePositionC).append('\n');
        if (transpose) {
            builder.append("\nTransposed Scan:\n")
                    .append("new String[][]{\n")
                    .append("    {\"");
            iterate(world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                    basePositionA, basePositionB, basePositionC, true,
                    sizeA, sizeB, sizeC, ((w, x, y, z) -> {
                        TileEntity tileEntity = w.getTileEntity(x, y, z);
                        if (tileEntity == null) {
                            Block block = w.getBlock(x, y, z);
                            if (block != null && block != Blocks.air) {
                                builder.append(map.get(block.getUnlocalizedName() + '\0' + world.getBlockMetadata(x, y, z)));
                            } else {
                                builder.append(' ');
                            }
                        } else {
                            if (tileEntity instanceof IGregTechTileEntity) {
                                IMetaTileEntity meta = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
                                if (meta != null) {
                                    builder.append(map.get(meta.getClass().getCanonicalName()));
                                } else {
                                    builder.append(map.get(tileEntity.getClass().getCanonicalName()));
                                }
                            } else {
                                builder.append(map.get(tileEntity.getClass().getCanonicalName()));
                            }
                        }
                    }),
                    () -> builder.append("\",\""),
                    () -> {
                        builder.setLength(builder.length() - 2);
                        builder.append("},\n    {\"");
                    });
            builder.setLength(builder.length() - 8);
            builder.append("\n}\n\n");
        } else {
            builder.append("\nNormal Scan:\n")
                    .append("new String[][]{{\n")
                    .append("    \"");
            iterate(world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                    basePositionA, basePositionB, basePositionC, false,
                    sizeA, sizeB, sizeC, ((w, x, y, z) -> {
                        TileEntity tileEntity = w.getTileEntity(x, y, z);
                        if (tileEntity == null) {
                            Block block = w.getBlock(x, y, z);
                            if (block != null && block != Blocks.air) {
                                builder.append(map.get(block.getUnlocalizedName() + '\0' + world.getBlockMetadata(x, y, z)));
                            } else {
                                builder.append(' ');
                            }
                        } else {
                            if (tileEntity instanceof IGregTechTileEntity) {
                                IMetaTileEntity meta = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
                                if (meta != null) {
                                    builder.append(map.get(meta.getClass().getCanonicalName()));
                                } else {
                                    builder.append(map.get(tileEntity.getClass().getCanonicalName()));
                                }
                            } else {
                                builder.append(map.get(tileEntity.getClass().getCanonicalName()));
                            }
                        }
                    }),
                    () -> builder.append("\",\n").append("    \""),
                    () -> {
                        builder.setLength(builder.length() - 7);
                        builder.append("\n").append("},{\n").append("    \"");
                    });
            builder.setLength(builder.length() - 8);
            builder.append("}\n\n");
        }
        return (builder.toString().replaceAll("\"\"", "E"));
    }

    public static void iterate(World world, ExtendedFacing extendedFacing,
                               int basePositionX, int basePositionY, int basePositionZ,
                               int basePositionA, int basePositionB, int basePositionC,
                               int sizeA, int sizeB, int sizeC,
                               IBlockPosConsumer iBlockPosConsumer) {
        sizeA -= basePositionA;
        sizeB -= basePositionB;
        sizeC -= basePositionC;

        int[] abc = new int[3];
        int[] xyz = new int[3];

        for (abc[2] = -basePositionC; abc[2] < sizeC; abc[2]++) {
            for (abc[1] = -basePositionB; abc[1] < sizeB; abc[1]++) {
                for (abc[0] = -basePositionA; abc[0] < sizeA; abc[0]++) {
                    extendedFacing.getWorldOffset(abc, xyz);
                    iBlockPosConsumer.consume(world, xyz[0] + basePositionX, xyz[1] + basePositionY, xyz[2] + basePositionZ);
                }

            }
        }
    }

    public static void iterate(World world, ExtendedFacing extendedFacing,
                               int basePositionX, int basePositionY, int basePositionZ,
                               int basePositionA, int basePositionB, int basePositionC,
                               boolean transpose, int sizeA, int sizeB, int sizeC,
                               IBlockPosConsumer iBlockPosConsumer,
                               Runnable nextB,
                               Runnable nextC) {
        sizeA -= basePositionA;
        sizeB -= basePositionB;
        sizeC -= basePositionC;

        int[] abc = new int[3];
        int[] xyz = new int[3];
        if (transpose) {
            for (abc[1] = -basePositionB; abc[1] < sizeB; abc[1]++) {
                for (abc[2] = -basePositionC; abc[2] < sizeC; abc[2]++) {
                    for (abc[0] = -basePositionA; abc[0] < sizeA; abc[0]++) {
                        extendedFacing.getWorldOffset(abc, xyz);
                        iBlockPosConsumer.consume(world, xyz[0] + basePositionX, xyz[1] + basePositionY, xyz[2] + basePositionZ);
                    }
                    nextB.run();
                }
                nextC.run();
            }
        } else {
            for (abc[2] = -basePositionC; abc[2] < sizeC; abc[2]++) {
                for (abc[1] = -basePositionB; abc[1] < sizeB; abc[1]++) {
                    for (abc[0] = -basePositionA; abc[0] < sizeA; abc[0]++) {
                        extendedFacing.getWorldOffset(abc, xyz);
                        iBlockPosConsumer.consume(world, xyz[0] + basePositionX, xyz[1] + basePositionY, xyz[2] + basePositionZ);
                    }
                    nextB.run();
                }
                nextC.run();
            }
        }
    }

    /**
     * Transposes shape (swaps B and C axis, can be used to un-transpose transposed shape)
     * WARNING! Do not use on old api...
     * @param structurePiece shape (transposed shape)
     * @return transposed shape (untransposed shape)
     */
    public static String[][] transpose(String[][] structurePiece){
        String[][] shape=new String[structurePiece[0].length][structurePiece.length];
        for (int i = 0; i < structurePiece.length; i++) {
            for (int j = 0; j < structurePiece[i].length; j++) {
                shape[j][i]=structurePiece[i][j];
            }
        }
        return shape;
    }
}
