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

import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

import org.junit.jupiter.api.Test;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.blocks.ItemMachines;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.Ic2Items;
import ic2.core.crop.TileEntityCrop;
import ic2.core.item.ItemCropSeed;
import kubatech.api.eig.EIGDropTable;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGIC2Bucket;

public class EIGTests {

    private static final int EIG_CONTROLLER_METADATA = 12_792;
    private static final int EIG_SIMULATION_TIME = 24 * HOURS;
    private static final int NUMBER_OF_TESTS_TO_DO = 1000;

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

                @Override
                public BiomeGenBase getBiomeGenForCoords(int x, int z) {
                    // give the environment a fighting chance of being bearable for crops
                    return BiomeGenBase.jungle;
                }
            };
        }
    }

    EIGDropTable getRealDrops(TileEntityCrop cropTile, CropCard cc, int growth, int gain, int resistance) {
        cropTile.setCrop(cc);
        cropTile.setGrowth((byte) growth);
        cropTile.setGain((byte) gain);
        cropTile.setResistance((byte) resistance);
        EIGDropTable expected = new EIGDropTable();
        byte startingHumidity = cropTile.humidity;
        byte startingNutrients = cropTile.nutrients;
        byte startingAirQuality = cropTile.airQuality;
        int startingNutrientStorage = cropTile.nutrientStorage;
        int startingWaterStorage = cropTile.waterStorage;
        // reset the crop to it's stage after harvest
        cropTile.setSize((byte) cc.maxSize());
        cropTile.setSize(cc.getSizeAfterHarvest(cropTile));
        for (int timeElapsed = 0; timeElapsed < EIG_SIMULATION_TIME; timeElapsed += TileEntityCrop.tickRate) {
            // force reset the stats to max because the eig shouldn't make them change.
            // some crops check water storage in the can grow and we are ticking which consumes water.
            cropTile.humidity = startingHumidity;
            cropTile.nutrients = startingNutrients;
            cropTile.airQuality = startingAirQuality;
            cropTile.nutrientStorage = startingNutrientStorage;
            cropTile.waterStorage = startingWaterStorage;
            // if fully grown harvest the crop
            if (cropTile.getSize() >= cc.maxSize()) {
                ItemStack[] stacks = cropTile.harvest_automated(false);
                for (ItemStack stack : stacks) {
                    expected.addDrop(stack, stack.stackSize);
                }
            }
            cropTile.tick();
        }
        // ensure it leaves the same way it came in
        cropTile.humidity = startingHumidity;
        cropTile.nutrients = startingNutrients;
        cropTile.airQuality = startingAirQuality;
        cropTile.nutrientStorage = startingNutrientStorage;
        cropTile.waterStorage = startingWaterStorage;
        return expected;
    }

    EIGDropTable getEIGDrops(MTEExtremeIndustrialGreenhouse EIG, ItemStack stack) {
        EIGDropTable generated = new EIGDropTable();
        EIGIC2Bucket bucket = new EIGIC2Bucket(stack, stack.stackSize, null, false);
        bucket.revalidate(EIG);
        bucket.addProgress(EIG_SIMULATION_TIME, generated);
        return generated;
    }

    @Test
    void EIGDrops() {
        myWorld.setBlock(10, 80, 0, Blocks.farmland, 0, 0);
        myWorld.setBlock(10, 81, 0, Block.getBlockFromItem(Ic2Items.crop.getItem()), 0, 0);
        // using stickreed since it has a random stage after harvest.
        // it's also more preferable to test using faster growing crops since they can be harvested more often.
        CropCard cc = Crops.instance.getCropCard("IC2", "stickreed");
        TileEntityCrop cropTile = (TileEntityCrop) myWorld.getTileEntity(10, 81, 0);
        ItemStack ccStack = ItemCropSeed.generateItemStackFromValues(cc, (byte) 10, (byte) 10, (byte) 10, (byte) 1);

        ItemMachines itemMachines = (ItemMachines) Item.getItemFromBlock(GregTechAPI.sBlockMachines);
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
        MTEExtremeIndustrialGreenhouse EIG = (MTEExtremeIndustrialGreenhouse) te.getMetaTileEntity();

        // update stats of crop TE to those provided by the EIG
        cropTile.humidity = EIGIC2Bucket.getHumidity(EIG, false);
        cropTile.nutrients = EIGIC2Bucket.getNutrients(EIG);
        cropTile.airQuality = EIGIC2Bucket.getAirQuality(EIG);

        int[] abc = new int[] { 0, -2, 3 };
        int[] xyz = new int[] { 0, 0, 0 };
        EIG.getExtendedFacing()
            .getWorldOffset(abc, xyz);
        xyz[0] += te.getXCoord();
        xyz[1] += te.getYCoord();
        xyz[2] += te.getZCoord();

        myWorld.setBlock(xyz[0], xyz[1] - 2, xyz[2], GregTechAPI.sBlockCasings4, 1, 0);
        myWorld.setBlock(xyz[0], xyz[1] - 1, xyz[2], Blocks.farmland, 0, 0);

        ItemStack stackToTest = null;

        double realAvg = 0, eigAvg = 0;

        for (int i = 0; i < NUMBER_OF_TESTS_TO_DO; i++) {
            EIGDropTable expected = getRealDrops(cropTile, cc, 10, 10, 10);
            EIGDropTable generated = getEIGDrops(EIG, ccStack);

            // we are only comparing one item from drops
            if (stackToTest == null) {
                stackToTest = expected.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .get()
                    .getKey();
            }

            realAvg += expected.getItemAmount(stackToTest);
            // EIG with ic2 crops doesn't actually have variance, it uses very precise approximations that create
            // accurate growth rate and drop quality approximations.
            eigAvg += generated.getItemAmount(stackToTest);
        }
        realAvg /= NUMBER_OF_TESTS_TO_DO;
        eigAvg /= NUMBER_OF_TESTS_TO_DO;
        double accuracy = Math.min(realAvg / eigAvg, eigAvg / realAvg);

        String debugInfo = String.format("realAvg: %.5f | eigAvg : %.5f | accuracy = %.5f", realAvg, eigAvg, accuracy);
        System.out.println(debugInfo);

        // We aim for about 99% accuracy over here.
        assertTrue(accuracy >= 0.99d);
    }

}
