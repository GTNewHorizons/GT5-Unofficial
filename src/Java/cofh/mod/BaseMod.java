package cofh.mod;

import cofh.mod.updater.IUpdatableMod;
import cofh.mod.updater.ModRange;
import cofh.mod.updater.ModVersion;
import com.google.common.base.Strings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICrashCallable;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringTranslate;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.spi.AbstractLogger;

public abstract class BaseMod implements IUpdatableMod {

	protected File _configFolder;
	protected final String _modid;
	protected final Logger _log;

	protected BaseMod(Logger log) {

		String name = getModId();
		_modid = name.toLowerCase(Locale.US);
		_log = log;
		init();
	}

	protected BaseMod() {

		String name = getModId();
		_modid = name.toLowerCase(Locale.US);
		_log = LogManager.getLogger(name);
		init();
	}

	private void init() {

		ModContainer container = cpw.mods.fml.common.Loader.instance().activeModContainer();
		if (container.getSource().isDirectory()) {
			FMLCommonHandler.instance().registerCrashCallable(new CrashCallable("Loaded from a directory"));
		} else {
			try {
				JarFile jar = new JarFile(container.getSource());
				ZipEntry file = jar.getEntry("vers.prop");
				if (file != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(jar.getInputStream(file)));
					String data = reader.readLine();
					FMLCommonHandler.instance().registerCrashCallable(new CrashCallable(data));
				} else {
					FMLCommonHandler.instance().registerCrashCallable(new CrashCallable("Lacking version information."));
				}
				jar.close();
			} catch (IOException e) {
				FMLCommonHandler.instance().registerCrashCallable(new CrashCallable("Error reading version information." + e.getMessage()));
			}
		}
	}

	@NetworkCheckHandler
	public final boolean networkCheck(Map<String, String> remoteVersions, Side side) throws InvalidVersionSpecificationException {

		if (!requiresRemoteFrom(side)) {
			return true;
		}
		Mod mod = getClass().getAnnotation(Mod.class);
		String _modid = mod.modid();
		if (!remoteVersions.containsKey(_modid)) {
			return false;
		}
		String remotes = mod.acceptableRemoteVersions();
		if (!"*".equals(remotes)) {

			String remote = remoteVersions.get(_modid);
			if (Strings.isNullOrEmpty(remotes)) {
				return getModVersion().equalsIgnoreCase(remote);
			}
			return ModRange.createFromVersionSpec(_modid, remotes).containsVersion(new ModVersion(_modid, remote));
		}
		return true;
	}

	protected boolean requiresRemoteFrom(Side side) {

		return true;
	}

	protected String getConfigBaseFolder() {

		String base = getClass().getPackage().getName();
		int i = base.indexOf('.');
		if (i >= 0) {
			return base.substring(0, i);
		}
		return "";
	}

	protected void setConfigFolderBase(File folder) {

		_configFolder = new File(folder, getConfigBaseFolder() + "/" + _modid + "/");
	}

	protected File getConfig(String name) {

		return new File(_configFolder, name + ".cfg");
	}

	protected File getClientConfig() {

		return getConfig("client");
	}

	protected File getCommonConfig() {

		return getConfig("common");
	}

	protected String getAssetDir() {

		return _modid;
	}

	@Override
	public Logger getLogger() {

		return _log;
	}

	private void loadLanguageFile(Properties lang, InputStream stream) throws Throwable {

		InputStreamReader is = new InputStreamReader(stream, "UTF-8");

		Properties langPack = new Properties();
		langPack.load(is);

		lang.putAll(langPack);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadLanguageFile(String lang, Properties langPack) {

		HashMap<String, String> parsedLangFile = new HashMap<String, String>();
		parsedLangFile.putAll((Map) langPack); // lovely casting hack

		LanguageRegistry.instance().injectLanguage(lang.intern(), parsedLangFile);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void loadLang() {

		if (FMLLaunchHandler.side() == Side.CLIENT) {
			try {
				loadClientLang();
				return;
			} catch (Throwable t) {
				_log.error(AbstractLogger.CATCHING_MARKER, "???", t);
			}
		}

		String path = "assets/" + getAssetDir() + "/language/";
		String lang = "en_US";
		try (InputStream is = Loader.getResource(path + lang + ".lang", null).openStream();) {
			Properties langPack = new Properties();
			loadLanguageFile(langPack, is);

			StringTranslate i = ObfuscationReflectionHelper.getPrivateValue(StringTranslate.class, null, "instance", "field_74817_a");
			Map m = ObfuscationReflectionHelper.getPrivateValue(StringTranslate.class, i, "field_74816_c", "languageList");
			m.putAll(langPack);
		} catch (Throwable t) {
			_log.catching(Level.INFO, t);
		}
	}

	@SideOnly(Side.CLIENT)
	private void loadClientLang() {

		IReloadableResourceManager manager = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
		manager.registerReloadListener(new LangManager(manager));
	}

	@SideOnly(Side.CLIENT)
	private class LangManager implements IResourceManagerReloadListener {

		private final String _path;

		public LangManager(IResourceManager manager) {

			_path = getAssetDir() + ":language/";
			onResourceManagerReload(manager);
		}

		@Override
		public void onResourceManagerReload(IResourceManager manager) {

			String l = null;
			try {
				l = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
			} catch (Throwable t) {
				_log.catching(Level.WARN, t);
			}

			for (String lang : Arrays.asList("en_US", l)) {
				if (lang != null) {
					Properties langPack = new Properties();
					try {
						List<IResource> files = manager.getAllResources(new ResourceLocation(_path + lang + ".lang"));
						for (IResource file : files) {
							if (file.getInputStream() == null) {
								_log.warn("A resource pack defines an entry for language '" + lang + "' but the InputStream is null.");
								continue;
							}
							try {
								loadLanguageFile(langPack, file.getInputStream());
							} catch (Throwable t) {
								_log.warn(AbstractLogger.CATCHING_MARKER, "A resource pack has a file for language '" + lang + "' but the file is invalid.", t);
							}
						}
					} catch (Throwable t) {
						_log.info(AbstractLogger.CATCHING_MARKER, "No language data for '" + lang + "'", t);
					}
					loadLanguageFile(lang, langPack);
				}
			}

			Minecraft.getMinecraft().getLanguageManager().onResourceManagerReload(manager);
		}
	}

	private class CrashCallable implements ICrashCallable {

		private final String data;

		private CrashCallable(String data) {

			this.data = data;
		}

		@Override
		public String call() throws Exception {

			return data;
		}

		@Override
		public String getLabel() {

			return getModId();
		}

	}

}
