package fr.sdecout.handson.rest.book

@JvmInline
value class Isbn(private val value: String) {

    val compressedValue: String get() = value.replace("-", "")
    val formattedValue: String
        get() = "${compressedValue.substring(0, 3)}-${
            compressedValue.substring(
                3,
                4
            )
        }-${compressedValue.substring(4, 6)}-${compressedValue.substring(6, 12)}-${compressedValue.substring(12, 13)}"

    init {
        require(compressedValue.matches(Regex("[0-9]{13}"))) { "ISBN must contain 13 numeric digits" }
    }

    override fun toString() = formattedValue

}
