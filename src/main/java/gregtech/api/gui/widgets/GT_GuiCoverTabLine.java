package gregtech.api.gui.widgets;

import java.awt.Rectangle;
import java.util.List;

import org.lwjgl.opengl.GL11;

import codechicken.nei.api.API;
import codechicken.nei.api.INEIGuiAdapter;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_GtTileEntityGuiRequest;
import gregtech.common.GT_Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * Let's you access a GregTech IGregTechTileEntity's covers as tabs on the GUI's sides
 */
public class GT_GuiCoverTabLine extends GT_GuiTabLine {
    // Names of the block a cover could be on
    private final static String[] SIDES = new String[]{
        "GT5U.interface.coverTabs.down",
        "GT5U.interface.coverTabs.up",
        "GT5U.interface.coverTabs.north",
        "GT5U.interface.coverTabs.south",
        "GT5U.interface.coverTabs.west",
        "GT5U.interface.coverTabs.east"};

    // Not sure there's a point in JIT translation but that's what this is
    private String[] translatedSides;
    private IGregTechTileEntity tile;
    private int colorization;

    /**
     * Let's you access an IGregTechTileEntity's covers as tabs on the GUI's sides
     * 
     * @param gui GT_ITabRenderer gui which this tab line attaches to
     * @param tabLineLeft left position of the tab line in relation to the gui
     * @param tabLineTop top position of the tab line in relation to the gui
     * @param tabHeight height of a tab
     * @param tabWidth width of a tab
     * @param tabSpacing pixels between each tab
     * @param xDir whether to extend the line horizontally to the right (NORMAL),
     * the left (INVERSE) or not at all (NONE)
     * @param yDir whether to extend the line vertically down (NORMAL), up (INVERSE)
     * or not at all (NONE)
     * @param displayMode  whether to display on the left side of the GT_ITabRenderer
     * (NORMAL), on it's right side (INVERSE) or not at all (NONE)
     * @param tabBackground the set of textures used to draw this tab line's tab backgrounds
     * @param tile The IGregTechTileEntity the covers of which we are accessing
     * @param colorization The colorization of the GUI we are adding tabs to
     */
    public GT_GuiCoverTabLine(GT_GUIContainerMetaTile_Machine gui,  int tabLineLeft, int tabLineTop, int tabHeight,
            int tabWidth, int tabSpacing, DisplayStyle xDir, DisplayStyle yDir, DisplayStyle displayMode,
            GT_GuiTabIconSet tabBackground, IGregTechTileEntity tile, int colorization) {
        super(gui, 6, tabLineLeft, tabLineTop, tabHeight, tabWidth, tabSpacing, xDir, yDir, displayMode, tabBackground);
        this.tile = tile;
        this.colorization = colorization;
        this.translatedSides = new String[6];
        setupTabs();
    }

    /**
     * Add a tab for each existing cover on this IGregTechTileEntity at creation time
     */
    private void setupTabs() {
        for (byte tSide = 0; tSide < 6; tSide++) {
            ItemStack cover = tile.getCoverItemAtSide(tSide);
            if (cover != null) {
                addCoverToTabs(tSide, cover);
            }
        }
    }    

    @Override
    protected void drawBackground(float parTicks, int mouseX, int mouseY) {
        // Apply this tile's coloration to draw the background
        GL11.glColor3ub((byte) ((colorization >> 16) & 0xFF), (byte) ((colorization >> 8) & 0xFF), (byte) (colorization & 0xFF));
        super.drawBackground(parTicks, mouseX, mouseY);
    }

    @Override
    protected void tabClicked(int tabId, int mouseButton) {
        if (mouseButton == 0 && mTabs[tabId].enabled) {
            GT_Values.NW.sendToServer(new GT_Packet_GtTileEntityGuiRequest(
                this.tile.getXCoord(),
                this.tile.getYCoord(),
                this.tile.getZCoord(),
                tabId + GT_Proxy.GUI_ID_COVER_SIDE_BASE,
                this.tile.getWorld().provider.dimensionId,
                Minecraft.getMinecraft().thePlayer.getEntityId(),
                0));
        }
    }

    /**
     * Add the cover on this side of the IGregTechTileEntity to the tabs
     * @param side
     * @param cover
     */
    private void addCoverToTabs(byte side, ItemStack cover) {
        boolean enabled = this.tile.getCoverBehaviorAtSideNew(side).hasCoverGUI();
        this.setTab(side, cover, null, getTooltipForCoverTab(side, cover, enabled));
        this.setTabEnabled(side, enabled);
        
    }

    /**
     * Decorate the cover's tooltips according to the side it's on and on whether the tab is enabled or not
     * @param side
     * @param cover
     * @param enabled
     * @return This cover tab's tooltip
     */
    private String[] getTooltipForCoverTab(byte side, ItemStack cover, boolean enabled) {
        List<String> tooltip = cover.getTooltip(Minecraft.getMinecraft().thePlayer, true);  
        tooltip.set(0, 
                (enabled ? EnumChatFormatting.UNDERLINE : EnumChatFormatting.DARK_GRAY)
                + getSideDescription(side)
                + (enabled ? EnumChatFormatting.RESET + ": " : ": " + EnumChatFormatting.RESET)
             + tooltip.get(0));
        return tooltip.toArray(new String[0]);
    }

    /**
     * Get the translated name for a side of the IGregTechTileEntity
     * @param side
     * @return translated name for a side of the IGregTechTileEntity
     */
    private String getSideDescription(byte side) {
        if (side < SIDES.length) {
            if (this.translatedSides[side] == null) {
                this.translatedSides[side] = StatCollector.translateToLocal(SIDES[side]);
            }
            return this.translatedSides[side] ;
        }
        return null;
    }

    /**
     * Hide any NEI slots that would intersect with a cover tab
     */
    static class CoverTabLineNEIHandler extends INEIGuiAdapter{
        @Override
        public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
            Rectangle neiSlotArea = new Rectangle(x, y, w, h);
            if (gui instanceof GT_GUIContainerMetaTile_Machine) {
                GT_GuiTabLine tabLine = ((GT_GUIContainerMetaTile_Machine) gui).coverTabs;
                if (!tabLine.visible) {
                    return false;
                }
                for (int i = 0; i < tabLine.mTabs.length; i++ ) {
                    if (tabLine.mTabs[i] != null && tabLine.mTabs[i].getBounds().intersects(neiSlotArea)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    static {
        API.registerNEIGuiHandler(new CoverTabLineNEIHandler());
    }
}
