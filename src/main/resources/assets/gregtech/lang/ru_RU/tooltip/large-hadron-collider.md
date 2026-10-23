Ускоряет частицы до высоких энергий, а затем сталкивает их, порождая новые частицы
Электрически нейтральные частицы не ускоряются
Задайте {color_electron_volt:целевую энергию пучка} (до {color_electron_volt:{var:max_energy} {TeV}}) и {light_purple:максимальное число циклов}
Один цикл в секунду
{hr}
{color_mode:Режим ускорителя}
Ускоряет пучок в малом кольце LHC
Каждый цикл повышает {color_electron_volt:энергию пучка} на {color_eu_usage:{EU}, потреблённые за цикл} * {color_electron_volt:{var:ev_per_eu} {eV}} / {gold:интенсивность пучка}
пока не будет достигнута {color_electron_volt:целевая энергия пучка} или {light_purple:максимальное число циклов}
Когда {color_electron_volt:целевая энергия пучка} достигнута, к текущей {gold:интенсивности пучка} добавляется {color_formula:{var:rate_gain}} * {gold:интенсивность пучка}
При {gold:интенсивности пучка} выше {color_formula:{var:nerf_cutoff}} вместо этого добавляется ({color_formula:{var:rate_gain}} * {gold:интенсивность пучка})^{dark_red:{var:nerf_power}}
{color_eu_usage:Мощность} начинается с {color_amperage:1A} {var:tier} на единицу {gold:интенсивности пучка} и растёт квадратично с {light_purple:числом завершённых циклов}
{color_eu_usage:P} = ({color_amperage:1A} {var:tier}) * {gold:R} * ({light_purple:N}+1)^2
Переключается в режим коллайдера, когда достигнуто {light_purple:максимальное число циклов}
{hr}
{color_mode:Режим коллайдера}
Делит пучок на {color_formula:два}, которые летят навстречу друг другу в большом кольце LHC и сталкиваются, порождая новые частицы
{color_electron_volt:Энергия столкновения} в {color_formula:{var:collision_multiplier}} раза больше {color_electron_volt:энергии пучка}
Частица появляется на выходе, когда {color_electron_volt:энергия столкновения} превышает её массу покоя
{color_name:Выходные шлюзы фильтруемого пучка} и таблица вероятностей показывают список возможных выходов и позволяют их отфильтровать
{hr}
{color_mode:Усиливающие жидкости}
{color_name:Усиливающие жидкости} повышают {dark_red:показатель масштабирования} при {gold:интенсивности пучка} выше {color_formula:{var:nerf_cutoff}}
{color_name:Без жидкости}: {dark_red:{var:nerf_power}}
{color_name:{fluid:quarkgluonplasma}}: {dark_red:{var:nerf_power_qgp}}
{color_name:{fluid:molten.magmatter}}: {dark_red:{var:nerf_power_magmatter}}
С усилителем {color_electron_volt:энергия пучка} сразу становится {color_electron_volt:{var:max_energy} {TeV}}, а каждый цикл расходуется {gold:интенсивность пучка {L}} жидкости
Если сменить {color_name:усиливающую жидкость}, пучок придётся разгонять заново
{hr}
Модулей LHC четыре, каждый отмечен буквой на верхней грани:
{aqua:E - Электромагнетизм}: Детектор заряженной материи ({aqua:CMS})
{dark_green:W - Слабое взаимодействие}: Улучшенная ловушка ассимиляции полных лептонов ({dark_green:ATLAS})
{white:S - Сильное взаимодействие}: Абсолютный решёточный интегрированный хромодинамический инкапсулятор ({white:ALICE})
{dark_purple:G - Гравитация}: Локализованный связующий элемент кривизны горизонта ({dark_purple:LHCb})
