// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.execution.junit.kotlin.codeInspection.deadCode

import org.jetbrains.kotlin.idea.base.plugin.KotlinPluginMode

class K1JUnit5ImplicitUsageProviderTest : KotlinJUnit5ImplicitUsageProviderTest() {
  override val pluginMode: KotlinPluginMode = KotlinPluginMode.K1
}