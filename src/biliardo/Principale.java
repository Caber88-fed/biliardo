package biliardo;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;

public class Principale {

	protected Shell shell;
	private LocalResourceManager localResourceManager;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Principale window = new Principale();
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
		shell.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 255, 0))));
		shell.setSize(990, 567);
		shell.setText("SWT Application");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(160, 388, 75, 25);
		btnNewButton.setText("New Button");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setBounds(400, 388, 75, 25);
		btnNewButton_1.setText("New Button");
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		btnNewButton_2.setBounds(643, 388, 75, 25);
		btnNewButton_2.setText("New Button");

	}
	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
	}
}
