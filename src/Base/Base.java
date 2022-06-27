package Base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public interface Base {

	ArrayList<ArrayList<String>> member = new ArrayList<>();
	ArrayList<ArrayList<String>> receipt = new ArrayList<>();
	ArrayList<ArrayList<String>> reoptions = new ArrayList<>();
	ArrayList<String> filter = new ArrayList<>();
	
	DecimalFormat df = new DecimalFormat("#,##0");
	
	Color green = new Color(0, 102, 0);
	
	public void design();
	public void action();
	
	default void SetDial(JDialog d, String title, int ex, int x, int y) {
		d.setTitle(title);
		d.setDefaultCloseOperation(ex);
		d.setSize(x, y);
		d.setIconImage(new ImageIcon(file("logo.png")).getImage());
		d.getContentPane().setBackground(Color.white);
		d.setLocationRelativeTo(null);
	}
	
	default <Any> Any get(JComponent comp, Set...sets) {
		
		comp.setBackground(Color.white);
		comp.setFont(new Font("맑은 고딕", 0, comp.getFont().getSize()));
		
		if (comp instanceof JButton) {
			comp.setForeground(Color.white);
			comp.setBackground(green);
		}
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return (Any) comp;
		
	}
	
	default JTextField gettext(String title, Set...sets) {
		
		JTextField txt = new JTextField() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				if (!this.getText().isBlank()) {
					return;
				}
				
				g.setColor(Color.gray);
				g.drawString(title, this.insets().left, this.insets().top + g.getFontMetrics().getMaxAscent() + 5);
				
			}
		};
		
		txt.setFont(new Font("맑은 고딕", 0, txt.getFont().getSize()));
		
		for (Set set : sets) {
			set.set(txt);
		}
		
		return txt;
		
	}
	
	default boolean isnum(String txt) {
		
		try {
			
			Integer.parseInt(txt);
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	default boolean istime(String txt) {
		
		try {
			
			LocalTime.parse(txt);
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	default boolean isnumbers(String txt, int len) {
		
		try {
			
			Integer.parseInt(txt);
			
			if (txt.length() == len) {
				return true;
			}else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	default void jop(String txt) {
		JOptionPane.showMessageDialog(null, txt, "메시지",JOptionPane.INFORMATION_MESSAGE);
	}

	default void err(String txt) {
		JOptionPane.showMessageDialog(null, txt, "메시지", JOptionPane.ERROR_MESSAGE);
	}
	
	default Set set(boolean tf) {
		return c->c.setEnabled(tf);
	}
	
	default Set set(Border border) {
		return c->c.setBorder(border);
	}
	
	default Set set(int x, int y) {
		return c->c.setPreferredSize(new Dimension(x, y));
	}
	
	default Set setf(Color color) {
		return c->c.setForeground(color);
	}
	
	default Set setb(Color color) {
		return c->c.setBackground(color);
	}
	
	default Set set(int font) {
		return c->c.setFont(new Font("맑은 고딕", 1, font));
	}
	
	default void allfont(JComponent comp) {
		for (Component c : comp.getComponents()) {
			c.setFont(new Font("맑은 고딕", 0, c.getFont().getSize()));
		}
		comp.revalidate();
		comp.repaint();
	}
	
	default String file(String txt) {
		return "지급자료/" + txt;
	}
	
	default JLabel getimg(String file, int x, int y, Set...sets ) {
		
		JLabel comp = new JLabel(new ImageIcon(new ImageIcon(file(file)).getImage().getScaledInstance(x, y, 4)));
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return comp;
		
	}
	
	default Integer intnum(String txt) {
		return Integer.parseInt(txt);
	}
	
	default void Query(String sql, ArrayList<ArrayList<String>> list, String...v) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/eats?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			list.clear();
			ResultSet rs = s.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			
			while (rs.next()) {
				ArrayList row = new ArrayList<>();
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				list.add(row);
			}
			
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	default void Update(String sql, String...v) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/eats?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			s.executeUpdate();
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	default void SetData(String sql, ArrayList<ArrayList<String>> list, DefaultTableModel model, int start, int fin, String...v) {
		
		Query(sql, list, v);
		
		model.setNumRows(0);
		
		for (int i = 0; i < list.size(); i++) {
			Vector row = new Vector<>();
			for (int j = start; j < fin; j++) {
				row.add(list.get(i).get(j));
			}
			model.addRow(row);
		}
		
	}
	
}
