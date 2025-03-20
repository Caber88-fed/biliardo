package biliardo;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;

import java.util.Arrays;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseAdapter;

public class Tavolo {

    protected Shell shell;
    private LocalResourceManager localResourceManager;
    Canvas canvas;
    // GC penna;
    final int nColonne = 5;
    Pallina[] p = new Pallina[0];
    Buca[] b = new Buca[0];
    Stecca st = new Stecca();
    Pallina gioc;
    boolean misuraPot = false;

    Color[] coloriPalline = {
            new Color(236, 218, 60), // giallo
            new Color(236, 218, 60), // giallo + bianco
            new Color(16, 122, 174), // blu
            new Color(237, 53, 55), // rosso chiaro
            new Color(0, 0, 0), // nero
            new Color(16, 122, 174), // blu + bianco
            new Color(237, 53, 55), // rosso + bianco
            new Color(179, 57, 62), // rosso scuro
            new Color(50, 134, 82), // verde + bianco
            new Color(137, 132, 173), // viola
            new Color(244, 133, 51), // arancione
            new Color(244, 133, 51), // arancione + bianco
            new Color(179, 57, 62), // rosso scuro + bianco
            new Color(50, 134, 82), // verde
            new Color(137, 132, 173), // viola + bianco
    };

    boolean[] pallineBianche = {false, true, false, false, false, true, true, false, true, false, false, true, true, false, true};

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Tavolo window = new Tavolo();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        final int refreshMS = 5;
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();

        Runnable runnable = new Runnable() {
            public void run() {
                canvas.redraw();
                // canvas.update();
                display.timerExec(refreshMS, this);
            }
        };
        display.timerExec(refreshMS, runnable);

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        createResourceManager();
        shell.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new Color(128, 64, 0))));
        shell.setSize(852, 497);
        shell.setText("Biliardo");

        canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                misuraPot = true;
            }

            @Override
            public void mouseUp(MouseEvent e) {
                misuraPot = false;
                st.setDistanza(0);
            }
        });
        canvas.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent arg0) {
                int xc = gioc.getX() + Pallina.getRaggio();
                int yc = gioc.getY() + Pallina.getRaggio();

                if (!misuraPot) {
                    int x0 = xc + Pallina.getRaggio();
                    // distanza xc,yc (centro) -> x0,y0 (centro proiettato sul lato sinistro (0
                    // gradi))
                    double a = Math.pow(Pallina.getRaggio(), 2);
                    // distanza xc,yc (centro) -> xm,ym (punto mouse)
                    double b = Math.pow(arg0.x - xc, 2) + Math.pow(arg0.y - yc, 2);
                    // distanza x0,y0 (centro proiettato sul lato sinistro (0 gradi)) -> xm,ym
                    // (punto mouse)
                    double c = Math.pow(x0 - arg0.x, 2) + Math.pow(yc - arg0.y, 2);

                    int angolo = (int) Math.toDegrees((Math.acos(((a + b - c) / Math.sqrt(4 * a * b)))));
                    if (arg0.y < yc) {
                        angolo = -angolo;
                    }
                    st.setRotazione(angolo);
                } else {
                    int dX = arg0.x - xc;
                    int dY = arg0.y - yc;
                    //if (dX+dY > 0) {
                    st.setDistanza((int) Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)));
                    //} else {
                    //	st.setDistanza(0);
                    //}
                }
            }
        });
        canvas.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent arg0) {
                // buffer
                Image image = new Image(shell.getDisplay(), canvas.getBounds());
                GC penna = new GC(image);
                penna.setBackground(arg0.gc.getBackground());
                penna.fillRectangle(image.getBounds());

                // preparazione rotazione
                Transform tr = new Transform(arg0.gc.getDevice());
                Transform trOg = new Transform(arg0.gc.getDevice());
                penna.getTransform(trOg);

                // movimento giocatore
                double rad = Math.toRadians(gioc.getDirezione());
                double deltaX = Math.cos(rad) * 5;
                double deltaY = Math.sin(rad) * 5;
                int x = (int) (gioc.getX() + deltaX);
                int y = (int) (gioc.getY() + deltaY);

                // controllo collisioni
                if (y < 0) {
                    gioc.setDirezione(-gioc.getDirezione());
                } else if (y > canvas.getBounds().height - Pallina.getRaggio() * 2) {
                    gioc.setDirezione(-gioc.getDirezione());
                }

                if (x < 0) {
                    gioc.setDirezione(180 - gioc.getDirezione());
                } else if (x > canvas.getBounds().width - Pallina.getRaggio() * 2) {
                    gioc.setDirezione(180 - gioc.getDirezione());
                }

                //gioc.setX(x);
                //gioc.setY(y);

                penna.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new Color(0, 0, 0))));

                // SCRITTURA BUCHI
                for (int i = 0; i < b.length; i++) {
                    penna.fillOval(b[i].getX(), b[i].getY(), 50, 50);
                }

                // palline
                for (int i = 0; i < p.length; i++) {
                    p[i].disegna(penna);
                }

                // giocatore
                gioc.disegna(penna);

                // PALLINE CHE SCOMPAIONO SE DENTRO BUCA
                for (int i = 0; i < p.length; i++) {
                    int c = 0;
                    while (c < 6) {
                        if (b[c].Dentro(p[i].getX(), p[i].getY(), 25)) {
                            p[i] = null;
                        }
                        c++;
                    }
                }
                ///////////////////////////////////////////////////////////

                // PALLINA GIOCATORE CHE RITORNA AL CENTRO SE DENTRO BUCA
                int c = 0;
                while (c < 6) {
                    if (b[c].Dentro(gioc.getX(), gioc.getY(), 25)) {
                        gioc.setX(25 * canvas.getBounds().width / 100 - Pallina.getRaggio());
                        gioc.setY(canvas.getBounds().height / 2);
                    }
                    c++;
                }
                ///////////////////////////////////////////////////////////

                // stecca
                penna.getTransform(tr);
                tr.translate(gioc.getX() + Pallina.getRaggio(), gioc.getY() + Pallina.getRaggio());
                tr.rotate(st.getRotazione());
                penna.setTransform(tr);
                penna.fillRectangle(st.getDistanza(), -3, 60, 6);
                penna.setTransform(trOg);

                // st.setRotazione(st.getRotazione() + 1);

                arg0.gc.drawImage(image, 0, 0);

                image.dispose();
                penna.dispose();

                tr.dispose();
                trOg.dispose();

            }
        });
        // penna = new GC(canvas);
        canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
        // con bordo = 25
        // width = shellWidth - 16 - bordo*2
        // height = shellHeight - 39 - bordo*2
        canvas.setBounds(25, 25, 786, 408);

        // creazione palline

        int xb = 75 * canvas.getBounds().width / 100 - Pallina.getRaggio();
        int yb = canvas.getBounds().height / 2;

        for (int i = 0; i < nColonne; i++) {
            int yc = yb;
            for (int ii = 0; ii < i + 1; ii++) {
                p = Arrays.copyOf(p, p.length + 1);
                p[p.length - 1] = new Pallina(xb, yc, coloriPalline[p.length - 1], 0, pallineBianche[p.length-1]);
                yc -= Pallina.getRaggio() * 2;
            }
            xb += Pallina.getRaggio() * 2;
            yb += Pallina.getRaggio();
        }
        gioc = new Pallina(25 * canvas.getBounds().width / 100 - Pallina.getRaggio(), canvas.getBounds().height / 2,
                new Color(255, 255, 255),
                70, false);
        ///////////////////////////////////////////////////////////

        // CREAZIONE BUCHE
        b = Arrays.copyOf(b, b.length + 1);
        b[b.length - 1] = new Buca(-15, -15);
        b = Arrays.copyOf(b, b.length + 1);
        b[b.length - 1] = new Buca(canvas.getBounds().width / 2 - 25, -15);
        b = Arrays.copyOf(b, b.length + 1);
        b[b.length - 1] = new Buca(canvas.getBounds().width - (50 - 15), -15);
        b = Arrays.copyOf(b, b.length + 1);
        b[b.length - 1] = new Buca(-15, canvas.getBounds().height - (50 - 15));
        b = Arrays.copyOf(b, b.length + 1);
        b[b.length - 1] = new Buca(canvas.getBounds().width / 2 - 25, canvas.getBounds().height - (50 - 15));
        b = Arrays.copyOf(b, b.length + 1);
        b[b.length - 1] = new Buca(canvas.getBounds().width - (50 - 15), canvas.getBounds().height - (50 - 15));
        ///////////////////////////////////////////////////////////

    }

    private void createResourceManager() {
        localResourceManager = new LocalResourceManager(JFaceResources.getResources(), shell);
    }
}
