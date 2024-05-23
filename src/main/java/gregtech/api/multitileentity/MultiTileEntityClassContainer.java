package gregtech.api.multitileentity;

import static gregtech.api.enums.GT_Values.NBT;

import java.lang.ref.WeakReference;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;

import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.base.MultiTileEntity;
import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.util.GT_Util;
import gregtech.common.tileentities.casings.upgrade.Inventory;
import gregtech.common.tileentities.casings.upgrade.Tank;

public class MultiTileEntityClassContainer {

    private final WeakReference<MultiTileEntityRegistry> registry;
    private String unlocalizedName;
    private String categoryName;

    private final int metaId;
    private Class<? extends MultiTileEntity> clazz;
    private MultiTileEntityBlock block;
    private MultiTileEntity originalTileEntity;
    private NBTTagCompound parameters;

    // These have defaults
    private int stackSize = 64;
    private boolean hidden = false;

    public MultiTileEntityClassContainer(MultiTileEntityRegistry registry, MultiTileEntityBlock block, int metaId,
        Class<? extends MultiTileEntity> clazz) {
        /* Start the Builder */
        this.registry = new WeakReference<>(registry);
        this.block = block;
        this.metaId = metaId;
        this.clazz = clazz;
        this.parameters = new NBTTagCompound();
    }

    public boolean isHidden() {
        return hidden;
    }

    public int getMetaId() {
        return metaId;
    }

    public int getStackSize() {
        return stackSize;
    }

    public Class<? extends MultiTileEntity> getClazz() {
        return clazz;
    }

    public MultiTileEntity getOriginalTileEntity() {
        return originalTileEntity;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public NBTTagCompound getParameters() {
        return parameters;
    }

    public boolean register() {
        /* End and register the Builder with the registry */
        final MultiTileEntityRegistry registry = this.registry.get();

        if (parameters.hasKey(NBT.MATERIAL) && !parameters.hasKey(NBT.COLOR)) parameters.setInteger(
            NBT.COLOR,
            GT_Util.getRGBInt(
                Materials.get(parameters.getString(NBT.MATERIAL))
                    .getRGBA()));

        try {
            originalTileEntity = clazz.newInstance();
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        originalTileEntity.initFromNBT(parameters, Block.getIdFromBlock(block), metaId);

        return registry != null && registry.add(this.categoryName, this) != null;
    }

    public MultiTileEntityClassContainer unlocalizedName(String unlocalized) {
        unlocalizedName = unlocalized;
        return this;
    }

    public MultiTileEntityClassContainer category(String aCategoryName) {
        categoryName = aCategoryName;
        return this;
    }

    public MultiTileEntityClassContainer stackSize(int aStackSize) {
        stackSize = aStackSize;
        return this;
    }

    public MultiTileEntityClassContainer hide() {
        hidden = true;
        return this;
    }

    /* These methods are builder methods for commonly used NBT tags */

    // Need a base texture for the MTE machine, and then a separate texture set for the machine/active overlays

    public MultiTileEntityClassContainer material(Materials material) {
        // Sets the material, and the color from the material, if not already set
        parameters.setString(NBT.MATERIAL, material.toString());
        if (!parameters.hasKey(NBT.COLOR)) parameters.setInteger(NBT.COLOR, GT_Util.getRGBInt(material.getRGBA()));
        return this;
    }

    public MultiTileEntityClassContainer color(int rbg) {
        parameters.setInteger(NBT.COLOR, rbg);
        return this;
    }

    public MultiTileEntityClassContainer color(short[] rgba) {
        parameters.setInteger(NBT.COLOR, GT_Util.getRGBInt(rgba));
        return this;
    }

    public MultiTileEntityClassContainer textureFolder(String texture) {
        parameters.setString(NBT.TEXTURE_FOLDER, texture);
        return this;
    }

    public MultiTileEntityClassContainer inputInventorySize(int aSize) {
        parameters.setInteger(NBT.INV_INPUT_SIZE, aSize);
        return this;
    }

    public MultiTileEntityClassContainer outputInventorySize(int aSize) {
        parameters.setInteger(NBT.INV_OUTPUT_SIZE, aSize);
        return this;
    }

    public MultiTileEntityClassContainer tankCapacity(Long aCapacity) {
        parameters.setLong(NBT.TANK_CAPACITY, aCapacity);
        return this;
    }

    public MultiTileEntityClassContainer tier(int aTier) {
        verifyDescendentOfMultiple(true, UpgradeCasing.class, FunctionalCasing.class);
        parameters.setInteger(NBT.TIER, aTier);
        return this;
    }

    public MultiTileEntityClassContainer upgradeInventorySize(int aSize) {
        verifyDescendentOf(Inventory.class);

        parameters.setInteger(NBT.UPGRADE_INVENTORY_SIZE, aSize);
        return this;
    }

    public MultiTileEntityClassContainer upgradeTankCount(int count) {
        verifyDescendentOf(Tank.class);

        parameters.setInteger(NBT.UPGRADE_TANK_COUNT, count);
        return this;
    }

    public MultiTileEntityClassContainer upgradeTankCapacity(Long aCapacity) {
        parameters.setLong(NBT.UPGRADE_TANK_CAPACITY, aCapacity);
        return this;
    }

    public MultiTileEntityClassContainer upgradeAmperage(long amperage) {
        parameters.setLong(NBT.UPGRADE_AMPERAGE, amperage);
        return this;
    }

    @SuppressWarnings("unused")
    public MultiTileEntityClassContainer setNBT(String key, Object val) {
        return setNBT(new Tuple(key, val));
    }

    public MultiTileEntityClassContainer setNBT(Tuple... aTags) {
        /*
         * Merge in arbitrary NBT tuples of (key, value). Useful for anything for which a custom method has not yet been
         * exposed
         */
        parameters = GT_Util.fuseNBT(parameters, GT_Util.makeNBT(aTags));
        return this;
    }

    private void verifyDescendentOf(Class<?> cls) {
        // Check if cls is extended by mClass
        if (!cls.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                "Expected a descendent of " + cls.getName() + " got " + clazz.getName() + " instead.");
        }
    }

    private void verifyDescendentOfMultiple(boolean onlyOne, Class<?>... classes) {
        boolean atLeastOne = false;
        String classNames = "";
        for (Class<?> cls : classes) {
            classNames += cls.getName() + " ";
            if (!onlyOne) {
                verifyDescendentOf(cls);
                atLeastOne = true;
            } else if (cls.isAssignableFrom(clazz)) {
                atLeastOne = true;
            }
        }

        if (!atLeastOne) {
            throw new IllegalArgumentException(
                "Expected a descendent of any of these " + classNames + " got " + clazz.getName() + " instead.");
        }
    }
}
