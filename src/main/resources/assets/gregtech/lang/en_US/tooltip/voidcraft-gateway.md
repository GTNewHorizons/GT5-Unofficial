The launch pad of the new Voidcraft program (Eye of Harmony rework). A 3x3x3 shell
of high-power casings with the ship slot at the center of the front face.
{gold:{hr:87}}
{blue}How it works:
- Right-click the gateway holding a {gold}Voidcraft{gray} ship item (digitized by a
  Voidcraft Assembler) to dock it in the front slot.
- While a ship is docked, the gateway renders it in place as a hologram above the
  structure.
- The gateway then searches up to 32 blocks for the nearest {gold}ignited{gray}
  Unstable Solar System and a {gold}Voidcraft Storage Bay{gray}, and launches the
  ship: outbound flight, a mining leg at the star, and the return flight.
{blue}Launch requirements:
- The ship must carry the {gold}Miner{gray} role (set at digitization time).
- A nearby Unstable Solar System must be ignited, and at most one ship may be in
  flight per system.
- A nearby Voidcraft Storage Bay receives the mined cargo.
{gold:{hr:87}}
{blue}After the mission:
- A ship's {gold}integrity{gray} is its {gold}time limit{gray}: it drops by 1 every
  second while the ship is in the system, starting at the ship's maximum.
- A ship that finishes before its time runs out comes back into the docked slot
  with its integrity restored, ready to fly again.
- A ship that runs out of time is lost with its cargo.
- Mined cargo lands in the Storage Bay's shared pool; pull it off with an output bus.
{gold:{hr:87}}
If the star burns out while a ship is away, the ship is lost with its cargo —
the system cannot catch what is already in flight.
