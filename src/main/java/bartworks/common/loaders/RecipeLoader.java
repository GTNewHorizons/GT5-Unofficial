/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.loaders;

import bartworks.common.loaders.recipes.Assembler;
import bartworks.common.loaders.recipes.AssemblyLine;
import bartworks.common.loaders.recipes.Autoclave;
import bartworks.common.loaders.recipes.Centrifuge;
import bartworks.common.loaders.recipes.ChemicalBath;
import bartworks.common.loaders.recipes.ChemicalReactor;
import bartworks.common.loaders.recipes.CraftingRecipes;
import bartworks.common.loaders.recipes.Electrolyzer;
import bartworks.common.loaders.recipes.Extractor;
import bartworks.common.loaders.recipes.FluidHeater;
import bartworks.common.loaders.recipes.FluidSolidifier;
import bartworks.common.loaders.recipes.FormingPress;
import bartworks.common.loaders.recipes.LaserEngraver;
import bartworks.common.loaders.recipes.Mixer;
import bartworks.common.loaders.recipes.Pulverizer;
import bartworks.common.loaders.recipes.PyrolyseOven;
import gregtech.api.util.GTModHandler;

public class RecipeLoader {

    public static final long BITSD = GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
        | GTModHandler.RecipeBits.REVERSIBLE;

    public static void run() {
        new Assembler().run();
        new AssemblyLine().run();
        new Autoclave().run();
        new Centrifuge().run();
        new ChemicalBath().run();
        new ChemicalReactor().run();
        new CraftingRecipes().run();
        new Electrolyzer().run();
        new Extractor().run();
        new FluidHeater().run();
        new FluidSolidifier().run();
        new FormingPress().run();
        new LaserEngraver().run();
        new Mixer().run();
        new Pulverizer().run();
        new PyrolyseOven().run();
    }
}
