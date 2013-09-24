public class useful {
	public static int gravity(int y, int a, int v, int max){
		if (v+a<max)v=v+a;
		y+=v;
		return y;
	}
	public static int water(int y, int floatConstant){
		return y-floatConstant;
	}
}
