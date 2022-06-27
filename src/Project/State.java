package Project;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class State extends JDialog implements Base{
	
	JLabel lab1 = get(new JLabel("통계"), set(30), set(new EmptyBorder(0, 5, 0, 0)));
	JPanel p1;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public State() {
		
		SetDial(this, "통계", DISPOSE_ON_CLOSE, 700, 550);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Seller();
			}
		});
		
	}

	@Override
	public void design() {
		
		Query("select m.NAME, sum(rd.COUNT) from receipt r, receipt_detail rd, menu m where r.NO = rd.RECEIPT and rd.MENU = m.NO and r.SELLER = ? group by m.no order by 2 desc limit 5;", list, member.get(0).get(0));
		
		add(lab1, "North");
		add(p1 = get(new JPanel() {
			public void paint(Graphics g) {
				super.paint(g);
				
				int max = intnum(list.get(0).get(1));
				
				for (int i = 0; i < list.size(); i++) {
					
					float a = (float) (Double.parseDouble(list.get(i).get(1)) / max);
					int w = (int) (a * 650);
					
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					GradientPaint gp = new GradientPaint(0,0, new Color(200, 230, 250), w, 0, new Color(45 + (i * 10), 105 + (i * 10), 200 + (i * 10)));
					
					g2.setPaint(gp);
					g2.fillRect(5, 50 + (70 * i), w, 40);
					
					g2.setColor(Color.black);
					g2.setFont(new Font("맑은 고딕", 1, 20));
					g2.drawString(list.get(i).get(0) + " (" + list.get(i).get(1) + ")", 5, 40 + (i * 70));
					
				}
				
			}
		}));
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
