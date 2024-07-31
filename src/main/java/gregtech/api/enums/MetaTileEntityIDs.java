package gregtech.api.enums;

/*
 * No more magic numbers about TE's IDs. Yay!!!
 * The entries are sorted by ID, so if you need to take one,
 * please, pretty please, insert it at the correct place.
 */
public enum MetaTileEntityIDs {

    HULL_BRONZE(1),
    HULL_BRICKED_BRONZE(2),
    HULL_STEEL(3),
    HULL_WROUGHT_IRON(4),
    HULL_ULV(10),
    HULL_LV(11),
    HULL_MV(12),
    HULL_HV(13),
    HULL_EV(14),
    HULL_IV(15),
    HULL_LuV(16),
    HULL_ZPM(17),
    HULL_UV(18),
    HULL_UHV(19),
    transformer_LV_ULV(20),
    transformer_MV_LV(21),
    transformer_HV_MV(22),
    transformer_EV_HV(23),
    transformer_IV_EV(24),
    transformer_LuV_IV(25),
    transformer_ZPM_LuV(26),
    transformer_UV_ZPM(27),
    transformer_UHV_UV(28),
    DYNAMO_HATCH_ULV(30),
    DYNAMO_HATCH_LV(31),
    DYNAMO_HATCH_MV(32),
    DYNAMO_HATCH_HV(33),
    DYNAMO_HATCH_EV(34),
    DYNAMO_HATCH_IV(35),
    DYNAMO_HATCH_LuV(36),
    DYNAMO_HATCH_ZPM(37),
    DYNAMO_HATCH_UV(38),
    DYNAMO_HATCH_UHV(39),
    ENERGY_HATCH_ULV(40),
    ENERGY_HATCH_LV(41),
    ENERGY_HATCH_MV(42),
    ENERGY_HATCH_HV(43),
    ENERGY_HATCH_EV(44),
    ENERGY_HATCH_IV(45),
    ENERGY_HATCH_LuV(46),
    ENERGY_HATCH_ZPM(47),
    ENERGY_HATCH_UV(48),
    ENERGY_HATCH_UHV(49),
    INPUT_HATCH_ULV(50),
    INPUT_HATCH_LV(51),
    INPUT_HATCH_MV(52),
    INPUT_HATCH_HV(53),
    INPUT_HATCH_EV(54),
    INPUT_HATCH_IV(55),
    INPUT_HATCH_LuV(56),
    INPUT_HATCH_ZPM(57),
    INPUT_HATCH_UV(58),
    INPUT_HATCH_UHV(59),
    OUTPUT_HATCH_ULV(60),
    OUTPUT_HATCH_LV(61),
    OUTPUT_HATCH_MV(62),
    OUTPUT_HATCH_HV(63),
    OUTPUT_HATCH_EV(64),
    OUTPUT_HATCH_IV(65),
    OUTPUT_HATCH_LuV(66),
    OUTPUT_HATCH_ZPM(67),
    OUTPUT_HATCH_UV(68),
    OUTPUT_HATCH_UHV(69),
    INPUT_BUS_ULV(70),
    INPUT_BUS_LV(71),
    INPUT_BUS_MV(72),
    INPUT_BUS_HV(73),
    INPUT_BUS_EV(74),
    INPUT_BUS_IV(75),
    INPUT_BUS_LuV(76),
    INPUT_BUS_ZPM(77),
    INPUT_BUS_UV(78),
    INPUT_BUS_UHV(79),
    OUTPUT_BUS_ULV(80),
    OUTPUT_BUS_LV(81),
    OUTPUT_BUS_MV(82),
    OUTPUT_BUS_HV(83),
    OUTPUT_BUS_EV(84),
    OUTPUT_BUS_IV(85),
    OUTPUT_BUS_LuV(86),
    OUTPUT_BUS_ZPM(87),
    OUTPUT_BUS_UV(88),
    OUTPUT_BUS_UHV(89),
    MAINTENANCE_HATCH(90),
    MUFFLER_HATCH_LV(91),
    MUFFLER_HATCH_MV(92),
    MUFFLER_HATCH_HV(93),
    MUFFLER_HATCH_EV(94),
    MUFFLER_HATCH_IV(95),
    MUFFLER_HATCH_LuV(96),
    MUFFLER_HATCH_ZPM(97),
    MUFFLER_HATCH_UV(98),
    MUFFLER_HATCH_UHV(99),
    SMALL_COAL_BOILER(100),
    HIGH_PRESSURE_COAL_BOILER(101),
    HIGH_PRESSURE_LAVA_BOILER(102),
    STEAM_FURNACE(103),
    HP_STEAM_FURNACE(104),
    SIMPLE_SOLAR_BOILER(105),
    STEAM_MACERATOR(106),
    HP_STEAM_MACERATOR(107),
    STEAM_EXTRACTOR(109),
    HP_STEAM_EXTRACTOR(110),
    AUTO_MAINTENANCE_HATCH(111),
    STEAM_FORGE_HAMMER(112),
    HP_STEAM_FORGE_HAMMER(113),
    HIGH_PRESSURE_SOLAR_BOILER(114),
    STEAM_COMPRESSOR(115),
    HP_STEAM_COMPRESSOR(116),
    STEAM_ALLOY_SMELTER(118),
    HP_STEAM_ALLOY_SMELTER(119),
    QUANTUM_TANK_LV(120),
    QUANTUM_TANK_MV(121),
    QUANTUM_TANK_HV(122),
    QUANTUM_TANK_EV(123),
    QUANTUM_TANK_IV(124),
    QUANTUM_CHEST_LV(125),
    QUANTUM_CHEST_MV(126),
    QUANTUM_CHEST_HV(127),
    QUANTUM_CHEST_EV(128),
    QUANTUM_CHEST_IV(129),
    SUPER_TANK_LV(130),
    SUPER_TANK_MV(131),
    SUPER_TANK_HV(132),
    SUPER_TANK_EV(133),
    SUPER_TANK_IV(134),
    SUPER_CHEST_LV(135),
    SUPER_CHEST_MV(136),
    SUPER_CHEST_HV(137),
    SUPER_CHEST_EV(138),
    SUPER_CHEST_IV(139),
    BRICKED_BLAST_FURNACE_CONTROLLER(140),
    MULTILOCK_PUMP_MKII_CONTROLLER(141),
    MULTILOCK_PUMP_MKIII_CONTROLLER(142),
    CONCRETE_BACKFILLER_I_CONTROLLER(143),
    CONCRETE_BACKFILLER_II_CONTROLLER(144),
    DATA_ACCESS_HATCH(145),
    ADVANCED_DATA_ACCESS_HATCH(146),
    AUTOMATABLE_DATA_ACCESS_HATCH(147),
    MULTIBLOCK_PUMP_INFINITE_CONTROLLER(148),
    MULTILOCK_PUMP_MKIV_CONTROLLER(149),
    LOCKER_ULV(150),
    LOCKER_LV(151),
    LOCKER_MV(152),
    LOCKER_HV(153),
    LOCKER_EV(154),
    LOCKER_IV(155),
    LOCKER_LuV(156),
    LOCKER_ZPM(157),
    LOCKER_UV(158),
    LOCKER_UHV(159),
    BATTERY_BUFFER_1_BY_1_ULV(160),
    BATTERY_BUFFER_1_BY_1_LV(161),
    BATTERY_BUFFER_1_BY_1_MV(162),
    BATTERY_BUFFER_1_BY_1_HV(163),
    BATTERY_BUFFER_1_BY_1_EV(164),
    BATTERY_BUFFER_1_BY_1_IV(165),
    BATTERY_BUFFER_1_BY_1_LuV(166),
    BATTERY_BUFFER_1_BY_1_ZPM(167),
    BATTERY_BUFFER_1_BY_1_UV(168),
    BATTERY_BUFFER_1_BY_1_UHV(169),
    BATTERY_BUFFER_2_BY_2_ULV(170),
    BATTERY_BUFFER_2_BY_2_LV(171),
    BATTERY_BUFFER_2_BY_2_MV(172),
    BATTERY_BUFFER_2_BY_2_HV(173),
    BATTERY_BUFFER_2_BY_2_EV(174),
    BATTERY_BUFFER_2_BY_2_IV(175),
    BATTERY_BUFFER_2_BY_2_LuV(176),
    BATTERY_BUFFER_2_BY_2_ZPM(177),
    BATTERY_BUFFER_2_BY_2_UV(178),
    BATTERY_BUFFER_2_BY_2_UHV(179),
    BATTERY_BUFFER_3_BY_3_ULV(180),
    BATTERY_BUFFER_3_BY_3_LV(181),
    BATTERY_BUFFER_3_BY_3_MV(182),
    BATTERY_BUFFER_3_BY_3_HV(183),
    BATTERY_BUFFER_3_BY_3_EV(184),
    BATTERY_BUFFER_3_BY_3_IV(185),
    BATTERY_BUFFER_3_BY_3_LuV(186),
    BATTERY_BUFFER_3_BY_3_ZPM(187),
    BATTERY_BUFFER_3_BY_3_UV(188),
    BATTERY_BUFFER_3_BY_3_UHV(189),
    BATTERY_BUFFER_4_BY_4_ULV(190),
    BATTERY_BUFFER_4_BY_4_LV(191),
    BATTERY_BUFFER_4_BY_4_MV(192),
    BATTERY_BUFFER_4_BY_4_HV(193),
    BATTERY_BUFFER_4_BY_4_EV(194),
    BATTERY_BUFFER_4_BY_4_IV(195),
    BATTERY_BUFFER_4_BY_4_LuV(196),
    BATTERY_BUFFER_4_BY_4_ZPM(197),
    BATTERY_BUFFER_4_BY_4_UV(198),
    BATTERY_BUFFER_4_BY_4_UHV(199),
    QUADRUPLE_INPUT_HATCHES_EV(200),
    ALLOY_SMELTER_LV(201),
    ALLOY_SMELTER_MV(202),
    ALLOY_SMELTER_HV(203),
    ALLOY_SMELTER_EV(204),
    ALLOY_SMELTER_IV(205),
    WIRELESS_HATCH_ENERGY_ULV(206),
    WIRELESS_HATCH_ENERGY_LV(207),
    WIRELESS_HATCH_ENERGY_MV(208),
    WIRELESS_HATCH_ENERGY_HV(209),
    ASSEMBLER_LV(211),
    ASSEMBLER_MV(212),
    ASSEMBLER_HV(213),
    ASSEMBLER_EV(214),
    ASSEMBLER_IV(215),
    WIRELESS_HATCH_ENERGY_EV(216),
    WIRELESS_HATCH_ENERGY_IV(217),
    WIRELESS_HATCH_ENERGY_LuV(218),
    WIRELESS_HATCH_ENERGY_ZPM(219),
    BENDING_MACHINE_LV(221),
    BENDING_MACHINE_MV(222),
    BENDING_MACHINE_HV(223),
    BENDING_MACHINE_EV(224),
    BENDING_MACHINE_IV(225),
    WIRELESS_HATCH_ENERGY_UV(227),
    WIRELESS_HATCH_ENERGY_UHV(229),
    CANNER_LV(231),
    CANNER_MV(232),
    CANNER_HV(233),
    CANNER_EV(234),
    CANNER_IV(235),
    COMPRESSOR_LV(241),
    COMPRESSOR_MV(242),
    COMPRESSOR_HV(243),
    COMPRESSOR_EV(244),
    COMPRESSOR_IV(245),
    CUTTING_MACHINE_LV(251),
    CUTTING_MACHINE_MV(252),
    CUTTING_MACHINE_HV(253),
    CUTTING_MACHINE_EV(254),
    CUTTING_MACHINE_IV(255),
    ELECTRIC_FURNACE_LV(261),
    ELECTRIC_FURNACE_MV(262),
    ELECTRIC_FURNACE_HV(263),
    ELECTRIC_FURNACE_EV(264),
    ELECTRIC_FURNACE_IV(265),
    WIRELESS_HATCH_ENERGY_UEV(266),
    WIRELESS_HATCH_ENERGY_UIV(267),
    WIRELESS_HATCH_ENERGY_UMV(268),
    WIRELESS_HATCH_ENERGY_UXV(269),
    EXTRACTOR_LV(271),
    EXTRACTOR_MV(272),
    EXTRACTOR_HV(273),
    EXTRACTOR_EV(274),
    EXTRACTOR_IV(275),
    EXTRUDER_LV(281),
    EXTRUDER_MV(282),
    EXTRUDER_HV(283),
    EXTRUDER_EV(284),
    EXTRUDER_IV(285),
    WIRELESS_HATCH_ENERGY_MAX(286),
    WIRELESS_DYNAMO_ENERGY_HATCH_ULV(287),
    WIRELESS_DYNAMO_ENERGY_HATCH_LV(288),
    WIRELESS_DYNAMO_ENERGY_HATCH_MV(289),
    LATHE_LV(291),
    LATHE_MV(292),
    LATHE_HV(293),
    LATHE_EV(294),
    LATHE_IV(295),
    WIRELESS_DYNAMO_ENERGY_HATCH_HV(296),
    WIRELESS_DYNAMO_ENERGY_HATCH_EV(297),
    WIRELESS_DYNAMO_ENERGY_HATCH_IV(298),
    WIRELESS_DYNAMO_ENERGY_HATCH_LuV(299),
    MACERATOR_LV(301),
    MACERATOR_MV(302),
    MACERATOR_HV(303),
    MACERATOR_EV(304),
    MACERATOR_IV(305),
    WIRELESS_DYNAMO_ENERGY_HATCH_ZPM(310),
    MICROWAVE_OVEN_LV(311),
    MICROWAVE_OVEN_MV(312),
    MICROWAVE_OVEN_HV(313),
    MICROWAVE_OVEN_EV(314),
    MICROWAVE_OVEN_IV(315),
    WIRELESS_DYNAMO_ENERGY_HATCH_UV(316),
    WIRELESS_DYNAMO_ENERGY_HATCH_UHV(317),
    WIRELESS_DYNAMO_ENERGY_HATCH_UEV(318),
    WIRELESS_DYNAMO_ENERGY_HATCH_UIV(319),
    PRINTER_LV(321),
    PRINTER_MV(322),
    PRINTER_HV(323),
    PRINTER_EV(324),
    PRINTER_IV(325),
    PRINTER_LuV(326),
    PRINTER_ZPM(327),
    PRINTER_UV(328),
    RECYCLER_LV(331),
    RECYCLER_MV(332),
    RECYCLER_HV(333),
    RECYCLER_EV(334),
    RECYCLER_IV(335),
    SCANNER_LV(341),
    SCANNER_MV(342),
    SCANNER_HV(343),
    SCANNER_EV(344),
    SCANNER_IV(345),
    WIRELESS_DYNAMO_ENERGY_HATCH_UMV(346),
    WIRELESS_DYNAMO_ENERGY_HATCH_UXV(347),
    WIRELESS_DYNAMO_ENERGY_HATCH_MAX(348),
    ADVANCED_DEBUG_STRUCTURE_WRITTER(349),
    WIREMILL_LV(351),
    WIREMILL_MV(352),
    WIREMILL_HV(353),
    WIREMILL_EV(354),
    WIREMILL_IV(355),
    PCB_FACTORY_CONTROLLER(356),
    NANO_FORGE_CONTROLLER(357),
    INDUSTRIAL_ELECTROMAGNETIC_SEPARATOR_CONTROLLER(358),
    MAG_HATCH(359),
    MULTI_CANNER_CONTROLLER(360),
    CENTRIFUGE_LV(361),
    CENTRIFUGE_MV(362),
    CENTRIFUGE_HV(363),
    CENTRIFUGE_EV(364),
    CENTRIFUGE_IV(365),
    ELECTROLYSER_LV(371),
    ELECTROLYSER_MV(372),
    ELECTROLYSER_HV(373),
    ELECTROLYSER_EV(374),
    ELECTROLYSER_IV(375),
    THERMAL_CENTRIFUGE_LV(381),
    THERMAL_CENTRIFUGE_MV(382),
    THERMAL_CENTRIFUGE_HV(383),
    THERMAL_CENTRIFUGE_EV(384),
    THERMAL_CENTRIFUGE_IV(385),
    ORE_WASHER_LV(391),
    ORE_WASHER_MV(392),
    ORE_WASHER_HV(393),
    ORE_WASHER_EV(394),
    ORE_WASHER_IV(395),
    PACKAGER_LV(401),
    PACKAGER_MV(402),
    PACKAGER_HV(403),
    PACKAGER_EV(404),
    PACKAGER_IV(405),
    PACKAGER_LuV(406),
    PACKAGER_ZPM(407),
    PACKAGER_UV(408),
    UNPACKAGER_LV(411),
    UNPACKAGER_MV(412),
    UNPACKAGER_HV(413),
    UNPACKAGER_EV(414),
    UNPACKAGER_IV(415),
    UNPACKAGER_LuV(416),
    UNPACKAGER_ZPM(417),
    UNPACKAGER_UV(418),
    CHEMICAL_REACTOR_LV(421),
    CHEMICAL_REACTOR_MV(422),
    CHEMICAL_REACTOR_HV(423),
    CHEMICAL_REACTOR_EV(424),
    CHEMICAL_REACTOR_IV(425),
    FLUID_CANNER_LV(431),
    FLUID_CANNER_MV(432),
    FLUID_CANNER_HV(433),
    FLUID_CANNER_EV(434),
    FLUID_CANNER_IV(435),
    ROCK_BREAKER_LV(441),
    ROCK_BREAKER_MV(442),
    ROCK_BREAKER_HV(443),
    ROCK_BREAKER_EV(444),
    ROCK_BREAKER_IV(445),
    MASS_FABRICATOR_LV(461),
    MASS_FABRICATOR_MV(462),
    MASS_FABRICATOR_HV(463),
    MASS_FABRICATOR_EV(464),
    MASS_FABRICATOR_IV(465),
    MATTER_AMPLIFIER_LV(471),
    MATTER_AMPLIFIER_MV(472),
    MATTER_AMPLIFIER_HV(473),
    MATTER_AMPLIFIER_EV(474),
    MATTER_AMPLIFIER_IV(475),
    REPLICATOR_LV(481),
    REPLICATOR_MV(482),
    REPLICATOR_HV(483),
    REPLICATOR_EV(484),
    REPLICATOR_IV(485),
    BREWERY_LV(491),
    BREWERY_MV(492),
    BREWERY_HV(493),
    BREWERY_EV(494),
    BREWERY_IV(495),
    FERMENTER_LV(501),
    FERMENTER_MV(502),
    FERMENTER_HV(503),
    FERMENTER_EV(504),
    FERMENTER_IV(505),
    FLUID_EXTRACTOR_LV(511),
    FLUID_EXTRACTOR_MV(512),
    FLUID_EXTRACTOR_HV(513),
    FLUID_EXTRACTOR_EV(514),
    FLUID_EXTRACTOR_IV(515),
    FLUID_SOLIDIFIER_LV(521),
    FLUID_SOLIDIFIER_MV(522),
    FLUID_SOLIDIFIER_HV(523),
    FLUID_SOLIDIFIER_EV(524),
    FLUID_SOLIDIFIER_IV(525),
    DISTILLERY_LV(531),
    DISTILLERY_MV(532),
    DISTILLERY_HV(533),
    DISTILLERY_EV(534),
    DISTILLERY_IV(535),
    CHEMICAL_BATH_LV(541),
    CHEMICAL_BATH_MV(542),
    CHEMICAL_BATH_HV(543),
    CHEMICAL_BATH_EV(544),
    CHEMICAL_BATH_IV(545),
    POLARIZER_LV(551),
    POLARIZER_MV(552),
    POLARIZER_HV(553),
    POLARIZER_EV(554),
    POLARIZER_IV(555),
    ELECTROMAGNETIC_SEPARATOR_LV(561),
    ELECTROMAGNETIC_SEPARATOR_MV(562),
    ELECTROMAGNETIC_SEPARATOR_HV(563),
    ELECTROMAGNETIC_SEPARATOR_EV(564),
    ELECTROMAGNETIC_SEPARATOR_IV(565),
    AUTOCLAVE_LV(571),
    AUTOCLAVE_MV(572),
    AUTOCLAVE_HV(573),
    AUTOCLAVE_EV(574),
    AUTOCLAVE_IV(575),
    MIXER_LV(581),
    MIXER_MV(582),
    MIXER_HV(583),
    MIXER_EV(584),
    MIXER_IV(585),
    LASER_ENGRAVER_LV(591),
    LASER_ENGRAVER_MV(592),
    LASER_ENGRAVER_HV(593),
    LASER_ENGRAVER_EV(594),
    LASER_ENGRAVER_IV(595),
    FORMING_PRESS_LV(601),
    FORMING_PRESS_MV(602),
    FORMING_PRESS_HV(603),
    FORMING_PRESS_EV(604),
    FORMING_PRESS_IV(605),
    FORGE_HAMMER_LV(611),
    FORGE_HAMMER_MV(612),
    FORGE_HAMMER_HV(613),
    FORGE_HAMMER_EV(614),
    FORGE_HAMMER_IV(615),
    FLUID_HEATER_LV(621),
    FLUID_HEATER_MV(622),
    FLUID_HEATER_HV(623),
    FLUID_HEATER_EV(624),
    FLUID_HEATER_IV(625),
    SLICER_LV(631),
    SLICER_MV(632),
    SLICER_HV(633),
    SLICER_EV(634),
    SLICER_IV(635),
    SIFTER_LV(641),
    SIFTER_MV(642),
    SIFTER_HV(643),
    SIFTER_EV(644),
    SIFTER_IV(645),
    ARC_FURNACE_LV(651),
    ARC_FURNACE_MV(652),
    ARC_FURNACE_HV(653),
    ARC_FURNACE_EV(654),
    ARC_FURNACE_IV(655),
    PLASMA_ARC_FURNACE_LV(661),
    PLASMA_ARC_FURNACE_MV(662),
    PLASMA_ARC_FURNACE_HV(663),
    PLASMA_ARC_FURNACE_EV(664),
    PLASMA_ARC_FURNACE_IV(665),
    OVEN_LV(671),
    OVEN_MV(672),
    OVEN_HV(673),
    OVEN_EV(674),
    OVEN_IV(675),
    MINER_LV(679),
    MINER_MV(680),
    MINER_HV(681),
    MULTI_LATHE_CONTROLLER(686),
    BATTERY_CHARGER_4_BY_4_ULV(690),
    BATTERY_CHARGER_4_BY_4_LV(691),
    BATTERY_CHARGER_4_BY_4_MV(692),
    BATTERY_CHARGER_4_BY_4_HV(693),
    BATTERY_CHARGER_4_BY_4_EV(694),
    BATTERY_CHARGER_4_BY_4_IV(695),
    BATTERY_CHARGER_4_BY_4_LuV(696),
    BATTERY_CHARGER_4_BY_4_ZPM(697),
    BATTERY_CHARGER_4_BY_4_UV(698),
    BATTERY_CHARGER_4_BY_4_UHV(699),
    QUADRUPLE_INPUT_HATCHES_IV(710),
    QUADRUPLE_INPUT_HATCHES_LuV(711),
    QUADRUPLE_INPUT_HATCHES_ZPM(712),
    QUADRUPLE_INPUT_HATCHES_UV(713),
    QUADRUPLE_INPUT_HATCHES_UHV(714),
    QUADRUPLE_INPUT_HATCHES_UEV(715),
    QUADRUPLE_INPUT_HATCHES_UIV(716),
    QUADRUPLE_INPUT_HATCHES_UMV(717),
    QUADRUPLE_INPUT_HATCHES_UXV(718),
    QUADRUPLE_INPUT_HATCHES_MAX(719),
    EBF_CONTROLLER(1000),
    IMPLOSION_COMPRESSOR_CONTROLLER(1001),
    VACUUM_FREEZER_CONTROLLER(1002),
    MULTI_SMELTER_CONTROLLER(1003),
    DTPF_CONTROLLER(1004),
    LARGE_ADVANCED_GAS_TURBINE_CONTROLLER(1005),
    TRANSCENDENT_PLASMA_MIXER_CONTROLLER(1006),
    LARGE_BRONZE_BOILER_CONTROLLER(1020),
    LARGE_STEEL_BOILER_CONTROLLER(1021),
    LARGE_TITANIUM_BOILER_CONTROLLER(1022),
    LARGE_TUNGSTENSTEEL_BOILER_CONTROLLER(1023),
    COMBUSTION_GENERATOR_LV(1110),
    COMBUSTION_GENERATOR_MV(1111),
    COMBUSTION_GENERATOR_HV(1112),
    GAS_TURBINE_LV(1115),
    GAS_TURBINE_MV(1116),
    GAS_TURBINE_HV(1117),
    GAS_TURBINE_EV(1118),
    GAS_TURBINE_IV(1119),
    STEAM_TURBINE_LV(1120),
    STEAM_TURBINE_MV(1121),
    STEAM_TURBINE_HV(1122),
    MAGIC_ENERGY_CONVERTER_LV(1123),
    MAGIC_ENERGY_CONVERTER_MV(1124),
    MAGIC_ENERGY_CONVERTER_HV(1125),
    DISTILLATION_TOWER_CONTROLLER(1126),
    MAGIC_ENERGY_ABSORBER_LV(1127),
    MAGIC_ENERGY_ABSORBER_MV(1128),
    MAGIC_ENERGY_ABSORBER_HV(1129),
    MAGIC_ENERGY_ABSORBER_EV(1130),
    LARGE_STEAM_TURBINE_CONTROLLER(1131),
    INTEGRATED_ORE_FACTORY_CONTROLLER(1132),
    MONSTER_REPELLATOR_LuV(1135),
    MONSTER_REPELLATOR_ZPM(1136),
    MONSTER_REPELLATOR_UV(1137),
    PUMP_LV(1140),
    PUMP_MV(1141),
    PUMP_HV(1142),
    PUMP_EV(1143),
    PUMP_IV(1144),
    TELEPORTER(1145),
    MONSTER_REPELLATOR_LV(1146),
    MONSTER_REPELLATOR_MV(1147),
    MONSTER_REPELLATOR_HV(1148),
    MONSTER_REPELLATOR_EV(1149),
    MONSTER_REPELLATOR_IV(1150),
    LARGE_GAS_TURBINE_CONTROLLER(1151),
    LARGE_HP_STEAM_TURBINE_CONTROLLER(1152),
    LARGE_PLASMA_TURBINE_CONTROLLER(1153),
    LARGE_HEAT_EXCHANGER_CONTROLLER(1154),
    CHARCOAL_PILE_IGNITER_CONTROLLER(1155),
    MULTIBLOCK_PUMP_MKI_CONTROLLER(1157),
    ORE_DRILL_MKI_CONTROLLER(1158),
    PYROLYSE_OVEN_CONTROLLER(1159),
    OIL_CRACKER_CONTROLLER(1160),
    MICROWAVE_ENERGY_TRANSMITTER_HV(1161),
    MICROWAVE_ENERGY_TRANSMITTER_EV(1162),
    MICROWAVE_ENERGY_TRANSMITTER_IV(1163),
    MICROWAVE_ENERGY_TRANSMITTER_LuV(1164),
    MICROWAVE_ENERGY_TRANSMITTER_ZPM(1165),
    MICROWAVE_ENERGY_TRANSMITTER_UV(1166),
    LCR_CONTROLLER(1169),
    ASSEMBLING_LINE_CONTROLLER(1170),
    COMBUSTION_ENGINE_CONTROLLER(1171),
    CLEANROOM_CONTROLLER(1172),
    ADVANCED_SEISMIC_PROSPECTOR_EV(1173),
    LIGHTNING_ROD_HV(1174),
    LIGHTNING_ROD_EV(1175),
    LIGHTNING_ROD_IV(1176),
    ORE_DRILL_MKII_CONTROLLER(1177),
    ORE_DRILL_MKIII_CONTROLLER(1178),
    ORE_DRILL_MKIV_CONTROLLER(1179),
    CIRCUIT_ASSEMBLER_LV(1180),
    CIRCUIT_ASSEMBLER_MV(1181),
    CIRCUIT_ASSEMBLER_HV(1182),
    CIRCUIT_ASSEMBLER_EV(1183),
    CIRCUIT_ASSEMBLER_IV(1184),
    CIRCUIT_ASSEMBLER_LuV(1185),
    CIRCUIT_ASSEMBLER_ZPM(1186),
    CIRCUIT_ASSEMBLER_UV(1187),
    NAQUADAH_REACTOR_ZPM(1188),
    NAQUADAH_REACTOR_UV(1189),
    NAQUADAH_REACTOR_EV(1190),
    NAQUADAH_REACTOR_IV(1191),
    NAQUADAH_REACTOR_LuV(1192),
    FUSION_CONTROLLER_MKI(1193),
    FUSION_CONTROLLER_MKII(1194),
    FUSION_CONTROLLER_MKIII(1195),
    PLASMA_GENERATOR_IV(1196),
    PLASMA_GENERATOR_LuV(1197),
    PLASMA_GENERATOR_ZPM(1198),
    PROCESSING_ARRAY_CONTROLLER(1199),
    ADVANCED_SEISMIC_PROSPECTOR_LV(2102),
    ADVANCED_SEISMIC_PROSPECTOR_MV(2103),
    ADVANCED_SEISMIC_PROSPECTOR_HV(2104),
    EXTREME_COMBUSTION_ENGINE_CONTROLLER(2105),
    LONG_DISTANCE_PIPELINE_FLUID(2700),
    LONG_DISTANCE_PIPELINE_ITEM(2701),
    OUTPUT_BUS_ME(2710),
    INPUT_BUS_ME_ADVANCED(2711),
    INPUT_HATCH_ME_ADVANCED(2712),
    OUTPUT_HATCH_ME(2713),
    CRAFTING_INPUT_ME(2714),
    CRAFTING_INPUT_ME_BUS(2715),
    CRAFTING_INPUT_SLAVE(2716),
    INPUT_HATCH_ME(2717),
    INPUT_BUS_ME(2718),
    INDUSTRIAL_LASER_ENGRAVER_CONTROLLER(3004),
    CHEST_BUFFER_ULV(9230),
    CHEST_BUFFER_LV(9231),
    CHEST_BUFFER_MV(9232),
    CHEST_BUFFER_HV(9233),
    CHEST_BUFFER_EV(9234),
    CHEST_BUFFER_IV(9235),
    CHEST_BUFFER_LuV(9236),
    CHEST_BUFFER_ZPM(9237),
    CHEST_BUFFER_UV(9238),
    CHEST_BUFFER_UHV(9239),
    ITEM_FILTER_ULV(9240),
    ITEM_FILTER_LV(9241),
    ITEM_FILTER_MV(9242),
    ITEM_FILTER_HV(9243),
    ITEM_FILTER_EV(9244),
    ITEM_FILTER_IV(9245),
    ITEM_FILTER_LuV(9246),
    ITEM_FILTER_ZPM(9247),
    ITEM_FILTER_UV(9248),
    ITEM_FILTER_UHV(9249),
    TYPE_FILTER_ULV(9250),
    TYPE_FILTER_LV(9251),
    TYPE_FILTER_MV(9252),
    TYPE_FILTER_HV(9253),
    TYPE_FILTER_EV(9254),
    TYPE_FILTER_IV(9255),
    TYPE_FILTER_LuV(9256),
    TYPE_FILTER_ZPM(9257),
    TYPE_FILTER_UV(9258),
    TYPE_FILTER_UHV(9259),
    VOLTAGE_REGULATOR_ULV(9270),
    VOLTAGE_REGULATOR_LV(9271),
    VOLTAGE_REGULATOR_MV(9272),
    VOLTAGE_REGULATOR_HV(9273),
    VOLTAGE_REGULATOR_EV(9274),
    VOLTAGE_REGULATOR_IV(9275),
    VOLTAGE_REGULATOR_LuV(9276),
    VOLTAGE_REGULATOR_ZPM(9277),
    VOLTAGE_REGULATOR_UV(9278),
    VOLTAGE_REGULATOR_UHV(9279),
    SUPER_BUFFER_ULV(9300),
    SUPER_BUFFER_LV(9301),
    SUPER_BUFFER_MV(9302),
    SUPER_BUFFER_HV(9303),
    SUPER_BUFFER_EV(9304),
    SUPER_BUFFER_IV(9305),
    SUPER_BUFFER_LuV(9306),
    SUPER_BUFFER_ZPM(9307),
    SUPER_BUFFER_UV(9308),
    SUPER_BUFFER_UHV(9309),
    ITEM_DISTRIBUTOR_ULV(9320),
    ITEM_DISTRIBUTOR_LV(9321),
    ITEM_DISTRIBUTOR_MV(9322),
    ITEM_DISTRIBUTOR_HV(9323),
    ITEM_DISTRIBUTOR_EV(9324),
    ITEM_DISTRIBUTOR_IV(9325),
    ITEM_DISTRIBUTOR_LuV(9326),
    ITEM_DISTRIBUTOR_ZPM(9327),
    ITEM_DISTRIBUTOR_UV(9328),
    ITEM_DISTRIBUTOR_UHV(9329),
    RECIPE_FILTER_ULV(9330),
    RECIPE_FILTER_LV(9331),
    RECIPE_FILTER_MV(9332),
    RECIPE_FILTER_HV(9333),
    RECIPE_FILTER_EV(9334),
    RECIPE_FILTER_IV(9335),
    RECIPE_FILTER_LuV(9336),
    RECIPE_FILTER_ZPM(9337),
    RECIPE_FILTER_UV(9338),
    RECIPE_FILTER_UHV(9339),
    INDUSTRIAL_APIARY(9399),
    Drone_Centre(9400),
    DroneDownLink(9401),
    PURIFICATION_PLANT_CONTROLLER(9402),
    PURIFICATION_UNIT_CLARIFIER(9403),
    PURIFICATION_UNIT_OZONATION(9404),
    PURIFICATION_UNIT_FLOCCULATOR(9405),
    PURIFICATION_UNIT_PH_ADJUSTMENT(9406),
    HATCH_PH_SENSOR(9407),
    PURIFICATION_UNIT_PLASMA_HEATER(9408),
    PURIFICATION_UNIT_UV_TREATMENT(9409),
    HATCH_LENS_HOUSING(9410),
    HATCH_LENS_INDICATOR(9411),
    PURIFICATION_UNIT_DEGASIFIER(9412),
    HATCH_DEGASIFIER_CONTROL(9413),
    PURIFICATION_UNIT_PARTICLE_EXTRACTOR(9414),
    sofc1(13101),
    sofc2(13102),
    tfft(13104),
    lsc(13106),
    tfftHatch(13109);

    public final int ID;

    private MetaTileEntityIDs(int ID) {
        this.ID = ID;
    }
}
