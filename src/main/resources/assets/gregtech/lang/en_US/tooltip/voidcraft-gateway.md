The launch pad of the new Voidcraft program (Eye of Harmony rework). A 3x3x3 shell
of high-power casings with the controller at the center of the front face, ringed
by the hatch perimeter.
{gold:{hr:87}}
{blue}How it works:
- Feed a {gold}Voidcraft{gray} ship item (digitized by a Voidcraft Assembler) into
  the {gold}input buses{gray} — a launch consumes one ship from them.
- Right-click the gateway holding a {gold}Voidbase blueprint{gray} (from the Voidbase
  Assembler) to load it into the blueprint slot. The blueprint is KEPT — every
  Constructor launch copies its data into the ship.
- The gateway then searches up to 32 blocks for the nearest {gold}ignited{gray}
  Unstable Solar System and a {gold}Voidcraft Storage Bay{gray}, and launches the
  ship: outbound flight, a work leg at the target, and the return flight.
{blue}Launch requirements:
- The ship must carry a mission role ({gold}Miner{gray}, {gold}Starlifter{gray},
  {gold}Explorer{gray} or {gold}Constructor{gray}, set at digitization time).
- At least one {gold}input bus{gray} (the ship source) and one {gold}output
  bus{gray} (the return path) on the front-face ring.
- A nearby Unstable Solar System must be ignited, with at least one free ship slot.
- A nearby Voidcraft Storage Bay receives the mined cargo.
{blue}Voidbase construction (the Constructor role):
- A Constructor launch needs a blueprint in the blueprint slot. The ship leaves
  with a copy of the blueprint plus a partial parts loadout, pulled from the input
  buses (component blocks and covers) and capped at the construction site
  remaining at the target anchor.
- In flight the first Constructor creates the {gold}construction site{gray} (a gray
  wireframe at the anchor); further Constructors fill it. When it is complete the
  Voidbase stands there — one base per anchor.
{gold:{hr:87}}
{blue}After the mission:
- A ship's {gold}integrity{gray} is its {gold}time limit{gray}: it drops by 1 every
  second while the ship is in the system, starting at the ship's maximum.
- A ship that finishes before its time runs out comes back into the gateway's
  {gold}output bus{gray} with its integrity restored, ready to fly again.
- A ship that runs out of time is lost with its cargo.
- Mined cargo lands in the Storage Bay's shared pool; pull it off with an output
  bus.
{gold:{hr:87}}
If the star burns out while a ship is away, the ship is lost with its cargo —
the system cannot catch what is already in flight. Voidbases and their
construction sites are lost with the star as well.