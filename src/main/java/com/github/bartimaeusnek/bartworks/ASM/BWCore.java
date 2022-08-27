/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.ASM;

import static com.github.bartimaeusnek.bartworks.ASM.BWCoreTransformer.shouldTransform;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("ALL")
public class BWCore extends DummyModContainer {

    public static final String BWCORE_NAME = "BartWorks ASM Core";
    public static final Logger BWCORE_LOG = LogManager.getLogger(BWCore.BWCORE_NAME);

    public BWCore() {
        super(new ModMetadata());
        ModMetadata metadata = this.getMetadata();
        metadata.modId = "BWCore";
        metadata.name = BWCore.BWCORE_NAME;
        metadata.version = "0.0.1";
        metadata.authorList.add("bartimaeusnek");
        metadata.dependants = this.getDependants();
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        shouldTransform[0] = ConfigHandler.enabledPatches[0];
        shouldTransform[1] = ConfigHandler.enabledPatches[1];
        shouldTransform[3] = ConfigHandler.enabledPatches[3];
        shouldTransform[4] = true;
        shouldTransform[5] = ConfigHandler.enabledPatches[5];
        shouldTransform[6] = ConfigHandler.enabledPatches[6];
        // shouldTransform[6] = true;
        BWCore.BWCORE_LOG.info("Extra Utilities found and ASM Patch enabled? " + shouldTransform[0]);
        BWCore.BWCORE_LOG.info("Thaumcraft found and ASM Patch enabled? " + shouldTransform[3]);
        BWCore.BWCORE_LOG.info("RWG found and ASM Patch enabled? " + shouldTransform[5]);
    }

    @Override
    public List<ArtifactVersion> getDependants() {
        List<ArtifactVersion> ret = new ArrayList<>();
        ret.add(new DefaultArtifactVersion("ExtraUtilities", true));
        ret.add(new DefaultArtifactVersion("Thaumcraft", true));
        ret.add(new DefaultArtifactVersion("miscutils", true));
        ret.add(new DefaultArtifactVersion("RWG", true));
        ret.add(new DefaultArtifactVersion("gregtech", true));
        ret.add(new DefaultArtifactVersion(MainMod.MOD_ID, true));
        ret.add(new DefaultArtifactVersion(BartWorksCrossmod.MOD_ID, true));
        return ret;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
