package biliardo;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Principale {

	protected Shell shell;
	private LocalResourceManager localResourceManager;
	private Text txtP2;
	private Text txtP1;

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
		
		Button btnApriTavolo = new Button(shell, SWT.NONE);
		btnApriTavolo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Tavolo c =new Tavolo();
				c.open();
				shell.setVisible(false);
				
			}
		});
		btnApriTavolo.setBounds(160, 388, 75, 25);
		btnApriTavolo.setText("GIOCHIAMO!!");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setBounds(400, 388, 75, 25);
		btnNewButton_1.setText("New Button");
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		btnNewButton_2.setBounds(643, 388, 75, 25);
		btnNewButton_2.setText("New Button");
		
		txtP2 = new Text(shell, SWT.BORDER);
		txtP2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		txtP2.setFont(localResourceManager.create(FontDescriptor.createFrom("Liberation Serif", 11, SWT.NORMAL)));
		txtP2.setBounds(52, 273, 83, 30);
		
		txtP1 = new Text(shell, SWT.BORDER);
		txtP1.setBounds(160, 273, 83, 30);
		
		txtP1.setText("" + Tavolo.punteggio_p1);
		txtP2.setText("" + Tavolo.punteggio_p2);
		
		

	}
	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
	}
}
