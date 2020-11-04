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

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.structure.StructureIterationType.SPAWN_HINTS;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sHintCasingsTT;
import static java.lang.Integer.MIN_VALUE;

/**
 * Fluent API for structure checking!
 *
 * (Just import static this class to have a nice fluent syntax while defining structure definitions)
 */
public class StructureUtility {
    private static final String NICE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz=|!@#$%&()[]{};:<>/?_,.*^'`";
    @SuppressWarnings("rawtypes")
    private static final Map<Vec3Impl, IStructureNavigate> STEP = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private static final IStructureElement AIR = new IStructureElement() {
        @Override
        public boolean check(Object multiBlock, World world, int x, int y, int z) {
            return world.getBlock(x, y, z).getMaterial() == Material.air;
        }

        @Override
        public boolean spawnHint(Object multiBlock, World world, int x, int y, int z, ItemStack trigger) {
            TecTech.proxy.hint_particle(world, x, y, z, sHintCasingsTT, 13);
            return true;
        }

        @Override
        public boolean placeBlock(Object multiBlock, World world, int x, int y, int z, ItemStack trigger) {
            world.setBlock(x, y, z, Blocks.air, 0, 2);
            return false;
        }
    };
    @SuppressWarnings("rawtypes")
    private static final IStructureElement NOT_AIR = new IStructureElement() {
        @Override
        public boolean check(Object multiBlock, World world, int x, int y, int z) {
            return world.getBlock(x, y, z).getMaterial() != Material.air;
        }

        @Override
        public boolean spawnHint(Object multiBlock, World world, int x, int y, int z, ItemStack trigger) {
            TecTech.proxy.hint_particle(world, x, y, z, sHintCasingsTT, 14);
            return true;
        }

        @Override
        public boolean placeBlock(Object multiBlock, World world, int x, int y, int z, ItemStack trigger) {
            world.setBlock(x, y, z, sHintCasingsTT, 14, 2);
            return true;
        }
    };
    @SuppressWarnings("rawtypes")
    private static final IStructureElement ERROR = new IStructureElement() {
        @Override
        public boolean check(Object multiBlock, World world, int x, int y, int z) {
            return false;
        }

        @Override
        public boolean spawnHint(Object multiBlock, World world, int x, int y, int z, ItemStack trigger) {
            TecTech.proxy.hint_particle(world, x, y, z, sHintCasingsTT, 15);
            return true;
        }

        @Override
        public boolean placeBlock(Object multiBlock, World world, int x, int y, int z, ItemStack trigger) {
            return true;
        }
    };

    private StructureUtility() {

    }

    @SuppressWarnings("unchecked")
    public static <MultiBlock> IStructureElement<MultiBlock> isAir() {
        return AIR;
    }

    @SuppressWarnings("unchecked")
    public static <MultiBlock> IStructureElement<MultiBlock> notAir() {
        return NOT_AIR;
    }

    /**
     * Check returns false.
     * Placement is always handled by this and does nothing.
     * Makes little to no use it in  fallback chain.
     *
     * @param <MultiBlock>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <MultiBlock> IStructureElement<MultiBlock> error() {
        return ERROR;
    }

    //region hint only

    /**
     * Check always returns: true.
     *
     * @param dots
     * @param <MultiBlock>
     * @return
     */
    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofHint(int dots) {
        int meta = dots - 1;
        return new IStructureElementNoPlacement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return true;
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, sHintCasingsTT, meta);
                return false;
            }
        };
    }

    /**
     * Check always returns: true.
     *
     * @param icons
     * @param <MultiBlock>
     * @return
     */
    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofHintDeferred(Supplier<IIcon[]> icons) {
        return new IStructureElementNoPlacement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return true;
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, icons.get());
                return false;
            }
        };
    }

    /**
     * Check always returns: true.
     *
     * @param icons
     * @param RGBa
     * @param <MultiBlock>
     * @return
     */
    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofHintDeferred(Supplier<IIcon[]> icons, short[] RGBa) {
        return new IStructureElementNoPlacement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return true;
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle_tinted(world, x, y, z, icons.get(), RGBa);
                return false;
            }
        };
    }

    //endregion

    //region block

    /**
     * Does not allow Block duplicates (with different meta)
     */
    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofBlocksFlatHint(Map<Block, Integer> blocsMap, Block hintBlock, int hintMeta) {
        if (blocsMap == null || blocsMap.isEmpty() || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                Block worldBlock = world.getBlock(x, y, z);
                return blocsMap.getOrDefault(worldBlock, MIN_VALUE) == worldBlock.getDamageValue(world, x, y, z);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    /**
     * Allows block duplicates (with different meta)
     */
    public static <T> IStructureElementNoPlacement<T> ofBlocksMapHint(Map<Block, Collection<Integer>> blocsMap, Block hintBlock, int hintMeta) {
        if (blocsMap == null || blocsMap.isEmpty() || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        for (Collection<Integer> value : blocsMap.values()) {
            if (value.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        return new IStructureElementNoPlacement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                Block worldBlock = world.getBlock(x, y, z);
                return blocsMap.getOrDefault(worldBlock, Collections.emptySet()).contains(worldBlock.getDamageValue(world, x, y, z));
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofBlockHint(Block block, int meta, Block hintBlock, int hintMeta) {
        if (block == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                Block worldBlock = world.getBlock(x, y, z);
                return block == worldBlock && meta == worldBlock.getDamageValue(world, x, y, z);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofBlockHint(Block block, int meta) {
        return ofBlockHint(block, meta, block, meta);
    }

    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofBlockAdderHint(IBlockAdder<MultiBlock> iBlockAdder, Block hintBlock, int hintMeta) {
        if (iBlockAdder == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                Block worldBlock = world.getBlock(x, y, z);
                return iBlockAdder.apply(multiBlock, worldBlock, worldBlock.getDamageValue(world, x, y, z));
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    /**
     * Does not allow Block duplicates (with different meta)
     */
    public static <MultiBlock> IStructureElement<MultiBlock> ofBlocksFlat(Map<Block, Integer> blocsMap, Block defaultBlock, int defaultMeta) {
        if (blocsMap == null || blocsMap.isEmpty() || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        if(defaultBlock instanceof ICustomBlockSetting){
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return blocsMap.getOrDefault(worldBlock, MIN_VALUE) == worldBlock.getDamageValue(world, x, y, z);
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    ((ICustomBlockSetting) defaultBlock).setBlock(world, x, y, z, defaultMeta);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        }else {
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return blocsMap.getOrDefault(worldBlock, MIN_VALUE) == worldBlock.getDamageValue(world, x, y, z);
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        }
    }

    /**
     * Allows block duplicates (with different meta)
     */
    public static <T> IStructureElement<T> ofBlocksMap(Map<Block, Collection<Integer>> blocsMap, Block defaultBlock, int defaultMeta) {
        if (blocsMap == null || blocsMap.isEmpty() || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        for (Collection<Integer> value : blocsMap.values()) {
            if (value.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        if(defaultBlock instanceof ICustomBlockSetting){
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return blocsMap.getOrDefault(worldBlock, Collections.emptySet()).contains(worldBlock.getDamageValue(world, x, y, z));
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    ((ICustomBlockSetting) defaultBlock).setBlock(world, x, y, z, defaultMeta);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        }else {
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return blocsMap.getOrDefault(worldBlock, Collections.emptySet()).contains(worldBlock.getDamageValue(world, x, y, z));
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        }
    }

    public static <MultiBlock> IStructureElement<MultiBlock> ofBlock(Block block, int meta, Block defaultBlock, int defaultMeta) {
        if (block == null || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        if(block instanceof ICustomBlockSetting){
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return block == worldBlock && meta == worldBlock.getDamageValue(world, x, y, z);
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    ((ICustomBlockSetting) defaultBlock).setBlock(world, x, y, z, defaultMeta);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        } else {
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return block == worldBlock && meta == worldBlock.getDamageValue(world, x, y, z);
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        }
    }

    /**
     * Same as above but ignores target meta id
     */
    public static <MultiBlock> IStructureElement<MultiBlock> ofBlockAnyMeta(Block block, Block defaultBlock, int defaultMeta) {
        if (block == null || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        if(block instanceof ICustomBlockSetting){
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    return block == world.getBlock(x, y, z);
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    ((ICustomBlockSetting) defaultBlock).setBlock(world, x, y, z, defaultMeta);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        } else {
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    return block == world.getBlock(x, y, z);
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        }
    }

    public static <MultiBlock> IStructureElement<MultiBlock> ofBlock(Block block, int meta) {
        return ofBlock(block, meta, block, meta);
    }

    /**
     * Same as above but ignores target meta id
     */
    public static <MultiBlock> IStructureElement<MultiBlock> ofBlockAnyMeta(Block block) {
        return ofBlockAnyMeta(block,  block, 0);
    }

    /**
     * Same as above but allows to set hint particle render
     */
    public static <MultiBlock> IStructureElement<MultiBlock> ofBlockAnyMeta(Block block,int defaultMeta) {
        return ofBlockAnyMeta(block,  block, defaultMeta);
    }

    //endregion

    //region adders

    public static <MultiBlock> IStructureElement<MultiBlock> ofBlockAdder(IBlockAdder<MultiBlock> iBlockAdder, Block defaultBlock, int defaultMeta) {
        if (iBlockAdder == null || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        if(defaultBlock instanceof  ICustomBlockSetting){
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return iBlockAdder.apply(multiBlock, worldBlock, worldBlock.getDamageValue(world, x, y, z));
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    ((ICustomBlockSetting) defaultBlock).setBlock(world, x, y, z, defaultMeta);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        }else {
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return iBlockAdder.apply(multiBlock, worldBlock, worldBlock.getDamageValue(world, x, y, z));
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                    return true;
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }
            };
        }
    }

    public static <MultiBlock> IStructureElement<MultiBlock> ofBlockAdder(IBlockAdder<MultiBlock> iBlockAdder, int dots) {
        return ofBlockAdder(iBlockAdder, sHintCasingsTT, dots - 1);
    }

    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofTileAdder(ITileAdder<MultiBlock> iTileAdder, Block hintBlock, int hintMeta) {
        if (iTileAdder == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity && iTileAdder.apply(multiBlock, tileEntity);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofHatchAdder(IHatchAdder<MultiBlock> iHatchAdder, int textureIndex, int dots) {
        return ofHatchAdder(iHatchAdder, textureIndex, sHintCasingsTT, dots - 1);
    }

    public static <MultiBlock> IStructureElementNoPlacement<MultiBlock> ofHatchAdder(IHatchAdder<MultiBlock> iHatchAdder, int textureIndex, Block hintBlock, int hintMeta) {
        if (iHatchAdder == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity && iHatchAdder.apply(multiBlock, (IGregTechTileEntity) tileEntity, (short) textureIndex);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }
        };
    }

    public static <MultiBlock> IStructureElement<MultiBlock> ofHatchAdderOptional(IHatchAdder<MultiBlock> iHatchAdder, int textureIndex, int dots, Block placeCasing, int placeCasingMeta) {
        return ofHatchAdderOptional(iHatchAdder, textureIndex, sHintCasingsTT, dots - 1, placeCasing, placeCasingMeta);
    }

    public static <MultiBlock> IStructureElement<MultiBlock> ofHatchAdderOptional(IHatchAdder<MultiBlock> iHatchAdder, int textureIndex, Block hintBlock, int hintMeta, Block placeCasing, int placeCasingMeta) {
        if (iHatchAdder == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        if(placeCasing instanceof ICustomBlockSetting){
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    Block worldBlock = world.getBlock(x, y, z);
                    return (tileEntity instanceof IGregTechTileEntity &&
                            iHatchAdder.apply(multiBlock, (IGregTechTileEntity) tileEntity, (short) textureIndex)) ||
                            (worldBlock == placeCasing && worldBlock.getDamageValue(world, x, y, z) == placeCasingMeta);
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                    return true;
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    ((ICustomBlockSetting) placeCasing).setBlock(world, x, y, z, placeCasingMeta);
                    return true;
                }
            };
        }else {
            return new IStructureElement<MultiBlock>() {
                @Override
                public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    Block worldBlock = world.getBlock(x, y, z);
                    return (tileEntity instanceof IGregTechTileEntity &&
                            iHatchAdder.apply(multiBlock, (IGregTechTileEntity) tileEntity, (short) textureIndex)) ||
                            (worldBlock == placeCasing && worldBlock.getDamageValue(world, x, y, z) == placeCasingMeta);
                }

                @Override
                public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    TecTech.proxy.hint_particle(world, x, y, z, hintBlock, hintMeta);
                    return true;
                }

                @Override
                public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                    world.setBlock(x, y, z, placeCasing, placeCasingMeta, 2);
                    return true;
                }
            };
        }
    }

    //endregion

    //region side effects

    public static <MultiBlock> IStructureElement<MultiBlock> onElementPass(Consumer<MultiBlock> onCheckPass, IStructureElement<MultiBlock> element) {
        return new IStructureElement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                boolean check = element.check(multiBlock, world, x, y, z);
                if (check) {
                    onCheckPass.accept(multiBlock);
                }
                return check;
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return element.placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return element.spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    public static <MultiBlock> IStructureElement<MultiBlock> onElementFail(Consumer<MultiBlock> onFail, IStructureElement<MultiBlock> element) {
        return new IStructureElement<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                boolean check = element.check(multiBlock, world, x, y, z);
                if (!check) {
                    onFail.accept(multiBlock);
                }
                return check;
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return element.placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return element.spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    //endregion

    /**
     * Take care while chaining, as it will try to call every structure element until it returns true.
     * If none does it will finally return false.
     *
     * @param elementChain
     * @param <MultiBlock>
     * @return
     */
    @SafeVarargs
    public static <MultiBlock> IStructureElementChain<MultiBlock> ofChain(IStructureElement<MultiBlock>... elementChain) {
        if (elementChain == null || elementChain.length == 0) {
            throw new IllegalArgumentException();
        }
        for (IStructureElement<MultiBlock> iStructureElement : elementChain) {
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
     * @param <MultiBlock>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <MultiBlock> IStructureElementChain<MultiBlock> ofChain(List<IStructureElement<MultiBlock>> elementChain) {
        return ofChain(elementChain.toArray(new IStructureElement[0]));
    }

    //region defer

    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(Supplier<IStructureElement<MultiBlock>> to) {
        if (to == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return to.get().check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return to.get().placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return to.get().spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, IStructureElement<MultiBlock>> to) {
        if (to == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return to.apply(multiBlock).check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(multiBlock).placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(multiBlock).spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    public static <MultiBlock, K> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, K> keyExtractor, Map<K, IStructureElement<MultiBlock>> map) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return map.get(keyExtractor.apply(multiBlock)).check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(multiBlock)).placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(multiBlock)).spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    public static <MultiBlock, K> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, K> keyExtractor, Map<K, IStructureElement<MultiBlock>> map, IStructureElement<MultiBlock> defaultElem) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return map.getOrDefault(keyExtractor.apply(multiBlock), defaultElem).check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(multiBlock), defaultElem).placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(multiBlock), defaultElem).spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    @SafeVarargs
    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, Integer> keyExtractor, IStructureElement<MultiBlock>... array) {
        if (keyExtractor == null || array == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return array[keyExtractor.apply(multiBlock)].check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(multiBlock)].placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(multiBlock)].spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, Integer> keyExtractor, List<IStructureElement<MultiBlock>> array) {
        return defer(keyExtractor, array.toArray(new IStructureElement[0]));
    }

    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(BiFunction<MultiBlock, ItemStack, IStructureElement<MultiBlock>> to) {
        if (to == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return to.apply(multiBlock, null).check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(multiBlock, trigger).placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(multiBlock, trigger).spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    public static <MultiBlock, K> IStructureElementDeferred<MultiBlock> defer(BiFunction<MultiBlock, ItemStack, K> keyExtractor, Map<K, IStructureElement<MultiBlock>> map) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return map.get(keyExtractor.apply(multiBlock, null)).check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(multiBlock, trigger)).placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(multiBlock, trigger)).spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    public static <MultiBlock, K> IStructureElementDeferred<MultiBlock> defer(BiFunction<MultiBlock, ItemStack, K> keyExtractor, Map<K, IStructureElement<MultiBlock>> map, IStructureElement<MultiBlock> defaultElem) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return map.getOrDefault(keyExtractor.apply(multiBlock, null), defaultElem).check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(multiBlock, trigger), defaultElem).placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(multiBlock, trigger), defaultElem).spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    @SafeVarargs
    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(BiFunction<MultiBlock, ItemStack, Integer> keyExtractor, IStructureElement<MultiBlock>... array) {
        if (keyExtractor == null || array == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return array[keyExtractor.apply(multiBlock, null)].check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(multiBlock, trigger)].placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(multiBlock, trigger)].spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(BiFunction<MultiBlock, ItemStack, Integer> keyExtractor, List<IStructureElement<MultiBlock>> array) {
        return defer(keyExtractor, array.toArray(new IStructureElement[0]));
    }

    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, IStructureElement<MultiBlock>> toCheck, BiFunction<MultiBlock, ItemStack, IStructureElement<MultiBlock>> to) {
        if (to == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return toCheck.apply(multiBlock).check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(multiBlock, trigger).placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return to.apply(multiBlock, trigger).spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    public static <MultiBlock, K> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, K> keyExtractorCheck, BiFunction<MultiBlock, ItemStack, K> keyExtractor, Map<K, IStructureElement<MultiBlock>> map) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return map.get(keyExtractorCheck.apply(multiBlock)).check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(multiBlock, trigger)).placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.get(keyExtractor.apply(multiBlock, trigger)).spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    public static <MultiBlock, K> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, K> keyExtractorCheck, BiFunction<MultiBlock, ItemStack, K> keyExtractor, Map<K, IStructureElement<MultiBlock>> map, IStructureElement<MultiBlock> defaultElem) {
        if (keyExtractor == null || map == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return map.getOrDefault(keyExtractorCheck.apply(multiBlock), defaultElem).check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(multiBlock, trigger), defaultElem).placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return map.getOrDefault(keyExtractor.apply(multiBlock, trigger), defaultElem).spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    @SafeVarargs
    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, Integer> keyExtractorCheck, BiFunction<MultiBlock, ItemStack, Integer> keyExtractor, IStructureElement<MultiBlock>... array) {
        if (keyExtractor == null || array == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementDeferred<MultiBlock>() {
            @Override
            public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
                return array[keyExtractorCheck.apply(multiBlock)].check(multiBlock, world, x, y, z);
            }

            @Override
            public boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(multiBlock, trigger)].placeBlock(multiBlock, world, x, y, z, trigger);
            }

            @Override
            public boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
                return array[keyExtractor.apply(multiBlock, trigger)].spawnHint(multiBlock, world, x, y, z, trigger);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <MultiBlock> IStructureElementDeferred<MultiBlock> defer(Function<MultiBlock, Integer> keyExtractorCheck, BiFunction<MultiBlock, ItemStack, Integer> keyExtractor, List<IStructureElement<MultiBlock>> array) {
        return defer(keyExtractorCheck, keyExtractor, array.toArray(new IStructureElement[0]));
    }

    //endregion

    /**
     * Used internally, to generate skips for structure definitions
     *
     * @param a
     * @param b
     * @param c
     * @param <MultiBlock>
     * @return
     */
    public static <MultiBlock> IStructureNavigate<MultiBlock> step(int a, int b, int c) {
        return step(new Vec3Impl(a, b, c));
    }

    /**
     * Used internally, to generate skips for structure definitions
     *
     * @param step
     * @param <MultiBlock>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <MultiBlock> IStructureNavigate<MultiBlock> step(Vec3Impl step) {
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

    private static <MultiBlock> IStructureNavigate<MultiBlock> stepA(int a, int b, int c) {
        return new IStructureNavigate<MultiBlock>() {
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

    private static <MultiBlock> IStructureNavigate<MultiBlock> stepB(int a, int b, int c) {
        return new IStructureNavigate<MultiBlock>() {
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

    private static <MultiBlock> IStructureNavigate<MultiBlock> stepC(int a, int b, int c) {
        return new IStructureNavigate<MultiBlock>() {
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
                                set.add(block.getDamageValue(world, x, y, z));
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
                                builder.append(map.get(block.getUnlocalizedName() + '\0' + block.getDamageValue(world, x, y, z)));
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
                                builder.append(map.get(block.getUnlocalizedName() + '\0' + block.getDamageValue(world, x, y, z)));
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

    public static <MultiBlock> boolean iterate(MultiBlock object,
                                      ItemStack trigger,
                                      IStructureElement<MultiBlock>[] elements,
                                      World world,
                                      ExtendedFacing extendedFacing,
                                      int basePositionX, int basePositionY, int basePositionZ,
                                      int basePositionA, int basePositionB, int basePositionC,
                                      StructureIterationType iterationType) {
        if (world.isRemote ^ (iterationType == SPAWN_HINTS)) {
            return false;
        }

        //change base position to base offset
        basePositionA = -basePositionA;
        basePositionB = -basePositionB;
        basePositionC = -basePositionC;

        int[] abc = new int[]{basePositionA, basePositionB, basePositionC};
        int[] xyz = new int[3];

        switch (iterationType) {
            case SPAWN_HINTS: {
                for (IStructureElement<MultiBlock> element : elements) {
                    if (element.isNavigating()) {
                        abc[0] = (element.resetA() ? basePositionA : abc[0]) + element.getStepA();
                        abc[1] = (element.resetB() ? basePositionB : abc[1]) + element.getStepB();
                        abc[2] = (element.resetC() ? basePositionC : abc[2]) + element.getStepC();
                    } else {
                        extendedFacing.getWorldOffset(abc, xyz);
                        xyz[0] += basePositionX;
                        xyz[1] += basePositionY;
                        xyz[2] += basePositionZ;

                        element.spawnHint(object, world, xyz[0], xyz[1], xyz[2], trigger);

                        abc[0] += 1;
                    }
                }
                break;
            }
            case BUILD_TEMPLATE: {
                for (IStructureElement<MultiBlock> element : elements) {
                    if (element.isNavigating()) {
                        abc[0] = (element.resetA() ? basePositionA : abc[0]) + element.getStepA();
                        abc[1] = (element.resetB() ? basePositionB : abc[1]) + element.getStepB();
                        abc[2] = (element.resetC() ? basePositionC : abc[2]) + element.getStepC();
                    } else {
                        extendedFacing.getWorldOffset(abc, xyz);
                        xyz[0] += basePositionX;
                        xyz[1] += basePositionY;
                        xyz[2] += basePositionZ;

                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            element.placeBlock(object, world, xyz[0], xyz[1], xyz[2], trigger);
                        }
                        abc[0] += 1;
                    }
                }
                break;
            }
            case CHECK: {
                for (IStructureElement<MultiBlock> element : elements) {
                    if (element.isNavigating()) {
                        abc[0] = (element.resetA() ? basePositionA : abc[0]) + element.getStepA();
                        abc[1] = (element.resetB() ? basePositionB : abc[1]) + element.getStepB();
                        abc[2] = (element.resetC() ? basePositionC : abc[2]) + element.getStepC();
                    } else {
                        extendedFacing.getWorldOffset(abc, xyz);
                        xyz[0] += basePositionX;
                        xyz[1] += basePositionY;
                        xyz[2] += basePositionZ;

                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            if (!element.check(object, world, xyz[0], xyz[1], xyz[2])) {
                                if (DEBUG_MODE) {
                                    TecTech.LOGGER.info("Multi [" + basePositionX + ", " + basePositionY + ", " + basePositionZ + "] failed @ " +
                                            Arrays.toString(xyz) + " " + Arrays.toString(abc));
                                }
                                return false;
                            }
                        } else {
                            if (DEBUG_MODE) {
                                TecTech.LOGGER.info("Multi [" + basePositionX + ", " + basePositionY + ", " + basePositionZ + "] !blockExists @ " +
                                        Arrays.toString(xyz) + " " + Arrays.toString(abc));
                            }
                        }
                        abc[0] += 1;
                    }
                    break;
                }
            }
            case CHECK_FULLY: {
                for (IStructureElement<MultiBlock> element : elements) {
                    if (element.isNavigating()) {
                        abc[0] = (element.resetA() ? basePositionA : abc[0]) + element.getStepA();
                        abc[1] = (element.resetB() ? basePositionB : abc[1]) + element.getStepB();
                        abc[2] = (element.resetC() ? basePositionC : abc[2]) + element.getStepC();
                    } else {
                        extendedFacing.getWorldOffset(abc, xyz);
                        xyz[0] += basePositionX;
                        xyz[1] += basePositionY;
                        xyz[2] += basePositionZ;

                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            if (!element.check(object, world, xyz[0], xyz[1], xyz[2])) {
                                if (DEBUG_MODE) {
                                    TecTech.LOGGER.info("Multi [" + basePositionX + ", " + basePositionY + ", " + basePositionZ + "] failed @ " +
                                            Arrays.toString(xyz) + " " + Arrays.toString(abc));
                                }
                                return false;
                            }
                        } else {
                            if (DEBUG_MODE) {
                                TecTech.LOGGER.info("Multi [" + basePositionX + ", " + basePositionY + ", " + basePositionZ + "] !blockExists @ " +
                                        Arrays.toString(xyz) + " " + Arrays.toString(abc));
                            }
                            return false;
                        }
                        abc[0] += 1;
                    }
                }
                break;
            }
            default: return false;
        }
        return true;
    }

    /**
     * Transposes shape (swaps B and C axis, can be used to un-transpose transposed shape)
     * WARNING! Do not use on old api...
     *
     * @param structurePiece shape (transposed shape)
     * @return transposed shape (untransposed shape)
     */
    public static String[][] transpose(String[][] structurePiece) {
        String[][] shape = new String[structurePiece[0].length][structurePiece.length];
        for (int i = 0; i < structurePiece.length; i++) {
            for (int j = 0; j < structurePiece[i].length; j++) {
                shape[j][i] = structurePiece[i][j];
            }
        }
        return shape;
    }
}
