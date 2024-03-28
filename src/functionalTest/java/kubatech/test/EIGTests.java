/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.test;

import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeIndustrialGreenhouse.EIG_BALANCE_IC2_ACCELERATOR_TIER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

import org.junit.jupiter.api.Test;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.blocks.GT_Item_Machines;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.Ic2Items;
import ic2.core.crop.TileEntityCrop;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeIndustrialGreenhouse;

public class EIGTests {

    private static final int EIG_CONTROLLER_METADATA = 12_792;
    private static final int NUMBER_OF_CROPS_PER_TEST = 90;
    private static final int NUMBER_OF_TESTS_TO_DO = 30;

    static World myWorld;

    public EIGTests() {
        if (!DimensionManager.isDimensionRegistered(256)) {
            DimensionManager.registerProviderType(256, WorldProviderSurface.class, false);
            DimensionManager.registerDimension(256, 256);
        }
        if (myWorld == null) {
            myWorld = new WorldServer(MinecraftServer.getServer(), new ISaveHandler() {

                @Override
                public WorldInfo loadWorldInfo() {
                    return null;
                }

                @Override
                public void checkSessionLock() throws MinecraftException {

                }

                @Override
                public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
                    return null;
                }

                @Override
                public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {

                }

                @Override
                public void saveWorldInfo(WorldInfo p_75761_1_) {

                }

                @Override
                public IPlayerFileData getSaveHandler() {
                    return null;
                }

                @Override
                public void flush() {

                }

                @Override
                public File getWorldDirectory() {
                    return null;
                }

                @Override
                public File getMapFileFromName(String p_75758_1_) {
                    return null;
                }

                @Override
                public String getWorldDirectoryName() {
                    return "dummy";
                }
            },
                "DummyTestWorld",
                256,
                new WorldSettings(256, WorldSettings.GameType.SURVIVAL, false, false, WorldType.DEFAULT),
                MinecraftServer.getServer().theProfiler) {

                @Override
                public File getChunkSaveLocation() {
                    return new File("ignoreme");
                }

                @Override
                public int getBlockLightValue(int p_72957_1_, int p_72957_2_, int p_72957_3_) {
                    return 4;
                }
            };
        }
    }

    private static int leftOverTicksFromRealRun = 0;

    ItemStackMap<Integer> getRealDrops(TileEntityCrop cropTile, CropCard cc, int growth, int gain, int resistance) {
        cropTile.setCrop(cc);
        cropTile.setGrowth((byte) growth);
        cropTile.setGain((byte) gain);
        cropTile.setResistance((byte) resistance);
        cropTile.tick();

        ItemStackMap<Integer> expected = new ItemStackMap<>();

        // run for 30 minutes
        for (int k = 0; k < NUMBER_OF_CROPS_PER_TEST; k++) {
            cropTile.ticker = 1;
            cropTile.setSize((byte) cc.maxSize());
            cropTile.setSize(cc.getSizeAfterHarvest(cropTile));
            cropTile.growthPoints = 0;
            int lastHarvestedAt = 0;
            int i;
            for (i = 0; i < (30 * MINUTES) * (1 << EIG_BALANCE_IC2_ACCELERATOR_TIER);) {
                i += TileEntityCrop.tickRate;
                cropTile.tick();
                if (!cc.canGrow(cropTile)) {
                    lastHarvestedAt = i;
                    ItemStack[] stacks = cropTile.harvest_automated(false);
                    for (ItemStack stack : stacks) {
                        expected.merge(stack, stack.stackSize, Integer::sum);
                    }
                }
            }
            leftOverTicksFromRealRun += i - lastHarvestedAt;
        }

        return expected;
    }

    ItemStackMap<Integer> getEIGDrops(GT_MetaTileEntity_ExtremeIndustrialGreenhouse EIG, ItemStack stack) {
        ItemStackMap<Integer> generated = new ItemStackMap<>();
        int imax = (30 * MINUTES) / (5 * SECONDS);
        double ticks_to_ignore_per_operation = Math
            .ceil((double) leftOverTicksFromRealRun / (NUMBER_OF_CROPS_PER_TEST * imax));
        for (int j = 0; j < NUMBER_OF_CROPS_PER_TEST; j++) {
            GT_MetaTileEntity_ExtremeIndustrialGreenhouse.GreenHouseSlot slot = new GT_MetaTileEntity_ExtremeIndustrialGreenhouse.GreenHouseSlot(
                EIG,
                stack.copy(),
                true,
                false);
            if (slot.isValid) {
                for (int i = 0; i < imax; i++) {
                    int ticks_to_ignore = (int) Math.min(ticks_to_ignore_per_operation, leftOverTicksFromRealRun);
                    leftOverTicksFromRealRun -= ticks_to_ignore;
                    for (ItemStack ic2Drop : slot.getIC2Drops(
                        EIG,
                        (5 * SECONDS * (1 << EIG_BALANCE_IC2_ACCELERATOR_TIER)) - (double) ticks_to_ignore)) {
                        generated.merge(ic2Drop, ic2Drop.stackSize, Integer::sum);
                    }
                }
            }
        }

        return generated;
    }

    @Test
    void EIGDrops() {

        myWorld.setBlock(10, 80, 0, Blocks.farmland, 0, 0);
        myWorld.setBlock(10, 81, 0, Block.getBlockFromItem(Ic2Items.crop.getItem()), 0, 0);
        CropCard cc = Crops.instance.getCropCard("gregtech", "Indigo");
        TileEntityCrop cropTile = (TileEntityCrop) myWorld.getTileEntity(10, 81, 0);
        ItemStack ccStack = cropTile.generateSeeds(cc, (byte) 10, (byte) 10, (byte) 10, (byte) 1);
        for (int i = 0; i < TileEntityCrop.tickRate; i++) {
            cropTile.waterStorage = 200;
            cropTile.updateEntity();
        }

        GT_Item_Machines itemMachines = (GT_Item_Machines) Item.getItemFromBlock(GregTech_API.sBlockMachines);
        itemMachines.placeBlockAt(
            new ItemStack(itemMachines, 1, EIG_CONTROLLER_METADATA),
            null,
            myWorld,
            0,
            81,
            0,
            2,
            0,
            0,
            0,
            EIG_CONTROLLER_METADATA);
        IGregTechTileEntity te = (IGregTechTileEntity) myWorld.getTileEntity(0, 81, 0);
        GT_MetaTileEntity_ExtremeIndustrialGreenhouse EIG = (GT_MetaTileEntity_ExtremeIndustrialGreenhouse) te
            .getMetaTileEntity();

        int[] abc = new int[] { 0, -2, 3 };
        int[] xyz = new int[] { 0, 0, 0 };
        EIG.getExtendedFacing()
            .getWorldOffset(abc, xyz);
        xyz[0] += te.getXCoord();
        xyz[1] += te.getYCoord();
        xyz[2] += te.getZCoord();

        myWorld.setBlock(xyz[0], xyz[1] - 2, xyz[2], GregTech_API.sBlockCasings4, 1, 0);
        myWorld.setBlock(xyz[0], xyz[1] - 1, xyz[2], Blocks.farmland, 0, 0);

        ItemStack stackToTest = null;

        for (int n = 0; n < 5; n++) {

            int[] x = new int[NUMBER_OF_TESTS_TO_DO];
            int[] y = new int[NUMBER_OF_TESTS_TO_DO];

            // MinecraftServer.getServer()
            // .addChatMessage(new ChatComponentText("[EIGTest results]"));

            for (int i = 0; i < NUMBER_OF_TESTS_TO_DO; i++) {
                leftOverTicksFromRealRun = 0;
                ItemStackMap<Integer> expected = getRealDrops(cropTile, cc, 10, 10, 10);
                ItemStackMap<Integer> generated = getEIGDrops(EIG, ccStack);

                // MinecraftServer.getServer()
                // .addChatMessage(new ChatComponentText("[TEST" + i + "]Real crop drops:"));
                // for (Map.Entry<ItemStack, Integer> entry : expected.entrySet()) {
                // MinecraftServer.getServer()
                // .addChatMessage(new ChatComponentText("- " + entry.getKey().getDisplayName() + " x" +
                // entry.getValue()));
                // }

                // MinecraftServer.getServer()
                // .addChatMessage(new ChatComponentText("[TEST" + i + "]EIG crop drops:"));
                // for (Map.Entry<ItemStack, Integer> entry : generated.entrySet()) {
                // MinecraftServer.getServer()
                // .addChatMessage(new ChatComponentText("- " + entry.getKey().getDisplayName() + " x" +
                // entry.getValue()));
                // }

                // we are only comparing one item from drops
                if (stackToTest == null) {
                    stackToTest = expected.entrySet()
                        .stream()
                        .max(Comparator.comparingInt(Map.Entry::getValue))
                        .get()
                        .getKey();
                }

                int expectedValue = expected.getOrDefault(stackToTest, 0);
                int generatedValue = generated.getOrDefault(stackToTest, 0);

                x[i] = expectedValue;
                y[i] = generatedValue;
            }

            double real_average = Arrays.stream(x)
                .average()
                .getAsDouble();
            double eig_average = Arrays.stream(y)
                .average()
                .getAsDouble();

            double real_variance = 0d;
            double a = 0d;
            for (int i : x) {
                a += (i - real_average) * (i - real_average);
            }
            a /= NUMBER_OF_TESTS_TO_DO;
            real_variance = a;

            double eig_variance = 0d;
            a = 0d;
            for (int i : y) {
                a += (i - eig_average) * (i - eig_average);
            }
            a /= NUMBER_OF_TESTS_TO_DO;
            eig_variance = a;

            double u = (real_average - eig_average)
                / Math.sqrt((real_variance / NUMBER_OF_TESTS_TO_DO) + (eig_variance / NUMBER_OF_TESTS_TO_DO));
            MinecraftServer.getServer()
                .addChatMessage(
                    new ChatComponentText(
                        "real average = " + Math
                            .round(real_average) + " eig average = " + Math.round(eig_average) + " u = " + u));
            double test_critical_value = 1.959964d;
            boolean passed = Math.abs(u) < test_critical_value;
            boolean instafail = Math.abs(u) > test_critical_value * 2;
            if (passed) return;
            assertFalse(instafail);
        }
        fail();

    }

}
