package edu.berkeley.wtchoi.cc;

import java.io.Serializable;
import java.util.LinkedList;
import java.io.BufferedWriter;

public class MonkeyView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5186309675577891457L;
	
	private int x;
	private int y;
	private int width;
	private int height;
	private LinkedList<MonkeyView> children;
	
	@SuppressWarnings("unchecked")
	public MonkeyView(int ix, int iy, int iw, int ih, LinkedList<MonkeyView> ic){
		x = ix;
		y = iy;
		width = iw;
		height = ih;
		
		if(ic != null) children = (LinkedList<MonkeyView>)ic.clone();
		else children = null;
	}
	
	public int getX(){return x;}
	
	public int getY(){return y;}
	
	public int getW(){return width;}
	
	public int getH(){return height;}
	
	public String toString(){
		java.io.StringWriter sw = new java.io.StringWriter();
		BufferedWriter buffer = new BufferedWriter(sw);
		try{
			MonkeyView.toString(buffer,this,0);
			buffer.flush();
		}
		catch(Exception e)
		{
			System.out.println("Error occur!");
		}
		
		return sw.toString();
	}
	
	private static void toString(BufferedWriter buffer, MonkeyView mv, int depth) throws java.io.IOException{
		for(int i=0;i<depth;i++){
			buffer.write("  ");
		}
		
		buffer.write("<");
		buffer.write(Integer.toString(mv.x));
		buffer.write(",");
		buffer.write(Integer.toString(mv.y));
		buffer.write(">");
		buffer.newLine();
		
		if(mv.children == null) return;
		
		for(MonkeyView child: mv.children){
			toString(buffer,child,depth+1);
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	LinkedList<MonkeyView> getChildren(){
		return (LinkedList<MonkeyView>)children.clone();
	}
}
