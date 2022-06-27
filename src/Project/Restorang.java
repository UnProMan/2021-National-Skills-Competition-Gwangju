package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Restorang extends JDialog implements Base{

	JPanel p1;
	JPanel p2 = get(new JPanel(new FlowLayout(0, 2, 2)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p3 = get(new JPanel(new FlowLayout(FlowLayout.CENTER)));
	
	JScrollPane scl1 = new JScrollPane(p2, 20, 31);
	JScrollPane scl2 = new JScrollPane(p3, 20 , 31);
	
	JTabbedPane tp = new JTabbedPane();
	
	JLabel lab1 = get(new JLabel("뒤로가기"), setf(Color.white));
	static JLabel lab2;
	JLabel lab3 = get(new JLabel(""), setf(Color.white), set(30));
	JLabel lab4 = get(new JLabel(""), setf(Color.white), set(15));
	JLabel lab5 = get(new JLabel(""), setf(Color.white));
	JLabel lab6 = get(new JLabel("♡"), setf(Color.red), set(20));
	
	ArrayList<String> data;
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	int type;
	
	public Restorang(int n,ArrayList<String> data) {
		
		this.type = n;
		
		this.data = data;
		
		SetDial(this, data.get(4), DISPOSE_ON_CLOSE, 1000, 600);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				receipt.clear();
				reoptions.clear();
				dispose();
				if (type == 0) {
					new Find(0);
				}else {
					new Favorite();
				}
			}
		});
		
	}

	@Override
	public void design() {
		
		lab2 = get(new JLabel("주문표 (" + receipt.size() + ")"), setf(Color.white));
		
		add(p1 = get(new JPanel(null) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(new Color(0, 0, 0, 120));
				
				g.drawImage(new ImageIcon(file("배경/" + data.get(0)+ ".png")).getImage(), 0, 0, 1000, 250, null);
				g.fillRect(0, 0, 1000, 250);
				
			}
		}, set(1000, 250)), "North");
		add(tp);
		
		LocalTime min = LocalTime.parse(data.get(9));
		LocalTime max = LocalTime.parse(data.get(10));
		
		int mit = (min.getHour() * 60) + min.getMinute();
		int mat = (max.getHour() * 60) + max.getMinute();
		
		lab3.setText(data.get(4));
		lab4.setText(df.format(intnum(data.get(7))) + "원 배달 수수료 / " + mit + " - " + max + " 분 / 평점 " + (data.get(12) == null ? "0.0" : data.get(12)));
		lab5.setText("<html>" + data.get(5));
		
		lab1.setBounds(30, 30, 70, 30);
		lab2.setBounds(910, 30, 80, 30);
		lab3.setBounds(30, 140, 900, 30);
		lab4.setBounds(30, 170, 900, 30);
		lab5.setBounds(30, 200, 900, 50);
		lab6.setBounds(950, 190, 50, 50);
		
		p1.add(lab1);
		p1.add(lab2);
		p1.add(lab3);
		p1.add(lab4);
		p1.add(lab5);
		p1.add(lab6);
		
		tp.add(scl1, "메뉴");
		tp.add(scl2, "리뷰");
		
		favorite();
		menu();
		review();
		
	}
	
	public void favorite() {
		
		Query("select * from favorite where seller = ? and user = ?;", list, data.get(0), member.get(0).get(0));
		
		if (list.isEmpty()) {
			lab6.setText("♡");
		}else {
			lab6.setText("♥");
		}
		
		revalidate();
		repaint();
		
	}
	
	public void menu() {
		
		Query("select * from type where seller = ?;", list, data.get(0));
		
		int height = 0;
		for (int i = 0; i < list.size(); i++) {
			
			JLabel lb1 = get(new JLabel(list.get(i).get(1)), set(20), set(900, 30));
			p2.add(lb1);
			
			Query("select * from menu where seller = ? and type = ?;", temp, data.get(0), list.get(i).get(0));
			
			for (int j = 0; j < temp.size(); j++) {
				p2.add(new Menulist(temp.get(j)));
			}
			
			height += 30 + (temp.size() / 3 + (temp.size()%3 == 0 ? 0 : 1) ) * 120;
			
		}
		
		p2.setPreferredSize(new Dimension(0, height));
		
		revalidate();
		repaint();
		
	}
	
	public void review() {
		
		Query("select r.*, u.NAME from review r, user u where seller = ? and r.user = u.no;", list, data.get(0));
		
		int height = 0;
		for (int i = 0; i < list.size(); i++) {
			
			JPanel pn1 = get(new JPanel(new BorderLayout()), set(930, 120), set(new LineBorder(Color.gray)));
			JPanel pn2 = get(new JPanel(new BorderLayout()));
			JLabel lb1 = get(new JLabel(list.get(i).get(1) + " / " + list.get(i).get(7) + " 작성함"),set(15));
			JLabel lb2 = get(new JLabel(start(intnum(list.get(i).get(3)))), set(15), setf(Color.orange));
			
			pn1.add(pn2, "North");
			pn1.add(new JLabel("<html>" + list.get(i).get(2)));
			allfont(pn1);
			
			pn2.add(lb1);
			pn2.add(lb2, "East");
			
			p3.add(pn1);
			height += pn1.getPreferredSize().height;
			
		}
		
		p3.setPreferredSize(new Dimension(0, height + 80));
		
		revalidate();
		repaint();
		
	}
	
	public String start(int n) {
		
		String st = "";
		
		for (int i = 0; i < n; i++) {
			st += "★";
		}
		
		for (int i = 0; i < 5 - n; i++) {
			st += "☆";
		}
		
		return st;
		
	}
	
	@Override
	public void action() {
		
		lab1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				receipt.clear();
				reoptions.clear();
				dispose();
				if (type == 0) {
					new Find(0);
				}else {
					new Favorite();
				}
			}
		});
		
		lab2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new OrderSheet(type, data);
			}
		});
		
		lab6.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (lab6.getText().contentEquals("♡")) {
					Update("insert into favorite values(null, ?,?);", data.get(0), member.get(0).get(0));
				}else {
					Update("delete from favorite where seller=  ? and user = ?;", data.get(0), member.get(0).get(0));
				}
				
				favorite();
				
			}
		});
		
	}

}
