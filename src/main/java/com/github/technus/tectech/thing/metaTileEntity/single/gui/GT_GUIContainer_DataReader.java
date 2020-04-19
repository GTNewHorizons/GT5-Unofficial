package com.github.technus.tectech.thing.metaTileEntity.single.gui;

import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_DataReader;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

public class GT_GUIContainer_DataReader extends GT_GUIContainerMetaTile_Machine {
    public final String mName;
    public final String mNEI;
    public final byte mProgressBarDirection;
    public final byte mProgressBarAmount;
    private ItemStack stack=null;

    public GT_GUIContainer_DataReader(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile, String aNEI) {
        this(aInventoryPlayer, aTileEntity, aName, aTextureFile, aNEI, (byte) 0, (byte) 1);
    }

    public GT_GUIContainer_DataReader(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile, String aNEI, byte aProgressBarDirection, byte aProgressBarAmount) {
        super(new GT_Container_DataReader(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/basicmachines/" + aTextureFile);
        this.mProgressBarDirection = aProgressBarDirection;
        this.mProgressBarAmount = (byte) Math.max(1, aProgressBarAmount);
        this.mName = aName;
        this.mNEI = aNEI;
        ySize = 256;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        if (mContainer != null) {
            if (mContainer.mTileEntity != null && mContainer.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_DataReader) {
                GT_MetaTileEntity_DataReader reader = (GT_MetaTileEntity_DataReader) mContainer.mTileEntity.getMetaTileEntity();
                renderDataTooltips(mouseX,mouseY,reader.mTier);
            }
        }
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (mContainer != null) {
            if (mContainer.mTileEntity != null && mContainer.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_DataReader) {
                GT_MetaTileEntity_DataReader reader = (GT_MetaTileEntity_DataReader) mContainer.mTileEntity.getMetaTileEntity();
                if (renderDataFG(mouseX, mouseY, reader.mTier)) {
                    return;
                }
            }
        }
        fontRendererObj.drawString(mName, 7, 8, 0xfafaff);
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(par1, mouseX, mouseY);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        if (this.mContainer != null) {
            if (((GT_Container_DataReader) this.mContainer).mStuttering) {
                this.drawTexturedModalRect(x + 127, y + 152, 176, 54, 18, 18);
            }

            if (this.mContainer.mMaxProgressTime > 0) {
                int tSize = this.mProgressBarDirection < 2 ? 20 : 18;
                int tProgress = Math.max(1, Math.min(tSize * this.mProgressBarAmount, (this.mContainer.mProgressTime > 0 ? 1 : 0) + this.mContainer.mProgressTime * tSize * this.mProgressBarAmount / this.mContainer.mMaxProgressTime)) % (tSize + 1);
                switch (this.mProgressBarDirection) {
                    case 0:
                        this.drawTexturedModalRect(x + 78, y + 152, 176, 0, tProgress, 18);
                        break;
                    case 1:
                        this.drawTexturedModalRect(x + 78 + 20 - tProgress, y + 152, 196 - tProgress, 0, tProgress, 18);
                        break;
                    case 2:
                        this.drawTexturedModalRect(x + 78, y + 152, 176, 0, 20, tProgress);
                        break;
                    case 3:
                        this.drawTexturedModalRect(x + 78, y + 152 + 18 - tProgress, 176, 18 - tProgress, 20, tProgress);
                        break;
                    case 4:
                        tProgress = 20 - tProgress;
                        this.drawTexturedModalRect(x + 78, y + 152, 176, 0, tProgress, 18);
                        break;
                    case 5:
                        tProgress = 20 - tProgress;
                        this.drawTexturedModalRect(x + 78 + 20 - tProgress, y + 152, 196 - tProgress, 0, tProgress, 18);
                        break;
                    case 6:
                        tProgress = 18 - tProgress;
                        this.drawTexturedModalRect(x + 78, y + 152, 176, 0, 20, tProgress);
                        break;
                    case 7:
                        tProgress = 18 - tProgress;
                        this.drawTexturedModalRect(x + 78, y + 152 + 18 - tProgress, 176, 18 - tProgress, 20, tProgress);
                }
            }
        }
        if (mContainer != null) {
            if (mContainer.mTileEntity != null && mContainer.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_DataReader) {
                GT_MetaTileEntity_DataReader reader = (GT_MetaTileEntity_DataReader) mContainer.mTileEntity.getMetaTileEntity();
                renderDataBG(reader.getStackInSlot(reader.getOutputSlot()), mouseX, mouseY, x, y, reader.mTier);
            }
        }
    }

    private void renderDataBG(ItemStack thing, int mouseX, int mouseY, int x, int y, byte mTier) {
        if (thing != null) {
            for (GT_MetaTileEntity_DataReader.IDataRender render :
                    GT_MetaTileEntity_DataReader.getRenders(new Util.ItemStack_NoNBT(thing))) {
                if (render.canRender(thing, mTier)) {
                    if (!GT_Utility.areStacksEqual(stack, thing, false)) {
                        render.initRender(thing);
                    }
                    render.renderBackgroundOverlay(thing, mouseX, mouseY, x, y, this);
                    break;
                }
            }
        }
        stack=thing;
    }

    private boolean renderDataFG(int mouseX, int mouseY, byte mTier) {
        if(stack==null){
            return false;
        }
        for (GT_MetaTileEntity_DataReader.IDataRender render :
                GT_MetaTileEntity_DataReader.getRenders(new Util.ItemStack_NoNBT(stack))) {
            if (render.canRender(stack, mTier)) {
                render.renderForeground(stack, mouseX, mouseY, this, fontRendererObj);
                return true;
            }
        }
        return false;
    }

    private boolean renderDataTooltips(int mouseX, int mouseY, byte mTier) {
        if(stack==null){
            return false;
        }
        for (GT_MetaTileEntity_DataReader.IDataRender render :
                GT_MetaTileEntity_DataReader.getRenders(new Util.ItemStack_NoNBT(stack))) {
            if (render.canRender(stack, mTier)) {
                render.renderTooltips(stack, mouseX, mouseY, this);
                return true;
            }
        }
        return false;
    }

    public void renderItemSimple(GT_Slot_Holo slot, ItemStack itemStack) {
        int x = slot.xDisplayPosition;
        int y = slot.yDisplayPosition;
        this.zLevel = 100.0F;
        itemRender.zLevel = 100.0F;

        if (itemStack == null) {
            IIcon iicon = slot.getBackgroundIconIndex();

            if (iicon != null) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_BLEND); // Forge: Blending needs to be enabled for this.
                this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
                this.drawTexturedModelRectFromIcon(x, y, iicon, 16, 16);
                GL11.glDisable(GL11.GL_BLEND); // Forge: And clean that up
                GL11.glEnable(GL11.GL_LIGHTING);
            }
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemStack, x, y);
        itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemStack, x, y);

        itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    public void renderTooltipSimple(int mouseX, int mouseY, GT_Slot_Holo slot, ItemStack itemStack) {
        int x = slot.xDisplayPosition + (width - xSize) / 2;
        int y = slot.yDisplayPosition + (height - ySize) / 2;
        if (mouseX >= x && mouseY >= y && mouseX <= x+16 && mouseY <= y+16 ) {
            List strings=itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, false);
            if(strings.size()>0){
                strings.set(0,itemStack.getRarity().rarityColor+(String)strings.get(0));
            }
            hoveringText(strings, mouseX, mouseY, fontRendererObj);
        }
    }

    private void hoveringText(List strings, int x, int y, FontRenderer font) {
        if (!strings.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            //RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;

            for (Object aP_146283_1_ : strings) {
                String s = (String) aP_146283_1_;
                int l = font.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int x2 = x + 12;
            int y2 = y - 12;
            int i1 = 8;

            if (strings.size() > 1) {
                i1 += 2 + (strings.size() - 1) * 10;
            }

            if (x2 + k > this.width) {
                x2 -= 28 + k;
            }

            if (y2 + i1 + 6 > this.height) {
                y2 = this.height - i1 - 6;
            }

            //this.zLevel = 300.0F;
            //itemRender.zLevel = 300.0F;
            int j1 = 0xf0001040;//bg
            this.drawGradientRect(x2 - 3, y2 - 4, x2 + k + 3, y2 - 3, j1, j1);
            this.drawGradientRect(x2 - 3, y2 + i1 + 3, x2 + k + 3, y2 + i1 + 4, j1, j1);
            this.drawGradientRect(x2 - 3, y2 - 3, x2 + k + 3, y2 + i1 + 3, j1, j1);
            this.drawGradientRect(x2 - 4, y2 - 3, x2 - 3, y2 + i1 + 3, j1, j1);
            this.drawGradientRect(x2 + k + 3, y2 - 3, x2 + k + 4, y2 + i1 + 3, j1, j1);
            int k1 = 0x500040ff;//border bright
            int l1 = (k1 & 0xfefefe) >> 1 | k1 & 0xff000000;//border dark???
            this.drawGradientRect(x2 - 3, y2 - 3 + 1, x2 - 3 + 1, y2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(x2 + k + 2, y2 - 3 + 1, x2 + k + 3, y2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(x2 - 3, y2 - 3, x2 + k + 3, y2 - 3 + 1, k1, k1);
            this.drawGradientRect(x2 - 3, y2 + i1 + 2, x2 + k + 3, y2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < strings.size(); ++i2) {
                String s1 = (String) strings.get(i2);
                font.drawStringWithShadow(s1, x2, y2, -1);

                if (i2 == 0) {
                    y2 += 2;
                }

                y2 += 10;
            }

            //this.zLevel = 0.0F;
            //itemRender.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            //RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
}
