package gregtech.api.multitileentity;

import static gregtech.api.enums.GTValues.NBT;

import java.lang.ref.WeakReference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;

import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.base.MultiTileEntity;
import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.util.GTUtil;
import gregtech.common.tileentities.casings.upgrade.Inventory;
import gregtech.common.tileentities.casings.upgrade.Tank;

public class MultiTileEntityClassContainer {

    private final WeakReference<MultiTileEntityRegistry> registry;
    private String localized;
    private String categoryName;

    private final short muteID;
    private final Class<? extends MultiTileEntity> muteClass;
    private MultiTileEntity referenceTileEntity;
    private NBTTagCompound parameters;

    public boolean hidden = false;

    public MultiTileEntityClassContainer(MultiTileEntityRegistry aRegistry, int aID,
        Class<? extends MultiTileEntity> aClass) {
        /* Start the Builder */
        registry = new WeakReference<>(aRegistry);
        muteID = (short) aID;
        muteClass = aClass;
        parameters = new NBTTagCompound();
    }

    public boolean register() {
        /* End and register the Builder with the registry */
        final MultiTileEntityRegistry registry = this.registry.get();

        if (parameters.hasKey(NBT.MATERIAL) && !parameters.hasKey(NBT.COLOR)) parameters.setInteger(
            NBT.COLOR,
            GTUtil.getRGBInt(
                Materials.get(parameters.getString(NBT.MATERIAL))
                    .getRGBA()));

        try {
            referenceTileEntity = muteClass.getDeclaredConstructor()
                .newInstance();
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        referenceTileEntity.initFromNBT(parameters, muteID, (short) -1);

        return registry != null && registry.add(this.localized, this) != null;
    }

    public MultiTileEntityClassContainer name(String aName) {
        localized = aName;
        return this;
    }

    public MultiTileEntityClassContainer category(String aCategoryName) {
        categoryName = aCategoryName;
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
        if (!parameters.hasKey(NBT.COLOR)) parameters.setInteger(NBT.COLOR, GTUtil.getRGBInt(material.getRGBA()));
        return this;
    }

    public MultiTileEntityClassContainer color(int rbg) {
        parameters.setInteger(NBT.COLOR, rbg);
        return this;
    }

    public MultiTileEntityClassContainer color(short[] rgba) {
        parameters.setInteger(NBT.COLOR, GTUtil.getRGBInt(rgba));
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
        parameters = GTUtil.fuseNBT(parameters, GTUtil.makeNBT(aTags));
        return this;
    }

    public WeakReference<MultiTileEntityRegistry> getRegistry() {
        return registry;
    }

    public Class<? extends MultiTileEntity> getMuteClass() {
        return muteClass;
    }

    public short getMuteID() {
        return muteID;
    }

    public MultiTileEntity getReferenceTileEntity() {
        return referenceTileEntity;
    }

    public NBTTagCompound getParameters() {
        return parameters;
    }

    private void verifyDescendentOf(Class<?> cls) {
        // Check if cls is extended by mClass
        if (!cls.isAssignableFrom(muteClass)) {
            throw new IllegalArgumentException(
                "Expected a descendent of " + cls.getName() + " got " + muteClass.getName() + " instead.");
        }
    }

    private void verifyDescendentOfMultiple(boolean onlyOne, Class<?>... classes) {
        boolean atLeastOne = false;
        final StringBuilder classNames = new StringBuilder();
        for (Class<?> cls : classes) {
            classNames.append(cls.getName())
                .append(" ");
            if (!onlyOne) {
                verifyDescendentOf(cls);
                atLeastOne = true;
            } else if (cls.isAssignableFrom(muteClass)) {
                atLeastOne = true;
            }
        }

        if (!atLeastOne) {
            throw new IllegalArgumentException(
                "Expected a descendent of any of these " + classNames + " got " + muteClass.getName() + " instead.");
        }
    }
}
