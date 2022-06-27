package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Login extends JDialog implements Base{

	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new GridLayout(0, 1, 0, 10)));
	JPanel p3 = get(new JPanel(new GridLayout(2, 1)));
	JPanel p4 = get(new JPanel());
	
	JLabel lab1 = get(new JLabel("돌아오신것을 환영합니다."), set(25));
	JLabel lab2 = get(new JLabel("이메일 주소 또는 휴대폰 번호로 로그인하세요."));
	JLabel lab3 = get(new JLabel(""), setf(Color.red));
	JLabel lab4 = get(new JLabel("<html><b><u>계정 만들기"), setf(green));
	
	JButton btn1 = get(new JButton("로그인"));
	
	JTextField txt1 = gettext("이메일 주소 또는 휴대폰 번호");
	
	String tbl[] = "user, seller, rider".split(", ");
	String type = "";
	
	public Login() {
		
		SetDial(this, "로그인", DISPOSE_ON_CLOSE, 400, 250);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1);
		
		p1.add(lab1, "North");
		p1.add(p2);
		p1.add(p3, "South");
		
		p3.add(btn1);
		p3.add(p4);
		
		p4.add(new JLabel("기능배달이 처음이십니까?"));
		p4.add(lab4);
		
		next();
		
	}

	public void next() {
		
		p2.removeAll();
		
		p2.add(lab2);
		p2.add(txt1);
		p2.add(lab3);
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {

		btn1.addActionListener(e->{
			
			if (txt1.getText().isBlank()) {
				lab3.setText("모든 항목을 입력해야 합니다.");
				return;
			}
			
			if (type.isBlank()) {
				
				for (int i = 0; i < tbl.length; i++) {
					Query("select * from " + tbl[i] + " where EMAIL = ? or PHONE = ?;", member, txt1.getText(), txt1.getText());
					if (!member.isEmpty()) {
						type = tbl[i];
						break;
					}
				}
				
				if (type.isBlank()) {
					lab3.setText("이 이메일 또는 휴대폰을 찾을 수 없습니다.");
				}else {
					
					lab2.setText("비밀번호를 입력하고 로그인하세요.");
					txt1 = gettext("대소문자, 숫자, 특수기호를 포함한 8자 이상");
					lab3.setText("");
					
					next();
					
				}
				
			}else {
				
				if (!member.get(0).get(3).contentEquals(txt1.getText())) {
					lab3.setText("입력한 비밀번호가 올바르지 않습니다.");
				}else {
					
					dispose();
					if (type.contentEquals("user")) {
						new User();
					}else if (type.contentEquals("seller")) {
						new Seller();
					}else {
						new Delivery();
					}
					
				}
				
			}
			
		});
		
		lab4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new MemberInsert();
			}
		});
		
	}

	public static void main(String[] args) {
		new Login();
	}
	
}
