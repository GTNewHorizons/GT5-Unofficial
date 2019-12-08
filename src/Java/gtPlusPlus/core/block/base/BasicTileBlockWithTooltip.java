package gtPlusPlus.core.block.base;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.CubicObject;
import gtPlusPlus.api.objects.minecraft.SafeTexture;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.InventoryUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BasicTileBlockWithTooltip extends BlockContainer implements ITileTooltip {

	/**
	 * Each mapped object holds the data for the six sides.
	 */
	@SideOnly(Side.CLIENT)
	private AutoMap<CubicObject<SafeTexture>> mSidedTextureArray = new AutoMap<CubicObject<SafeTexture>>();

	/**
	 * Holds the data for the six sides, each side holds an array of data for each respective meta.
	 */
	@SideOnly(Side.CLIENT)
	private AutoMap<CubicObject<String>> mSidedTexturePathArray = new AutoMap<CubicObject<String>>();

	/**
	 * Does this block have any meta at all?
	 * @return
	 */
	public final boolean hasMeta() {
		return getMetaCount() > 0;
	}

	/**
	 * The amount of meta this block has.
	 * @return
	 */
	public abstract int getMetaCount();

	/**
	 * Does this {@link Block} require special {@link ItemBlock} handling?
	 * @return The {@link Class} that will be used for this {@link Block}.
	 */
	public Class<? extends ItemBlock> getItemBlockClass() {
		return ItemBlock.class;
	}

	/**
	 * A lazy way to declare the unlocal name for the block, makes boilerplating easy.
	 * @return The internal name for this block.
	 */
	public abstract String getUnlocalBlockName();

	/**
	 * Does this Block have {@link ITileTooltip} support?
	 * @return {@link boolean} that represents if this block supports {@link ITileTooltip} or not.
	 */
	public final boolean hasTooltip() {
		return getTooltipID() >= -1;
	}

	/**
	 * Lazy Boilerplating.
	 * @return Block Hardness.
	 */
	protected abstract float initBlockHardness();

	/**
	 * Lazy Boilerplating.
	 * @return Block Resistance.
	 */
	protected abstract float initBlockResistance();	

	/**
	 * Lazy Boilerplating.
	 * @return The {@link CreativeTab} this Block is shown on.
	 */
	protected abstract CreativeTabs initCreativeTab();

	/**
	 * The ID used by the {@link ITileTooltip} handler. Return -1 if you are not providing a custom {@link ItemBlock} in {@link #getItemBlockClass}().
	 * @return 
	 */
	@Override
	public abstract int getTooltipID();

	public BasicTileBlockWithTooltip(Material aBlockMat){
		super(aBlockMat);
		//Use Abstract method values
		this.setHardness(initBlockHardness());
		this.setResistance(initBlockResistance());
		this.setBlockName(getUnlocalBlockName());
		this.setCreativeTab(initCreativeTab());
		// Register the block last.
		GameRegistry.registerBlock(this, getItemBlockClass(), getUnlocalBlockName());		
		Logger.INFO("Registered "+getTileEntityName()+".");
		if (Utils.isClient()) {
			// Handle Textures
			handleTextures();	
		}
	}

	/**
	 * The name of the Tile Entity.
	 * @return
	 */
	protected abstract String getTileEntityName();

	/**
	 * The String used for texture pathing.
	 * @return Sanitized {@link String}, containing no spaces or illegal characters.
	 */
	private final String getTileEntityNameForTexturePathing() {
		return Utils.sanitizeString(getTileEntityName().replace(" ", ""));
	}

	/**
	 * An array of CubicObjects, one for each meta, else just a single cell array.
	 * Expected to be null regularly, as the default texture handling should suffice.
	 * Handy if re-using textures or using a non-standard structure for them. FULL texture path must be used,
	 * inclusive of the MODID and a colon.
	 * @return
	 */
	public CubicObject<String>[] getCustomTextureDirectoryObject(){
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final IIcon getIcon(final int aSide, final int aMeta) {
		return mSidedTextureArray.get(aMeta).get(aSide).getIcon();	
	}

	@Override
	public IIcon getIcon(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
		return super.getIcon(aWorld, aX, aY, aZ, aSide);
	}

	@SideOnly(Side.CLIENT)
	private final void handleTextures() {

		Logger.INFO("[TeTexture] Building Texture Maps for "+getTileEntityName()+".");
		//Store them in forge order
		//DOWN, UP, NORTH, SOUTH, WEST, EAST	

		// Default Path Name, this will make us look inside 'miscutils\textures\blocks'
		final String aPrefixTexPath = CORE.MODID + ":";
		// Default Path Name, this will make us look in the sub-directory for this Tile Entity.		
		final String aTexPathMid =  "TileEntities"+CORE.SEPERATOR+getTileEntityNameForTexturePathing()+CORE.SEPERATOR;
		// Construct a full path
		String aTexPathBuilt = aPrefixTexPath + aTexPathMid;
		// File Name Suffixes, without meta tags
		String aStringBot;
		String aStringTop;
		String aStringBack;
		String aStringFront;
		String aStringLeft;
		String aStringRight;
		// Do we provide a matrix of custom data to be used for texture processing instead?
		if (getCustomTextureDirectoryObject() != null) {
			// Get custom provided texture data.
			CubicObject<String>[] aDataMap = getCustomTextureDirectoryObject();	
			Logger.INFO("[TeTexture] Found custom texture data, using this instead. Size: "+aDataMap.length);	
			// Map each meta string data to the main map.
			for (int i=0;i<aDataMap.length;i++) {
				mSidedTexturePathArray.put(aDataMap[i]);
				Logger.INFO("Mapped value for meta "+i+".");
			}			
		}
		else {	
			Logger.INFO("[TeTexture] Processing "+(1+getMetaCount())+" sets.");		
			// Iterate once for each meta
			for (int i=0;i<(1+getMetaCount());i++) {				

				// File Name Suffixes, without meta tags
				aStringBot = "Bottom";
				aStringTop = "Top";
				aStringBack = "Back";
				aStringFront = "Front";
				aStringLeft = "Left";
				aStringRight = "Right";

				// Add tails if we have meta
				if (hasMeta()) {
					aStringBot = aStringBot + "_"+i;
					aStringTop = aStringTop + "_"+i;
					aStringBack = aStringBack + "_"+i;
					aStringFront = aStringFront + "_"+i;
					aStringLeft = aStringLeft + "_"+i;
					aStringRight = aStringRight + "_"+i;
				}
				// Append the full path
				aStringBot = aTexPathBuilt + aStringBot;
				aStringTop = aTexPathBuilt + aStringTop;
				aStringBack = aTexPathBuilt + aStringBack;
				aStringFront = aTexPathBuilt + aStringFront;
				aStringLeft = aTexPathBuilt + aStringLeft;
				aStringRight = aTexPathBuilt + aStringRight;
				// Convenience Blob
				CubicObject<String> aMetaBlob = new CubicObject<String>(aStringBot, aStringTop, aStringBack, aStringFront, aStringLeft, aStringRight);
				mSidedTexturePathArray.put(aMetaBlob);
				Logger.INFO("[TeTexture] Added Texture Path data to map for meta "+i);	
			}
		}
		Logger.INFO("[TeTexture] Map size for pathing: "+mSidedTexturePathArray.size());

		// Iteration Index
		int aIndex = 0;

		// Iterate each CubicObject, holding the six texture paths for each meta.
		for (CubicObject<String> aMetaBlob : mSidedTexturePathArray) {			
			// Make a Safe Texture for each side
			SafeTexture aBottom = SafeTexture.register(aMetaBlob.DOWN);
			SafeTexture aTop = SafeTexture.register(aMetaBlob.UP);						
			SafeTexture aBack = SafeTexture.register(aMetaBlob.NORTH);
			SafeTexture aFont = SafeTexture.register(aMetaBlob.SOUTH);						
			SafeTexture aWest = SafeTexture.register(aMetaBlob.WEST);
			SafeTexture aEast = SafeTexture.register(aMetaBlob.EAST);
			// Store them in an Array
			SafeTexture[] aInjectBlob = new SafeTexture[] {
					aBottom,
					aTop,
					aBack,
					aFont,
					aWest,
					aEast
			};
			// Convenience Blob
			CubicObject<SafeTexture> aMetaBlob2 = new CubicObject<SafeTexture>(aInjectBlob);
			// Store this Blob into
			mSidedTextureArray.put(aMetaBlob2);
			Logger.INFO("[TeTexture] Added SafeTexture data to map for meta "+(aIndex++));				
		}
		Logger.INFO("[TeTexture] Map size for registration: "+mSidedTextureArray.size());


	}

	@Override
	@SideOnly(Side.CLIENT)
	public final void registerBlockIcons(final IIconRegister aRegisterer){
		this.blockIcon = aRegisterer.registerIcon(CORE.MODID + ":" + "net");
	}

	@Override
	public abstract TileEntity createNewTileEntity(final World world, final int p_149915_2_);

	/**
	 * Called when {@link #breakBlock}() is called, but before {@link InventoryUtils#dropInventoryItems} and the super call.
	 */
	public void onBlockBreak() {

	}

	@Override
	public final void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int number) {
		onBlockBreak();
		InventoryUtils.dropInventoryItems(world, x, y, z, block);
		super.breakBlock(world, x, y, z, block, number);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public final void getSubBlocks(Item aItem, CreativeTabs p_149666_2_, List aList) {	
		if (hasMeta()) {
			for (int i=0;i<getMetaCount();i++) {
				aList.add(ItemUtils.simpleMetaStack(aItem, i, 1));
			}
		}
		else {
			aList.add(ItemUtils.getSimpleStack(aItem));			
		}				
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}



	/**
	 * Get the block's damage value (for use with pick block).
	 */
	@Override
	public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
		int l = aWorld.getBlockMetadata(aX, aY, aZ);
		return l;
	}

}