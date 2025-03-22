package biliardo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class Buca {
    private final int x;
    private final int y;
    private static final int ragBuca = 25;
    private static final int ragEffettivo = 18;

    public Buca(int x, int y) {
        this.x = x;
        this.y = y;
    }

	public static int getRagBuca() {
        return ragBuca;
    }

    public static int getRagEffettivo() {
        return ragEffettivo;
    }

    //METODO CHE CONTROLLA SE UNA PALLINA ENTRA IN BUCA
    public boolean dentro(Pallina p) {
        double dist = Math.sqrt(Math.pow(x - (p.getX()+Pallina.getRaggio()), 2) + Math.pow(y - (p.getY()+Pallina.getRaggio()), 2));
        return dist <= ragEffettivo+Pallina.getRaggio();
    }

    public void disegna(GC penna) {
        penna.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        penna.fillOval(x-ragBuca, y-ragBuca, ragBuca*2, ragBuca*2);
        penna.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        penna.fillOval(x-ragEffettivo, y-ragEffettivo, ragEffettivo*2, ragEffettivo*2);
    }
}
