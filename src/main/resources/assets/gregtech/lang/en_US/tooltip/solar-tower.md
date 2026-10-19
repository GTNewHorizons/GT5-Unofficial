Contributing Green Energy towards the future
Surround with 1-5 rings of {color_name:Solar Reflectors} to generate heat
More Solar Reflectors means more heat gain and more overall coolant throughput
Maximum {color_hot:100,000 heat} but there is no risk or danger in reaching that
{hr}
Converts {color_cold:Cold Solar Salt} into {color_red:Hot Solar Salt} at a 1:1 ratio every {var:cycle}s
Every 1{var:unit} converted reduces the heat by 1 until it reaches zero
Excess Cold Solar Salt is not consumed
{hr}
Conversion only happens if {white:heat >=} {red:{var:threshold}} and {white:efficiency =} {aqua:100%}
Efficiency starts at {aqua:0%} and increases by {aqua:+1%} every 10s
{hr}
{color_hot:Heat Efficiency}{color_formula: = 1 - (Heat - {yellow:{var:center}})^{var:exp} / {var:coefficient}}, peaks at {yellow:{var:center}}
{color_hot:Heat/Cycle}{color_formula: = Solar Reflectors * {color_hot:Heat Efficiency} * ({var:loss} + 2^(Rings - 1)) - {var:loss}}
{yellow:Heat gain is halved in rain and disabled at night}
