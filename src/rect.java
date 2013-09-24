import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
public abstract class rect{
	private int x,y,length,height;
	public rect(int x, int y, int length, int height){
		this.x=x;
		this.y=y;
		this.length=length;
		this.height=height;
	}
	public void setSize(int newLen, int newHei){
		length=newLen;
		height=newHei;
	}
	public void Move(int x, int y){
		this.x=x;
		this.y=y;
	}public int getX(){
		return x;
	}public int getY(){
		return y;
	}public int getLength(){
		return length;
	}public int getHeight(){
		return height;
	}public boolean collide(rect rect){
		int x1 = rect.getX();
		int y1 = rect.getY();
		int y2 = y1+rect.getHeight();
		int x2 = x1+rect.getLength();
		if((x1<x||x1>x+length)&&(x2<x||x2>x+length)){
			return false;
		}
		if((y1<y||y1>y+height)&&(y2<y||y2>y+height)){
			return false;
		}
		return true;
	}public boolean Mouse(int x1, int y1){
		int x2 = x+length;
		int y2 = y+height;
		if(x1>x2||x1<x){
			return false;
		}
		else if(y1>y2||y1<y){
			return false;
		}
		return true;
	}
	public abstract void Draw(Graphics g, Camera c);
	public abstract void Draw(Graphics g, int layer);
	
}
class TestRect extends rect{
	private boolean highLighted = false;
	public TestRect(int x, int y, int length, int height){
		super(x,y,length,height);
	}
	public void Draw(Graphics g) {
		if(highLighted){
			g.setColor(Color.WHITE);
			g.drawRect(getX(),getY(),getLength(),getHeight());
		}
	}
	public void setHighlight(boolean b){
		highLighted = b;
	}
	public void Draw(Graphics g, int layer) {
		
	}
	public void Draw(Graphics g,Camera c) {
	}
}

class Player extends rect{
	private BufferedImage imagemap;
	private BufferedImage attackMap;
	private int attackCount=0;
	private TestRect t;
	private int vX;
	private int vY;
	private boolean jumped=false;
	private boolean boost = false;
	private int picLocX=0;;
	private int picLocY=0;
	private final double gravity = 2.3;
	private boolean right=true;
	private int runCnt = 0;
	private int mana=100;
	private int killcount = 0;
	private boolean dead=false;
	private boolean Attacked;
	public Player(int x, int y, int length,int height){
		super(x,y,length,height);
		t = new TestRect(x-100,y-100,250,250);
		try{
			imagemap = ImageIO.read(new File("bin\\images\\BrainMap.png"));
			attackMap = ImageIO.read(new File("bin\\images\\Attack.png"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void keyPressed(String temp) {
		if(temp.equals("A")||temp.equals("Left")){
			vX=-8;
			right=false;
		}
		else if (temp.equals("D")||temp.equals("Right")){
			vX=8;
			right=true;
		}
		if ((temp.equals("Up")||temp.equals("W"))&&!jumped){
			vY=-25;
			jumped=true;
		}
		if(temp.equals("Space")){
			Attacked=true;
		}
		
	}
	public void kill(){
		dead=true;
	}
	public boolean isdead(){
		return dead;
	}
	public boolean getAttack(){
		return Attacked;
	}
	public void keyReleased(String temp) {
		if((temp.equals("A")||temp.equals("Left"))&&vX<0){
			vX=0;
			right=false;
		}
		if((temp.equals("Right")||temp.equals("D"))&&vX>0){
			vX=0;
			right=true;
		}
		if ((temp.equals("Up")||temp.equals("W"))&&vY<0){
			if(!boost){
				vY=0;
			}
		}
		if(temp.equals("Space")){
			Attacked=false;
		}
	}
	public void boost(){
		vY=-40;
		jumped=true;
		boost=true;
	}
	public void keyTyped(KeyEvent e) {
		
	}
	public void Move(ArrayList<rect> blocks){
		if(vY>2.3){
			jumped=true;
		}
		if(!dead){
			vY+=gravity;
			int tX = super.getX()+vX;
			int tY = super.getY()+vY;
			Move(tX,tY);
			for(int i=0;i<blocks.size();i++){
				if (collide(blocks.get(i))){
					rect top = new TestRect(getX()+10,getY(), 30,25);
					rect left = new TestRect(getX(),getY()+15,24,10);
					rect right = new TestRect(getX()+25,getY()+15,24,10);
					rect bottom = new TestRect(getX()+10,getY()+25,30,25);
					if(vY>0){
						while(blocks.get(i).collide(bottom)){
							tY--;
							vY=0;
							Move(tX,tY);
							bottom.Move(tX+10,tY+25);
							jumped=false;
							boost=false;
						}
						while(blocks.get(i).collide(top)){
							tY++;
							vY=0;
							Move(tX,tY);
							top.Move(tX+10, tY);
						}
						while(blocks.get(i).collide(left)){
							tX++;
							Move(tX,tY);
							left.Move(tX,tY+15);
						}
						while(blocks.get(i).collide(right)){
							tX--;
							Move(tX,tY);
							right.Move(tX+25,tY+15);
						}
					}
					else{
						while(blocks.get(i).collide(top)){
							tY++;
							vY=0;
							Move(tX,tY);
							top.Move(tX+10, tY);
						}
						while(blocks.get(i).collide(bottom)){
							tY--;
							vY=0;
							Move(tX,tY);
							bottom.Move(tX+10,tY+25);
							jumped=false;
						}
						while(blocks.get(i).collide(left)){
							tX++;
							Move(tX,tY);
							left.Move(tX,tY+15);
						}
						while(blocks.get(i).collide(right)){
							tX--;
							Move(tX,tY);
							right.Move(tX+25,tY+15);
						}
					}
				}
			}
			Move(tX,tY);
			t.Move(tX-100, tY-100);
		}
		if(mana==0){
			Attacked=false;
		}
		if (Attacked) {
			mana--;
		}
	}
	public void Draw(Graphics g, int layer) {
		
	}
	public TestRect getT(){
		return t;
	}
	public void Draw(Graphics g, Camera c) {
		g.setColor(Color.YELLOW);
		g.fillRect(0,0,20,110);
		g.setColor(Color.black);
		g.fillRect(5,105,10,-100);
		g.setColor(Color.BLUE);
		g.fillRect(5,105,10,-mana);
		int x = c.getRectX(this);
		int y = c.getRectY(this);
		int length = getLength();
		int height = getHeight();
		if(!dead){
			if (Attacked){
				attackCount++;
				if(attackCount>=4){
					attackCount=0;
				}
				int x1 = c.getRectX(t);
				int y1 = c.getRectY(t);
				g.drawImage(attackMap, x1, y1, x1+250, y1+250, attackCount*250, 0, (attackCount+1)*250, 250, null);
			}
			if(jumped){
				picLocY=100;
				if(vY<=0){
					if(right){
						g.drawImage(imagemap, x,y, x+length, y+height, 0, picLocY, 50, picLocY+50, null);
					}else g.drawImage(imagemap, x+length,y, x, y+height, 0, picLocY, 50, picLocY+50, null);
				}
				else{
					if(right){
						g.drawImage(imagemap, x,y, x+length, y+height, 50, picLocY, 100, picLocY+50, null);
					}else g.drawImage(imagemap, x+length,y, x, y+height, 50, picLocY, 100, picLocY+50, null);
				}
			}
			else if (vX!=0){
				picLocY=50;
				picLocX=50*runCnt;
				if (runCnt>8){
					runCnt=0;
				}
				runCnt++;
				if(right){
					g.drawImage(imagemap, x,y, x+length, y+height, picLocX, picLocY, picLocX+50, picLocY+50, null);
				}
				else{
					g.drawImage(imagemap,  x+length,y,x, y+height, picLocX, picLocY, picLocX+50, picLocY+50, null);
				}
			}
			else{
				if(right){
					g.drawImage(imagemap, x,y, x+length, y+height, 0, 0, 50, 50, null);
				}else g.drawImage(imagemap, x+length,y, x, y+height, 0, 0, 50, 50, null);
			}
		}else{
			g.drawImage(imagemap,x,y,x+length,y+height,killcount*50,150,(killcount+1)*50,200,null);
			killcount++;
			if (killcount>=6){
				killcount=7;
				try {
					this.finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}
}
class tile extends rect{
	private BufferedImage img;
	private int decrease;
	public tile(int x, int y, int length, int height,int decrease, BufferedImage img){
		super(x+decrease,y+decrease,length-2*decrease,height-2*decrease);
		this.img=img;
		this.decrease = decrease;
	}
	public void Draw(Graphics g){
		g.drawImage(img, super.getX(), super.getY(), super.getLength(), super.getHeight(), null);
	}

	@Override
	public void Draw(Graphics g, int layer) {
		// TODO Auto-generated method stub
		
	}
	public void Draw(Graphics g,Camera c) {
		int x = c.getRectX(this);
		int y = c.getRectY(this);
		g.drawImage(img, x-decrease, y-decrease, super.getLength()+2*decrease, super.getHeight()+2*decrease, null);
		
	}
}
class spear extends rect{
	private double vX;
	private double vY;
	private BufferedImage img;
	guard g;
	public spear(guard g,int x, int y,int tx,int ty){
		super(x,y,50,6);
		try {
			img = ImageIO.read(new File("bin\\images\\spear.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initCalcs(tx,x,ty,y);
		this.g = g;
	}
	public void initCalcs(int targetX, int getX, int targetY, int getY){
		int tempX = targetX-getX;
		int tempY = targetY-getY;
		double dist = Math.sqrt(tempX*tempX+tempY*tempY);
		double ratio = 20.0/dist;
		vX=tempX*ratio;
		vY=tempY*ratio;
	}
	public void Move(ArrayList<rect> blocks, Player p, Camera c){
		Move((int)(getX()+vX),(int)(getY()+vY));
		if(!c.collide(this)){
			this.g.setAttack();
		}
		for(int i=0;i<blocks.size();i++){
			if(blocks.get(i).collide(this)){
				g.setAttack();
			}
		}
		if(p.collide(this)){
			g.setAttack();
			p.kill();
			System.out.println("hit");
		}
	}
	public void Draw(Graphics g,Camera c) {
		int x = c.getRectX(this);
		int y = c.getRectY(this);  
		if(vX>0){
			g.drawImage(img, x+getLength(), y, -super.getLength(), super.getHeight(), null);
		}
		else{
			g.drawImage(img, x, y, super.getLength(), super.getHeight(), null);
		}
		
	}
	@Override
	public void Draw(Graphics g, int layer) {
		// TODO Auto-generated method stub
		
	}
}
class guard extends AI{
	private BufferedImage img;
	private boolean chkpoint=true;
	private boolean stop = false;
	private boolean attack;
	private boolean right;
	public boolean stun = false;
	private int count = 10;
	private int vX=0;
	public int stuncount = 75;
	private int runCount=4;
	private spear s;
	public guard(int x, int y,int length,int height, int[][][] layers){
		super( x,  y, length, height, layers);
		try {
			this.img=ImageIO.read(new File("bin\\images\\Guardsmap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void stun(){
		stun=true;
	}
	public boolean getStun(){
		return stun;
	}
	public void Draw(Graphics g, Camera c) {
		int x = c.getRectX(this);
		int y = c.getRectY(this);
		if(stun){
			g.setColor(Color.WHITE);
			g.drawString("ZZZ", x, y);
		}
		runCount++;
		runCount=runCount%4;
		if(vX>0||right){
			g.drawImage(img, x+50, y, x, y+50, runCount*50, 0, (runCount+1)*50, 50, null);
		}
		else{
			g.drawImage(img, x, y, x+50, y+50, runCount*50, 0, (runCount+1)*50, 50, null);
		}
		if(attack){
			s.Draw(g,c);
		}
		
	}
	public boolean getAttack(){
		return attack;
	}
	public void chuck(Player p){
		attack = true;
		stop = true;
		if(getX()-p.getX()<=0){
			right=true;
		}
		else right=false;
		s = new spear(this, getX(),getY()+30, p.getX(),p.getY()+30);
		
	}
	public void setAttack(){
		attack=false;
		s = null;
	}
	public void calculate(ArrayList<rect> blocks, Player p, Camera c) {
		if(!stun){	
			if(lim[0]==lim[1]){
				stop=true;
			}
			if(!stop&&(int)(Math.random()*250+1)==1){
				stop=true;
				if(getX()!=lim[0]||getX()!=lim[1]){
					chkpoint=!chkpoint;
				}
			}
			if(!stop){
				if(chkpoint){
					vX=5;
				}
				else{
					vX=-5;
				}
				super.Move(getX()+vX,getY());
				if(getX()>=lim[0]){
					chkpoint = false;
				}
				if(getX()<=lim[1]){
					chkpoint = true;
				}
			}
			else{
				count--;
				if (count<=0){
					count=30;
					stop=false;
				}
			}
			if(attack){
				s.Move(blocks, p, c);
			}
		}else{
			stuncount--;
			if(stuncount<=0){
				stun=false;
				stuncount=75;
			}
		}
	}
}
abstract class AI extends rect{
	protected int [] lim;
	public AI(int x, int y,int length,int height, int[][][] layers){
		super(x,y,length,height);
		lim = findLim(x/length,y/height,layers,new int[layers[0].length][layers[0][0].length]);
		lim[0]=(lim[0]-1)*50;
		lim[1]=(lim[1]+1)*50;
	}
	public int[] findLim(int x, int y, int [][][] layers, int[][] checked){
		if(x>layers[0][0].length||x<0){
			return new int[]{x,x};
		}
		if(checked[y][x]==1){
			return new int []{layers[0][0].length+2,-1};
		}
		if (layers[1][y][x]>-1||layers[1][y+1][x]==-1){
			return new int[]{x,x};
		}
		else{
			checked[y][x]=1;
			int []t1=findLim(x+1,y,layers,checked);
			int []t2=findLim(x-1,y,layers,checked);
			int max = Math.max(t1[1],t2[1]);
			int min = Math.min(t1[0],t2[0]);
			return new int[] {max,min};
		}
	}
	public void Draw(Graphics g, int layer) {
		
	}
	public abstract void Draw(Graphics g,Camera c);
}
class NPC extends AI{
	private BufferedImage img;
	private boolean chkpoint=true;
	private boolean stop = false;
	private boolean action;
	private boolean stun=false;
	private int stuncount=75;
	private int count = 10;
	private int vX=0;
	private int runCount=16;
	private int boostCount=0;
	public NPC(int x, int y,int length,int height, int[][][] layers){
		super( x,  y, length, height, layers);
		try {
			this.img=ImageIO.read(new File("bin\\images\\SpringMap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean getStun(){
		return stun;
	}
	public void action(){
		stop=true;
		action=true;
	}
	public boolean getAction(){
		return action;
	}
	public void stun(){
		stun=true;
	}
	public void calculate(){
		if(!stun){
			if(!stop&&(int)(Math.random()*250+1)==1){
				stop=true;
				if(getX()!=lim[0]||getX()!=lim[1]){
					chkpoint=!chkpoint;
				}
			}
			if(!stop){
				if(chkpoint){
					vX=5;
				}
				else{
					vX=-5;
				}
				super.Move(getX()+vX,getY());
				if(getX()>=lim[0]){
					chkpoint = false;
				}
				if(getX()<=lim[1]){
					chkpoint = true;
				}
			}
			else{
				count--;
				if (count<=0){
					count=10;
					stop=false;
					action=false;
				}
			}
		}else{
			stuncount--;
			if(stuncount<=0){
				stun=false;
				stuncount=75;
			}
		}
	}
	public void Draw(Graphics g, int layer) {
		
	}
	public void Draw(Graphics g, Camera c) {
		int x = c.getRectX(this);
		int y = c.getRectY(this);
		if(stun){
			g.setColor(Color.WHITE);
			g.drawString("ZZZ", x, y);
		}
		if(stop){
			if(getAction()){
				g.drawImage(img, x+getLength(), y, x, y+getHeight(), boostCount*50,100, (boostCount+1)*50, 150, null);
				boostCount++;
				if (boostCount>3){
					action=false;
					boostCount=0;
				}
			}
			else{
				g.drawImage(img, x+getLength(), y, x, y+getHeight(), 0,0,50,50, null);
			}
		}
		else{
			runCount++;
			runCount=runCount%4;
			if(vX>0){
				g.drawImage(img, x, y, x+getLength(), y+getHeight(), runCount*50, 50, (runCount+1)*50, 100, null);
			}
			else{
				g.drawImage(img, x+getLength(), y, x, y+getHeight(), runCount*50, 50, (runCount+1)*50,100, null);
			}
		}
	}
}