package bloodasp.galacticgreg;

import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;

public class WorldGenGaGT implements Runnable {

    @Override
    public void run() {
        new GT_Worldgenerator_Space();

        /*
         * new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.copper", true, 60, 120, 32, Materials.Copper); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.tin", true, 60, 120, 32, Materials.Tin); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.bismuth", true, 80, 120, 8, Materials.Bismuth); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.coal", true, 60, 100, 24, Materials.Coal); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.iron", true, 40, 80, 16, Materials.Iron); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.lead", true, 40, 80, 16, Materials.Lead); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.zinc", true, 30, 60, 12, Materials.Zinc); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.gold", true, 20, 40, 8, Materials.Gold); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.silver", true, 20, 40, 8, Materials.Silver); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.nickel", true, 20, 40, 8, Materials.Nickel); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.lapis", true, 20, 40, 4, Materials.Lapis); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.diamond", true, 5, 10, 2, Materials.Diamond); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.redstone", true, 5, 20, 8, Materials.Redstone); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.platinum", true, 20, 40, 8, Materials.Platinum); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.iridium", true, 20, 40, 8, Materials.Iridium); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.netherquartz", true, 30, 120, 64, Materials.NetherQuartz);
         * new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.saltpeter", true, 10, 60, 8, Materials.Saltpeter); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.sulfur_n", true, 10, 60, 32, Materials.Sulfur); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.sulfur_o", true, 5, 15, 8, Materials.Sulfur); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.emerald", true, 5, 250, 1, Materials.Emerald); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.ruby", true, 5, 250, 1, Materials.Ruby); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.sapphire", true, 5, 250, 1, Materials.Sapphire); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.greensapphire", true, 5, 250, 1, Materials.GreenSapphire);
         * new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.olivine", true, 5, 250, 1, Materials.Olivine); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.topaz", true, 5, 250, 1, Materials.Topaz); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.tanzanite", true, 5, 250, 1, Materials.Tanzanite); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.amethyst", true, 5, 250, 1, Materials.Amethyst); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.opal", true, 5, 250, 1, Materials.Opal); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.jasper", true, 5, 250, 1, Materials.Jasper); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.bluetopaz", true, 5, 250, 1, Materials.BlueTopaz); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.amber", true, 5, 250, 1, Materials.Amber); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.foolsruby", true, 5, 250, 1, Materials.FoolsRuby); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.garnetred", true, 5, 250, 1, Materials.GarnetRed); new
         * GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.garnetyellow", true, 5, 250, 1, Materials.GarnetYellow);
         */
        /*
         * This part here enables every GT Small Ore for Space Dims.
         */
        for (int k = 0; k < GT_Worldgen_GT_Ore_SmallPieces.sList.size(); ++k) {
            new GT_Worldgen_GT_Ore_SmallPieces_Space(
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mWorldGenName,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mEnabled,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mMinY,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mMaxY,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mAmount,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mMeta);
        }

        /*
         * This part here enables every GT Ore for Space Dims.
         */
        for (int k = 0; k < GT_Worldgen_GT_Ore_Layer.sList.size(); ++k) {
            new GT_Worldgen_GT_Ore_Layer_Space(
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mWorldGenName,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mEnabled,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mMinY,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mMaxY,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mWeight,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mDensity,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mSize,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mPrimaryMeta,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mSecondaryMeta,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mBetweenMeta,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mSporadicMeta);
        }

        /*
         * int f = 0; for (int j = GregTech_API.sWorldgenFile.get("worldgen", "AmountOfCustomSmallOreSlots", 16); f < j;
         * f++) { new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.custom." + (f < 10 ? "0" : "") + f, false, 0, 0,
         * 0, Materials._NULL); } int i = 0; for (int j = GregTech_API.sWorldgenFile.get("worldgen",
         * "AmountOfCustomLargeVeinSlots", 16); i < j; i++) { new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.custom." + (i
         * < 10 ? "0" : "") + i, false, 0, 0, 0, 0, 0, Materials._NULL, Materials._NULL, Materials._NULL,
         * Materials._NULL); }
         */
    }

}
