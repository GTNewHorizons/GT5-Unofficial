package gregtech.api.interfaces;

import net.minecraft.client.renderer.texture.IIconRegister;

import gregtech.api.enums.Mods;

/**
 * Special interface to be given to classes implementing {@link net.minecraftforge.client.IItemRenderer}, to allow
 * {@link gregtech.api.items.MetaBaseItem} to register any out of band icons that might be needed.
 */
public interface IIconRegistration {

    /**
     * Register any special icons needed for this render.
     *
     * @param iconRegister The vanilla Minecraft icon registry. Call {@link IIconRegister#registerIcon(String)} to
     *                     register icons inside the method.
     * @param basePath     The base icon path for this meta-item. Intended to be fed to
     *                     {@link Mods#getResourcePath(String...)}. (Probably {@link Mods#GregTech}, but we won't
     *                     judge.) The base path will include the FULL metadata ID, not offset (e.g. 32468 instead
     *                     of 468), so place in the appropriate directory.
     */
    default void registerIcons(final IIconRegister iconRegister, final String basePath) {}
}
