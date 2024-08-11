# Fuel Boilers README (ya don't say)

### What is this though, actually?
This file is intended to be temporary, for planning out fuel boiler mechanics.
If you're reading this, you're probably reviewing the PR or checking git history.
Docs remaining outside the code permanently are undesirable, as they tend to drift out of
date - if this is still here and nobody is working on these, there's a problem.

## Concept
Fuel boilers consume fuel directly to boil water and make steam. In the early game,
this is a good means to feed Steam multis, which can draw up to 10 2/3 A worth of steam each,
without setting up massive low-tier production like spamming solars or charcoal/creosote
production.

However, when players hit EV and get multiblock power, this effectively converts every
thermal powergen (oil and gas) into Steam with just one extra step. I'm not sure if this is a
good idea without rebalancing all three methods together. For now, it stops at HV to prevent
people from trying to scale it up for EV powergen. Larger boilers will wait until after
2.7.0, unless the powergen rebalance is changed to include this.

### Tiering
Tiering is limited by how hot the fuel gets (which in turn is based on it's EU/L). Higher
tier structures can take more heat, allowing them to transfer energy faster and thus boil
water faster without expanding the surface area (i.e. adding more boilers). Each tier also
double fuel consumption without changing fuels, but you only get 4x if you max out heat too.
The existing thermal powergen tierS go:
- LV: Methane? (104 EU/L) and Light Fuel (305 EU/L), ~ 193% oil advantage
- MV: B E N Z E N E (360 EU/L) and Diesel (480 EU/L), 33% oil adv.
- HV: ditto and Cetane-Boosted Diesel (1 kEU/L), 177% oil adv.
- EV: Nitrobenzene (1.6 kEU/L) and High-Octane Gasoline (2.5 kEU/L), 56.25% oil adv.

This heavily discourages not switching to Nitrobenzene in EV, and gives oil a large advantage
generally. Prior to the 2.7.0 changes that might be a good idea, but I'm not so sure
that's true afterward. Solid fuels are not considered yet, I've no idea how they go.

### Heat
Production of steam is based on heat - it starts at room temp and slowly heats up
to 100 + EU/L / 5, or 120C - 600C (Fuels below 100 EU/L won't be accepted, and HOG is the
densest combustion fuel I know of). Modern COGAS plants run gas turbines at 900-1400C, but
only heat steam to 655C due to corrosion problems, and I'm pretty sure NH still uses
"realistic" alloys in IV. As the boiler warms up, fuel consumption is constant but steam
production is reduced - at 125C, an LV boiler is making half the steam. Fuel efficiency also
decreases by 5% per tier, so there's a 20% efficiency drop at EV. If a fuel is too
high-tier for the boiler, it gets capped at the boiler's max heat rating, but can still be
used.

Now tiering is as follows:
- LV: Methane? (120.8 C) and Light Fuel (161 C), ~ 193% oil advantage
- MV: B E N Z E N E (172 C) and Diesel (196 C), 33% oil adv.
- HV: ditto and Cetane-Boosted Diesel (300 C), 177% oil adv.
- EV: Nitrobenzene (420 C) and High-Octane Gasoline (600 C), 56.25% oil adv.

Steam generation starts after 100C and scales exponentially after that - the first 50C
doubles output, the next 100C doubles it again, etc. This means that the output is
(heat / 25) * 32 EU/t * 2^tier. For example, an LV boiler running on light fuel is
50/25*32*2^1 = 128 EU/t. A HOG EV boiler is 500/25*32*2^4 = 10240 EU/t

Given these numbers, it seems that heat capacity should be:
- LV: 150C, throttles light fuel to 150C for a 140% oil adv.
- MV: 200C, +50C
- HV: 300C, +100C
- EV: 600C, +300C (actually a +150% instead of +100%, to allow oil players to go HOG-wild)

Small boilers would then generate:
- LV: 125.6 - 128 EU/t, ~4A (because it won't operate with <120C fuels)
- MV: 256 - 512 EU/t, 2-4A
- HV: 1024 - 2048 EU/t, 2-4A
- EV: 4096 - 10240 EU/t, 2-5A

Large boilers theoretically should generate 2-4x as much per boiler chamber, but would
upset balance too much, for now. ~~Ditto for EV small boilers~~ they're on the table now,
with the 20% malus. Assuming, of course, that it's still not as good (or at least not much
better) than directly burning your fuel.
