package binnie.craftgui.resource.minecraft;

import binnie.core.resource.IBinnieTexture;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IBorder;
import binnie.craftgui.resource.Texture;
import com.google.common.base.Charsets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

@SideOnly(Side.CLIENT)
public class CraftGUIResourceManager
  implements IResourceManagerReloadListener
{
  private Map<String, ParsedTextureSheet> textureSheets = new HashMap();
  private Map<String, Texture> textures = new HashMap();
  
  public CraftGUIResourceManager()
  {
    binnie.craftgui.core.CraftGUI.ResourceManager = this;
  }
  
  public void onResourceManagerReload(IResourceManager manager)
  {
    this.textureSheets.clear();
    try
    {
      IResource res = manager.getResource(new ResourceLocation("binniecore", "gui/stylesheet.json"));
      
      JsonObject jsonobject = null;
      BufferedReader bufferedreader = null;
      try
      {
        bufferedreader = new BufferedReader(new InputStreamReader(res.getInputStream(), Charsets.UTF_8));
        jsonobject = new JsonParser().parse(bufferedreader).getAsJsonObject();
        for (JsonElement el : jsonobject.get("texture-sheets").getAsJsonArray()) {
          if ((el instanceof JsonObject))
          {
            JsonObject sheet = (JsonObject)el;
            String name = sheet.get("name").getAsString();
            String modid = sheet.get("modid").getAsString();
            String path = sheet.get("path").getAsString();
            this.textureSheets.put(name, new ParsedTextureSheet(name, modid, path));
          }
        }
        for (JsonElement el : jsonobject.get("textures").getAsJsonArray()) {
          if ((el instanceof JsonObject))
          {
            JsonObject sheet = (JsonObject)el;
            String name = sheet.get("name").getAsString();
            IBinnieTexture textureSheet = getTextureSheet(sheet.get("sheet").getAsString());
            IArea uv = getArea(sheet.get("uv").getAsString());
            IBorder border = IBorder.ZERO;
            IBorder padding = IBorder.ZERO;
            if (sheet.has("border")) {
              border = getBorder(sheet.get("border").getAsString());
            }
            if (sheet.has("padding")) {
              padding = getBorder(sheet.get("padding").getAsString());
            }
            this.textures.put(name, new Texture(uv, padding, border, textureSheet.getTexture()));
          }
        }
      }
      catch (RuntimeException runtimeexception)
      {
        throw new JsonParseException("Failed to parse stylesheet for Binnie's Mods", runtimeexception);
      }
      finally
      {
        IOUtils.closeQuietly(bufferedreader);
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException("Failed to load default stylesheet for Binnie's Mods.", e);
    }
  }
  
  public IArea getArea(String name)
  {
    String[] split = name.split(" ");
    if ((split.length < 1) || (split.length > 4)) {
      throw new RuntimeException("Parameter must have between one and four numbers");
    }
    List<Float> f = new ArrayList();
    for (String string : split) {
      f.add(Float.valueOf(Float.parseFloat(string)));
    }
    if (f.size() == 1) {
      return new IArea(((Float)f.get(0)).floatValue());
    }
    if (f.size() == 2) {
      return new IArea(((Float)f.get(0)).floatValue(), ((Float)f.get(1)).floatValue());
    }
    if (f.size() == 3) {
      return new IArea(((Float)f.get(0)).floatValue(), ((Float)f.get(1)).floatValue(), ((Float)f.get(2)).floatValue());
    }
    return new IArea(((Float)f.get(0)).floatValue(), ((Float)f.get(1)).floatValue(), ((Float)f.get(2)).floatValue(), ((Float)f.get(3)).floatValue());
  }
  
  public IBorder getBorder(String name)
  {
    String[] split = name.split(" ");
    if ((split.length < 1) || (split.length > 4)) {
      throw new RuntimeException("Parameter must have between one and four numbers");
    }
    List<Float> f = new ArrayList();
    for (String string : split) {
      f.add(Float.valueOf(Float.parseFloat(string)));
    }
    if (f.size() == 1) {
      return new IBorder(((Float)f.get(0)).floatValue());
    }
    if (f.size() == 2) {
      return new IBorder(((Float)f.get(0)).floatValue(), ((Float)f.get(1)).floatValue());
    }
    if (f.size() == 3) {
      return new IBorder(((Float)f.get(0)).floatValue(), ((Float)f.get(1)).floatValue(), ((Float)f.get(2)).floatValue());
    }
    return new IBorder(((Float)f.get(0)).floatValue(), ((Float)f.get(1)).floatValue(), ((Float)f.get(2)).floatValue(), ((Float)f.get(3)).floatValue());
  }
  
  public IBinnieTexture getTextureSheet(String name)
  {
    if (!this.textureSheets.containsKey(name)) {
      throw new RuntimeException("Missing GUI texture sheet for Binnie Mods: " + name);
    }
    return (IBinnieTexture)this.textureSheets.get(name);
  }
  
  public Texture getTexture(String name)
  {
    if (!this.textures.containsKey(name)) {
      throw new RuntimeException("Missing GUI texture Binnie Mods: " + name);
    }
    return (Texture)this.textures.get(name);
  }
}
