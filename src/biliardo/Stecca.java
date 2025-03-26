package biliardo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

public class Stecca {
    private Color colore = new Color(199, 171, 130);
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
    
    
    public void setNascosto(boolean nascosto) {
		this.nascosto = nascosto;
	}


	public void setColore(int a, int b, int c) {
    	Color cl=new Color(a,b,c);
    	this.colore=cl;
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
    	if (nascosto) return;
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
        } else if (pAssociata.getVx() < 0.001 && pAssociata.getVy() < 0.001) {
            distanza = dMin;
            nascosto = false;
        }
    }
    
    public void colpisci() {
        if (!anim) {
            anim = true;
            push = 0;
            this.setNascosto(false); 
        }
        if (distanza < Pallina.getRaggio()*4) {
            anim = false;
            this.setNascosto(true);  
            double forza = (dMax - distanza) / 2.0;
            pAssociata.setVx(Math.cos(Math.toRadians(180+rotazione))*(push*5));
            pAssociata.setVy(Math.sin(Math.toRadians(180+rotazione))*(push*5));
        } else {
            distanza -= 20;
            push++;
        }
    }

    public boolean isAnim() {
        return anim;
    }

    public boolean isNascosto() {
        return nascosto;
    }
    
    public boolean Punto(Pallina[] p, Buca[] b) {
        boolean sent = false;
        for (int i = 0; i < b.length; i++) {
            for(int j = 0; j < p.length; j++) {
                if(p[j] != null && b[i].dentro(p[j])) {
                    sent = true;
                    break;
                }
            }
            if(sent) break;
        }
        return sent;
    }
    
    public static void cambiaTurno() {
    	if(Tavolo.gcorrente==1) {
    		Tavolo.setGiocatore(2);
    	}else {
    		Tavolo.setGiocatore(1);
    	}
    }
    
    
    //la stecca torna visibile quando le palline si fermano ( più carino penso :) )
    public void mostraStecca(Pallina[] palline, Pallina giocatore) {
        if (nascosto && !Pallina.isMoving(palline, giocatore)) {
            nascosto = false;
            distanza = dMin;  
        }
    }


	public int getDMin() {
		return dMin;
	}


	public int getDMax() {
		return dMax;
	}
}