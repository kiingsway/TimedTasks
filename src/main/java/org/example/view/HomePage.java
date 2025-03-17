package org.example.view;

import org.example.model.AppConstants;

import javax.swing.*;
import java.awt.*;

import static org.example.model.AppConstants.COL_TITLE;

public class HomePage extends JPanel {

  private final GridBagConstraints gbc = new GridBagConstraints();

  public HomePage() {
    setLayout(new GridBagLayout());

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.NORTHWEST;

    renderHeaders();
    gbc.insets.top = 5;
  }

  private void renderHeaders() {
    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    JLabel[] labels = {
            new JLabel(""),
            new JLabel(""),
            new JLabel("Title"),
            new JLabel("ETA"),
            new JLabel("Start"),
            new JLabel("End"),
            new JLabel(""),
    };

    c.gridx = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.CENTER;


    for (int i = 0; i < labels.length; i++) {
      JLabel label = labels[i];
      label.setHorizontalAlignment(SwingConstants.CENTER);
      label.setFont(AppConstants.FONT_LABEL);

      c.weightx = i == COL_TITLE ? 4 : 1;
      headerPanel.add(label, c);
      c.gridx++;
    }

    add(headerPanel, gbc);
  }

  public GridBagConstraints gbc() {
    return gbc;
  }

}
