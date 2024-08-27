package gregtech.api.enums;

import gregtech.api.GregTech_API;
import gregtech.common.GT_Worldgen_Stone;
import gregtech.common.StoneBuilder;

public enum GTStones {

    NetherBlackgraniteTiny(new StoneBuilder().name("nether.stone.blackgranite.tiny")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(-1)
        .size(50)
        .probability(45)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherBlackgraniteSmall(new StoneBuilder().name("nether.stone.blackgranite.small")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(-1)
        .size(100)
        .probability(60)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherBlackgraniteMedium(new StoneBuilder().name("nether.stone.blackgranite.medium")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(-1)
        .size(200)
        .probability(80)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherBlackgraniteLarge(new StoneBuilder().name("nether.stone.blackgranite.large")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(-1)
        .size(300)
        .probability(70)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherBlackgraniteHuge(new StoneBuilder().name("nether.stone.blackgranite.huge")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(-1)
        .size(400)
        .probability(150)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherRedgraniteTiny(new StoneBuilder().name("nether.stone.redgranite.tiny")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(-1)
        .size(50)
        .probability(45)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherRedgraniteSmall(new StoneBuilder().name("nether.stone.redgranite.small")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(-1)
        .size(100)
        .probability(60)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherRedgraniteMedium(new StoneBuilder().name("nether.stone.redgranite.medium")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(-1)
        .size(200)
        .probability(80)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherRedgraniteLarge(new StoneBuilder().name("nether.stone.redgranite.large")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(-1)
        .size(300)
        .probability(70)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherRedgraniteHuge(new StoneBuilder().name("nether.stone.redgranite.huge")
        .disabledByDefault()
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(-1)
        .size(400)
        .probability(150)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherMarbleTiny(new StoneBuilder().name("nether.stone.marble.tiny")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(-1)
        .size(50)
        .probability(45)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherMarbleSmall(new StoneBuilder().name("nether.stone.marble.small")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(-1)
        .size(100)
        .probability(60)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherMarbleMedium(new StoneBuilder().name("nether.stone.marble.medium")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(-1)
        .size(200)
        .probability(80)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherMarbleLarge(new StoneBuilder().name("nether.stone.marble.large")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(-1)
        .size(300)
        .probability(70)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherMarbleHuge(new StoneBuilder().name("nether.stone.marble.huge")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(-1)
        .size(400)
        .probability(150)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherBasaltTiny(new StoneBuilder().name("nether.stone.basalt.tiny")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(-1)
        .size(50)
        .probability(45)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherBasaltSmall(new StoneBuilder().name("nether.stone.basalt.small")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(-1)
        .size(100)
        .probability(60)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherBasaltMedium(new StoneBuilder().name("nether.stone.basalt.medium")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(-1)
        .size(200)
        .probability(80)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherBasaltLarge(new StoneBuilder().name("nether.stone.basalt.large")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(-1)
        .size(300)
        .probability(70)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    NetherBasaltHuge(new StoneBuilder().name("nether.stone.basalt.huge")
        .disabledByDefault()
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(-1)
        .size(400)
        .probability(150)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),
    OverworldBlackgraniteTiny(new StoneBuilder().name("overworld.stone.blackgranite.tiny")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(0)
        .size(75)
        .probability(5)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldBlackgraniteSmall(new StoneBuilder().name("overworld.stone.blackgranite.small")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(0)
        .size(100)
        .probability(10)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldBlackgraniteMedium(new StoneBuilder().name("overworld.stone.blackgranite.medium")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(0)
        .size(200)
        .probability(10)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldBlackgraniteLarge(new StoneBuilder().name("overworld.stone.blackgranite.large")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(0)
        .size(300)
        .probability(70)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    OverworldBlackgraniteHuge(new StoneBuilder().name("overworld.stone.blackgranite.huge")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(0)
        .dimension(0)
        .size(400)
        .probability(150)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    OverworldRedgraniteTiny(new StoneBuilder().name("overworld.stone.redgranite.tiny")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(0)
        .size(75)
        .probability(5)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldRedgraniteSmall(new StoneBuilder().name("overworld.stone.redgranite.small")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(0)
        .size(100)
        .probability(10)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldRedgraniteMedium(new StoneBuilder().name("overworld.stone.redgranite.medium")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(0)
        .size(200)
        .probability(10)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldRedgraniteLarge(new StoneBuilder().name("overworld.stone.redgranite.large")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(0)
        .size(300)
        .probability(70)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    OverworldRedgraniteHuge(new StoneBuilder().name("overworld.stone.redgranite.huge")
        .block(GregTech_API.sBlockGranites)
        .blockMeta(8)
        .dimension(0)
        .size(400)
        .probability(150)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    OverworldMarbleTiny(new StoneBuilder().name("overworld.stone.marble.tiny")
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(0)
        .size(75)
        .probability(5)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldMarbleSmall(new StoneBuilder().name("overworld.stone.marble.small")
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(0)
        .size(100)
        .probability(10)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldMarbleMedium(new StoneBuilder().name("overworld.stone.marble.medium")
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(0)
        .size(200)
        .probability(10)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldMarbleLarge(new StoneBuilder().name("overworld.stone.marble.large")
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(0)
        .size(300)
        .probability(70)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    OverworldMarbleHuge(new StoneBuilder().name("overworld.stone.marble.huge")
        .block(GregTech_API.sBlockStones)
        .blockMeta(0)
        .dimension(0)
        .size(400)
        .probability(150)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    OverworldBasaltTiny(new StoneBuilder().name("overworld.stone.basalt.tiny")
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(0)
        .size(75)
        .probability(5)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldBasaltSmall(new StoneBuilder().name("overworld.stone.basalt.small")
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(0)
        .size(100)
        .probability(10)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldBasaltMedium(new StoneBuilder().name("overworld.stone.basalt.medium")
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(0)
        .size(200)
        .probability(10)
        .heightRange(0, 180)
        .generationInVoidEnabled(false)),

    OverworldBasaltLarge(new StoneBuilder().name("overworld.stone.basalt.large")
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(0)
        .size(300)
        .probability(70)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),

    OverworldBasaltHuge(new StoneBuilder().name("overworld.stone.basalt.huge")
        .block(GregTech_API.sBlockStones)
        .blockMeta(8)
        .dimension(0)
        .size(400)
        .probability(150)
        .heightRange(0, 120)
        .generationInVoidEnabled(false)),;

    public final StoneBuilder stone;

    private GTStones(StoneBuilder stone) {
        this.stone = stone;
    }

    public GT_Worldgen_Stone addGTStone() {
        return new GT_Worldgen_Stone(this.stone);
    }

}
