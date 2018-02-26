package gtPlusPlus.core.handler.analytics;

import com.segment.analytics.*;
import com.segment.analytics.messages.Message;

import gtPlusPlus.api.objects.Logger;

/**
 * A {@link Plugin} implementation that redirects client logs to standard output and logs callback
 * events.
 */
public class AnalyticsLoggingPlugin implements Plugin {
  @Override public void configure(Analytics.Builder builder) {
    builder.log(new Log() {
      @Override public void print(Level level, String format, Object... args) {
        Logger.WARNING(level + ":\t" + String.format(format, args));
      }

      @Override public void print(Level level, Throwable error, String format, Object... args) {
        Logger.WARNING(level + ":\t" + String.format(format, args));
        //Utils.LOG_WARNING(error);
      }
    });

    builder.callback(new Callback() {
      @Override public void success(Message message) {
        Logger.WARNING("Uploaded " + message);
      }

      @Override public void failure(Message message, Throwable throwable) {
        Logger.WARNING("Could not upload " + message);
        //Utils.LOG_WARNING(throwable);
      }
    });
  }
}