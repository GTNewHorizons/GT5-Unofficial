package gtPlusPlus.api.analytics;

import com.segment.analytics.Analytics;
import com.segment.analytics.Callback;
import com.segment.analytics.Log;
import com.segment.analytics.Plugin;
import com.segment.analytics.messages.Message;

import gtPlusPlus.core.util.Utils;

/**
 * A {@link Plugin} implementation that redirects client logs to standard output and logs callback
 * events.
 */
public class AnalyticsLoggingPlugin implements Plugin {
  @Override public void configure(Analytics.Builder builder) {
    builder.log(new Log() {
      @Override public void print(Level level, String format, Object... args) {
        Utils.LOG_WARNING(level + ":\t" + String.format(format, args));
      }

      @Override public void print(Level level, Throwable error, String format, Object... args) {
        Utils.LOG_WARNING(level + ":\t" + String.format(format, args));
        //Utils.LOG_WARNING(error);
      }
    });

    builder.callback(new Callback() {
      @Override public void success(Message message) {
        Utils.LOG_WARNING("Uploaded " + message);
      }

      @Override public void failure(Message message, Throwable throwable) {
        Utils.LOG_WARNING("Could not upload " + message);
        //Utils.LOG_WARNING(throwable);
      }
    });
  }
}