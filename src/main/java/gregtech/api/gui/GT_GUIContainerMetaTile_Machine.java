package gregtech.api.gui;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.gui.widgets.GT_GuiTooltipManager;
import gregtech.api.gui.widgets.GT_GuiTooltipManager.GT_IToolTipRenderer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_TooltipDataCache;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.List;

import org.lwjgl.opengl.GL11;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The GUI-Container I use for all my MetaTileEntities
 */
public class GT_GUIContainerMetaTile_Machine extends GT_GUIContainer implements GT_IToolTipRenderer {

    public final GT_ContainerMetaTile_Machine mContainer;
    
	protected GT_GuiTooltipManager mTooltipManager = new GT_GuiTooltipManager();
    protected GT_TooltipDataCache mTooltipCache = new GT_TooltipDataCache();

    public GT_GUIContainerMetaTile_Machine(GT_ContainerMetaTile_Machine aContainer, String aGUIbackground) {
        super(aContainer, aGUIbackground);
        mContainer = aContainer;

        // Only setup tooltips if they're currently enabled.
        if (GT_Mod.gregtechproxy.mTooltipVerbosity > 0 || GT_Mod.gregtechproxy.mTooltipShiftVerbosity > 0) {
            setupTooltips();
        }
    }

    public GT_GUIContainerMetaTile_Machine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aGUIbackground) {
        this(new GT_ContainerMetaTile_Machine(aInventoryPlayer, aTileEntity), aGUIbackground);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float parTicks) {
        super.drawScreen(mouseX, mouseY, parTicks);
        if (mc.thePlayer.inventory.getItemStack() == null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(guiLeft, guiTop, 0.0F);
            mTooltipManager.onTick(this, mouseX, mouseY);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        if (GregTech_API.sMachineMetalGUI) {
            GL11.glColor3ub((byte) Dyes.MACHINE_METAL.mRGBa[0], (byte) Dyes.MACHINE_METAL.mRGBa[1], (byte) Dyes.MACHINE_METAL.mRGBa[2]);
        } else if (GregTech_API.sColoredGUI && mContainer != null && mContainer.mTileEntity != null) {
            byte colorByte = mContainer.mTileEntity.getColorization();
            Dyes color;
            if (colorByte != -1)
                color = Dyes.get(colorByte);
            else
                color = Dyes.MACHINE_METAL;
            GL11.glColor3ub((byte) color.mRGBa[0], (byte) color.mRGBa[1], (byte) color.mRGBa[2]);
        } else {
            GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
        }
    }

    /**
     * Load data for and create appropriate tooltips for this machine.
     * Only called when one of regular or shift tooltips are enabled.
     */
    protected void setupTooltips() {    }

    // GT_IToolTipRenderer and GT_ITabRenderer implementations
    @Override
    public void drawHoveringText(List text, int x, int y, FontRenderer font) {
        super.drawHoveringText(text, x, y, font);
    }
	@Override
	public int getGuiTop() {
		return guiTop;
	}
	@Override
	public int getGuiLeft() {
		return guiLeft;
	}
	@Override
	public int getXSize() {
		return xSize;
	}
	@Override
	public FontRenderer getFontRenderer() {
		return fontRendererObj;
	}

	public void addToolTip(GT_GuiTooltip toolTip) {
		mTooltipManager.addToolTip(toolTip);
	}
    
	public boolean removeToolTip(GT_GuiTooltip toolTip) {
		return mTooltipManager.removeToolTip(toolTip);
	}
}
