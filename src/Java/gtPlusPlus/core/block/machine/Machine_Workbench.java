package gtPlusPlus.core.block.machine;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbench;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.PlayerUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import ic2.core.item.tool.ItemToolWrench;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "EnderIO")
public class Machine_Workbench extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private IIcon textureTop;
    @SideOnly(Side.CLIENT)
    private IIcon textureBottom;
    @SideOnly(Side.CLIENT)
    private IIcon textureFront;

    @SuppressWarnings("deprecation")
	public Machine_Workbench()
    {
        super(Material.iron);
        this.setBlockName("blockWorkbenchGT");
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        GameRegistry.registerBlock(this, "blockWorkbenchGT");
		LanguageRegistry.addName(this, "Bronze Workbench");
        
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
        this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "bronze_side_cabinet");
        this.textureTop = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "bronze_top_crafting");
        this.textureBottom = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "bronze_side");
        this.textureFront = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "bronze_side_cabinet");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz)
	{

		ItemStack heldItem = null;
    	if (world.isRemote){
    		heldItem = PlayerUtils.getItemStackInPlayersHand();
    	}

		boolean holdingWrench = false;

		if (heldItem != null){
			holdingWrench =  isWrench(heldItem);			
		}

		if (world.isRemote) return true;

		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityWorkbench)
		{
			if (!holdingWrench){
				player.openGui(GTplusplus.instance, 3, world, x, y, z);
				return true;				
			}
			Utils.LOG_INFO("Holding a Wrench, doing wrench things instead.");
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityWorkbench();
	}
	
	public static boolean isWrench(ItemStack item){		
		if (item.getItem() instanceof ItemToolWrench){
			return true;
		}
		if (LoadedMods.BuildCraft){
			return checkBuildcraftWrench(item);
		}
		if (LoadedMods.EnderIO){
			return checkEnderIOWrench(item);
		}		
		return false;
	}
	
	@Optional.Method(modid = "EnderIO")
	private static boolean checkEnderIOWrench(ItemStack item){
		if (ReflectionUtils.doesClassExist("crazypants.enderio.api.tool.ITool")){
			Class<?> wrenchClass;
			try {
				wrenchClass = Class.forName("crazypants.enderio.api.tool.ITool");
				if (wrenchClass.isInstance(item.getItem())){
					return true;
				}
			}
			catch (ClassNotFoundException e1) {
				return false;
			}			
		}		
		return false;
	}
	
	@Optional.Method(modid = "Buildcraft")
	private static boolean checkBuildcraftWrench(ItemStack item){
		if (ReflectionUtils.doesClassExist("buildcraft.api.tools.IToolWrench")){
			Class<?> wrenchClass;
			try {
				wrenchClass = Class.forName("buildcraft.api.tools.IToolWrench");
				if (wrenchClass.isInstance(item.getItem())){
					return true;
				}
			}
			catch (ClassNotFoundException e1) {
				return false;
			}			
		}		
		return false;
	}
	
}