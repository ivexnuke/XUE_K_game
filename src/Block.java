import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Block extends rect{
	private BufferedImage[] img;
	private File[] file;
	public Block(int x, int y, int length, int height){
		super (x,y,length,height);
		img = new BufferedImage[3];
		file = new File[3];
	}
	public void Draw (Graphics g, int layer){
		int x=super.getX();
		int y=super.getY();
		int length=super.getLength();
		int height=super.getHeight();
		g.setColor(Color.BLACK);
		g.drawRect(x,y,length,height);
		if (img[layer]!=null){
			g.drawImage(img[layer], x,y,length,height,null);
		}
	}public void setImage(File img, int layer){
		file[layer]=img;
		if(file[layer]!=null){
			try {
				this.img[layer]=ImageIO.read(file[layer]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			this.img[layer]=null;
		}
	}
	public File toOut(int l){
		return file[l];
	}
	public void Draw(Graphics g) {}
	@Override
	public void Draw(Graphics g, Camera c) {
		// TODO Auto-generated method stub
		
	}
}