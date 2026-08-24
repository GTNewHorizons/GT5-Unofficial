The shared cargo pool of the new Voidcraft program (Eye of Harmony rework).
A 5x5x3 shell of high-power casings with input and output busses around the front face.
{gold:{hr:87}}
{blue}How it works:
- Holds a {gold}shared pool{gray} of up to 16 item slots (64 per slot) that
  persists across machine restarts and star collapses — the pool belongs to the
  bay, not to any particular star.
- When a Miner ship completes its mining leg near the bay, the Unstable Solar
  System delivers the cargo into the pool (overflow that does not fit is dropped
  at the bay instead of being lost).
- Items in the pool are automatically pushed out onto output busses whenever space
  allows, so a pipeline of busses keeps the pool drained.
- Input busses feed the pool: anything you push in is stored for the fleet.
{gold:{hr:87}}
Multiple busses and multiple ships may share one bay — cargo is pooled, not
assigned.
