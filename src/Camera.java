import java.awt.Graphics;
import java.util.ArrayList;


public class Camera extends rect{
	private static final int length=800;
	private static final int height=600;
	public Camera(int x, int y){
		super(x,y,length,height);
	}
	public void Draw(Graphics g) {
		
	}
	public int getRectX(rect r){
		return r.getX()-getX();
	}
	public int getRectY(rect r){
		return r.getY()-getY();
	}
	public void Draw(Graphics g, int layer) {
		
	}
	public void Calculate(int pX, int pY, int maxX, int maxY){
		int x=pX-350;
		int y=pY-250;
		int length = getLength();
		int height = getHeight();
		if (x<0){
			x=0;
		}
		if(x+length>maxX){
			x=maxX-length;
		}
		if (y<0){
			y=0;
		}
		if(y+height>maxY){
			y=maxY-height;
		}
		Move(x,y);
	}

	public void Draw(Graphics g, int x, int y) {

	}
	@Override
	public void Draw(Graphics g, Camera c) {
		// TODO Auto-generated method stub
		
	}
}
