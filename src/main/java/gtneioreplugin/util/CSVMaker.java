package gtneioreplugin.util;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gtneioreplugin.Config;
import gtneioreplugin.GTNEIOrePlugin;
import gtneioreplugin.util.GT5OreLayerHelper.OreLayerWrapper;

// todo: yeet any opencsv usage.
public class CSVMaker implements Runnable {

    public void runSmallOres() {
        try {
            Iterator<Map.Entry<String, GT5OreSmallHelper.OreSmallWrapper>> it = GT5OreSmallHelper.SMALL_ORES_BY_NAME
                .entrySet()
                .iterator();
            List<SmallOre> SmallOreVeins = new ArrayList<>();
            while (it.hasNext()) {
                SmallOre oremix = new SmallOre();

                Map.Entry<String, GT5OreSmallHelper.OreSmallWrapper> pair = it.next();
                GT5OreSmallHelper.OreSmallWrapper oreLayer = pair.getValue();

                oremix.setOreName(oreLayer.oreGenName);
                oremix.setOreMaterial(oreLayer.material.getInternalName());
                oremix.setHeight(oreLayer.worldGenHeightRange);
                oremix.setAmount(oreLayer.amountPerChunk);
                oremix.setDims(oreLayer.enabledDims);

                SmallOreVeins.add(oremix);

                it.remove(); // avoids a ConcurrentModificationException
            }

            BufferedWriter one = Files.newBufferedWriter(
                GTNEIOrePlugin.instanceDir.toPath()
                    .resolve(Config.CSVnameSmall));
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
            Iterator<Map.Entry<String, OreLayerWrapper>> it = GT5OreLayerHelper.getOreVeinsByName()
                .entrySet()
                .iterator();
            List<Oremix> OreVeins = new ArrayList<>();
            while (it.hasNext()) {
                Oremix oremix = new Oremix();

                Map.Entry<String, OreLayerWrapper> pair = it.next();
                Set<String> Dims = pair.getValue().abbrDimNames;
                OreLayerWrapper oreLayer = pair.getValue();
                oremix.setOreMixName(oreLayer.veinName);
                oremix.setPrimary(oreLayer.ores[0].getInternalName());
                oremix.setSecondary(oreLayer.ores[1].getInternalName());
                oremix.setInbetween(oreLayer.ores[2].getInternalName());
                oremix.setSporadic(oreLayer.ores[3].getInternalName());
                oremix.setSize(oreLayer.size);
                oremix.setHeight(oreLayer.worldGenHeightRange);
                oremix.setDensity(oreLayer.density);
                oremix.setWeight(oreLayer.randomWeight);
                oremix.setOreMixIDs(
                    oreLayer.ores[0].getInternalName() + "|"
                        + oreLayer.ores[1].getInternalName()
                        + "|"
                        + oreLayer.ores[2].getInternalName()
                        + "|"
                        + oreLayer.ores[3].getInternalName());
                oremix.setDims(Dims);
                OreVeins.add(oremix);

                it.remove(); // avoids a ConcurrentModificationException
            }
            BufferedWriter one = Files.newBufferedWriter(
                GTNEIOrePlugin.instanceDir.toPath()
                    .resolve(Config.CSVName));
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
