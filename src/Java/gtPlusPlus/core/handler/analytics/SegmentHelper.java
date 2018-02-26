package gtPlusPlus.core.handler.analytics;

import java.util.Map;

import com.segment.analytics.Analytics;
import com.segment.analytics.Callback;
import com.segment.analytics.messages.*;
 
public class SegmentHelper implements Callback {
	
	/**
	 * Credits to Author: FLAMINSAGANAKI/Theodore Mavrakis
	 * http://domisydev.com/2015/11/05/using-segment-analytics-in-your-java-servlet/
	 */	
	
    private static final String writeKey = "EDOWl9peleGlUqe1ZwTqKDyuTMFhyT4k";
    private static volatile SegmentHelper segment = new SegmentHelper();
    private Analytics analytics;
 
    public SegmentHelper(){
        try{
            this.analytics = Analytics.builder(writeKey).callback(this).build();
        }catch(Exception e){
            SegmentAnalytics.LOG("exception while creating Analytics : " + e);
        }
    }
 
    public static SegmentHelper getInstance(){
        return segment;
    }
  
    public Analytics getAnalyticsClient(){
        return segment.analytics;
    }
 
    public void success(Message message) {
        SegmentAnalytics.LOG("Successfully uploaded " + message);
    }
 
    public void failure(Message message, Throwable throwable) {
        SegmentAnalytics.LOG("Could not upload " + message);
    }
 
    public void addUser(String user_id, Map<String, Object> properties) {
        try {
            this.analytics.enqueue(IdentifyMessage.builder().userId(user_id).traits(properties));
            //trackUser(user_id, "Logged In", properties);
        } catch (Exception e) {
            SegmentAnalytics.LOG("Exception in addUser() - " + e);
        }
    }
 
    public void trackUser(String user_id, String description, Map<String, Object> properties) {
        try {
            this.analytics.enqueue(TrackMessage.builder(description).userId(user_id).properties(properties));
        } catch (Exception e) {
           SegmentAnalytics.LOG("Exception in trackUser() - " + e);
        }
    }
    
    public void trackUser(String user_id, String description) {
        try {
            this.analytics.enqueue(TrackMessage.builder(description).userId(user_id));
        } catch (Exception e) {
           SegmentAnalytics.LOG("Exception in trackUser() - " + e);
        }
    }
 
    public void groupUser(String group_id, String user_id) {
        try {
            this.analytics.enqueue(GroupMessage.builder(group_id).userId(user_id));
        } catch (Exception e) {
            SegmentAnalytics.LOG("Exception in groupUser() - " + e);
        }
    }
 
    public static void main(String[] args){
 
    }
}