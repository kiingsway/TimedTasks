package org.example.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AppButton extends JButton {

  public AppButton(String text) {
    super(text);
    setText(text);

    // Definindo o fundo transparente e sem bordas
    setContentAreaFilled(false);
    setBorderPainted(false);
    setFocusPainted(false);

    // Adicionando listeners para mudança de cor
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        // Escurece quando o mouse passa sobre o botão
        setBackground(getDarkenedColor(getBackground(), 0.2f));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        // Volta à cor original
        setBackground(null);
      }

      @Override
      public void mousePressed(MouseEvent e) {
        // Escurece ainda mais quando clicado
        setBackground(getDarkenedColor(getBackground(), 0.4f));
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        // Volta à cor original após o clique
        setBackground(getDarkenedColor(getBackground(), 0.2f));
      }
    });

    // Definir a cor inicial do botão
    setBackground(Color.LIGHT_GRAY);  // Cor inicial do botão
  }

  // Função para escurecer uma cor
  private Color getDarkenedColor(Color color, float factor) {
    int red = (int) (color.getRed() * (1 - factor));
    int green = (int) (color.getGreen() * (1 - factor));
    int blue = (int) (color.getBlue() * (1 - factor));
    return new Color(red, green, blue);
  }

  // Sobrescrevendo o método paintComponent para garantir que o fundo seja desenhado corretamente
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g); // Garante que o texto seja desenhado

    if (getModel().isPressed()) {
      g.setColor(getDarkenedColor(getBackground(), 0.4f)); // Mais escuro quando clicado
    } else if (getModel().isRollover()) {
      g.setColor(getDarkenedColor(getBackground(), 0.2f)); // Um pouco mais escuro no hover
    } else {
      g.setColor(getBackground());
    }
    g.fillRect(0, 0, getWidth(), getHeight());  // Desenha o fundo

    // Garanta que o texto seja desenhado com a cor correta (preto ou outra cor)
    g.setColor(getForeground());
    FontMetrics fontMetrics = g.getFontMetrics();
    int textWidth = fontMetrics.stringWidth(getText());
    int textHeight = fontMetrics.getHeight();
    int x = (getWidth() - textWidth) / 2;
    int y = (getHeight() + textHeight) / 2 - fontMetrics.getDescent();
    g.drawString(getText(), x, y);  // Desenha o texto centralizado
  }
}
