// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.kotlin.idea.fir.findUsages

import org.jetbrains.kotlin.idea.base.test.IgnoreTests
import java.nio.file.Path
import java.nio.file.Paths


fun doTestWithFIRFlagsByPath(path: String, body: () -> Unit) =
    doTestWithFIRFlags(Paths.get(path), body)

fun doTestWithFIRFlags(testFile: Path, body: () -> Unit) {
    IgnoreTests.runTestIfNotDisabledByFileDirective(testFile, IgnoreTests.DIRECTIVES.IGNORE_K2) {
        body()
    }
}