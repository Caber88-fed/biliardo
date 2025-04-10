package biliardo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class Buca {
    private final int x;
    private final int y;
    private static final int ragBuca = 25;
    private static final int ragEffettivo = 16;

    public Buca(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //METODO CHE CONTROLLA SE UNA PALLINA ENTRA IN BUCA
    public boolean dentro(Pallina p) {
        double dist = Math.sqrt(Math.pow(x - (p.getX()+Pallina.getRaggio()), 2) + Math.pow(y - (p.getY()+Pallina.getRaggio()), 2));
        return dist <= ragEffettivo+Pallina.getRaggio();
    }

    public void disegna(GC penna) {
        penna.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        penna.fillOval(x-ragBuca, y-ragBuca, ragBuca*2, ragBuca*2);
    }
}
