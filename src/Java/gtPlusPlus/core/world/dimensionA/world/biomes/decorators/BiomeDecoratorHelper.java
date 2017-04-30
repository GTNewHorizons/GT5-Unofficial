package gtPlusPlus.core.world.dimensionA.world.biomes.decorators;

import gtPlusPlus.core.world.dimensionA.world.biomes.BiomeLightForest;
import gtPlusPlus.core.world.dimensionA.world.biomes.ModBiomes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;

public class BiomeDecoratorHelper {

    private static WorldGenerator glowStone;

    public BiomeDecoratorHelper(){
        //		glowStone = new WorldGenMinable(Blocks.glowstone, 30, Blockss.lightStone);
    }

    protected static void decorateBiome(BiomeGenBase biome) {
        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(BiomeDecoratorMod.currentWorld, BiomeDecoratorMod.randomGenerator, BiomeDecoratorMod.chunk_X, BiomeDecoratorMod.chunk_Z));
        //perpere ores for generation
        initOres();
        //GenerateOres
        generateOreInBiome(biome);

        if(biome == ModBiomes.forestLight){
            BiomeDecoratorMod.howMenyTrees = BiomeLightForest.treesPerChunk;
            int i = BiomeDecoratorMod.howMenyTrees;
            /** Chunk Postions **/
            int chunkX;
            int chunkZ;
            int y;
            /** get blocks at the given locations **/
            Block block;
            Block blockA;
            /** Generates Small tree **/
            for(int a = 0; a < i; ++a){
                if(i == BiomeDecoratorMod.randomGenerator.nextInt(8)){
                    chunkX = BiomeDecoratorMod.chunk_X + BiomeDecoratorMod.randomGenerator.nextInt(16) + 8;
                    chunkZ = BiomeDecoratorMod.chunk_Z + BiomeDecoratorMod.randomGenerator.nextInt(16) + 8;
                    y = BiomeDecoratorMod.currentWorld.getTopSolidOrLiquidBlock(chunkX, chunkZ);
                    block = BiomeDecoratorMod.currentWorld.getBlock(chunkX, y, chunkZ);
                    blockA = BiomeDecoratorMod.currentWorld.getBlock(chunkX, y - 1, chunkZ);
                    if(block != Blocks.air || block != Blocks.water){
                        if(blockA != Blocks.air || blockA != Blocks.water){
                            BiomeDecoratorMod.smallTree.generate(BiomeDecoratorMod.currentWorld, BiomeDecoratorMod.randomGenerator, chunkX, y, chunkZ);
                        }
                    }
                }
                /** Generates Big tree **/
                if(i == BiomeDecoratorMod.randomGenerator.nextInt(15)){
                    chunkX = BiomeDecoratorMod.chunk_X + BiomeDecoratorMod.randomGenerator.nextInt(16) + 8;
                    chunkZ = BiomeDecoratorMod.chunk_Z + BiomeDecoratorMod.randomGenerator.nextInt(16) + 8;
                    y = BiomeDecoratorMod.currentWorld.getTopSolidOrLiquidBlock(chunkX, chunkZ);
                    block = BiomeDecoratorMod.currentWorld.getBlock(chunkX, y, chunkZ);
                    blockA = BiomeDecoratorMod.currentWorld.getBlock(chunkX, y - 1, chunkZ);
                    if(block != Blocks.air || block != Blocks.water){
                        if(blockA != Blocks.air || blockA != Blocks.water){
                            BiomeDecoratorMod.bigTree.generate(BiomeDecoratorMod.currentWorld, BiomeDecoratorMod.randomGenerator, chunkX, y, chunkZ);
                        }
                    }
                }
            }
            if(biome == ModBiomes.forestDark){
                //GENERARTE DARK FOREST STUFF HERE 
            }
        }
    }

    /**
     * Prepare ores for generation
     */
    private static void initOres() {
        //glowstone used fotr testing generation
        glowStone = new WorldGenMinable(Blocks.glowstone, 30, Blocks.stone);
    }

    /**
     * Geberate Ores In a Biome
     * @param biome
     */
    private static void generateOreInBiome(BiomeGenBase biome){
        if(biome == ModBiomes.forestLight){
            genStandardOre(20, glowStone, 0, 128);
        }
    }

    /**
     * Generate ores in chunks.
     * @param spawnWeight
     * @param generatorToSpawn
     * @param minSpawnHeight
     * @param maxYSpawnHeight
     */
    private static void genStandardOre(int spawnWeight, WorldGenerator generatorToSpawn, int minSpawnHeight, int maxYSpawnHeight) {
        for (int l = 0; l < spawnWeight; ++l) {
            int i1 = BiomeDecoratorMod.chunk_X + BiomeDecoratorMod.randomGenerator.nextInt(16);
            int j1 = BiomeDecoratorMod.randomGenerator.nextInt(maxYSpawnHeight - minSpawnHeight) + minSpawnHeight;
            int k1 = BiomeDecoratorMod.chunk_Z + BiomeDecoratorMod.randomGenerator.nextInt(16);
            generatorToSpawn.generate(BiomeDecoratorMod.currentWorld, BiomeDecoratorMod.randomGenerator, i1, j1, k1);
        }
    }
}
