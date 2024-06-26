/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.execution.testframework.ui;

import com.intellij.ui.components.panels.NonOpaquePanel;
import org.jetbrains.annotations.ApiStatus;

import javax.swing.*;
import java.awt.*;

@ApiStatus.Internal
public class SameHeightPanel extends NonOpaquePanel {
  private final JComponent myOriginalHeightComponent;

  public SameHeightPanel(final LayoutManager layout, final JComponent originalHeightComponent) {
    super(layout);
    myOriginalHeightComponent = originalHeightComponent;
  }

  @Override
  public Dimension getPreferredSize() {
    final Dimension preferredSize = super.getPreferredSize();
    final int originalHeight = myOriginalHeightComponent.getPreferredSize().height;
    if (preferredSize.height > originalHeight)
      return preferredSize;
    return new Dimension(preferredSize.width, originalHeight);
  }

  public static SameHeightPanel wrap(final JComponent component, final JComponent original) {
    final SameHeightPanel panel = new SameHeightPanel(new BorderLayout(), original);
    panel.add(component, BorderLayout.CENTER);
    return panel;
  }
}
