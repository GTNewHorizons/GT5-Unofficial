package gtPlusPlus.core.world.darkworld.object;

import gtPlusPlus.core.util.array.Pair;
import net.minecraft.block.Block;

public class BoxedQuad<K,V,C,R> {

	private final Pair<Block, Integer> key;
	private final Pair<Block, Integer> value;
	private final Pair<Block, Integer> value2;
	private final Pair<Block, Integer> value3;
	private final Pair<Block, Integer> [] mInternalPairArray;
	

	public BoxedQuad(final Pair<Block, Integer> key, final Pair<Block, Integer> value, final Pair<Block, Integer> value2, final Pair<Block, Integer> value3){
		this.key = key;
		this.value = value;
		this.value2 = value2;
		this.value3 = value3;
		mInternalPairArray = new Pair[]{key, value, value2, value3};
	}

	final public Pair<Block, Integer> getKey(){
		return this.key;
	}
	
	final public Pair<Block, Integer> getValue_1(){
		return this.value;
	}

	final public Pair<Block, Integer> getValue_2(){
		return this.value2;
	}

	final public Pair<Block, Integer> getValue_3(){
		return this.value3;
	}
	
	final synchronized Pair<Block, Integer> unbox(int pos){
		return this.mInternalPairArray[pos];
	}
	
	final synchronized Block getBlock(int pos){
		return this.mInternalPairArray[pos].getKey();		
	}
	
	final synchronized int getMeta(int pos){
		return this.mInternalPairArray[pos].getValue();		
	}
	
	
	
	

}