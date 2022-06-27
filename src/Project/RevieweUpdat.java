package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class RevieweUpdat extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(5, 5)), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p2 = get(new JPanel(new BorderLayout()));
	JPanel p3 = get(new JPanel(new FlowLayout(0, 0, 0)));
	
	JLabel lab[] = new JLabel[5];
	JButton btn1 = get(new JButton("수정"));
	
	JTextField txt1 = gettext("제목");
	JTextArea txt2;
	
	ArrayList<String> data;
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	int index = 0;
	
	public RevieweUpdat(ArrayList<String> data) {
		
		this.data = data;
		
		SetDial(this, "리뷰 수정", DISPOSE_ON_CLOSE, 350, 400);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Reivew();
			}
		});
		
	}

	@Override
	public void design() {
		
		Query("select * from review where user = ? and seller = ? and RECEIPT = ?;", list, member.get(0).get(0), data.get(5), data.get(0));
		
		index = intnum(list.get(0).get(3));
		
		txt2 = get(new JTextArea() {
			public void paint(Graphics g) {
				super.paint(g);
				
				if (!this.getText().isBlank()) {
					return;
				}
				
				g.setColor(Color.gray);
				g.drawString("내용", this.insets().left, this.insets().top + g.getFontMetrics().getMaxAscent());
				
			}
		}, set(new LineBorder(Color.LIGHT_GRAY)));
		
		add(p1);
		
		p1.add(txt1, "North");
		p1.add(txt2);
		p1.add(p2, "South");
		
		p2.add(p3);
		p2.add(btn1, "East");
		
		for (int i = 0; i < lab.length; i++) {
			p3.add(lab[i] = get(new JLabel(index - 1 >= i ? "★" : "☆"), setf(Color.orange), set(15)));
		}
		
		txt2.setLineWrap(true);
		
		txt1.setText(list.get(0).get(1));
		txt2.setText(list.get(0).get(2));
		
	}

	@Override
	public void action() {
		
		for (int i = 0; i < lab.length; i++) {
			
			int n = i;
			
			lab[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					for (int j = 0; j < lab.length; j++) {
						lab[j].setText("☆");
					}
					
					for (int j = 0; j <= n; j++) {
						lab[j].setText("★");
					}
					
					index = n + 1;
					
					revalidate();
					repaint();
					
				}
			});
			
		}
		
		btn1.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank()) {
				err("내용을 입력해주세요.");
			}else if (index == 0) {
				err("평점을 선택해주세요.");
			}else {
				
				jop("리뷰를 수정했습니다.");
				
				Update("update review set title = ?, CONTENT = ?, rate = ? where no = ?;", txt1.getText(), txt2.getText(), index + "", list.get(0).get(0));
				
				dispose();
				new Reivew();
				
			}
			
		});
		
	}

}
