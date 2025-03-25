package biliardo;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;

import java.awt.event.ActionEvent;
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
	private Canvas canvas;

	// cambia numero colonne palline (rimuovi dopo collisioni)
	private final int nColonne = 5;

	// elementi
	private Pallina[] p = new Pallina[0];
	private Pallina gioc;
	private Buca[] b = new Buca[0];
	private Stecca st;

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
	private final boolean[] pallineBianche = {false, true, false, false, false, true, true, false, true, false, false, true, true, false, true};


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
				canvas.redraw();
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

					st.setDistanza((int)((dX*Math.cos(rads)) + (dY*Math.sin(rads))));
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


				for (int j = 0; j< p.length; j++) {
		            p[j].update(canvas.getBounds().width, canvas.getBounds().height, p, j, Pallina.getRaggio());
		        }

				penna.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 0, 0))));

				// SCRITTURA BUCHI
				for (int i = 0; i < b.length; i++) {
					b[i].disegna(penna);
				}

				// palline
				for (int i = 0; i < p.length; i++) {
					p[i].disegna(penna);
					//penna.fillOval((int)p[i].getX(), (int)p[i].getY(), Pallina.getRaggio() * 2, Pallina.getRaggio() * 2);
				}

				// stecca
				st.disegna(penna, trOg);

				// giocatore
				gioc.muovi(canvas.getBounds().height, canvas.getBounds().width);
				gioc.disegna(penna);

				// PALLINE CHE SCOMPAIONO SE DENTRO BUCA
				for (int i = 0; i < b.length; i++) {
					for (int j = 0; j < p.length; j++) {
						if (p[j] != null && b[i].dentro(p[j])) {
							//p[j] = null;
						}
					}
				}
				///////////////////////////////////////////////////////////

				// PALLINA GIOCATORE CHE RITORNA AL CENTRO SE DENTRO BUCA
				for (int i = 0; i < b.length; i++) {
					if (b[i].dentro(gioc)) {
						gioc.reset();
					}
				}
				///////////////////////////////////////////////////////////


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

		// creazione palline
		int xb = 75 * canvas.getBounds().width / 100 - Pallina.getRaggio();
		int yb = canvas.getBounds().height / 2;

		for (int i = 0; i < nColonne; i++) {
			int yc = yb;
			for (int ii = 0; ii < i + 1; ii++) {
				p = Arrays.copyOf(p, p.length + 1);
				p[p.length - 1] = new Pallina(xb, yc, coloriPalline[p.length - 1], pallineBianche[p.length-1]);
				yc -= Pallina.getRaggio() * 2;
			}
			xb += Pallina.getRaggio() * 2;
			yb += Pallina.getRaggio();
		}
		gioc = new Pallina(25 * canvas.getBounds().width / 100 - Pallina.getRaggio(),
				canvas.getBounds().height / 2,
				Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
				false);

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

	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(), shell);
	}
}