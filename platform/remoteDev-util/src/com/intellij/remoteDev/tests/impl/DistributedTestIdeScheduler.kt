package com.intellij.remoteDev.tests.impl

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.application.ModalityState
import com.intellij.util.application
import com.jetbrains.rd.util.reactive.ExecutionOrder
import com.jetbrains.rd.util.reactive.IScheduler
import org.jetbrains.annotations.ApiStatus
import javax.swing.SwingUtilities

@ApiStatus.Internal
object DistributedTestIdeScheduler : IScheduler {
  override val executionOrder: ExecutionOrder
    get() = ExecutionOrder.Sequential

  override val isActive: Boolean
    get() = SwingUtilities.isEventDispatchThread()

  override fun flush() {
    IdeEventQueue.getInstance().flushQueue()
  }

  override fun queue(action: () -> Unit) {
    application.invokeLater(action, ModalityState.any())
  }
}