package galacticgreg.schematics;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

import galacticgreg.GalacticGreg;

/**
 * Class for XML Structure files. You only should edit/use this file/class if you want to add/fix stuff with
 * GalacticGreg itself, and never if you're a mod developer and want to add support for GGreg to your mod. However, feel
 * free to copy this code to your own mod to implement structures. If you have questions, find me on github and ask
 */
public class SpaceSchematicHandler {

    File _mConfigFolderName;
    File _mSchematicsFolderName;
    private List<SpaceSchematic> _mSpaceSchematics;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public SpaceSchematicHandler(File pConfigFolder) {
        _mConfigFolderName = new File(String.format("%s/%s", pConfigFolder.toString(), GalacticGreg.NICE_MODID));
        _mSchematicsFolderName = new File(String.format("%s/schematics", _mConfigFolderName));

        _mSpaceSchematics = new ArrayList<>();

        if (!_mSchematicsFolderName.exists()) _mSchematicsFolderName.mkdirs();
    }

    /**
     * Get a random schematic to be placed.
     *
     * @return A schematic that can be spawned in space
     */
    public SpaceSchematic getRandomSpaceSchematic() {
        int tRandomChance = GalacticGreg.GalacticRandom.nextInt(100);
        List<Integer> tRandomIDs = new ArrayList<>();
        SpaceSchematic tChoosenSchematic = null;

        if (_mSpaceSchematics == null) return null;

        if (_mSpaceSchematics.size() == 0) return null;

        if (_mSpaceSchematics.size() == 1) {
            tChoosenSchematic = _mSpaceSchematics.get(0);
            if (tChoosenSchematic.getRarity() < tRandomChance) tChoosenSchematic = null;
        } else {
            for (int i = 0; i < _mSpaceSchematics.size(); i++) {
                if (_mSpaceSchematics.get(i)
                    .getRarity() >= tRandomChance) tRandomIDs.add(i);
            }
        }

        if (tRandomIDs.size() > 0) {
            int tRnd = GalacticGreg.GalacticRandom.nextInt(tRandomIDs.size());
            tChoosenSchematic = _mSpaceSchematics.get(tRandomIDs.get(tRnd));
        }

        return tChoosenSchematic;
    }

    /**
     * Try to reload the schematics. Will not change the list of currently loaded schematics if any errors are detected,
     * except if you force it to do so
     *
     * @return
     */
    public boolean reloadSchematics(boolean pForceReload) {
        try {
            Collection<File> structureFiles = FileUtils
                .listFiles(_mSchematicsFolderName, new String[] { "xml" }, false);
            List<SpaceSchematic> tNewSpaceSchematics = new ArrayList<>();
            int tErrorsFound = 0;

            if (structureFiles.isEmpty()) return true;

            for (File tSchematic : structureFiles) {
                try {
                    SpaceSchematic tSchematicObj = LoadSpaceSchematic(tSchematic);
                    if (tSchematicObj != null) tNewSpaceSchematics.add(tSchematicObj);
                    else {
                        GalacticGreg.Logger.warn("Could not load Schematic %s. Please check the syntax", tSchematic);
                        tErrorsFound++;
                    }
                } catch (Exception e) {
                    GalacticGreg.Logger.error("Error while loading Schematic %s", tSchematic);
                    e.printStackTrace();
                }

            }

            GalacticGreg.Logger.info("Successfully loaded %d Schematics", tNewSpaceSchematics.size());
            boolean tDoReplace = true;

            if (tErrorsFound > 0) {
                GalacticGreg.Logger.warn("Found %d errors while loading, not all schematics will be available");
                if (pForceReload)
                    GalacticGreg.Logger.info("Reload was forced, replacing currently active list with new one");
                else {
                    GalacticGreg.Logger.warn("Nothing was replaced. Fix any errors and reload again");
                    tDoReplace = false;
                }
            }

            if (tDoReplace) _mSpaceSchematics = tNewSpaceSchematics;

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves the schematic to disk. The schematics name will be used as filename
     *
     * @param pSchematic
     * @return
     */
    public boolean SaveSpaceStructure(SpaceSchematic pSchematic) {
        try {
            if (pSchematic.getName()
                .length() < 1) return false;

            JAXBContext tJaxbCtx = JAXBContext.newInstance(SpaceSchematic.class);
            Marshaller jaxMarsh = tJaxbCtx.createMarshaller();
            jaxMarsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxMarsh.marshal(
                pSchematic,
                new FileOutputStream(String.format("%s/%s.xml", _mSchematicsFolderName, pSchematic.getName()), false));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Load a schematic from disk by the schematic-name itself, without .xml or path
     *
     * @param pSchematicName
     * @return
     */
    public SpaceSchematic LoadSpaceSchematic(String pSchematicName) {
        return LoadSpaceSchematic(new File(String.format("%s/%s.xml", _mSchematicsFolderName, pSchematicName)));
    }

    /**
     * Load a schematic file from disk by providing the actual file-object
     *
     * @param pName
     * @return
     */
    public SpaceSchematic LoadSpaceSchematic(File pName) {
        SpaceSchematic tSchematic = null;

        try {
            JAXBContext tJaxbCtx = JAXBContext.newInstance(SpaceSchematic.class);
            if (!pName.exists()) {
                GalacticGreg.Logger.error("SchematicFile %s could not be found", pName);
                return null;
            }

            Unmarshaller jaxUnmarsh = tJaxbCtx.createUnmarshaller();
            tSchematic = (SpaceSchematic) jaxUnmarsh.unmarshal(pName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tSchematic;
    }
}
