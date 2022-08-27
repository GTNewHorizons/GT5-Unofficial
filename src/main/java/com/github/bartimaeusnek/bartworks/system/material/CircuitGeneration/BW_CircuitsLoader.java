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

package com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration;

public class BW_CircuitsLoader {

    private static final BW_Meta_Items NEW_CIRCUITS = new BW_Meta_Items();

    private BW_CircuitsLoader() {}

    public static BW_Meta_Items getNewCircuits() {
        return BW_CircuitsLoader.NEW_CIRCUITS;
    }

    public static void initNewCircuits() {
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(0, 4, "Primitive Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(1, 5, "Basic Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(2, 6, "Good Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(3, 7, "Advanced Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(4, 8, "Data Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(5, 9, "Elite Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(6, 10, "Master Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(7, 11, "Ultimate Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(8, 12, "Superconductor Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(9, 13, "Infinite Magneto Resonatic Circuit");
        BW_CircuitsLoader.NEW_CIRCUITS.addNewCircuit(10, 14, "Bio Magneto Resonatic Circuit");
    }
}
