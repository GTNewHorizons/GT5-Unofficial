///*
// * Copyright (c) 2019 bartimaeusnek
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//
//package com.github.bartimaeusnek.bartworks.system.material;
//
//import com.github.bartimaeusnek.bartworks.MainMod;
//import cpw.mods.fml.common.ProgressManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.*;
//
//public class ThreadedLoader  implements Runnable {
//
//    List<Thread> threads = new ArrayList<>();
//
//    @Override
//    public synchronized void run() {
//        threads.add(new AllRecipes());
//
//        threads.forEach(Thread::start);
//
//
//    }
//
//
//    class AllRecipes extends Thread {
//
//        public synchronized void run() {
//            MainMod.LOGGER.info("Loading Processing Recipes for BW Materials");
//            long timepre = System.nanoTime();
//            ProgressManager.ProgressBar progressBar = ProgressManager.push("Register BW Materials", Werkstoff.werkstoffHashMap.size());
//
//            for (short i = 0; i < Werkstoff.werkstoffHashMap.size(); i++) {
//                Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get(i);
//                if (werkstoff == null || werkstoff.getmID() < 0) {
//                    progressBar.step("");
//                    continue;
//                }
//                addDustRecipes(werkstoff);
//                addGemRecipes(werkstoff);
//                addOreRecipes(werkstoff);
//                addCrushedRecipes(werkstoff);
//                progressBar.step(werkstoff.getDefaultName());
//            }
//            ProgressManager.pop(progressBar);
//            long timepost = System.nanoTime();
//            MainMod.LOGGER.info("Loading Processing Recipes for BW Materials took " + (timepost - timepre) + "ns/" + ((timepost - timepre) / 1000000) + "ms/" + ((timepost - timepre) / 1000000000) + "s!");
//        }
//    }
//
//
//
//}
