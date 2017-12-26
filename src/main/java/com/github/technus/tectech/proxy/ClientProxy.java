package com.github.technus.tectech.proxy;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.entity.fx.BlockHint;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.QuantumGlassRender;
import com.github.technus.tectech.thing.block.QuantumStuffBlock;
import com.github.technus.tectech.thing.block.QuantumStuffRender;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmodularturrets.TT_turret_loader;
import org.lwjgl.opengl.GL11;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderInfo() {
        QuantumGlassBlock.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(QuantumGlassBlock.renderID, new QuantumGlassRender());

        QuantumStuffBlock.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(QuantumStuffBlock.renderID, new QuantumStuffRender());

        if(Loader.isModLoaded("openmodularturrets")) new TT_turret_loader().run();
    }

    @Override
    public void hint_particle(World world, int x, int y, int z, Block block, int meta) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new BlockHint(world,x,y,z,block,meta));

        EntityFX particle = new EntityExplodeFX(world, x + TecTech.Rnd.nextFloat() * 0.5F, y + TecTech.Rnd.nextFloat() * 0.5F, z + TecTech.Rnd.nextFloat() * 0.5F, 0, 0, 0);
        particle.setRBGColorF(0, 0.6F * TecTech.Rnd.nextFloat(), 0.8f);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @Override
    public void em_particle(IGregTechTileEntity aMuffler, byte facing) {//CUTE!
        ForgeDirection aDir = ForgeDirection.getOrientation(facing);
        float xPos = aDir.offsetX * 0.76F + aMuffler.getXCoord() + 0.25F;
        float yPos = aDir.offsetY * 0.76F + aMuffler.getYCoord() + 0.25F;
        float zPos = aDir.offsetZ * 0.76F + aMuffler.getZCoord() + 0.25F;

        float ySpd = 0;
        //aDir.offsetY*0.1F+0.2F+0.1F*floatGen.nextFloat();
        float xSpd = 0;
        float zSpd = 0;
        EntityFX particle = new EntityExplodeFX(aMuffler.getWorld(), xPos + TecTech.Rnd.nextFloat() * 0.5F, yPos + TecTech.Rnd.nextFloat() * 0.5F, zPos + TecTech.Rnd.nextFloat() * 0.5F, xSpd, ySpd, zSpd);
        particle.setRBGColorF(0, 0.6F * TecTech.Rnd.nextFloat(), 0.8f);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void renderUnicodeString(String str, int x, int y, int maxWidth, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRenderer;

        boolean origFont = fontRenderer.getUnicodeFlag();

        if (mc.gameSettings.guiScale == 3) {
            fontRenderer.setUnicodeFlag(true);
            float dist = 0.08F;
            y--;
            for (int cycle = 0; cycle < 2; cycle++) {
                GL11.glTranslatef(-dist, 0F, 0F);
                fontRenderer.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(dist, -dist, 0F);
                fontRenderer.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(dist, 0F, 0F);
                fontRenderer.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(-dist, dist, 0F);

                dist = -dist;
            }
            fontRenderer.setUnicodeFlag(origFont);
        } else
            fontRenderer.drawSplitString(str, x, y, maxWidth, color);
    }

    @Override
    public void printInchat(String... strings) {
        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        for (String s : strings) {
            chat.printChatMessage(new ChatComponentText(s));
        }
    }
}
