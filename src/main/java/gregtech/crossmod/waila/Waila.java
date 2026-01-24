package gregtech.crossmod.waila;

import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.Mods;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.blocks.BlockCasings5;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila {

    public static void callbackRegister(IWailaRegistrar register) {
        final IWailaDataProvider multiBlockProvider = new GregtechTEWailaDataProvider();

        register.registerBodyProvider(multiBlockProvider, BaseMetaTileEntity.class);
        register.registerBodyProvider(multiBlockProvider, BaseMetaPipeEntity.class);

        register.registerNBTProvider(multiBlockProvider, BaseMetaTileEntity.class);
        register.registerNBTProvider(multiBlockProvider, BaseMetaPipeEntity.class);

        register.registerTailProvider(multiBlockProvider, BaseMetaTileEntity.class);
        register.registerTailProvider(multiBlockProvider, BaseMetaPipeEntity.class);

        final IWailaDataProvider blockProvider = new GregtechBlockWailaDataProvider();

        register.registerStackProvider(blockProvider, BlockCasings5.class);
    }

    public static void init() {
        FMLInterModComms.sendMessage(Mods.Waila.ID, "register", Waila.class.getName() + ".callbackRegister");
    }
}
