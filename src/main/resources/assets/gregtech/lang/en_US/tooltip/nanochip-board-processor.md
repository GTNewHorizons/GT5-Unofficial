#include:gregtech:nanochip-module
Processes your Board {yellow:CCs} by cleaning them in {aqua:'Immersion Fluid'}
{hr}
Will try to fill its internal tank with provided {aqua:'Immersion Fluids'} every second
The internal tank has a capacity of {dark_aqua:{var:capacity}{L}} and must be at least half full for the machine to work
Running recipes gradually increases fluid impurity, with bigger amounts of fluid taking longer to become impure
Set an ejection threshold in the GUI to automatically flush the fluid out to be processed, which is {aqua:{var:flush}%} by default
Supplying a redstone signal to a Nanochip Redstone Input Hatch will automatically flush the fluid tank
Manual and automatic flushing require an Output Hatch
{hr}
The Impurity percentage {aqua:(y%)} affects the module's efficiency:
{aqua:0%-{var:low}%} - {green:Duration Modifier = {var:low_base}% + ({var:low_slope} * y%)}
{aqua:{var:low}%-{var:high}%} - Default behaviour
{aqua:{var:high}%-100%} - {dark_red:Duration Modifier = 100% + (2% * y%)}
{hr}
{light_purple:{italic:Gotta get'em wet}}
