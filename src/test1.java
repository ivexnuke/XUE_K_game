import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;


public class test1 extends JFrame implements MouseMotionListener{
	private physics p;
	int count = 0;
	int[]x=new int[50];
	int[]y=new int[50];
	boolean begin = false;
	boolean calib=false;
	Container c;
	public static void main(String [] args){
		test1 t =new test1();
		t.setVisible(true);
		t.setSize(800,600);
	}
	public test1(){
		super("test1");
		c=getContentPane();
		this.addMouseMotionListener(this);
	}
	public void mouseDragged(MouseEvent e) {
		if(!begin){
			calib=true;
		}
		
	}
	public void mouseMoved(MouseEvent e) {
		if(calib){
			x[count]=e.getX();
			y[count]=e.getY();
			count++;
			if (count>49){
				p=new physics(x,y);
				begin=true;
				calib=false;
			}
		}
		else if (begin){
			p.update(e.getX(), e.getY());
		}
		repaint();
	}
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.BLACK);
		if(p!=null){
			System.out.println(p.calibX()+" "+p.calibY());
			System.out.println((p.returnX()-25)+"   "+(p.returnY()-25));
			g.drawLine(p.calibX()-50,p.calibY(),p.calibX()+50,p.calibY());
			g.drawLine(p.calibX(),p.calibY()-50,p.calibX(),p.calibY()+50);
			g.fillOval(p.returnX()-25, p.returnY()-25,50, 50);
		}
	}
	
}
