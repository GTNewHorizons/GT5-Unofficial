package gtneioreplugin.util;

import static gtneioreplugin.GTNEIOrePlugin.LOG;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import gregtech.api.material.MaterialUtils;
import gtneioreplugin.Config;
import gtneioreplugin.GTNEIOrePlugin;
import gtneioreplugin.util.GT5OreLayerHelper.OreLayerWrapper;

// todo: yeet any opencsv usage.
public class CSVMaker implements Runnable {

    public void runSmallOres() {
        try {
            List<SmallOre> SmallOreVeins = new ArrayList<>();
            for (GT5OreSmallHelper.OreSmallWrapper oreLayer : GT5OreSmallHelper.SMALL_ORES_BY_NAME.values()) {
                SmallOre oremix = new SmallOre();

                oremix.setOreName(oreLayer.oreGenName);
                oremix.setOreMaterial(MaterialUtils.internalName(oreLayer.material));
                oremix.setHeight(oreLayer.worldGenHeightRange);
                oremix.setAmount(oreLayer.amountPerChunk);
                oremix.setDims(oreLayer.enabledDims);

                SmallOreVeins.add(oremix);
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
            LOG.error(e);
        }
    }

    @Override
    public void run() {
        runVeins();
        runSmallOres();
    }

    public void runVeins() {
        try {
            List<Oremix> OreVeins = new ArrayList<>();
            for (OreLayerWrapper oreLayer : GT5OreLayerHelper.getOreVeinsByName()
                .values()) {
                Oremix oremix = new Oremix();

                Set<String> Dims = oreLayer.abbrDimNames;
                oremix.setOreMixName(oreLayer.veinName);
                oremix.setPrimary(MaterialUtils.internalName(oreLayer.ores[0]));
                oremix.setSecondary(MaterialUtils.internalName(oreLayer.ores[1]));
                oremix.setInbetween(MaterialUtils.internalName(oreLayer.ores[2]));
                oremix.setSporadic(MaterialUtils.internalName(oreLayer.ores[3]));
                oremix.setSize(oreLayer.size);
                oremix.setHeight(oreLayer.worldGenHeightRange);
                oremix.setDensity(oreLayer.density);
                oremix.setWeight(oreLayer.randomWeight);
                oremix.setOreMixIDs(
                    MaterialUtils.internalName(oreLayer.ores[0]) + "|"
                        + MaterialUtils.internalName(oreLayer.ores[1])
                        + "|"
                        + MaterialUtils.internalName(oreLayer.ores[2])
                        + "|"
                        + MaterialUtils.internalName(oreLayer.ores[3]));
                oremix.setDims(Dims);
                OreVeins.add(oremix);
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
            LOG.error(e);
        }
    }
}
