## Introducing the Advanced Circuit Assembly Line

As progression advances in GTNH, crafting high-tier circuits often becomes more about scale. In my opinion, the game feels very solid from Dirt tier to late UEV: it’s smooth, enjoyable, and well balanced, with plenty to do and multiple progression paths.

Around the DTPF stage, some rough edges start to appear. One example is the gap between Awak Coils and Eternal Coils, and similarly between QFT1 and QFT4. After that point, progression becomes more enjoyable again with access to the gorge, BHC, and larger-scale production.

With this project, I want to help smooth out that transition. The goal is to give players more options so they spend less time waiting and more time playing. At the endgame, the main challenge often becomes managing idle time rather than gathering resources or generating energy.

One area I wanted to focus on is circuit crafting. Many players have been waiting for the NAC, and while it may arrive someday, there’s currently room for an additional approach. This project aims to provide that option in the meantime.

This is the first release of the ACAL: a simpler and faster-to-develop alternative to large-scale CAL setups, and something new to experiment with while waiting for the NAC.

## What is the ACAL ?

The ACAL is a giant CAL, 15 blocks width x 15 blocks height x 47 blocks deep. Structure below:

![ACAL OVERVIEW](https://raw.githubusercontent.com/mamiemru/GT5-Unofficial/refs/heads/Advanced-Circuit-Assembly-Line/overview/Acaloverview2.png)

![ACAL overview](https://raw.githubusercontent.com/mamiemru/GT5-Unofficial/refs/heads/Advanced-Circuit-Assembly-Line/overview/ACALoverview1.png)

This machine comes with two settings:

- MaxParallel: Controls how much parallel processing is allowed (interacts with batch mode).
- Expected Duration: Sets the time required to craft a batch of circuits.

![Acal inside 1](https://raw.githubusercontent.com/mamiemru/GT5-Unofficial/refs/heads/Advanced-Circuit-Assembly-Line/overview/ACALinside1.png)

By default, MaxParallel (P) is set to 64 and Expected Duration (D) is set to 70 seconds.

Behind the scenes, it uses the OverclockCalculator algorithm, with:

- The same settings as the TPM for parallel (DAH)
- A ×20 factor applied to the expected duration to match what is displayed in the ACAL terminal

These settings can be adjusted depending on how much energy you want to allocate.

![Acal inside 2](https://raw.githubusercontent.com/mamiemru/GT5-Unofficial/refs/heads/Advanced-Circuit-Assembly-Line/overview/ACALinside2.png)

The energy shown below represents how much energy is pulled from the wireless network when the craft starts.
This energy is only pulled once, at the beginning of the craft.

## When is the ACAL on the progression ?

The multi is unlockable in UIV. the requirements are:
- Space assembler MK1
- some PICO circuits
- Some UIV components
- Spacetime

The structure require 336 Eternal coils, 333 Infinit cooled casings, the rest is cheap.

![ACAL RECIPE](https://raw.githubusercontent.com/mamiemru/GT5-Unofficial/refs/heads/Advanced-Circuit-Assembly-Line/overview/ACALrecipe1.png)

## What can i do with the ACAL?

As you can see below, for now (2026-1-1), the only use of the ACAL is to craft:

- Any circuits from ULV to ZPM
- Exotic line circuits

I'm working to add Cosmic line and Temporal line, but not for now.

If you are interested about math comparisons, please see this:
https://github.com/mamiemru/GT5-Unofficial/blob/Advanced-Circuit-Assembly-Line/maths.md

## Why did I choose these recipes?

![Any circuit recipe](https://raw.githubusercontent.com/mamiemru/GT5-Unofficial/refs/heads/Advanced-Circuit-Assembly-Line/overview/ACALanycircuits.png)

Above is the current mapping of Any Circuits. I chose to use Any Circuits to add a new layer to circuit crafting. At this stage of the game, all the circuits shown above should be easy to craft.

By using Innovative Circuit Boards (the purple item), you unlock circuit crafting beyond DTPF materials.

For those curious about the numbers: the amount of circuits was calculated (and rounded up) based on how many circuit boards of a given tier you are able to craft in one batch in UIV using the PCB Factory.

As you can see, you don’t need as many materials to craft lower-tier circuits. However, you do require more raw materials, such as bolts or fine wires. A major gain comes from the use of wafers, which removes the cutting recipe entirely.

![ACAL recipe exotic](https://raw.githubusercontent.com/mamiemru/GT5-Unofficial/refs/heads/Advanced-Circuit-Assembly-Line/overview/ACALrecipeExotic.png)

Here we have the Exotic line. It’s a pleasure to see these magenta circuits appear in the crafting tree.

As shown, almost all materials used here are new or late-game materials. The idea is to avoid lower-tier ingredients. As a result, these circuits have a relatively small crafting tree, usually requiring only 2–5 steps.

You can also see that the cutting machine step is skipped entirely, as we directly use QPIC wafers (Beamline enjoyers will be happy to know this is not a bottleneck).

These circuits are gated behind Metastable Oganesson.

The Exotic Mainframe (the PICO equivalent) requires Quark–Gluon Plasma, meaning you still need to progress through Quantum circuits before unlocking these shorter recipes.

You can also see new SMDs introduced here. There are five Innovative SMDs, all using late-game materials. In addition, the Innovative Universal SMD requires all Innovative SMDs along with Lossless Phonon Transfer Medium.

For the new components, here is the recipe tree:

![Compoennts](https://raw.githubusercontent.com/mamiemru/GT5-Unofficial/refs/heads/Advanced-Circuit-Assembly-Line/overview/Components.png)

All ISMD are assembler recipe gated to UIV energy.
The last foil is Superconductor base UMV foil and the cable is UIV SU.
The Universal ISMD (the first one) change color.

The Exotic Super CPU is a space assembler recipe UHV.

The 2 last recipe are PCB one.

A Better recipe for Optical CPU containment housing:

![Compoennts](https://raw.githubusercontent.com/mamiemru/GT5-Unofficial/refs/heads/Advanced-Circuit-Assembly-Line/overview/occh.png)


If you’re interested, here is the development history of the project:

[link to changelog.md] **(Will come Soon)**


## My thoughts

I’ve really enjoyed developing the ACAL. For me, it’s a beautiful machine, and I love its spatial design. I have a run in parallel (2025-01-03, pre-EOH) to test the balance, and after several iterations, I can say this multiblock is well implemented. You won’t have to worry about low-tier circuits anymore. The ACAL is not a bottleneck as long as you are willing to provide enough energy.

At the moment:

- I only use 3 CALs in normal mode and 8 in OPTICAL mode.
- Cutting recipes are not a bottleneck either; I only have 16 cutting machines in 8 UIV.
- The Space Elevator is used more heavily, but I already have some plans for this bad boy in the future. My setup ranges from 1 to 12 Space Assembler MK2s.
- The Prass is not a bottleneck, except for superconducting wire, but we are used to that.
- Beamlines are running strong with lasers and UMV 64 wireless.
- For Compact Fusion MK5 used for MTO, I started with 1 and now have 8.
- DTPF materials were an issue a few weeks ago; some adjustments to Exotic recipes have been made to reduce this.

The ACAL only becomes a bottleneck if you don’t have enough energy to supply it.

A list of pros and cons can be found in maths.md.

I’m open to new textures (the controller texture is a joke) if someone wants to contribute.

## Future plans

Yes, the goal of this first release is to check whether the multiblock is well balanced. I already have some plans for the future.

#### Modules

Some modules will include puzzles, others will not.

- Array Warping System: Allows crafting up to four different circuits at the same time
- Borg Cooling Drones: Puzzle mandatory; allows faster crafting — this machine will comply
- Perfect Alignment Sensor: Allows perfect overclock

And last, but not least:

- Tiers Annihilation Matrix: Uses Stargate items and allows treating high-tier recipes as low-tier recipes

#### more Any ?

Yes, with new machines, challenges, UV UHV UEV UIV Any circuit will be available.

#### Cosmic circuit line and Temporal circuit line

I have plans for these circuits, but before that, I want to explore new ideas on how to integrate them. There is a French developer named Yamikami_sama on Discord, and I’m very interested in his ideas for an out-of-bounds endgame multiblock. I would like to build on his work later. I don’t want to spoil anything — he’s the one leading that concept.
