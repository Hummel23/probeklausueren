package sose15_probeklausuren.probeklausueren;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;

public class Figures extends JFrame implements MouseListener, MouseMotionListener{

	Container contentPane;
	JPanel canvas, buttons;
	JToggleButton btnCircle, btnSquare;
	JButton btnDelete;
	ButtonGroup btngroup = new ButtonGroup();
	ConcurrentHashMap<Point, Boolean> fig = new ConcurrentHashMap <Point, Boolean>();
	static int size = 100;
	int indexShape, distanceX, distanceY;
	Point p, rememberP;	
	boolean dragged = false, rememberValue;

	public Figures (){
		super ("Kreise und Rechtecke");
		contentPane = this.getContentPane();

		this.canvas = new DrawCanvas();
		canvas.setBackground(Color.white);
		canvas.addMouseListener(this);
		this.add(canvas, BorderLayout.CENTER);

		buttons = initButtons();
		this.add(buttons, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600,600);
		this.setVisible(true);

	}

	class DrawCanvas extends JPanel{

		@Override
		protected void paintComponent (Graphics g){
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			for (Point key : fig.keySet()) {
				if(fig.get(key)){
					g2.setColor(Color.green);
					g2.fillRect(key.x-(size/2), key.y-(size/2), size, size);
				}else{
					g2.setColor(Color.blue);
					g2.fillOval(key.x-(size/2), key.y-(size/2), size, size);
				}
			}
			if (dragged){
				if(rememberValue){
					g2.setColor(Color.green);
					g2.fillRect(rememberP.x-(size/2), rememberP.y-(size/2), size, size);
				}else{
					g2.setColor(Color.blue);
					g2.fillOval(rememberP.x-(size/2), rememberP.y-(size/2), size, size);
				}
			}
		}
	}

	JPanel initButtons(){
		JPanel panel = new JPanel();

		btnCircle = new JToggleButton("Kreise");
		btnCircle.setSelected(true);
		btnSquare = new JToggleButton("Rechtecke");
		btngroup.add(btnSquare);
		btngroup.add(btnCircle);

		btnDelete = new JButton("Löschen");
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String []option = {"Ja", "Nein"};
				int close = JOptionPane.showOptionDialog(Figures.this, "Willst du wirklich alles löschen?",
						"Wirklich löschen?", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, null,option, option[1]);
				switch(close){
				case JOptionPane.YES_OPTION:
					Figures.this.fig.clear();
					Figures.this.repaint();
					break;
				}
			}
		});
		panel.add(btnCircle);
		panel.add(btnSquare);
		panel.add(btnDelete);

		return panel;

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mousePressed(MouseEvent e) {
		p= e.getPoint();
		for (Point key : fig.keySet()) {
			if(new Rectangle(key.x, key.y,size, size).contains(p)){
				dragged = true;
				rememberP = (Point) key.clone();
				rememberValue = fig.get(key);
				distanceX = p.x - key.x;
				distanceY = p.y - key.y;
			}
		}
		if (dragged){
			for (Point key : fig.keySet()) {
				if(key.equals(rememberP)){
					fig.remove(key);
				}
			}
		}
		repaint();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(dragged){
			boolean deleted = false;
			rememberP = e.getPoint(); 
			for (Point key : fig.keySet()) {
				if ( !(fig.get(key) == rememberValue)){
					int overlapX = Math.abs(key.x - rememberP.x);
					int overlapY = Math.abs(key.x - rememberP.y);
					if(overlapX <= 20 && overlapY <= 20){
						fig.remove(key);
						deleted = true;
					}
				}	
			}
			if (!deleted){
				fig.put(rememberP, rememberValue);
			}
			dragged = false;
		}else{
			boolean rect;
			if (btnSquare.isSelected()){
				rect = true;
			}else{
				rect = false;
			}
			fig.put(p, rect);
		}
		repaint();
	}
	@Override
	public void mouseDragged(MouseEvent e) {

		if (dragged){
			rememberP = e.getPoint();
		}
		repaint();
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public static void main(String[] args) {
		Figures f = new Figures();

	}
}
