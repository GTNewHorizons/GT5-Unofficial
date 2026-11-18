Эквивалентно 32 большим теплообменникам
Вводите {color_hot:лаву}, {color_hot:горячий хладагент} или {color_hot:раскалённую солнечную соль}
Выдаёт {color_cold:базальтовую лаву}, {color_cold:IC2 хладагент} или {color_cold:охлаждённую солнечную соль}
Преобразует дистиллированную воду в {color_steam:пар} или {color_steam:ПГ пар} в процессе
Выдаёт {color_steam:ПГ пар}, если входной поток горячей жидкости выше определённого {light_purple:порога}
Взрывается мгновенно, если в нём заканчивается дистиллированная вода
{hr}
{color_hot:Лава} | порог ПГ {light_purple:{var:lava_threshold} {L}/с} | макс. ввод {color_hot:{var:lava_max_input} {L}/с} | макс. выход {color_steam:{var:lava_max_output} {L}/т ПГ пара}
{color_hot:Горячий хладагент} | порог ПГ {light_purple:{var:coolant_threshold} {L}/с} | макс. ввод {color_hot:{var:coolant_max_input} {L}/с} | макс. выход {color_steam:{var:coolant_max_output} {L}/т ПГ пара}
{color_hot:Раскалённая солнечная соль} | порог ПГ {light_purple:{var:salt_threshold} {L}/с} | макс. ввод {color_hot:{var:salt_max_input} {L}/с} | макс. выход {color_steam:{var:salt_max_output} {L}/т ПГ пара}
{hr}
Схема в контроллере снижает порог ПГ ценой выработки пара
{light_purple:-{var:threshold_reduction}%} порога ПГ и {color_loss:-{var:steam_penalty}%} выработки пара за каждое значение схемы выше {var:min_circuit}
