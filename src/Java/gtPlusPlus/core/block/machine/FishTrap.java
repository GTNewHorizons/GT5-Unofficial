package gtPlusPlus.core.block.machine;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityFishtrap;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FishTrap extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private IIcon textureTop;
    @SideOnly(Side.CLIENT)
    private IIcon textureBottom;
    @SideOnly(Side.CLIENT)
    private IIcon textureFront;

    
	@SuppressWarnings("deprecation")
	public FishTrap()
    {
        super(Material.wood);
        this.setBlockName("blockFishTrap");
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        GameRegistry.registerBlock(this, "blockFishTrap");
		LanguageRegistry.addName(this, "Fish Catcher");
        
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.textureTop : (p_149691_1_ == 0 ? this.textureBottom : (p_149691_1_ != 2 && p_149691_1_ != 4 ? this.blockIcon : this.textureFront));
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "fishtrap");
        this.textureTop = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "fishtrap");
        this.textureBottom = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "fishtrap");
        this.textureFront = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "fishtrap");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz)
	{
		if (world.isRemote) return true;

		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityFishtrap){
				player.openGui(GTplusplus.instance, 5, world, x, y, z);
				return true;				
			}
		return false;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityFishtrap();
	}
	
}