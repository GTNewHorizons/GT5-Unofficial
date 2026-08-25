# Eye of Harmony Rework Proposal — Unstable Solar System Simulator

> Canonical copy of the rework proposal (source: design discussion, 2026-08). The
> implementation plan in this repository is [Voidcraft_Implementation_Plan.md](./Voidcraft_Implementation_Plan.md).

## Summary

Eye of Harmony creates temporary solar systems that collapse after a certain amount of time. New EoH modules allow insertion of dimensional spacecraft for exploitation of the solar system.

- Automation challenges around control of spacecraft, temporary infra projects, and I/O optimization to the solar system.
- Removes EoH RNG, allows specialization, provides more engaging gameplay for EoH. Introduces more upgrade paths.

## Eye of Harmony — Main Multi

The existing Eye of Harmony structure is largely unchanged. The 5 blank faces become slots for new module multiblocks that allow I/O with the Eye of Harmony.

The main multiblock now only generates an **Unstable Solar System (USS)**, consuming helium and hydrogen to generate a tiered, miniaturized solar system that collapses after a certain amount of time. The main machine no longer produces anything, and only exists to support the new modules.

## New mechanic: Autonomous Voidcraft

All interaction with the USS happens through customizable, automated vehicles, called **Voidcraft**. They are built as full-sized "multiblocks", ranging between 1x1x3 for the simplest Voidcraft that are barely an engine, a controller and a utility module, up to 5x5x10 behemoths that can mine entire planets.

Voidcraft will "spawn" inside the USS at the edge, requiring engines to move to the points of interest like planets or the star. Similarly, getting out of the USS takes time. This provides the player with an optimization choice: Do they create recoverable Voidcraft that can be reused between solar systems, but lose on throughput, or create expendable swarms of low-cost Voidcraft? Different exploitation strategies are better for different target resources, with better tech levels providing increasing options.

Voidcraft can be classified into different roles depending on what they are built to do. As they are fully custom, hybrids can also exist, but will generally not be as efficient as dedicated ships.

### Miners
Basic planetary miners, consisting of mining drone command centres and cargo bays. They mine the planets in USS for resources.

### Constructors
Voidcraft that carry solar system sized infrastructure with them, capable of constructing and deconstructing them to improve other Voidcraft. The constructions provide temporary benefits to the USS before it collapses, enabling more efficient resource extraction. Some examples include hyperlanes, which reduce the travel times inside the USS, rail accelerator stations, which allow miners to work faster by not requiring to travel back, and Dyson Spheres, which allow energy extraction from the USS.

### Starlifters
Voidcraft that mine the central Star of a USS. They provide the EoH-unique resources such as White and Black Dwarf Matter and Stellar Plasma Mixture.

### Explorers
Voidcraft that survey the USS for Spacetime Fabric ripples. These are high tier Voidcraft that allow industrial-scale extraction of Universium.

Universium cannot be directly produced during the lifetime of the USS. It is a byproduct of the central star going supernova, with trace amounts becoming available during the mid-tiers of star systems. Exploring the spacetime fabric ripples increases the yield of universium from each Supernova substantially. Not all star systems will go supernova — only supermassive stars supernova at the end of the USS lifecycle. This "natural" reaction is the first way to gain Universium.

With access to universium, constructors can construct **Star Igniters**, which forcefully cause the star to go supernova before the USS collapses. These only work on white dwarfs, and allow specializing Eye of Harmonies for Universium production by lowering the cycle time between solar systems. Combined with explorers, this provides an optimization challenge between exploring the star system or directly igniting the star.

## Modules

### Dimensional Gateway
The main entry point for Voidcraft to enter the USS. Three tiers: 1x1, 3x3 and 5x5, where the tier determines the maximum size of the Voidcraft that can enter and exit the USS. Bigger gateways allow faster operations for smaller ships.

The gateway maximum size is determined by the spacetime compression block tiers.

### Voidcraft Assembler
A stand-alone multi that is used to construct Voidcraft. The player builds the Voidcraft out of blocks inside the assembler, and the assembler "digitizes" the Voidcraft into an "item" representation while calculating the stats. Can connect directly to a Gateway and act as storage for a single Voidcraft, or send the Voidcraft into a storage bay.

Voidcraft are not actual items and cannot be taken into inventory/storage.

Automating the creation of Voidcraft is possible, but intended to be cumbersome. Using the Matter Manipulator makes it easy to copy Voidcraft (but is manual).

### Voidcraft Storage Bay
A module that stores assembler Voidcraft and sends/receives them through a dimensional gateway. Allows storing & retrieving voidcraft between USS collapsing and reopening through the gateway. Acts as the input/output hatch module for Voidcraft.

### Dimensional Extractor
Hatch interface module for EOH. Requires various infrastructure in the USS for hatches to work. Allows extracting items/fluids/energy from the USS without requiring the Voidcraft to come back.
