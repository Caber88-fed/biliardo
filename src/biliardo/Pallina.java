package biliardo;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class Pallina {
    private int x, y, direzione;
    private Color colore;
    private static final int raggio = 10;
    private boolean bianca;

    public Pallina(int x, int y, Color c, int direzione, boolean bianca) {
        this.x = x;
        this.y = y;
        this.colore = c;
        this.direzione = direzione;
        this.bianca = bianca;
    }

    public static int getRaggio() {
        return raggio;
    }

    public int getDirezione() {
        return direzione;
    }

    public void setDirezione(int direzione) {
        this.direzione = direzione;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColore() {
        return colore;
    }

    public void setColore(Color colore) {
        this.colore = colore;
    }

    public void disegna(GC penna) {
        penna.setBackground(colore);
        penna.fillOval(x, y, raggio * 2, raggio * 2);
        if (bianca) {
            penna.setBackground(new Color(Display.getCurrent(), new RGB(255, 255, 255)));
            penna.fillOval(x+raggio/2, y+raggio/2, raggio, raggio);
        }
    }
}
