package gregtech.api.items;

import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.enums.Mods.AppleCore;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.interfaces.IFoodStat;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTConfig;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.render.items.GeneratedMaterialRenderer;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.food.IEdible;
import squeek.applecore.api.food.ItemFoodProxy;

/**
 * @author Gregorius Techneticies
 *         <p/>
 *         One Item for everything!
 *         <p/>
 *         This brilliant Item Class is used for automatically generating all possible variations of Material Items,
 *         like Dusts, Ingots, Gems, Plates and similar. It saves me a ton of work, when adding Items, because I always
 *         have to make a new Item SubType for each OreDict Prefix, when adding a new Material.
 *         <p/>
 *         As you can see, up to 32766 Items can be generated using this Class. And the last 766 Items can be custom
 *         defined, just to save space and MetaData.
 *         <p/>
 *         These Items can also have special RightClick abilities, electric Charge or even be set to become a Food alike
 *         Item.
 */
@Optional.Interface(iface = "squeek.applecore.api.food.IEdible", modid = Mods.Names.APPLE_CORE)
public abstract class MetaGeneratedItem extends MetaBaseItem implements IGT_ItemWithMaterialRenderer, IEdible {

    /**
     * All instances of this Item Class are listed here. This gets used to register the Renderer to all Items of this
     * Type, if useStandardMetaItemRenderer() returns true.
     * <p/>
     * You can also use the unlocalized Name gotten from getUnlocalizedName() as Key if you want to get a specific Item.
     */
    public static final ConcurrentHashMap<String, MetaGeneratedItem> sInstances = new ConcurrentHashMap<>();

    /* ---------- CONSTRUCTOR AND MEMBER VARIABLES ---------- */

    public final short mOffset, mItemAmount;
    public final BitSet mEnabledItems;
    public final BitSet mVisibleItems;
    public final IIcon[][] mIconList;

    public final ConcurrentHashMap<Short, IFoodStat> mFoodStats = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Short, Long[]> mElectricStats = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Short, Long[]> mFluidContainerStats = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Short, Short> mBurnValues = new ConcurrentHashMap<>();

    /**
     * Creates the Item using these Parameters.
     *
     * @param aUnlocalized The Unlocalized Name of this Item.
     */
    public MetaGeneratedItem(String aUnlocalized, short aOffset, short aItemAmount) {
        super(aUnlocalized);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);
        setHasSubtypes(true);
        setMaxDamage(0);
        mEnabledItems = new BitSet(aItemAmount);
        mVisibleItems = new BitSet(aItemAmount);

        mOffset = (short) Math.min(32766, aOffset);
        mItemAmount = (short) Math.min(aItemAmount, 32766 - mOffset);
        mIconList = new IIcon[aItemAmount][1];

        sInstances.put(getUnlocalizedName(), this);
    }

    /**
     * This adds a Custom Item to the ending Range.
     *
     * @param aID         The Id of the assigned Item [0 - mItemAmount] (The MetaData gets auto-shifted by +mOffset)
     * @param aEnglish    The Default Localized Name of the created Item
     * @param aToolTip    The Default ToolTip of the created Item, you can also insert null for having no ToolTip
     * @param aRandomData The OreDict Names you want to give the Item. Also used for TC Aspects and some other things.
     * @return An ItemStack containing the newly created Item.
     */
    public final ItemStack addItem(int aID, String aEnglish, String aToolTip, Object... aRandomData) {
        if (aToolTip == null) aToolTip = "";
        if (aID >= 0 && aID < mItemAmount) {
            ItemStack rStack = new ItemStack(this, 1, mOffset + aID);
            if (mEnabledItems.get(aID)) {
                throw new IllegalArgumentException(
                    String.format("ID %s is already reserved for %s!", aID, rStack.getDisplayName()));
            }
            mEnabledItems.set(aID);
            mVisibleItems.set(aID);
            GTLanguageManager.addStringLocalization(getUnlocalizedName(rStack) + ".name", aEnglish);
            GTLanguageManager.addStringLocalization(getUnlocalizedName(rStack) + ".tooltip", aToolTip);
            List<TC_AspectStack> tAspects = new ArrayList<>();
            // Important Stuff to do first
            for (Object tRandomData : aRandomData) if (tRandomData instanceof SubTag) {
                if (tRandomData == SubTag.INVISIBLE) {
                    mVisibleItems.set(aID, false);
                    continue;
                }
                if (tRandomData == SubTag.NO_UNIFICATION) {
                    GTOreDictUnificator.addToBlacklist(rStack);
                }
            }
            // now check for the rest
            for (Object tRandomData : aRandomData) if (tRandomData != null) {
                boolean tUseOreDict = true;
                if (tRandomData instanceof IFoodStat) {
                    setFoodBehavior(mOffset + aID, (IFoodStat) tRandomData);
                    if (((IFoodStat) tRandomData).getFoodAction(this, rStack) == EnumAction.eat) {
                        int tFoodValue = ((IFoodStat) tRandomData).getFoodLevel(this, rStack, null);
                        if (tFoodValue > 0) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(rStack, ItemList.IC2_Food_Can_Empty.get(tFoodValue))
                                .itemOutputs(
                                    ((IFoodStat) tRandomData).isRotten(this, rStack, null)
                                        ? ItemList.IC2_Food_Can_Spoiled.get(tFoodValue)
                                        : ItemList.IC2_Food_Can_Filled.get(tFoodValue))
                                .duration(tFoodValue * 5 * SECONDS)
                                .eut(1)
                                .addTo(cannerRecipes);
                        }
                    }
                    tUseOreDict = false;
                }
                if (tRandomData instanceof IItemBehaviour) {
                    // The cast below from is not safe. If you know how to make it safe, please do.
                    // noinspection unchecked
                    addItemBehavior(mOffset + aID, (IItemBehaviour<MetaBaseItem>) tRandomData);
                    tUseOreDict = false;
                }
                if (tRandomData instanceof IItemContainer) {
                    ((IItemContainer) tRandomData).set(rStack);
                    tUseOreDict = false;
                }
                if (tRandomData instanceof SubTag) {
                    continue;
                }
                if (tRandomData instanceof TC_AspectStack) {
                    ((TC_AspectStack) tRandomData).addToAspectList(tAspects);
                    continue;
                }
                if (tRandomData instanceof ItemData) {
                    if (GTUtility.isStringValid(tRandomData)) GTOreDictUnificator.registerOre(tRandomData, rStack);
                    else GTOreDictUnificator.addItemData(rStack, (ItemData) tRandomData);
                    continue;
                }
                if (tUseOreDict) {
                    GTOreDictUnificator.registerOre(tRandomData, rStack);
                }
            }
            if (GregTechAPI.sThaumcraftCompat != null)
                GregTechAPI.sThaumcraftCompat.registerThaumcraftAspectsToItem(rStack, tAspects, false);
            return rStack;
        }
        return null;
    }

    /**
     * Sets a Food Behavior for the Item.
     *
     * @param aMetaValue    the Meta Value of the Item you want to set it to. [0 - 32765]
     * @param aFoodBehavior the Food Behavior you want to add.
     * @return the Item itself for convenience in constructing.
     */
    public final MetaGeneratedItem setFoodBehavior(int aMetaValue, IFoodStat aFoodBehavior) {
        if (aMetaValue < 0 || aMetaValue >= mOffset + mEnabledItems.length()) return this;
        if (aFoodBehavior == null) mFoodStats.remove((short) aMetaValue);
        else mFoodStats.put((short) aMetaValue, aFoodBehavior);
        return this;
    }

    /**
     * Sets the Furnace Burn Value for the Item.
     *
     * @param aMetaValue the Meta Value of the Item you want to set it to. [0 - 32765]
     * @param aValue     200 = 1 Burn Process = 500 EU, max = 32767 (that is 81917.5 EU)
     * @return the Item itself for convenience in constructing.
     */
    public final MetaGeneratedItem setBurnValue(int aMetaValue, int aValue) {
        if (aMetaValue < 0 || aMetaValue >= mOffset + mEnabledItems.length() || aValue < 0) return this;
        if (aValue == 0) mBurnValues.remove((short) aMetaValue);
        else mBurnValues.put((short) aMetaValue, aValue > Short.MAX_VALUE ? Short.MAX_VALUE : (short) aValue);
        return this;
    }

    /**
     * @param aMetaValue     the Meta Value of the Item you want to set it to. [0 - 32765]
     * @param aMaxCharge     Maximum Charge. (if this is == 0 it will remove the Electric Behavior)
     * @param aTransferLimit Transfer Limit.
     * @param aTier          The electric Tier.
     * @param aSpecialData   If this Item has a Fixed Charge, like a SingleUse Battery (if > 0). Use -1 if you want to
     *                       make this Battery chargeable (the use and canUse Functions will still discharge if you just
     *                       use this) Use -2 if you want to make this Battery dischargeable. Use -3 if you want to make
     *                       this Battery charge/discharge-able.
     * @return the Item itself for convenience in constructing.
     */
    public final MetaGeneratedItem setElectricStats(int aMetaValue, long aMaxCharge, long aTransferLimit, long aTier,
        long aSpecialData, boolean aUseAnimations) {
        if (aMetaValue < 0 || aMetaValue >= mOffset + mEnabledItems.length()) return this;
        if (aMaxCharge == 0) mElectricStats.remove((short) aMetaValue);
        else {
            mElectricStats.put(
                (short) aMetaValue,
                new Long[] { aMaxCharge, Math.max(0, aTransferLimit), Math.max(-1, aTier), aSpecialData });
            if (aMetaValue >= mOffset && aUseAnimations) mIconList[aMetaValue - mOffset] = Arrays
                .copyOf(mIconList[aMetaValue - mOffset], Math.max(9, mIconList[aMetaValue - mOffset].length));
        }
        return this;
    }

    /**
     *
     * @param aMetaValue the Meta Value of the Item you want to set it to. [0 - 32765]
     * @param aCapacity  fluid capacity in L or mb
     * @param aStacksize item stack size
     * @return the Item itself for convenience in constructing.
     */
    public final MetaGeneratedItem setFluidContainerStats(int aMetaValue, long aCapacity, long aStacksize) {
        if (aMetaValue < 0 || aMetaValue >= mOffset + mEnabledItems.length()) return this;
        if (aCapacity < 0) mElectricStats.remove((short) aMetaValue);
        else mFluidContainerStats.put((short) aMetaValue, new Long[] { aCapacity, Math.max(1, aStacksize) });
        return this;
    }

    /**
     * @return if this MetaGenerated Item should use my Default Renderer System.
     */
    public boolean useStandardMetaItemRenderer() {
        return true;
    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        return Materials._NULL.getRGBA();
    }

    /**
     * @return the Icon the Material is going to be rendered with.
     */
    public IIconContainer getIconContainer(int aMetaData) {
        return null;
    }

    @Override
    public IIcon getIcon(int aMetaData, int pass) {
        IIconContainer iconContainer = getIconContainer(aMetaData);
        return iconContainer != null ? iconContainer.getIcon() : null;
    }

    @Override
    public IIcon getOverlayIcon(int aMetaData, int pass) {
        IIconContainer iconContainer = getIconContainer(aMetaData);
        return iconContainer != null ? iconContainer.getOverlayIcon() : null;
    }

    @Override
    public boolean shouldUseCustomRenderer(int aMetaData) {
        return true;
    }

    @Override
    public GeneratedMaterialRenderer getMaterialRenderer(int aMetaData) {
        return null;
    }

    @Override
    public boolean allowMaterialRenderer(int aMetaData) {
        return aMetaData < this.mOffset;
    }

    /* ---------- INTERNAL OVERRIDES ---------- */

    @Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        IFoodStat tStat = mFoodStats.get((short) getDamage(aStack));
        if (tStat != null && aPlayer.canEat(tStat.alwaysEdible(this, aStack, aPlayer)))
            aPlayer.setItemInUse(aStack, 32);
        return super.onItemRightClick(aStack, aWorld, aPlayer);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack aStack) {
        return mFoodStats.get((short) getDamage(aStack)) == null ? 0 : 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack aStack) {
        IFoodStat tStat = mFoodStats.get((short) getDamage(aStack));
        return tStat == null ? EnumAction.none : tStat.getFoodAction(this, aStack);
    }

    @Override
    public final ItemStack onEaten(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        IFoodStat tStat = mFoodStats.get((short) getDamage(aStack));
        if (tStat != null) {
            if (AppleCore.isModLoaded()) {
                aPlayer.getFoodStats()
                    .func_151686_a(getFoodProxy(this), aStack);
            } else {
                aPlayer.getFoodStats()
                    .addStats(tStat.getFoodLevel(this, aStack, aPlayer), tStat.getSaturation(this, aStack, aPlayer));
            }
            tStat.onEaten(this, aStack, aPlayer);
        }
        return aStack;
    }

    @Optional.Method(modid = Mods.Names.APPLE_CORE)
    private static ItemFood getFoodProxy(Object edible) {
        return new ItemFoodProxy((IEdible) edible);
    }

    @Override
    @Optional.Method(modid = Mods.Names.APPLE_CORE)
    public FoodValues getFoodValues(ItemStack aStack) {
        IFoodStat tStat = mFoodStats.get((short) getDamage(aStack));
        return tStat == null ? null
            : new FoodValues(tStat.getFoodLevel(this, aStack, null), tStat.getSaturation(this, aStack, null));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        int j = mEnabledItems.length();
        for (int i = 0; i < j; i++) if (mVisibleItems.get(i) || (D1 && mEnabledItems.get(i))) {
            Long[] tStats = mElectricStats.get((short) (mOffset + i));
            if (tStats != null && tStats[3] < 0) {
                ItemStack tStack = new ItemStack(this, 1, mOffset + i);
                setCharge(tStack, Math.abs(tStats[0]));
                isItemStackUsable(tStack);
                aList.add(tStack);
            }
            if (tStats == null || tStats[3] != -2) {
                ItemStack tStack = new ItemStack(this, 1, mOffset + i);
                isItemStackUsable(tStack);
                aList.add(tStack);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void registerIcons(IIconRegister aIconRegister) {
        short j = (short) mEnabledItems.length();
        for (short i = 0; i < j; i++) if (mEnabledItems.get(i)) {
            for (byte k = 1; k < mIconList[i].length; k++) {
                mIconList[i][k] = aIconRegister.registerIcon(
                    GregTech.getResourcePath(GTConfig.troll ? "troll" : getUnlocalizedName() + "/" + i + "/" + k));
            }
            mIconList[i][0] = aIconRegister
                .registerIcon(GregTech.getResourcePath(GTConfig.troll ? "troll" : getUnlocalizedName() + "/" + i));
        }
    }

    @Override
    public final Long[] getElectricStats(ItemStack aStack) {
        return mElectricStats.get((short) aStack.getItemDamage());
    }

    @Override
    public final Long[] getFluidContainerStats(ItemStack aStack) {
        return mFluidContainerStats.get((short) aStack.getItemDamage());
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack aStack, ItemStack aBook) {
        return false;
    }

    @Override
    public boolean getIsRepairable(ItemStack aStack, ItemStack aMaterial) {
        return false;
    }
}
