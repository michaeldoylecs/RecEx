package com.bigbass.recex;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;

public final class Logger
{
	private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger("RecEx");
	private static final HashMap<String, IntermittentLogInfo> intermittentLogInfoMap = new HashMap<>();
	
	private Logger() {}
	
	public static void info(String message) {
		LOGGER.info(message);
	}
	
	public static void error(String message) {
		LOGGER.error(message);
	}
	
	public static void chatMessage(String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
	}
	
	public static void intermittentLog(String logKey, String message) {
		IntermittentLogInfo loggerInfo = intermittentLogInfoMap.get(logKey);
		if (loggerInfo == null) {
			loggerInfo = new IntermittentLogInfo(100);
			intermittentLogInfoMap.put(logKey, loggerInfo);
		}
		if (loggerInfo.shouldLog()) {
			LOGGER.info(message);
		}
		loggerInfo.increment();
	}
	
	private static class IntermittentLogInfo{
		int logInterval;
		int logCount;
		
		public IntermittentLogInfo(int logInterval) {
			this.logInterval = logInterval;
			this.logCount = 0;
		}
		
		public boolean shouldLog() {
			if (logCount == 0) { return true; }
			return logCount % logInterval == 0;
		}
		
		public void increment() {
			logCount++;
		}
		
	}
}
