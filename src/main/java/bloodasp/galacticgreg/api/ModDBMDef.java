package bloodasp.galacticgreg.api;

import net.minecraft.block.Block;

import bloodasp.galacticgreg.api.Enums.ReplaceState;

/**
 * Mod "Dimension Block Meta Definition" Defines the Block-Meta combination for Blocks that can be replaced by the
 * oregen.
 *
 */
public class ModDBMDef {

    private String _targetBlockName;
    private int _targetMeta;
    private boolean _canAlwaysReplace;

    public String getBlockName() {
        return _targetBlockName;
    }

    public int getMeta() {
        return _targetMeta;
    }

    public boolean getCanAlwaysReplace() {
        return _canAlwaysReplace;
    }

    /**
     * Internal function
     *
     * Check if the given Block is equal to the block in this instance
     *
     * @param pBlock the Block in question
     * @return
     */
    public ReplaceState blockEquals(Block pBlock) {
        if (pBlock == null) return ReplaceState.Unknown;

        if (Block.blockRegistry.getNameForObject(pBlock)
            .equals(_targetBlockName)) return ReplaceState.CanReplace;
        else return ReplaceState.CannotReplace;
    }

    /**
     * Internal function
     *
     * Check if the given Block is equal to the block in this instance and matches the metadata
     *
     * @param pBlock the block in question
     * @param pMeta  the metadata in question
     * @return
     */
    public ReplaceState blockEquals(Block pBlock, int pMeta) {
        ReplaceState tFlag = ReplaceState.Unknown;
        if (blockEquals(pBlock) == ReplaceState.CanReplace) {
            if (pMeta == _targetMeta || _canAlwaysReplace) tFlag = ReplaceState.CanReplace;
            else tFlag = ReplaceState.CannotReplace;
        }

        return tFlag;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ModDBMDef)) return false;
        ModDBMDef otherModDBMDef = (ModDBMDef) other;
        return (otherModDBMDef._targetBlockName.equals(_targetBlockName) && otherModDBMDef._targetMeta == _targetMeta);
    }

    /**
     * Create a new "Block that can be replaced by ores" definition. Meta defaults to 0 here
     *
     * @param pTargetBlockName The unlocalizedName of the block
     */
    public ModDBMDef(String pTargetBlockName) {
        this(pTargetBlockName, 0, false);
    }

    /**
     * Create a new "Block that can be replaced by ores" definition
     *
     * @param pTargetBlockName The unlocalizedName of the block
     * @param pMetaData        The blocks metadata
     */
    public ModDBMDef(String pTargetBlockName, int pMetaData) {
        this(pTargetBlockName, pMetaData, false);
    }

    /**
     * Create a new "Block that can be replaced by ores" definition
     *
     * @param pTargetBlock The instance of the block that can be replaced
     * @param pMetaData    The blocks metadata
     */
    public ModDBMDef(Block pTargetBlock, int pMetaData) {
        this(Block.blockRegistry.getNameForObject(pTargetBlock), pMetaData, false);
    }

    /**
     * Create a new "Block that can be replaced by ores" definition. Meta defaults to 0 here
     *
     * @param pTargetBlock The instance of the block that can be replaced
     */
    public ModDBMDef(Block pTargetBlock) {
        this(Block.blockRegistry.getNameForObject(pTargetBlock), 0, false);
    }

    /**
     * Create a new "Block that can be replaced by ores" definition
     *
     * @param pTargetBlock
     * @param pCanAlwaysReplace set to true if this block can always be replaced, regardless of it's metavalue. Like:
     *                          [block]:*
     */
    public ModDBMDef(Block pTargetBlock, boolean pCanAlwaysReplace) {
        this(Block.blockRegistry.getNameForObject(pTargetBlock), -1, pCanAlwaysReplace);
    }

    /**
     * Create a new "Block that can be replaced by ores" definition
     *
     * @param pTargetBlockName  The unlocalizedName of the block
     * @param pCanAlwaysReplace set to true if this block can always be replaced, regardless of it's metavalue. Like:
     *                          [block]:*
     */
    public ModDBMDef(String pTargetBlockName, boolean pCanAlwaysReplace) {
        this(pTargetBlockName, -1, false);
    }

    /**
     * Create a new "Block that can be replaced by ores" definition
     *
     * @param pTargetBlockName  The unlocalizedName of the block
     * @param pMetaData         The blocks metadata
     * @param pCanAlwaysReplace set to true if this block can always be replaced, regardless of it's metavalue. Like:
     *                          [block]:*
     */
    public ModDBMDef(String pTargetBlockName, int pMetaData, boolean pCanAlwaysReplace) {
        _targetBlockName = pTargetBlockName;
        _targetMeta = pMetaData;
        _canAlwaysReplace = pCanAlwaysReplace;
    }

    /**
     * Internal function Never run this function. It is used to update the blocks name when GalacticGreg is initializing
     * its internal structures
     *
     * @param pParentModName The modname to be attached to the block-name
     */
    public void updateBlockName(String pParentModName) {
        // Do we already have a FQBN? then do nothing
        if (_targetBlockName.contains(":")) {
            return;
        }
        _targetBlockName = String.format("%s:%s", pParentModName, _targetBlockName);
    }
}
