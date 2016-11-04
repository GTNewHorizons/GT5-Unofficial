package gtPlusPlus.core.util.debug;

import java.util.concurrent.TimeUnit;

import gtPlusPlus.core.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DEBUG_TimerThread implements Runnable {

	private final World			world;
	private final EntityPlayer	player;

	public DEBUG_TimerThread(final World WORLD, final EntityPlayer PLAYER) {
		this.world = WORLD;
		this.player = PLAYER;
	}

	@Override
	public void run() {
		int xDir = ForgeDirection.getOrientation(this.player.getPlayerCoordinates().posX).offsetX;
		int zDir = ForgeDirection.getOrientation(this.player.getPlayerCoordinates().posZ).offsetZ;

		final int stepX = Minecraft.getMinecraft().objectMouseOver.blockX;
		final int stepY = Minecraft.getMinecraft().objectMouseOver.blockY;
		final int stepZ = Minecraft.getMinecraft().objectMouseOver.blockZ;
		Utils.LOG_INFO("Clicked on a Block @ " + "[X:" + stepX + "][Y:" + stepY + "][Z:" + stepZ + "]" + " with xDir:"
				+ xDir + " zDir:" + zDir);
		this.world.setBlock(stepX, stepY, stepZ, Blocks.bedrock, 0, 3);
		Utils.LOG_INFO("Makng it Bedrock for future investment.");
		// for (int i = -1; i <= 1; i++) {
		// stepX = stepX+i;
		for (int i = stepX - 1; i <= stepX + 1; i++) {
			for (int j = stepZ - 1; j <= stepZ + 1; j++) {
				for (int h = stepY - 1; h <= stepY + 1; h++) {

					xDir = ForgeDirection.getOrientation(this.player.getPlayerCoordinates().posX).offsetX;
					zDir = ForgeDirection.getOrientation(this.player.getPlayerCoordinates().posZ).offsetZ;

					// for (int j = -1; j <= 1; j++) {
					// stepZ = stepZ+j;
					// for (int h = -1; h <= 1; h++) {
					// stepY = stepY+h;
					Utils.LOG_INFO("Placing Block @ " + "[X:" + i + "][Y:" + h + "][Z:" + j + "]" + " with xDir:" + xDir
							+ " zDir:" + zDir);
					if (h != 0 || (xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0)) {
						this.world.setBlock(i, h, j, Blocks.stone, 0, 3);
					}
					else {
						Utils.LOG_INFO("Not even sure what this is for, but I got here.");
					}
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					}
					catch (final InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}