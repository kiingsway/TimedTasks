package org.example.view.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimePicker extends JSpinner {

  private final SimpleDateFormat utcFormatter;
  private final SimpleDateFormat inputFormatter = new SimpleDateFormat("HHmm"); // Formato da entrada
  private final SimpleDateFormat displayFormatter; // Formato do display

  public DateTimePicker(String dateFormatPattern) {
    SpinnerDateModel model = new SpinnerDateModel();
    setModel(model);

    displayFormatter = new SimpleDateFormat(dateFormatPattern);

    // Define o editor com o formato desejado
    DateEditor editor = new DateEditor(this, dateFormatPattern);
    setEditor(editor);

    // Formatter para UTC (GMT 0)
    utcFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

    removeIncrementButton();
    formatUserInput();

    ((DefaultEditor) getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
  }

  private void removeIncrementButton() {
    JComponent editor = getEditor();
    if (editor instanceof DefaultEditor) {
      for (Component comp : getComponents()) if (comp instanceof JButton) remove(comp);
    }
  }

  private void formatUserInput() {
    JTextField textField = ((DefaultEditor) getEditor()).getTextField();
    textField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        formatTime();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        formatTime();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        formatTime();
      }

      private void formatTime() {
        SwingUtilities.invokeLater(() -> {
          String input = textField.getText().replaceAll("[^0-9]", ""); // Remove caracteres não numéricos
          if (input.length() == 4) { // Apenas formata se tiver 4 dígitos
            try {
              Date parsedDate = inputFormatter.parse(input);
              textField.setText(displayFormatter.format(parsedDate)); // Atualiza o campo formatado
              getModel().setValue(parsedDate); // Atualiza o valor interno
            } catch (ParseException ex) {
              ex.printStackTrace();
            }
          }
        });
      }
    });
  }

  public void setValue(String valueString) throws Exception {
    try {
      // Recebe a String UTC e converte para o horário local
      Date utcDate = utcFormatter.parse(valueString);
      getModel().setValue(utcDate);
    } catch (ParseException e) {
      String msg = "Error setting value: " + valueString + ". Error offset: " + e.getErrorOffset();
      throw new Exception(msg);
    }
  }

  public String value() {
    // Retorna a data como String no formato GMT 0
    Date localDate = (Date) getModel().getValue();
    return utcFormatter.format(localDate); // Converte de local para GMT 0
  }
}
