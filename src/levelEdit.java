import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class levelEdit extends JFrame implements ActionListener{
	private Container c;
	private JButton open, nu;
	public levelEdit(){
		super("XUE_K_LEVELEDITOR");
		c = getContentPane();
		c.setLayout(new FlowLayout());
		open = new JButton("open");
		nu = new JButton("new");
		open.addActionListener(this);
		nu.addActionListener(this);
		c.add(open);
		c.add(nu);
		this.setVisible(true);
		this.setSize(300,100);
		
	}
	public static void main (String [] args) throws IOException{
		levelEdit le = new levelEdit();
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==nu){
			NewFile nf = new NewFile();
			nf.setVisible(true);
			nf.setSize(300,200);
		}
		if(e.getSource()==open){
			File f=null;
			JFileChooser chooser = new JFileChooser("bin\\maps\\");
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "map files","map");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	f =  chooser.getSelectedFile();
		    	ProjectFrame p = new ProjectFrame(f.getName(), f,true);
		    }
		}
	}
}
class ProjectFrame extends JFrame implements ActionListener, ItemListener{
	private int wid, hei, bWid, bHei;
	private Project p;
	private ArrayList<Icon>imgs;
	private Container c;
	private ArrayList<JButton>imageButton;
	private JPanel misc, layers, images;
	private JButton[] layer;
	private JCheckBox[] visible;
	private JButton clear, save,zoom;
	private JTextField z;
	private File file;
	private String name; 
	private JScrollPane scrollPane, iButtons;
	public ProjectFrame(String name, File file, boolean load){
		super(name);
		this.name=name;
		this.file=file;
		try{
			Scanner read = new Scanner(file);
			wid = read.nextInt();
			hei = read.nextInt();
			bWid = read.nextInt();
			bHei = read.nextInt();
		}catch(FileNotFoundException e){
			System.out.println("File not found");
		}
		c=getContentPane();
		c.setLayout(new BorderLayout());
		this.setSize(wid*bWid+400,hei*bHei+100);
		this.setVisible(true);
		
		p=new Project(this,wid,hei,bWid,bHei,file);
		
		visible = new JCheckBox[3];
		layers = new JPanel();
		layers.setLayout(new BoxLayout(layers, BoxLayout.Y_AXIS));
		JLabel l = new JLabel("layers:");
		layers.add(l);
		layer= new JButton[3];
		layer[0]=new JButton("Background");
		layer[0].addActionListener(this);
		layers.add(layer[0]);
		visible[0]=new JCheckBox("show");
		visible[0].setSelected(true);
		visible[0].addItemListener(this);
		layers.add(visible[0]);
		layer[1]=new JButton("Foreground");
		layer[1].addActionListener(this);
		layers.add(layer[1]);
		visible[1]=new JCheckBox("show");
		visible[1].setSelected(true);
		visible[1].addItemListener(this);
		layers.add(visible[1]);
		layer[2]=new JButton("Interactive");
		layer[2].addActionListener(this);
		layers.add(layer[2]);
		visible[2]=new JCheckBox("show");
		visible[2].setSelected(true);
		visible[2].addItemListener(this);
		layers.add(visible[2]);
		c.add(layers, BorderLayout.EAST);
		
		
		imageButton = new ArrayList<JButton>();
		imgs = new ArrayList<Icon>();
		images = new JPanel();
		images.setLayout(new BoxLayout(images, BoxLayout.Y_AXIS));
		imageButton.add(new JButton("New Image"));
		images.add(imageButton.get(0));
		imageButton.get(0).addActionListener(this);
		images.setPreferredSize( new Dimension(150, hei*bHei) );
		iButtons = new JScrollPane(images);
		
		c.add(iButtons, BorderLayout.WEST);
		
		misc = new JPanel(new FlowLayout());
		
		clear = new JButton("remove");
		clear.addActionListener(this);
		misc.add(clear);
		zoom = new JButton("zoom");
		zoom.addActionListener(this);
		misc.add(zoom);
		z= new JTextField("100");
		misc.add(z);
		
		save = new JButton("save");
		save.addActionListener(this);
		c.add(save, BorderLayout.NORTH);
		c.add(misc, BorderLayout.SOUTH);
		
		
		if(load){
			p.load();
			try {
				Scanner Kevin = new Scanner(file);
				Kevin.nextLine();
				int temp = Kevin.nextInt();
				System.out.println(temp);
				System.out.println(Kevin.nextLine());
				for(int i=0;i<temp;i++){
					String t = Kevin.nextLine();
					System.out.println(t);
					imgs.add(new ImageIcon(t));
					imageButton.add(new JButton());
					int loc = imageButton.size()-1;
					imageButton.get(loc).setIcon(imgs.get(loc-1));
					imageButton.get(loc).addActionListener(this);
					images.add(imageButton.get(loc));
				}
				c.validate();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		p.setPreferredSize( new Dimension(wid*bWid, hei*bHei) );
		scrollPane = new JScrollPane( p );
		c.add(scrollPane, BorderLayout.CENTER);
	}
	public void addImg(){
	    File f=null;
		JFileChooser chooser = new JFileChooser("bin\\images\\");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "JPG,GIF,PNG images", "jpg", "gif","png");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	f =  chooser.getSelectedFile();
	    	imgs.add(new ImageIcon("bin\\images\\"+f.getName()));
			imageButton.add(new JButton());
			int loc = imageButton.size()-1;
			imageButton.get(loc).setIcon(imgs.get(loc-1));
			imageButton.get(loc).addActionListener(this);
			images.add(imageButton.get(loc));
	    }
	}
	
	public void actionPerformed(ActionEvent e) {
		for(int i=1;i<imageButton.size();i++){
			if(e.getSource()==imageButton.get(i)){
				File temp = null;
				temp = new File(imgs.get(i-1).toString());
				p.changeImage(temp);
			}
		}
		for(int i=0;i<layer.length;i++){
			if(layer[i]==e.getSource()){
				p.changeLayer(i);
			}
		}
		if (e.getSource()==imageButton.get(0)){
			addImg();
		}
		else if(e.getSource()==clear){
			p.changeImage(null);
		}
		else if(e.getSource()==save){
			try {
				p.save(name, imgs);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==zoom){
			int temp = Integer.parseInt(z.getText());
			p.zoom(temp);
		}
		c.validate();
	}
	public void itemStateChanged(ItemEvent e) {
		for(int i=0;i<3;i++){
			if(e.getSource()==visible[i]){
				p.setLayer(i);
				break;
			}
		}
	}
}
class Project extends JPanel implements MouseListener{
	private Block[][] grid;
	private int wid, hei, bWid, bHei,x,y;
	private File out = null;
	private File img;
	private int l=0;
	private boolean[] show={true,true,true};
	private ProjectFrame c;
	public Project(ProjectFrame c,int wid, int hei, int bWid, int bHei, File file){
		this.c=c;
		this.wid=wid;
		this.hei=hei;
		this.bWid=bWid;
		this.bHei=bHei;
		this.addMouseListener(this);
		out=file;
		grid = new Block[hei][wid];
		for(int i=0;i<hei;i++){
			for(int j=0;j<wid;j++){
				grid[i][j]=new Block(j*bWid,i*bHei,bWid,bHei);
			}
		}
	}
	public void setLayer(int loc){
		show[loc]=(!show[loc]);
		repaint();
	}
	//JFILECHOOSER
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		x=e.getX();
		y=e.getY();
		for(int i=0;i<hei;i++){
			for(int j=0;j<wid;j++){
				if (grid[i][j].Mouse(x,y)){
					grid[i][j].setImage(img,l);
					repaint();
					break;
				}
			}
		}
	}
	public void zoom(int percent){
		int nusize = bWid*percent/100;
		for(int i=0;i<hei;i++){
			for(int j=0;j<wid;j++){
				grid[i][j].setSize(nusize,nusize);
				grid[i][j].Move(j*nusize,i*nusize);
			}
		}
		setPreferredSize(new Dimension(wid*nusize,hei*nusize));
		c.validate();
		repaint();
	}
	public void changeLayer(int l){
		this.l=l;
		repaint();
	}public void changeImage(File img){
		this.img = img;
	}
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0,0,(int)getPreferredSize().getWidth(),(int)getPreferredSize().getHeight());
		for(int i=0;i<hei;i++){
			for(int j=0;j<wid;j++){
				for(int k=0;k<3;k++){
					if(show[k]){
						grid[i][j].Draw(g, k);
					}
				}
			}
		}
	}
	public void load(){
		try {
			Scanner Kevin = new Scanner(out);
			Kevin.nextLine();
			int imgs = Kevin.nextInt();
			File[] files = new File[imgs];
			Kevin.nextLine();
			for(int i=0;i<imgs;i++){
				String temp = Kevin.nextLine();
				files[i]=new File(temp);
			}
			if(Kevin.nextBoolean()){
				for(int i=0;i<3;i++){
					for(int j=0;j<hei;j++){
						for(int k=0;k<wid;k++){
							int temp1 = Kevin.nextInt();
							if (temp1!=-1){
								grid[j][k].setImage(files[temp1], i);
							}
						}
					}
				}
			}
			Kevin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void save(String name, ArrayList<Icon> list) throws IOException{
		PrintWriter map = new PrintWriter(new BufferedWriter(new FileWriter(out.toString())));
		String init = wid+" "+hei+" "+bWid+" "+bHei+"\n";
		init+=list.size()+"\n";
		ArrayList<String>imgs = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			init+=list.get(i).toString()+"\n";
			imgs.add(list.get(i).toString());
		}
		init+=true+"\n";
		for(int h = 0;h<3;h++){
			for(int i=0;i<hei;i++){
				for(int j=0;j<wid;j++){
					File temp = grid[i][j].toOut(h);
					if (temp == null){
						init+="-1"+" ";
					}
					else{
						init+=imgs.indexOf(temp.getPath())+" ";
					}
				}
				init+="\n";
			}
		}
		map.write(init);
		map.close();
		
		String listMaps="";
		boolean alreadyThere = false;
		Scanner Kevin = new Scanner(new File("start.ini"));
		while(Kevin.hasNext()){
			String line = Kevin.nextLine();
			if(line.equals("bin\\maps\\"+out.getName())){
				alreadyThere = true;
			}
			listMaps+=line+"\n";
		}
		if(!alreadyThere){
			listMaps+="bin\\maps\\"+out.getName();
		}
		listMaps.trim();
		Kevin.close();
		PrintWriter initializeFile = new PrintWriter(new BufferedWriter(new FileWriter("start.ini")));
		initializeFile.write(listMaps);
		initializeFile.close();
	}
	public void mouseReleased(MouseEvent e) {}
}
class NewFile extends JFrame implements ActionListener{
	private Container c;
	private JLabel width, height, bWidth, bHeight,name;
	private JTextField w,h,bW,bH,n;
	private JPanel middle;
	private JButton ok;
	private int wid, hei, bWid, bHei; 
	private String nom ="0";
	public NewFile(){
		super("New");
		c = getContentPane();
		c.setLayout(new BorderLayout());
		middle = new JPanel(new GridLayout(5,2,5,5));
		
		width = new JLabel("Width:");
		middle.add(width);
		w = new JTextField(10);
		middle.add(w);
		
		height = new JLabel("Height:");
		middle.add(height);
		h = new JTextField(10);
		middle.add(h);
		
		bWidth = new JLabel("Block's Width:");
		middle.add(bWidth);
		bW = new JTextField(10);
		middle.add(bW);
		
		bHeight=new JLabel("Block's Height:");
		middle.add(bHeight);
		bH = new JTextField(10);
		middle.add(bH);
		
		name = new JLabel("Name:");
		middle.add(name);
		n = new JTextField(10);
		middle.add(n);
		
		c.add(middle,BorderLayout.CENTER);
		ok = new JButton("ok");
		ok.addActionListener(this);
		c.add(ok,BorderLayout.SOUTH);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ok){
			nom = n.getText();
			wid = Integer.parseInt(w.getText());
			hei = Integer.parseInt(h.getText());
			bWid = Integer.parseInt(bW.getText());
			bHei = Integer.parseInt(bH.getText());
			try {
				writeIntro();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			this.setVisible(false);
			ProjectFrame p = new ProjectFrame(nom,new File("bin/maps/"+nom+".map"),true);
		}
	}
	public void writeIntro() throws IOException{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("bin/maps/"+nom+".map")));
		String init = wid+" "+hei+" "+bWid+" "+bHei;
		init+="\n"+5;
		init+="\n"+"bin\\images\\DoorStart.png";
		init+="\n"+"bin\\images\\FlagFinish.png";
		init+="\n"+"bin\\images\\SpringStand.png";
		init+="\n"+"bin\\images\\DeathSpikes.png";
		init+="\n"+"bin\\images\\Guardsmen.png";
		init+="\n"+false;
		out.write(init);
		out.close();
	}
}