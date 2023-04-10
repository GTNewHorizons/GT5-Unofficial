/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.api.helpers;

import static kubatech.api.utils.ModUtils.isClientSided;

import java.io.IOException;

import kubatech.Tags;
import kubatech.api.LoaderReference;
import alexiil.mods.load.ProgressDisplayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ProgressManager;

@SuppressWarnings("deprecation")
public class ProgressBarWrapper {

    ProgressManager.ProgressBar internalFMLBar;
    boolean isFMLBar;
    String name;
    int maxSteps;
    int steps = 0;

    public ProgressBarWrapper(String name, int steps) {
        if (!isClientSided) return;
        maxSteps = steps;
        this.name = name;
        if (!LoaderReference.BetterLoadingScreen) {
            internalFMLBar = ProgressManager.push(name, steps);
            isFMLBar = true;
            return;
        }
        isFMLBar = false;
    }

    public void step(String message) {
        if (!isClientSided) return;
        if (isFMLBar) internalFMLBar.step(message);
        else {
            steps++;
            try {
                ProgressDisplayer
                    .displayProgress(Tags.MODNAME + ": " + name + " -> " + message, (float) steps / (float) maxSteps);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Prevent game freeze
            FMLCommonHandler.instance()
                .processWindowMessages();
        }
    }

    public void end() {
        if (!isClientSided) return;
        if (isFMLBar) ProgressManager.pop(internalFMLBar);
    }
}
