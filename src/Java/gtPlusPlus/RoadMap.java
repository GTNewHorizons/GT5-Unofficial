package gtPlusPlus;

/**
 * This Class purely exists to note down ideas and or plans to (re)implement things.
 * 
 * @author Alkalus
 *
 */
public class RoadMap {

	//Reorganization of Item, Block and Common Class loading.
	/*
	 * So, due to the complex/silly way I've done things, I've ran into some circular loading problems around the mod.
	 * Issues occur where Classes like CI.java try access the GregtechItemList.java objects before they're actually set.
	 * A plan should be created to organize the best scheme to load things in the best order.
	 */
	
	//Recreation of GUIs for all Multiblocks
	/*
	 * Most Multi's use generic or straight out wrong GUI's on the controller.
	 * I'd like to go back and recreate all of these.
	 * 
	 * Some could even benefit from a totally new type of UI (Instead of Text issues, just change a 2x2px area between red and green for status lights)
	 * These advanced GUIs are probably out of my capability, but if anyone thinks they're a good idea, I'll give them a go.
	 */
	
	//Better Integration with GTNH
	/*
	 * Refactor things to be more common, refactor things to automatically switch between GTNH and standard variants 
	 * without having to over-abuse CORE.GTNH switches everywhere.
	 * Most of this can be done via expanding CI.java, so that we have automated handlers for everything 
	 * (IE. getX(5) will get 5x of the correct version of X)
	 */
	
	
}
