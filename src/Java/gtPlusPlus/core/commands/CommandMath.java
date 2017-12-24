package gtPlusPlus.core.commands;

import java.util.ArrayList;
import java.util.List;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.player.PlayerUtils;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;


public class CommandMath implements ICommand
{
	private final List<String> aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	public CommandMath()
	{
		this.aliases = new ArrayList<>();

		this.aliases.add("hometele");

		this.aliases.add("warphome");

	}

	@Override
	public int compareTo(final Object o)
	{
		return 0;

	}

	@Override
	public String getCommandName()
	{
		return "bed";

	}

	@Override
	public String getCommandUsage(final ICommandSender var1)
	{
		return "/bed [Teleports you to your bed for XP]";

	}

	@Override
	public List<String> getCommandAliases()
	{
		return this.aliases;

	}

	@Override
	public void processCommand(final ICommandSender S, final String[] argString)
	{
		final World W = S.getEntityWorld();
		final CommandUtils C = new CommandUtils();
		final EntityPlayer P = C.getPlayer(S);
		//System.out.println(P.getCommandSenderName());
		//System.out.println(P.getDisplayName());
		if (W.isRemote)

		{

			System.out.println("Not processing on Client side");

		}

		else

		{

			System.out.println("Processing on Server side - Home Teleport engaged by: "+P.getDisplayName());

			final int XP_TOTAL = P.experienceTotal;
			Logger.WARNING("Total Xp:" + XP_TOTAL);
			final ChunkCoordinates X = P.getPlayerCoordinates();
			Logger.WARNING("Player Location: "+X);
			ChunkCoordinates Y = null;
			Logger.WARNING("Bed Location: "+Y);
			try {
				if (P.getBedLocation(0) == null){
					Y = W.getSpawnPoint();
					Logger.WARNING("Spawn Location: "+Y);
				}
				else if (P.getBedLocation(0) != null){
					Y = P.getBedLocation(0);
					Logger.WARNING("Bed Location: "+Y);
				}
				else {
					Y = W.getSpawnPoint();
					Logger.WARNING("Spawn Location: "+Y);
				}
			}
			catch(final NullPointerException e) {
				PlayerUtils.messagePlayer(P, "You do not have a spawn, so...");
			}
			if (Y == null) {
				Y = W.getSpawnPoint();
				Logger.WARNING("Spawn Location: "+Y);
			}

			final int x1 = X.posX;
			Logger.WARNING("X1: "+x1);
			final int x2 = Y.posX;
			Logger.WARNING("X2: "+x2);
			final int y1 = X.posY;
			Logger.WARNING("Y1: "+y1);
			final int y2 = Y.posY;
			Logger.WARNING("Y2: "+y2);
			final int z1 = X.posZ;
			Logger.WARNING("Z1: "+z1);
			final int z2 = Y.posZ;
			Logger.WARNING("Z2: "+z2);


			final double d = Math.sqrt(((x2-x1)*(x2-x1))+((y2-y1)*(y2-y1))+((z2-z1)*(z2-z1)));
			final String xpCost = String.valueOf((int)(d*0.15));

			Logger.WARNING("d:" + d);
			Logger.WARNING("-----------------------------------------");
			Logger.WARNING("Actual math formulae");
			Logger.WARNING(String.valueOf(d));
			Logger.WARNING("-----------------------------------------");
			Logger.WARNING("Xp Cost based on answer B.");
			Logger.WARNING(String.valueOf(d*0.15) + " | " + String.valueOf(xpCost));
			Logger.WARNING("-----------------------------------------");
			Logger.WARNING("Xp Total");
			Logger.WARNING(String.valueOf(XP_TOTAL));
			Logger.WARNING("-----------------------------------------");



			if ((XP_TOTAL-Float.valueOf(xpCost)) > 0){
				final EntityXPOrb E = new EntityXPOrb(W, P.posX, (P.posY + 1.62D) - P.yOffset, P.posZ, 1);
				//E.moveTowards((double) Y.posX + 0.5D, (int) Y.posY + 3, (double) Y.posZ + 0.5D);
				E.setVelocity(Y.posX + 0.5D, Y.posY + 0.1, Y.posZ + 0.5D);
				W.spawnEntityInWorld(E);
				W.playAuxSFXAtEntity((EntityPlayer) null, 1002, (int) P.posX, (int) P.posY, (int) P.posZ, 0);
				P.setPositionAndUpdate(x2, y2+1, z2);

				//gregtech.api.util.GT_Utility.sendChatToPlayer(P, "Movement | X:"+x2+" | Y:"+y2+" | Z:"+z2);
				gregtech.api.util.GT_Utility.sendChatToPlayer(P, "Distance Traveled | "+String.valueOf((int)(d)) + " Blocks & " + xpCost + "xp");
				gregtech.api.util.GT_Utility.sendChatToPlayer(P, "You suddenly feel at home.");
				P.experienceTotal = (int) (XP_TOTAL-Float.valueOf(xpCost));
				if (!xpCost.equals("0") && (Float.valueOf(xpCost) > 0)){
					gregtech.api.util.GT_Utility.sendChatToPlayer(P, "...At the loss of "+xpCost+" xp.");
				}
				else if (xpCost.equals("0")){
					gregtech.api.util.GT_Utility.sendChatToPlayer(P, "...At the loss of very little xp.");
				}
				else {
					gregtech.api.util.GT_Utility.sendChatToPlayer(P, "Something went wrong with the math, have this one on the house. :)");
				}
			}

			else {
				gregtech.api.util.GT_Utility.sendChatToPlayer(P, "You don't feel you're able to do this yet.");
			}

		}
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender var1)
	{
		return true;

	}

	@Override
	public List<?> addTabCompletionOptions(final ICommandSender var1, final String[] var2)
	{
		// TODO Auto-generated method stub

		return null;

	}

	@Override
	public boolean isUsernameIndex(final String[] var1, final int var2)
	{
		// TODO Auto-generated method stub

		return false;

	}

	public boolean playerUsesCommand(final World W, final EntityPlayer P, final int cost)
	{


		return true;
	}

}

