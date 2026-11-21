Горячие жидкости: {color_hot:{fluid:lava}}, {color_hot:{fluid:ic2hotcoolant}}, {color_hot:{fluid:molten.solarsalthot}}
На выходе: {color_cold:{fluid:ic2pahoehoelava}}, {color_cold:{fluid:ic2coolant}}, {color_cold:{fluid:molten.solarsaltcold}}
Превращает {color_name:дистиллированную воду} в {color_steam:пар}
Если подача горячей жидкости достигает {color_note:порога ПГ}, выдаёт вдвое меньше {color_steam:перегретого пара}
{color_danger:Взрывается через {var:explode_seconds} секунд без дистиллированной воды}
{hr}
{color_hot:{fluid:lava}}: порог ПГ {color_hot:{var:lava_threshold} {L}/с}, макс. ввод {color_hot:{var:lava_max_input} {L}/с}, макс. выход {color_steam:{var:lava_max_output} {L}/т ПГ пара}
{color_hot:{fluid:ic2hotcoolant}}: порог ПГ {color_hot:{var:coolant_threshold} {L}/с}, макс. ввод {color_hot:{var:coolant_max_input} {L}/с}, макс. выход {color_steam:{var:coolant_max_output} {L}/т ПГ пара}
{color_hot:{fluid:molten.solarsalthot}}: порог ПГ {color_hot:{var:salt_threshold} {L}/с}, макс. ввод {color_hot:{var:salt_max_input} {L}/с}, макс. выход {color_steam:{var:salt_max_output} {L}/т ПГ пара}
{hr}
{color_name:Запрограммированная схема} в контроллере снижает порог ПГ на {color_amount:{var:threshold_reduction}%} и выработку пара на {color_loss:{var:steam_penalty}%} за каждое значение выше {var:min_circuit}
