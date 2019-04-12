/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.system.material;

import java.util.ArrayList;
import java.util.List;

public class ThreadedLoader  implements Runnable {

    List<Thread> threads = new ArrayList<>();
    List<Thread> threadsInit = new ArrayList<>();


    @Override
    public synchronized void run() {
        threads.add(new AllRecipes());
        threads.forEach(Thread::start);
    }

    public synchronized void runInit() {
        threadsInit.add(new MaterialGen());
        threadsInit.forEach(Thread::start);
    }

    class AllRecipes extends Thread {

        public synchronized void run() {
            WerkstoffLoader.INSTANCE.run();
        }
    }

    class MaterialGen extends Thread {
        public synchronized void run() {
            WerkstoffLoader.INSTANCE.runInit();
        }
    }



}
