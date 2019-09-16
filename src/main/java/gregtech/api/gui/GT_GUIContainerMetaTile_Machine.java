package gregtech.api.gui;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The GUI-Container I use for all my MetaTileEntities
 */
public class GT_GUIContainerMetaTile_Machine extends GT_GUIContainer {

    public final GT_ContainerMetaTile_Machine mContainer;

    public GT_GUIContainerMetaTile_Machine(GT_ContainerMetaTile_Machine aContainer, String aGUIbackground) {
        super(aContainer, aGUIbackground);
        mContainer = aContainer;
    }

    public GT_GUIContainerMetaTile_Machine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aGUIbackground) {
        this(new GT_ContainerMetaTile_Machine(aInventoryPlayer, aTileEntity), aGUIbackground);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        if (GregTech_API.sColoredGUI && mContainer != null && mContainer.mTileEntity != null) {
            byte colorByte=mContainer.mTileEntity.getColorization();
            Dyes color;
            if(colorByte != -1) color= Dyes.get(colorByte);
            else color=Dyes.MACHINE_METAL;
            GL11.glColor3ub((byte)color.mRGBa[0], (byte)color.mRGBa[1], (byte)color.mRGBa[2]);
        } else GL11.glColor3ub((byte)255,(byte)255,(byte)255);
    }
}