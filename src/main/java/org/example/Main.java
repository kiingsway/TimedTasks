package org.example;

import org.example.model.AppConstants;
import org.example.view.HomePage;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

import static org.example.model.AppConstants.SHOW_ERROR_DIALOG;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      App app = new App();
      app.setVisible(true);
    });
  }
}

class App extends JFrame {

  public App() {
    setTitle(AppConstants.APP_TITLE);
    setSize(AppConstants.APP_WIDTH, AppConstants.APP_HEIGHT);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    JPanel home = null;
    try {
      home = new HomePage();
    } catch (ParseException e) {
      SHOW_ERROR_DIALOG(this, e);
    }

    // Adicionando os "cards" ao painel principal
    mainPanel.add(home, "home");

    // Ação dos botões para mudar de tela
    //btnGoHome.addActionListener(e -> cardLayout.show(mainPanel, "home"));

    home.setVisible(true);
    add(mainPanel);
  }
}

