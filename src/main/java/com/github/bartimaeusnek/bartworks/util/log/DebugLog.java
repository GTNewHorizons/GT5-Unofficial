/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.util.log;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class DebugLog {
    private static boolean init;
    static FileHandler fh;
    private static Logger utilLog;

    public static void initDebugLog(FMLPreInitializationEvent event) throws IOException {
        if (DebugLog.init) return;
        DebugLog.fh = new FileHandler(
                new File(new File(event.getModConfigurationDirectory().getParentFile(), "logs"), "BWLog.log")
                        .toString());
        DebugLog.utilLog = Logger.getLogger("DebugLog");
        DebugLog.utilLog.setUseParentHandlers(false);
        DebugLog.utilLog.addHandler(DebugLog.fh);
        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                SimpleDateFormat logTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(record.getMillis());
                return "Level: " + record.getLevel()
                        + " at " + logTime.format(cal.getTime())
                        + " " + record.getMessage() + "\n";
            }
        };
        DebugLog.fh.setFormatter(formatter);
        DebugLog.init = true;
    }

    public static void log(String record) {
        if (!DebugLog.init) return;
        DebugLog.utilLog.info(record);
    }
}
