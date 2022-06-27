package Project;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Find extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(5, 5)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new BorderLayout()), set(new LineBorder(Color.gray)), set(200, 0));
	JPanel p3 = get(new JPanel(new GridLayout(14, 1, 0, 10)), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p4 = get(new JPanel(new FlowLayout(0, 5, 5)));
	
	JScrollPane scl = get(new JScrollPane(p4, 20, 31), set(new LineBorder(Color.white)));
	
	JTextField txt1 = gettext("검색할 음식점 이름을 입력하세요.");
	JTextField txt2 = gettext("최소 가격");
	JTextField txt3 = gettext("최대 가격");
	
	JComboBox com1 = new JComboBox();
	
	JRadioButton rdo1 = get(new JRadioButton("기본 정렬"));
	JRadioButton rdo2 = get(new JRadioButton("평점 정렬"));
	JRadioButton rdo3 = get(new JRadioButton("배달 비용 정렬"));
	ButtonGroup gb = new ButtonGroup();
	
	JLabel lab1 = get(new JLabel("평점"));
	JLabel lab2 = get(new JLabel("가격 필터"));
	
	JButton btn1 = get(new JButton("적용"));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	int n;
	
	public Find(int n) {
		
		this.n = n;
		
		SetDial(this, "검색", DISPOSE_ON_CLOSE, 1000, 600);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new User();
			}
		});
		
	}

	@Override
	public void design() {
		
		Query("SELECT * FROM eats.category;", list);
		com1.addItem("모두");
		for (ArrayList<String> list : list) {
			com1.addItem(list.get(1));
		}
		
		gb.add(rdo1);
		gb.add(rdo2);
		gb.add(rdo3);
		
		add(p1);
		
		p1.add(txt1, "North");
		p1.add(p2, "West");
		p1.add(scl);
		
		p2.add(p3);
		
		p3.add(com1);
		p3.add(lab1);
		p3.add(rdo1);
		p3.add(rdo2);
		p3.add(rdo3);
		p3.add(lab2);
		p3.add(txt2);
		p3.add(txt3);
		p3.add(btn1);
		
		com1.setSelectedIndex(n);
		rdo1.setSelected(true);
		
		if (!filter.isEmpty()) {
			txt1.setText(filter.get(0));
			com1.setSelectedItem(filter.get(1));
			rdo1.setSelected(filter.get(2).contentEquals("1"));
			rdo2.setSelected(filter.get(2).contentEquals("2"));
			rdo3.setSelected(filter.get(2).contentEquals("3"));
			txt2.setText(filter.get(3));
			txt3.setText(filter.get(4));
		}
		
		find();
		
	}
	
	public void find() {
		
		p4.removeAll();
		
		String min = txt2.getText().isBlank() ? "0" : txt2.getText();
		String max = txt3.getText().isBlank() ? "0" : txt3.getText();
		
		if (!isnum(min) || !isnum(max)) {
			err("가격 필터에 숫자만 입력 가능합니다.");
		}else if (!txt3.getText().isBlank() && intnum(min) > intnum(max)) {
			err("최저 가격이 최대 가격보다 클 수 없습니다.");
		}else {
			
			String sql = " and s.name like '%" + txt1.getText() + "%'";
			sql += com1.getSelectedIndex() == 0 ? "" : " and s.category = " + com1.getSelectedIndex();
			
			String order = " order by ";
			if (rdo1.isSelected()) {
				order += "s.no";
			}else if (rdo2.isSelected()) {
				order += "13 desc";
			}else {
				order += "DELIVERYFEE";
			}
			
			String having = txt2.getText().isBlank() && txt3.getText().isBlank() ? "" : " having";
			
			if (!txt2.getText().isBlank()) {
				having += having.contentEquals(" having") ? " round(avg(price), 0) >= " + min : " and round(avg(price), 0) >= " + min;
			}
			
			if (!txt3.getText().isBlank()) {
				having += having.contentEquals(" having") ? " round(avg(price), 0) <= " + max : " and round(avg(price), 0) <= " + max;
			}
			
			Query("select s.*, min(COOKTIME), max(COOKTIME), round(avg(price), 0), (select round(avg(rate), 1) from review where review.seller = s.no) from seller s, menu m where s.NO = m.seller" + sql + " group by s.no" + having + order, temp);
			
			
			for (int i = 0; i < temp.size(); i++) {
				p4.add(new Scroll(this, temp.get(i)));
			}
			
			p4.setPreferredSize(new Dimension(0, (temp.size() / 3  + 1) * 170));
			
			filter.clear();
			
			filter.add(txt1.getText());
			filter.add(com1.getSelectedItem().toString());
			filter.add(rdo1.isSelected() ? "1" : rdo2.isSelected() ? "2" : "3");
			filter.add(txt2.getText());
			filter.add(txt3.getText());
			
			revalidate();
			repaint();
			
		}
		
	}
	
	public void action() {
		
		txt1.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					find();
				}
				
			}
		});
		
		btn1.addActionListener(e->{
			find();
		});
		
	}

}
