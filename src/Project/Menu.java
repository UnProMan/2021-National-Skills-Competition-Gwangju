package Project;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class Menu extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(5, 5)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new BorderLayout(10, 0)));
	JPanel p3 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	
	JButton btn1 = get(new JButton("검색"));
	JButton btn2 = get(new JButton("추가"));
	JButton btn3 = get(new JButton("수정"));
	
	JTextField txt1 = gettext("검색할 이름명을 입력해주세요.");
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList("번호, 이름, 설명, 가격, 조리시간".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public Menu() {
		
		SetDial(this, "메뉴 관리", DISPOSE_ON_CLOSE, 700, 400);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Seller();
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1);
		
		p1.add(p2, "North");
		p1.add(scl);
		p1.add(p3, "South");
		
		p2.add(txt1);
		p2.add(btn1, "East");
		
		p3.add(btn2);
		p3.add(btn3);
		
		table();
		
	}
	
	public void table() {
		
		SetData("select * from menu where seller = ? and name like ?", list, model, 0, 5, member.get(0).get(0), "%" + txt1.getText() + "%");
		
		tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				this.setHorizontalAlignment(JLabel.CENTER);
				
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			table();
		});
		
		btn2.addActionListener(e->{
			
			Query("select no from menu order by no desc limit 1;", temp);
			int count = intnum(temp.get(0).get(0)) + 1;
			
			dispose();
			new MenuInsert(count + "");
			
		});
		
		btn3.addActionListener(e->{
			
			if (tbl.getSelectedRow() == - 1) {
				err("수정할 메뉴를 선택해야 합니다.");
			}else {
				dispose();
				new MenuUPdate(list.get(tbl.getSelectedRow()).get(0));
			}
		});
		
	}

}
