package pers.gwyog.gtneioreplugin.util;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5VeinStat;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper.OreLayerWrapper;

public class CSVMaker implements Runnable {

    public CSVMaker() {}

    public static List<Oremix> Combsort(List<Oremix> list) {
        try {
            List<Oremix> list2 = new ArrayList<>(list.size());
            list2.addAll(list);

            int step = list2.size();
            boolean swapped;
            do {
                swapped = false;
                if (step > 1) {
                    step = (int) (step / 1.3);
                }
                for (int i = 0; i < list2.size() - step; i++) {
                    if (list2.get(i)
                        .getOreName()
                        .substring(0, 3)
                        .compareTo(
                            (list2.get(i + step)
                                .getOreName()
                                .substring(0, 3)))
                        > 0) {
                        Oremix tmp = list2.get(i);
                        list2.set(i, list2.get(i + step));
                        list2.set(i + step, tmp);
                        swapped = true;
                    }
                }
            } while (swapped || step > 1);
            return list2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void runSmallOres() {
        try {
            Iterator<Map.Entry<String, GT5OreSmallHelper.OreSmallWrapper>> it = GT5OreSmallHelper.mapOreSmallWrapper
                .entrySet()
                .iterator();
            List<Oremix> OreVeins = new ArrayList<>();
            while (it.hasNext()) {
                Oremix oremix = new Oremix();

                Map.Entry<String, GT5OreSmallHelper.OreSmallWrapper> pair = it.next();
                String Dims = GT5OreSmallHelper.bufferedDims.get(pair.getValue());
                GT5OreSmallHelper.OreSmallWrapper oreLayer = pair.getValue();
                oremix.setOreName(oreLayer.oreGenName.split("\\.")[2]);
                oremix.setHeight(oreLayer.worldGenHeightRange);
                oremix.setDensity(oreLayer.amountPerChunk);
                oremix.an = Dims.contains("An");
                oremix.as = Dims.contains("As");
                oremix.bc = Dims.contains("BC");
                oremix.be = Dims.contains("BE");
                oremix.bf = Dims.contains("BF");
                oremix.ca = Dims.contains("Ca");
                oremix.cb = Dims.contains("CA");
                oremix.ce = Dims.contains("Ce");
                oremix.dd = Dims.contains("DD");
                oremix.de = Dims.contains("De");
                oremix.ea = Dims.contains("EA");
                oremix.en = Dims.contains("En");
                oremix.eu = Dims.contains("Eu");
                oremix.ga = Dims.contains("Ga");
                oremix.ha = Dims.contains("Ha");
                oremix.ho = Dims.contains("Ho");
                oremix.io = Dims.contains("Io");
                oremix.kb = Dims.contains("KB");
                oremix.make = Dims.contains("MM");
                oremix.ma = Dims.contains("Ma");
                oremix.mb = Dims.contains("MB");
                oremix.me = Dims.contains("Me");
                oremix.mh = Dims.contains("Mh");
                oremix.mi = Dims.contains("Mi");
                oremix.mo = Dims.contains("Mo");
                oremix.np = Dims.contains("Np");
                oremix.ob = Dims.contains("Ob");
                oremix.ph = Dims.contains("Ph");
                oremix.pl = Dims.contains("Pl");
                oremix.pr = Dims.contains("Pr");
                oremix.se = Dims.contains("Se");
                oremix.tcetie = Dims.contains("TE");
                oremix.tf = Dims.contains("TF");
                oremix.ti = Dims.contains("Ti");
                oremix.tr = Dims.contains("Tr");
                oremix.vb = Dims.contains("VB");
                oremix.ve = Dims.contains("Ve");
                oremix.setOverworld(Dims.contains("Ow"));
                oremix.setNether(Dims.contains("Ne"));
                oremix.setEnd(Dims.contains("EN"));
                OreVeins.add(oremix);

                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            BufferedWriter one = Files.newBufferedWriter(Paths.get(GTNEIOrePlugin.CSVnameSmall));
            ColumnPositionMappingStrategy<Oremix> strat = new ColumnPositionMappingStrategy<>();
            strat.setType(Oremix.class);
            String[] columns = "ORENAME,mix,DENSITY,overworld,nether,end,ea,tf,mo,ma,ph,de,as,ce,eu,ga,ca,io,ve,me,en,ti,mi,ob,pr,tr,pl,kb,ha,make,dd,cb,vb,bc,be,bf,tcetie,an,ho,np,mh,mb,se"
                .split("\\,");
            strat.setColumnMapping(columns);
            StatefulBeanToCsv<Oremix> beanToCsv = new StatefulBeanToCsvBuilder<Oremix>(one)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(strat)
                .build();
            List<Oremix> towrite = Combsort(OreVeins);
            one.write(
                "Ore Name,Primary,Secondary,Inbetween,Around,ID,Tier,Height,Density,Size,Weight,Overworld,Nether,End,End Asteroids,Twilight Forest,Moon,Mars,Phobos,Deimos,Asteroids,Ceres,Europa,Ganymede,Callisto,Io,Venus,Mercury,Enceladus,Titan,Miranda,Oberon,Proteus,Triton,Pluto,Kuiper Belt,Haumea,Makemake,Deep Dark,Centauri Bb,Vega B,Barnard C,Barnard E,Barnard F,T Ceti E,Anubis,Horus,Neper,Maahes,Mehen Belt,Seth");
            one.newLine();
            beanToCsv.write(towrite);
            one.flush();
            one.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        runVeins();
        runSmallOres();
    }

    public void runVeins() {
        try {
            Iterator<Map.Entry<String, OreLayerWrapper>> it = GT5OreLayerHelper.mapOreLayerWrapper.entrySet()
                .iterator();
            List<Oremix> OreVeins = new ArrayList<>();
            while (it.hasNext()) {
                Oremix oremix = new Oremix();

                Map.Entry<String, OreLayerWrapper> pair = it.next();
                String Dims = GT5OreLayerHelper.bufferedDims.get(pair.getValue());
                OreLayerWrapper oreLayer = pair.getValue();
                oremix.setOreName(oreLayer.veinName.split("\\.")[2]);
                oremix.setPrimary(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[0]));
                oremix.setSecondary(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[1]));
                oremix.setInbetween(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[2]));
                oremix.setAround(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[3]));
                oremix.setSize(oreLayer.size);
                oremix.setHeight(oreLayer.worldGenHeightRange);
                oremix.setDensity(oreLayer.density);
                oremix.setWeight(oreLayer.randomWeight);
                oremix.setMix(
                    Integer.toString(oreLayer.Meta[0]) + "|"
                        + Integer.toString(oreLayer.Meta[1])
                        + "|"
                        + Integer.toString(oreLayer.Meta[2])
                        + "|"
                        + Integer.toString(oreLayer.Meta[3]));
                oremix.an = Dims.contains("An");
                oremix.as = Dims.contains("As");
                oremix.bc = Dims.contains("BC");
                oremix.be = Dims.contains("BE");
                oremix.bf = Dims.contains("BF");
                oremix.ca = Dims.contains("Ca");
                oremix.cb = Dims.contains("CA");
                oremix.ce = Dims.contains("Ce");
                oremix.dd = Dims.contains("DD");
                oremix.de = Dims.contains("De");
                oremix.ea = Dims.contains("EA");
                oremix.en = Dims.contains("En");
                oremix.eu = Dims.contains("Eu");
                oremix.ga = Dims.contains("Ga");
                oremix.ha = Dims.contains("Ha");
                oremix.ho = Dims.contains("Ho");
                oremix.io = Dims.contains("Io");
                oremix.kb = Dims.contains("KB");
                oremix.make = Dims.contains("MM");
                oremix.ma = Dims.contains("Ma");
                oremix.mb = Dims.contains("MB");
                oremix.me = Dims.contains("Me");
                oremix.mh = Dims.contains("Mh");
                oremix.mi = Dims.contains("Mi");
                oremix.mo = Dims.contains("Mo");
                oremix.np = Dims.contains("Np");
                oremix.ob = Dims.contains("Ob");
                oremix.ph = Dims.contains("Ph");
                oremix.pl = Dims.contains("Pl");
                oremix.pr = Dims.contains("Pr");
                oremix.se = Dims.contains("Se");
                oremix.tcetie = Dims.contains("TE");
                oremix.tf = Dims.contains("TF");
                oremix.ti = Dims.contains("Ti");
                oremix.tr = Dims.contains("Tr");
                oremix.vb = Dims.contains("VB");
                oremix.ve = Dims.contains("Ve");
                oremix.setOverworld(Dims.contains("Ow"));
                oremix.setNether(Dims.contains("Ne"));
                oremix.setEnd(Dims.contains("EN"));
                OreVeins.add(oremix);

                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            BufferedWriter one = Files.newBufferedWriter(Paths.get(GTNEIOrePlugin.CSVname));
            ColumnPositionMappingStrategy<Oremix> strat = new ColumnPositionMappingStrategy<>();
            strat.setType(Oremix.class);
            String[] columns = "ORENAME,PRIMARY,SECONDARY,INBETWEEN,AROUND,mix,TIER,HEIGHT,DENSITY,SIZE,WEIGHT,overworld,nether,end,ea,tf,mo,ma,ph,de,as,ce,eu,ga,ca,io,ve,me,en,ti,mi,ob,pr,tr,pl,kb,ha,make,dd,cb,vb,bc,be,bf,tcetie,an,ho,np,mh,mb,se"
                .split("\\,");
            strat.setColumnMapping(columns);
            StatefulBeanToCsv<Oremix> beanToCsv = new StatefulBeanToCsvBuilder<Oremix>(one)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(strat)
                .build();
            List<Oremix> towrite = Combsort(OreVeins);
            one.write(
                "Ore Name,Primary,Secondary,Inbetween,Around,ID,Tier,Height,Density,Size,Weight,Overworld,Nether,End,End Asteroids,Twilight Forest,Moon,Mars,Phobos,Deimos,Asteroids,Ceres,Europa,Ganymede,Callisto,Io,Venus,Mercury,Enceladus,Titan,Miranda,Oberon,Proteus,Triton,Pluto,Kuiper Belt,Haumea,Makemake,Deep Dark,Centauri Bb,Vega B,Barnard C,Barnard E,Barnard F,T Ceti E,Anubis,Horus,Neper,Maahes,Mehen Belt,Seth");
            one.newLine();
            beanToCsv.write(towrite);
            one.flush();
            one.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
