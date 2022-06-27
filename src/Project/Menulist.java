package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Base.Base;

public class Menulist extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()));
	
	JLabel img;
	JLabel lab1 = get(new JLabel(""),set(13));
	JLabel lab2 = get(new JLabel(""));
	JLabel lab3 = get(new JLabel(""), set(11));
	
	ArrayList<String> data;
	
	public Menulist(ArrayList<String> data) {
		
		this.data = data;
		
		setPreferredSize(new Dimension(310, 110));
		setBackground(Color.white);
		setLayout(new BorderLayout());
		setBorder(new LineBorder(green));
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		File file = new File(file("메뉴/" + data.get(0) + ".png"));
		
		if (file.exists()) {
			add(img = getimg("메뉴/" + data.get(0) + ".png", 100, 110), "East");
		}
		add(p1);
		
		LocalTime cook = LocalTime.parse(data.get(4));
		int time = (cook.getHour() * 60) + cook.getMinute();
		
		lab1.setText(data.get(1));
		lab2.setText("<html>" + data.get(2));
		lab3.setText(df.format(intnum(data.get(3))) + "원 / " + time + "분 소요" );
		
		p1.add(lab1, "North");
		p1.add(lab2);
		p1.add(lab3, "South");
		
		lab2.setVerticalAlignment(JLabel.TOP);
		
	}

	@Override
	public void action() {
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2) {
					new ReceiptInsert(data);
				}
				
			}
		});
		
	}

}
