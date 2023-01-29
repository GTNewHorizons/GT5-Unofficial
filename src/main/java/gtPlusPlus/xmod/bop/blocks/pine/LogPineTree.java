package gtPlusPlus.xmod.bop.blocks.pine;

import gtPlusPlus.xmod.bop.blocks.base.LogBase;

public class LogPineTree extends LogBase {

    public LogPineTree() {
        super("Pine Log", "pine", new String[] { "pine" });
        this.treeType = new String[] { "pine" };
    }

    /*
     * @Override
     * @SideOnly(Side.CLIENT) protected IIcon getSideIcon(int metaID){ return this.textureSide[metaID %
     * this.textureSide.length]; }
     * @Override
     * @SideOnly(Side.CLIENT) protected IIcon getTopIcon(int metaID){ return this.textureTop[metaID %
     * this.textureTop.length]; }
     */

}
