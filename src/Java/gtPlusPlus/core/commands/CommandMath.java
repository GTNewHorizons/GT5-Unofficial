package gtPlusPlus.core.commands;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.PlayerUtils;

import java.util.ArrayList;
import java.util.List;

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
		aliases = new ArrayList<String>(); 

		aliases.add("hometele"); 

		aliases.add("warphome"); 

	} 

	@Override 
	public int compareTo(Object o)
	{ 
		return 0; 

	} 

	@Override 
	public String getCommandName() 
	{ 
		return "bed"; 

	} 

	@Override         
	public String getCommandUsage(ICommandSender var1) 
	{ 
		return "/bed [Teleports you to your bed for XP]"; 

	} 

	@Override 
	public List<String> getCommandAliases() 
	{ 
		return this.aliases;

	} 

	public void processCommand(ICommandSender S, String[] argString)
	{ 
		World W = S.getEntityWorld(); 
		CommandUtils C = new CommandUtils();
		EntityPlayer P = C.getPlayer(S);
		//System.out.println(P.getCommandSenderName());
		//System.out.println(P.getDisplayName());
		if (W.isRemote) 

		{ 

			System.out.println("Not processing on Client side"); 

		} 

		else 

		{ 

			System.out.println("Processing on Server side - Home Teleport engaged by: "+P.getDisplayName()); 

			int XP_TOTAL = P.experienceTotal;
			Utils.LOG_WARNING("Total Xp:" + XP_TOTAL);
			ChunkCoordinates X = P.getPlayerCoordinates();
			Utils.LOG_WARNING("Player Location: "+X);
			ChunkCoordinates Y = null;
			Utils.LOG_WARNING("Bed Location: "+Y);
			try {
				if (P.getBedLocation(0) == null){
					Y = W.getSpawnPoint();
					Utils.LOG_WARNING("Spawn Location: "+Y);
				}				
				else if (P.getBedLocation(0) != null){
					Y = P.getBedLocation(0);
					Utils.LOG_WARNING("Bed Location: "+Y);
				}
				else {
					Y = W.getSpawnPoint();
					Utils.LOG_WARNING("Spawn Location: "+Y);
				}
			}
			catch(NullPointerException e) {
			    PlayerUtils.messagePlayer(P, "You do not have a spawn, so...");
			}
			if (Y == null) {
				Y = W.getSpawnPoint();
				Utils.LOG_WARNING("Spawn Location: "+Y);
			}

			int x1 = X.posX;
			Utils.LOG_WARNING("X1: "+x1);
			int x2 = Y.posX;
			Utils.LOG_WARNING("X2: "+x2);
			int y1 = X.posY;
			Utils.LOG_WARNING("Y1: "+y1);
			int y2 = Y.posY;
			Utils.LOG_WARNING("Y2: "+y2);
			int z1 = X.posZ;
			Utils.LOG_WARNING("Z1: "+z1);
			int z2 = Y.posZ;
			Utils.LOG_WARNING("Z2: "+z2);


			double d = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)+(z2-z1)*(z2-z1));
			String xpCost = String.valueOf((int)(d*0.15));

			Utils.LOG_WARNING("d:" + d);
			Utils.LOG_WARNING("-----------------------------------------");
			Utils.LOG_WARNING("Actual math formulae");
			Utils.LOG_WARNING(String.valueOf(d));
			Utils.LOG_WARNING("-----------------------------------------");
			Utils.LOG_WARNING("Xp Cost based on answer B.");
			Utils.LOG_WARNING(String.valueOf(d*0.15) + " | " + String.valueOf(xpCost));
			Utils.LOG_WARNING("-----------------------------------------");
			Utils.LOG_WARNING("Xp Total");
			Utils.LOG_WARNING(String.valueOf(XP_TOTAL));
			Utils.LOG_WARNING("-----------------------------------------");



			if ((XP_TOTAL-Float.valueOf(xpCost)) > 0){
				EntityXPOrb E = new EntityXPOrb(W, P.posX, P.posY + 1.62D - (double) P.yOffset, P.posZ, 1);
				//E.moveTowards((double) Y.posX + 0.5D, (int) Y.posY + 3, (double) Y.posZ + 0.5D);
				E.setVelocity((double) Y.posX + 0.5D, (int) Y.posY + 0.1, (double) Y.posZ + 0.5D);
				W.spawnEntityInWorld(E);
				W.playAuxSFXAtEntity((EntityPlayer) null, 1002, (int) P.posX, (int) P.posY, (int) P.posZ, 0);
				P.setPositionAndUpdate(x2, y2+1, z2);

				//gregtech.api.util.GT_Utility.sendChatToPlayer(P, "Movement | X:"+x2+" | Y:"+y2+" | Z:"+z2);
				gregtech.api.util.GT_Utility.sendChatToPlayer(P, "Distance Traveled | "+String.valueOf((int)(d)) + " Blocks & " + xpCost + "xp");
				gregtech.api.util.GT_Utility.sendChatToPlayer(P, "You suddenly feel at home.");
				P.experienceTotal = (int) (XP_TOTAL-Float.valueOf(xpCost));
				if (!xpCost.equals("0") && Float.valueOf(xpCost) > 0){
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
	public boolean canCommandSenderUseCommand(ICommandSender var1) 
	{ 
		return true;

	} 

	@Override  
	public List<?> addTabCompletionOptions(ICommandSender var1, String[] var2) 
	{ 
		// TODO Auto-generated method stub 

		return null; 

	} 

	@Override 
	public boolean isUsernameIndex(String[] var1, int var2) 
	{ 
		// TODO Auto-generated method stub 

		return false;

	}

	public boolean playerUsesCommand(World W, EntityPlayer P, int cost)
	{


		return true;
	}

}

