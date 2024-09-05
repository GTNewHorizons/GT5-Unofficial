package tectech.compatibility.openComputers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.compress.utils.IOUtils;

import com.github.technus.avrClone.AvrCore;
import com.github.technus.avrClone.instructions.ExecutionEvent;
import com.github.technus.avrClone.instructions.InstructionRegistry;
import com.github.technus.avrClone.instructions.exceptions.DebugEvent;
import com.github.technus.avrClone.instructions.exceptions.DelayEvent;
import com.github.technus.avrClone.memory.EepromMemory;
import com.github.technus.avrClone.memory.RemovableMemory;
import com.github.technus.avrClone.memory.program.ProgramMemory;

import li.cil.oc.Settings;
import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.Item;
import li.cil.oc.api.driver.item.Memory;
import li.cil.oc.api.machine.Architecture;
import li.cil.oc.api.machine.ExecutionResult;
import li.cil.oc.api.machine.Machine;
import li.cil.oc.api.machine.Signal;
import li.cil.oc.common.SaveHandler;
import tectech.TecTech;
import tectech.util.Converter;

@Architecture.Name("AVR 32Bit Clone")
@Architecture.NoMemoryRequirements
public class AvrArchitecture implements Architecture {

    private final Machine machine;
    private AvrCore core;
    private boolean debugRun;
    private int delay;
    private int[] tempData;
    private int memSize;

    public AvrArchitecture(Machine machine) {
        this.machine = machine;
    }

    @Override
    public boolean isInitialized() {
        return core != null && core.checkValid();
    }

    @Override
    public boolean recomputeMemory(Iterable<ItemStack> components) {
        computeMemory(components);
        return true;
    }

    private void computeMemory(Iterable<ItemStack> components) {
        int memory = 0;
        for (ItemStack component : components) {
            Item driver = Driver.driverFor(component);
            if (driver instanceof Memory memoryDriver) {
                memory += memoryDriver.amount(component) * 256; // in integers
            } // else if (driver instanceof DriverEEPROM$) {

            // }
        }
        memory = Math.min(
            Math.max(memory, 0),
            Settings.get()
                .maxTotalRam());
        if (memory != memSize) {}
    }

    @Override
    public boolean initialize() {
        core = new AvrCore();

        computeMemory(
            this.machine.host()
                .internalComponents());

        if (isInitialized()) {
            machine.beep(".");
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        core = null;
        tempData = null;
        delay = 0;
    }

    @Override
    public void runSynchronized() {
        core.cycle();
    }

    @Override
    public ExecutionResult runThreaded(boolean isSynchronizedReturn) {
        if (core.awoken) {
            delay = 0;
            for (int load = 0; load < 512;) {
                load += core.getInstruction()
                    .getCost(core);
                ExecutionEvent executionEvent = core.cpuCycleForce();
                if (executionEvent != null) {
                    if (executionEvent.throwable instanceof DelayEvent) {
                        delay = executionEvent.data[0];
                        break;
                    } else if (executionEvent.throwable instanceof DebugEvent) {
                        if (debugRun) {
                            // aBaseMetaTileEntity.setActive(false);
                            break;
                        }
                    }
                }
            }
        } else if (delay > 0) {
            delay--;
            if (delay == 0) {
                core.awoken = true;
            }
        }
        return null;
    }

    @Override
    public void onSignal() {
        Signal signal = machine.popSignal();

        core.interruptsHandle();
    }

    @Override
    public void onConnect() {
        // init network components, in case init was called from load logic (pre first tick?)
    }

    @Override
    public void load(NBTTagCompound avr) {
        debugRun = avr.getBoolean("debugRun");
        delay = avr.getInteger("delay");
        core.active = avr.getBoolean("active");
        core.awoken = (avr.getBoolean("awoken"));
        core.programCounter = avr.getInteger("programCounter");
        InstructionRegistry registry = InstructionRegistry.REGISTRIES.get(avr.getString("instructionRegistry"));
        if (registry != null) {
            byte[] instructions = SaveHandler.load(
                avr,
                this.machine.node()
                    .address() + "_instructionsMemory");
            byte[] param0 = SaveHandler.load(
                avr,
                this.machine.node()
                    .address() + "_param0Memory");
            byte[] param1 = SaveHandler.load(
                avr,
                this.machine.node()
                    .address() + "_param1Memory");
            if (instructions != null && param0 != null
                && param1 != null
                && instructions.length > 0
                && param0.length > 0
                && param1.length > 0) {
                int[] instr = null, par0 = null, par1 = null;
                try {
                    GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(instructions));
                    instr = Converter.readInts(IOUtils.toByteArray(gzis));
                    IOUtils.closeQuietly(gzis);
                } catch (IOException e) {
                    TecTech.LOGGER.error("Failed to decompress instructions memory from disk.");
                    e.printStackTrace();
                }
                try {
                    GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(param0));
                    par0 = Converter.readInts(IOUtils.toByteArray(gzis));
                    IOUtils.closeQuietly(gzis);
                } catch (IOException e) {
                    TecTech.LOGGER.error("Failed to decompress param0 memory from disk.");
                    e.printStackTrace();
                }
                try {
                    GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(param1));
                    par1 = Converter.readInts(IOUtils.toByteArray(gzis));
                    IOUtils.closeQuietly(gzis);
                } catch (IOException e) {
                    TecTech.LOGGER.error("Failed to decompress param1 memory from disk.");
                    e.printStackTrace();
                }
                if (instr != null && par0 != null
                    && par1 != null
                    && instr.length == par0.length
                    && instr.length == par1.length) {
                    core.setProgramMemory(new ProgramMemory(registry, avr.getBoolean("immersive"), instr, par0, par1));
                }
            }
        }
        if (avr.hasKey("eepromSize")) {
            core.restoreEepromDefinition(EepromMemory.make(avr.getInteger("eepromSize")));
        }
        byte[] data = SaveHandler.load(
            avr,
            this.machine.node()
                .address() + "_dataMemory");
        if (data != null && data.length > 0) {
            try {
                GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(data));
                tempData = Converter.readInts(IOUtils.toByteArray(gzis));
                IOUtils.closeQuietly(gzis);
            } catch (IOException e) {
                TecTech.LOGGER.error("Failed to decompress data memory from disk.");
                e.printStackTrace();
            }
        }
        core.checkValid();
    }

    @Override
    public void save(NBTTagCompound avr) {
        avr.setBoolean("debugRun", debugRun);
        avr.setInteger("delay", delay);
        avr.setBoolean("active", core.active);
        avr.setBoolean("awoken", core.awoken);
        avr.setInteger("programCounter", core.programCounter);
        ProgramMemory programMemory = core.getProgramMemory();
        if (programMemory != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzos = new GZIPOutputStream(baos);
                gzos.write(Converter.writeInts(programMemory.instructions));
                gzos.close();
                SaveHandler.scheduleSave(
                    machine.host(),
                    avr,
                    machine.node()
                        .address() + "_instructionsMemory",
                    baos.toByteArray());
            } catch (IOException e) {
                TecTech.LOGGER.error("Failed to compress instructions memory to disk");
                e.printStackTrace();
            }
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzos = new GZIPOutputStream(baos);
                gzos.write(Converter.writeInts(programMemory.param0));
                gzos.close();
                SaveHandler.scheduleSave(
                    machine.host(),
                    avr,
                    machine.node()
                        .address() + "_param0Memory",
                    baos.toByteArray());
            } catch (IOException e) {
                TecTech.LOGGER.error("Failed to compress param0 memory to disk");
                e.printStackTrace();
            }
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzos = new GZIPOutputStream(baos);
                gzos.write(Converter.writeInts(programMemory.param1));
                gzos.close();
                SaveHandler.scheduleSave(
                    machine.host(),
                    avr,
                    machine.node()
                        .address() + "_param1Memory",
                    baos.toByteArray());
            } catch (IOException e) {
                TecTech.LOGGER.error("Failed to compress param1 memory to disk");
                e.printStackTrace();
            }
            avr.setBoolean("immersive", programMemory.immersiveOperands);
            avr.setString("instructionRegistry", programMemory.registry.toString());
        }
        RemovableMemory<EepromMemory> eeprom = core.getEepromMemory();
        if (eeprom != null) {
            avr.setInteger(
                "eepromSize",
                eeprom.getDefinition()
                    .getSize());
        }
        if (core.dataMemory != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzos = new GZIPOutputStream(baos);
                gzos.write(Converter.writeInts(core.dataMemory));
                gzos.close();
                SaveHandler.scheduleSave(
                    machine.host(),
                    avr,
                    machine.node()
                        .address() + "_dataMemory",
                    baos.toByteArray());
            } catch (IOException e) {
                TecTech.LOGGER.error("Failed to compress data memory to disk");
                e.printStackTrace();
            }
        }
    }
}
