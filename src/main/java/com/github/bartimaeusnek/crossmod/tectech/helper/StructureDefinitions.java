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

package com.github.bartimaeusnek.crossmod.tectech.helper;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.transpose;

public enum StructureDefinitions {
    CUBE_NO_MUFFLER(transpose(
            new String[][]{
                    {"VVV", "VVV", "VVV"},
                    {"V~V", "V-V", "VVV"},
                    {"VVV", "VVV", "VVV"}
            }))
    ,
    CUBE_MUFFLER(transpose(
            new String[][]{
                    {"VVV", "VMV", "VVV"},
                    {"V~V", "V-V", "VVV"},
                    {"VVV", "VVV", "VVV"}
            }))
    ;

    private final String[][] definition;

    public String[][] getDefinition() {
        return definition;
    }

    StructureDefinitions(String[][] definition) {
        this.definition = definition;
    }
}
