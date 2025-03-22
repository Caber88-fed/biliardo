package biliardo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class Pallina {
    private static final int raggio = 10;
    private final Color colore;
    private final boolean bianca;
    private final int xinit;
    private final int yinit;

    private int x;
    private int y;
    private int dir;
    private int v;

    public Pallina(int x, int y, Color c, boolean bianca) {
        this.x = x;
        this.y = y;
        this.colore = c;
        this.bianca = bianca;
        this.dir = 0;
        this.v = 0;
        this.xinit = x;
        this.yinit = y;
    }

    public static int getRaggio() {
        return raggio;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void disegna(GC penna) {
        penna.setBackground(colore);
        penna.fillOval(x, y, raggio * 2, raggio * 2);
        if (bianca) {
            penna.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
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

        v *= 0.92;
    }

    public void reset() {
        x = xinit;
        y = yinit;
        dir = 0;
        v = 0;
    }

    public void setVelocita(int v) {
        this.v = v;
    }

    public int getVelocita() {
        return v;
    }
}