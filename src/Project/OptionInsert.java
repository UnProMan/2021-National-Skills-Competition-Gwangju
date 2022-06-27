package Project;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class OptionInsert extends JDialog implements Base{
		
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)), set(0, 140));
	
	JTextField txt1 = gettext("옵션 이름", set(360, 30));
	JTextField txt2 = gettext("옵션 가격", set(360, 30));
	JTextField txt3 = gettext("옵션 그룹 이름 입력하기", set(250, 30));
	
	JButton btn1 = get(new JButton("옵션 등록"), set(360, 30));
	JButton btn2 = get(new JButton("옵션 저장"), set(100, 30));
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList("번호, 옵션명, 가격".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	DefaultTableModel m;
	String mno;
	
	public OptionInsert(DefaultTableModel m, String mno) {
		
		this.m = m;
		this.mno = mno;
		
		SetDial(this, "옵션추가", DISPOSE_ON_CLOSE, 400, 500);
		design();
		action();
		setModal(true);
		setVisible(true);

	}

	@Override
	public void design() {
		
		add(p1);
		p1.add(scl);
		p1.add(p2, "South");
		
		p2.add(txt1);
		p2.add(txt2);
		p2.add(btn1);
		p2.add(txt3);
		p2.add(btn2);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank()) {
				err("빈 칸 없이 모두 입력해야 합니다.");
			}else if (!isnum(txt2.getText())) {
				err("옵션 가격은 숫자로만 입력해야 합니다.");
			}else {
				
				model.addRow(new Object[] {model.getRowCount() + 1, txt1.getText(), txt2.getText()});
				
			}
			
		});
		
		btn2.addActionListener(e->{
			
			if (txt3.getText().isBlank()) {
				err("옵션 그룹 이름을 입력해야 합니다.");
			}else if (model.getRowCount() == 0) {
				err("1개 이상의 옵션을 등록해야 합니다.");
			}else {
				insert();
				dispose();
			}
			
		});
		
	}

	public void insert() {
		
		String options = "", pays = "";
		for (int i = 0; i < model.getRowCount(); i++) {
			options = options.isBlank() ? model.getValueAt(i, 1).toString() : options + "," + model.getValueAt(i, 1).toString();
			pays = pays.isBlank() ? model.getValueAt(i, 2).toString() : pays + "," + model.getValueAt(i, 2).toString();
		}
		
		for (int i = 0; i < m.getRowCount(); i++) {
			
			String name = m.getValueAt(i, 1).toString();
			String option = m.getValueAt(i, 2).toString();
			String pay = m.getValueAt(i, 3).toString();
			
			if (name.contentEquals(txt3.getText())) {
				m.setValueAt(option + "," + options, i, 2);
				m.setValueAt(pay + pays, i, 3);
				return;
			}
			
		}
		
		m.addRow(new Object[] {mno, txt3.getText(), options, pays});
		
	}
	
}
