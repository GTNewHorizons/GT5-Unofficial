package pers.gwyog.gtneioreplugin.util;

import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.ANUBIS;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.ASTEROIDS;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.BARNARDC;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.BARNARDE;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.BARNARDF;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.CALLISTO;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.CENTAURIA;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.CERES;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.DEEPDARK;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.DEIMOS;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.ENCELADUS;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.ENDASTEROIDS;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.EUROPA;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.GANYMEDE;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.HAUMEA;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.HORUS;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.IO;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.KUIPERBELT;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.MAAHES;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.MAKEMAKE;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.MARS;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.MEHENBELT;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.MERCURY;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.MIRANDA;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.MOON;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.NEPER;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.OBERON;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.PHOBOS;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.PLUTO;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.PROTEUS;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.SETH;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.TCETIE;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.TITAN;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.TRITON;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.VEGAB;
import static bloodasp.galacticgreg.api.enums.DimensionDef.DimNames.VENUS;
import static pers.gwyog.gtneioreplugin.util.OreVeinLayer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gregtech.api.enums.OreMixes;
import gregtech.common.OreMixBuilder;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;

public class GT5OreLayerHelper {

    public static class NormalOreDimensionWrapper {

        public final ArrayList<OreLayerWrapper> internalDimOreList = new ArrayList<>();
        public final HashMap<OreLayerWrapper, Double> oreVeinToProbabilityInDimension = new HashMap<>();

        // Calculate all weights of ore veins once dimension is initialised.
        private void calculateWeights() {
            int totalWeight = 0;
            for (OreLayerWrapper oreVein : internalDimOreList) {
                totalWeight += oreVein.randomWeight;
            }
            for (OreLayerWrapper oreVein : internalDimOreList) {
                oreVeinToProbabilityInDimension.put(oreVein, ((double) oreVein.randomWeight) / ((double) totalWeight));
            }
        }
    }

    private static final int DIMENSION_COUNT = 33;
    public static final Integer[] weightPerWorld = new Integer[DIMENSION_COUNT];
    public static final Integer[] DimIDs = new Integer[DIMENSION_COUNT];
    public static final HashMap<String, OreLayerWrapper> mapOreLayerWrapper = new HashMap<>();
    public static final HashMap<OreLayerWrapper, Map<String, Boolean>> bufferedDims = new HashMap<>();
    public static final HashMap<String, NormalOreDimensionWrapper> dimToOreWrapper = new HashMap<>();

    public static void init() {
        Arrays.fill(weightPerWorld, 0);
        Arrays.fill(DimIDs, 0);
        for (OreMixes mix : OreMixes.values())
            mapOreLayerWrapper.put(mix.oreMixBuilder.oreMixName, new OreLayerWrapper(mix.oreMixBuilder));
        for (OreLayerWrapper layer : mapOreLayerWrapper.values()) {
            bufferedDims.put(layer, getDims(layer));
        }

        // --- Handling of dimToOreWrapper ---

        // Get dims as "Ow,Ne,Ma" etc.
        bufferedDims.forEach((veinInfo, dims) -> {


            for ( String dim : dims.keySet()){
                NormalOreDimensionWrapper dimensionOres = dimToOreWrapper.getOrDefault(dim, new NormalOreDimensionWrapper());
                dimensionOres.internalDimOreList.add(veinInfo);
                dimToOreWrapper.put(dim, dimensionOres);
            }

            // Calculate probabilities for each dim.
            for (String dim : dimToOreWrapper.keySet()) {
                dimToOreWrapper.get(dim)
                    .calculateWeights();
            }
        });
        // --- End of handling for dimToOreWrapper ---
    }

    public static Map<String, Boolean> getDims(OreLayerWrapper oreLayer) {
        Map<String, Boolean> enabledDims = new HashMap<>();
        Map<String, Boolean> origNames = oreLayer.allowedDimWithOrigNames;

        for (String dimName : origNames.keySet()){
            String abbr = getDimAbbreviatedName(dimName);
            if (!origNames.getOrDefault(dimName, false)){
                continue;
            }
            enabledDims.put(abbr,true);
        }
        return enabledDims;
    }



    public static String getDimAbbreviatedName(String dimName){
        String abbreviatedName;
        switch (dimName){
            case (OreMixBuilder.OW) -> abbreviatedName="Ow";  // Overworld
            case OreMixBuilder.NETHER -> abbreviatedName=  "Ne"; // Nether
            case OreMixBuilder.TWILIGHT_FOREST -> abbreviatedName= "TF"; // Twilight
            case OreMixBuilder.THE_END -> abbreviatedName= "ED"; // TheEnd because En = Encalus
            // "VA" seems to be another occurence of the end asteroid dims see Vanilla_EndAsteroids and EndAsteroid in old WorldGeneration.cfg
            case ENDASTEROIDS -> abbreviatedName= "EA"; // EndAsteroid
            // T1
            case MOON -> abbreviatedName= "Mo"; // GalacticraftCore_Moon
            // T2
            case DEIMOS -> abbreviatedName= "De"; // GalaxySpace_Deimos
            case MARS -> abbreviatedName= "Ma"; // GalacticraftMars_Mars
            case PHOBOS -> abbreviatedName= "Ph"; // GalaxySpace_Phobos
            // T3
            case ASTEROIDS -> abbreviatedName= "As"; // GalacticraftMars_Asteroids
            case CALLISTO -> abbreviatedName= "Ca"; // GalaxySpace_Callisto
            case CERES -> abbreviatedName= "Ce"; // GalaxySpace_Ceres
            case EUROPA -> abbreviatedName= "Eu"; // GalaxySpace_Europa
            case GANYMEDE -> abbreviatedName= "Ga"; // GalaxySpace_Ganymede
            // todo case  -> abbreviatedName= "Rb"; // Ross128b
            // T4
            case IO -> abbreviatedName= "Io"; // GalaxySpace_Io
            case MERCURY -> abbreviatedName= "Me"; // GalaxySpace_Mercury
            case VENUS -> abbreviatedName= "Ve"; // GalaxySpace_Venus
            // T5
            case ENCELADUS -> abbreviatedName= "En"; // GalaxySpace_Enceladus
            case MIRANDA -> abbreviatedName= "Mi"; // GalaxySpace_Miranda
            case OBERON -> abbreviatedName= "Ob"; // GalaxySpace_Oberon
            case TITAN -> abbreviatedName= "Ti"; // GalaxySpace_Titan
            // todo case  -> abbreviatedName= "Ra"; // Ross128ba
            // T6
            case PROTEUS -> abbreviatedName= "Pr"; // GalaxySpace_Proteus
            case TRITON -> abbreviatedName= "Tr"; // GalaxySpace_Triton
            // T7
            case HAUMEA -> abbreviatedName= "Ha"; // GalaxySpace_Haumea
            case KUIPERBELT -> abbreviatedName= "KB"; // GalaxySpace_Kuiperbelt
            case MAKEMAKE -> abbreviatedName= "MM"; // GalaxySpace_MakeMake
            case PLUTO -> abbreviatedName= "Pl"; // GalaxySpace_Pluto
            // T8
            case BARNARDC -> abbreviatedName= "BC"; // GalaxySpace_BarnardC
            case BARNARDE -> abbreviatedName= "BE"; // GalaxySpace_BarnardE
            case BARNARDF -> abbreviatedName= "BF"; // GalaxySpace_BarnardF
            case CENTAURIA -> abbreviatedName= "CB"; // GalaxySpace_CentauriA is actually α Centauri Bb
            case TCETIE -> abbreviatedName= "TE"; // GalaxySpace_TcetiE
            case VEGAB -> abbreviatedName= "VB"; // GalaxySpace_VegaB
            // T9
            case ANUBIS -> abbreviatedName= "An"; // GalacticraftAmunRa_Anubis
            case HORUS -> abbreviatedName= "Ho"; // GalacticraftAmunRa_Horus
            case MAAHES -> abbreviatedName= "Mh"; // GalacticraftAmunRa_Maahes
            case MEHENBELT -> abbreviatedName= "MB"; // GalacticraftAmunRa_MehenBelt
            case NEPER -> abbreviatedName= "Np"; // GalacticraftAmunRa_Neper
            case SETH -> abbreviatedName= "Se"; // GalacticraftAmunRa_Seth
            // T10
            case DEEPDARK -> abbreviatedName= "DD"; // Underdark
            default -> {throw new IllegalStateException("String: "+dimName+" has no abbredged name!");}
        }
        return abbreviatedName;
    }

    public static class OreLayerWrapper {

        public final String veinName, worldGenHeightRange;
        public final short[] Meta = new short[4];
        public final short randomWeight, size, density;
        public final Map<String, Boolean> allowedDimWithOrigNames;

        public final Materials mPrimaryVeinMaterial;
        public final Materials mSecondaryMaterial;
        public final Materials mBetweenMaterial;
        public final Materials mSporadicMaterial;

        public OreLayerWrapper(OreMixBuilder mix){
            this.veinName = mix.oreMixName;
            this.Meta[0] = (short) mix.primary.mMetaItemSubID;
            this.Meta[1] = (short) mix.secondary.mMetaItemSubID;
            this.Meta[2] = (short) mix.between.mMetaItemSubID;
            this.Meta[3] = (short) mix.sporadic.mMetaItemSubID;

            this.mPrimaryVeinMaterial = mix.primary;
            this.mSecondaryMaterial = mix.secondary;
            this.mBetweenMaterial = mix.between;
            this.mSporadicMaterial = mix.sporadic;

            this.size = (short) mix.size;
            this.density = (short) mix.density;
            this.worldGenHeightRange = mix.minY + "-" + mix.maxY;
            this.randomWeight = (short) mix.weight;

            this.allowedDimWithOrigNames = mix.dimsEnabled;
        }

        public List<ItemStack> getVeinLayerOre(int maximumMaterialIndex, int veinLayer) {
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < maximumMaterialIndex; i++) {
                stackList.add(getLayerOre(veinLayer, i));
            }
            return stackList;
        }

        public ItemStack getLayerOre(int veinLayer, int materialIndex) {
            return new ItemStack(GregTech_API.sBlockOres1, 1, Meta[veinLayer] + materialIndex * 1000);
        }

        public boolean containsOre(short materialIndex) {
            return Meta[VEIN_PRIMARY] == materialIndex || Meta[VEIN_SECONDARY] == materialIndex
                || Meta[VEIN_BETWEEN] == materialIndex
                || Meta[VEIN_SPORADIC] == materialIndex;
        }
    }
}
