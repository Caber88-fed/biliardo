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
    private int direzione;
    private int v;

    private double vx;
    private double vy;

    public Pallina(int x, int y, Color c, boolean bianca) {
        this.x = x;
        this.y = y;
        this.colore = c;
        this.direzione = 0;
        this.vx = 0;
        this.vy = 0;
        this.bianca = bianca;
        this.xinit = x;
        this.yinit = y;
        this.v = 0;
    }

    public static int getRaggio() {
        return raggio;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double distanza(Pallina p) {
        double dx = this.x - p.x;
        double dy = this.y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    //Rotazione vettore velocità
    private double[] rotazione(double[] velocita, double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        return new double[]{
                velocita[0] * cosTheta - velocita[1] * sinTheta,
                velocita[0] * sinTheta + velocita[1] * cosTheta
        };
    }

    public void update(int canvasWidth, int canvasHeight, Pallina[] p, int inizio, int r) {
        for (int i = inizio + 1; i < p.length; i++) {
            Pallina p1 = p[i];
            if (this.distanza(p1) < r + r) {
                double[] res = {this.vx - p1.vx, this.vy - p1.vy};
                if (res[0] * (p1.x - this.x) + res[1] * (p1.y - this.y) >= 0) {

                    // angolo di collisione
                    double theta = -Math.atan2(p1.y - this.y, p1.x - this.x);

                    // rotazione velocità di riferimento della collisione
                    double[] u1 = rotazione(new double[]{this.vx, this.vy}, theta);
                    double[] u2 = rotazione(new double[]{p1.vx, p1.vy}, theta);

                    // collisione elastica
                    double[] v1 = rotazione(new double[]{(u1[0] * (0) + 2 * 1 * u2[0]) / (1 + 1), u1[1]}, -theta);

                    double[] v2 = rotazione(new double[]{(u2[0] * (0) + 2 * 1 * u1[0]) / (1 + 1), u2[1]}, -theta);

                    // Aggiorna le velocità
                    this.vx = v1[0];
                    this.vy = v1[1];
                    p1.vx = v2[0];
                    p1.vy = v2[1];
                }
            }
        }

        //collisioni con i bordi del canvas
        if (this.x - r <= 0) {
            this.x = r;
            this.vx = -this.vx;
        }
        if (this.x + r >= canvasWidth) {
            this.x = canvasWidth - r;
            this.vx = -this.vx;
        }
        if (this.y - r <= 0) {
            this.y = r;
            this.vy = -this.vy;
        }
        if (this.y + r >= canvasHeight) {
            this.y = canvasHeight - r;
            this.vy = -this.vy;
        }

        //Aggiornamento della posizione
        this.x += this.vx;
        this.y += this.vy;
        this.vx /= 1.05;
        this.vy /= 1.05;
        if (Math.abs(this.vx) < 0.1) this.vx = 0;
        if (Math.abs(this.vy) < 0.1) this.vy = 0;
    }

    public void reset() {
        x = xinit;
        y = yinit;
        v = 0;
        direzione = 0;
        vx = 0;
        vy = 0;
    }

    public void disegna(GC penna) {
        penna.setBackground(colore);
        penna.fillOval(x, y, raggio * 2, raggio * 2);
        if (bianca) {
            penna.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            penna.fillOval(x + raggio / 2, y + raggio / 2, raggio, raggio);
        }
    }

    public void muovi(int canvasHeight, int canvasWidth) {
        // movimento giocatore
        double rad = Math.toRadians(direzione);
        double deltaX = Math.cos(rad) * v;
        double deltaY = Math.sin(rad) * v;
        x += deltaX;
        y += deltaY;

        // controllo collisioni
        if (y < 0 || y > canvasHeight - raggio * 2) {
            direzione = -direzione;
        }

        if (x < 0 || x > canvasWidth - raggio * 2) {
            direzione = 180 - direzione;
        }

        v *= 0.92;
    }


    public void setVelocita(int v) {
        this.v = v;
    }

    public int getVelocita() {
        return v;
    }

    public void setDir(int dir) {
        this.direzione = dir;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }
}