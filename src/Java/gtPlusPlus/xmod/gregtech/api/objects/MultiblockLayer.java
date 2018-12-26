package gtPlusPlus.xmod.gregtech.api.objects;

import java.util.HashMap;

import gregtech.api.GregTech_API;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.data.ArrayUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraftforge.common.util.ForgeDirection;

public class MultiblockLayer {
	
	public final int width;
	public final int depth;
	
	private boolean mFinalised;
	
	/**
	 * WARNING!! May be {@link null}.
	 */
	private Pair<Integer, Integer> mControllerLocation;
	
	/**
	 * Holds the North facing Orientation data.
	 */
	public final LayerBlockData[][] mLayerData;
	public final AutoMap<LayerBlockData[][]> mVariantOrientations = new AutoMap<LayerBlockData[][]>();
	
	/**
	 * A detailed class which will contain a single x/z Layer for a {@link MultiblockBlueprint}. 
	 * Values are not relative, but in total.
	 * @param x - Overall width
	 * @param z - Overall depth
	 */
	public MultiblockLayer(final int x, final int z) {
		width = x;
		depth = z;
		mLayerData = new LayerBlockData[x][z];
		Logger.INFO("Created new Blueprint Layer.");
	}
	
	/**
	 * A detailed class which will contain a single x/z Layer for a {@link MultiblockBlueprint}. 
	 * Values are not relative, but in total.
	 */
	public MultiblockLayer(final LayerBlockData[][] aData) {
		width = aData.length;
		depth = aData[0].length;
		mLayerData = aData;
	}
	
	/**
	 * 
	 * @param aBlock - The block expected as this location.
	 * @param aMeta - The meta for the block above.
	 * @param x - The X location, where 0 is the top left corner & counts upwards, moving right.
	 * @param z - The Z location, where 0 is the top left corner & counts upwards, moving down.
	 * @param canBeHatch - is this location able to be substituted for any hatch?
	 * @return - Is this data added to the layer data map?
	 */
	public boolean addBlockForPos(Block aBlock, int aMeta, int x, int z, boolean canBeHatch) {		
		return addBlockForPos(aBlock, aMeta, x, z, canBeHatch, null);
	}
	
	/**
	 * 
	 * @param aBlock - The block expected as this location.
	 * @param aMeta - The meta for the block above.
	 * @param x - The X location, where 0 is the top left corner & counts upwards, moving right.
	 * @param z - The Z location, where 0 is the top left corner & counts upwards, moving down.
	 * @param canBeHatch - is this location able to be substituted for a hatch?
	 * @param aHatchTypeClass - Specify the class for the hatch if you want it explicit. Use base classes to allow custom hatches which extend.
	 * @return - Is this data added to the layer data map?
	 */
	public boolean addBlockForPos(Block aBlock, int aMeta, int x, int z, boolean canBeHatch, Class aHatchTypeClass) {		
		if (x > width -1) {
			return false;
		}
		if (z > depth - 1) {
			return false;
		}		
		mLayerData[x][z] = new LayerBlockData(aBlock, aMeta, canBeHatch, aHatchTypeClass);		
		return true;
	}
	
	/**
	 * Adds a controller to the layer at the designated location, Details about the controller do not need to be specified.
	 * @param x - The X location, where 0 is the top left corner & counts upwards, moving right.
	 * @param z - The Z location, where 0 is the top left corner & counts upwards, moving down.
	 * @return - Is this controller added to the layer data map?
	 */
	public boolean addController(int x, int z) {
		setControllerLocation(new Pair<Integer, Integer>(x, z));		
		return addBlockForPos(GregTech_API.sBlockMachines, 0, x, z, true, GT_MetaTileEntity_MultiBlockBase.class);
	}
	
	/**
	 * 
	 * @param aBlock - The block you expect.
	 * @param aMeta - The meta you expect.
	 * @param x - the non-relative x location you expect it.
	 * @param z - the non-relative z location you expect it.
	 * @param aDir - The direction the controller is facing.
	 * @return - True if the correct Block was found. May also return true if a hatch is found when allowed or it's the controller.
	 */
	public boolean getBlockForPos(Block aBlock, int aMeta, int x, int z, ForgeDirection aDir) {			
		LayerBlockData g;
		if (aDir == ForgeDirection.NORTH) {
			g = mVariantOrientations.get(0)[x][z];
		}
		else if (aDir == ForgeDirection.EAST) {
			g = mVariantOrientations.get(1)[x][z];
		}
		else if (aDir == ForgeDirection.SOUTH) {
			g = mVariantOrientations.get(2)[x][z];
		}
		else if (aDir == ForgeDirection.WEST) {
			g = mVariantOrientations.get(3)[x][z];
		}
		else {
			g = mLayerData[x][z];			
		}		
		if (g == null) {
			return false;
		}
		
		return g.match(aBlock, aMeta);
	}
	
	
	
	/**
	 * Is this layer final?
	 * @return - If true, layer data cannot be edited.
	 */
	public final boolean isLocked() {
		return mFinalised;
	}

	/**
	 * Used to finalize the layer, after which all four Orientations are then generated. 
	 * Cannot be set to false, useful for not locking the layer if an error occurs.
	 * @param lockData
	 */
	public final void lock(boolean lockData) {
		if (!lockData) {
			return;
		}
		this.mFinalised = true;
		generateOrientations();
	}
	
	private void generateOrientations() {
		//North
		mVariantOrientations.put(mLayerData);
		LayerBlockData[][] val;
		//East
		val = ArrayUtils.rotateArrayClockwise(mLayerData);
		mVariantOrientations.put(val);
		//South
		val = ArrayUtils.rotateArrayClockwise(mLayerData);
		mVariantOrientations.put(val);
		//West
		val = ArrayUtils.rotateArrayClockwise(mLayerData);
		mVariantOrientations.put(val);
	}
	
	public boolean hasController() {
		if (getControllerLocation() == null) {
			return false;
		}
		return true;
	}

	public Pair<Integer, Integer> getControllerLocation() {
		return mControllerLocation;
	}

	public void setControllerLocation(Pair<Integer, Integer> mControllerLocation) {
		if (hasController()) {
			return;
		}		
		this.mControllerLocation = mControllerLocation;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * Generates a complete {@link MultiblockLayer} from String data.
	 * @param aDataMap - A  {@link HashMap} containing single character {@link String}s, which map to {@link Pair}<{@link Block}, {@link Integer}>s contains pairs of Blocks & Meta.
	 * @param aHorizontalStringRows - The horizontal rows used to map blocks to a grid. Each array slot is one vertical row going downwards as the index increases.
	 * @return
	 */
	public static MultiblockLayer generateLayerFromData(HashMap<String, Pair<Block, Integer>> aDataMap, String[] aHorizontalStringRows) {
		AutoMap<Pair<String, Pair<Block, Integer>>> x = new AutoMap<Pair<String, Pair<Block, Integer>>>();
		
		for (String u : aDataMap.keySet()) {
			Pair<Block, Integer> r = aDataMap.get(u);
			if (r != null) {
				x.put(new Pair<String, Pair<Block, Integer>>(u, r));
			}
		}

		//String aFreeLetters = "abdefgijklmnopqrstuvwxyz";
		/*for (Pair<Block, Integer> h : aDataMap.values()) {
			String y = aFreeLetters.substring(0, 0);
			aFreeLetters = aFreeLetters.replace(y.toLowerCase(), "");
			Pair<String, Pair<Block, Integer>> t = new Pair<String, Pair<Block, Integer>>(y, h);
			x.put(t);
		}*/
		return generateLayerFromData(x, aHorizontalStringRows);
	}
	
	
	/**
	 * Generates a complete {@link MultiblockLayer} from String data.
	 * @param aDataMap - An {@link AutoMap} which contains {@link Pair}s. These Pairs hold a single character {@link String} and another Pair. This inner pair holds a {@link Block} and an {@link Integer}.
	 * @param aHorizontalStringRows - An array which holds the horizontal (X/Width) string data for the layer.
	 * @return A complete Multiblock Layer.
	 */
	public static MultiblockLayer generateLayerFromData(AutoMap<Pair<String, Pair<Block, Integer>>> aDataMap, String[] aHorizontalStringRows) {
		int width = aHorizontalStringRows[0].length();
		int depth = aHorizontalStringRows.length;
		MultiblockLayer L = new MultiblockLayer(width, depth);
		HashMap<String, Pair<Block, Integer>> K = new HashMap<String, Pair<Block, Integer>>();
		
		//24 Free Letters
		//C = Controller
		//H = Hatch
		String aFreeLetters = "abdefgijklmnopqrstuvwxyz";
		AutoMap<Pair<String, Pair<Block, Integer>>> j = new AutoMap<Pair<String, Pair<Block, Integer>>>();
		
		//Map the keys to a Hashmap
		for (Pair<String, Pair<Block, Integer>> t : aDataMap) {
			String aKeyTemp = t.getKey();
			if (aKeyTemp.toUpperCase().equals("C")){
				j.put(t);
			}
			else if (aKeyTemp.toUpperCase().equals("H")){
				j.put(t);
			}
			else {
				K.put(aKeyTemp.toLowerCase(), t.getValue());
				aFreeLetters.replace(aKeyTemp.toLowerCase(), "");
			}
		}
		
		//Map any Invalid Characters to new ones, in case someone uses C/H.
		if (j.size() > 0) {
			for (Pair<String, Pair<Block, Integer>> h : j) {
				String newKey = aFreeLetters.substring(0, 0);
				K.put(newKey.toLowerCase(), h.getValue());
				aFreeLetters.replace(newKey.toLowerCase(), "");				
			}
		}
		
		int xPos = 0;
		int zPos = 0;
		
		//Vertical Iterator
		for (String s : aHorizontalStringRows) {			
			//Horizontal Iterator
			for (int q = 0; q < s.length(); q++) {				
				//Get char as a String at index q.
				String c = s.substring(q, q);				
				//if the character at c matches the character in this row, we add it to the map.
				if (c.toLowerCase().equals(s.toLowerCase())) {
					Pair<Block, Integer> p = K.get(c);					
					if (c.toLowerCase().equals("c")) {
						L.addController(xPos, zPos);	
					}
					else if (c.toLowerCase().equals("h")) {
						L.addBlockForPos(p.getKey(), p.getValue(), xPos, zPos, true);					
					}
					else {
						L.addBlockForPos(p.getKey(), p.getValue(), xPos, zPos, false);						
					}					
				}
				xPos++;
			}
			xPos = 0;
			zPos++;
		}	
		L.lock(true);
		return L;
	}




	public class LayerBlockData{
		
		public final Block mBlock;
		public final int mMeta;
		public final boolean canBeHatch;
		public final Class mHatchClass;
		
		private final boolean isController;
		

		public LayerBlockData(Block aBlock, int aMeta, boolean aHatch) {
			this(aBlock, aMeta, aHatch, null);
		}
		
		public LayerBlockData(Block aBlock, int aMeta, boolean aHatch, Class clazz) {
			mBlock = aBlock;
			mMeta = aMeta;
			canBeHatch = aHatch;
			mHatchClass = clazz;
			if (clazz.equals(GT_MetaTileEntity_MultiBlockBase.class)) {
				isController = true;
			}
			else {
				isController = false;
			}
		}
		
		public boolean match(Block blockToTest, int metaToTest) {
			
			//If Both are some kind of Air Block, good enough.
			if (blockToTest instanceof BlockAir && mBlock instanceof BlockAir) {
				return true;
			}
			
			//If Block does't match at all and it cannot be hatch
			if (blockToTest != mBlock && !canBeHatch) {
				return false;
			}
			
			//If Block does Match, is not controller, is not hatch and Meta does not match
			if (!isController && !canBeHatch && metaToTest != mMeta) {
				return false;
			}			
			return true;
		}
	}
	
}
