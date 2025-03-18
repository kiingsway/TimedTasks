package org.example.view;

import org.example.model.AppConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.model.AppConstants.COL_TITLE;

public class HomePage extends JPanel {

  private final GridBagConstraints gbc = new GridBagConstraints();
  private final JPanel contentPanel = new JPanel(new GridBagLayout());

  public HomePage() {
    setLayout(new GridBagLayout());

    // Configuração inicial do GridBagConstraints
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.NORTHWEST;

    // Renderiza os cabeçalhos das colunas
    renderHeaders();

    // Configuração do painel de conteúdo e JScrollPane
    contentPanel.setLayout(new GridBagLayout());

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setPreferredSize(new Dimension(800, 400)); // Ajustável conforme necessário

    // Adicionando JScrollPane ao layout
    gbc.gridy++;
    gbc.weighty = 1; // Faz o scrollPane ocupar o espaço disponível
    gbc.fill = GridBagConstraints.BOTH;
    add(scrollPane, gbc);
  }

  private void renderHeaders() {
    JPanel headerPanel = new JPanel(new GridBagLayout());
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

  public List<JPanel> getItems() {
    List<JPanel> items = new ArrayList<>();
    for (Component component : contentPanel.getComponents()) {
      if (component instanceof JPanel) items.add((JPanel) component);
    }
    return items;
  }

  public void addItem(JPanel itemPanel) {
    GridBagConstraints itemConstraints = new GridBagConstraints();
    itemConstraints.gridx = 0;
    itemConstraints.gridy = contentPanel.getComponentCount(); // Adiciona na última posição
    itemConstraints.weightx = 1;
    itemConstraints.fill = GridBagConstraints.HORIZONTAL;
    contentPanel.add(itemPanel, itemConstraints);
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  // Método para remover um item do painel
  public void removeItem(int index) {
    contentPanel.remove(index);
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  // Método para mover um item dentro do GridBagLayout
  public void moveItem(JPanel itemPanel, int currentPos, int newPos) {
    // Remover o componente do painel
    contentPanel.remove(itemPanel);

    // Ajustar a posição do GridBagConstraints com base na nova posição
    GridBagConstraints newConstraints = new GridBagConstraints();
    newConstraints.gridx = 0;
    newConstraints.gridy = newPos; // Nova posição
    newConstraints.weightx = 1;
    newConstraints.fill = GridBagConstraints.HORIZONTAL;

    // Adicionar o item na nova posição
    contentPanel.add(itemPanel, newConstraints);
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  public GridBagConstraints gbc() {
    return gbc;
  }
}
