package biliardo;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;

import java.util.Arrays;
import javax.swing.JOptionPane;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class Tavolo {

    static Shell shell;
    private LocalResourceManager localResourceManager;
    private Canvas canvas;

    // elementi
    private Pallina[] p = new Pallina[0];
    private Pallina gioc;
    private Buca[] b = new Buca[0];
    private Stecca st;

    //CONTATORI PUNTEGGI
    private int[] punteggio = {0, 0};
    private int g1tipo = -1;

    // transform per stecca
    private Transform trOg;

    // cambia modalità stecca
    private boolean misuraPot = false;

    // colori palline
    private final Color[] coloriPalline = {
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

    // indica se la pallina è bianca
    // 0 = piena
    // 1 = bianca
    // 2 = nera
    // -1 = giocatore
    private final int[] tipiPalline = {0, 1, 0, 0, 2, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1};
    private Label lblG1;
    private Label lblG2;

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
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();

        // Prepara transform per stecca
        trOg = new Transform(display);

        // refresh del canvas ogni 5ms
        // necessario per avere movimento fluido e costante
        final int refreshMS = 5;
        Runnable runnable = new Runnable() {
            public void run() {
                if (!canvas.isDisposed()) {
                    canvas.redraw();
                    display.timerExec(refreshMS, this);
                }
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
        shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				Principale.shell.setVisible(true);
			}
		});
		// Colore bordo
		shell.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new Color(163, 106, 63))));
		shell.setSize(852, 497);
		shell.setText("Biliardo");

        canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                // alla pressione del mouse, la stecca si prepara a colpire
                misuraPot = true;
            }

            @Override
            public void mouseUp(MouseEvent e) {
                // al rilascio del mouse, la stecca colpisce
                misuraPot = false;
                st.colpisci();
            }
        });
        canvas.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent arg0) {
                // se la stecca è nascosta o in animazione, non si muove
                if (st.isAnim() || st.isNascosto()) return;

                int xc = gioc.getX() + Pallina.getRaggio();
                int yc = gioc.getY() + Pallina.getRaggio();

                // altrimenti controlla la modalità
                if (!misuraPot) {
                    // se !misuraPot, la stecca ruota attorno alla pallina

                    int x0 = xc + Pallina.getRaggio();
                    // distanza xc,yc (centro) -> x0,y0 (centro proiettato sul lato sinistro (0 gradi))
                    double a = Math.pow(Pallina.getRaggio(), 2);
                    // distanza xc,yc (centro) -> xm,ym (punto mouse)
                    double b = Math.pow(arg0.x - xc, 2) + Math.pow(arg0.y - yc, 2);
                    // distanza x0,y0 (centro proiettato sul lato sinistro (0 gradi)) -> xm,ym (punto mouse)
                    double c = Math.pow(x0 - arg0.x, 2) + Math.pow(yc - arg0.y, 2);

                    double alpha = Math.acos((a + b - c) / (2 * Math.sqrt(a * b)));

                    if (arg0.y < yc) {
                        alpha = -alpha;
                    }
                    st.setRotazione((int) Math.toDegrees(alpha));
                } else {
                    // se misuraPot, la stecca si sposta per dare più o meno forza al colpo
                    double rads = Math.toRadians(st.getRotazione());
                    int dX = arg0.x - xc;
                    int dY = arg0.y - yc;

                    st.setDistanza((int) ((dX * Math.cos(rads)) + (dY * Math.sin(rads))));
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


                for (int j = 0; j < p.length; j++) {
                    if (p[j] != null)
                        p[j].update(canvas.getBounds().width, canvas.getBounds().height, p, j, Pallina.getRaggio());
                }

                penna.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 0, 0))));

                // SCRITTURA BUCHI
                for (int i = 0; i < b.length; i++) {
                    b[i].disegna(penna);
                }

                // palline
                for (int i = 0; i < p.length; i++) {
                    if (p[i] != null) {
                        p[i].disegna(penna);
                    }
                }

                // stecca
                st.disegna(penna, trOg);

                // giocatore
                gioc.update(canvas.getBounds().width, canvas.getBounds().height, p, -1, Pallina.getRaggio());
                if (gioc.isNascosto() && !Pallina.isMoving(p)) {
                    gioc.setNascosto(false);
                }
                gioc.disegna(penna);

                // PALLINE CHE SCOMPAIONO SE DENTRO BUCA
                //CONTROLLO GAME OVER

                if (gameOver(p)) {;
                    shell.close();
                }

                int nbuche = 0;
                for (int i = 0; i < b.length; i++) {
                    for (int j = 0; j < p.length; j++) {
                        // pallina in buca
                        if (p[j] != null && b[i].dentro(p[j])) {
                            // punteggio
                            int tipo = p[j].getTipo();
                            if (tipo == 0 || tipo == 1) {
                                punteggio[tipo]++;
                            }
                            if (g1tipo == -1) {
                                if (st.isTurnoG1()) {
                                    g1tipo = tipo;
                                } else {
                                    g1tipo = Math.abs(1 - tipo);
                                }
                            }

                            // cambio giocatore se palla opposta in buca
                            if (st.isTurnoG1() && p[j].getTipo() != g1tipo) {
                                st.setTurnoG1(false);
                            } else if (!st.isTurnoG1() && p[j].getTipo() == g1tipo) {
                                st.setTurnoG1(true);
                            }

                            // cancella pallina
                            p[j] = null;

                            // aggiorna pt
                            lblG1.setText("" + punteggio[g1tipo]);
                            lblG2.setText("" + punteggio[Math.abs(1 - g1tipo)]);

                            // conta palline in buca in questo round
                            nbuche++;
                        }
                    }

                    // Giocatore in buca
                    if (b[i].dentro(gioc)) {
                        gioc.setNascosto(Pallina.isMoving(p));
                        gioc.reset();
                    }
                }

                // cambia turno per colpo a vuoto
                if (st.isColpito() && nbuche == 0) {
                    st.setTurnoG1(!st.isTurnoG1());
                    st.setColpito(false);
                } else if (st.isColpito()) {
                    st.setColpito(false);
                }

                arg0.gc.drawImage(image, 0, 0);

                image.dispose();
                penna.dispose();

            }
        });
        // colore interno
        canvas.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new Color(87, 143, 54))));

        // con bordo = 25
        // width = shellWidth - 16 - bordo*2
        // height = shellHeight - 39 - bordo*2
        canvas.setBounds(25, 25, 786, 408);

        lblG1 = new Label(shell, SWT.NONE);
        lblG1.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 179, 186))));
        lblG1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
        lblG1.setFont(localResourceManager.create(FontDescriptor.createFrom("Arial", 20, SWT.BOLD | SWT.ITALIC)));
        lblG1.setAlignment(SWT.CENTER);
        lblG1.setBounds(0, 0, 30, 32);
        lblG1.setText("0");

        lblG2 = new Label(shell, SWT.NONE);
        lblG2.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(186, 225, 255))));
        lblG2.setText("0");
        lblG2.setFont(localResourceManager.create(FontDescriptor.createFrom("Arial", 20, SWT.BOLD | SWT.ITALIC)));
        lblG2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
        lblG2.setAlignment(SWT.CENTER);
        lblG2.setBounds(806, 0, 30, 32);

        // creazione palline
        int xb = 75 * canvas.getBounds().width / 100 - Pallina.getRaggio();
        int yb = canvas.getBounds().height / 2;

        // cambia numero colonne palline (rimuovi dopo collisioni)
        for (int i = 0; i < 5; i++) {
            int yc = yb;
            for (int ii = 0; ii < i + 1; ii++) {
                p = Arrays.copyOf(p, p.length + 1);
                p[p.length - 1] = new Pallina(xb, yc, coloriPalline[p.length - 1], tipiPalline[p.length - 1]);
                yc -= Pallina.getRaggio() * 2;
            }
            xb += Pallina.getRaggio() * 2;
            yb += Pallina.getRaggio();
        }// giallo 0 2 3  7  9 10 13
        gioc = new Pallina(25 * canvas.getBounds().width / 100 - Pallina.getRaggio(),
                canvas.getBounds().height / 2,
                Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
                -1);

        st = new Stecca(gioc);
        ///////////////////////////////////////////////////////////

        // creazione buche
        int[] bucaY = {10, canvas.getBounds().height - 10};
        int[] bucaX = {10, canvas.getBounds().width / 2, canvas.getBounds().width - 10};

        for (int i = 0; i < bucaY.length; i++) {
            for (int j = 0; j < bucaX.length; j++) {
                b = Arrays.copyOf(b, b.length + 1);
                b[b.length - 1] = new Buca(bucaX[j], bucaY[i]);
            }
        }
        ///////////////////////////////////////////////////////////

    }

    private boolean gameOver(Pallina[] p) {
        boolean pallinaNera = false;
        int[] pRimanenti = {0, 0};
        int i = 0;
        while (!pallinaNera && i < p.length) {
            if (p[i] != null) {
                if (p[i].getTipo() == 2) {
                    pallinaNera = true;
                } else if (p[i].getTipo() == 0 || p[i].getTipo() == 1) {
                    pRimanenti[p[i].getTipo()]++;
                }
            }
            i++;
        }
        if (!pallinaNera) {
            if (pRimanenti[0] != 0 && pRimanenti[1] != 0) {
                JOptionPane.showMessageDialog(null, "PALLINA NERA IMBUCATA PRIMA DELLE ALTRE", "GAME OVER!!!!", 2);
                return true;
            } else {
                int giocCorr;
                if (st.isTurnoG1()) giocCorr = 1;
                else giocCorr = 2;
                JOptionPane.showMessageDialog(null, "HA VINTO IL GIOCATORE " + giocCorr, "GAME OVER!!!!", 2);
                return true;
            }
        }
        return false;
    }

    private void createResourceManager() {
        localResourceManager = new LocalResourceManager(JFaceResources.getResources(), shell);
    }
}