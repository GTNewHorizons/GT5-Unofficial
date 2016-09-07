package gtPlusPlus.core.util.debug;

import gtPlusPlus.core.util.Utils;

import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DEBUG_TimerThread implements Runnable {

	private World world;
	private EntityPlayer player;
	

	public DEBUG_TimerThread(World WORLD, EntityPlayer PLAYER) {
		world = WORLD;
		player = PLAYER;
	}

	@Override
	public void run(){
		int xDir = ForgeDirection.getOrientation(player.getPlayerCoordinates().posX).offsetX;
		int zDir = ForgeDirection.getOrientation(player.getPlayerCoordinates().posZ).offsetZ;
		
		int stepX = Minecraft.getMinecraft().objectMouseOver.blockX;
		int stepY = Minecraft.getMinecraft().objectMouseOver.blockY;
		int stepZ = Minecraft.getMinecraft().objectMouseOver.blockZ;
		Utils.LOG_INFO("Clicked on a Block @ "+"[X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]"+" with xDir:"+xDir+" zDir:"+zDir);
		world.setBlock(stepX, stepY, stepZ, Blocks.bedrock,0,3);
		Utils.LOG_INFO("Makng it Bedrock for future investment.");
		//for (int i = -1; i <= 1; i++) {
		//stepX = stepX+i;
			for (int i = stepX-1; i <= stepX+1; i++){
				for (int j = stepZ-1; j <= stepZ+1; j++){
					for (int h = stepY-1; h <= stepY+1; h++){	
						
						xDir = ForgeDirection.getOrientation(player.getPlayerCoordinates().posX).offsetX;
						zDir = ForgeDirection.getOrientation(player.getPlayerCoordinates().posZ).offsetZ;
						
			//for (int j = -1; j <= 1; j++) {
				//stepZ = stepZ+j;
				//for (int h = -1; h <= 1; h++) {
					//stepY = stepY+h;
					Utils.LOG_INFO("Placing Block @ "+"[X:"+i+"][Y:"+h+"][Z:"+j+"]"+" with xDir:"+xDir+" zDir:"+zDir);		
					if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {	
					world.setBlock(i, h, j, Blocks.stone,0,3);
					}
					else {
						Utils.LOG_INFO("Not even sure what this is for, but I got here.");
					}
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}				
				}
			}
		}
	}
}