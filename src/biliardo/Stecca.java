package biliardo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

public class Stecca {
    private static final Color colore = new Color(199, 171, 130);
    private final int dMin = Pallina.getRaggio() * 2;
    private final int dMax = dMin + 100;
    private final int spessore = 6; // divisibile per 2
    private final int lunghezza = 140;

    private int rotazione;
    private int distanza;
    private boolean nascosto;
    private boolean anim;

    private final Pallina pAssociata;
    private int push;

    public Stecca(Pallina pAssociata) {
        rotazione = 0;
        distanza = dMin;
        nascosto = false;
        anim = false;
        this.pAssociata = pAssociata;
        push = 0;
    }

    public int getRotazione() {
        return rotazione;
    }

    public void setRotazione(int rotazione) {
        this.rotazione = rotazione;
    }

    public void setDistanza(int d) {
        this.distanza = Math.min(dMax, Math.max(dMin, d));
    }

    public void disegna(GC penna, Transform trOg) {
        if (!nascosto) {
            if (anim) colpisci();
            Transform tr = new Transform(penna.getDevice());
            penna.getTransform(tr);
            tr.translate(pAssociata.getX() + Pallina.getRaggio(), pAssociata.getY() + Pallina.getRaggio());
            tr.rotate(rotazione);
            penna.setTransform(tr);
            penna.setBackground(colore);
            penna.fillRectangle(distanza, -(spessore / 2), lunghezza, spessore);
            penna.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            penna.fillRectangle(distanza, -(spessore / 2), spessore, spessore);
            penna.setTransform(trOg);
            tr.dispose();
        } else if (pAssociata.getVx() == 0 && pAssociata.getVy() == 0 && !pAssociata.isNascosto()) {
            distanza = dMin;
            nascosto = false;
        }
    }
    
    public void colpisci() {
        if (!anim) {
            anim = true;
            push = 0;
        }
        if (distanza < Pallina.getRaggio()*2) {
            anim = false;
            nascosto = true;
            pAssociata.setVx(Math.cos(Math.toRadians(180+rotazione))*(push*5));
            pAssociata.setVy(Math.sin(Math.toRadians(180+rotazione))*(push*5));
            return;
        }
        distanza -= 20;
        push++;
    }

    public boolean isAnim() {
        return anim;
    }

    public boolean isNascosto() {
        return nascosto;
    }
}
