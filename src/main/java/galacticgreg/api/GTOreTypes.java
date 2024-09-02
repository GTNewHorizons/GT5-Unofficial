package galacticgreg.api;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import gregtech.api.GregTechAPI;

/**
 * Representation of the various GregTech ores, with their counterpart in VanillaBlocks, and the OreOffset that is
 * required to generate the proper ores
 */
public enum GTOreTypes {

    /**
     * The Definition for Gregtech's RedGranite
     **/
    RedGranite(4000, GregTechAPI.sBlockGranites, 8, 3),
    /**
     * The Definition for Gregtech's BlackGranite
     */
    BlackGranite(3000, GregTechAPI.sBlockGranites, 0, 3),
    /**
     * The Definition for EndStone
     */
    EndStone(2000, Blocks.end_stone, 0, 0),
    /**
     * The Definition for Netherrack
     */
    Netherrack(1000, Blocks.netherrack, 0, 0), // Unsure about blockupdate value!
    /**
     * The Definition for SmallOres (And BlockType Stone)
     */
    SmallOres(16000, Blocks.stone, 0, 0), // Unsure about blockupdate value!
    /**
     * The Definition for Ores (And BlockType Stone)
     */
    NormalOres(0, Blocks.stone, 0, 0); // Unsure about blockupdate value!

    private int _mOffset;
    private Block _mStoneBlock;
    private int _mBlockMeta;
    private int _mUpdateMode;

    GTOreTypes(int pOffset, Block pBlock, int pMeta, int pUpdateMode) {
        _mOffset = pOffset;
        _mStoneBlock = pBlock;
        _mBlockMeta = pMeta;
        _mUpdateMode = pUpdateMode;
    }

    public Block getBlock() {
        return _mStoneBlock;
    }

    public int getMeta() {
        return _mBlockMeta;
    }

    public int getOffset() {
        return _mOffset;
    }

    public int getUpdateMode() {
        return _mUpdateMode;
    }
}
