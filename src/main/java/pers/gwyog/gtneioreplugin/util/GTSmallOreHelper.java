package pers.gwyog.gtneioreplugin.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class GTSmallOreHelper {
    public static boolean restrictBiomeSupport = false;
    public static boolean gcBasicSupport = false;
    public static List<ItemStack> smallOreList = new ArrayList<ItemStack>();
    public static HashMap<String, SmallOreWrapper> mapSmallOreWrapper = new HashMap<String, SmallOreWrapper>();
    public static HashMap<String, Short> mapOreDropUnlocalizedNameToOreMeta = new HashMap<String, Short>();
    public static HashMap<Short, List<ItemStack>> mapOreMetaToOreDrops = new HashMap<Short, List<ItemStack>>();    
    
    public GTSmallOreHelper() {
        checkExtraSupport();
        ItemStack stack;
        Materials material;
        short meta;
        for (GT_Worldgen worldGen : GregTech_API.sWorldgenList)
            if (worldGen.mWorldGenName.startsWith("ore.small.") && worldGen instanceof GT_Worldgen_GT_Ore_SmallPieces) {
                GT_Worldgen_GT_Ore_SmallPieces worldGenSmallPieces = (GT_Worldgen_GT_Ore_SmallPieces)worldGen;
                meta = worldGenSmallPieces.mMeta;
                material = GregTech_API.sGeneratedMaterials[meta];
                mapSmallOreWrapper.put(worldGen.mWorldGenName, new SmallOreWrapper(worldGenSmallPieces));
                if (!mapOreMetaToOreDrops.keySet().contains(meta)) {
                    List<ItemStack> stackList = new ArrayList<ItemStack>();
                    stack = GT_OreDictUnificator.get(OrePrefixes.gemExquisite, material, GT_OreDictUnificator.get(OrePrefixes.gem, material, 1L), 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = GT_OreDictUnificator.get(OrePrefixes.gemFlawless, material, GT_OreDictUnificator.get(OrePrefixes.gem, material, 1L), 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = GT_OreDictUnificator.get(OrePrefixes.gem, material, 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = GT_OreDictUnificator.get(OrePrefixes.gemFlawed, material, GT_OreDictUnificator.get(OrePrefixes.crushed, material, 1L), 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = GT_OreDictUnificator.get(OrePrefixes.crushed, material, 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = GT_OreDictUnificator.get(OrePrefixes.gemChipped, material, GT_OreDictUnificator.get(OrePrefixes.dustImpure, material, 1L), 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    stack = GT_OreDictUnificator.get(OrePrefixes.dustImpure, material, 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);stackList.add(stack);}
                    smallOreList.add(new ItemStack(GregTech_API.sBlockOres1, 1, meta+16000));
                    mapOreMetaToOreDrops.put(meta, stackList);
                }
            }
    }
    
    private static void checkExtraSupport() {
        Class clazzGTSmallOre = null;
        try {
            clazzGTSmallOre = Class.forName("gregtech.common.GT_Worldgen_GT_Ore_SmallPieces");
        } catch (ClassNotFoundException e) {}
        if (clazzGTSmallOre != null) {
            try {
                Field fieldRestrictBiome = clazzGTSmallOre.getField("mRestrictBiome");;
                restrictBiomeSupport = true;
            } catch (Exception e) {}
            try {
                Field fieldGCMoon = clazzGTSmallOre.getField("mMoon");
                Field fieldGCMars = clazzGTSmallOre.getField("mMars");
                gcBasicSupport = true;
            } catch (Exception e) {}
        }
    }
    
    public static Materials[] getDroppedDusts() {
        return new Materials[]{Materials.Stone, Materials.Netherrack, Materials.Endstone, Materials.GraniteBlack, Materials.GraniteRed, Materials.Marble, Materials.Basalt, Materials.Stone};
    }
    
    public class SmallOreWrapper {
        public String oreGenName;
        public short oreMeta;
        public String worldGenHeightRange;
        public short amountPerChunk;
        public String restrictBiome;
        public boolean genOverworld = false;
        public boolean genNether = false;
        public boolean genEnd = false;
        public boolean genMoon = false;
        public boolean genMars = false;
        
        public SmallOreWrapper(GT_Worldgen_GT_Ore_SmallPieces worldGen) {
            this.oreGenName = worldGen.mWorldGenName;
            this.oreMeta = worldGen.mMeta;
            this.worldGenHeightRange = worldGen.mMinY + "-" + worldGen.mMaxY;
            this.amountPerChunk = worldGen.mAmount;
            this.genOverworld = worldGen.mOverworld;
            this.genNether = worldGen.mNether;
            this.genEnd = worldGen.mEnd;
            if (GTSmallOreHelper.restrictBiomeSupport)
                this.restrictBiome = worldGen.mRestrictBiome;
            if (GTSmallOreHelper.gcBasicSupport) {
                this.genMoon = worldGen.mMoon;
                this.genMars = worldGen.mMars;
            }
        }
    }
}
