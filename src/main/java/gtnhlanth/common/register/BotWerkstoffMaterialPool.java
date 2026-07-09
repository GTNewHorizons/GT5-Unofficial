package gtnhlanth.common.register;

import static bartworks.system.material.Werkstoff.Types.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.TextureSet.*;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffReconstruction;

/*
 * Originally authored by botn365 under the MIT License. See BotdustriesLICENSE
 */
public class BotWerkstoffMaterialPool implements Runnable {

    public static final Werkstoff TungsticAcid = WerkstoffReconstruction.byId(29900);
    public static final Werkstoff TungstenTrioxide = WerkstoffReconstruction.byId(29901);
    public static final Werkstoff AmmoniumNitrate = WerkstoffReconstruction.byId(29903);
    public static final Werkstoff SodiumTungstate = WerkstoffReconstruction.byId(29904);
    public static final Werkstoff Phosgene = WerkstoffReconstruction.byId(29905);
    public static final Werkstoff Nitromethane = WerkstoffReconstruction.byId(29914);

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }
}
