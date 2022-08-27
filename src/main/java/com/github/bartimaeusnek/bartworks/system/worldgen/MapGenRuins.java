/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.worldgen;

import static com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler.maxTierRoss;
import static net.minecraftforge.common.ChestGenHooks.PYRAMID_JUNGLE_CHEST;

import com.github.bartimaeusnek.bartworks.util.Pair;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.threads.GT_Runnable_MachineBlockUpdate;
import gregtech.api.util.GT_Utility;
import java.security.SecureRandom;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ChestGenHooks;

@SuppressWarnings({"ALL"})
public abstract class MapGenRuins extends WorldGenerator {

    protected Pair<Block, Integer>[][] ToBuildWith = new Pair[4][0];

    @Override
    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
        return false;
    }

    protected void setFloorBlocks(int[] metas, Block... blocks) {
        this.ToBuildWith[0] = new Pair[metas.length];
        for (int i = 0; i < metas.length; i++) {
            this.ToBuildWith[0][i] = new Pair<>(blocks[i % blocks.length], metas[i]);
        }
    }

    protected void setWallBlocks(int[] metas, Block... blocks) {
        this.ToBuildWith[1] = new Pair[metas.length];
        for (int i = 0; i < metas.length; i++) {
            this.ToBuildWith[1][i] = new Pair<>(blocks[i % blocks.length], metas[i]);
        }
    }

    protected void setRoofBlocks(int[] metas, Block... blocks) {
        this.ToBuildWith[2] = new Pair[metas.length];
        for (int i = 0; i < metas.length; i++) {
            this.ToBuildWith[2][i] = new Pair<>(blocks[i % blocks.length], metas[i]);
        }
    }

    protected void setMiscBlocks(int[] metas, Block... blocks) {
        this.ToBuildWith[3] = new Pair[metas.length];
        for (int i = 0; i < metas.length; i++) {
            this.ToBuildWith[3][i] = new Pair<>(blocks[i % blocks.length], metas[i]);
        }
    }

    int[] statBlocks = new int[4];

    protected void setRandomBlockWAirChance(
            World worldObj, int x, int y, int z, Random rand, int airchance, Pair<Block, Integer>... blocks) {
        if (rand.nextInt(100) > airchance) this.setRandomBlock(worldObj, x, y, z, rand, blocks);
        else this.setBlock(worldObj, x, y, z, Blocks.air, 0);
    }

    protected void setRandomBlock(World worldObj, int x, int y, int z, Random rand, Pair<Block, Integer>... blocks) {
        Block toSet = blocks[rand.nextInt(blocks.length)].getKey();
        int meta = blocks[rand.nextInt(blocks.length)].getValue();
        this.setBlock(worldObj, x, y, z, toSet, meta);
    }

    protected void setBlock(World worldObj, int x, int y, int z, Block block, int meta) {
        this.setBlockAndNotifyAdequately(worldObj, x, y, z, block, meta);
    }

    protected void setBlock(World worldObj, int x, int y, int z, Pair<Block, Integer> pair) {
        this.setBlockAndNotifyAdequately(worldObj, x, y, z, pair.getKey(), pair.getValue());
    }

    private TileEntity setGTMachineBlock(World worldObj, int x, int y, int z, int meta) {
        boolean isEnabled = true;
        try {
            isEnabled = GT_Runnable_MachineBlockUpdate.isEnabled();
        } catch (Throwable ignored) {
            isEnabled = false;
        }
        if (isEnabled)
            throw new IllegalStateException("Machine Block Runnable needs to be disabled while creating world!");
        this.setBlockAndNotifyAdequately(
                worldObj,
                x,
                y,
                z,
                GT_WorldgenUtil.GT_TILES,
                GregTech_API.METATILEENTITIES[meta].getTileEntityBaseType());
        TileEntity tile = worldObj.getTileEntity(x, y, z);
        ((IGregTechTileEntity) tile).setInitialValuesAsNBT(null, (short) meta);
        return tile;
    }

    protected TileEntity reSetGTTileEntity(IGregTechTileEntity bte, World worldObj, int x, int y, int z, int meta) {
        worldObj.removeTileEntity(x, y, z);
        this.setBlock(worldObj, x, y, z, Blocks.air, 0);
        return setGTMachineBlock(worldObj, x, y, z, meta);
    }

    protected void setGTMachineBlockWChance(World worldObj, int x, int y, int z, Random rand, int airchance, int meta) {
        if (rand.nextInt(100) > airchance) {
            this.setGTMachineBlock(worldObj, x, y, z, meta);
        } else this.setBlock(worldObj, x, y, z, Blocks.air, 0);
    }

    protected void setGTCablekWChance(World worldObj, int x, int y, int z, Random rand, int airchance, int meta) {
        if (rand.nextInt(100) > airchance) {
            this.setGTCable(worldObj, x, y, z, meta);
        } else this.setBlock(worldObj, x, y, z, Blocks.air, 0);
    }

    protected void setGTMachine(World worldObj, int x, int y, int z, int meta, String ownerName, byte facing) {
        try {
            GT_Runnable_MachineBlockUpdate.setDisabled();
        } catch (Throwable ignored) {
        }
        setGTMachineBlock(worldObj, x, y, z, meta);
        BaseMetaTileEntity BTE = (BaseMetaTileEntity) worldObj.getTileEntity(x, y, z);
        BTE.setOwnerName(ownerName);
        BTE.setFrontFacing(facing);
        BTE = (BaseMetaTileEntity) worldObj.getTileEntity(x, y, z);
        checkTile(BTE, worldObj, x, y, z, meta, ownerName, facing, 0);
        try {
            GT_Runnable_MachineBlockUpdate.setEnabled();
        } catch (Throwable ignored) {
        }
    }

    private void checkTile(
            BaseMetaTileEntity BTE,
            World worldObj,
            int x,
            int y,
            int z,
            int meta,
            String ownerName,
            byte facing,
            int depth) {
        if (depth < 25) {
            if (BTE.getMetaTileID() != meta || worldObj.getTileEntity(x, y, z) != BTE || BTE.isInvalid()) {
                redoTile(BTE, worldObj, x, y, z, meta, ownerName, facing);
                checkTile(BTE, worldObj, x, y, z, meta, ownerName, facing, depth++);
            }
        } else {
            worldObj.removeTileEntity(x, y, z);
            worldObj.setBlockToAir(x, y, z);
        }
    }

    private void redoTile(
            BaseMetaTileEntity BTE, World worldObj, int x, int y, int z, int meta, String ownerName, byte facing) {
        reSetGTTileEntity(BTE, worldObj, x, y, z, meta);
        BTE = (BaseMetaTileEntity) worldObj.getTileEntity(x, y, z);
        BTE.setOwnerName(ownerName);
        BTE.setFrontFacing(facing);
    }

    protected void setGTCable(World worldObj, int x, int y, int z, int meta) {
        try {
            GT_Runnable_MachineBlockUpdate.setDisabled();
        } catch (Throwable ignored) {
        }
        BaseMetaPipeEntity BTE = (BaseMetaPipeEntity) setGTMachineBlock(worldObj, x, y, z, meta);
        MetaPipeEntity MPE = (MetaPipeEntity) BTE.getMetaTileEntity();
        BTE.mConnections |= (byte) (1 << (byte) 4);
        BTE.mConnections |= (byte) (1 << GT_Utility.getOppositeSide(4));
        BaseMetaTileEntity BPE = (BaseMetaTileEntity) worldObj.getTileEntity(x, y, z - 1);
        if (BPE != null) {
            BTE.mConnections |= (byte) (1 << (byte) 2);
        }
        MPE.mConnections = BTE.mConnections;
        try {
            GT_Runnable_MachineBlockUpdate.setEnabled();
        } catch (Throwable ignored) {
        }
    }

    public static class RuinsBase extends MapGenRuins {

        private static final String owner = "Ancient Cultures";

        @Override
        public boolean generate(World worldObj, Random rand1, int x, int y, int z) {

            for (int i = 0; i < rand1.nextInt(144); i++) {
                rand1.nextLong();
            }

            Random rand = new XSTR(rand1.nextLong());
            SecureRandom secureRandom = new SecureRandom();

            if (worldObj.getBlock(x, y, z) == Blocks.air) {
                while (worldObj.getBlock(x, y, z) == Blocks.air) {
                    y--;
                }
            }

            this.setFloorBlocks(new int[] {0, 0, 0}, Blocks.brick_block, Blocks.double_stone_slab, Blocks.stonebrick);
            this.setWallBlocks(new int[] {0, 1, 2, 1, 1}, Blocks.stonebrick);
            this.setRoofBlocks(new int[] {9}, Blocks.log);
            this.setMiscBlocks(new int[] {1}, Blocks.log);
            this.statBlocks = new int[] {rand.nextInt(this.ToBuildWith[0].length)};
            int colored = rand.nextInt(15);
            int tier = secureRandom.nextInt(maxTierRoss);
            boolean useColor = rand.nextBoolean();
            byte set = 0;
            byte toSet = (byte) (rand.nextInt(maxTierRoss - tier) + 1);
            short cablemeta = GT_WorldgenUtil.getCable(secureRandom, tier);
            byte treeinaRow = 0;
            boolean lastset = rand.nextBoolean();
            for (int dx = -6; dx <= 6; dx++) {
                for (int dy = 0; dy <= 8; dy++) {
                    for (int dz = -6; dz <= 6; dz++) {
                        this.setBlock(worldObj, x + dx, y + dy, z + dz, Blocks.air, 0);
                        if (dy == 0) {
                            Pair<Block, Integer> floor = this.ToBuildWith[0][this.statBlocks[0]];
                            this.setBlock(worldObj, x + dx, y + 0, z + dz, floor.getKey(), floor.getValue());
                        } else if (dy > 0 && dy < 4) {
                            if (Math.abs(dx) == 5 && Math.abs(dz) == 5) {
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + dy, z + dz, rand, 5, this.ToBuildWith[3][0]);
                            } else if ((dx == 0) && dz == -5 && (dy == 1 || dy == 2)) {
                                if (dy == 1) this.setBlock(worldObj, x + dx, y + 1, z + -5, Blocks.iron_door, 1);
                                if (dy == 2) this.setBlock(worldObj, x + dx, y + 2, z + dz, Blocks.iron_door, 8);
                            } else if (Math.abs(dx) == 5 && Math.abs(dz) < 5 || Math.abs(dz) == 5 && Math.abs(dx) < 5) {
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + dy, z + dz, rand, 25, this.ToBuildWith[1]);
                                if (dy == 2) {
                                    if (rand.nextInt(100) < 12)
                                        if (useColor)
                                            this.setRandomBlockWAirChance(
                                                    worldObj,
                                                    x + dx,
                                                    y + 2,
                                                    z + dz,
                                                    rand,
                                                    25,
                                                    new Pair<>(Blocks.stained_glass_pane, colored));
                                        else
                                            this.setRandomBlockWAirChance(
                                                    worldObj,
                                                    x + dx,
                                                    y + dy,
                                                    z + dz,
                                                    rand,
                                                    25,
                                                    new Pair<>(Blocks.glass_pane, 0));
                                }
                            }

                            if (dy == 3 && Math.abs(dx) == 6) {
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + 3, z + dz, rand, 25, this.ToBuildWith[2]);
                            }

                            if (dy == 1) {
                                if (dx == 3 && dz == -3) {
                                    this.setBlock(worldObj, x + 3, y + 1, z + dz, Blocks.crafting_table, 0);
                                }
                                if (dx == -3 && (dz == -3 || dz == -2)) {
                                    this.setBlock(worldObj, x + -3, y + dy, z + dz, Blocks.chest, 5);
                                    IInventory chest = (IInventory) worldObj.getTileEntity(x + dx, y + dy, z + dz);
                                    if (chest != null) {
                                        WeightedRandomChestContent.generateChestContents(
                                                secureRandom,
                                                ChestGenHooks.getItems(PYRAMID_JUNGLE_CHEST, rand),
                                                chest,
                                                ChestGenHooks.getCount(PYRAMID_JUNGLE_CHEST, rand));
                                    }
                                }

                                if (dx == 4 && dz == 4) {
                                    short meta = GT_WorldgenUtil.getGenerator(secureRandom, tier);
                                    this.setGTMachine(
                                            worldObj,
                                            x + dx,
                                            y + dy,
                                            z + dz,
                                            meta,
                                            owner,
                                            tier > 0 ? (byte) 4 : (byte) 2);
                                } else if (dx == 3 && dz == 4) {
                                    if (tier > 0) {
                                        short meta = GT_WorldgenUtil.getBuffer(secureRandom, tier);
                                        this.setGTMachine(worldObj, x + dx, y + dy, z + dz, meta, owner, (byte) 4);
                                    } else {
                                        this.setGTCablekWChance(worldObj, x + dx, y + dy, z + dz, rand, 33, cablemeta);
                                    }
                                } else if (dx < 3 && dx > -5 && dz == 4) {
                                    this.setGTCablekWChance(worldObj, x + dx, y + dy, z + dz, rand, 33, cablemeta);
                                } else if (dx < 3 && dx > -5 && dz == 3 && set < toSet) {
                                    if (!lastset || treeinaRow > 2) {
                                        short meta = GT_WorldgenUtil.getMachine(secureRandom, tier);
                                        this.setGTMachine(worldObj, x + dx, y + dy, z + dz, meta, owner, (byte) 2);

                                        set++;
                                        treeinaRow = 0;
                                        lastset = true;
                                    } else {
                                        lastset = rand.nextBoolean();
                                        if (lastset) treeinaRow++;
                                    }
                                }
                            }
                        } else if (dy == 4) {
                            if (Math.abs(dx) == 5)
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + 4, z + dz, rand, 25, this.ToBuildWith[2]);
                            else if (Math.abs(dz) == 5 && Math.abs(dx) < 5)
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + dy, z + dz, rand, 25, this.ToBuildWith[1]);
                        } else if (dy == 5) {
                            if (Math.abs(dx) == 4)
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + 5, z + dz, rand, 25, this.ToBuildWith[2]);
                            else if (Math.abs(dz) == 5 && Math.abs(dx) < 4)
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + dy, z + dz, rand, 25, this.ToBuildWith[1]);
                        } else if (dy == 6) {
                            if (Math.abs(dx) == 3)
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + 6, z + dz, rand, 25, this.ToBuildWith[2]);
                            else if (Math.abs(dz) == 5 && Math.abs(dx) < 3)
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + dy, z + dz, rand, 25, this.ToBuildWith[1]);
                        } else if (dy == 7) {
                            if (Math.abs(dx) == 2)
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + 7, z + dz, rand, 25, this.ToBuildWith[2]);
                            else if (Math.abs(dz) == 5 && Math.abs(dx) < 2)
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + dy, z + dz, rand, 25, this.ToBuildWith[1]);
                        } else if (dy == 8) {
                            if (Math.abs(dx) == 1 || Math.abs(dx) == 0)
                                this.setRandomBlockWAirChance(
                                        worldObj, x + dx, y + 8, z + dz, rand, 25, this.ToBuildWith[2]);
                        }
                    }
                }
            }
            tosetloop:
            while (set < toSet) {
                int dy = 1;
                int dz = 3;
                for (int dx = 2; dx > -5; dx--) {
                    if (set < toSet) {
                        if (!lastset || treeinaRow > 2 && worldObj.getTileEntity(x + dx, y + dy, z + dz) == null) {
                            short meta = GT_WorldgenUtil.getMachine(secureRandom, tier);
                            this.setGTMachine(worldObj, x + dx, y + dy, z + dz, meta, owner, (byte) 2);

                            set++;
                            treeinaRow = 0;
                            lastset = true;
                        } else {
                            lastset = rand.nextBoolean();
                            if (lastset) treeinaRow++;
                        }
                    } else break tosetloop;
                }
            }
            return true;
        }
    }
}
