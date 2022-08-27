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

package com.github.bartimaeusnek.bartworks.ASM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.objectweb.asm.tree.MethodNode;

public class ASMUtils {

    public static String matchAny(String toCompare, String... args) {
        for (String arg : args) {
            if (toCompare.equalsIgnoreCase(arg)) return arg;
        }
        return "";
    }

    /**
     * Call this Method twice, one time for the Descriptor and one time for the Name.
     */
    public static boolean isCorrectMethod(MethodNode methodNode, String... args) {
        for (String arg : args) {
            if (methodNode.name.equalsIgnoreCase(arg) || methodNode.desc.equalsIgnoreCase(arg)) return true;
        }
        return false;
    }

    public static boolean writeClassToDisk(byte[] towrite, String Classname, String Path) {
        try {
            if (Path.charAt(Path.length() - 1) == '/' || Path.charAt(Path.length() - 1) == '\\')
                Path = Path.substring(0, Path.length() - 1);
            OutputStream os = new FileOutputStream(new File(Path + '/' + Classname + ".class"));
            os.write(towrite);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
