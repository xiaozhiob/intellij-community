// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package com.intellij.history.integration.ui.views;

import com.intellij.history.core.LocalHistoryFacade;
import com.intellij.history.integration.IdeaGateway;
import com.intellij.history.integration.LocalHistoryBundle;
import com.intellij.history.integration.ui.models.DirectoryHistoryDialogModel;
import com.intellij.history.integration.ui.models.RecentChange;
import com.intellij.history.integration.ui.models.RecentChangeDialogModel;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@ApiStatus.Internal
public final class RecentChangeDialog extends DirectoryHistoryDialog {
  private final RecentChange myChange;

  public RecentChangeDialog(Project p, IdeaGateway gw, RecentChange c) {
    super(p, gw, null, false);
    myChange = c;
    init();
  }

  @Override
  protected DirectoryHistoryDialogModel createModel(LocalHistoryFacade vcs) {
    return new RecentChangeDialogModel(myProject, myGateway, vcs, myChange);
  }

  @Override
  protected @NotNull JComponent createComponent() {
    JPanel result = new JPanel(new BorderLayout());
    result.add(super.createComponent(), BorderLayout.CENTER);
    result.add(createButtonsPanel(), BorderLayout.SOUTH);
    return result;
  }

  @Override
  protected boolean showRevisionsList() {
    return false;
  }

  @Override
  protected boolean showSearchField() {
    return false;
  }

  private @NotNull JPanel createButtonsPanel() {
    AbstractAction revert = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        revert();
        close();
      }
    };
    UIUtil.setActionNameAndMnemonic(LocalHistoryBundle.message("action.revert"), revert);

    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
    p.add(Box.createHorizontalGlue());
    p.add(new JButton(revert));
    return p;
  }

  @Override
  protected @NotNull String getHelpId() {
    return "reference.dialogs.recentChanges";
  }
}
