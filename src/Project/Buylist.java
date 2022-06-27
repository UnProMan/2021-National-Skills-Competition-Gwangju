package Project;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class Buylist extends JDialog implements Base{
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList("주문번호, 주문일시, 주문금액, 음식점, 결제수단, 주문상태".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item1 = new JMenuItem("주문 취소");
	
	public Buylist() {
		
		SetDial(this, "주문내역", DISPOSE_ON_CLOSE, 700, 500);
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
		
		pop.add(item1);
		
		add(scl);
		
		table();
		
	}
	
	public void table() {
		
		SetData("select r.no, RECEIPT_TIME, format(price,0), name, concat(issuer, ' - ', right(card, 4)), if(status = 0, '결제완료', if(status = 1, '조리완료', if(status = 2, '배달중', '배달완료'))) from receipt r, seller s, payment p where r.SELLER = s.NO and p.no = r.PAYMENT and r.user = ? order by RECEIPT_TIME;", list, model, 0, 6, member.get(0).get(0));
		
		tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				this.setHorizontalAlignment(JLabel.CENTER);
				
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});
		
	}
	
	@Override
	public void action() {
		
		tbl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			
				tbl.setRowSelectionInterval(tbl.rowAtPoint(e.getPoint()), tbl.rowAtPoint(e.getPoint()));
				
				if (e.getButton() == 3) {
					pop.show(tbl, e.getX(), e.getY());
				}

			}
		});
		
		item1.addActionListener(e->{
			
			if (!list.get(tbl.getSelectedRow()).get(5).contentEquals("결제완료")) {
				err("결제 이후의 상태에서는 취소할 수 없습니다.");
			}else {
				
				jop("주문이 취소되었습니다.");
				
				Query("select * from receipt_detail where RECEIPT = ?", temp, list.get(tbl.getSelectedRow()).get(0));
				
				for (int i = 0; i < temp.size(); i++) {
					Update("delete from receipt_options where RECEIPT_DETAIL = ?;", temp.get(i).get(0));
				}
				
				Update("delete from receipt_detail where RECEIPT = ?;", list.get(tbl.getSelectedRow()).get(0));
				Update("delete from receipt where no = ?;", list.get(tbl.getSelectedRow()).get(0));
				
				table();
				
			}
			
		});
		
	}

}
