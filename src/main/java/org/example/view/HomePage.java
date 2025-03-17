package org.example.view;

import org.example.model.AppConstants;
import org.example.view.components.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.example.model.AppConstants.SHOW_ERROR_DIALOG;
import static org.example.view.components.TimePicker.sumTimes;

public class HomePage extends JPanel {

  private final List<Component[]> fields = new ArrayList<>();
  private final GridBagConstraints gbc = new GridBagConstraints();

  public HomePage() throws ParseException {
    setLayout(new GridBagLayout());

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.NORTHWEST;

    renderHeaders();
    gbc.insets.top = 5;
    addNewTask();
  }

  private void addNewTask() throws ParseException {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    Component[] inputs = getInputs();

    fields.add(inputs);

    c.gridx = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.CENTER;

    for (int i = 0; i < inputs.length; i++) {
      Component input = inputs[i];
      input.setFont(AppConstants.FONT_TEXTFIELD);

      input.setPreferredSize(new Dimension(300, input.getPreferredSize().height));

      c.weightx = i == 0 ? 4 : 1;
      panel.add(input, c);
      c.gridx++;
    }

    gbc.gridy++;
    add(panel, gbc);
    revalidate();
    repaint();
  }

  private Component[] getInputs() throws ParseException {
    JTextField txtTitle = new JTextField();
    TimePicker txtETA = new TimePicker();
    TimePicker txtStart = new TimePicker();
    TimePicker txtEnd = new TimePicker();

    int index = fields.size();

    boolean isFirstField = (index == 0);
    txtStart.setEnabled(isFirstField);
    txtStart.setFocusable(isFirstField);

    txtEnd.setEnabled(false);
    txtEnd.setFocusable(false);

    // Configuração de eventos dos TimePickers
    configureTimePickerEvents(txtETA, txtETA, txtStart, txtEnd);
    configureTimePickerEvents(txtStart, txtETA, txtStart, txtEnd);
    txtEnd.addChangeListener(_ -> {
      // Verifica se o índice é válido antes de tentar acessar o próximo campo
      if (index + 1 < fields.size()) {
        TimePicker nextTxtETA = (TimePicker) fields.get(index + 1)[1];
        TimePicker nextTxtStart = (TimePicker) fields.get(index + 1)[2];
        TimePicker nextTxtEnd = (TimePicker) fields.get(index + 1)[3];

        timesUpdate(nextTxtETA, nextTxtStart, nextTxtEnd);

        // Verifica se o próximo TimePicker está presente
        if (nextTxtStart != null) {
          try {
            nextTxtStart.setTime(txtEnd.getTime());
          } catch (ParseException e) {
            SHOW_ERROR_DIALOG(this, e);
          }
        }
      }
    });

    // Adiciona KeyListener ao txtTitle para detectar novas tarefas
    txtTitle.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (!txtTitle.getText().isEmpty() && isLastTask(txtTitle)) {
          try {
            addNewTask();
          } catch (ParseException ex) {
            SHOW_ERROR_DIALOG(HomePage.this, ex);
          }
        }
      }
    });

    timesUpdate(txtETA, txtStart, txtEnd);

    if (!isFirstField) {
      TimePicker lastTxtEnd = (TimePicker) fields.get(index - 1)[3];
      System.out.println("lastTxtEnd: " + lastTxtEnd.getTime());
      txtStart.setTime(lastTxtEnd.getTime());
    }

    return new Component[]{txtTitle, txtETA, txtStart, txtEnd};
  }

  // Método auxiliar para configurar eventos do TimePicker
  private void configureTimePickerEvents(TimePicker picker, TimePicker txtETA, TimePicker txtStart, TimePicker txtEnd) {
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

  // Método auxiliar para verificar se o txtTitle está na última posição da lista
  private boolean isLastTask(JTextField txtTitle) {
    return IntStream.range(0, fields.size())
            .filter(i -> Arrays.asList(fields.get(i)).contains(txtTitle))
            .findFirst()
            .orElse(-1) == fields.size() - 1;
  }

  private void timesUpdate(TimePicker txtETA, TimePicker txtStart, TimePicker txtEnd) {
    String eta = txtETA.getTime();
    if (eta.isEmpty()) return;

    try {
      String newEnd = sumTimes(txtStart.getTime(), eta);
      txtEnd.setTime(newEnd);
      revalidate();
      repaint();
    } catch (ParseException ex) {
      SHOW_ERROR_DIALOG(HomePage.this, ex);
    }
  }

  private void updateTimePickerValue(TimePicker tp, Runnable r) {
    try {
      tp.commitEdit();
      r.run();
    } catch (ParseException ex) {
      SHOW_ERROR_DIALOG(HomePage.this, ex);
    }
  }

  private void renderHeaders() {
    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    JLabel[] labels = {
            new JLabel("Title"),
            new JLabel("ETA"),
            new JLabel("Start"),
            new JLabel("End")
    };

    c.gridx = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.CENTER;


    for (int i = 0; i < labels.length; i++) {
      JLabel label = labels[i];
      label.setHorizontalAlignment(SwingConstants.CENTER);
      label.setFont(AppConstants.FONT_LABEL);

      c.weightx = i == 0 ? 4 : 1;
      headerPanel.add(label, c);
      c.gridx++;
    }

    add(headerPanel, gbc);
  }

}
