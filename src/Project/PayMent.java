package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class PayMent extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(10, 10, 10, 10)), set(380, 0));
	JPanel p2 = get(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5)));
	JPanel p3 = get(new JPanel(new GridLayout(14, 1, 0, 5)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p4 = get(new JPanel(new GridLayout(1, 4, 5, 5)));
	
	JScrollPane scl = get(new JScrollPane(p2, 20 ,31), set(new LineBorder(Color.white)));
	JButton btn1 = get(new JButton("간편 결제 등록하기"));
	
	JTextField txt1= gettext("카드 발급자");
	JTextField txt2= gettext("0000");
	JTextField txt3= gettext("0000");
	JTextField txt4= gettext("0000");
	JTextField txt5= gettext("0000");
	JTextField txt6= gettext("CVV (000/0000)");
	JTextField txt7= gettext("000000");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public PayMent() {
		
		SetDial(this, "결제수단 관리", DISPOSE_ON_CLOSE, 400, 500);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Mypage();
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1, "West");
		add(p3);
		
		p1.add(scl);
		
		p3.add(txt1);
		p3.add(p4);
		p3.add(txt6);
		p3.add(txt7);
		p3.add(btn1);
		
		p4.add(txt2);
		p4.add(txt3);
		p4.add(txt4);
		p4.add(txt5);
		
		cards();
		
	}
	
	public void cards() {
		
		p2.removeAll();
		
		Query("select * from payment where user = ?;", list, member.get(0).get(0));
		
		JPanel p;
		int height = 0;
		for (int i = 0; i <= list.size(); i++) {
			
			int j = i;
			
			if (i == list.size()) {
				
				p2.add(p = get(new JPanel(new BorderLayout()) {
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						
						g.setColor(Color.black);
						g.fillRect(0, 0, 340, 120);
						
					}
				}, set(340, 120)));
				JLabel lab = get(new JLabel("+",0), setf(Color.white) ,set(40));
				
				p.add(lab);
				
			}else {
				
				p2.add(p = get(new JPanel(new GridLayout(3, 1)) {
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						
						Graphics2D g2 = (Graphics2D) g;
						GradientPaint gp = new GradientPaint(0, 60, Color.blue, 340, 60, Color.cyan);
						
						g2.setPaint(gp);
						g2.fillRoundRect(0, 0, 340, 120, 30, 30);
						
					}
				}, set(new EmptyBorder(20, 20, 20, 20)), set(340, 120)));
				
				String card = list.get(i).get(2);
				
				JLabel lb1 = get(new JLabel(list.get(i).get(1)),setf(Color.white), set(15));
				JLabel lb2 = get(new JLabel(card.substring(0, 4) + "-" + card.substring(4, 8) + "-" + card.substring(8, 12)  + "-" + card.substring(12, 16)), setf(Color.white));
				JLabel lb3 = get(new JLabel(member.get(0).get(4)), setf(Color.white));
			
				p.add(lb1);
				p.add(lb2);
				p.add(lb3);
				
			}
			
			p.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					if (j == list.size()) {
						setSize(800, 500);
					}
					
				}
			});
			
			height += p.getPreferredSize().height;
			
		}
		
		p2.setPreferredSize(new Dimension(380, height + 30));
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank() || txt3.getText().isBlank() || txt4.getText().isBlank() || txt5.getText().isBlank() || txt6.getText().isBlank() || txt7.getText().isBlank()) {
				err("빈 칸 없이 입력해야 합니다.");
			}else if (!isnumbers(txt2.getText(), 4) || !isnumbers(txt3.getText(), 4) || !isnumbers(txt4.getText(), 4) || !isnumbers(txt5.getText(), 4)) {
				err("카드 번호의 각 항목은 숫자 4자리로 구성되어야 합니다.");
			}else if (!isnum(txt6.getText()) || txt6.getText().length() < 3 || txt6.getText().length() > 4) {
				err("CVV번호는 3~4자리 숫자로 구성되어야 합니다.");
			}else if (!isnumbers(txt7.getText(), 6)) {
				err("간편결제 비밀번호는 6자리 숫자로만 구성되어야 합니다.");
			}else {
				
				Query("select * from payment where ISSUER = ? and card = ?;", temp, txt1.getText(), txt2.getText() + txt3.getText() + txt4.getText() + txt5.getText());
				
				if (!temp.isEmpty()) {
					err("이미 등록되어있는 카드입니다.");
				}else {
					
					jop("카드 등록은 완료했습니다.");
					
					Update("insert into payment values(null, ?,?,?,?,?);", txt1.getText(), txt2.getText() + txt3.getText() + txt4.getText() + txt5.getText(), txt6.getText(), txt7.getText(), member.get(0).get(0));
					
					cards();
					
				}
				
			}
			
		});
		
	}

}
