package Project;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class Delivery extends JDialog implements Base{
	
	JLabel lab1= get(new JLabel("내 배달"), set(20));
	
	JPanel p1 = get(new JPanel(new FlowLayout(0, 5, 5)), set(new EmptyBorder(10, 0, 0, 0)));
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList("주문번호, 주문시각, 음식점위치, 배달위치".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	JButton btn1= get(new JButton("새로운 배달 받기"));
	JButton btn2 = get(new JButton("배달 출발"));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public Delivery() {
		
		SetDial(this, "딜리버리", DISPOSE_ON_CLOSE, 700, 500);
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
		
		add(lab1, "North");
		add(scl);
		add(p1, "South");
		
		p1.add(btn1);
		p1.add(btn2);
		
		table();
		
	}
	
	void table() {
		
		SetData("select r.no, r.RECEIPT_TIME, (select concat(x, ',', y) from map where no = s.map), (select concat(x, ',', y) from map where no = u.map) from delivery d, receipt r, seller s, user u where r.NO = d.RECEIPT and r.STATUS = 2 and RIDER = ? and r.seller = s.no and r.user = u.no;", list, model, 0, 4, member.get(0).get(0));
		
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
			
			Query("select * from receipt where STATUS = 1;", temp);
			
			if (temp.isEmpty()) {
				err("배정 가능한 주문건이 없습니다.");
			}else {
				
				jop("신규 주문건이 배정되었습니다!");
				
				int r = ThreadLocalRandom.current().nextInt(0, temp.size());
				
				Update("update receipt set STATUS = 2 where no = ?;", temp.get(r).get(0));
				Update("insert into delivery values(null, ?,?,?);", member.get(0).get(0), temp.get(r).get(0), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				
				table();
				
			}
			
		});
		
		btn2.addActionListener(e->{
			
			int row = tbl.getSelectedRow();
			
			if (row == - 1) {
				err("배달할 목록을 선택해주세요.");
			}else {
				
				dispose();
				new Map(1, list.get(row));
				
			}
			
		});
		
	}

}
