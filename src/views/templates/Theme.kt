package fan.zheyuan.ktorexposed.views.templates

enum class ThemeColor(val color: String) {
    PINK("default"),
    ORANGE("orange"),
    BLUE("blue"),
    GREEN("green"),
    RED("red"),
    INDIGO("indigo"),
    YELLOW("yellow"),
    TEAL("teal"),
    CYAN("cyan"),
    PURPLE("purple")
}
val total by lazy {
    ThemeColor.values().size
}