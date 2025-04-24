package gtPlusPlus.core.block.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;

public class BasicBlock extends BlockContainer {

    public BasicBlock(BlockTypes type, final String unlocalizedName, final Material material, final int harvestLevel) {
        super(material);
        this.setBlockName(Utils.sanitizeString(unlocalizedName));

        if (type != BlockTypes.ORE && !unlocalizedName.toLowerCase()
            .contains("ore")) {
            this.setBlockTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        }

        this.setCreativeTab(AddToCreativeTab.tabBlock);
        this.setResistance(6.0F);
        this.setLightLevel(0.0F);
        this.setHardness(1.0f * harvestLevel);
        this.setHarvestLevel("pickaxe", harvestLevel);
        this.setStepSound(soundTypeMetal);
    }

    public enum BlockTypes {

        STANDARD("blockBlock", "pickaxe", soundTypeMetal),
        FRAME("blockFrameGt", "wrench", soundTypeMetal),
        ORE("blockStone", "pickaxe", soundTypeStone);

        private final String TEXTURE_NAME;
        private final String HARVEST_TOOL;
        private final SoundType soundOfBlock;

        BlockTypes(final String textureName, final String harvestTool, final SoundType blockSound) {
            this.TEXTURE_NAME = textureName;
            this.HARVEST_TOOL = harvestTool;
            this.soundOfBlock = blockSound;
        }

        public String getTexture() {
            return this.TEXTURE_NAME;
        }

        public String getHarvestTool() {
            return this.HARVEST_TOOL;
        }

    }

    @Override
    public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
        return null;
    }

    @Override
    public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
        final int z) {
        return false;
    }
}
