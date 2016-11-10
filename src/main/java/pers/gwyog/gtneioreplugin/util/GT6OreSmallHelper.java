package pers.gwyog.gtneioreplugin.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gregapi.block.IBlockPlacable;
import gregapi.block.prefixblock.PrefixBlock;
import gregapi.data.CS;
import gregapi.data.MT;
import gregapi.data.OP;
import gregapi.lang.LanguageHandler;
import gregapi.oredict.OreDictMaterial;
import gregapi.worldgen.WorldgenObject;
import gregapi.worldgen.Worldgen_GT_Ore_SmallPieces;
import gregapi.worldgen.Worldgenerator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;

public class GT6OreSmallHelper {
    public static List<ItemStack> oreSmallList = new ArrayList<ItemStack>();
    public static HashMap<String, OreSmallWrapper> mapOreSmallWrapper = new HashMap<String, OreSmallWrapper>();
    public static HashMap<String, Short> mapOreDropUnlocalizedNameToOreMeta = new HashMap<String, Short>();
    public static HashMap<Short, List<ItemStack>> mapOreMetaToOreDrops = new HashMap<Short, List<ItemStack>>();    
    public static HashMap<Short, String> mapMetaToLocalizedName = new HashMap<Short, String>();
    public static Set<PrefixBlock> setOreSmallBasicTypes = new HashSet<PrefixBlock>();
    
    public GT6OreSmallHelper() {
        ItemStack stack;
        OreDictMaterial material;
        PrefixBlock oreSmall = CS.BlocksGT.oreSmall;
        short meta;
        for (WorldgenObject worldGen : Worldgenerator.sWorldgenList)
            if (worldGen.mWorldGenName.startsWith("ore.small.") && worldGen instanceof Worldgen_GT_Ore_SmallPieces) {
                Worldgen_GT_Ore_SmallPieces worldGenSmallPieces = (Worldgen_GT_Ore_SmallPieces)worldGen;
                meta = worldGenSmallPieces.mMeta;
                material = oreSmall.getMetaMaterial(meta);
                mapOreSmallWrapper.put(worldGen.mWorldGenName, new OreSmallWrapper(worldGenSmallPieces));
                if (!mapOreMetaToOreDrops.keySet().contains(meta)) {
                    List<ItemStack> stackList = new ArrayList<ItemStack>();
                    stack = OP.gemExquisite.mat(material, OP.gem.mat(material, 1L), 1L); 
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = OP.gemFlawless.mat(material, OP.gem.mat(material, 1L), 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = OP.gem.mat(material, 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = OP.gemFlawed.mat(material, OP.crushed.mat(material, 1L), 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = OP.crushed.mat(material, 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = OP.gemChipped.mat(material, OP.dustImpure.mat(material, 1L), 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = OP.dustImpure.mat(material, OP.dust.mat(material, 1L), 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = OP.gemLegendary.mat(material, 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    oreSmallList.add(new ItemStack(oreSmall, 1, meta));
                    mapOreMetaToOreDrops.put(meta, stackList);
                }
                mapMetaToLocalizedName.put(meta, getLocalizedSmallOreName(meta));
                Set<IBlockPlacable> setSmallOreBasicTypesTemp = new HashSet<IBlockPlacable>(CS.BlocksGT.stoneToSmallOres.values());
                for (IBlockPlacable block: setSmallOreBasicTypesTemp)
                    if (block instanceof PrefixBlock)
                        setOreSmallBasicTypes.add((PrefixBlock)block);       
            }
    }
    
    public static String getLocalizedSmallOreName(short meta) {
        // meta==-1 means OreDictMaterial is MT.NULL
        if (meta==-1)
            return I18n.format("gtnop.ore.null.name");
        else
            return LanguageHandler.getLocalName(OP.oreSmall, CS.BlocksGT.oreSmall.getMetaMaterial(meta));
    }
    
    public class OreSmallWrapper {
        public String oreGenName;
        public short oreMeta;
        public String worldGenHeightRange;
        public String amountPerChunk;
        public boolean genOverworld = false;
        public boolean genNether = false;
        public boolean genEnd = false;
        
        public OreSmallWrapper(Worldgen_GT_Ore_SmallPieces worldGen) {
            this.oreGenName = worldGen.mWorldGenName;
            this.oreMeta = worldGen.mMeta;
            this.worldGenHeightRange = worldGen.mMinY + "-" + worldGen.mMaxY;
            this.amountPerChunk = worldGen.mAmount/2 + "-" + (worldGen.mAmount/2 + (worldGen.mAmount+1)/2);
            this.genOverworld = worldGen.mOverworld;
            this.genNether = worldGen.mNether;
            this.genEnd = worldGen.mEnd;
        }
    }
    
}
