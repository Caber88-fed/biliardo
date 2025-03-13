package biliardo;

public class Buca {
	private int x;
	private int y;
	public static final int raggio=25;
	private static final int rc=18;
	public Buca(int x, int y) {
		super();
		this.x = x;
		this.y = y;
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
	public int getRc() {
		return rc;
	}
	public static int getRaggio() {
		return raggio;
	}
	
	
	//METODO CHE CONTROLLA SE UNA PALLINA ENTRA IN BUCA
	public boolean Dentro(int a, int b, int r) {
		int x1=this.getX()+25;
		int y1=this.getY()+25;
		double distanza=(double)Math.sqrt(Math.pow(a-x1, 2) + Math.pow(b-y1, 2));
		if(distanza<this.getRc() + r) {
			return true;
		}
		return false;
		
	}
	
	
}
