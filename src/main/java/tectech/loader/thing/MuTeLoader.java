package tectech.loader.thing;

import tectech.TecTech;

public class MuTeLoader implements Runnable {

    @Override
    public void run() {
        TecTech.LOGGER.info("TecTech: Registering MultiTileEntities");
        registerMachines();
        registerCasings();
    }

    private static void registerMachines() {

    }

    private static void registerCasings() {

    }
}
