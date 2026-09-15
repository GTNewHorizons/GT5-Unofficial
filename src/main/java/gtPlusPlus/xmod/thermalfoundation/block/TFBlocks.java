package gtPlusPlus.xmod.thermalfoundation.block;

import cofh.core.fluid.BlockFluidCoFHBase;

public class TFBlocks {

    public static BlockFluidCoFHBase blockFluidPyrotheum;
    public static BlockFluidCoFHBase blockFluidCryotheum;
    public static BlockFluidCoFHBase blockFluidEnder;

    public static void preInit() {
        blockFluidPyrotheum = new TFBlockFluidPyrotheum();
        blockFluidCryotheum = new TFBlockFluidCryotheum();
        blockFluidEnder = new TFBlockFluidEnder();
        blockFluidPyrotheum.preInit();
        blockFluidCryotheum.preInit();
        blockFluidEnder.preInit();
    }

    public static void init() {}

    public static void postInit() {}
}
