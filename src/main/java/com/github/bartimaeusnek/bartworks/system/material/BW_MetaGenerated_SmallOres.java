package com.github.bartimaeusnek.bartworks.system.material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Arrays;

public class BW_MetaGenerated_SmallOres extends BW_MetaGenerated_Ores {
    public BW_MetaGenerated_SmallOres(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName) {
        super(p_i45386_1_, tileEntity, blockName);
    }

    @Override
    protected void doRegistrationStuff(Werkstoff w) {
        if (w != null) {
            if ((w.getGenerationFeatures().toGenerate & 0b1000) == 0 || ((w.getGenerationFeatures().blacklist & 0b1000) != 0))
                return;
            GT_ModHandler.addValuableOre(this, w.getmID(), 1);
            GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + "." + w.getmID() + ".name", "Small " + w.getDefaultName() + OrePrefixes.oreSmall.mLocalizedMaterialPost);
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "bw.blockores.02";
    }

    public static boolean setOreBlock(World aWorld, int aX, int aY, int aZ, int aMetaData, boolean air, Block block, int[] aBlockMeta) {
        if (!air) {
            aY = Math.min(aWorld.getActualHeight(), Math.max(aY, 1));
        }

        Block tBlock = aWorld.getBlock(aX, aY, aZ);
        Block tOreBlock = WerkstoffLoader.BWSmallOres;
        if (aMetaData < 0 || tBlock == Blocks.air && !air) {
            return false;
        } else {

            if (Block.getIdFromBlock(tBlock) != Block.getIdFromBlock(block)) {
                return false;
            }
            final int aaY = aY;
            if (Arrays.stream(aBlockMeta).noneMatch(e -> e == aWorld.getBlockMetadata(aX, aaY, aZ))) {
                return false;
            }

            aWorld.setBlock(aX, aY, aZ, tOreBlock, aMetaData, 0);
            TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if (tTileEntity instanceof BW_MetaGeneratedOreTE) {
                ((BW_MetaGeneratedOreTE) tTileEntity).mMetaData = (short) aMetaData;
            }

            return true;
        }
    }

}
