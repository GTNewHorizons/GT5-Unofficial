package gtPlusPlus.xmod.gregtech.loaders;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.*;

public class Gregtech_Blocks {

    public static void run() {

        // Casing Blocks
        ModBlocks.blockCasingsMisc = new GregtechMetaCasingBlocks();
        ModBlocks.blockCasings2Misc = new GregtechMetaCasingBlocks2();
        ModBlocks.blockCasings3Misc = new GregtechMetaCasingBlocks3();
        ModBlocks.blockCasings4Misc = new GregtechMetaCasingBlocks4();
        ModBlocks.blockCasings5Misc = new GregtechMetaCasingBlocks5();
        ModBlocks.blockCasings6Misc = new GregtechMetaCasingBlocks6();

        ModBlocks.blockCasingsTieredGTPP = new GregtechMetaTieredCasingBlocks1();
        ModBlocks.blockSpecialMultiCasings = new GregtechMetaSpecialMultiCasings();
        ModBlocks.blockSpecialMultiCasings2 = new GregtechMetaSpecialMultiCasings2();
        ModBlocks.blockCustomMachineCasings = new GregtechMetaSpecialMachineCasings();
        ModBlocks.blockCustomPipeGearCasings = new GregtechMetaCasingBlocksPipeGearbox();
    }
}
