package bloodasp.galacticgreg;

import gregtech.common.blocks.TileEntityOres;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import bloodasp.galacticgreg.api.Enums.ReplaceState;
import bloodasp.galacticgreg.api.ModDimensionDef;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Log;
import gregtech.common.blocks.BlockOresAbstract;

public class GT_TileEntity_Ores_Space {

    // Renamed function to prevent function shadowing with base GT-code
    public static boolean setOuterSpaceOreBlock(ModDimensionDef pDimensionDef, World pWorld, int pX, int pY, int pZ,
        int pMetaData) {
        return setOuterSpaceOreBlock(pDimensionDef, pWorld, pX, pY, pZ, pMetaData, false, -1);
    }

    public static boolean setOuterSpaceOreBlock(ModDimensionDef pDimensionDef, World pWorld, int pX, int pY, int pZ,
        int pMetaData, boolean pAir) {
        return setOuterSpaceOreBlock(pDimensionDef, pWorld, pX, pY, pZ, pMetaData, pAir, -1);
    }

    /**
     * Check if the block at given position may be replaced by an ore
     *
     * @param pWorld the world in question
     * @param pX     X-Cord
     * @param pY     Y-Cord
     * @param pZ     Z-Cord
     * @return
     */
    private static ReplaceState CheckForReplaceableBlock(World pWorld, int pX, int pY, int pZ,
        ModDimensionDef pDimDef) {
        try {
            ReplaceState tFlag = ReplaceState.Unknown;

            Block targetBlock = pWorld.getBlock(pX, pY, pZ);
            int targetBlockMeta = pWorld.getBlockMetadata(pX, pY, pZ);

            if (targetBlock == Blocks.air) tFlag = ReplaceState.Airblock;
            else tFlag = pDimDef.getReplaceStateForBlock(targetBlock, targetBlockMeta);

            return tFlag;
        } catch (Exception e) {
            e.printStackTrace(GT_Log.err);
            GalacticGreg.Logger.error("Error while processing CheckForReplaceableBlock(), defaulting to UNKNOWN");
            return ReplaceState.Unknown;
        }
    }

    /**
     * Actually set the OreBlock
     *
     * @param pWorld    the world in question
     * @param pX
     * @param pY
     * @param pZ
     * @param pMetaData GT-Ore metadata
     * @param pAir
     * @return
     */
    public static boolean setOuterSpaceOreBlock(ModDimensionDef pDimensionDef, World pWorld, int pX, int pY, int pZ,
        int pMetaData, boolean pAir, int pCustomGTOreOffset) {
        if (!pAir) pY = Math.min(pWorld.getActualHeight(), Math.max(pY, 1));

        if (pDimensionDef == null) {
            GalacticGreg.Logger
                .warn("Unknown DimensionID: %d. Will not set anything here", pWorld.provider.dimensionId);
            return false;
        }
        try {
            Block tBlock = pWorld.getBlock(pX, pY, pZ);
            // If the meta is non-zero, and the target block is either non-air or the air-override is active
            if ((pMetaData > 0) && ((tBlock != Blocks.air) || pAir)) {
                // make sure we're either going with normal ore-metas, or small ores.
                // Probably should do another check for <= 1700
                if (pMetaData < 1000 || pMetaData >= 16000) {
                    ReplaceState tRS = CheckForReplaceableBlock(pWorld, pX, pY, pZ, pDimensionDef);

                    // Unable to lookup replacement state. Means: The block is unknown, and shall not be replaced
                    if (tRS == ReplaceState.Unknown) {
                        GalacticGreg.Logger.trace("Not placing ore Meta %d, as target block is unknown", pMetaData);
                        return false;
                    } else if (tRS == ReplaceState.Airblock && !pAir) {
                        GalacticGreg.Logger.trace("Not placing ore Meta %d in midair, as AIR is FALSE", pMetaData);
                        return false;
                    }
                    if (tRS == ReplaceState.CannotReplace) {
                        // wrong metaData ID for target block
                        GalacticGreg.Logger.trace("Not placing ore Meta %d, as the state is CANNOTREPLACE", pMetaData);
                        return false;
                    }

                    if (pCustomGTOreOffset == -1) pMetaData += pDimensionDef.getStoneType()
                        .getOffset();
                    else pMetaData += pCustomGTOreOffset;
                    // This fix seems like cargo cult coding...The Abstract class just returns 0 for the harvest level.
                    // But it aligns with the GT5U method, so yay?
                    pWorld.setBlock(
                        pX,
                        pY,
                        pZ,
                        GregTech_API.sBlockOres1,
                        TileEntityOres.getHarvestData(
                            (short) pMetaData,
                            ((BlockOresAbstract) GregTech_API.sBlockOres1)
                                .getBaseBlockHarvestLevel(pMetaData % 16000 / 1000)),
                        0);
                    TileEntity tTileEntity = pWorld.getTileEntity(pX, pY, pZ);
                    if ((tTileEntity instanceof TileEntityOres)) {
                        ((TileEntityOres) tTileEntity).mMetaData = ((short) pMetaData);
                        ((TileEntityOres) tTileEntity).mNatural = true;
                    } else {
                        // This is somehow triggered randomly, and most times the target block is air, which should
                        // never happen as we check for air...
                        // That's why I put this behind a debug config option. If you ever find the reason for it,
                        // please tell me what caused this
                        if (GalacticGreg.GalacticConfig.ReportOreGenFailures) GalacticGreg.Logger.warn(
                            "Something went wrong while placing GT OreTileEntity. Meta: %d X [%d] Y [%d] Z [%d]",
                            pMetaData,
                            pX,
                            pY,
                            pZ);
                    }

                    return true;
                } else GalacticGreg.Logger.warn(
                    "Not replacing block at pos %d %d %d due unexpected metaData for OreBlock: %d",
                    pX,
                    pY,
                    pZ,
                    pMetaData);
            }
        } catch (Exception e) {
            if (GalacticGreg.GalacticConfig.ReportOreGenFailures) e.printStackTrace();
        }
        return false;
    }
}
