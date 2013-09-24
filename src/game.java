import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
//remember to make .ini file to list levels 
public class game{
	public static void main (String [] args){
		myFrame f = new myFrame("test");
	}
}
class myFrame extends JFrame implements KeyListener{
	private Container c;
	private level l;
	private JPanel p;
	private ArrayList<String> levels;
	private int count=0;
	private boolean playing=false;
	public myFrame(String name){
		super(name);
		c=getContentPane();
		levels = new ArrayList<String>();
		addKeyListener(this);
		try {
			Scanner Kevin = new Scanner(new File("start.ini"));
			while(Kevin.hasNext()){
				levels.add(Kevin.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		p = new menu(this);
		c.add(p);
		this.setVisible(true);
		this.setSize(816,638);
		this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );  
	}
	public void keyPressed(KeyEvent e) {
		if(playing)l.keyPressed(e.getKeyText( e.getKeyCode() ));
		
	}
	public void keyReleased(KeyEvent e) {
		if(playing)l.keyReleased(e.getKeyText( e.getKeyCode() ));
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
	public void refresh(JPanel p){
		c.removeAll();
		c.add(p);
		c.invalidate();
		c.validate();
	}
	public void start(int i){
		playing=true;
		l=new level(this,new File(levels.get(i)));
		refresh(l);
	}
	public void next(){
		count++;
		if (count<levels.size()){
			System.out.println(2);
			l.load(this, new File(levels.get(count)));
			refresh(l);
		}else{
			System.out.println(1);
			count=0;
			playing=false;
			l.close();
			menu();
		}
	}
	public void gameOver(){
		p = new gameOverState(this);
		refresh(p);
	}
	public void restart(){
		l.load(this, new File(levels.get(count)));
		refresh(l);
	}
	public void menu(){
		count=0;
		p = new menu(this);
		refresh(p);
	}
	public void instructions(){
		p = new instruction(this);
		refresh(p);
	}
	public void select(){
		File f=null;
		JFileChooser chooser = new JFileChooser("bin\\maps\\");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "map files","map");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	f =  chooser.getSelectedFile();
	    	String temp = "bin\\maps\\"+f.getName();
	    	for(int i=0;i<levels.size();i++){
	    		if (temp.equals(levels.get(i))){
	    			count=i;
	    			start(0);
	    			break;
	    		}
	    	}
	    }else{
	    	menu();
	    }
	}
}
class instruction extends JPanel implements MouseListener, MouseMotionListener{
	private BufferedImage instructions;
	private TestRect next;
	private myFrame m;
	public instruction(myFrame m){
		this.m=m;
		addMouseListener(this);
		addMouseMotionListener(this);
		try {
			instructions = ImageIO.read(new File("bin\\images\\Instructions.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		next = new TestRect(390,525,85,25);
	}
	public void paint(Graphics g){
		g.drawImage(instructions,0,0,800,600,null);
		next.Draw(g);
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(next.Mouse(x,y)){
			m.menu();
			instructions=null;
			next=null;
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(next.Mouse(x,y)) next.setHighlight(true);
		else next.setHighlight(false);

		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
class menu extends JPanel implements MouseListener, MouseMotionListener{
	private TestRect start, instructions, quit, editor, select;
	private myFrame m;
	private BufferedImage img;
	public menu(myFrame m){
		this.m=m;
		addMouseListener(this);
		addMouseMotionListener(this);
		editor = new TestRect(275,300,250,50);
		start = new TestRect(325,400,150,50);
		select = new TestRect(275,500,250,50);
		instructions = new TestRect(40,500,150,50);
		quit = new TestRect(620,500,150,50);
		try {
			img = ImageIO.read(new File("bin\\images\\Title.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void paint(Graphics g){
		g.drawImage(img, 0,0,800,600,null);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void close(){
		editor=null;
		start=null;
		select=null;
		instructions=null;
		quit=null;
		img=null;
	}
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(editor.Mouse(x, y)){ 
			System.out.println("editor");
			levelEdit l = new levelEdit();
		}
		else if(start.Mouse(x, y)){
			System.out.println("start");
			m.start(0);
			close();

		}
		else if(instructions.Mouse(x,y)){
			System.out.println("instructions");
			m.instructions();
			close();

		}else if(select.Mouse(x,y)){
			System.out.println("select");
			m.select();
			close();
		}
		else if(quit.Mouse(x, y)){
			System.out.println("exit");
			System.exit(0);
		}
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
class gameOverState extends JPanel implements MouseListener, MouseMotionListener{
	private BufferedImage img;
	private myFrame m;
	private TestRect cont, menu;
	public gameOverState(myFrame m ){
		try {
			img = ImageIO.read(new File("bin\\images\\GameOver.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.m = m;
		addMouseListener(this);
		addMouseMotionListener(this);
		cont = new TestRect(100,400,125,50);
		menu = new TestRect(550,400,150,50);
	}
	public void close(){
		cont=null;
		menu=null;
	}
	public void paint(Graphics g){
		g.drawImage(img, 0, 0, 800,600,null);
		cont.Draw(g);
		menu.Draw(g);
	}
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(cont.Mouse(x,y)) cont.setHighlight(true);
		else cont.setHighlight(false);
		if(menu.Mouse(x,y)) menu.setHighlight(true);
		else menu.setHighlight(false);
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(cont.Mouse(x,y)){
			m.restart();
			close();
		}
		else if(menu.Mouse(x,y)){
			m.menu();
			close();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
class level extends JPanel implements ActionListener{
	private int[][][] layers;
	private ArrayList<rect> tangible;
	private ArrayList<rect> intangible;
	private ArrayList<NPC> characters;
	private ArrayList<rect> deathObjects;
	private ArrayList<guard> enemies;
	private File[] img;
	private int length, height, bLength, bHeight, deadCount;
	private BufferedImage[] images;
	private BufferedImage bG;
	private Player player;
	private tile door, finish;
	private Camera c;
	private Timer myTimer1= new Timer( 40, this );
	private myFrame mF;
	public level(myFrame f,File file){
		mF=f;
		tangible = new ArrayList<rect>();
		intangible= new ArrayList<rect>();
		characters= new ArrayList<NPC>();
		deathObjects = new ArrayList<rect>();
		enemies = new ArrayList<guard>();
		 
		load( f, file);
		f.validate();
	}
	public void close(){
		layers=null;
		tangible.clear();
		intangible.clear();
		characters.clear();
		deathObjects.clear();
		enemies.clear();
		img=null;
		images=null;
		bG=null;
		player=null;
		door=null;
		finish=null;
		c=null;
		myTimer1.stop();
	}
	public void load(myFrame f, File file){
		close();
		deadCount=15;
		try{
			bG=ImageIO.read(new File("bin\\images\\BG1.png"));
			Scanner Kevin = new Scanner(file);
			length = Kevin.nextInt();
			height = Kevin.nextInt();
			bLength = Kevin.nextInt();
			bHeight = Kevin.nextInt();
			layers = new int[3][height][length];
			int temp = Kevin.nextInt();
			System.out.println(Kevin.nextLine());
			images=new BufferedImage[temp];
			img=new File[temp];
			for(int i=0;i<images.length;i++){
				String tFile = Kevin.nextLine();
				img[i]=new File(tFile); 
				images[i]=ImageIO.read(img[i]);
			}
			Kevin.nextBoolean();
			for(int i=0;i<3;i++){
				for(int j=0;j<height;j++){
					for(int k=0;k<length;k++){
						layers[i][j][k]=Kevin.nextInt();
						if(i==0&&layers[i][j][k]!=-1){
							intangible.add(new tile(k*bLength,j*bHeight,bLength,bHeight,0,images[layers[i][j][k]]));
						}
						if(i==1&&layers[i][j][k]!=-1){
							tangible.add(new tile(k*bLength,j*bHeight,bLength,bHeight,0,images[layers[i][j][k]]));
						}if(i==2&&layers[i][j][k]!=-1){
							if(layers[i][j][k]==0){
								player = new Player(k*bLength,j*bHeight,bLength,bHeight);
								door= new tile(k*bLength,j*bHeight,bLength,bHeight,0,images[layers[i][j][k]]);
							}
							if(layers[i][j][k]==1){
								finish = new tile(k*bLength,j*bHeight,bLength,bHeight,0,images[layers[i][j][k]]);
							}
							if(layers[i][j][k]==2){
								characters.add(new NPC(k*bLength,j*bHeight,bLength,bHeight,layers));
							}
							if(layers[i][j][k]==3){
								deathObjects.add(new tile(k*bLength,j*bHeight,bLength,bHeight,10,images[layers[i][j][k]]));
							}
							if(layers[i][j][k]==4){
								enemies.add(new guard(k*bLength,j*bHeight,bLength,bHeight,layers));
							}
						}
					}
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("File not found 1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		c=new Camera(player.getX()-350,player.getY()-250);
		myTimer1.start();
	}
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		g.drawImage(bG,0,0,800,600,null);
		if(c.collide(door)){
			door.Draw(g,c);
		}
		if(c.collide(finish)){
			finish.Draw(g,c);
		}
		for(int i=0;i<intangible.size();i++){
			if(c.collide(intangible.get(i))){
				intangible.get(i).Draw(g,c);
			}
		}
		for(int i=0;i<tangible.size();i++){
			if(c.collide(tangible.get(i))){
				tangible.get(i).Draw(g,c);
			}
		}for(int i=0;i<characters.size();i++){
			if(c.collide(characters.get(i))){
				characters.get(i).Draw(g, c);
			}
		}
		for(int i=0;i<deathObjects.size();i++){
			if(c.collide(deathObjects.get(i))){
				deathObjects.get(i).Draw(g, c);
			}
		}
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).Draw(g, c);
		}
		player.Draw(g,c);
	}
	public void actionPerformed(ActionEvent e) {
		player.Move(tangible);
		c.Calculate(player.getX(),player.getY(),length*bLength, height*bHeight);
		for(int i=0;i<characters.size();i++){
			characters.get(i).calculate();
			if(player.getT().collide(characters.get(i))&&player.getAttack()){
				characters.get(i).stun();
			}
			if(characters.get(i).collide(player)&&!characters.get(i).getAction()&&!characters.get(i).getStun()){
				characters.get(i).action();
				player.boost();
			}
		}
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).calculate(tangible,player,c);
			if(player.getT().collide(enemies.get(i))&&player.getAttack()){
				enemies.get(i).stun();
			}
			if(Math.abs(enemies.get(i).getX()-player.getX())<300&&!enemies.get(i).getAttack()&&Math.abs(enemies.get(i).getY()-player.getY())<200&&!enemies.get(i).getStun()){
				enemies.get(i).chuck(player);
			}
			if(enemies.get(i).collide(player)&&!enemies.get(i).getStun()){
				player.kill();
			}
		}
		if(player.getY()>height*bHeight){
			player.kill();
		}
		for(int i=0;i<deathObjects.size();i++){
			if(player.collide(deathObjects.get(i))){
				player.kill();
			}
		}
		if(player.isdead()){
			deadCount--;
			if(deadCount==0){
				mF.gameOver();
			}
		}
		if (player.collide(finish)){
			mF.next();
		}
		repaint();
	}
	public void keyPressed(String temp) {
		player.keyPressed(temp);
		
	}
	public void keyReleased(String temp) {
		player.keyReleased(temp);
		
	}
}
