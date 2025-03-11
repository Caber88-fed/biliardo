package biliardo;

public class Pallina {
	private int x, y, colore, direzione;

	public Pallina(int x, int y, int c, int direzione) {
		this.x = x;
		this.y = y;
		this.colore = c;
		this.direzione = direzione;
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

	public int getColore() {
		return colore;
	}

	public void setColore(int colore) {
		this.colore = colore;
	}

}
