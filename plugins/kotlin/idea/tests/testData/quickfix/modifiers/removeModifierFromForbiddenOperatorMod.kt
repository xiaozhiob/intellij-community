// "Remove 'operator' modifier" "true"
// COMPILER_ARGUMENTS: -XXLanguage:+ProhibitOperatorMod

object A {
    operator<caret> fun mod(x: Int) {}
}
// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.RemoveModifierFixBase
// FUS_K2_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.RemoveModifierFixBase