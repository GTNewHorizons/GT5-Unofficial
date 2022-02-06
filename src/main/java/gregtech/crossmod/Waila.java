package gregtech.crossmod;

import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila {
    public static void callbackRegister(IWailaRegistrar register){
        final IWailaDataProvider multiBlockProvider = new GregtechWailaDataProvider();

        register.registerBodyProvider(multiBlockProvider, BaseMetaTileEntity.class);
        register.registerBodyProvider(multiBlockProvider, BaseMetaPipeEntity.class);
        
        register.registerNBTProvider(multiBlockProvider, BaseMetaTileEntity.class);
        register.registerNBTProvider(multiBlockProvider, BaseMetaPipeEntity.class);
        
        register.registerTailProvider(multiBlockProvider, BaseMetaTileEntity.class);
        register.registerTailProvider(multiBlockProvider, BaseMetaPipeEntity.class);
    }

    public static void init()  {
        FMLInterModComms.sendMessage("Waila", "register", Waila.class.getName() + ".callbackRegister" );
    }


}
