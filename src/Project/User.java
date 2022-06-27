package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class User extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(null));
	JPanel p3 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(10, 10, 10, 10)), set(0, 200));
	JPanel p4 = get(new JPanel(new GridLayout(1, 0, 10, 10)));
	JPanel p;
	
	JLabel lab1 = get(new JLabel("로그아웃"));
	JLabel lab2 = get(new JLabel("마이페이지"));
	JLabel lab3 = get(new JLabel("카테고리별로 탐색"), set(20));
	JLabel lab[] = new JLabel[10];
	
	String category[] = "모두, 베이커리, 디저트 , 편의점, 중식, 일식, 멕시칸, 아메리칸, 한식, 알코올".split(", ");
	Color color[] = {Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY};
	
	ArrayList<Component> img = new ArrayList<>();
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public User() {
		
		SetDial(this, "기능 배달", DISPOSE_ON_CLOSE, 1000, 600);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Login();
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1, "North");
		add(p2);
		add(p3, "South");
		
		p3.add(lab3, "North");
		p3.add(p4);
		
		p1.add(lab1);
		p1.add(lab2);
		
		Query("select s.* , c.NAME from receipt r, seller s, category c where s.NO = r.SELLER and s.CATEGORY = c.NO group by r.seller order by count(*) desc limit 5;", list);
		
		for (int i = 0; i < list.size(); i++) {
			
			int j = i;
			
			img.add(p = new JPanel() {
				public void paint(Graphics g) {
					super.paint(g);
					
					this.setBackground(Color.white);
					Graphics2D g2 = (Graphics2D) g;
					GradientPaint gp = new GradientPaint(0, 150, new Color(255, 153, 204, 120), 960, 150, new Color(204, 153, 255, 120));
					
					g2.setPaint(gp);
					g2.drawImage(new ImageIcon(file("배경/" + list.get(j).get(0) + ".png")).getImage(), 0, 0, 960, 300, null);
					g2.fillRect(0, 0, 990, 300);
					
					g2.setColor(Color.white);
					g2.setFont(new Font("맑은 고딕", 1, 35));
					g2.drawString("#" + (j + 1), 100, 150);
					g2.drawString(list.get(j).get(4), 100, 190);
					
					g2.setFont(new Font("맑은 고딕", 0, 20));
					g2.drawString(list.get(j).get(9), 100, 220);
					
				}
			});
			
			p.setBounds(i * 1000 + 5, 0, 960, 300);
			p2.add(img.get(i));
			
		}
		
		for (int i = 0; i < category.length; i++) {
			
			int j =i;
			
			p4.add(lab[i] = get(new JLabel() {
				public void paint(Graphics g) {
					super.paint(g);
					
					g.setColor(color[j]);
					g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 80, 80);
					
					g.setColor(Color.black);
					g.setFont(new Font("맑은 고딕", 0, 12));
					g.drawString(category[j], 35 - (category[j].length() * 4) + 2, 120);
					
					String file = "";
					if (j== 0) {
						file = file("카테고리/all.png");
					}else {
						file = file("카테고리/" + j + ".png");
					}
					
					g.setColor(Color.white);
					g.fillOval(8, 8, 70, 70);
					g.drawImage(new ImageIcon(file).getImage(), 15, 15, 50, 50, null);
					
				}
			}));
			
			lab[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					for (int j2 = 0; j2 < color.length; j2++) {
						color[j2] = Color.LIGHT_GRAY;
					}
					
					color[j] = Color.orange;
					
					repaint();
					revalidate();
					
					if (e.getClickCount() == 2) {
						dispose();
						new Find(j);
					}
					
				}
			});
			
		}
		
		new Thread(()->{
			try {
				while (true) {
					
					Thread.sleep(3000);
					
					for (int k = 0; k < 1000; k++) {
						
						img.get(0).setBounds(5 - k, 0, 960, 300);
						img.get(1).setBounds(1000 - k + 5, 0, 960, 300);
						
						Thread.sleep(1);
						
						repaint();
						revalidate();
						
					}
					
					img.add(img.get(0));
					img.remove(0);
					
				}
			} catch (Exception e) {
			}
		}).start();
		
	}

	@Override
	public void action() {

		lab1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new Login();
			}
		});
		
		lab2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new Mypage();
			}
		});
		
	}
}
