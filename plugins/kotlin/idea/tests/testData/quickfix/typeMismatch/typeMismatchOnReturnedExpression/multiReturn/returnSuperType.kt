// "Change return type of enclosing function 'test' to 'CharSequence'" "true"
fun test(x: CharSequence) {
    if (true) return "foo"<caret>
    return x
}

// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.ChangeCallableReturnTypeFix$ForEnclosing
// FUS_K2_QUICKFIX_NAME: org.jetbrains.kotlin.idea.k2.codeinsight.fixes.ChangeTypeQuickFixFactories$UpdateTypeQuickFix