package gtPlusPlus.api.analytics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;

import com.mojang.authlib.GameProfile;
import com.segment.analytics.Analytics;
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
	final SegmentHelper mHelper;
	final UUIDGenerator mUuidGenerator;

	public final GameProfile mLocalProfile;
	public final String mLocalName;
	public final UUID mUUID;
	public final String mUserName;
	public final String mAnonymousId;
	protected Map<String, Object> mProperties = new LinkedHashMap<>();
	final protected Phaser mPhaser;

	//Build a new instance of this class
	public SegmentAnalytics(EntityPlayer mPlayer){
		LOG("Initializing Segment for "+mPlayer.getDisplayName());

		//Give this Object an ID
		int currentID = sAnalyticsMapID;
		sAnalyticsMapID++;

		//Map this Object to it's ID and a Player UUID.
		sAnalyticsMasterList.put(currentID, this);
		sAnalyticsToPlayermap.put(mPlayer.getUniqueID(), currentID);

		//Create a Phaser
		this.mPhaser = new Phaser(1);	

		//Set vars for player
		this.mLocalProfile = mPlayer.getGameProfile();
		this.mLocalName = mLocalProfile.getName();
		this.mUUID = PlayerUtils.getPlayersUUIDByName(mLocalName);
		this.mUserName = mUUID.toString();
		this.mAnonymousId = getStringForm(generateIdForSession());

		//Create a new UUID generator.
		this.mUuidGenerator = new UUIDGenerator();

		//Use Segment Analytics instead of plain Google Analytics.
		this.mBlockingFlush = BlockingFlush.create(); 
		this.mHelper = SegmentHelper.getInstance();
		initTimer(mPlayer);
	}

	//Sets vars and stops Analytics running if the player profile is invalid.
	private boolean canProcess(){
		//Invalid Player Profile
		if (mLocalProfile == null || !isEnabled){			
			return false;
		}
		if (mLocalName == null || mUUID == null || mUserName == null || mAnonymousId == null){
			//LOG("One player var remained null, returning false.");
			return false;
		}
		if (mLocalName != null && mUUID != null && mUserName != null && mAnonymousId != null){
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
		mProperties = new LinkedHashMap<>();
		mProperties.put("username", mLocalName);
		mProperties.put("gt_version", Utils.getGregtechVersionAsString());
		if (LoadedMods.IndustrialCraft2){
			mProperties.put("ic2_version", IC2.VERSION);
		}
		mProperties.put("country_code", CORE.USER_COUNTRY);
		mProperties.put("gtnh", CORE.GTNH);		

		LOG("Created new Data packet, queued for submission.");	

		//Old Code, now passed to Helper Class
		/*mHelper.enqueue(IdentifyMessage.builder()
				.userId(mUserName) //Save Username as UUID, for future sessions to attach to.
				.traits(mProperties)
				//.anonymousId(mAnonymousId) //Save Random Session UUID
				);*/

		mHelper.addUser(this.mUserName, mProperties);
		
		if (CORE.GTNH){
			mHelper.groupUser("GT:NewHorizons", this.mUserName);
		}
		else {
			mHelper.groupUser("GT:Vanilla", this.mUserName);			
		}
		
	}

	public void submitTrackingData(String aActionPerformed){
		submitTrackingData(aActionPerformed, null);
	}

	public void submitTrackingData(String aActionPerformed, Object aObject){
		if (!canProcess()){
			return;
		}		

		Map<String, Object> properties = new LinkedHashMap<>();
		properties.put("blockType", aObject);
		String mObjectAsString = "Unknown";

		if (aObject != null){
			mObjectAsString = aObject.toString();
		}

		LOG("Queued submission of data for event "+aActionPerformed+". This was performed on "+mObjectAsString+".");	

		mHelper.trackUser(this.mUserName, aActionPerformed, properties);

		//Old Code, now passed to Helper Class
		/*mHelper.enqueue(TrackMessage.builder(aActionPerformed) //
				.userId(mUserName) // Save Username as UUID, for future sessions to attach to.	
				.properties(mProperties) //Save Stats
				//.anonymousId(mAnonymousId) //Save Random Session UUID
			);
		flushData();
		 */
	}

	public void flushData(){
		getAnalyticObject().flush();
	}

	public void flushDataFinal(){
		LOG("Flushing all data from Queue to Segment Analytics database.");
		getAnalyticObject().flush();
		mBlockingFlush.block();
		mPhaser.arriveAndAwaitAdvance();
		getAnalyticObject().shutdown();
		/*try {
			this.finalize();
		}
		catch (Throwable e) {
			Utils.LOG_INFO("Could not finalize Analytics Object.");
		}*/
	}

	public UUID generateIdForSession(){
		return UUIDUtils.getUUIDFromBytes(generateUUID());
	}

	private final byte[] generateUUID(){
		byte[] mUUID;

		if (this.mUuidGenerator != null){
			try {
				if ((mUUID = mUuidGenerator.next(4)) != null){
					LOG("Generated Type 4 UUID for Session ID.");
					return mUUID;
				}
				else if ((mUUID = mUuidGenerator.next(1)) != null){
					LOG("Generated Type 1 UUID for Session ID.");
					return mUUID;
				}
			}
			catch (Throwable t){
				t.printStackTrace();
			}
		}

		LOG("Generated Type 3 UUID for Session ID.");
		return UUIDUtils.getBytesFromUUID(UUID.randomUUID());

	}

	public final String getStringForm(UUID mID){
		return mID.toString();
	}

	// Non-Dev Comments
	public static void LOG(final String s) {
		if (CORE.DEBUG){
			Utils.getLogger().info("[Analytics] "+s);
		}
	}

	public static SegmentAnalytics getAnalyticsForPlayer(EntityPlayer mPlayer){
		try {
			if (mPlayer != null){
				if (SegmentAnalytics.sAnalyticsToPlayermap.containsKey(mPlayer.getUniqueID())){
					int ID = sAnalyticsToPlayermap.get(mPlayer.getUniqueID());
					return SegmentAnalytics.sAnalyticsMasterList.get(ID);
				}
				else {
					LOG("Map does not contain Player.");
				}
			}		
			else {
				LOG("Invalid Player.");	
			}
		}
		catch (Throwable t){
			t.printStackTrace();
		}
		return null;
	}

	public final Analytics getAnalyticObject() {
		return mHelper.getAnalyticsClient();
	}
	
	public final Map<String, Object> getPlayerProperties(){
		return this.mProperties;
	}


	public Timer initTimer(EntityPlayer mPlayer) {
		Timer timer;
		timer = new Timer();
		timer.schedule(new initPlayer(mPlayer), 2 * 1000);
		return timer;
	}

	//Timer Task for notifying the player.
	class initPlayer extends TimerTask {
		final EntityPlayer aPlayer;
		public initPlayer(EntityPlayer mPlayer) {
			this.aPlayer = mPlayer;
		}
		@Override
		public void run() {
			//Let us submit a doorknock to Segment to let them know who this is.
			submitInitData(aPlayer);
		}
	}

}
