
public class physics {
	private final int threshold = 40;
	private int vX;
	private int vY;
	private int x;
	private int y;
	private int calibX;
	private int calibY;
	public physics(int[]firstX, int firstY[]){
		vX=0;
		vY=0;
		x=400;
		y=300;
		calibX=calibrateX(firstX);
		calibY=calibrateY(firstY);
	}
	public int calibrateX(int[] firstX){
		int sum=0;
		for(int i=0;i<firstX.length;i++){
			sum+=firstX[i];
		}
		sum=sum/firstX.length;
		return sum;
	}
	public int calibrateY(int[] firstY){
		int sum=0;
		for(int i=0;i<firstY.length;i++){
			sum+=firstY[i];
		}
		sum=sum/firstY.length;
		return sum;
	}
	public void update( int accelX, int accelY){
		int deltaAX = accelX-calibX;
		if(Math.abs(deltaAX)>threshold){
			calibX=accelX;
			vX=deltaAX;
		}
		int deltaAY = accelY-calibY;
		if(Math.abs(deltaAY)>threshold){
			calibY=accelY;
			vY=deltaAY;
		}
		x+=vX;
		y+=vY;
	}
	public int calibX(){
		return calibX;
	}
	public int calibY(){
		return calibY;
	}
	public int returnX(){
		if(x>800)return 800;
		if(x<0)return 0;
		return x;
	}
	public int returnY(){
		if(y>600)return 600;
		if(y<0)return 0;
		return y;
	}
}
