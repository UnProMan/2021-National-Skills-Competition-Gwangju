package Project;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;

import Base.Base;

public class Mypage extends JDialog implements Base{
	
	JButton btn1 = get(new JButton("주문내역"));
	JButton btn2 = get(new JButton("리뷰관리"));
	JButton btn3 = get(new JButton("결제수단관리"));
	JButton btn4 = get(new JButton("찜한 음식점"));
	
	public Mypage() {
		
		SetDial(this, "마이페이지", DISPOSE_ON_CLOSE, 300, 300);
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
		
		setLayout(new GridLayout(0, 1));
		
		add(btn1);
		add(btn2);
		add(btn3);
		add(btn4);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			dispose();
			new Buylist();
		});
		
		btn2.addActionListener(e->{
			dispose();
			new Reivew();
		});
		
		btn3.addActionListener(e->{
			dispose();
			new PayMent();
		});
		
		btn4.addActionListener(e->{
			dispose();
			new Favorite();
		});
		
	}

}
