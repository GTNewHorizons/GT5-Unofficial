package gregtech.common.items;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;

/** This class holds cells for non-GT fluids. */
public class GT_MetaGenerated_Item_98 extends GT_MetaGenerated_Item {

    public static GT_MetaGenerated_Item_98 INSTANCE;

    /**
     * Registered fluids.
     *
     * <p>
     * When adding a fluid, don't forget to make sure that GregTech loads after the mod that adds that fluid!
     *
     * <p>
     * In order to avoid breaking existing worlds, fluids must not have their IDs changed! The only safe modification
     * that can be made to this enum is adding new fluids, or removing existing fluids. When removing fluids, maybe
     * leave a comment mentioning the old ID, so that we don't re-use it for a new fluid.
     */
    public enum FluidCell {
        // Next unused ID: 22

        // GregTech
        DRILLING_FLUID(5, "liquid_drillingfluid", CellType.REGULAR),
        SQUID_INK(6, "squidink", CellType.SMALL),

        // New Horizons Core Mod
        UNKNOWN_NUTRIENT_AGAR(7, "unknownnutrientagar", CellType.REGULAR),
        SEAWEED_BROTH(8, "seaweedbroth", CellType.REGULAR),
        SUPER_LIGHT_RADOX(9, "superlightradox", CellType.REGULAR),
        SODIUM_POTASSIUM(18, "sodiumpotassium", CellType.REGULAR),
        ENRICHED_BACTERIAL_SLUDGE(19, "enrichedbacterialsludge", CellType.REGULAR),
        FERMENTED_BACTERIAL_SLUDGE(20, "fermentedbacterialsludge", CellType.REGULAR),
        POLLUTION(21, "pollution", CellType.REGULAR),

        // BartWorks
        ENZYME_SOLUTION(10, "enzymessollution", CellType.REGULAR),
        ESCHERICHIA_COLI_FLUID(11, "escherichiakolifluid", CellType.REGULAR),
        PENICILLIN(12, "penicillin", CellType.REGULAR),
        FLUORESCENT_DNA(13, "fluorecentddna", CellType.REGULAR),
        POLYMERASE(17, "polymerase", CellType.REGULAR),

        // Good Generator
        COMBUSTION_PROMOTER(14, "combustionpromotor", CellType.REGULAR),

        // Galacticraft
        BACTERIAL_SLUDGE(1, "bacterialsludge", CellType.REGULAR),

        // Railcraft
        STEAM(0, "steam", CellType.REGULAR),

        // Gendustry
        BACTERIA(15, "binnie.bacteria", CellType.REGULAR),
        MUTAGEN(2, "mutagen", CellType.REGULAR),
        LIQUID_DNA(16, "liquiddna", CellType.REGULAR),

        // Tinker's Construct
        LIQUID_ENDER(3, "ender", CellType.REGULAR),

        // Hardcore Ender Expansion
        ENDER_GOO(4, "endergoo", CellType.REGULAR),;

        private final int mId;
        /** This is the Forge internal fluid name. */
        private final String mfluidName;

        private final CellType mType;

        @Nullable
        private ItemStack mStack;

        FluidCell(int aId, String aFluidName, CellType aType) {
            mId = aId;
            mfluidName = aFluidName;
            mType = aType;
        }

        public int getId() {
            return mId;
        }

        public String getFluidName() {
            return mfluidName;
        }

        public CellType getDisplayType() {
            return mType;
        }

        /**
         * Get a copy of this stack with stack size 1.
         *
         * Always returns non-null stack even if the fluid referenced doesn't exist, so don't assume it's always valid.
         */
        public ItemStack get() {
            trySetStack();
            return GT_Utility.copy(mStack);
        }

        /**
         * Get a copy of this cell WITHOUT copy.
         *
         * Always returns non-null stack even if the fluid referenced doesn't exist, so don't assume it's always valid.
         *
         * Use with caution.
         */
        public ItemStack getNoCopy() {
            trySetStack();
            return mStack;
        }

        /**
         * Get a copy of this cell with specified stack size.
         *
         * Always returns non-null stack even if the fluid referenced doesn't exist, so don't assume it's always valid.
         */
        public ItemStack get(int aStackSize) {
            trySetStack();
            return GT_Utility.copyAmount(aStackSize, mStack);
        }

        private void trySetStack() {
            if (mStack == null) {
                mStack = new ItemStack(GT_MetaGenerated_Item_98.INSTANCE, 1, mId);
            }
        }
    }

    /** Cell type specifies the cell capacity, appearance, and item name format. */
    private enum CellType {

        REGULAR(1_000, OrePrefixes.cell),
        SMALL(144, OrePrefixes.cell),
        MOLTEN(144, OrePrefixes.cellMolten),
        PLASMA(1_000, OrePrefixes.cellPlasma);

        private final int capacity;
        private final OrePrefixes prefix;

        CellType(int capacity, OrePrefixes prefix) {
            this.capacity = capacity;
            this.prefix = prefix;
        }
    }

    /** Struct class holding data that we need to properly handle a registered fluid cell item. */
    private static class RegisteredFluidData {

        private final short[] rgba;
        private final IIconContainer iconContainer;

        private RegisteredFluidData(short[] rgba, IIconContainer iconContainer) {
            this.rgba = rgba;
            this.iconContainer = iconContainer;
        }
    }

    /**
     * Map of ID to registered fluid data.
     *
     * <p>
     * Only contains IDs that were successfully registered.
     */
    private final Map<Integer, RegisteredFluidData> registeredFluidDataMap;

    private final EnumMap<CellType, IIconContainer> iconContainerMap;

    private GT_MetaGenerated_Item_98() {
        // For some reason, fluid cells will be rendered only if the metadata ID is less than the
        // offset. So we will specify maximum offset here.
        // See: GT_MetaGenerated_Item_Renderer.java
        super("metaitem.98", (short) 32766, (short) 0);

        registeredFluidDataMap = new HashMap<>();
        iconContainerMap = new EnumMap<>(CellType.class);
    }

    /**
     * Loading needs to happen after the fluids we need have been registered, which means during post-load. However,
     * cell icons seem to be deleted some time between load and post-load, so we must pre-cache them.
     */
    public static synchronized void preInit() {
        if (INSTANCE == null) INSTANCE = new GT_MetaGenerated_Item_98();

        // We'll just steal the icons from Water. They are all the same anyway (except _NULL is broken for cells).
        for (CellType cellType : CellType.values()) {
            IIconContainer iconContainer = Materials.Water.mIconSet.mTextures[cellType.prefix.mTextureIndex];
            INSTANCE.iconContainerMap.put(cellType, iconContainer);
        }
    }

    public static synchronized void init() {
        INSTANCE.createAllItems();
        INSTANCE.registerOreDict();
    }

    private void createAllItems() {
        for (FluidCell tCell : FluidCell.values()) {
            tryToInitialize(tCell);
        }

        // We're not going to use these BitSets, so clear them to save memory.
        mEnabledItems.clear();
        mVisibleItems.clear();
    }

    private void tryToInitialize(FluidCell aCell) {
        final boolean isStackAlreadySet = aCell.mStack != null;

        int id = aCell.getId();
        String fluidName = aCell.getFluidName();
        CellType cellType = aCell.getDisplayType();

        // We'll check for ID uniqueness. Better to throw an exception than silently overwrite some
        // fluid cells with other fluids due to ID collision.
        if (registeredFluidDataMap.containsKey(id)) {
            throw new IllegalStateException("Got ID collision for ID: " + id);
        }

        aCell.trySetStack();

        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null) {
            // The fluid is not guaranteed to exist.
            // These fluids are non-GT fluids, so the mod may not be present.
            if (isStackAlreadySet) {
                throw new RuntimeException(
                    "Cell item for fluid " + fluidName
                        + " has already been created, but the fluid doesn't exist during postload");
            } else {
                // fluid doesn't exist and this item has not been referenced
                return;
            }
        }

        FluidStack fluidStack = new FluidStack(fluid, cellType.capacity);

        ItemStack emptyCell = ItemList.Cell_Empty.get(1L);
        FluidContainerRegistry
            .registerFluidContainer(new FluidContainerRegistry.FluidContainerData(fluidStack, aCell.mStack, emptyCell));

        GT_LanguageManager.addStringLocalization(
            getUnlocalizedName(aCell.mStack) + ".name",
            cellType.prefix.mLocalizedMaterialPre + fluid.getLocalizedName(fluidStack)
                + cellType.prefix.mLocalizedMaterialPost);

        int color = fluid.getColor();
        short[] rgba = GT_Util.getRGBaArray(color);

        registeredFluidDataMap.put(id, new RegisteredFluidData(rgba, iconContainerMap.get(cellType)));
    }

    private void registerOreDict() {
        // The GregTech ore dictionary requires an entry in the Materials enum, and since the whole
        // point of this class is to add cell items for non-GregTech fluids, the vast majority of
        // cell items won't have an associated material. So only a rare few cell items will need to
        // be registered.

        // Register IC2 steam cell and Railcraft steam cell as synonyms.
        // There is no steam material, so we'll use Water.cellMolten instead.
        GT_OreDictUnificator.add(OrePrefixes.cellMolten, Materials.Water, GT_ModHandler.getIC2Item("steamCell", 1L));
        GT_OreDictUnificator.add(OrePrefixes.cellMolten, Materials.Water, FluidCell.STEAM.getNoCopy());
    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        RegisteredFluidData fluidData = registeredFluidDataMap.get(aStack.getItemDamage());
        if (fluidData == null) {
            return Materials._NULL.mRGBa;
        }

        return fluidData.rgba;
    }

    @Override
    public ItemStack getContainerItem(ItemStack aStack) {
        return ItemList.Cell_Empty.get(1L);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        Arrays.stream(FluidCell.values())
            .filter(fluid -> FluidRegistry.getFluid(fluid.getFluidName()) != null)
            .map(FluidCell::get)
            .filter(Objects::nonNull)
            .forEach(aList::add);
    }

    @Override
    public final IIcon getIconFromDamage(int aMetaData) {
        IIconContainer iconContainer = getIconContainer(aMetaData);
        if (iconContainer != null) {
            return iconContainer.getIcon();
        }
        return null;
    }

    @Override
    public IIconContainer getIconContainer(int aMetaData) {
        RegisteredFluidData fluidData = registeredFluidDataMap.get(aMetaData);
        if (fluidData == null) {
            return null;
        }
        return fluidData.iconContainer;
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        return 64;
    }
}
