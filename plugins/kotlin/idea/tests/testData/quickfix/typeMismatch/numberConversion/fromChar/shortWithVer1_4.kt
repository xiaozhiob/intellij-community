// "Convert expression to 'Short'" "true"
// API_VERSION: 1.4
fun short(x: Short) {}

fun test(c: Char) {
    short(<caret>c)
}
// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.NumberConversionFix
// FUS_K2_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.NumberConversionFix