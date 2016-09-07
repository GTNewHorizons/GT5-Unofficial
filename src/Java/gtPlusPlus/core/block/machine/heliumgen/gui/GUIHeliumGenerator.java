package gtPlusPlus.core.block.machine.heliumgen.gui;

import gtPlusPlus.core.block.machine.heliumgen.container.ContainerHeliumGenerator;
import gtPlusPlus.core.block.machine.heliumgen.tileentity.TileEntityHeliumGenerator;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIHeliumGenerator extends GuiContainer
{
    private static final ResourceLocation collectorGuiTexture = new ResourceLocation(CORE.MODID, "textures/gui/helium_collector_gui.png");

    public GUIHeliumGenerator(InventoryPlayer player, TileEntityHeliumGenerator machine)
    {
        super(new ContainerHeliumGenerator(player, machine));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = StatCollector.translateToLocal("Helium Collector");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
        
        this.fontRendererObj.drawString(StatCollector.translateToLocal("|"+-1), 80, 35, 2);
        
        short sr = 3;
        int size = sr;
        int startX = 16;
        int startY = 16;
        int i = 0;
        for (i = 0; i < 9; i++)
        {
          int x = i % size;
          int y = i / size;
          this.fontRendererObj.drawString(StatCollector.translateToLocal("|"+i), startX + 18 * x, startY + 18 * y, 4210752);
          //addSlotToContainer(new SlotInvSlot(machine.reactorSlot, i, startX + 18 * x, startY + 18 * y));
        }
        startX = 108;
        startY = 16;
        for (i = 9; i < 18; i++)
        {
          int x = i % size;
          int y = (i-9) / size;
          this.fontRendererObj.drawString(StatCollector.translateToLocal("|"+i), startX + 18 * x, startY + 18 * y, 4210752);
         // addSlotToContainer(new SlotInvSlot(machine.reactorSlot, i, startX + 18 * x, startY + 18 * y));
        }
        
    }

    @Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(collectorGuiTexture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

    }
}