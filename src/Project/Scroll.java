package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Base.Base;

public class Scroll extends JPanel implements Base{
	
	JLabel img;
	
	JPanel p1 = get(new JPanel(new GridLayout(2, 1)));
	
	JLabel lab1 = get(new JLabel(""), set(12));
	JLabel lab2 = get(new JLabel(""));
	
	ArrayList<String> list;
	JDialog d;
	
	public Scroll(JDialog d ,ArrayList<String> list) {
		
		this.d = d;
		this.list = list;
		
		setLayout(new BorderLayout());
		setBackground(Color.white);
		setPreferredSize(new Dimension(240, 160));
		
		design();
		action();
		
	}

	@Override
	public void design() {

		File file = new File(file("배경/" + list.get(0) + ".png"));
		
		if (file.exists()) {
			add(img = getimg("배경/" + list.get(0) + ".png", 250, 150));
		}else {
			add(img = get(new JLabel(""), setb(Color.DARK_GRAY)));
			img.setOpaque(true);
		}
		
		add(p1, "South");
		
		LocalTime min = LocalTime.parse(list.get(9));
		LocalTime max = LocalTime.parse(list.get(10));
		
		int mins = (min.getHour() * 60) + min.getMinute();
		int maxs = (max.getHour() * 60) + max.getMinute();
		
		lab1.setText(list.get(4));
		lab2.setText(df.format(intnum(list.get(7))) + "원 배달 수수료 / " + mins + " - " + maxs + "분 / 평점 " + (list.get(12) == null ? "0.0" : list.get(12)));
		
		p1.add(lab1);
		p1.add(lab2);
		
	}

	@Override
	public void action() {
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2) {
					d.dispose();
					new Restorang(0, list);
				}
				
			}
		});
		
	}

}
