package Project;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Pay extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new GridLayout(2, 1)));
	JPanel p3 = get(new JPanel(new BorderLayout()));
	JPanel p4 = get(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5)));
	
	JScrollPane scl = get(new JScrollPane(p4, 20 ,31), set(new LineBorder(Color.white)));
	
	JButton btn1 = get(new JButton("선택한 결제수단으로 결제하기"));
	
	JLabel lab1 = get(new JLabel(receipt.get(0).get(1) + (receipt.size() != 1 ? "의 결제건 외 " + (receipt.size() - 1) + "건" : "")),set(15));
	JLabel lab2 = get(new JLabel("총 금액"), set(12));
	JLabel lab3 = get(new JLabel(""));
	JLabel empty = get(new JLabel("결제수단이 없습니다.", 0), setf(Color.red), set(20));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<String> data;
	
	Color w[];
	Color e[];
	
	int allpay;
	int index = -1;
	int type;
	
	public Pay(ArrayList<String> data, int allpay, int type) {
		
		this.allpay = allpay;
		this.data = data;
		this.type = type;
		
		SetDial(this, "결제", DISPOSE_ON_CLOSE, 400, 500);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new OrderSheet(type, data);
			}
		});
		
	}

	@Override
	public void design() {
		
		Query("select * from payment where user = ?;", list, member.get(0).get(0));
		
		w = new Color[list.size()];
		e = new Color[list.size()];
		
		for (int i = 0; i < e.length; i++) {
			w[i] = Color.blue;
			e[i] = Color.cyan;
		}
		
		add(p1);
		
		p1.add(p2, "North");
		if (list.isEmpty()) {
			p1.add(empty);
		}else {
			p1.add(scl);
		}
		p1.add(btn1, "South");
		
		p2.add(lab1);
		p2.add(p3);
		
		p3.add(lab2);
		p3.add(lab3, "East");
		
		lab3.setText(df.format(allpay) + "원");
		
		JPanel p;
		int height = 0;
		for (int i = 0; i < list.size(); i++) {
			
			int j = i;
			
			p4.add(p = get(new JPanel(new GridLayout(3, 1)) {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					
					Graphics2D g2 = (Graphics2D) g;
					GradientPaint gp = new GradientPaint(0, 60, w[j], 340, 60, e[j]);
					
					g2.setPaint(gp);
					g2.fillRoundRect(0, 0, 340, 120, 30, 30);
					
				}
			}, set(new EmptyBorder(20, 20, 20, 20)), set(340, 120)));
			
			String card = list.get(i).get(2);
			
			JLabel lb1 = get(new JLabel(list.get(i).get(1)),setf(Color.white), set(15));
			JLabel lb2 = get(new JLabel(card.substring(0, 4) + "-" + card.substring(4, 8) + "-" + card.substring(8, 12) + "-" + card.substring(12, 16)), setf(Color.white));
			JLabel lb3 = get(new JLabel(member.get(0).get(4)), setf(Color.white));
			
			p.add(lb1);
			p.add(lb2);
			p.add(lb3);
			
			p.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e2) {
					
					for (int i = 0; i < e.length; i++) {
						w[i] = Color.blue;
						e[i] = Color.cyan;
					}
					
					w[j] = Color.red;
					e[j] = Color.orange;
					
					index = j;
					
					repaint();
					revalidate();
					
				}
			});
			
			height += p.getPreferredSize().height;
			
		}
		
		p4.setPreferredSize(new Dimension(380, height + 100));
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (index == -1) {
				err("결제할 카드를 선택해야 합니다.");
			}else {
				
				int a = JOptionPane.showConfirmDialog(null, "정말로 " + lab3.getText() + "을 결제하시겠습니까?", "메시지", JOptionPane.YES_NO_OPTION);
				
				if (a== 0) {
					
					String pw = JOptionPane.showInputDialog(null, "결제 비밀번호를 입력해주세요.", "메시지", JOptionPane.INFORMATION_MESSAGE);
					
					if (pw == null) {
						err("사용자가 결제를 취소했습니다.");
					}else if (!pw.contentEquals(list.get(index).get(4))) {
						err("결제 비밀번호가 일치하지 않습니다.");
					}else {
						
						jop("주문이 완료되었습니다.");
						
						Update("insert into receipt values(null, ?,?,?,?,?,?);", allpay + "", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), data.get(0), member.get(0).get(0), list.get(index).get(0), "0");
						
						for (int i = 0; i < receipt.size(); i++) {
							Update("insert into receipt_detail values(null, ?,?,?, (select no from receipt order by no desc limit 1));", receipt.get(i).get(0), receipt.get(i).get(3), ( intnum(receipt.get(i).get(3)) * intnum(receipt.get(i).get(2)) ) + "" );
							for (int j = 0; j < reoptions.size(); j++) {
								if (i + 1 == intnum(reoptions.get(j).get(0))) {
									Update("insert into receipt_options values(null, ?,?, (select no from receipt_detail order by no desc limit 1));", reoptions.get(j).get(2), reoptions.get(j).get(4));
								}
							}
						}
						
						dispose();
						new User();
						
					}
					
				}
				
			}
			
		});
		
	}

}
