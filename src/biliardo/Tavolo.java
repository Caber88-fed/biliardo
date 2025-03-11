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
		Transform tr = new Transform(display);
		createContents();
		shell.open();
		shell.layout();

		penna.fillRectangle(100, 100, 50, 150);
		
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
		shell.setSize((284*3), (int)(142*3.5));
		shell.setText("SWT Application");
		
		canvas = new Canvas(shell, SWT.NONE);
		penna = new GC(canvas);
		canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		canvas.setBounds(25, 25, (284*3)-(40*2), (int)(142*3.5)-(50*2));

	}
	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
	}
}
