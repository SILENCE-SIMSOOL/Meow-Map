package silence.simsool.meowmap.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.*
import silence.simsool.meowmap.MeowMap
import silence.simsool.meowmap.ui.EditLocationGui
import java.awt.Color
import java.io.File

object Config : Vigilant(File("./config/SilenceUtils/MeowMap/config.toml"), MeowMap.NAME + " " + MeowMap.VERSION, sortingBehavior = CategorySorting) {

// GENERAL - CATEGORY
    @Property(
        name = "Meow Map",
        type = PropertyType.SWITCH,
        description = "Displays a map on the screen for Dungeons.",
        category = "General",
        subcategory = "Enable"
    )
    var mapEnabled = true

    @Property(
        name = "Edit Map Position",
        type = PropertyType.BUTTON,
        category = "General",
        subcategory = "Position",
        placeholder = "Edit GUI"
    )
    fun openMoveMapGui() {
        MeowMap.display = EditLocationGui()
    }

    @Property(
        name = "Reset Map Position",
        type = PropertyType.BUTTON,
        category = "General",
        subcategory = "Position",
        placeholder = "Reset"
    )
    fun resetMapLocation() {
        mapX = 10
        mapY = 10
    }



// MAP - CATEGORY
    @Property(
        name = "Rotate Map",
        type = PropertyType.SWITCH,
        description = "Rotates map to follow the player.",
        category = "Map",
        subcategory = "Map Setup"
    )
    var mapRotate = false

    @Property(
        name = "Center Map §7(Rotate Map)",
        type = PropertyType.SWITCH,
        description = "Centers the map on the player.",
        category = "Map",
        subcategory = "Map Setup"
    )
    var mapCenter = false

    @Property(
        name = "Dynamic Rotate §7(Rotate Map)",
        type = PropertyType.SWITCH,
        description = "Keeps the entrance room at the bottom.",
        category = "Map",
        subcategory = "Map Setup"
    )
    var mapDynamicRotate = false

    @Property(
        name = "Hide In Boss",
        type = PropertyType.SWITCH,
        description = "Hides the map in boss.",
        category = "Map",
        subcategory = "Map Setup"
    )
    var mapHideInBoss = true

    @Property(
        name = "Show Player Names",
        type = PropertyType.SELECTOR,
        description = "Show player name under player head",
        category = "Map",
        subcategory = "Toggle",
        options = ["Off", "Holding Leap", "Always"]
    )
    var playerHeads = 1

    @Property(
        name = "Vanilla Head Marker",
        type = PropertyType.SWITCH,
        description = "Uses the vanilla head marker for yourself.",
        category = "Map",
        subcategory = "Toggle"
    )
    var mapVanillaMarker = true




    @Property(
        name = "Map X",
        type = PropertyType.NUMBER,
        category = "Map",
        subcategory = "Size",
        hidden = true
    )
    var mapX = 10

    @Property(
        name = "Map Y",
        type = PropertyType.NUMBER,
        category = "Map",
        subcategory = "Size",
        hidden = true
    )
    var mapY = 10




    @Property(
        name = "Map Scale",
        type = PropertyType.DECIMAL_SLIDER,
        category = "Map",
        subcategory = "Scale",
        minF = 0.1f,
        maxF = 4f,
        decimalPlaces = 2,
        hidden = true
    )
    var mapScale = 1.25f

    @Property(
        name = "Room Name Text Scale",
        type = PropertyType.DECIMAL_SLIDER,
        description = "Scale of room names and secret counts relative to map size.",
        category = "Map",
        subcategory = "Scale",
        maxF = 2f,
        decimalPlaces = 2
    )
    var textScale = 0.75f

    @Property(
        name = "Player Heads Scale",
        type = PropertyType.DECIMAL_SLIDER,
        description = "Scale of player heads relative to map size.",
        category = "Map",
        subcategory = "Scale",
        maxF = 2f,
        decimalPlaces = 2
    )
    var playerHeadScale = 0.75f

    @Property(
        name = "Player Name Scale",
        type = PropertyType.DECIMAL_SLIDER,
        description = "Scale of player names relative to head size.",
        category = "Map",
        subcategory = "Scale",
        maxF = 2f,
        decimalPlaces = 2
    )
    var playerNameScale = .8f


    @Property(
        name = "Map Background Color",
        type = PropertyType.COLOR,
        category = "Map",
        subcategory = "Background Color",
        allowAlpha = true
    )
    var mapBackground = Color(0, 0, 0, 0)

    @Property(
        name = "Map Border Color",
        type = PropertyType.COLOR,
        category = "Map",
        subcategory = "Background Color",
        allowAlpha = true
    )
    var mapBorder = Color(0, 0, 0, 255)

    @Property(
        name = "Border Thickness",
        type = PropertyType.DECIMAL_SLIDER,
        category = "Map",
        subcategory = "Background Color",
        maxF = 10f
    )
    var mapBorderWidth = 0f


    @Property(
        name = "Show Room Name",
        type = PropertyType.SELECTOR,
        description = "Shows names of rooms on map.",
        category = "Room",
        subcategory = "Room Mark",
        options = ["None", "Puzzles / Trap", "All"]
    )
    var mapRoomNames = 0

    @Property(
        name = "Show Room Secret",
        type = PropertyType.SELECTOR,
        description = "Shows total secrets of rooms on map.",
        category = "Room",
        subcategory = "Room Mark",
        options = ["Off", "On", "Replace Checkmark"]
    )
    var mapRoomSecrets = 0

    @Property(
        name = "Show Room Checkmark",
        type = PropertyType.SELECTOR,
        description = "Shows room checkmarks on room state.",
        category = "Room",
        subcategory = "Room Mark",
        options = ["None", "Default", "NEU", "Legacy"]
    )
    var mapCheckmark = 1

    @Property(
        name = "Center Room Name",
        type = PropertyType.SWITCH,
        description = "Center room names.",
        subcategory = "Setup",
        category = "Room"
    )
    var mapCenterRoomName = true

    @Property(
        name = "Color Text",
        type = PropertyType.SWITCH,
        description = "Colors name and secret count based on room state.",
        subcategory = "Setup",
        category = "Room"
    )
    var mapColorText = true

    @Property(
        name = "Center Room Checkmarks",
        type = PropertyType.SWITCH,
        description = "Center room checkmarks.",
        subcategory = "Setup",
        category = "Room"
    )
    var mapCenterCheckmark = false





// COLOR - CATEGORY

    var colorBloodDoor = Color(231, 0, 0)
    var colorEntranceDoor = Color(114, 67, 27)
    var colorRoomDoor = Color(114, 67, 27)
    var colorWitherDoor = Color(0, 0, 0)
    var colorOpenWitherDoor = Color(114, 67, 27)
    var colorUnopenedDoor = Color(65, 65, 65)

    var colorBlood = Color(255, 0, 0)
    var colorEntrance = Color(0, 130, 0)
    var colorFairy = Color(239, 126, 163)
    var colorMiniboss = Color(226, 226, 50)
    var colorRoom = Color(114, 67, 27)
    var colorPuzzle = Color(176, 75, 213)
    var colorRare = Color(255, 203, 89)
    var colorTrap = Color(213, 126, 50)
    var colorUnopened = Color(64, 64, 64)

    var colorTextCleared = Color(220, 220, 220)
    var colorTextUncleared = Color(155, 155, 155)
    var colorTextGreen = Color(15, 135, 15)
    var colorTextFailed = Color(220, 220, 220)



    // SCORE - CATEGORY
    @Property(
        name = "Display Score Status",
        type = PropertyType.SWITCH,
        description = "Display score status.",
        category = "Score",
        subcategory = "Score Status"
    )
    var scoreElementEnabled = false

    @Property(
        name = "Assume Spirit",
        type = PropertyType.SWITCH,
        description = "Assume everyone has a legendary spirit pet.",
        category = "Score",
        subcategory = "Score Status"
    )
    var scoreAssumeSpirit = true

    @Property(
        name = "Minimized Text",
        description = "Shortens description for score elements.",
        type = PropertyType.SWITCH,
        category = "Score",
        subcategory = "Score Status"
    )
    var scoreMinimizedName = false

    @Property(
        name = "Hide in Boss",
        type = PropertyType.SWITCH,
        category = "Score",
        subcategory = "Score Status"
    )
    var scoreHideInBoss = true

    @Property(
        name = "Score Calc X",
        type = PropertyType.NUMBER,
        category = "Score",
        subcategory = "Size",
        hidden = true
    )
    var scoreX = 10

    @Property(
        name = "Score Calc Y",
        type = PropertyType.NUMBER,
        category = "Score",
        subcategory = "Size",
        hidden = true
    )
    var scoreY = 10

    @Property(
        name = "Score Calc Size",
        type = PropertyType.DECIMAL_SLIDER,
        category = "Score",
        subcategory = "Size",
        minF = 0.1f,
        maxF = 4f,
        decimalPlaces = 2,
        hidden = true
    )
    var scoreScale = 1f

    @Property(
        name = "Score",
        type = PropertyType.SELECTOR,
        category = "Score",
        subcategory = "Elements",
        options = ["Off", "On", "Separate"]
    )
    var scoreTotalScore = 2

    @Property(
        name = "Secrets",
        type = PropertyType.SELECTOR,
        category = "Score",
        subcategory = "Elements",
        options = ["Off", "Total", "Total and Missing"]
    )
    var scoreSecrets = 1

    @Property(
        name = "Crypts",
        type = PropertyType.SWITCH,
        category = "Score",
        subcategory = "Elements"
    )
    var scoreCrypts = false

    @Property(
        name = "Mimic",
        type = PropertyType.SWITCH,
        category = "Score",
        subcategory = "Elements"
    )
    var scoreMimic = false

    @Property(
        name = "Deaths",
        type = PropertyType.SWITCH,
        category = "Score",
        subcategory = "Elements"
    )
    var scoreDeaths = false

    @Property(
        name = "Puzzles",
        type = PropertyType.SELECTOR,
        category = "Score",
        subcategory = "Elements",
        options = ["Off", "Total", "Completed and Total"]
    )
    var scorePuzzles = 0

    @Property(
        name = "Send Score Messages",
        type = PropertyType.SELECTOR,
        description = "Sends score message.",
        category = "Score",
        subcategory = "Message",
        options = ["Off", "300", "270 & 300"]
    )
    var scoreMessage = 0

    @Property(
        name = "Show Score Title",
        type = PropertyType.SELECTOR,
        description = "Shows score messages as a title notification.",
        category = "Score",
        subcategory = "Message",
        options = ["Off", "300", "270 & 300"]
    )
    var scoreTitle = 0

    @Property(
        name = "270 Message",
        type = PropertyType.TEXT,
        category = "Score",
        subcategory = "Message"
    )
    var message270 = "Now S (270 Score)!"

    @Property(
        name = "300 Message",
        type = PropertyType.TEXT,
        category = "Score",
        subcategory = "Message"
    )
    var message300 = "Now S+ (300 Score)!"

    @Property(
        name = "300 Time",
        type = PropertyType.SWITCH,
        description = "Shows time to reach 300 score.",
        category = "Score",
        subcategory = "Message"
    )
    var timeTo300 = false

    @Property(
        name = "Show Run Information",
        type = PropertyType.SWITCH,
        description = "Shows information box under map.",
        category = "Map",
        subcategory = "Information Box"
    )
    var mapShowRunInformation = false

    @Property(
        name = "Score",
        type = PropertyType.SWITCH,
        category = "Map",
        subcategory = "Information Box"
    )
    var runInformationScore = true

    @Property(
        name = "Secrets",
        type = PropertyType.SELECTOR,
        category = "Map",
        subcategory = "Information Box",
        options = ["Off", "Total", "Total and Missing"]
    )
    var runInformationSecrets = 1

    @Property(
        name = "Crypts",
        type = PropertyType.SWITCH,
        category = "Map",
        subcategory = "Information Box"
    )
    var runInformationCrypts = true

    @Property(
        name = "Mimic",
        type = PropertyType.SWITCH,
        category = "Map",
        subcategory = "Information Box"
    )
    var runInformationMimic = true

    @Property(
        name = "Deaths",
        type = PropertyType.SWITCH,
        category = "Map",
        subcategory = "Information Box"
    )
    var runInformationDeaths = true

    @Property(
        name = "Mimic Message",
        type = PropertyType.SWITCH,
        description = "Sends party message when a mimic is killed. Detects most instant kills.",
        category = "Other Features",
        subcategory = "Mimic Message"
    )
    var mimicMessageEnabled = true

    @Property(
        name = "Mimic Message Text",
        type = PropertyType.TEXT,
        category = "Other Features",
        subcategory = "Mimic Message"
    )
    var mimicMessage = "Mimic Killed!"

    @Property(
        name = "Box Wither Door",
        description = "Boxes unopened wither doors.",
        type = PropertyType.SWITCH,
        category = "Other Features",
        subcategory = "Wither Door"
    )
    var witherDoorESP = false

    @Property(
        name = "No Key Color",
        type = PropertyType.COLOR,
        category = "Other Features",
        subcategory = "Wither Door",
        allowAlpha = true
    )
    var witherDoorNoKeyColor = Color(255, 0, 0)

    @Property(
        name = "Has Key Color",
        type = PropertyType.COLOR,
        category = "Other Features",
        subcategory = "Wither Door",
        allowAlpha = true
    )
    var witherDoorKeyColor = Color(0, 255, 0)

    @Property(
        name = "Door Outline Width",
        type = PropertyType.DECIMAL_SLIDER,
        category = "Other Features",
        subcategory = "Wither Door",
        minF = 1f,
        maxF = 10f
    )
    var witherDoorOutlineWidth = 3f

    @Property(
        name = "Door Outline Opacity",
        type = PropertyType.PERCENT_SLIDER,
        category = "Other Features",
        subcategory = "Wither Door"
    )
    var witherDoorOutline = 1f

    @Property(
        name = "Door Fill Opacity",
        type = PropertyType.PERCENT_SLIDER,
        category = "Other Features",
        subcategory = "Wither Door"
    )
    var witherDoorFill = 0.07f

    @Property(
        name = "Paul Score",
        type = PropertyType.SWITCH,
        description = "Assumes paul perk is active to give 10 bonus score.",
        category = "Score",
        subcategory = "Paul"
    )
    var paulBonus = false

    init {
        initialize()

        arrayOf(
            "mapCenter",
            "mapDynamicRotate"
        ).forEach { propertyName -> addDependency(propertyName, "mapRotate") }

        arrayOf(
            "runInformationScore",
            "runInformationSecrets",
            "runInformationCrypts",
            "runInformationMimic",
            "runInformationDeaths"
        ).forEach { propertyName -> addDependency(propertyName, "mapShowRunInformation") }

        arrayOf(
            "witherDoorNoKeyColor",
            "witherDoorKeyColor",
            "witherDoorOutlineWidth",
            "witherDoorOutline",
            "witherDoorFill"
        ).forEach { propertyName -> addDependency(propertyName, "witherDoorESP") }

        arrayOf(
            "scoreAssumeSpirit",
            "scoreMinimizedName",
            "scoreHideInBoss",

            "scoreTotalScore",
            "scoreSecrets",
            "scoreCrypts",
            "scoreMimic",
            "scoreDeaths",
            "scorePuzzles",

        ).forEach { propertyName -> addDependency(propertyName, "scoreElementEnabled") }

        addDependency("mimicMessage", "mimicMessageEnabled")
    }

    private object CategorySorting : SortingBehavior() {

        private val configCategories = listOf(
            "General", "Map", "Room", "Score", "Other Features"
        )

        private val configSubcategories = listOf(
            "Toggle", "Message", "Elements"
        )

        override fun getCategoryComparator(): Comparator<in Category> = compareBy { configCategories.indexOf(it.name) }

        override fun getSubcategoryComparator(): Comparator<in Map.Entry<String, List<PropertyData>>> =
            compareBy { configSubcategories.indexOf(it.key) }
    }
}
