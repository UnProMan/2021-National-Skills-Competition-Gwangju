package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class OrderSheet extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new FlowLayout(0)));
	JPanel p3 = get(new JPanel(new GridLayout(2, 1)));
	JPanel p4 = get(new JPanel(new BorderLayout()));
	
	JScrollPane scl = get(new JScrollPane(p2, 20, 31), set(new LineBorder(Color.white)));
	
	JLabel lab1 = get(new JLabel("주문표"), set(25), set(0, 40));
	JLabel lab2 = get(new JLabel("최종 금액"), set(12));
	JLabel lab3 = get(new JLabel("0원", JLabel.RIGHT));
	
	JButton btn1 = get(new JButton("주문하기"));
	
	int allpay = 0;
	
	ArrayList<String> data;
	int type;
	
	public OrderSheet(int type, ArrayList<String> data) {
		
		this.type = type;
		this.data = data;
		
		SetDial(this, "주문표", DISPOSE_ON_CLOSE, 400, 500);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Restorang(type, data);
			}
		});
		
	}

	@Override
	public void design() {
		
		add(lab1, "North");
		add(p1);
		
		p1.add(scl);
		p1.add(p3, "South");
		
		p3.add(p4);
		p3.add(btn1);
		
		p4.add(lab2);
		p4.add(lab3, "East");
		
		table();
		
	}

	public void table() {
		
		p2.removeAll();
		allpay =0;
		
		int height = 0;
		for (int i = 0; i < receipt.size(); i++) {
			
			JPanel pn1 = get(new JPanel(new BorderLayout()), set(340, 80), set(new LineBorder(Color.gray)));
			JPanel pn2 = get(new JPanel(new BorderLayout()));
			JLabel lb1 = get(new JLabel(receipt.get(i).get(1)), set(13));
			JLabel lb2 = get(new JLabel(""));
			JLabel lb3 = get(new JLabel("삭제"));
			
			pn1.add(lb1, "North");
			pn1.add(lb2);
			pn1.add(pn2, "South");
			
			String op = "";
			int pay = intnum(receipt.get(i).get(2)) * intnum(receipt.get(i).get(3));
			
			for (int j = 0; j < reoptions.size(); j++) {
				if (i + 1 == intnum(reoptions.get(j).get(0))) {
					op += reoptions.get(j).get(1);
					pay += intnum(reoptions.get(j).get(4));
				}
			}
			
			lb2.setText(op);
			pn2.add(new JLabel(receipt.get(i).get(3) + "개, " + df.format(pay) + "원"));
			pn2.add(lb3, "East");
			
			int j =i;
			
			lb3.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					for (int c = 0; c < reoptions.size(); c++) {
						if (j + 1 == intnum(reoptions.get(c).get(0))) {
							reoptions.remove(c);
						}
					}
					
					receipt.remove(j);
					
					table();
					
				}
			});
			
			p2.add(pn1);
			
			allpay += pay;
			height += 80;
			
		}
		
		p2.setPreferredSize(new Dimension(0, height + 100));
		
		lab3.setText(df.format(allpay) + "원");
		revalidate();
		repaint();

	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (receipt.isEmpty()) {
				err("주문표에 담김 메뉴가 없습니다.");
			}else {
				dispose();
				new Pay( data, allpay, type);
			}
			
		});
		
	}

}
