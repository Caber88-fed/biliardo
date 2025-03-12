package biliardo;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;
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
	Pallina[] p;
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
				int x0=75*canvas.getBounds().width/100-Pallina.raggio;
				int y0=canvas.getBounds().height/2;
				int x1=x0+Pallina.raggio*2;
				int y1=y0+Pallina.raggio;
				int x2=x1;
				int y2=y1-Pallina.raggio*2;
				int x3=x1+Pallina.raggio*2;
				int y3=y1+Pallina.raggio;
				int x4=x3;
				int y4=y3-Pallina.raggio*2;
				int x5=x3;
				int y5=y4-Pallina.raggio*2;
				int x6=x3+Pallina.raggio*2;
				int y6=y3+Pallina.raggio;
				int x7=x6;
				int y7=y6-Pallina.raggio*2;
				int x8=x7;
				int y8=y7-Pallina.raggio*2;
				int x9=x8;
				int y9=y8-Pallina.raggio*2;
				
				
				int x10=x6+Pallina.raggio*2;
				int y10=y6+Pallina.raggio;
				int x11=x10;
				int y11=y10-Pallina.raggio*2;
				int x12=x11;
				int y12=y11-Pallina.raggio*2;
				int x13=x12;
				int y13=y12-Pallina.raggio*2;
				int x14=x13;
				int y14=y13-Pallina.raggio*2;
				
				
				int xx=25*canvas.getBounds().width/100-Pallina.raggio;
				int yy=canvas.getBounds().height/2;
				penna.fillOval(xx, yy, Pallina.raggio*2, Pallina.raggio*2);
				
				penna.fillOval(x0, y0, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x1, y1, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x2, y2, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x3, y3, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x4, y4, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x5, y5, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x6, y6, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x7, y7, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x8, y8, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x9, y9, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x10, y10, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x11, y11, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x12, y12, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x13, y13, Pallina.raggio*2, Pallina.raggio*2);
				penna.fillOval(x14, y14, Pallina.raggio*2, Pallina.raggio*2);
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

	}
	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
	}
}
