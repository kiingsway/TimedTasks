package org.example.model;

import javax.swing.*;
import java.awt.*;

public class AppConstants {
  public static final String APP_TITLE = "Timed Tasks";
  public static final int APP_WIDTH = 800;
  public static final int APP_HEIGHT = 600;

  public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 12);
  public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
  public static final Font FONT_TEXTFIELD = new Font("Roboto", Font.PLAIN, 18);

  public static final int COL_UP = 0;
  public static final int COL_DOWN = 1;
  public static final int COL_TITLE = 2;
  public static final int COL_ETA = 3;
  public static final int COL_START = 4;
  public static final int COL_END = 5;
  public static final int COL_DEL = 6;

  public static void SHOW_ERROR_DIALOG(Component view, Exception e) {
    String errorType = e.getClass().getSimpleName();
    String title = "ERROR - " + errorType;
    JOptionPane.showMessageDialog(view, e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
  }
}
