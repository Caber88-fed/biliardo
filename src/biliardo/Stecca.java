package biliardo;

public class Stecca {
    private int rotazione;
    private int dMin = Pallina.getRaggio()*2;
    private int dMax = dMin+100;
    private int distanza;

    public Stecca() {
        rotazione = 0;
        distanza = dMin;
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
}
