package biliardo;

import java.util.Arrays;





public class Pallina {
    private double x, y, direzione;
    private static final int raggio = 10;
    private int colore;
    private double vx;
    private double vy;
    

    public Pallina(double x, double y, int c, double direzione, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.colore = c;
        this.direzione = direzione;
        this.vx=vx;
        this.vy=vy;
    }

	public static int getRaggio() {
        return raggio;
    }

    public double getDirezione() {
        return direzione;
    }

    public void setDirezione(double direzione) {
        this.direzione = direzione;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
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

    
    public void aggiornaPosizione() {
    	this.x+=this.vx;
    	this.y+=this.vy;
    }
    
    
    public double distanza(Pallina p) {
        double dx = this.x - p.x;
        double dy = this.y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    
  //Restituisce l'angolo tra le velocità y e x in radianti
    public double getRadAngle()
    {
        return Math.atan2(vy, vx);
    }
                
    //restituisce la velocità della palla corrente in pixel per iterazione
    public double getVelocity()
    {
        return Math.sqrt(vx*vx + vy*vy);
    }
    
    public double getVx() {
		return vx;
	}

	public void setVx(int vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(int vy) {
		this.vy = vy;
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
            Pallina p1= p[i];
            if (this.distanza(p1) < r + r) {
                double[] res = {this.vx - p1.vx, this.vy - p1.vy};
                if (res[0] * (p1.x - this.x) + res[1] * (p1.y - this.y) >= 0) {
                    
                    // angolo di collisione
                    double theta = -Math.atan2(p1.y - this.y, p1.x - this.x);

                    // rotazione velocità di riferimento della collisione
                    double[] u1 = rotazione(new double[]{this.vx, this.vy}, theta);
                    double[] u2 = rotazione(new double[]{p1.vx, p1.vy}, theta);

                    // collisione elastica
                    double[] v1 = rotazione(new double[]{(u1[0] * (1 - 1) + 2 * 1 * u2[0]) / (1 + 1),u1[1]}, -theta);

                    double[] v2 = rotazione(new double[]{(u2[0] * (1 - 1) + 2 * 1 * u1[0]) / (1 + 1),u2[1]}, -theta);

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
	}
}