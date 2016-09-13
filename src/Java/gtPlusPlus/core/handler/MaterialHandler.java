package gtPlusPlus.core.handler;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.materials.MaterialUtils;

//Finally Wrote a proper material class, sigh.
public class MaterialHandler implements Runnable{

	int arrayPos = 0;
	boolean[] oneOfEachElementArray;
	Material[] AllGregtechMaterials;	
	
	
	@Override
	public void run() {		
		//Register GT Base Materials First
		Utils.LOG_INFO("Adding All basic elements to the Material Dictionary.");
		for (Materials x: Materials.values()){
			if (x.getProtons() <= 100 && !oneOfEachElementArray[arrayPos]){
				AllGregtechMaterials[arrayPos] = MaterialUtils.generateMaterialFromGtENUM(x);
				oneOfEachElementArray[arrayPos] = true;
				arrayPos++;
			}
		}		
	}	
	
}
