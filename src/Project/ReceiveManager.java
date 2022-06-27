package Project;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class ReceiveManager extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new GridLayout(2, 1, 0, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList("주문번호, 주문일시, 주문금액, 주문상태".split(", ")));
	DefaultTableModel model1 = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl1 = new JTable(model1);
	JScrollPane scl1 = new JScrollPane(tbl1);
	
	Vector v3;
	Vector v4 = new Vector<>(Arrays.asList("번호, 메뉴명, 수량, 옵션".split(", ")));
	DefaultTableModel model2 = new DefaultTableModel(v3, v4) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl2 = new JTable(model2);
	JScrollPane scl2 = new JScrollPane(tbl2);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public ReceiveManager() {
		
		SetDial(this, "주문 관리", DISPOSE_ON_CLOSE, 700, 700);
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
		
		p1.add(scl1);
		p1.add(scl2);
		
		table1();
		
	}
	
	public void table1() {
		
		SetData("select no, RECEIPT_TIME, format(price, 0), if(status = 0, '결제완료', if(status = 1, '조리완료', if(status = 2, '배달중', '배달완료'))) from receipt where seller = ? order by RECEIPT_TIME;", list, model1, 0, 4, member.get(0).get(0));
		
		tbl1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				this.setHorizontalAlignment(JLabel.CENTER);
				
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});
		
	}
	
	@Override
	public void action() {
		
		tbl1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				SetData("select rd.no, m.name, rd.COUNT, group_concat(o.NAME) from menu m, receipt_detail rd left join receipt_options op on op.RECEIPT_DETAIL = rd.no left join options o on o.NO = op.OPTIONS where rd.MENU = m.no and rd.RECEIPT = ? group by rd.no", temp, model2, 0, 4, list.get(tbl1.getSelectedRow()).get(0));
				
				if (e.getClickCount() == 2) {
					
					if (list.get(tbl1.getSelectedRow()).get(3).contentEquals("결제완료")) {
						
						int a = JOptionPane.showConfirmDialog(null, "조리를 완료하셨습니까?", "메시지", JOptionPane.YES_NO_OPTION);
						
						if (a == 0) {
							Update("update receipt set status = 1 where no = ?;", list.get(tbl1.getSelectedRow()).get(0));
						}
						
						table1();
						
					}
					
				}
				
			}
		});
		
	}

}
