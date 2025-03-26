package biliardo;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Label;
import javax.swing.JOptionPane;
public class Principale {

	static Shell shell;
	private LocalResourceManager localResourceManager;
	Pallina p;
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
		
		Button btnApriTavolo = new Button(shell, SWT.NONE);
		btnApriTavolo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Tavolo c =new Tavolo();
				shell.setVisible(false);
				c.open();


			}
		});
		btnApriTavolo.setBounds(504, 276, 134, 25);
		btnApriTavolo.setText("GIOCHIAMO!!");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//APERTURA REGOLAMENTO
				JOptionPane.showMessageDialog(null, " 1. Scopo del Gioco\r\n"
						+ "L’obiettivo del gioco è imbucare tutte le proprie palle (piene o a strisce) e, infine, la palla 8 (nera) prima dell'avversario.\r\n"
						+ "\r\n"
						+ "2. Regole Generali\r\n"
						+ "Il gioco inizia con il posizionamento delle palle nel triangolo e il tiro di apertura.\r\n"
						+ "\r\n"
						+ "Un giocatore è assegnato alle palle piene (1-7), l'altro alle strisce (9-15).\r\n"
						+ "\r\n"
						+ "Se un giocatore imbuca una palla del proprio gruppo, può continuare il turno.\r\n"
						+ "\r\n"
						+ "Se un giocatore sbaglia un tiro o imbuca una palla non appartenente al proprio gruppo, il turno passa all'avversario.\r\n"
						+ "\r\n"
						+ "La palla 8 deve essere imbucata solo dopo aver mandato in buca tutte le palle del proprio gruppo.\r\n"
						+ "\r\n"
						+ "3. Vittoria e Sconfitta\r\n"
						+ "Vince il giocatore che imbuca per ultimo la palla 8 in modo regolare.\r\n"
						+ "\r\n"
						+ "Se un giocatore imbuca la palla 8 prima del tempo o commette fallo mentre la imbuca, perde la partita immediatamente.\r\n"
						+ "\r\n"
						+ "Buon divertimento! 🎱", "REGOLAMENTO",1);


			}
		});
		btnNewButton_1.setBounds(245, 276, 139, 25);
		btnNewButton_1.setText("REGOLAMENTO");
		
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(102, 51, 0))));
		canvas.setBounds(0, 0, 44, 511);
		
		Canvas canvas_1 = new Canvas(shell, SWT.NONE);
		canvas_1.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(102, 51, 0))));
		canvas_1.setBounds(34, 471, 955, 40);
		
		Canvas canvas_2 = new Canvas(shell, SWT.NONE);
		canvas_2.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(102, 51, 0))));
		canvas_2.setBounds(42, 0, 926, 40);
		
		Canvas canvas_3 = new Canvas(shell, SWT.NONE);
		canvas_3.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(102, 51, 0))));
		canvas_3.setBounds(929, 38, 39, 437);
		
		Label lblBiliardo = new Label(shell, SWT.NONE);
		lblBiliardo.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 255, 0))));
		lblBiliardo.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI Black", 50, SWT.BOLD | SWT.ITALIC)));
		lblBiliardo.setBounds(216, 52, 503, 124);
		lblBiliardo.setText("BILIARDO");

		Label lblDavideGasparetto = new Label(shell, SWT.NONE);
		lblDavideGasparetto.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 255, 0))));
		lblDavideGasparetto.setBounds(51, 409, 151, 25);
		lblDavideGasparetto.setText("Davide Gasparetto");

		Label lblCaberlottoFrancesco = new Label(shell, SWT.NONE);
		lblCaberlottoFrancesco.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 255, 0))));
		lblCaberlottoFrancesco.setBounds(50, 440, 175, 25);
		lblCaberlottoFrancesco.setText("Caberlotto Francesco");
		Color c=new Color(0,0,0);
		p=new Pallina(canvas.getBounds().width/2, canvas.getBounds().height/2, c, false, "piena");

	}
	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
	}
}
