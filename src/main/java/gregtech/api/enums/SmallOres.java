package gregtech.api.enums;


import bloodasp.galacticgreg.GT_Worldgen_GT_Ore_SmallPieces_Space;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import gregtech.common.SmallOreBuilder;

public enum SmallOres {
    // spotless : off
    Copper(
        new SmallOreBuilder().name("ore.small.copper")
            .heightRange(60,180)
            .amount(32)
            .ore(Materials.Copper)
    ),

    Tin(
        new SmallOreBuilder().name("ore.small.tin")
            .heightRange(80,220)
            .amount(32)
            .ore(Materials.Tin)
    ),

    Bismuth(
        new SmallOreBuilder().name("ore.small.bismuth")
            .heightRange(80,120)
            .amount(8)
            .ore(Materials.Bismuth)
    ),

    Coal(
        new SmallOreBuilder().name("ore.small.coal")
            .heightRange(120,250)
            .amount(24)
            .ore(Materials.Coal)
    ),

    Iron(
        new SmallOreBuilder().name("ore.small.iron")
            .heightRange(40,100)
            .amount(16)
            .ore(Materials.Iron)
    ),

    Lead(
        new SmallOreBuilder().name("ore.small.lead")
            .heightRange(40,180)
            .amount(16)
            .ore(Materials.Lead)
    ),

    Zinc(
        new SmallOreBuilder().name("ore.small.zinc")
            .heightRange(80,210)
            .amount(24)
            .ore(Materials.Zinc)
    ),

    Gold(
        new SmallOreBuilder().name("ore.small.gold")
            .heightRange(20,60)
            .amount(8)
            .ore(Materials.Gold)
    ),

    Silver(
        new SmallOreBuilder().name("ore.small.silver")
            .heightRange(20,60)
            .amount(20)
            .ore(Materials.Silver)
    ),

    Nickel(
        new SmallOreBuilder().name("ore.small.nickel")
            .heightRange(80,150)
            .amount(8)
            .ore(Materials.Nickel)
    ),

    Lapis(
        new SmallOreBuilder().name("ore.small.lapis")
            .heightRange(10,50)
            .amount(4)
            .ore(Materials.Lapis)
    ),

    Diamond(
        new SmallOreBuilder().name("ore.small.diamond")
            .heightRange(5,15)
            .amount(2)
            .ore(Materials.Diamond)
    ),

    Emerald(
        new SmallOreBuilder().name("ore.small.emerald")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Emerald)
    ),

    Ruby(
        new SmallOreBuilder().name("ore.small.ruby")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Ruby)
    ),

    Sapphire(
        new SmallOreBuilder().name("ore.small.sapphire")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Sapphire)
    ),

    Greensapphire(
        new SmallOreBuilder().name("ore.small.greensapphire")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.GreenSapphire)
    ),

    Olivine(
        new SmallOreBuilder().name("ore.small.olivine")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Olivine)
    ),

    Topaz(
        new SmallOreBuilder().name("ore.small.topaz")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Topaz)
    ),

    Tanzanite(
        new SmallOreBuilder().name("ore.small.tanzanite")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Tanzanite)
    ),

    Amethyst(
        new SmallOreBuilder().name("ore.small.amethyst")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Amethyst)
    ),

    Opal(
        new SmallOreBuilder().name("ore.small.opal")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Opal)
    ),

    Jasper(
        new SmallOreBuilder().name("ore.small.jasper")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Jasper)
    ),

    Bluetopaz(
        new SmallOreBuilder().name("ore.small.bluetopaz")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.BlueTopaz)
    ),

    Amber(
        new SmallOreBuilder().name("ore.small.amber")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Amber)
    ),

    Foolsruby(
        new SmallOreBuilder().name("ore.small.foolsruby")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.FoolsRuby)
    ),

    Garnetred(
        new SmallOreBuilder().name("ore.small.garnetred")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.GarnetRed)
    ),

    Garnetyellow(
        new SmallOreBuilder().name("ore.small.garnetyellow")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.GarnetYellow)
    ),

    Redstone(
        new SmallOreBuilder().name("ore.small.redstone")
            .heightRange(5,25)
            .amount(8)
            .ore(Materials.Redstone)
    ),

    Netherquartz(
        new SmallOreBuilder().name("ore.small.netherquartz")
            .heightRange(30,120)
            .amount(64)
            .ore(Materials.NetherQuartz)
    ),

    Saltpeter(
        new SmallOreBuilder().name("ore.small.saltpeter")
            .heightRange(10,60)
            .amount(8)
            .ore(Materials.Saltpeter)
    ),

    Sulfur(
        new SmallOreBuilder().name("ore.small.sulfur")
            .heightRange(5,60)
            .amount(40)
            .ore(Materials.Sulfur)
    ),

    Titanium(new SmallOreBuilder().name("ore.small.titanium")
            .heightRange(10,180)
            .amount(32)
            .ore(Materials.Titanium)
    ),

    Tungsten(
        new SmallOreBuilder().name("ore.small.tungsten")
            .heightRange(10,120)
            .amount(16)
            .ore(Materials.Tungsten)
    ),

    Meteoriciron(
        new SmallOreBuilder().name("ore.small.meteoriciron")
            .heightRange(50,70)
            .amount(8)
            .ore(Materials.MeteoricIron)
    ),

    Firestone(
        new SmallOreBuilder().name("ore.small.firestone")
            .heightRange(5,15)
            .amount(2)
            .ore(Materials.Firestone)
    ),

    Neutronium(
        new SmallOreBuilder().name("ore.small.neutronium")
            .heightRange(5,15)
            .amount(8)
            .ore(Materials.Neutronium)
    ),

    Chromite(
        new SmallOreBuilder().name("ore.small.chromite")
            .heightRange(20,40)
            .amount(8)
            .ore(Materials.Chromite)
    ),

    Tungstate(
        new SmallOreBuilder().name("ore.small.tungstate")
            .heightRange(20,40)
            .amount(8)
            .ore(Materials.Tungstate)
    ),

    Naquadah(
        new SmallOreBuilder().name("ore.small.naquadah")
            .heightRange(5,25)
            .amount(8)
            .ore(Materials.Naquadah)
    ),

    Quantium(
        new SmallOreBuilder().name("ore.small.quantium")
            .heightRange(5,25)
            .amount(6)
            .ore(Materials.Quantium)
    ),

    Mythril(
        new SmallOreBuilder().name("ore.small.mythril")
            .heightRange(5,25)
            .amount(6)
            .ore(Materials.Mytryl)
    ),

    Ledox(
        new SmallOreBuilder().name("ore.small.ledox")
            .heightRange(40,60)
            .amount(4)
            .ore(Materials.Ledox)
    ),

    Oriharukon(
        new SmallOreBuilder().name("ore.small.oriharukon")
            .heightRange(20,40)
            .amount(6)
            .ore(Materials.Oriharukon)
    ),

    Draconium(
        new SmallOreBuilder().name("ore.small.draconium")
            .heightRange(5,15)
            .amount(4)
            .ore(Materials.Draconium)
    ),

    Awdraconium(
        new SmallOreBuilder().name("ore.small.awdraconium")
            .heightRange(5,15)
            .amount(2)
            .ore(Materials.DraconiumAwakened)
    ),

    Desh(
        new SmallOreBuilder().name("ore.small.desh")
            .heightRange(10,30)
            .amount(6)
            .ore(Materials.Desh)
    ),

    Blackplutonium(
        new SmallOreBuilder().name("ore.small.blackplutonium")
            .heightRange(25,45)
            .amount(6)
            .ore(Materials.BlackPlutonium)
    ),

    Infinitycatalyst(
        new SmallOreBuilder().name("ore.small.infinitycatalyst")
            .heightRange(40,80)
            .amount(6)
            .ore(Materials.InfinityCatalyst)
    ),

    Infinity(
        new SmallOreBuilder().name("ore.small.infinity")
            .heightRange(2,40)
            .amount(2)
            .ore(Materials.Infinity)
    ),

    Bedrockium(
        new SmallOreBuilder().name("ore.small.bedrockium")
            .heightRange(5,25)
            .amount(6)
            .ore(Materials.Bedrockium)
    ),

    Realgar(
        new SmallOreBuilder().name("ore.small.realgar")
            .heightRange(15,85)
            .amount(32)
            .ore(Materials.Realgar)
    ),

    Certusquartz(
        new SmallOreBuilder().name("ore.small.certusquartz")
            .heightRange(5,115)
            .amount(16)
            .ore(Materials.CertusQuartz)
    ),

    Jade(
        new SmallOreBuilder().name("ore.small.jade")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.Jade)
    ),

    Deepiron(
        new SmallOreBuilder().name("ore.small.deepiron")
            .heightRange(5,40)
            .amount(8)
            .ore(Materials.DeepIron)
    ),

    Redgarnet(
        new SmallOreBuilder().name("ore.small.redgarnet")
            .heightRange(5,35)
            .amount(2)
            .ore(Materials.GarnetRed)
    ),

    Chargedcertus(
        new SmallOreBuilder().name("ore.small.chargedcertus")
            .heightRange(5,115)
            .amount(4)
            .ore(Materials.CertusQuartzCharged)
    ),
    ;
    // spotless : on

    public final SmallOreBuilder smallOreBuilder;

    private SmallOres(gregtech.common.SmallOreBuilder smallOreBuilder) {
        this.smallOreBuilder = smallOreBuilder;
    }

    public GT_Worldgen_GT_Ore_SmallPieces addGTSmallOre(){
        return new GT_Worldgen_GT_Ore_SmallPieces(this.smallOreBuilder);
    }
    public GT_Worldgen_GT_Ore_SmallPieces_Space addGaGregSmallOre(){
        return new GT_Worldgen_GT_Ore_SmallPieces_Space(this.smallOreBuilder);
    }
}
