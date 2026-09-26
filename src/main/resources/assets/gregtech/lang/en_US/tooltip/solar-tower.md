Contributing Green Energy towards the future
Surround with 1-5 rings of {white:Solar Reflectors} to generate heat
More {white:Solar Reflectors} means more heat and more overall power output
{gray:{hr}}
Converts {gold:Cold Solar Salt} into {gold:Hot Solar Salt} at a {aqua:1:1} ratio every {aqua:{var:cycle}s}
Every {aqua:1{var:unit}} converted reduces the heat by {aqua:1} until it reaches zero
Conversion only happens if heat >= {red:{var:threshold}} and efficiency is {aqua:100%}
Excess {gold:Cold Solar Salt} is not consumed
{gray:{hr}}
Heat Efficiency = {aqua:1 - (Heat - {var:center})^{var:exp} / {var:coefficient}}, peaks at {aqua:{var:center}}
Heat/Cycle = {aqua:Solar Reflectors * Heat Efficiency * ({var:loss} + 2^(Rings - 1)) - {var:loss}}
{yellow:Heat gain is halved in rain and disabled at night}
