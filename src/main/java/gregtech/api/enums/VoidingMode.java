package gregtech.api.enums;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.modularui2.GTGuiTextures;

public enum VoidingMode {

    /**
     * Voids nothing, protects both item and fluid
     */
    VOID_NONE(true, true, GTGuiTextures.BUTTON_STANDARD, GTUITextures.BUTTON_STANDARD,
        GTGuiTextures.OVERLAY_BUTTON_VOID_EXCESS_NONE, GTUITextures.OVERLAY_BUTTON_VOID_EXCESS_NONE, "none"),
    /**
     * Voids item, protects fluid
     */
    VOID_ITEM(false, true, GTGuiTextures.BUTTON_STANDARD_PRESSED, GTUITextures.BUTTON_STANDARD_PRESSED,
        GTGuiTextures.OVERLAY_BUTTON_VOID_EXCESS_ITEM, GTUITextures.OVERLAY_BUTTON_VOID_EXCESS_ITEM, "item"),
    /**
     * Voids fluid, protects item
     */
    VOID_FLUID(true, false, GTGuiTextures.BUTTON_STANDARD_PRESSED, GTUITextures.BUTTON_STANDARD_PRESSED,
        GTGuiTextures.OVERLAY_BUTTON_VOID_EXCESS_FLUID, GTUITextures.OVERLAY_BUTTON_VOID_EXCESS_FLUID, "fluid"),
    /**
     * Voids all, protects nothing
     */
    VOID_ALL(false, false, GTGuiTextures.BUTTON_STANDARD_PRESSED, GTUITextures.BUTTON_STANDARD_PRESSED,
        GTGuiTextures.OVERLAY_BUTTON_VOID_EXCESS_ALL, GTUITextures.OVERLAY_BUTTON_VOID_EXCESS_ALL, "all");

    /**
     * Default set of voiding mode you will probably support.
     */
    public static final Set<VoidingMode> ALL_OPTIONS = EnumSet.allOf(VoidingMode.class);
    /**
     * Set of voiding mode you will probably support if your machine has no item output
     */
    public static final Set<VoidingMode> FLUID_ONLY_MODES = EnumSet.of(VOID_FLUID, VOID_NONE);
    /**
     * Set of voiding mode you will probably support if your machine has no fluid output
     */
    public static final Set<VoidingMode> ITEM_ONLY_MODES = EnumSet.of(VOID_ITEM, VOID_NONE);
    public final boolean protectItem;
    public final boolean protectFluid;
    public final com.cleanroommc.modularui.drawable.UITexture buttonTexture;
    public final com.cleanroommc.modularui.drawable.UITexture buttonOverlay;
    public final UITexture buttonTextureLegacy;
    public final UITexture buttonOverlayLegacy;
    public final String name;

    VoidingMode(boolean protectItem, boolean protectFluid, com.cleanroommc.modularui.drawable.UITexture buttonTexture,
        UITexture buttonTextureLegacy, com.cleanroommc.modularui.drawable.UITexture buttonOverlay,
        UITexture buttonOverlayLegacy, String name) {
        this.protectItem = protectItem;
        this.protectFluid = protectFluid;
        this.buttonTexture = buttonTexture;
        this.buttonOverlay = buttonOverlay;
        this.buttonTextureLegacy = buttonTextureLegacy;
        this.buttonOverlayLegacy = buttonOverlayLegacy;
        this.name = name;
    }

    public String getTransKey() {
        return "GT5U.gui.button.voiding_mode_" + name;
    }

    public VoidingMode next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public VoidingMode previous() {
        return values()[(ordinal() + values().length - 1) % values().length];
    }

    public VoidingMode nextInCollection(Collection<VoidingMode> allowed) {
        if (allowed.isEmpty()) throw new IllegalArgumentException("nothing allowed");
        VoidingMode ret = this;
        do {
            ret = ret.next();
        } while (!allowed.contains(ret));
        return ret;
    }

    public VoidingMode previousInCollection(Collection<VoidingMode> allowed) {
        if (allowed.isEmpty()) throw new IllegalArgumentException("nothing allowed");
        VoidingMode ret = this;
        do {
            ret = ret.previous();
        } while (!allowed.contains(ret));
        return ret;
    }

    /**
     * Do not use this for loading mode from TEs, to prevent mode being shifted when new mode is added.
     */
    @Nonnull
    public static VoidingMode fromOrdinal(int ordinal) {
        if (ordinal >= 0 && ordinal < values().length) {
            return values()[ordinal];
        }
        return VOID_NONE;
    }

    @Nonnull
    public static VoidingMode fromName(String name) {
        for (VoidingMode mode : values()) {
            if (mode.name.equals(name)) {
                return mode;
            }
        }
        return VOID_NONE;
    }

}
