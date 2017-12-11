package gtPlusPlus.api.analytics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.authlib.GameProfile;
import com.segment.analytics.Analytics;
import com.segment.analytics.messages.IdentifyMessage;
import com.segment.analytics.messages.TrackMessage;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.PlayerUtils;
import gtPlusPlus.core.util.uuid.UUIDGenerator;
import gtPlusPlus.core.util.uuid.UUIDUtils;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;

public class SegmentAnalytics {	
	
	//Globally Enabled
	public static boolean isEnabled = true;
	
	//Analytics Map with IDs
	 public static final Map<Integer, SegmentAnalytics> sAnalyticsMasterList = new ConcurrentHashMap<Integer, SegmentAnalytics>();
	//ID count
	 private static int sAnalyticsMapID = 0;
	 
	//Analytics Player Mapping
	 public static final Map<UUID, Integer> sAnalyticsToPlayermap = new ConcurrentHashMap<UUID, Integer>();
	
	//Set some Vars
	final BlockingFlush mBlockingFlush;
	final Analytics mAnalytics;
	final UUIDGenerator mUuidGenerator;
	
	final GameProfile mLocalProfile;
	final String aLocalName;
	final UUID aUUID;
	final String aUserName;
	final String anonymousId;

	//Build a new instance of this class
	public SegmentAnalytics(EntityPlayer mPlayer){
		LOG("Generating a new instance of Segment Analytics Handler 2.1.0 for "+mPlayer.getDisplayName());
		
		int currentID = sAnalyticsMapID;
		sAnalyticsMapID++;
		
		sAnalyticsMasterList.put(currentID, this);
		sAnalyticsToPlayermap.put(mPlayer.getUniqueID(), currentID);		
		
		this.mLocalProfile = mPlayer.getGameProfile();
		this.aLocalName = mLocalProfile.getName();
		this.aUUID = PlayerUtils.getPlayersUUIDByName(aLocalName);
		this.aUserName = aUUID.toString();
		this.anonymousId = getStringForm(generateIdForSession());

		//Create a new UUID generator.
		this.mUuidGenerator = new UUIDGenerator();

		//Use Segment Analytics instead of plain Google Analytics.
		this.mBlockingFlush = BlockingFlush.create(); 
		this.mAnalytics = Analytics.builder("API_KEY_GOES_HERE") //
				.plugin(mBlockingFlush.plugin())
				.plugin(new AnalyticsLoggingPlugin())
				.build();
		
		//Let us submit a doorknock to Segment to let them know who this is.
		submitInitData(mPlayer);
	}

	//Sets vars and stops Analytics running if the player profile is invalid.
	private boolean canProcess(){
		//Invalid Player Profile
		if (mLocalProfile == null || !isEnabled){			
			return false;
		}
		if (aLocalName == null || aUUID == null || aUserName == null || anonymousId == null){
			//LOG("One player var remained null, returning false.");
			return false;
		}
		if (aLocalName != null && aUUID != null && aUserName != null && anonymousId != null){
			//LOG("All player vars are ok, returning true.");
			return true;
		}
		LOG("Something went wrong, returning false.");
		return false;
	}


	public void submitInitData(EntityPlayer mPlayer){
		if (!canProcess()){
			return;
		}
		Map<String, Object> properties = new LinkedHashMap<>();
		properties.put("username", aLocalName);
		properties.put("gt_version", Utils.getGregtechVersionAsString());
		if (LoadedMods.IndustrialCraft2){
			properties.put("ic2_version", IC2.VERSION);
		}
		properties.put("country_code", CORE.USER_COUNTRY);
		properties.put("gtnh", CORE.GTNH);		

		LOG("Created new Data packet, queued for submission.");		
		mAnalytics.enqueue(IdentifyMessage.builder()
				.userId(aUserName) //Save Username as UUID, for future sessions to attach to.
				.anonymousId(anonymousId) //Save Random Session UUID
				.traits(properties));

		mAnalytics.flush();
	}

	public void submitTrackingData(String aActionPerformed){
		if (!canProcess()){
			return;
		}		
		LOG("Queued submission of data for event "+aActionPerformed+".");	
		mAnalytics.enqueue(TrackMessage.builder(aActionPerformed) //
				//.properties(properties) //Save Stats
				.anonymousId(anonymousId) //Save Random Session UUID
				.userId(aUserName)); // Save Username as UUID, for future sessions to attach to.		
	}

	public UUID generateIdForSession(){
		return UUIDUtils.getUUIDFromBytes(generateUUID());
	}

	private final byte[] generateUUID(){
		return mUuidGenerator.next(4);
	}

	public final String getStringForm(UUID mID){
		return mID.toString();
	}
	
	// Non-Dev Comments
	public static void LOG(final String s) {
			Utils.getLogger().info("[Analytics] "+s);
	}
	
	public static SegmentAnalytics getAnalyticsForPlayer(EntityPlayer mPlayer){
		if (SegmentAnalytics.sAnalyticsToPlayermap.containsKey(mPlayer)){
			return SegmentAnalytics.sAnalyticsMasterList.get(SegmentAnalytics.sAnalyticsToPlayermap.get(mPlayer.getUniqueID()));
		}
		return null;
	}

}
