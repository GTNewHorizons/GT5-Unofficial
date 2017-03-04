package gtPlusPlus.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Log
{
	public static final Logger LOGGER = LogManager.getLogger("MiscUtils");

	public static void warn(final String msg)
	{
		LOGGER.warn(msg);
	}

	public static void error(final String msg)
	{
		LOGGER.error(msg);
	}

	public static void info(final String msg)
	{
		LOGGER.info(msg);
	}

	public static void debug(final String msg)
	{
		LOGGER.debug(msg);
	}
}
