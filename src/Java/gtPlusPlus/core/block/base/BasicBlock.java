package gtPlusPlus.core.block.base;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BasicBlock extends BlockContainer {

    public BasicBlock(final String unlocalizedName, final Material material) {
        super(material);
        this.setBlockName(Utils.sanitizeString(unlocalizedName));
        this.setBlockTextureName(CORE.MODID + ":" + unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabBlock);
        this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setLightLevel(0.0F);
        this.setHarvestLevel("pickaxe", 2);
        this.setStepSound(soundTypeMetal);
    }

    
    public static enum BlockTypes {
    	STANDARD("blockBlock", "pickaxe", soundTypeStone),
    	FRAME("blockFrameGt", "wrench", soundTypeMetal);
    
    	private String TEXTURE_NAME;
    	private String HARVEST_TOOL;
    	private SoundType soundOfBlock;
    	private BlockTypes (final String textureName, final String harvestTool, final SoundType blockSound)
    	{
    		this.TEXTURE_NAME = textureName;
    		this.HARVEST_TOOL = harvestTool;
    		this.soundOfBlock = blockSound;
    	}

    	public String getTexture() {
    		return TEXTURE_NAME;
    	}
    	
    	public String getHarvestTool(){
    		return HARVEST_TOOL;
    	}
    	
    	public SoundType getBlockSoundType(){
    		return soundOfBlock;
    	}
    
    }


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return null;
	}
    
}
