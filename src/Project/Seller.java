package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Seller extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(5, 5, 5, 5)), set(250, 0));
	JPanel p2 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p3 = get(new JPanel(new GridLayout(4, 1, 0, 5)));
	JPanel p4 = get(new JPanel(new GridLayout(7, 1, 0, 10)), set(0, 305), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p5 = get(new JPanel(new BorderLayout(0, 10)));
	JPanel p6 = get(new JPanel(new GridLayout(2, 1, 10, 10)));
	JPanel p7 = get(new JPanel(), set(0, 360));
	
	JTextField txt1= gettext("", set(false));
	JTextField txt2 = gettext("", set(false));
	JTextField txt3 = gettext("비밀번호");
	JTextField txt4 = gettext("상호명");
	JTextField txt5 = gettext("배달수수료");
	JTextArea txt6 = get(new JTextArea(), set(new LineBorder(Color.LIGHT_GRAY)));
	
	JComboBox com1 = new JComboBox<>();
	
	JButton btn1 = get(new JButton("메뉴관리"));
	JButton btn2 = get(new JButton("주문관리"));
	JButton btn3 = get(new JButton("통계"));
	JButton btn4 = get(new JButton("저장"));
	JButton btn5 = get(new JButton("배경 사진 등록"));
	
	JLabel img;
	JLabel lab1 = get(new JLabel("", 0), set(12));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	File file = new File("");
	
	public Seller() {
		
		SetDial(this, "판매자 대시보드", DISPOSE_ON_CLOSE, 900, 700);
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
			com1.addItem(list.get(1));
		}
		
		file = new File(file("프로필/" + member.get(0).get(0) + ".png"));
		if (file.exists()) {
			img = getimg("프로필/" + member.get(0).get(0) + ".png", 160, 140, set(160, 140));
		}else {
			img = getimg("프로필/upload.png", 160, 140, set(160, 140));
		}
		
		add(p1, "West");
		add(p2);
		
		p1.add(img, "North");
		p1.add(p3);
		p1.add(p7, "South");
		
		p3.add(lab1);
		p3.add(btn1);
		p3.add(btn2);
		p3.add(btn3);
		
		p2.add(p4, "North");
		p2.add(p5);
		p2.add(p6, "South");
		
		p4.add(txt1);
		p4.add(txt2);
		p4.add(txt3);
		p4.add(txt4);
		p4.add(com1);
		p4.add(txt5);
		p4.add(new JLabel("가게 설명"));
		
		p5.add(txt6);
		txt6.setLineWrap(true);
		
		p6.add(btn4);
		p6.add(btn5);
		
		lab1.setText(member.get(0).get(4));
		
		txt1.setText(member.get(0).get(1));
		txt2.setText(member.get(0).get(2));
		txt3.setText(member.get(0).get(3));
		txt4.setText(member.get(0).get(4));
		txt5.setText(member.get(0).get(7));
		txt6.setText(member.get(0).get(5));
		
		com1.setSelectedIndex(intnum(member.get(0).get(6)) - 1);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			dispose();
			new Menu();
		});
		
		btn2.addActionListener(e->{
			dispose();
			new ReceiveManager();
		});
		
		btn3.addActionListener(e->{
			dispose();
			new State();
		});
		
		btn4.addActionListener(e->{
			
			if (txt3.getText().isBlank() || txt4.getText().isBlank() || txt5.getText().isBlank()) {
				err("기본 정보는 모두 입력해야 합니다.");
			}else if (!isnum(txt5.getText())) {
				err("배달 수수료는 숫자로 입력해야 합니다.");
			}else {
				
				jop("정보가 수정되었습니다.");
				
				Update("update seller set pw = ?, name = ?, about = ?, category = ?, DELIVERYFEE = ? where no = ?;", txt3.getText(), txt4.getText(), txt6.getText(), (com1.getSelectedIndex() + 1) + "", txt5.getText(), member.get(0).get(0));
				Query("select * from seller where no = ?", member, member.get(0).get(0));
				
				lab1.setText(txt4.getText());
				
			}
			
		});
		
		btn5.addActionListener(e->{
			
			FileDialog f = new FileDialog(Seller.this, "", FileDialog.LOAD);
			f.setFile("*.png");
			
			f.setVisible(true);
			
			file = new File(f.getDirectory() + f.getFile());
			
			try {
				ImageIO.write(ImageIO.read(file), "png", new File(file("배경/" + member.get(0).get(0) + ".png")));
			} catch (Exception e2) {
			}
			
		});
		
		
		img.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				FileDialog f = new FileDialog(Seller.this, "", FileDialog.LOAD);
				f.setFile("*.png");
				
				f.setVisible(true);
				
				file = new File(f.getDirectory() + f.getFile());
				img.setIcon(new ImageIcon(new ImageIcon(file.toString()).getImage().getScaledInstance(160, 140, 4)));
				
				try {
					ImageIO.write(ImageIO.read(file), "png", new File(file("프로필/" + member.get(0).get(0) + ".png")));
				} catch (Exception e2) {
				}
				
				repaint();
				
			}
		});
	}

}
