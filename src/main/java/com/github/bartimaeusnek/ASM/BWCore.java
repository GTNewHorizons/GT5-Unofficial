/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.ASM;

import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BWCore extends DummyModContainer {

    public static final String BWCORE_NAME = "BartWorks ASM Core";
    public static final Logger BWCORE_LOG = LogManager.getLogger(BWCORE_NAME);

    public BWCore() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.modId = "BWCore";
        metadata.name = BWCORE_NAME;
        metadata.version = "0.0.1";
        metadata.authorList.add("bartimaeusnek");
        metadata.dependants = getDependants();
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        Configuration asmconfighandler = new Configuration(event.getSuggestedConfigurationFile());
        for (int i = 0; i < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length; i++) {
            BWCoreTransformer.shouldTransform[i]=asmconfighandler.get("ASM fixes",BWCoreTransformer.DESCRIPTIONFORCONFIG[i]+" in class: "+BWCoreTransformer.CLASSESBEEINGTRANSFORMED[i],true).getBoolean(true);
        }
        if (asmconfighandler.hasChanged())
            asmconfighandler.save();
    }

    @Override
    public List<ArtifactVersion> getDependants() {
        List<ArtifactVersion> ret = new ArrayList<ArtifactVersion>();
        ret.add(new DefaultArtifactVersion("ExtraUtilities", true));
        ret.add(new DefaultArtifactVersion(BartWorksCrossmod.MOD_ID, true));
        return ret;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
