package pers.gwyog.gtneioreplugin.util;

import static pers.gwyog.gtneioreplugin.Config.CSVName;
import static pers.gwyog.gtneioreplugin.Config.CSVnameSmall;
import static pers.gwyog.gtneioreplugin.GTNEIOrePlugin.instanceDir;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5VeinStat;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper.OreLayerWrapper;

// todo: yeet any opencsv usage.
public class CSVMaker implements Runnable {

    public void runSmallOres() {
        try {
            Iterator<Map.Entry<String, GT5OreSmallHelper.OreSmallWrapper>> it = GT5OreSmallHelper.mapOreSmallWrapper
                .entrySet()
                .iterator();
            List<SmallOre> SmallOreVeins = new ArrayList<>();
            while (it.hasNext()) {
                SmallOre oremix = new SmallOre();

                Map.Entry<String, GT5OreSmallHelper.OreSmallWrapper> pair = it.next();
                GT5OreSmallHelper.OreSmallWrapper oreLayer = pair.getValue();

                Map<String, Boolean> Dims = GT5OreSmallHelper.bufferedDims.get(oreLayer);

                oremix.setOreName(oreLayer.oreGenName);
                oremix.setOreMeta(oreLayer.oreMeta);
                oremix.setHeight(oreLayer.worldGenHeightRange);
                oremix.setAmount(oreLayer.amountPerChunk);
                oremix.setDims(Dims);

                SmallOreVeins.add(oremix);

                it.remove(); // avoids a ConcurrentModificationException
            }

            BufferedWriter one = Files.newBufferedWriter(
                instanceDir.toPath()
                    .resolve(CSVnameSmall));
            Collections.sort(SmallOreVeins);

            // header first
            one.write(SmallOre.getCsvHeader());
            one.newLine();
            for (SmallOre ore : SmallOreVeins) {
                one.write(ore.getCsvEntry());
                one.newLine();
            }
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
                Map<String, Boolean> Dims = GT5OreLayerHelper.bufferedDims.get(pair.getValue());
                OreLayerWrapper oreLayer = pair.getValue();
                oremix.setOreMixName(oreLayer.veinName);
                oremix.setPrimary(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[0]));
                oremix.setSecondary(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[1]));
                oremix.setInbetween(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[2]));
                oremix.setSporadic(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[3]));
                oremix.setSize(oreLayer.size);
                oremix.setHeight(oreLayer.worldGenHeightRange);
                oremix.setDensity(oreLayer.density);
                oremix.setWeight(oreLayer.randomWeight);
                oremix.setOreMixIDs(
                    Integer.toString(oreLayer.Meta[0]) + "|"
                        + Integer.toString(oreLayer.Meta[1])
                        + "|"
                        + Integer.toString(oreLayer.Meta[2])
                        + "|"
                        + Integer.toString(oreLayer.Meta[3]));
                oremix.setDims(Dims);
                OreVeins.add(oremix);

                it.remove(); // avoids a ConcurrentModificationException
            }
            BufferedWriter one = Files.newBufferedWriter(
                instanceDir.toPath()
                    .resolve(CSVName));
            Collections.sort(OreVeins);

            // header first
            one.write(Oremix.getCsvHeader());
            one.newLine();
            for (Oremix ore : OreVeins) {
                one.write(ore.getCsvEntry());
                one.newLine();
            }
            one.flush();
            one.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
