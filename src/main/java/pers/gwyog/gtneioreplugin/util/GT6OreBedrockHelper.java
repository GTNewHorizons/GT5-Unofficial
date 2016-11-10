package pers.gwyog.gtneioreplugin.util;

import java.util.HashMap;

import gregapi.data.CS;
import gregapi.data.OP;
import gregapi.lang.LanguageHandler;
import gregapi.oredict.OreDictMaterial;
import gregapi.worldgen.WorldgenObject;
import gregapi.worldgen.Worldgen_GT_Ore_Bedrock;
import gregapi.worldgen.Worldgenerator;
import net.minecraft.client.resources.I18n;

public class GT6OreBedrockHelper {
    public static HashMap<String, OreBedrockWrapper> mapOreBedrockWrapper = new HashMap<String, OreBedrockWrapper>();
    public static HashMap<Short, String> mapMetaToLocalizedName = new HashMap<Short, String>();
    
    public GT6OreBedrockHelper() {
        for (WorldgenObject worldGenOreBedrock : Worldgenerator.sWorldgenList)
            if (worldGenOreBedrock.mWorldGenName.startsWith("ore.bedrock.") && worldGenOreBedrock instanceof Worldgen_GT_Ore_Bedrock)
                mapOreBedrockWrapper.put(worldGenOreBedrock.mWorldGenName, new OreBedrockWrapper((Worldgen_GT_Ore_Bedrock)worldGenOreBedrock));         
    }
    
    public static String getLocalizedOreBedrockName(short meta) {
        // meta==-1 means OreDictMaterial is MT.NULL
        if (meta==-1)
            return I18n.format("gtnop.ore.null.name");
        else
            return LanguageHandler.getLocalName(OP.oreBedrock, CS.BlocksGT.oreBedrock.getMetaMaterial(meta));
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
            if (!GT6OreLayerHelper.mapMetaToLocalizedName.keySet().contains(meta))
                GT6OreLayerHelper.mapMetaToLocalizedName.put(meta, GT6OreLayerHelper.getLocalizedOreName(meta));
            if (!GT6OreSmallHelper.mapMetaToLocalizedName.keySet().contains(meta))
                GT6OreSmallHelper.mapMetaToLocalizedName.put(meta, GT6OreSmallHelper.getLocalizedSmallOreName(meta));
            if (!mapMetaToLocalizedName.keySet().contains(meta))
                mapMetaToLocalizedName.put(meta, getLocalizedOreBedrockName(meta));
        } 
    }
    
}
