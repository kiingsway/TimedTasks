package org.example.controller;

import org.example.model.AppConstants;
import org.example.view.HomePage;
import org.example.view.components.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.example.model.AppConstants.*;
import static org.example.view.components.TimePicker.sumTimes;

public class HomeController {

  //private final List<JPanel> panels = new ArrayList<>();
  private final HomePage view;
  //private final JPanel panel = new JPanel();

  public HomeController(HomePage view) throws ParseException {
    this.view = view;

    newTaskPanel();
  }

  private void newTaskPanel() throws ParseException {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    Component[] inputs = getPanelFormInputs();

    c.gridx = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.CENTER;

    for (int i = 0; i < inputs.length; i++) {
      Component input = inputs[i];
      input.setFont(AppConstants.FONT_TEXTFIELD);

      if (input instanceof JTextField) {
        input.setPreferredSize(new Dimension(300, input.getPreferredSize().height));
      } else if (input instanceof JButton) {
        input.setFont(AppConstants.FONT_BUTTON);
        input.setPreferredSize(new Dimension(20, input.getPreferredSize().height));
      }

      c.weightx = i == COL_TITLE ? 4 : 1;
      panel.add(input, c);
      c.gridx++;
    }

    view.addItem(panel);
    handleActionAvailability();
  }

  private Component[] getPanelFormInputs() throws ParseException {
    JButton btnDown = new JButton("\\/");
    JButton btnUp = new JButton("/\\");
    JTextField txtTitle = new JTextField();
    TimePicker txtETA = new TimePicker();
    TimePicker txtStart = new TimePicker();
    TimePicker txtEnd = new TimePicker();
    JButton btnDel = new JButton("X");

    int index = view.getItems().size();

    boolean isFirstField = (index == 0);
    txtStart.setEnabled(isFirstField);
    txtStart.setFocusable(isFirstField);

    txtEnd.setEnabled(false);
    txtEnd.setFocusable(false);

    configureTimePickerEvents(txtETA, txtETA, txtStart, txtEnd);
    configureTimePickerEvents(txtStart, txtETA, txtStart, txtEnd);

    txtStart.addChangeListener(_ -> handleStartChange(index));
    txtEnd.addChangeListener(_ -> handleEndChange(index));

    btnDown.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        moveComponent(index, index - 1);
      }
    });
    btnUp.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        moveComponent(index, index + 1);
      }
    });
    btnDel.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        removeField(index);
      }
    });

    if (!isFirstField) {
      TimePicker lastTxtEnd = (TimePicker) getFieldsFromPanels().get(index - 1).get(COL_END);
      txtStart.setTime(lastTxtEnd.getTime());
      txtEnd.setTime(lastTxtEnd.getTime());
    }

    setAddTaskWhenNewTitleListener(txtTitle);

    return new Component[]{btnDown, btnUp, txtTitle, txtETA, txtStart, txtEnd, btnDel};
  }

  private void setAddTaskWhenNewTitleListener(JTextField txtTitle) {
    txtTitle.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        List<List<Component>> fields = getFieldsFromPanels();
        boolean isLastTask = IntStream.range(0, fields.size())
                .filter(i -> fields.get(i).contains(txtTitle))
                .findFirst()
                .orElse(-1) == fields.size() - 1;

        if (!txtTitle.getText().isEmpty() && isLastTask) {
          try {
            newTaskPanel();
          } catch (ParseException ex) {
            SHOW_ERROR_DIALOG(view, ex);
          }
        }
      }
    });
  }

  private void configureTimePickerEvents(TimePicker picker, TimePicker txtETA, TimePicker txtStart, TimePicker txtEnd) {
    // Método auxiliar para configurar eventos do TimePicker
    JFormattedTextField textField = picker.getTextField();
    if (textField != null) {
      Runnable updateValue = () -> timesUpdate(txtETA, txtStart, txtEnd);
      textField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
          updateTimePickerValue(picker, updateValue);
        }
      });
      textField.addActionListener(_ -> updateTimePickerValue(picker, updateValue));
    }
  }

  private void timesUpdate(TimePicker txtETA, TimePicker txtStart, TimePicker txtEnd) {
    String eta = txtETA.getTime();
    if (eta.isEmpty()) return;

    try {
      String newEnd = sumTimes(txtStart.getTime(), eta);
      txtEnd.setTime(newEnd);
//      panel.revalidate();
//      panel.repaint();
    } catch (ParseException ex) {
      SHOW_ERROR_DIALOG(view, ex);
    }
  }

  private void updateTimePickerValue(TimePicker tp, Runnable r) {
    try {
      tp.commitEdit();
      r.run();
    } catch (ParseException ex) {
      SHOW_ERROR_DIALOG(view, ex);
    }
  }

  private void handleStartChange(int index) {
    try {
      List<List<Component>> fields = getFieldsFromPanels();
      if (fields.isEmpty() || index >= fields.size() - 1) return;
      TimePicker txtStart = (TimePicker) fields.get(index).get(COL_START);
      TimePicker txtEnd = (TimePicker) fields.get(index).get(COL_END);
      TimePicker nextTxtETA = (TimePicker) fields.get(index + 1).get(COL_ETA);
      TimePicker nextTxtStart = (TimePicker) fields.get(index + 1).get(COL_START);
      TimePicker nextTxtEnd = (TimePicker) fields.get(index + 1).get(COL_END);

      String end = sumTimes(txtStart.getTime(), txtEnd.getTime());
      txtEnd.setTime(end);
      nextTxtStart.setTime(end);
      nextTxtEnd.setTime(sumTimes(nextTxtETA.getTime(), end));
    } catch (Exception e) {
      SHOW_ERROR_DIALOG(view, e);
    }
  }

  private void handleEndChange(int index) {
    try {
      List<List<Component>> fields = getFieldsFromPanels();
      if (fields.isEmpty() || index >= fields.size() - 1) return;
      TimePicker txtEnd = (TimePicker) fields.get(index).get(COL_END);
      TimePicker nextTxtETA = (TimePicker) fields.get(index + 1).get(COL_ETA);
      TimePicker nextTxtStart = (TimePicker) fields.get(index + 1).get(COL_START);
      TimePicker nextTxtEnd = (TimePicker) fields.get(index + 1).get(COL_END);

      nextTxtStart.setTime(txtEnd.getTime());
      nextTxtEnd.setTime(sumTimes(nextTxtETA.getTime(), nextTxtStart.getTime()));
    } catch (Exception e) {
      SHOW_ERROR_DIALOG(view, e);
    }
  }

  private void updateAllItems() {
    List<List<Component>> fields = getFieldsFromPanels();
    for (int i = 0; i < fields.size(); i++) {
      List<Component> inputs = fields.get(i);
      JButton btnUp = (JButton) inputs.get(COL_UP);
      JButton btnDown = (JButton) inputs.get(COL_DOWN);
      JButton btnDel = (JButton) inputs.get(COL_DEL);

      btnUp.setEnabled(i > 0);
      btnDown.setEnabled(i < fields.size() - 1);
      btnDel.setEnabled(fields.size() > 1);

      handleStartChange(i);
    }

    view.revalidate();
    view.repaint();
  }

  private void handleActionAvailability() {
    List<List<Component>> fields = getFieldsFromPanels();
    for (int i = 0; i < fields.size(); i++) {
      List<Component> inputs = fields.get(i);
      JButton btnUp = (JButton) inputs.get(COL_UP);
      JButton btnDown = (JButton) inputs.get(COL_DOWN);
      JButton btnDel = (JButton) inputs.get(COL_DEL);

      btnUp.setEnabled(i > 0);
      btnDown.setEnabled(i < fields.size() - 1);
      btnDel.setEnabled(fields.size() > 1);

      handleStartChange(i);
    }

    view.revalidate();
    view.repaint();
  }

  public void moveComponent(int fromIndex, int toIndex) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    String msg = "Implementing...";
    String tit = methodName + "(" + fromIndex + ", " + toIndex + ")";
    JOptionPane.showMessageDialog(view, msg, tit, JOptionPane.INFORMATION_MESSAGE);

    /*Component[] components = view.getComponents();

    if (fromIndex < 0 || fromIndex >= components.length || toIndex < 0 || toIndex >= components.length) {
      return; // Índices inválidos
    }

    Component movingComponent = components[fromIndex];

    panel.remove(fromIndex);  // Remove da posição original
    panel.add(movingComponent, toIndex);  // Adiciona na nova posição

    panel.revalidate();  // Atualiza o layout
    panel.repaint();*/
  }

  private void removeField(int index) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    String msg = "Implementing...";
    String tit = methodName + "(" + index + ")";
    JOptionPane.showMessageDialog(view, msg, tit, JOptionPane.INFORMATION_MESSAGE);

    view.removeItem(index);
    handleStartChange(index);
    handleActionAvailability();
  }

  private List<List<Component>> getFieldsFromPanels() {
    List<List<Component>> fieldsFromPanels = new ArrayList<>();
    for (JPanel panel : view.getItems()) {
      fieldsFromPanels.add(Arrays.asList(panel.getComponents()));
    }
    return fieldsFromPanels;
  }

}