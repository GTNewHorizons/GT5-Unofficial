package gtPlusPlus.core.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.preloader.ChunkDebugger;
import gtPlusPlus.preloader.asm.AsmConfig;


public class CommandDebugChunks implements ICommand
{
	private final List<String> aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	public CommandDebugChunks(){
		this.aliases = new ArrayList<>();
		aliases.add("debugchunks");
		aliases.add("DC");
	}

	@Override
	public int compareTo(final Object o){
		return 0;

	}

	@Override
	public String getCommandName(){
		return "DebugChunks";

	}

	@Override
	public String getCommandUsage(final ICommandSender var1){
		return "/DebugChunks";

	}

	@Override
	public List<String> getCommandAliases(){
		return this.aliases;

	}

	@Override
	public void processCommand(final ICommandSender S, final String[] argString){
		Logger.INFO("Debug Command");		

		final World W = S.getEntityWorld();
		final EntityPlayer P = CommandUtils.getPlayer(S);
		
		if (!AsmConfig.enableChunkDebugging) {
			PlayerUtils.messagePlayer(P, "Chunk Debugging is disabled.");
			return;
		}
		
		
		Map<String, Integer> aTicketCounter = new LinkedHashMap<String, Integer>();		
		for (Pair<String, String> f : ChunkDebugger.mChunkTicketsMap.values()) {
			String aMod = f.getKey();
			String aDim = f.getValue();			
			if (aMod != null) {
				if (aTicketCounter.containsKey(aMod)) {
					int aModTicketCount = aTicketCounter.get(aMod);
					aModTicketCount++;
					aTicketCounter.put(aMod, aModTicketCount);
				}
				else {
					aTicketCounter.put(aMod, 1);					
				}				
			}
		}	
		if (aTicketCounter.isEmpty()) {
			PlayerUtils.messagePlayer(P, "No forced chunk tickets active.");			
		}		
		for (String x : aTicketCounter.keySet()) {
			PlayerUtils.messagePlayer(P, x+" has "+aTicketCounter.get(x)+" tickets active.");			
		}
		

		Map<String, Integer> aChunkCounter = new LinkedHashMap<String, Integer>();
		for (Pair<String, String> f : ChunkDebugger.mChunksLoadedByModsMap.values()) {
			String aMod = f.getKey();
			String aDim = f.getValue();	
			if (aMod == null) {
				aMod = "Bad ModId";
			}
			
			if (aMod != null) {
				//PlayerUtils.messagePlayer(P, aMod+" has "+aDim+" active.");	
				if (aChunkCounter.containsKey(aMod)) {
					int aModTicketCount = aChunkCounter.get(aMod);
					aModTicketCount = aModTicketCount + 1;
					aChunkCounter.put(aMod, aModTicketCount);
					Logger.INFO("Counting +1 for "+aMod+", total of "+aModTicketCount);
				}
				else {
					aChunkCounter.put(aMod, 1);	
					Logger.INFO("Counting +1 for "+aMod);				
				}				
			}
		}	
		if (aChunkCounter.isEmpty()) {
			PlayerUtils.messagePlayer(P, "No chunks force loaded.");			
		}		
		for (String x : aChunkCounter.keySet()) {
			PlayerUtils.messagePlayer(P, x+" has "+aChunkCounter.get(x)+" chunks active.");			
		}
		
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender var1){
		final EntityPlayer P = CommandUtils.getPlayer(var1);
		if (P == null){
			return false;
		}
		if (PlayerUtils.isPlayerOP(P)) {
			return true;
		}
		return false;
	}

	@Override
	public List<?> addTabCompletionOptions(final ICommandSender var1, final String[] var2){
		return new ArrayList<>();
	}

	@Override
	public boolean isUsernameIndex(final String[] var1, final int var2){
		// TODO Auto-generated method stub
		return false;
	}

	public boolean playerUsesCommand(final World W, final EntityPlayer P, final int cost){
		return true;
	}

}