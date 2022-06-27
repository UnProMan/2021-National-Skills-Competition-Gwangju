package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import Base.Base;

public class ReceiptInsert extends JDialog implements Base{
	
	JPanel pn = get(new JPanel(new BorderLayout(10, 10)));
	JPanel p1 = get(new JPanel(new GridLayout(2, 1)));
	JPanel p2 = get(new JPanel(new FlowLayout(0, 10, 10)));
	JPanel p3 = get(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)));
	
	JScrollPane scl = get(new JScrollPane(p2, 20, 31), set(new LineBorder(Color.white)));
	
	JButton btn1 = get(new JButton("-"));
	JButton btn2 = get(new JButton("+"));
	JButton btn3 = get(new JButton("1개를 주문표에 추가하기"));
	
	JLabel lab1 = get(new JLabel(""), set(15));
	JLabel lab2 = get(new JLabel(""));
	JLabel lab3 = get(new JLabel("1", 0), set(80, 30));
	JLabel empty = get(new JLabel("옵션이 없는 상품입니다.", 0), setf(Color.red) , set(20));
	JLabel img;
	
	ArrayList<String> data;
	ArrayList<ArrayList<String>> group = new ArrayList<>();
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	JRadioButton rdo[];
	
	public ReceiptInsert(ArrayList<String> data) {
		
		this.data = data;
		
		SetDial(this, "주문표에 추가", DISPOSE_ON_CLOSE, 400, 800);
		design();
		action();
		setModal(true);
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		Query("select distinct title from options where menu = ?;", group, data.get(0));
		Query("select * from options where menu = ?;", list, data.get(0));
		
		File file = new File(file("메뉴/" + data.get(0) + ".png"));
	
		if (file.exists()) {
			img = getimg("메뉴/" + data.get(0) + ".png", 400, 400, set(new LineBorder(Color.black)));
		}else {
			img = get(new JLabel(""), set(400, 400), setb(Color.white), set(new LineBorder(Color.black)));
			img.setOpaque(true);
		}
		
		add(img, "North");
		add(pn);
		
		pn.add(p1, "North");
		if (group.isEmpty()) {
			pn.add(empty);
			empty.setVerticalAlignment(JLabel.TOP);
		}else {
			pn.add(scl);
		}
		pn.add(p3, "South");
		
		lab1.setText("<html>" + data.get(1));
		lab2.setText("<html>" + data.get(2));
		
		p1.add(lab1);
		p1.add(lab2);
		
		p3.add(btn1);
		p3.add(lab3);
		p3.add(btn2);
		p3.add(btn3);
		
		options();
		
	}
	
	public void options() {
		
		rdo = new JRadioButton[list.size()];
		
		int height = 0;
		for (int i = 0; i < group.size(); i++) {
			
			JLabel title = get(new JLabel(group.get(i).get(0)), set(13), set(380, 30));
			ButtonGroup bg  = new ButtonGroup();
			
			p2.add(title);
			
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).get(1).contentEquals(group.get(i).get(0))) {
					
					rdo[j] = get(new JRadioButton(list.get(j).get(2)), set(220, 30));
					JLabel lab = get(new JLabel(list.get(j).get(3).contentEquals("0") ? "추가 비용 없음" : "+" + list.get(j).get(3), JLabel.RIGHT), set(120, 30));
					bg.add(rdo[j]);
					
					p2.add(rdo[j]);
					p2.add(lab);
					
				}
			}
			
			
		}
		
		p2.setPreferredSize(new Dimension(380, list.size() * 45 + 45));
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (!lab3.getText().contentEquals("1")) {
				
				int count = intnum(lab3.getText()) - 1;
				
				lab3.setText(count + "");
				btn3.setText(count + "개를 주문표에 추가하기");
				
			}
			
		});
		
		btn2.addActionListener(e->{
			
			if (!lab3.getText().contentEquals("9")) {
				
				int count = intnum(lab3.getText()) + 1;
				
				lab3.setText(count + "");
				btn3.setText(count + "개를 주문표에 추가하기");
				
			}
			
		});
		
		btn3.addActionListener(e->{
			
			int count = 0;
			
			for (int i = 0; i < rdo.length; i++) {
				if (rdo[i].isSelected()) {
					count++;
				}
			}
			
			if (count != group.size()) {
				err("옵션을 모두 선택해주세요.");
			}else {
				
				ArrayList r = new ArrayList<>();
				
				r.add(data.get(0));
				r.add(data.get(1));
				r.add(data.get(3));
				r.add(lab3.getText());
				receipt.add(r);
				
				for (int i = 0; i < rdo.length; i++) {
					ArrayList row = new ArrayList<>();
					if (rdo[i].isSelected()) {
						
						row.add(receipt.size() + "");
						row.add(rdo[i].getText());
						row.add(list.get(i).get(0));
						row.add(list.get(i).get(1));
						row.add(list.get(i).get(3));
						
						reoptions.add(row);
						
					}
				}
				
				Restorang.lab2.setText("주문표 (" + receipt.size() + ")");
				
				dispose();
				
			}
			
		});
		
	}

}
