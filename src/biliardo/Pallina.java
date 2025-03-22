package biliardo;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class Pallina {
    private int x, y, dir;
    private Color colore;
    private static final int raggio = 10;
    private boolean bianca;
    private int v;

    public Pallina(int x, int y, Color c, int direzione, boolean bianca) {
        this.x = x;
        this.y = y;
        this.colore = c;
        this.dir = direzione;
        this.bianca = bianca;
        v = 10;
    }

    public static int getRaggio() {
        return raggio;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
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

    public void muovi(int canvasHeight, int canvasWidth) {
        // movimento giocatore
        double rad = Math.toRadians(dir);
        double deltaX = Math.cos(rad) * v;
        double deltaY = Math.sin(rad) * v;
        x += deltaX;
        y += deltaY;

        // controllo collisioni
        if (y < 0 || y > canvasHeight - raggio * 2) {
            dir = -dir;
        }

        if (x < 0 || x > canvasWidth - raggio * 2) {
            dir = 180 - dir;
        }
    }
}