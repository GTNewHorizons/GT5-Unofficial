package gregtech.api.enums;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.cleanroommc.modularui.drawable.UITexture;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import gregtech.api.GregTechAPI;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.objects.GTHashSet;
import gregtech.api.util.GTUtility;
import gregtech.common.items.ItemGTToolbox;

/**
 * Defines the various slots in the toolbox. Used both to enforce inventory restrictions as well assist with drawing
 * GUIs.
 */
public enum ToolboxSlot {

    WRENCH(0, isItemInToolSet(GregTechAPI.sWrenchList), GTGuiTextures.OVERLAY_TOOLBOX_WRENCH),
    WIRE_CUTTER(1, isItemInToolSet(GregTechAPI.sWireCutterList), GTGuiTextures.OVERLAY_TOOLBOX_WIRE_CUTTER),
    SCREWDRIVER(2, isItemInToolSet(GregTechAPI.sScrewdriverList), GTGuiTextures.OVERLAY_TOOLBOX_SCREWDRIVER),
    SOFT_MALLET(3, isItemInToolSet(GregTechAPI.sSoftMalletList), GTGuiTextures.OVERLAY_TOOLBOX_SOFT_MALLET),

    GENERIC_SLOT0(4),
    GENERIC_SLOT1(5),
    GENERIC_SLOT2(6),

    // Charges any item in the toolbox.
    BATTERY(7, OrePrefixes.battery::containsUnCached, GTGuiTextures.OVERLAY_TOOLBOX_BATTERY),
    HARD_HAMMER(8, isItemInToolSet(GregTechAPI.sHardHammerList), GTGuiTextures.OVERLAY_TOOLBOX_HARD_HAMMER),
    CROWBAR(9, isItemInToolSet(GregTechAPI.sCrowbarList), GTGuiTextures.OVERLAY_TOOLBOX_CROWBAR),
    SOLDERING_IRON(10, isItemInToolSet(GregTechAPI.sSolderingToolList), GTGuiTextures.OVERLAY_TOOLBOX_SOLDERING_IRON),

    GENERIC_SLOT3(11),
    GENERIC_SLOT4(12),
    GENERIC_SLOT5(13),

    // merge conflict defeater comment
    ;

    public static final ImmutableList<ToolboxSlot> GENERIC_SLOTS = ImmutableList
        .of(GENERIC_SLOT0, GENERIC_SLOT1, GENERIC_SLOT2, GENERIC_SLOT3, GENERIC_SLOT4, GENERIC_SLOT5);
    public static final ImmutableList<ToolboxSlot> TOOL_SLOTS = ImmutableList
        .of(WRENCH, WIRE_CUTTER, SCREWDRIVER, SOFT_MALLET, HARD_HAMMER, CROWBAR, SOLDERING_IRON);
    public static final int ROW_WIDTH = 7;

    private static final ImmutableMap<Integer, ToolboxSlot> LOOKUP;

    static {
        final ImmutableMap.Builder<Integer, ToolboxSlot> builder = new ImmutableMap.Builder<>();
        for (final ToolboxSlot toolboxSlot : ToolboxSlot.values()) {
            builder.put(toolboxSlot.getSlotID(), toolboxSlot);
        }

        LOOKUP = builder.build();
    }

    private final Predicate<ItemStack> itemStackTest;
    private final int slot;
    private final boolean isGeneric;
    private final UITexture overlay;

    /** Used to register the overlay textures as icons for rendering during the "tool recently broken" animation. */
    private IIcon icon = null;

    ToolboxSlot(final int slot) {
        this(slot, x -> true, true, null);
    }

    ToolboxSlot(final int slot, final Predicate<ItemStack> itemStackTest, final UITexture overlay) {
        this(slot, itemStackTest, false, overlay);
    }

    ToolboxSlot(final int slot, final Predicate<ItemStack> itemStackTest, boolean isGeneric, UITexture overlay) {
        if (slot < 0) {
            throw new RuntimeException("Invalid slot " + slot + ". Must be greater than or equal to zero.");
        }
        this.slot = slot;
        this.itemStackTest = itemStackTest;
        this.isGeneric = isGeneric;
        this.overlay = overlay;
    }

    public boolean test(final ItemStack stack) {
        if (stack == null) {
            return false;
        }

        if (stack.getItem() instanceof ItemGTToolbox) {
            return false;
        }

        return itemStackTest.test(stack);
    }

    public int getSlotID() {
        return slot;
    }

    public Optional<UITexture> getOverlay() {
        return Optional.ofNullable(overlay);
    }

    public void registerIcon(final IIconRegister iconRegister) {
        icon = iconRegister.registerIcon(GregTech.getID() + ":" + overlay.location.getResourcePath());
    }

    public Optional<IIcon> getIcon() {
        return Optional.ofNullable(icon);
    }

    public int getRow() {
        // Using integer division on purpose here.
        return slot / ROW_WIDTH;
    }

    public int getColumn() {
        return slot % ROW_WIDTH;
    }

    public boolean isGeneric() {
        return isGeneric;
    }

    public boolean isTool() {
        // Makes the Optional maps a little easier to read.
        return !isGeneric;
    }

    public static Optional<ToolboxSlot> getBySlot(int slot) {
        return Optional.ofNullable(LOOKUP.get(slot));
    }

    public static boolean slotIsTool(int slot) {
        return getBySlot(slot).map(ToolboxSlot::isTool)
            .orElse(false);
    }

    private static Predicate<ItemStack> isItemInToolSet(GTHashSet toolSet) {
        return (ItemStack itemStack) -> {
            if (!(itemStack.getItem() instanceof MetaGeneratedTool)) {
                return false;
            }

            return GTUtility.isStackInList(itemStack, toolSet);
        };
    }
}
