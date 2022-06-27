package Project;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JLabel;

import Base.Base;

public class Map extends JDialog implements Base{
	
	JLabel lab[][] = new JLabel[15][15];
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	int load[][] = new int[15][15];
	
	int sx = 0, sy= 0;
	int fx = 0, fy= 0;
	
	int x;
	int y;
	
	int type;
	ArrayList<String> data;
	
	public Map(int i, ArrayList<String> data) {
		
		this.type = i;
		this.data = data;
		
		SetDial(this, "지도", DISPOSE_ON_CLOSE, 650, 650);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				
				if (i != 0) {
					new Delivery();
				}
				
			}
		});
		
	}

	@Override
	public void design() {
		
		setLayout(new GridLayout(15, 15));
		
		for (int i = 0; i < lab.length; i++) {
			for (int j = 0; j < lab[i].length; j++) {
				add(lab[i][j] = get(new JLabel(""), setb(Color.orange)));//15 15 2중 배열 라벨을 넣기
				lab[i][j].setOpaque(true);
				load[i][j] = 0;//2중 배열 load 길은 0
			}
		}
		
		Query("select * from map where type = 0;", list);
		
		for (int i = 0; i < list.size(); i++) {
			int x = intnum(list.get(i).get(2)) - 1;
			int y = intnum(list.get(i).get(1)) - 1;
			lab[x][y].setBackground(Color.white);
			load[x][y] = 1000;// 맵 테이블에 있는 경우 1000
		}
		
		Query("select u.no, u.name, m.* from map m, user u where m.no = u.MAP;", list);
		
		for (int i = 0; i < list.size(); i++) {
			int x = intnum(list.get(i).get(4)) - 1;
			int y = intnum(list.get(i).get(3)) - 1;
			lab[x][y].setBackground(Color.cyan);
			load[x][y] = 1000;// 맵 테이블에 있는 경우 1000
		}
		
		Query("select s.no, s.name, m.* from map m, seller s where s.map = m.no;", list);
		
		for (int i = 0; i < list.size(); i++) {
			int x = intnum(list.get(i).get(4)) - 1;
			int y = intnum(list.get(i).get(3)) - 1;
			lab[x][y].setBackground(Color.pink);
			lab[x][y].setText(list.get(i).get(1));
			lab[x][y].setToolTipText(list.get(i).get(1));
			load[x][y] = 1000;// 맵 테이블에 있는 경우 1000
		}
		
		Query("select r.no, r.name, m.* from map m, rider r where m.no = r.map;", list);
		
		for (int i = 0; i < list.size(); i++) {
			int x = intnum(list.get(i).get(4)) - 1;
			int y = intnum(list.get(i).get(3)) - 1;
			lab[x][y].setBackground(Color.green);
			load[x][y] = 1000;// 맵 테이블에 있는 경우 1000
		}
		
		load();
		
	}

	public void load() {
		
		if (data != null) {
			
			new Thread(()->{
				
				try {
					
					Query("select * from map where no = ?", list, member.get(0).get(5));//라이더 위치 찾기
					
					String del[] = {list.get(0).get(1) + "," + list.get(0).get(2), data.get(2), data.get(3)};
					
					for (int i = 0; i < del.length - 1; i++) {
						//처음 출발 라이더 -> 도착 셀러위치
						//2번째 출발 셀러위치 -> 유저위치
						String start[] = del[i].split(",");
						String fin[] = del[i + 1].split(",");
						
						sx = intnum(start[1]) -1;//츌발지 xy
						sy = intnum(start[0]) - 1;
						
						x = sx;
						y = sy;
						
						fx = intnum(fin[1]) -1;// 도착지 xy
						fy = intnum(fin[0]) - 1;
						
						lab[sx][sy].setBackground(new Color(255, 0, 153));//출발지는 진한 보라색
						lab[fx][fy].setBackground(Color.red);//도착지는 빨간색
						
						reset();
						nevi(i);
						
						Thread.sleep(4000);
						
					}
					
				} catch (Exception e) {
				}
				
			}).start();
			
		}
		
	}
	
	public void nevi(int i) {
		
		new Thread(()->{
			
			try {
				
				while (true) {
					
					Thread.sleep(100);
					
					int q = load[sx == 0 ? sx : sx - 1][sy] == 1000 ? 1000 : load[sx == 0 ? sx : sx - 1][sy];//1000은 맵에 있는 공간이기에 1000이 아닐때 load값을 받아옴
					int w = load[sx == 14 ? sx : sx + 1][sy] == 1000 ? 1000 : load[sx == 14 ? sx : sx + 1][sy];
					int e = load[sx][sy == 0 ? sy : sy - 1] == 1000 ? 1000 : load[sx][sy == 0 ? sy : sy - 1];
					int r = load[sx][sy == 14 ? sy : sy + 1] == 1000 ? 1000 : load[sx][sy == 14 ? sy : sy + 1];
					//위 아래 왼쪽 오른쪽으로 검사하고 값을 받음
					int min = Math.min(Math.min(q, w), Math.min(e, r));//받은 값중 가장 작은 값을 찾음
					
					if (load[sx == 0 ? sx : sx - 1][sy] == min) sx = sx == 0 ? sx : sx - 1;//찾은 가장 작은 값을 검사해서 가장 작은값과 같은 걸 찾아서 위 아래 왼쪽 오른쪽중 작은 값으로 감
					if (load[sx == 14 ? sx : sx + 1][sy] == min) sx = sx == 14 ? sx : sx + 1;
					if (load[sx][sy == 0 ? sy : sy - 1] == min) sy = sy == 0 ? sy : sy - 1;
					if (load[sx][sy == 14 ? sy : sy + 1] == min) sy = sy == 14 ? sy : sy + 1;
					
					lab[sx][sy].setBackground(Color.gray);
						
					if (min == 1) {//load에서 나올 수 있는 가장 작은 값 = 1
						
						if (i == 0) {
							jop("음식점에 도착했습니다!");
							lab[x][y].setBackground(Color.green);
							break;
						}else {
							jop("배달을 완료했습니다!");
							Update("update receipt set status = 3 where no = ?", data.get(0));
							break;
						}
						
					}
					
				}
				
			} catch (Exception e) {
			}
			
		}).start();
		
	}
	
	public void reset() {
		
		for (int i = 0; i < lab.length; i++) {
			for (int j = 0; j < lab[i].length; j++) {
				
				if (lab[i][j].getBackground().equals(Color.orange) || lab[i][j].getBackground().equals(Color.gray)) {
					load[i][j] = 0;
				}
			}
		}
		
		con(fx, fy, 1);//가장 작은 값인 1은 도착값에 위 아래 오른쪽 왼쪽에 넣을 수 있는곳에 넣은다
		
		int count = 1;
		
		while (true) {
			
			for (int i = 0; i < lab.length; i++) {
				for (int j = 0; j < lab[i].length; j++) {
					if (load[i][j] == count) {
						con(i, j, count + 1);//시작값부터 시작해서 차례차례 값을 늘려서 넣어준다
					}
				}
			}
			
			count++;
			
			if (count == 40) {
				break;
			}
			
		}
		
	}
	
	public void con(int x, int y, int count) {
		
		if (load[x == 0 ? x : x - 1][y] == 0 ) load[x == 0 ? x : x - 1][y] = count;
		if (load[x == 14 ? x : x + 1][y] == 0 ) load[x == 14 ? x : x + 1][y] = count;
		if (load[x][y == 0 ? y : y - 1] == 0 ) load[x][y == 0 ? y : y - 1] = count;
		if (load[x][y == 14 ? y : y + 1] == 0 ) load[x][y == 14 ? y : y + 1] = count;
		
		//오른쪽 왼쪽 위 아래 중에서 load가 0인것에 값을 넣어준다
		
	}
	
	@Override
	public void action() {
		
		for (int i = 0; i < lab.length; i++) {
			for (int j = 0; j < lab[i].length; j++) {
				
				int x = i;
				int y = j;
				
				lab[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						
						if (type == 0 && e.getClickCount() == 2) {
							
							if (lab[x][y].getBackground().equals(Color.orange)) {
								err("길은 선택할 수 없습니다.");
							}else if (!lab[x][y].getBackground().equals(Color.white)) {
								err("이미 사용중인 위치입니다.");
							}else {
								
								Query("select * from map where x = ? and y = ?;", list, (y + 1) + "", (x + 1) + "" );
								
								MemberInsert.txt1.setText(list.get(0).get(0));
								MemberInsert.btn1.setEnabled(true);
								
								dispose();
								
							}
							
						}
						
					}
				});
				
			}
		}
		
	}

}
