package miscutil.core.util.debug;

import javax.vecmath.Point3d;

import miscutil.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class UtilsRendering {
	static Tessellator drawBlock =  Tessellator.instance;

	/*public static void drawBlockInWorld(final int x, final int y, final int z, final ReadonlyColor colours){
        Thread t = new Thread() {        	
			@Override
			public void run() {
				LibShapeDraw drawThing = new LibShapeDraw();				
		        WireframeCuboid box = new WireframeCuboid(x,y,z, x+1,y+1,z+1);
		        box.setLineStyle(colours.copy(), 2.0F, false);
		        drawThing.addShape(box);

				Utils.LOG_INFO("Drawing wireframe at: ["+x+"] ["+y+"] ["+z+"]");
				if(true) {
					try {						
						Thread.sleep(1000*10*1);
						drawThing.removeShape(box);
					} catch (Throwable ie) {
						Utils.LOG_INFO("Failed creating render removal timer.");
						drawThing.removeShape(box);
					}
				}
			}
		};
		t.start();		
	}*/
	
	public static void renderStuff (int x, int y, int z){
		Utils.LOG_INFO("Doing Some Debug Rendering.");
		double doubleX = Minecraft.getMinecraft().thePlayer.posX - 0.5;
		double doubleY = Minecraft.getMinecraft().thePlayer.posY + 0.1;
		double doubleZ = Minecraft.getMinecraft().thePlayer.posZ - 0.5;

		GL11.glPushMatrix();
		GL11.glTranslated(-doubleX, -doubleY, -doubleZ);
		GL11.glColor3ub((byte)255,(byte)0,(byte)0);
		float mx = x;
		float my = y;
		float mz = z;
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(mx+0.4f,my,mz+0.4f);
		GL11.glVertex3f(mx-0.4f,my,mz-0.4f);
		GL11.glVertex3f(mx+0.4f,my,mz-0.4f);
		GL11.glVertex3f(mx-0.4f,my,mz+0.4f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
		
	public static void renderBoxAt(double x, double y, double z) {

		   // locationBlocksTexture is a "ResourceLocation" that points to a texture made of many block "icons".
		   // It will look very ugly, but creating our own ResourceLocation is beyond the scope of this tutorial.
		   Tessellator tessellator = Tessellator.instance;
		   GL11.glPushMatrix();
		   GL11.glTranslated(x, y+1, z); // +1 so that our "drawing" appears 1 block over our block (to get a better view)
		   tessellator.startDrawingQuads();
		   tessellator.addVertexWithUV(0, 0, 0, 0, 0);
		   tessellator.addVertexWithUV(0, 1, 0, 0, 1);
		   tessellator.addVertexWithUV(1, 1, 0, 1, 1);
		   tessellator.addVertexWithUV(1, 0, 0, 1, 0);

		   tessellator.addVertexWithUV(0, 0, 0, 0, 0);
		   tessellator.addVertexWithUV(1, 0, 0, 1, 0);
		   tessellator.addVertexWithUV(1, 1, 0, 1, 1);
		   tessellator.addVertexWithUV(0, 1, 0, 0, 1);

		   tessellator.draw();
		   GL11.glPopMatrix();

		}
	
	@SubscribeEvent
    public static void Block_Render(RenderWorldLastEvent evt, int x, int y, int z){
       
        Minecraft mc = Minecraft.getMinecraft();
          MovingObjectPosition mouseOver = Minecraft.getMinecraft().objectMouseOver;
 
            int mx = mouseOver.blockX;
            int my = mouseOver.blockY;
            int mz = mouseOver.blockZ;
           
            int hit;
            hit = mouseOver.sideHit;
            ForgeDirection direction = ForgeDirection.getOrientation(hit);
           
          World world = mc.thePlayer.worldObj;
          EntityPlayer player = mc.thePlayer;
          EntityClientPlayerMP p= mc.thePlayer;
            ChunkCoordinates coord;
            coord = player.getPlayerCoordinates();
           
            int px = (int) coord.posX;
            int py = (int) player.posY-1;
            int pz = (int) coord.posZ;
           
            double dx = p.lastTickPosX + (p.posX - p.lastTickPosX)
                    * evt.partialTicks;
            double dy = p.lastTickPosY + (p.posY - p.lastTickPosY)
                    * evt.partialTicks;
            dy = dy -1;
            double dz = p.lastTickPosZ + (p.posZ - p.lastTickPosZ)
                    * evt.partialTicks;
           
            int tx = mx - px;
            int ty = my - py;
            int tz = mz - pz;
         
           
            Point3d translate;
            translate= new Point3d(tx, ty, tz)/*.add(direction)*/;
           
            Block block = world.getBlock(x, y, z);
            Material matt;
            matt = block.getMaterial();
           
            ItemStack stack;
            stack = player.getHeldItem();
            Item held = null;
            if(stack != null)
            held = stack.getItem();        
 
            if (block == Blocks.air || matt == Material.fire
                    || matt == Material.water || matt == Material.lava
                    || matt == Material.plants || matt == Material.vine
                    || block == Blocks.snow_layer) {
               
                translate= new Point3d(tx, ty, tz);
            }
            else translate= new Point3d(tx, ty, tz)/*.add(direction)*/;
           
            tx = (int) translate.x;
            ty = (int) translate.y;
            tz = (int) translate.z;
            double fx = px - dx;
            double fy = py - dy;
            double fz = pz - dz;

          //Block block3 = Common_Registry.Floater;
         // Block block4 = Common_Registry.Balloon;         
          Block block2 = world.getBlock(20, 20, 20);
                  
           GL11.glPushMatrix();  
           GL11.glEnable(GL11.GL_BLEND);
           GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        Tessellator t = Tessellator.instance;
        RenderBlocks renderBlocks = new RenderBlocks();
        renderBlocks.blockAccess = world;
       
        renderBlocks.setRenderBounds(0D, 0D, 0D, 1D, 1D, 1D);
        t.startDrawingQuads();
        t.setBrightness(15 << 20 | 15 << 4);
       
        try{
            if(block != Blocks.air){
            renderBlocks.renderBlockByRenderType(block2, 0, -11, 0);           
            }          
           
        GL11.glTranslated( 0F, 10f, 0F);       
        GL11.glTranslated( tx, ty, tz);
        GL11.glTranslated( fx, fy, fz);
        }
          catch(NullPointerException exception){
              System.out.println(exception);
          }
        t.draw();              
       
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();       
    }
	
}
