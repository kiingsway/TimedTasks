package org.example;

import org.example.controller.HomeController;
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

    try {
      HomePage home = new HomePage();
      new HomeController(home);
      mainPanel.add(home, "home");
    } catch (ParseException e) {
      SHOW_ERROR_DIALOG(this, e);
    }
    add(mainPanel);
  }
}

