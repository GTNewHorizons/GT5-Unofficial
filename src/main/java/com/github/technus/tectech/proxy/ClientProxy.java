package com.github.technus.tectech.proxy;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.block.QuantumGlassRender;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;
import openmodularturrets.blocks.turretheads.TurretHeadEM;
import openmodularturrets.blocks.turretheads.TurretHeadItemRenderEM;
import openmodularturrets.blocks.turretheads.TurretHeadRenderEM;
import openmodularturrets.entity.projectiles.projectileEM;
import openmodularturrets.entity.projectiles.projectileRenderEM;
import openmodularturrets.tileentity.turret.TileTurretHeadEM;
import org.lwjgl.opengl.GL11;

public class ClientProxy extends CommonProxy {
    @Override
    public void addTexturePage(byte page){
        if(Textures.BlockIcons.casingTexturePages[page]==null)
            Textures.BlockIcons.casingTexturePages[page]=new ITexture[128];
    }

    @Override
    public void registerRenderInfo() {
        QuantumGlassRender.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(QuantumGlassRender.renderID, new QuantumGlassRender());

        if(Loader.isModLoaded("openmodularturrets")) {
            TurretHeadRenderEM turretHeadRenderEM=new TurretHeadRenderEM();
            ClientRegistry.bindTileEntitySpecialRenderer(TileTurretHeadEM.class, turretHeadRenderEM);
            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TurretHeadEM.INSTANCE), new TurretHeadItemRenderEM(turretHeadRenderEM, new TileTurretHeadEM()));

            RenderingRegistry.registerEntityRenderingHandler(projectileEM.class, new projectileRenderEM());
        }
    }

    @Override
    public void particles(IGregTechTileEntity aMuffler, byte facing) {//CUTE!
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

        if ((mc.gameSettings.guiScale) == 3) {
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
}
