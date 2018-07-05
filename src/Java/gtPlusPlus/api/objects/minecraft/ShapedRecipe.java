package gtPlusPlus.api.objects.minecraft;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedRecipe {

	private final static String CHARS = "abcdefghijklmnop";
	public ShapedOreRecipe mRecipe;

	public ShapedRecipe(
			Object aInput1, Object aInput2, Object aInput3,
			Object aInput4, Object aInput5, Object aInput6,
			Object aInput7, Object aInput8, Object aInput9, 
			ItemStack aOutput) {

		this(new Object[] {aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9}, aOutput);

	}

	public ShapedRecipe(Object[] aInputs, ItemStack aOutput) {
		String aGridWhole = "";
		String aGrid[] = new String[3];

		//Just to be safe
		try {

			//Check if the output is invalid
			if (aOutput != null) {
				Object[] mVarags2 = null;
				Logger.RECIPE("Generating Shaped Crafting Recipe for "+aOutput.getDisplayName());

				//Build a Pair for each slot
				AutoMap<Pair<Character, Object>> aRecipePairs = new AutoMap<Pair<Character, Object>>();
				int aCharSlot = 0;
				for (Object stack : aInputs) {
					if (stack != null) {
						aRecipePairs.put(new Pair<Character, Object>(CHARS.charAt(aCharSlot++), stack));
					}
					else {
						aRecipePairs.put(new Pair<Character, Object>(' ', (ItemStack) null));				
					}
				}				

				//If we have enough valid slots, iterate them and build a String which represents the entire grid.
				//If this String is the correct length, we will split it into thirds and build the grid String array.
				if (aRecipePairs.size() == 9) {

					Logger.RECIPE("9 Char|Object pairs registered for recipe.");

					for (Pair<Character, Object> h : aRecipePairs) {
						if (h.getKey() != null) {
							aGridWhole += String.valueOf(h.getKey());
							Logger.RECIPE("Adding '"+String.valueOf(h.getKey())+"' to aGridWhole.");
						}				
					}

					Logger.RECIPE("aGridWhole: "+aGridWhole+" | size: "+aGridWhole.length());

					//Build crafting grid
					if (aGridWhole.length() == 9) {
						Logger.RECIPE("aGridWhole size == 9");
						aGrid[0] = ""+aGridWhole.charAt(0)+aGridWhole.charAt(1)+aGridWhole.charAt(2);
						aGrid[1] = ""+aGridWhole.charAt(3)+aGridWhole.charAt(4)+aGridWhole.charAt(5);
						aGrid[2] = ""+aGridWhole.charAt(6)+aGridWhole.charAt(7)+aGridWhole.charAt(8);
					}

					//Rebuild the Map without spaces
					aRecipePairs.clear();
					aCharSlot = 0;
					int counter = 3;
					for (Object stack : aInputs) {
						if (stack != null) {
							aRecipePairs.put(new Pair<Character, Object>(CHARS.charAt(aCharSlot), stack));
							Logger.RECIPE("Registering Pair of '"+CHARS.charAt(aCharSlot)+" and a "+stack.getClass().getSimpleName()+" object.");
							aCharSlot++;
							counter++;
						}
					}

					Logger.RECIPE("Counter started at 3, counter is now at "+counter+". Trying to create Varag array with a size of "+((counter-3)*2));

					//Register the shaped grid straight to the varags
					mVarags2 = new Object[((counter-3)*2)];
					mVarags2[0] = aGrid[0];
					mVarags2[1] = aGrid[1];
					mVarags2[2] = aGrid[2];

					//Add Each Char, then Item to the varags, sequentially.
					int counter2 = 3;		
					for (Pair<Character, Object> r : aRecipePairs) {			
						char c = r.getKey();
						Object o = r.getValue();			
						mVarags2[counter2] = (char) c;
						mVarags2[counter2+1] = o;	
						counter2 += 2;
					}	
				}

				//Try set the recipe for this object.
				ShapedOreRecipe testRecipe = null;		
				try {
					testRecipe = new ShapedOreRecipe(aOutput, mVarags2);
				}
				catch (Throwable t) {
					Logger.RECIPE("Error thrown when making a ShapedOreRecipe object.");
					t.printStackTrace();
				}
				if (testRecipe == null) {
					this.mRecipe = null;
					Logger.RECIPE("Failed to generate a shaped recipe.");
				}
				else {
					this.mRecipe = testRecipe;
					Logger.RECIPE("Generated a shaped recipe successfully.");
				}
			}

			//Output was not valid
			else {			
				this.mRecipe = null;	
				Logger.RECIPE("Failed to generate a shaped recipe. Output was not valid.");		
			}


		}
		catch(Throwable t) {
			this.mRecipe = null;			
		}
	}

}
