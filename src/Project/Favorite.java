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
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Favorite extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5)));
	JScrollPane scl = get(new JScrollPane(p1, 20, 31), set(new LineBorder(Color.white)));
	
	JLabel img;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Favorite() {
		
		SetDial(this, "찜한 음식점", DISPOSE_ON_CLOSE, 400, 550);
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
		
		Query("select s.*, min(COOKTIME), max(COOKTIME), round(avg(price), 0), (select round(avg(rate), 1) from review where review.seller = s.no), c.NAME, round(avg(cooktime), -2) from seller s, menu m, favorite f, category c where s.NO = m.seller and f.user = ? and c.NO = s.CATEGORY and f.SELLER = s.no group by s.no;", list, member.get(0).get(0));
		
		add(scl);
		
		File file = new File("");
		
		int height = 0;
		for (int i = 0; i < list.size(); i++) {
			
			file = new File(file("프로필/" + list.get(i).get(0) + ".png"));
			if (file.exists()) {
				img = getimg("프로필/" + list.get(i).get(0) + ".png", 80, 80, set(new LineBorder(Color.black)));
			}else {
				img = get(new JLabel(""), setb(Color.white), set(80, 80), set(new LineBorder(Color.black)));
				img.setOpaque(true);
			}
			
			JPanel pn1 = get(new JPanel(new BorderLayout()), set(new LineBorder(Color.LIGHT_GRAY)), set(360, 80));
			JPanel pn2 = get(new JPanel(new BorderLayout(5, 5)), set(new EmptyBorder(5, 5, 5, 5)));
			JPanel pn3 = get(new JPanel(new GridLayout(0, 1)));
			
			pn1.add(pn2);
			
			pn2.add(img, "West");
			pn2.add(pn3);
			
			int avg = (int) (intnum(list.get(i).get(14)) * 0.01);
			
			pn3.add(new JLabel("<html><b>" + list.get(i).get(4)));
			pn3.add(new JLabel(list.get(i).get(13)));
			pn3.add(new JLabel(df.format(intnum(list.get(i).get(7))) + "원 배달 수수료 / 조리 평균 " + avg + "분"));
			
			int j = 0;
			pn1.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					if (e.getClickCount() == 2) {
						dispose();
						new Restorang(1, list.get(j));
					}
					
				}
			});
			
			p1.add(pn1);
			height += p1.getPreferredSize().height;
			
		}
		
		p1.setPreferredSize(new Dimension(0, height + 150));
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
