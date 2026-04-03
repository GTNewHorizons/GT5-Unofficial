package gtPlusPlus.core.block.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gregtech.api.util.StringUtils;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class BasicBlock extends BlockContainer {

    public BasicBlock(BlockTypes type, final String unlocalizedName, final Material material, final int harvestLevel) {
        super(material);
        this.setBlockName(StringUtils.sanitizeString(unlocalizedName));

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

        STANDARD("blockBlock", "pickaxe", soundTypeMetal, "Block of %s"),
        FRAME("blockFrameGt", "wrench", soundTypeMetal, "%s Frame Box"),
        ORE("blockStone", "pickaxe", soundTypeStone, "%s Ore [Old]");

        private final String TEXTURE_NAME;
        private final String HARVEST_TOOL;
        private final SoundType soundOfBlock;
        private final String properName;

        public String getProperName() {
            return properName;
        }

        BlockTypes(final String textureName, final String harvestTool, final SoundType blockSound, String properName) {
            this.TEXTURE_NAME = textureName;
            this.HARVEST_TOOL = harvestTool;
            this.soundOfBlock = blockSound;
            this.properName = properName;
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
