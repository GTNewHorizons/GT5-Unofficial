The Voidbase Assembler reads a station built out of Voidcraft component blocks from a
15x15x15 build volume in front of its front face, validates it with the BASE rules (the ship rules minus
the thruster audit — a Voidbase is immobile), digitizes it into a single non-stackable Voidbase blueprint
item, and clears the component blocks.
{gold:{hr:87}}
{blue}How it works:
- Build a station out of Voidcraft components inside the 15x15x15 volume in front of the machine.
- The station must contain exactly one Controller, at least one Frame, and be at least 3 components.
- No engines or thruster nozzles are required — bases do not move. Nozzles are inert if placed.
- The Assembler scans the volume, validates the station, and then digitizes it.
{blue}Validation (base rules):
- Exactly one Controller (required).
- At least one Frame.
- At least 3 components total.
- Cover-only catalog entries (everything except Controller and Frame) must be placed as covers on hull faces.
- Component tier must be at or below the Assembler's circuit tier.
{gold:{hr:87}}
{blue}Circuit tier:
- Place an integrated circuit in an input bus to raise the component tier the Assembler may digitize.
- No circuit -> tier 0 (base components only). Higher circuit -> higher component tiers unlocked.
{gold:{hr:87}}
{blue}Output:
- One non-stackable, REUSABLE Voidbase blueprint item per station, carrying the full 15x15x15 blueprint,
  the derived stats and the controller's stored program (if any) in its NBT.
- The blueprint is NOT consumed: the gateway copies it into the Constructor's payload at launch, and the
  item stays in the gateway's blueprint slot for the next launch. Several Constructors can build the same
  base from the same blueprint — the first one creates the construction site, the rest fill it.
- If the output bus cannot accept the item the operation aborts and the component blocks are kept.
{gold:{hr:87}}
Bases are anchored to the star, a planet, or a ripple point and sit at that target's ship hover point,
which follows the target (planet orbits). A base loses integrity at 1 per second; at zero it is
decommissioned. Bases are powered by their own energy buffer and Solar Panel covers.
