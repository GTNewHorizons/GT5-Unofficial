package gregtech.api.items.extendedmaterials;

import static gregtech.api.enums.GT_Values.M;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

/**
 * @author Gregorius Techneticies
 *         <p/>
 *         One Item for everything!
 *         <p/>
 *         This brilliant Item Class is used for automatically generating all possible variations of Material Items, like Dusts, Ingots, Gems, Plates and similar.
 *         It saves me a ton of work, when adding Items, because I always have to make a new Item SubType for each OreDict Prefix, when adding a new Material.
 *         <p/>
 *         As you can see, up to 32766 Items can be generated using this Class. And the last 766 Items can be custom defined, just to save space and MetaData.
 *         <p/>
 *         These Items can also have special RightClick abilities, electric Charge or even be set to become a Food alike Item.
 */
public class GT_MetaGenerated_Item_ExtendedMaterials_Base extends GT_MetaGenerated_Item {

    protected final OrePrefixes mGeneratedPrefix;
    private final String mToolTipPurify = GT_LanguageManager.addStringLocalization("metaitem.01.tooltip.purify", "Throw into Cauldron to get clean Dust");
    public static HashMap<OrePrefixes, GT_MetaGenerated_Item_ExtendedMaterials_Base> sInstanceMap = new HashMap<OrePrefixes, GT_MetaGenerated_Item_ExtendedMaterials_Base>();
    
    
    /**
     * Creates the Item using these Parameters.
     *
     * @param tPrefix The OreDict Prefixes you want to have generated.
     */
    public GT_MetaGenerated_Item_ExtendedMaterials_Base(OrePrefixes tPrefix) {
        super("metaitem.exmat."+tPrefix.name().toLowerCase(), (short) 32000, (short) 766);
        mGeneratedPrefix = tPrefix;
        sInstanceMap.put(tPrefix, this);
        for (int i = 0; i < 32000; i++) {
            Materials tMaterial = GregTech_API.sGeneratedExtendedMaterials[i];
            if (tMaterial == null) continue;
            if (doesMaterialAllowGeneration(tPrefix, tMaterial)) {
                ItemStack tStack = new ItemStack(this, 1, i);
                GT_LanguageManager.addStringLocalization(getUnlocalizedName(tStack) + ".name", GT_LanguageManager.i18nPlaceholder ? getDefaultLocalizationFormat(tPrefix, tMaterial, i) : getDefaultLocalization(tPrefix, tMaterial, i));
                GT_LanguageManager.addStringLocalization(getUnlocalizedName(tStack) + ".tooltip", tMaterial.getToolTip(tPrefix.mMaterialAmount / M));
                if (tPrefix.mIsUnificatable) {
                    GT_OreDictUnificator.set(tPrefix, tMaterial, tStack);
                }
                else {
                    GT_OreDictUnificator.registerOre(tPrefix.get(tMaterial), tStack);
                }
				/*if ((tPrefix == OrePrefixes.stick || tPrefix == OrePrefixes.wireFine || tPrefix == OrePrefixes.ingot) && (tMaterial == Materials.Lead || tMaterial == Materials.Tin || tMaterial == Materials.SolderingAlloy)) {
				    GregTech_API.sSolderingMetalList.add(tStack);
				}*/
            }
        }
    }

	/* ---------- OVERRIDEABLE FUNCTIONS ---------- */

    /**
     * @return the Color Modulation the Material is going to be rendered with.
     */
    @Override
    public short[] getRGBa(ItemStack aStack) {
        Materials tMaterial = GregTech_API.sGeneratedExtendedMaterials[getDamage(aStack)];
        return tMaterial == null ? Materials._NULL.mRGBa : tMaterial.mRGBa;
    }

    /**
     * @param aPrefix   this can be null, you have to return false in that case
     * @param aMaterial this can be null, you have to return false in that case
     * @return if this Item should be generated and visible.
     */
    public boolean doesMaterialAllowGeneration(OrePrefixes aPrefix, Materials aMaterial) {
        // You have to check for at least these Conditions in every Case! So add a super Call like the following for this before executing your Code:
        // if (!super.doesMaterialAllowGeneration(aPrefix, aMaterial)) return false;
        return aPrefix != null && aMaterial != null && aPrefix.doGenerateItem(aMaterial);
    }
	
	/* ---------- OVERRIDEABLE FUNCTIONS ---------- */

    /**
     * @param aPrefix   the OreDict Prefix
     * @param aMaterial the Material
     * @param aMetaData a Index from [0 - 31999]
     * @return the Localized Name when default LangFiles are used.
     */
    public String getDefaultLocalization(OrePrefixes aPrefix, Materials aMaterial, int aMetaData) {
        return aPrefix.getDefaultLocalNameForItem(aMaterial);
    }

    /**
     * @param aPrefix   the OreDict Prefix
     * @param aMaterial the Material
     * @param aMetaData a Index from [0 - 31999]
     * @return the Localized Name Format when default LangFiles are used.
     */
    public String getDefaultLocalizationFormat(OrePrefixes aPrefix, Materials aMaterial, int aMetaData) {
        return aPrefix.getDefaultLocalNameFormatForItem(aMaterial);
    }

    /**
     * @param aMetaData a Index from [0 - 31999]
     * @param aMaterial the Material
     * @return an Icon Container for the Item Display.
     */
    public final IIconContainer getIconContainer(int aMetaData, Materials aMaterial) {
        return mGeneratedPrefix != null && mGeneratedPrefix.mTextureIndex >= 0 ? aMaterial.mIconSet.mTextures[mGeneratedPrefix.mTextureIndex] : null;
    }
	
	/* ---------- INTERNAL OVERRIDES ---------- */

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
    	String aName = super.getItemStackDisplayName(aStack);
    	int aDamage = aStack.getItemDamage();
    	if (aDamage < 32000 && aDamage >= 0)
    		return Materials.getLocalizedNameForItem(aName, aDamage);
    	return aName;
    }

    @Override
    public final IIconContainer getIconContainer(int aMetaData) {
        return GregTech_API.sGeneratedExtendedMaterials[aMetaData] == null ? null : getIconContainer(aMetaData, GregTech_API.sGeneratedExtendedMaterials[aMetaData]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void getSubItems(Item var1, CreativeTabs aCreativeTab, List aList) {
        for (int i = 0; i < 32000; i++) {
            OrePrefixes aPrefix = mGeneratedPrefix;
            Materials aMaterial = GregTech_API.sGeneratedExtendedMaterials[i];
            if (aPrefix != null && aMaterial != null) {
                if (doesMaterialAllowGeneration(aPrefix, aMaterial) && doesShowInCreative(aPrefix, aMaterial, GregTech_API.sDoShowAllItemsInCreative)) {
                    ItemStack tStack = new ItemStack(this, 1, i);
                    isItemStackUsable(tStack);
                    aList.add(tStack);
                }
            }
        }
        super.getSubItems(var1, aCreativeTab, aList);
    }

    @Override
    public final IIcon getIconFromDamage(int aMetaData) {
        if (aMetaData < 0) {
        	return null;
        }
        if (aMetaData < 32000) {
            Materials tMaterial = GregTech_API.sGeneratedExtendedMaterials[aMetaData];
            if (tMaterial == null) {
            	return null;
            }
            IIconContainer tIcon = getIconContainer(aMetaData, tMaterial);
            if (tIcon != null) {
            	return tIcon.getIcon();
            }
            return null;
        }
        return aMetaData - 32000 < mIconList.length ? mIconList[aMetaData - 32000][0] : null;
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        int tDamage = getDamage(aStack);
        if (tDamage < 32000 && mGeneratedPrefix != null) {
        	return Math.min(super.getItemStackLimit(aStack), mGeneratedPrefix.mDefaultStackSize);
        }
        return super.getItemStackLimit(aStack);
    }
    
    private static final Map<Materials,Materials> cauldronRemap =new HashMap<>();

    public static void registerCauldronCleaningFor(Materials in,Materials out){
        cauldronRemap.put(in,out);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem aItemEntity) {
        int aDamage = aItemEntity.getEntityItem().getItemDamage();
        if ((aDamage < 32000) && (aDamage >= 0) && (!aItemEntity.worldObj.isRemote)) {
            Materials aMaterial = GregTech_API.sGeneratedExtendedMaterials[aDamage];
            if ((aMaterial != null) && (aMaterial != Materials.Empty) && (aMaterial != Materials._NULL)) {
                int tX = MathHelper.floor_double(aItemEntity.posX);
                int tY = MathHelper.floor_double(aItemEntity.posY);
                int tZ = MathHelper.floor_double(aItemEntity.posZ);
                OrePrefixes aPrefix = this.mGeneratedPrefix;
                if ((aPrefix == OrePrefixes.dustImpure) || (aPrefix == OrePrefixes.dustPure)) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {

                        aMaterial= cauldronRemap.getOrDefault(aMaterial,aMaterial);

                        aItemEntity.setEntityItemStack(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, aItemEntity.getEntityItem().stackSize));
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                } else if (aPrefix == OrePrefixes.crushed) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                        aItemEntity.setEntityItemStack(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, aItemEntity.getEntityItem().stackSize));
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                } else if(aPrefix == OrePrefixes.dust && aMaterial == Materials.Wheat) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                        aItemEntity.setEntityItemStack(ItemList.Food_Dough.get(aItemEntity.getEntityItem().stackSize));
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        int aDamage = aStack.getItemDamage();
        if ((aDamage < 32000) && (aDamage >= 0)) {
            Materials aMaterial = GregTech_API.sGeneratedExtendedMaterials[aDamage];
            if ((aMaterial != null) && (aMaterial != Materials.Empty) && (aMaterial != Materials._NULL)) {
                OrePrefixes aPrefix = this.mGeneratedPrefix;
                if ((aPrefix == OrePrefixes.dustImpure) || (aPrefix == OrePrefixes.dustPure)) {
                    aList.add(this.mToolTipPurify);
                }
            }
        }
    }

    public boolean isPlasmaCellUsed(OrePrefixes aPrefix, Materials aMaterial) {
        Collection<GT_Recipe> fusionRecipes = GT_Recipe.GT_Recipe_Map.sFusionRecipes.mRecipeList;
        if(aPrefix == OrePrefixes.cellPlasma && aMaterial.getPlasma(1L) != null) { //Materials has a plasma fluid
            for(GT_Recipe recipe : fusionRecipes) { //Loop through fusion recipes
                if(recipe.getFluidOutput(0) != null) { //Make sure fluid output can't be null (not sure if possible)
                    if (recipe.getFluidOutput(0).isFluidEqual(aMaterial.getPlasma(1L)))
                        return true; //Fusion recipe output matches current plasma cell fluid
                }
            }
        }
        return false;
    }
    
    /**
     * @param aPrefix         always != null
     * @param aMaterial       always != null
     * @param aDoShowAllItems this is the Configuration Setting of the User, if he wants to see all the Stuff like Tiny Dusts or Crushed Ores as well.
     * @return if this Item should be visible in NEI or Creative
     */
    public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return (aDoShowAllItems) || (((aPrefix != OrePrefixes.gem) || (!aMaterial.mName.startsWith("Infused"))) && (aPrefix != OrePrefixes.dustTiny) && (aPrefix != OrePrefixes.dustSmall) && (aPrefix != OrePrefixes.dustImpure) && (aPrefix != OrePrefixes.dustPure) && (aPrefix != OrePrefixes.crushed) && (aPrefix != OrePrefixes.crushedPurified) && (aPrefix != OrePrefixes.crushedCentrifuged) && (aPrefix != OrePrefixes.ingotHot) && !(aPrefix == OrePrefixes.cellPlasma && !isPlasmaCellUsed(aPrefix, aMaterial)));
    }

    public ItemStack getContainerItem(ItemStack aStack) {
        int aDamage = aStack.getItemDamage();
        if (aDamage < 32000 && aDamage >= 0) {
            Materials aMaterial = GregTech_API.sGeneratedExtendedMaterials[aDamage];
            if (aMaterial != null && aMaterial != Materials.Empty && aMaterial != Materials._NULL) {
                if (mGeneratedPrefix != null) return GT_Utility.copyAmount(1, mGeneratedPrefix.mContainerItem);
            }
        }
        return null;
    }
    
}
