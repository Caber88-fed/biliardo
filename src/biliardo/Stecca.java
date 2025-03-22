package biliardo;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;

public class Stecca {
	private final int dMin = Pallina.getRaggio() * 2;
	private final int dMax = dMin + 100;
	private final Color colore = new Color(199, 171, 130);
	private final int spessore = 6; // divisibile per 2
	private final int lunghezza = 140;

	private int rotazione;
	private int distanza;
	private boolean nascosto;
	private boolean anim;

	private Pallina pAssociata;

	public Stecca(Pallina pAssociata) {
		rotazione = 0;
		distanza = dMin;
		nascosto = false;
		anim = false;
		this.pAssociata = pAssociata;
	}

	public int getRotazione() {
		return rotazione;
	}

	public void setRotazione(int rotazione) {
		this.rotazione = rotazione;
	}

	public int getDistanza() {
		return distanza;
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
			penna.setBackground(new Color(255, 255, 255));
			penna.fillRectangle(distanza, -(spessore / 2), spessore, spessore);
			penna.setTransform(trOg);
			tr.dispose();
		}
	}
	
	public void colpisci() {
		if (!anim) anim = true;
		if (distanza < Pallina.getRaggio()*2) {
			anim = false;
			nascosto = true;
			return;
		}
		distanza -= 20;
	}

	public boolean isAnim() {
		return anim;
	}
	
	
}
