package pers.gwyog.gtneioreplugin.util;

import java.util.HashMap;

import gregapi.worldgen.WorldgenObject;
import gregapi.worldgen.Worldgen_GT_Ore_Bedrock;
import gregapi.worldgen.Worldgenerator;

public class GT6OreBedrockHelper {
    public static HashMap<String, OreBedrockWrapper> mapOreBedrockWrapper = new HashMap<String, OreBedrockWrapper>();
    
    public GT6OreBedrockHelper() {
        for (WorldgenObject worldGenOreBedrock : Worldgenerator.sWorldgenList)
            if (worldGenOreBedrock.mWorldGenName.startsWith("ore.bedrock.") && worldGenOreBedrock instanceof Worldgen_GT_Ore_Bedrock)
                mapOreBedrockWrapper.put(worldGenOreBedrock.mWorldGenName, new OreBedrockWrapper((Worldgen_GT_Ore_Bedrock)worldGenOreBedrock));         
    }
    
    public class OreBedrockWrapper {
        public String veinName;
        public short meta;
        public int probability;
        public boolean genOverworld = false;
        public boolean genNether = false;
        public boolean genEnd = false;

        public OreBedrockWrapper(Worldgen_GT_Ore_Bedrock worldGen) {
            this.veinName = worldGen.mWorldGenName;
            this.meta = worldGen.mMaterial.mID;
            this.probability = worldGen.mProbability;
            this.genOverworld = worldGen.mOverworld;
            this.genNether = worldGen.mNether;
            this.genEnd = worldGen.mEnd;
        } 
    }
    
}
