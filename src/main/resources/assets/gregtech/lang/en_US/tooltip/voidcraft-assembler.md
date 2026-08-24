The Voidcraft Assembler (part of the Eye of Harmony rework) reads a ship built out of
Voidcraft component blocks from a 5x5x10 build volume in front of its front face, validates it,
digitizes it into a single non-stackable Voidcraft item, and clears the component blocks.
{gold:{hr:87}}
{blue}How it works:
- Build a ship out of Voidcraft components inside the 5x5x10 volume in front of the machine.
- The ship must contain exactly one Controller and at least one Engine, and be at least 3 components.
- The Assembler scans the volume, validates the ship, and then digitizes it.
{blue}Validation:
- Exactly one Controller (required).
- At least one Engine (thrust).
- At least 3 components total.
- Component tier must be at or below the Assembler's circuit tier.
{gold:{hr:87}}
{blue}Circuit tier:
- Place an integrated circuit in an input bus to raise the component tier the Assembler may digitize.
- No circuit -> tier 0 (base components only). Higher circuit -> higher component tiers unlocked.
{gold:{hr:87}}
{blue}Output:
- One non-stackable Voidcraft item per ship, carrying the blueprint, stats, role set and
  hybrid efficiency in its NBT. It is consumed later by the Unstable Solar System gateway.
- If the output bus cannot accept the item the operation aborts and the component blocks are kept.
{gold:{hr:87}}
This machine is a parallel to the legacy Eye of Harmony and never modifies it.
