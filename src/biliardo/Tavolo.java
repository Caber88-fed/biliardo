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
	Buca[] b=new Buca[0];
	Stecca st = new Stecca();
	Pallina gioc;

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
		Transform trOg = new Transform(display);
		createContents();
		shell.open();
		shell.layout();

		penna.getTransform(trOg);

		while (!shell.isDisposed()) {
			
			double rad=Math.toRadians(gioc.getDirezione());
			double deltaX=Math.cos(rad)*10;
			double deltaY=Math.sin(rad)*10;
			int x=gioc.getX();
			int y=gioc.getY();
			
			penna.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
			penna.fillOval(gioc.getX(), gioc.getY(), Pallina.getRaggio()*2, Pallina.getRaggio()*2);
			
			x=x+(int)deltaX;
			y=y+(int)deltaY;
			gioc.setX(x);
			gioc.setY(y);
			if(y<0) {
				//y=0;
				gioc.setDirezione(-gioc.getDirezione());
			}
			if(y>canvas.getBounds().height-10) {
				//y=canvas.getBounds().height-10;
				gioc.setDirezione(-gioc.getDirezione());
			}
			if(x<0) {
				//x=0;
				gioc.setDirezione(180-gioc.getDirezione());
			}
			if(x>canvas.getBounds().width-10) {
				//x=canvas.getBounds().width-10;
				gioc.setDirezione(180-gioc.getDirezione());
			}
			gioc.setX(x);
			gioc.setY(y);
			penna.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 0, 0))));
			
			
			//SCRITTURA BUCHI
			for(int i=0;i<b.length;i++) {
				penna.fillOval(b[i].getX(), b[i].getY(), 50, 50);
			}
			///////////////////////////////////////////////////////////
			
			
			/*penna.fillOval(b[0].getX(), -15, 50, 50);
			penna.fillOval(canvas.getBounds().width/2-25, -15, 50, 50);
			penna.fillOval(canvas.getBounds().width-(50-15), -15, 50, 50);
			penna.fillOval(-15, canvas.getBounds().height-(50-15), 50, 50);
			penna.fillOval(canvas.getBounds().width/2-25, canvas.getBounds().height-(50-15), 50, 50);
			penna.fillOval(canvas.getBounds().width-(50-15), canvas.getBounds().height-(50-15), 50, 50);
			*/
			
			//palline
			
			//p[0]=new Pallina((3/4)*canvas.getBounds().width-Pallina.getRaggio(), canvas.getBounds().height/2, 0, 0);
			for (int i = 0; i<p.length; i++) {
				penna.fillOval(p[i].getX(), p[i].getY(), Pallina.getRaggio()*2, Pallina.getRaggio()*2);
			}
			
			penna.fillOval(gioc.getX(), gioc.getY(), Pallina.getRaggio()*2, Pallina.getRaggio()*2);
			
			
			
			
			//PALLINE CHE SCOMPAIONO SE DENTRO BUCA
			for(int i=0;i<p.length;i++) {
				int c=0;
				while(c<6) {
					if(b[c].Dentro(p[i].getX(), p[i].getY(), 25)==true) {
						penna.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
						penna.fillOval(p[i].getX(), p[i].getY(), 50, 50);
						p[i]=null;
					}
					c++;
				}
			}
			///////////////////////////////////////////////////////////
			
			
			
			//PALLINA GIOCATORE CHE RITORNA AL CENTRO SE DENTRO BUCA
			int c=0;
			while(c<6) {
				if(b[c].Dentro(gioc.getX(), gioc.getY(), 25)==true) {
					penna.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
					penna.fillOval(gioc.getX(), gioc.getY(), 60, 60);
					gioc.setX(25*canvas.getBounds().width/100-Pallina.getRaggio());
					gioc.setY(canvas.getBounds().height/2);
				}
				c++;
			}
			///////////////////////////////////////////////////////////
			
			
			/*int xx=25*canvas.getBounds().width/100-Pallina.getRaggio();
			int yy=canvas.getBounds().height/2;
			penna.fillOval(xx, yy, Pallina.getRaggio()*2, Pallina.getRaggio()*2);*/

			//stecca
			/*penna.getTransform(tr);
			tr.translate(gioc.getX() + Pallina.getRaggio(), gioc.getY() + Pallina.getRaggio());
			tr.rotate(st.getRotazione());
			penna.setTransform(tr);
			penna.fillRectangle(Pallina.getRaggio()*2, -5, 60, 5);
			penna.setTransform(trOg);

			st.setRotazione(st.getRotazione()+1);
*/
			
			
			
			
			if (!display.readAndDispatch()) {
				
				display.sleep();
			}
			
			
			
			
			
		}
		tr.dispose();
		trOg.dispose();
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
		int xb=75*canvas.getBounds().width/100-Pallina.getRaggio();
		int yb=canvas.getBounds().height/2;
		
		for (int i = 0; i<nColonne; i++) {
			int yc = yb;
			for (int ii = 0; ii<i+1; ii++) {
				p = Arrays.copyOf(p, p.length+1);
				p[p.length-1] = new Pallina(xb, yc, 0, 0);
				yc-=Pallina.getRaggio()*2;
			}
			xb += Pallina.getRaggio()*2;
			yb += Pallina.getRaggio();
		}
		 gioc = new Pallina(25*canvas.getBounds().width/100-Pallina.getRaggio(), canvas.getBounds().height/2, 0, 70);
		 ///////////////////////////////////////////////////////////
		 
		 
		 //CREAZIONE BUCHE
		 b=Arrays.copyOf(b, b.length+1);
			b[b.length-1]=new Buca(-15,-15);
			b=Arrays.copyOf(b, b.length+1);
			b[b.length-1]=new Buca(canvas.getBounds().width/2-25,-15);
			b=Arrays.copyOf(b, b.length+1);
			b[b.length-1]=new Buca(canvas.getBounds().width-(50-15),-15);
			b=Arrays.copyOf(b, b.length+1);
			b[b.length-1]=new Buca(-15,canvas.getBounds().height-(50-15));
			b=Arrays.copyOf(b, b.length+1);
			b[b.length-1]=new Buca(canvas.getBounds().width/2-25,canvas.getBounds().height-(50-15));
			b=Arrays.copyOf(b, b.length+1);
			b[b.length-1]=new Buca(canvas.getBounds().width-(50-15),canvas.getBounds().height-(50-15));
		 ///////////////////////////////////////////////////////////
			
			
			
	}
	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
	}
}
