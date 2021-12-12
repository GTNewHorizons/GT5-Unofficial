/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gtPlusPlus.preloader.keyboard;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.asm.transformers.ClassTransformer_LWJGL_Keyboard;
import net.minecraft.client.resources.I18n;

/**
 * <br>
 * A raw Keyboard interface. This can be used to poll the current state of the
 * keys, or read all the keyboard presses / releases since the last read.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$
 * $Id$
 */
public class BetterKeyboard {

	public static final int KEYBOARD_SIZE = Short.MAX_VALUE;

	private static boolean init = false;

	public static void init() {
		if (!init) {
			FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Trying to patch out LWJGL internal arrays with larger ones.");			
			Field aKeyNameSize = ReflectionUtils.getField(Keyboard.class, "keyName");
			Field aKeyMapSize = ReflectionUtils.getField(Keyboard.class, "keyMap");
			Field aKeyDownBuffer = ReflectionUtils.getField(Keyboard.class, "keyDownBuffer");			
			String[] aOldKeyNameArray = (String[]) ReflectionUtils.getFieldValue(aKeyNameSize);
			if (aOldKeyNameArray != null && aOldKeyNameArray.length < Short.MAX_VALUE) {
				String[] aNewKeyNameArray = new String[Short.MAX_VALUE];	
				for (int i=0;i<aOldKeyNameArray.length;i++) {
					aNewKeyNameArray[i] = aOldKeyNameArray[i];
				}
				try {
					ReflectionUtils.setFinalFieldValue(Keyboard.class, aKeyNameSize.getName(), aNewKeyNameArray);	
					FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Patched Field: "+aKeyNameSize.getName());
				}
				catch (Throwable t) {
					FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Failed Patching Field: "+aKeyDownBuffer.getName());
				}
			}			
			Map<String, Integer> aOldKeyMapArray = (Map<String, Integer>) ReflectionUtils.getFieldValue(aKeyMapSize);
			if (aOldKeyNameArray != null && aOldKeyMapArray.size() < Short.MAX_VALUE) {
				Map<String, Integer>  aNewKeyMapArray = new HashMap<String, Integer>(Short.MAX_VALUE);	
				aNewKeyMapArray.putAll(aOldKeyMapArray);	
				try {
					ReflectionUtils.setFinalFieldValue(Keyboard.class, aKeyMapSize.getName(), aNewKeyMapArray);		
					FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Patched Field: "+aKeyMapSize.getName());
				}
				catch (Throwable t) {
					FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Failed Patching Field: "+aKeyDownBuffer.getName());
				}		
			}			
			ByteBuffer aOldByteBuffer = (ByteBuffer) ReflectionUtils.getFieldValue(aKeyDownBuffer);
			if (aOldByteBuffer != null && aOldByteBuffer.capacity() == Keyboard.KEYBOARD_SIZE) {
				ByteBuffer  aNewByteBuffer = BufferUtils.createByteBuffer(Short.MAX_VALUE);	
				try {
					ReflectionUtils.setFinalFieldValue(Keyboard.class, aKeyDownBuffer.getName(), aNewByteBuffer);		
					FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Patched Field: "+aKeyDownBuffer.getName());
				}
				catch (Throwable t) {	
					FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Failed Patching Field: "+aKeyDownBuffer.getName());
				}
			}			
			init = true;
		}				
	}


	/**
	 * Gets a key's name
	 * @param key The key
	 * @return a String with the key's human readable name in it or null if the key is unnamed
	 */
	public static synchronized String getKeyName(int key) {
		return ClassTransformer_LWJGL_Keyboard.getKeyName(key);
	}




	/**
	 * Represents a key or mouse button as a string. Args: key
	 */
	public static String getKeyDisplayString(int aKeyValue) {
		return aKeyValue < 0 ? I18n.format("key.mouseButton", new Object[] {Integer.valueOf(aKeyValue + 101)}): getKeyName(aKeyValue);
	}

}
