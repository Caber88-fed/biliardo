package biliardo;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;

import java.util.Arrays;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.SWT;

public class Tavolo {

	protected Shell shell;
	private LocalResourceManager localResourceManager;
	Canvas canvas;
	GC penna;
	final int nColonne = 5;
	Pallina[] p = new Pallina[0];
	/**
	 * Launch the application.
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
		//Transform tr = new Transform(display);
		createContents();
		shell.open();
		shell.layout();

		//penna.fillRectangle(100, 100, 50, 150);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				penna.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 0, 0))));
				//buchi
				penna.fillOval(-15, -15, 50, 50);
				penna.fillOval(canvas.getBounds().width/2-25, -15, 50, 50);
				penna.fillOval(canvas.getBounds().width-(50-15), -15, 50, 50);
				penna.fillOval(-15, canvas.getBounds().height-(50-15), 50, 50);
				penna.fillOval(canvas.getBounds().width/2-25, canvas.getBounds().height-(50-15), 50, 50);
				penna.fillOval(canvas.getBounds().width-(50-15), canvas.getBounds().height-(50-15), 50, 50);
				
				
				//palline
				
				//p[0]=new Pallina((3/4)*canvas.getBounds().width-Pallina.raggio, canvas.getBounds().height/2, 0, 0);
				for (int i = 0; i<p.length; i++) {
					penna.fillOval(p[i].getX(), p[i].getY(), Pallina.raggio*2, Pallina.raggio*2);
				}
				
				int xx=25*canvas.getBounds().width/100-Pallina.raggio;
				int yy=canvas.getBounds().height/2;
				penna.fillOval(xx, yy, Pallina.raggio*2, Pallina.raggio*2);
				
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
		shell.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(128, 64, 0))));
		shell.setSize(852, 497);
		shell.setText("SWT Application");
		
		canvas = new Canvas(shell, SWT.NONE);
		penna = new GC(canvas);
		canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		// con bordo = 25
		// width = shellWidth - 16 - bordo*2
		// height = shellHeight - 39 - bordo*2
		canvas.setBounds(25, 25, 786, 408);
		
		// creazione palline
		int xb=75*canvas.getBounds().width/100-Pallina.raggio;
		int yb=canvas.getBounds().height/2;
		
		for (int i = 0; i<nColonne; i++) {
			int yc = yb;
			for (int ii = 0; ii<i+1; ii++) {
				p = Arrays.copyOf(p, p.length+1);
				p[p.length-1] = new Pallina(xb, yc, 0, 0);
				yc-=Pallina.raggio*2;
			}
			xb += Pallina.raggio*2;
			yb += Pallina.raggio;
		}	

	}
	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
	}
}
