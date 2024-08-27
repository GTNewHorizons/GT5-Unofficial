package gregtech.api.objects;

import static gregtech.api.enums.Dimensions.AlphaCentauriBb;
import static gregtech.api.enums.Dimensions.BarnardaC;
import static gregtech.api.enums.Dimensions.BarnardaE;
import static gregtech.api.enums.Dimensions.BarnardaF;
import static gregtech.api.enums.Dimensions.Callisto;
import static gregtech.api.enums.Dimensions.Europa;
import static gregtech.api.enums.Dimensions.Io;
import static gregtech.api.enums.Dimensions.Makemake;
import static gregtech.api.enums.Dimensions.Mars;
import static gregtech.api.enums.Dimensions.Mercury;
import static gregtech.api.enums.Dimensions.Miranda;
import static gregtech.api.enums.Dimensions.Moon;
import static gregtech.api.enums.Dimensions.Oberon;
import static gregtech.api.enums.Dimensions.Overworld;
import static gregtech.api.enums.Dimensions.Pluto;
import static gregtech.api.enums.Dimensions.Proteus;
import static gregtech.api.enums.Dimensions.Ross128b;
import static gregtech.api.enums.Dimensions.Ross128ba;
import static gregtech.api.enums.Dimensions.TCetiE;
import static gregtech.api.enums.Dimensions.Titan;
import static gregtech.api.enums.Dimensions.Triton;
import static gregtech.api.enums.Dimensions.Venus;
import static gregtech.api.enums.UndergroundFluidNames.carbonDioxide;
import static gregtech.api.enums.UndergroundFluidNames.carbonMonoxide;
import static gregtech.api.enums.UndergroundFluidNames.chlorobenzene;
import static gregtech.api.enums.UndergroundFluidNames.deuterium;
import static gregtech.api.enums.UndergroundFluidNames.distilledWater;
import static gregtech.api.enums.UndergroundFluidNames.ethane;
import static gregtech.api.enums.UndergroundFluidNames.ethylene;
import static gregtech.api.enums.UndergroundFluidNames.fluorine;
import static gregtech.api.enums.UndergroundFluidNames.heavyOil;
import static gregtech.api.enums.UndergroundFluidNames.helium3;
import static gregtech.api.enums.UndergroundFluidNames.hydrofluoricAcid;
import static gregtech.api.enums.UndergroundFluidNames.hydrogen;
import static gregtech.api.enums.UndergroundFluidNames.hydrogenSulfide;
import static gregtech.api.enums.UndergroundFluidNames.lava;
import static gregtech.api.enums.UndergroundFluidNames.lightOil;
import static gregtech.api.enums.UndergroundFluidNames.liquidAir;
import static gregtech.api.enums.UndergroundFluidNames.mediumOil;
import static gregtech.api.enums.UndergroundFluidNames.methane;
import static gregtech.api.enums.UndergroundFluidNames.moltenCopper;
import static gregtech.api.enums.UndergroundFluidNames.moltenIron;
import static gregtech.api.enums.UndergroundFluidNames.moltenLead;
import static gregtech.api.enums.UndergroundFluidNames.moltenTin;
import static gregtech.api.enums.UndergroundFluidNames.naturalGas;
import static gregtech.api.enums.UndergroundFluidNames.nitrogen;
import static gregtech.api.enums.UndergroundFluidNames.oil;
import static gregtech.api.enums.UndergroundFluidNames.oxygen;
import static gregtech.api.enums.UndergroundFluidNames.saltWater;
import static gregtech.api.enums.UndergroundFluidNames.sulfuricAcid;
import static gregtech.api.enums.UndergroundFluidNames.unknownWater;
import static gregtech.api.enums.UndergroundFluidNames.veryHeavyOil;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import gregtech.api.enums.Dimensions;
import gregtech.api.enums.UndergroundFluidNames;

public class GT_UO_DimensionList {

    private Configuration fConfig;
    private String fCategory;
    private final BiMap<String, GT_UO_Dimension> fDimensionList;

    public int[] blackList = new int[0];

    public GT_UO_DimensionList() {
        fDimensionList = HashBiMap.create();
    }

    public void save() {
        fConfig.save();
    }

    public GT_UO_Dimension GetDimension(int aDimension) {
        if (CheckBlackList(aDimension)) return null;
        if (fDimensionList.containsKey(Integer.toString(aDimension)))
            return fDimensionList.get(Integer.toString(aDimension));
        for (BiMap.Entry<String, GT_UO_Dimension> dl : fDimensionList.entrySet())
            if (DimensionManager.getProvider(aDimension)
                .getClass()
                .getName()
                .contains(dl.getValue().Dimension)) return dl.getValue();
        return fDimensionList.get("Default");
    }

    private boolean CheckBlackList(int aDimensionId) {
        try {
            return java.util.Arrays.binarySearch(blackList, aDimensionId) >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void SetConfigValues(String aDimensionName, String aDimension, String aName, String aRegistry,
        int aMinAmount, int aMaxAmount, int aChance, int aDecreasePerOperationAmount) {
        String Category = fCategory + "." + aDimensionName;
        fConfig.get(Category, "Dimension", aDimension)
            .getString();
        Category += "." + aName;
        fConfig.get(Category, "Registry", aRegistry)
            .getString();
        fConfig.get(Category, "MinAmount", aMinAmount)
            .getInt(aMinAmount);
        fConfig.get(Category, "MaxAmount", aMaxAmount)
            .getInt(aMaxAmount);
        fConfig.get(Category, "Chance", aChance)
            .getInt(aChance);
        fConfig.get(Category, "DecreasePerOperationAmount", aDecreasePerOperationAmount)
            .getInt(aDecreasePerOperationAmount);
        // IT IS IN BUCKETS!!!
    }

    private void setOverworldValues() {
        new ConfigSetter().dimension(Overworld)
            .fluid(naturalGas)
            .chance(20)
            .decreaseAmount(5)
            .maxAmount(350)
            .minAmount(10)
            .writeToConfig();

        new ConfigSetter().dimension(Overworld)
            .fluid(lightOil)
            .chance(20)
            .decreaseAmount(5)
            .maxAmount(350)
            .minAmount(10)
            .writeToConfig();

        new ConfigSetter().dimension(Overworld)
            .fluid(mediumOil)
            .chance(20)
            .decreaseAmount(5)
            .maxAmount(625)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Overworld)
            .fluid(heavyOil)
            .chance(20)
            .decreaseAmount(5)
            .maxAmount(625)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Overworld)
            .fluid(oil)
            .chance(20)
            .decreaseAmount(5)
            .maxAmount(625)
            .minAmount(0)
            .writeToConfig();
    }

    private void setMoonValues() {
        new ConfigSetter().dimension(Moon)
            .fluid(helium3)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(425)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Moon)
            .fluid(saltWater)
            .chance(20)
            .decreaseAmount(5)
            .maxAmount(200)
            .minAmount(0)
            .writeToConfig();
    }

    private void setMercuryValues() {
        new ConfigSetter().dimension(Mercury)
            .fluid(helium3)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(800)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Mercury)
            .fluid(moltenIron)
            .chance(30)
            .decreaseAmount(5)
            .maxAmount(400)
            .minAmount(0)
            .writeToConfig();
    }

    private void setVenusValues() {
        new ConfigSetter().dimension(Venus)
            .fluid(moltenLead)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(1600)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Venus)
            .fluid(sulfuricAcid)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(250)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Venus)
            .fluid(carbonDioxide)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(1500)
            .minAmount(0)
            .writeToConfig();
    }

    private void setMarsValues() {
        new ConfigSetter().dimension(Mars)
            .fluid(saltWater)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(400)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Mars)
            .fluid(chlorobenzene)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(400)
            .minAmount(0)
            .writeToConfig();
    }

    private void setIoValues() {
        new ConfigSetter().dimension(Io)
            .fluid(moltenLead)
            .chance(20)
            .decreaseAmount(5)
            .maxAmount(650)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Io)
            .fluid(sulfuricAcid)
            .chance(80)
            .decreaseAmount(5)
            .maxAmount(350)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Io)
            .fluid(carbonDioxide)
            .chance(80)
            .decreaseAmount(5)
            .maxAmount(750)
            .minAmount(0)
            .writeToConfig();
    }

    private void setEuropaValues() {
        new ConfigSetter().dimension(Europa)
            .fluid(saltWater)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(800)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Europa)
            .fluid(veryHeavyOil)
            .chance(20)
            .decreaseAmount(5)
            .maxAmount(200)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Europa)
            .fluid(distilledWater)
            .chance(80)
            .decreaseAmount(5)
            .maxAmount(3500)
            .minAmount(0)
            .writeToConfig();
    }

    private void setCallistoValues() {
        new ConfigSetter().dimension(Callisto)
            .fluid(oxygen)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(200)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Callisto)
            .fluid(liquidAir)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(200)
            .minAmount(0)
            .writeToConfig();
    }

    private void setTitanValues() {
        new ConfigSetter().dimension(Titan)
            .fluid(methane)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(800)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Titan)
            .fluid(ethane)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(200)
            .minAmount(0)
            .writeToConfig();
    }

    private void setMirandaValues() {
        new ConfigSetter().dimension(Miranda)
            .fluid(hydrogenSulfide)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(900)
            .minAmount(0)
            .writeToConfig();
    }

    private void setOberonValues() {
        new ConfigSetter().dimension(Oberon)
            .fluid(carbonMonoxide)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(2000)
            .minAmount(0)
            .writeToConfig();
    }

    private void setTritonValues() {
        new ConfigSetter().dimension(Triton)
            .fluid(nitrogen)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(800)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Triton)
            .fluid(ethylene)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(800)
            .minAmount(0)
            .writeToConfig();
    }

    private void setProteusValues() {
        new ConfigSetter().dimension(Proteus)
            .fluid(deuterium)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(700)
            .minAmount(0)
            .writeToConfig();
    }

    private void setPlutoValues() {
        new ConfigSetter().dimension(Pluto)
            .fluid(nitrogen)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(800)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Pluto)
            .fluid(oxygen)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(800)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Pluto)
            .fluid(liquidAir)
            .chance(40)
            .decreaseAmount(5)
            .maxAmount(300)
            .minAmount(4)
            .writeToConfig();

        new ConfigSetter().dimension(Pluto)
            .fluid(fluorine)
            .chance(80)
            .decreaseAmount(5)
            .maxAmount(800)
            .minAmount(4)
            .writeToConfig();
    }

    private void setMakeMakeValues() {
        new ConfigSetter().dimension(Makemake)
            .fluid(hydrofluoricAcid)
            .chance(80)
            .decreaseAmount(5)
            .maxAmount(300)
            .minAmount(0)
            .writeToConfig();
    }

    private void setAlphaCentauriBBValues() {
        new ConfigSetter().dimension(AlphaCentauriBb)
            .fluid(moltenCopper)
            .chance(10)
            .decreaseAmount(5)
            .maxAmount(300)
            .minAmount(0)
            .writeToConfig();
    }

    private void setBarnardaCValues() {
        new ConfigSetter().dimension(BarnardaC)
            .fluid(veryHeavyOil)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(800)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(BarnardaC)
            .fluid(unknownWater)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(300)
            .minAmount(0)
            .writeToConfig();
    }

    private void setBarnardaEValues() {
        new ConfigSetter().dimension(BarnardaE)
            .fluid(liquidAir)
            .chance(20)
            .decreaseAmount(5)
            .maxAmount(400)
            .minAmount(0)
            .writeToConfig();
    }

    private void setBarnardaFValues() {
        new ConfigSetter().dimension(BarnardaF)
            .fluid(moltenTin)
            .chance(15)
            .decreaseAmount(5)
            .maxAmount(400)
            .minAmount(0)
            .writeToConfig();
    }

    private void setTcetiEValues() {
        new ConfigSetter().dimension(TCetiE)
            .fluid(veryHeavyOil)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(200)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(TCetiE)
            .fluid(hydrogen)
            .chance(50)
            .decreaseAmount(5)
            .maxAmount(700)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(TCetiE)
            .fluid(distilledWater)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(10_000)
            .minAmount(0)
            .writeToConfig();
    }

    private void setRoss128bValues() {
        new ConfigSetter().dimension(Ross128b)
            .fluid(veryHeavyOil)
            .chance(40)
            .decreaseAmount(5)
            .maxAmount(625)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Ross128b)
            .fluid(lava)
            .chance(5)
            .decreaseAmount(5)
            .maxAmount(820)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Ross128b)
            .fluid(naturalGas)
            .chance(65)
            .decreaseAmount(5)
            .maxAmount(625)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Ross128b)
            .fluid(distilledWater)
            .chance(100)
            .decreaseAmount(5)
            .maxAmount(5000)
            .minAmount(0)
            .writeToConfig();
    }

    private void setRoss128baValues() {
        new ConfigSetter().dimension(Ross128ba)
            .fluid(saltWater)
            .chance(40)
            .decreaseAmount(5)
            .maxAmount(1250)
            .minAmount(0)
            .writeToConfig();

        new ConfigSetter().dimension(Ross128ba)
            .fluid(helium3)
            .chance(60)
            .decreaseAmount(5)
            .maxAmount(1250)
            .minAmount(0)
            .writeToConfig();
    }

    public void setDefaultValues() {
        setOverworldValues();
        setMoonValues();
        setMercuryValues();
        setVenusValues();
        setMarsValues();
        setIoValues();
        setEuropaValues();
        setCallistoValues();
        setTitanValues();
        setMirandaValues();
        setOberonValues();
        setTritonValues();
        setProteusValues();
        setPlutoValues();
        setMakeMakeValues();
        setAlphaCentauriBBValues();
        setBarnardaCValues();
        setBarnardaEValues();
        setBarnardaFValues();
        setTcetiEValues();
        setRoss128bValues();
        setRoss128baValues();
    }

    public void getConfig(Configuration aConfig, String aCategory) {
        fCategory = aCategory;
        fConfig = aConfig;
        if (!fConfig.hasCategory(fCategory)) setDefaultValues();

        blackList = new int[] { -1, 1 };
        blackList = aConfig.get(fCategory, "DimBlackList", blackList, "Dimension IDs Black List")
            .getIntList();
        java.util.Arrays.sort(blackList);

        for (int i = 0; i < fConfig.getCategory(fCategory)
            .getChildren()
            .size(); i++) {
            GT_UO_Dimension Dimension = new GT_UO_Dimension(
                (ConfigCategory) fConfig.getCategory(fCategory)
                    .getChildren()
                    .toArray()[i]);
            fDimensionList.put(Dimension.Dimension, Dimension);
        }
        save();
    }

    public class ConfigSetter {

        private int chance;
        private int decreaseAmount;
        private int maxAmount;
        private int minAmount;
        private UndergroundFluidNames fluid;
        private Dimensions dim;

        public ConfigSetter chance(int chance) {
            this.chance = chance;
            return this;
        }

        public ConfigSetter decreaseAmount(int decreaseAmount) {
            this.decreaseAmount = decreaseAmount;
            return this;
        }

        public ConfigSetter maxAmount(int maxAmount) {
            this.maxAmount = maxAmount;
            return this;
        }

        public ConfigSetter minAmount(int minAmount) {
            this.minAmount = minAmount;
            return this;
        }

        public ConfigSetter fluid(UndergroundFluidNames fluid) {
            this.fluid = fluid;
            return this;
        }

        public ConfigSetter dimension(Dimensions dim) {
            this.dim = dim;
            return this;
        }

        public void writeToConfig() {
            SetConfigValues(
                dim.toString(),
                dim.id,
                fluid.toString(),
                fluid.name,
                minAmount,
                maxAmount,
                chance,
                decreaseAmount);
        }
    }
}
