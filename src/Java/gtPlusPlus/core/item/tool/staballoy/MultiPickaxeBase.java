package gtPlusPlus.core.item.tool.staballoy;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.player.UtilsMining;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MultiPickaxeBase extends StaballoyPickaxe{

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getDurabilityForDisplay(net.minecraft.item.ItemStack)
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (super.getDurabilityForDisplay(stack) > 0){
			return super.getDurabilityForDisplay(stack);}
		return 0;
	}

	protected Boolean FACING_HORIZONTAL = true;
	protected String FACING = "north";
	protected EntityPlayer localPlayer;
	protected String lookingDirection;
	protected World localWorld;
	protected ItemStack thisPickaxe = null;
	protected final int colour;
	protected final String materialName;

	public MultiPickaxeBase(String unlocalizedName, ToolMaterial material, int materialDurability, int colour) {
		super(Utils.sanitizeString(unlocalizedName), material);
		this.setUnlocalizedName(Utils.sanitizeString(unlocalizedName));
		this.setTextureName(CORE.MODID + ":" + "itemPickaxe");
		this.FACING_HORIZONTAL=true;
		this.setMaxStackSize(1);
		this.setMaxDamage(materialDurability);
		this.colour = colour;
		this.materialName = material.name();
		GameRegistry.registerItem(this, Utils.sanitizeString(unlocalizedName));
		this.setCreativeTab(AddToCreativeTab.tabTools);
	}
	
	/*
	 * 
	 * 
	 * 
	 *  Methods 
	 * 
	 * 
	 * 
	 */
	
	public final String getMaterialName() {
		return materialName;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack iStack) {

		String name;
		if (getUnlocalizedName().toLowerCase().contains("wood")){
			name = "Wooden";
		}
		else if (getUnlocalizedName().toLowerCase().contains("cobblestone")){
			name = "Cobblestone";
		}
		else if (getUnlocalizedName().toLowerCase().contains("iron")){
			name = "Iron";
		}
		else if (getUnlocalizedName().toLowerCase().contains("gold")){
			name = "Gold";
		}
		else if (getUnlocalizedName().toLowerCase().contains("diamond")){
			name = "Diamond";
		}
		else {
			name = materialName;
		}
		return name+" Multipickaxe";
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return colour;

	}
	
	@SuppressWarnings("static-method")
	private float calculateDurabilityLoss(World world, int X, int Y, int Z){
		float bDurabilityLoss = 0;
		Boolean correctTool = false;
		float bHardness = 0;
		if (!world.isRemote){			
			try {
				Block removalist = world.getBlock(X, Y, Z);
				//Utils.LOG_WARNING(removalist.toString());

				bHardness = removalist.getBlockHardness(world, X, Y, Z);
				Utils.LOG_WARNING("Hardness: "+bHardness);

				bDurabilityLoss = (bDurabilityLoss + bHardness);
				//Utils.LOG_WARNING("Durability Loss: "+bDurabilityLoss);

				correctTool = canPickaxeBlock(removalist, world);
				Utils.LOG_WARNING(""+correctTool);

				if (!correctTool){
					return 0;
				}

			} catch (NullPointerException e){

			}
		}
		return bDurabilityLoss;
	}

	@Override
	public int doDurabilityDamage(int x){
		return x;
	}

	//Should clear up blocks quicker if I chain it.
	@Override
	public void removeBlockAndDropAsItem(World world, int X, int Y, int Z, ItemStack heldItem){
		localWorld = world;
		try {
			Block block = world.getBlock(X, Y, Z);
			float dur = calculateDurabilityLoss(world, X, Y, Z);
			Utils.LOG_WARNING(block.toString());
			String removalTool = "";
			removalTool = block.getHarvestTool(1);
			
			if (removalTool.equals("pickaxe") || UtilsMining.getBlockType(block)){				
				if (canPickaxeBlock(block, world)){
					if((block != Blocks.bedrock) && (block.getBlockHardness(world, X, Y, Z) != -1) && (block.getBlockHardness(world, X, Y, Z) <= 100) && (block != Blocks.water) && (block != Blocks.lava)){
						
						if (heldItem.getItemDamage() <= (heldItem.getMaxDamage()-dur)){
						
						block.dropBlockAsItem(world, X, Y, Z, world.getBlockMetadata(X, Y, Z), 0);
						world.setBlockToAir(X, Y, Z);
					
						}
					
					}
				}
				else {
					Utils.LOG_WARNING("Incorrect Tool for mining this block.");
				}
			}
		} catch (NullPointerException e){

		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack){
		return EnumRarity.uncommon;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		return false;
	}

}
