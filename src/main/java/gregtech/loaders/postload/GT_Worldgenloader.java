package gregtech.loaders.postload;

import bloodasp.galacticgreg.WorldGenGaGT;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreMixes;
import gregtech.api.enums.SmallOres;
import gregtech.api.util.GT_Log;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import gregtech.common.GT_Worldgen_Stone;
import gregtech.common.GT_Worldgenerator;

public class GT_Worldgenloader implements Runnable {
    private void registerNetherStones(){

        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.tiny",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            50,
            45,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.small",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            100,
            60,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.medium",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            200,
            80,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.large",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.huge",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "nether.stone.redgranite.tiny",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            50,
            45,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.redgranite.small",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            100,
            60,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.redgranite.medium",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            200,
            80,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.redgranite.large",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.redgranite.huge",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "nether.stone.marble.tiny",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            50,
            45,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.marble.small",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            100,
            60,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.marble.medium",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            200,
            80,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.marble.large",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.marble.huge",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "nether.stone.basalt.tiny",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            50,
            45,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.basalt.small",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            100,
            60,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.basalt.medium",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            200,
            80,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.basalt.large",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.basalt.huge",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            400,
            150,
            0,
            120,
            null,
            false);
    }

    private void registerOWStones(){
        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.tiny",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            75,
            5,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.small",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            100,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.medium",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            200,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.large",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.huge",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.tiny",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            75,
            5,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.small",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            100,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.medium",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            200,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.large",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.huge",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "overworld.stone.marble.tiny",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            75,
            5,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.marble.small",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            100,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.marble.medium",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            200,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.marble.large",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.marble.huge",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "overworld.stone.basalt.tiny",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            75,
            5,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.basalt.small",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            100,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.basalt.medium",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            200,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.basalt.large",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.basalt.huge",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            400,
            150,
            0,
            120,
            null,
            false);
    }

    public void run() {
        new GT_Worldgenerator();

        registerNetherStones();
        registerOWStones();

        // GT Default Small Ores
        for (SmallOres smallOre: SmallOres.values()){
            smallOre.addGTSmallOre();
        }

        // GT Veins registration
        for (OreMixes oreMix : OreMixes.values()){
            oreMix.addGTOreLayer();
        }


        new WorldGenGaGT().run();
        GT_Log.out.println("Started Galactic Greg ore gen code");
    }
}
