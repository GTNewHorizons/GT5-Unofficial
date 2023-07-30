package gregtech.api.enums;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import gregtech.api.gui.modularui.GT_UITextures;

public enum VoidingMode {

    /**
     * Voids nothing, protects both item and fluid
     */
    VOID_NONE(true, true, GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_NONE, "none"),
    /**
     * Voids item, protects fluid
     */
    VOID_ITEM(false, true, GT_UITextures.BUTTON_STANDARD_PRESSED, GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_ITEM,
        "item"),
    /**
     * Voids fluid, protects item
     */
    VOID_FLUID(true, false, GT_UITextures.BUTTON_STANDARD_PRESSED, GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_FLUID,
        "fluid"),
    /**
     * Voids all, protects nothing
     */
    VOID_ALL(false, false, GT_UITextures.BUTTON_STANDARD_PRESSED, GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_ALL, "all");

    /**
     * Default set of voiding mode you will probably support.
     */
    public static final Set<VoidingMode> ALL_OPTIONS = EnumSet.allOf(VoidingMode.class);
    /**
     * Set of voiding mode you will probably support if your machine has no item output
     */
    public static final Set<VoidingMode> NO_ITEM_OUTPUT = EnumSet.of(VOID_FLUID, VOID_NONE);
    /**
     * Set of voiding mode you will probably support if your machine has no fluid output
     */
    public static final Set<VoidingMode> NO_FLUID_OUTPUT = EnumSet.of(VOID_ITEM, VOID_NONE);
    public final boolean protectItem;
    public final boolean protectFluid;
    public final UITexture buttonTexture;
    public final UITexture buttonOverlay;
    public final String name;

    VoidingMode(boolean protectItem, boolean protectFluid, UITexture buttonTexture, UITexture buttonOverlay,
        String name) {
        this.protectItem = protectItem;
        this.protectFluid = protectFluid;
        this.buttonTexture = buttonTexture;
        this.buttonOverlay = buttonOverlay;
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
