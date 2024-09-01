package detrav;

import detrav.items.DetravMetaGeneratedTool01;
import detrav.items.processing.ProcessingDetravToolProspector;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class DetravLoaderAfterGTPreload implements Runnable {

    @Override
    public void run() {

        // items
        new DetravMetaGeneratedTool01();

        // recipes and etc
        new ProcessingDetravToolProspector();

    }
}
