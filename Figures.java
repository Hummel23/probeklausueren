package sose15_probeklausuren.probeklausueren;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class Figures extends JFrame implements MouseListener, MouseMotionListener{

	Container contentPane;
	JPanel canvas, buttons;
	JToggleButton btnCircle, btnSquare;
	JButton btnDelete;
	ButtonGroup btngroup = new ButtonGroup();
	List<Shape> fig = Collections.synchronizedList(new ArrayList<>());
	int size = 100, x, y, indexShape;
	boolean inShape = false;
	boolean rectangle, dragging = false;
	Rectangle rec;
	Ellipse2D.Double circ;

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
			for (Shape shape : fig) {
				if(shape instanceof Rectangle){
					g2.setColor(Color.green);
				}else{
					g2.setColor(Color.blue);
				}
				g2.fill(shape);
			}
			if (dragging){
				if(rectangle){
					g2.setColor(Color.green);
					g2.fill(rec);
				}else{
					g2.setColor(Color.BLUE);
					g2.fill(circ);
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

		btnDelete = new JButton("L�schen");
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String []option = {"Ja", "Nein"};
				int close = JOptionPane.showOptionDialog(Figures.this, "Willst du wirklich alles l�schen?",
						"Wirklich l�schen?", JOptionPane.YES_NO_OPTION,
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
		this.x = e.getX();
		this.y = e.getY();
		Point p = e.getPoint();

		for (Shape shape : fig) {
			if (shape.contains(p)){
				dragging = true;
				if(shape instanceof Rectangle){
					rectangle=true;
					rec = new Rectangle(p);
				}else{
					rectangle=false;
					circ = new Ellipse2D.Double();
				}
				indexShape = fig.indexOf(shape);
			}
		}
		if(dragging){
			fig.remove(indexShape);
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		int xnew = e.getX();
		int ynew = e.getY();
		Point p = e.getPoint();

		if(!inShape){
			if (btnSquare.isSelected()){
				fig.add(new Rectangle(x, y, this.size, this.size));
			}else{
				fig.add(new Ellipse2D.Double(x, y, this.size, this.size));
			}
			repaint();
		}else{
			boolean overlap = false;
			fig.remove(indexShape);
			for (Shape shape : fig) {
				if (shape.getBounds().equals(p)){
					fig.remove(shape);
					overlap = true;
				}
			}if (!overlap){
				if(rectangle){
					fig.add(new Rectangle (xnew, ynew, this.size, this.size));
				}else{
					fig.add(new Ellipse2D.Double(xnew, ynew, this.size, this.size));
				}
			}
//			inShape = false;
			dragging = false;
			repaint();
		}

	}
	@Override
	public void mouseDragged(MouseEvent e) {
		int xnew = e.getX();
		int ynew = e.getY();
		if(dragging){
			if (rectangle){
				rec.setLocation(xnew, ynew);	
			}else{
				circ.setFrame(xnew, ynew, this.size, this.size);
			}
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