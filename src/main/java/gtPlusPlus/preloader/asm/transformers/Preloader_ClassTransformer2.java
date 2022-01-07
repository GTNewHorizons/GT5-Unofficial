package gtPlusPlus.preloader.asm.transformers;

public class Preloader_ClassTransformer2 {

	/**
	 * 
	 * So what I'd try is something like patch a new field into BaseMetaTileEntity to hold the ItemNBT, 
	 * then patch GT_Block_Machines.breakBlock to store the ItemNBT into that field by calling setItemNBT, 
	 * and then patch BaseMetaTileEntity.getDrops to retrieve that field instead of calling setItemNBT
	 * But there's probably a simpler solution if all you want to do is fix this 
	 * for your super tanks rather than for all GT machines 
	 * (which would only include saving the output count for chest buffers and item distributors...)
	 *
	 */ 

}
