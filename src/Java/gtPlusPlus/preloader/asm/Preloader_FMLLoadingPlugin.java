package gtPlusPlus.preloader.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import gtPlusPlus.preloader.asm.transformers.Preloader_Transformer_Handler;

@SortingIndex(10097) 
@MCVersion(value = "1.7.10")
public class Preloader_FMLLoadingPlugin implements IFMLLoadingPlugin  {

	//-Dfml.coreMods.load=gtPlusPlus.preloader.asm.Preloader_FMLLoadingPlugin
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		//This will return the name of the class
		return new String[]{Preloader_Transformer_Handler.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		//This is the name of our dummy container
		return Preloader_DummyContainer.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

}