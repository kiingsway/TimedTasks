package org.example.view.components;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePicker extends JSpinner {

  private final SimpleDateFormat utcFormatter;

  public TimePicker() {
    super(new SpinnerDateModel());

    String pattern = "HH:mm";
    setEditor(new JSpinner.DateEditor(this, pattern));
    utcFormatter = new SimpleDateFormat(pattern);
    removeIncrementButton();

    // Define o valor inicial como "00:00"
    setInitialTime();
  }

  // Define o valor inicial como "00:00"
  private void setInitialTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0); // Define a hora como 00
    calendar.set(Calendar.MINUTE, 0); // Define o minuto como 00
    calendar.set(Calendar.SECOND, 0); // Opcional, para garantir que os segundos sejam 0

    Date initialTime = calendar.getTime();
    setValue(initialTime); // Define o valor inicial
  }

  private void removeIncrementButton() {
    JComponent editor = getEditor();
    if (editor instanceof DefaultEditor) {
      for (Component comp : getComponents()) {
        if (comp instanceof JButton) remove(comp);
      }
    }
  }

  public void setTime(String timeString) throws ParseException {
    // Converte a String UTC para um Date
    Date utcDate = utcFormatter.parse(timeString);
    setValue(utcDate);

    // Força o editor a atualizar a interface
    JFormattedTextField textField = getTextField();
    if (textField != null) textField.commitEdit();
  }

  public String getTime() {
    // Retorna a hora como String no formato GMT 0
    Date localDate = (Date) getValue();
    return utcFormatter.format(localDate);
  }

  public static String sumTimes(String time1, String time2) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    // Converte as Strings para Date
    Date date1 = sdf.parse(time1);
    Date date2 = sdf.parse(time2);

    // Usa Calendar para somar os tempos
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date1);

    Calendar calendar2 = Calendar.getInstance();
    calendar2.setTime(date2);

    calendar.add(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY));
    calendar.add(Calendar.MINUTE, calendar2.get(Calendar.MINUTE));

    return sdf.format(calendar.getTime());
  }

  // Método para obter o campo de texto do editor (para adicionar eventos externamente)
  public JFormattedTextField getTextField() {
    JComponent editor = getEditor();
    if (editor instanceof JSpinner.DefaultEditor) {
      return ((JSpinner.DefaultEditor) editor).getTextField();
    }
    return null;
  }
}
