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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class MenuUPdate extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(5, 5)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new BorderLayout(5, 0)));
	JPanel p3=  get(new JPanel(new GridLayout(0, 1, 0, 5)));
	JPanel p4 = get(new JPanel(new GridLayout(2, 1, 0, 10)));
	JPanel p5 = get(new JPanel(new GridLayout(1, 4, 5, 0)));
	
	JButton btn1 = get(new JButton("옵션 추가"));
	JButton btn2 = get(new JButton("옵션 삭제"));
	JButton btn3 = get(new JButton("등록"));
	
	JTextField txt1 = gettext("메뉴명");
	JTextField txt2 = gettext("설명");
	JTextField txt3 = gettext("판매가");
	JTextField txt4 = gettext("조리시간");
	
	JComboBox com1 = new JComboBox();
	
	JLabel img;
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList("번호, 이름, 옵션명, 가격".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	ArrayList<ArrayList<String>> com = new ArrayList<>();
	
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item1 = new JMenuItem("음식 그룹 추가");
	
	File file = new File("");
	String mno;
	
	public MenuUPdate(String mno) {
		
		this.mno = mno;
		
		SetDial(this, "메뉴수정", DISPOSE_ON_CLOSE, 500, 600);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Menu();
			}
		});
		
	}

	@Override
	public void design() {
		
		com();
		Query("select * from menu where no = ?;", list, mno);
		
		pop.add(item1);
		
		add(p1);
		
		p1.add(p2, "North");
		p1.add(scl);
		p1.add(p4, "South");
		
		p2.add(img = getimg("메뉴/" + mno + ".png", 160, 140, set(new LineBorder(Color.black)), setb(Color.white), set(160, 140)), "West");
		p2.add(p3);
		img.setOpaque(true);
		
		p3.add(txt1);
		p3.add(txt2);
		p3.add(txt3);
		p3.add(txt4);
		p3.add(com1);
		
		p4.add(p5);
		p4.add(btn3);
		
		p5.add(btn1);
		p5.add(btn2);
		p5.add(new JLabel(""));
		p5.add(new JLabel(""));
		
		txt1.setText(list.get(0).get(1));
		txt2.setText(list.get(0).get(2));
		txt3.setText(list.get(0).get(3));
		txt4.setText(list.get(0).get(4));
		
		for (int i = 0; i < com.size(); i++) {
			if (com.get(i).get(0).contentEquals(list.get(0).get(6))) {
				com1.setSelectedIndex(i);
			}
		}
		
		Query("select no, title, group_concat(name), group_concat(price), group_concat(no) from options where menu = ? group by title;", temp, mno);
		
		if (temp.get(0).get(0) != null) {
			for (int i = 0; i < temp.size(); i++) {
				model.addRow(new Object[] {temp.get(i).get(0), temp.get(i).get(1), temp.get(i).get(2), temp.get(i).get(3)});
			}
		}
		
		tbl.getColumnModel().getColumn(3).setMinWidth(0);
		tbl.getColumnModel().getColumn(3).setMaxWidth(0);
		tbl.getColumnModel().getColumn(3).setPreferredWidth(0);
		
		tbl.getColumnModel().getColumn(0).setPreferredWidth(70);
		tbl.getColumnModel().getColumn(1).setPreferredWidth(70);
		tbl.getColumnModel().getColumn(2).setPreferredWidth(120);
		
	}
	
	public void com() {
		
		com1.removeAllItems();
		
		Query("select * from type where seller = ?;", com, member.get(0).get(0));
		for (ArrayList<String> list : com) {
			com1.addItem(list.get(1));
		}
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			new OptionInsert(model, mno);
		});
		
		btn2.addActionListener(e->{
			
			if (tbl.getSelectedRow() == -1) {
				err("삭제할 옵션을 선택해야 합니다.");
			}else {
				
				String st[] = tbl.getValueAt(tbl.getSelectedRow(), 4).toString().split(",");
				
				for (int i = 0; i < st.length; i++) {
					Update("delete from receipt_options where options = ?;", st[i]);
					Update("delete from options where no = ?;", st[i]);
				}
				
				((DefaultTableModel)tbl.getModel()).removeRow(tbl.getSelectedRow());
				
			}
			
		});
		
		btn3.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank() || txt3.getText().isBlank() || txt4.getText().isBlank()) {
				err("빈 칸 없이 모두 입력해야 합니다.");
			}else if (!isnum(txt3.getText())) {
				err("판매 가격은 숫자로 입력해야 합니다.");
			}else if (!istime(txt4.getText()) || Pattern.matches("\\d{2}:\\d{2}:\\d{2}", txt4.getText()) == false) {
				err("조리시간을 hh:mm:ss 형식으로 입력해야 합니다.");
			}else if (LocalTime.parse(txt4.getText()).isBefore(LocalTime.of(0, 0, 1))) {
				err("조리시간은 1초 이상이어야 합니다.");
			}else {
				
				Query("select * from menu where name = ? and seller = ? and not no = ?;", list, txt1.getText(), member.get(0).get(0), mno);
				
				if (!list.isEmpty()) {
					err("중복되는 메뉴명입니다.");
				}else {
					
					jop("메뉴를 수정했습니다.");
					
					Update("update menu set name = ?, DESCRIPTION = ?, price = ?, cooktime = ?, type = ? where no = ?;", txt1.getText(), txt2.getText(), txt3.getText(), txt4.getText(), com.get(com1.getSelectedIndex()).get(0), mno);
					
					for (int i = 0; i < model.getRowCount(); i++) {
						
						String name = tbl.getValueAt(i, 1).toString();
						String st1[] = tbl.getValueAt(i, 2).toString().split(",");
						String st2[] = tbl.getValueAt(i, 3).toString().split(",");
						
						for (int j = 0; j < st2.length; j++) {
							Query("select * from options where title = ? and name = ? and menu = ?;", list, name, st1[j], mno);
							if (list.isEmpty()) {
								Update("insert into options values(null, ?,?,?,?);", name, st1[j], st2[j], mno);
							}
						}
						
					}
					
					if (!file.toString().isBlank()) {
						try {
							ImageIO.write(ImageIO.read(file), "png", new File(file("메뉴/" + mno + ".png")));
						} catch (Exception e2) {
						}
					}
					
					dispose();
					new Menu();
					
				}
				
			}
			
		});
		
		img.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				FileDialog f = new FileDialog(MenuUPdate.this, "", FileDialog.LOAD);
				f.setFile("*.png");
				
				f.setVisible(true);
				
				file = new File(f.getDirectory() + f.getFile());
				img.setIcon(new ImageIcon(new ImageIcon(file.toString()).getImage().getScaledInstance(160, 140, 4)));
				
				repaint();
				
			}
		});
		
		com1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
					
				if (e.getButton() == 3) {
					pop.show(com1, e.getX(), e.getY());
				}
				
			}
		});
		
		item1.addActionListener(e->{
			
			String a = JOptionPane.showInputDialog(null, "새로 추가할 음식 그룹 이름을 입력하세요.", "메시지", JOptionPane.INFORMATION_MESSAGE);
			
			if (a == null || a.isBlank()) {
				err("이름을 입력해야 합니다.");
			}else {
				
				Query("select * from type where seller = ? and name = ?;", list, member.get(0).get(0), a);
				
				if (!list.isEmpty()) {
					err("중복되는 음식 그룹입니다.");
				}else {
					Update("insert into type values(null, ?,?);", a, member.get(0).get(0));
					com();
				}
				
			}
			
		});
		
	}

}
