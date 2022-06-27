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
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class Reivew extends JDialog implements Base{
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList("주문번호, 주문일시, 주문금액, 음식점, 리뷰".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	JPopupMenu pop =new JPopupMenu();
	JMenuItem item1 = new JMenuItem("");
	
	public Reivew() {
		
		SetDial(this, "리뷰", DISPOSE_ON_CLOSE, 600, 400);
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
		
		SetData("select r.no, RECEIPT_TIME, format(price, 0), s.NAME, rv.title, s.no from receipt r left join review rv on r.NO = rv.RECEIPT, seller s where s.NO = r.SELLER and r.user = ? and r.STATUS = 3;", list, model, 0, 5, member.get(0).get(0));
		
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
				
				if (list.get(tbl.getSelectedRow()).get(4) == null) {
					item1.setText("리뷰 작성");
				}else {
					item1.setText("리뷰 수정");
				}
				
				if (e.getButton() == 3) {
					pop.show(tbl, e.getX(), e.getY());
				}
				
			}
		});
		
		item1.addActionListener(e->{
			
			dispose();
			if (item1.getText().contentEquals("리뷰 작성")) {
				new ReviewInsert(list.get(tbl.getSelectedRow()));
			}else {
				new RevieweUpdat(list.get(tbl.getSelectedRow()));
			}
			
		});
		
	}

}
