package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class MemberInsert extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new GridLayout(0, 1, 0, 10)));
	JPanel p3 = get(new JPanel(new GridLayout(2, 1)));
	JPanel p4 = get(new JPanel());
	
	JLabel lab1 = get(new JLabel("시작하기."), set(25));
	JLabel lab2 = get(new JLabel("전화번호를 입력하세요(필수)"));
	JLabel lab3 = get(new JLabel(""), setf(Color.red));
	JLabel lab4 = get(new JLabel("<html><b><u>로그인"), setf(green));
	
	static JButton btn1;
	JButton btn2 = get(new JButton("일반 회원가입"));
	JButton btn3 = get(new JButton("판매자 회원가입"));
	JButton btn4 = get(new JButton("라이더 회원가입"));
	
	String st1[] = "전화번호, 이메일, 비밀번호, 이름, 주소, 배달 수수료".split(", ");
	String st2[] = "000-0000-0000/ example@example.next/ 대소문자, 숫자, 특수기호를 포함한 8자 이상/ 홍길동/ 클릭하여 주소를 입력하기/ 1000".split("/ ");
	String tbl[] = "user, seller, rider".split(", ");
	
	static JTextField txt1;
	
	int index = 0;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<String> data = new ArrayList<>();
	
	JComboBox com = new JComboBox<>();
	
	public MemberInsert() {
		
		SetDial(this, "회원가입", DISPOSE_ON_CLOSE, 400, 250);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Login();
			}
		});
		
	}

	@Override
	public void design() {
		
		Query("SELECT * FROM eats.category;", list);
		for (ArrayList<String> list : list) {
			com.addItem(list.get(1));
		}
		
		btn1 = get(new JButton("다음"));
		
		add(p1);
		
		p1.add(lab1, "North");
		p1.add(p2);
		p1.add(p3, "South");
		
		p3.add(btn1);
		p3.add(p4);
		
		p4.add(new JLabel("이미 기능배달 회원입시니까?"));
		p4.add(lab4);
		allfont(p4);
		
		next();
		
	}

	public void next() {
		
		p2.removeAll();
		
		lab2.setText(st1[index] + "를 입력하세요 (필수)");
		txt1 = gettext(st2[index]);
		lab3.setText("");
		
		p2.add(lab2);
		p2.add(txt1);
		p2.add(lab3);
		
		txt1.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (index == 4) {
					e.consume();
				}
			}
		});
		
		txt1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (index == 4) {
					new Map(0, null);
				}
			}
		});
		
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
			
			if (index == 0) {
				
				if (Pattern.matches("\\d{3}-\\d{4}-\\d{4}", txt1.getText()) == false) {
					lab3.setText("전화번호 형식이 올바르지 않습니다.");
				}else {
					
					for (int i = 0; i < tbl.length; i++) {
						Query("select * from " + tbl[i] + " where phone = ?;", list, txt1.getText());
						if (!list.isEmpty()) {
							lab3.setText("이 번호의 계정이 이미 있습니다.");
							return;
						}
					}
					
					index++;
					data.add(txt1.getText());
					next();
					
				}
				
			}else if (index == 1) {
				
				if (Pattern.matches("^[a-zA-Z0-9]*@(outlook|daum|naver|gmail){1}.(net|com|kr){1}", txt1.getText()) == false) {
					lab3.setText("이메일 형식이 올바르지 않습니다.");
				}else {
					
					for (int i = 0; i < tbl.length; i++) {
						Query("select * from " + tbl[i] + " where email = ?;", list, txt1.getText());
						if (!list.isEmpty()) {
							lab3.setText("이 이메일의 계정이 이미 있습니다.");
							return;
						}
					}
					
					index++;
					data.add(txt1.getText());
					next();
					
				}
				
			}else if (index == 2) {
				
				if (Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$]).{8,}$", txt1.getText()) == false) {
					lab3.setText("비밀번호 형식이 올바르지 않습니다.");
				}else {
					
					index++;
					data.add(txt1.getText());
					next();
					
				}
				
			}else if (index == 3) {
				
				index++;
				data.add(txt1.getText());
				next();
				
				btn1.setEnabled(false);
				
			}else if (index == 4) {
				
				index++;
				data.add(txt1.getText());
				
				p2.removeAll();
				btn1.setVisible(false);
				
				p2.add(btn2);
				p2.add(btn3);
				p2.add(btn4);
				
				p2.revalidate();
				p2.repaint();
				
			}else {
				
				if (!isnum(txt1.getText())) {
					lab3.setText("배달 수수료 형식이 올바르지 않습니다.");
				}else {
					
					data.add(txt1.getText());
					
					JOptionPane.showMessageDialog(null, com, "카테고리 선택하기", JOptionPane.INFORMATION_MESSAGE);
					
					jop("기능배달의 판매자가 되신 것을 환영합니다.");
					
					Update("insert into seller values(null, ?,?,?,?,?,?,?,?);", data.get(1), data.get(0), data.get(2), data.get(3), "", (com.getSelectedIndex() + 1) + "", data.get(5), data.get(4));
					Update("update map set type = ? where no = ?;", "2", data.get(4));
					
					dispose();
					new Login();
					
				}
				
			}
			
		});
		
		btn2.addActionListener(e->{
			
			jop("기능배달의 회원이 되신 것을 환영합니다.");
			
			Update("insert into user values(null, ?,?,?,?, ?);", data.get(1), data.get(0), data.get(2), data.get(3), data.get(4));
			Update("update map set type = ? where no = ?;", "3", data.get(4));
			
			dispose();
			new Login();
			
		});
		
		btn3.addActionListener(e->{
			next();
			btn1.setVisible(true);
		});
		
		btn4.addActionListener(e->{
			
			jop("기능배달의 라이더가 되신 것을 환영합니다.");
			
			Update("insert into rider values(null, ?,?,?,?, ?);", data.get(1), data.get(0), data.get(2), data.get(3), data.get(4));
			Update("update map set type = ? where no = ?;", "4", data.get(4));
			
			dispose();
			new Login();
			
		});
		
	}

}
