package gregtech.loaders.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

import org.json.JSONObject;

import gregtech.api.util.GT_Log;

@SideOnly(Side.CLIENT)
public class GT_JsonLoader implements IResourceManagerReloadListener {
	private JSONObject json;
	private ResourceLocation jsonLocation;
	private IResourceManager resourceManager;

	public String getString(String key) {
		String s = "";
		try {
			s = this.json.getString(key);
		}
		catch (Exception e) {
			GT_Log.err.println("GT_JsonLoader" + e);
		}
		return s;
	}

	public void loadJson() {
		this.json = new JSONObject("{}");
		try {
			BufferedInputStream bis = new BufferedInputStream(this.resourceManager.getResource(this.jsonLocation).getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			for (int result = bis.read(); result != -1; result = bis.read()) {
				bos.write((byte)result);
			}

			this.json = new JSONObject(bos.toString("UTF-8"));
		}
		catch (Exception e) {
			GT_Log.err.println("GT_JsonLoader: " + e);
		}
	}

	public void onResourceManagerReload(IResourceManager rm) {
		loadJson();
	}

	public GT_JsonLoader (String resourcePath) {
		GT_Log.err.println("GT_JsonLoader: Init");
		this.jsonLocation = new ResourceLocation("gregtech", resourcePath);
		this.resourceManager = Minecraft.getMinecraft().getResourceManager();
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
		loadJson();
	}
}