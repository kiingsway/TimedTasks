package org.example.view;

import javax.swing.*;
import java.awt.*;

public class Test extends JFrame {
  private final JPanel contentPanel; // Painel que contém os componentes
  private int buttonCount = 1;

  public Test() {
    setTitle("Adicionar Itens ao ScrollPane");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Painel principal que usa GridBagLayout
    contentPanel = new JPanel(new GridBagLayout());

    // Criando um JScrollPane para envolver o painel
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    // Botão para adicionar novos itens ao painel dentro do ScrollPane
    JButton addButton = new JButton("Adicionar Botão");
    addButton.addActionListener(_ -> addNewButton());

    // Layout do JFrame
    setLayout(new BorderLayout());
    add(addButton, BorderLayout.NORTH); // Botão no topo
    add(scrollPane, BorderLayout.CENTER); // ScrollPane no centro

    setVisible(true);
  }

  private void addNewButton() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = GridBagConstraints.RELATIVE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.insets = new Insets(5, 5, 5, 5);

    // Criar um novo botão e adicioná-lo ao painel
    JButton newButton = new JButton("Botão " + buttonCount++);
    contentPanel.add(newButton, gbc);

    // Atualizar a UI
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(Test::new);
  }
}

