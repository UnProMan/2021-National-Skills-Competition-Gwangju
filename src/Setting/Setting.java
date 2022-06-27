package Setting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Setting {

	public Setting() {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			Statement s= c.createStatement();
			
			s.executeUpdate("drop database if exists eats");
			s.executeUpdate("create database if not exists eats");
			s.executeUpdate("use eats");
			s.executeUpdate("create table Category(NO INT primary KEY auto_increment, NAME VARCHAR(20))");
			s.executeUpdate("CREATE TABLE Map(NO INT primary KEY auto_increment, X INT, Y INT, TYPE INT)");
			s.executeUpdate("CREATE TABLE User(NO INT primary KEY auto_increment, EMAIL VARCHAR(50), PHONE VARCHAR(13), PW VARCHAR(20), NAME VARCHAR(40), MAP INT, foreign key(MAP) references Map(NO))");
			s.executeUpdate("CREATE TABLE Seller(NO INT primary KEY auto_increment, EMAIL VARCHAR(50), PHONE VARCHAR(13), PW VARCHAR(20), NAME VARCHAR(40), ABOUT TEXT, CATEGORY INT, DELIVERYFEE INT, MAP INT, foreign key(CATEGORY) references Category(NO), foreign key(MAP) references Map(NO))");
			s.executeUpdate("CREATE TABLE Rider(NO INT primary KEY auto_increment, EMAIL VARCHAR(20), PHONE VARCHAR(13), PW VARCHAR(20), NAME VARCHAR(40), MAP INT, foreign key(MAP) references Map(NO))");
			s.executeUpdate("CREATE TABLE Payment(NO INT primary KEY auto_increment, ISSUER VARCHAR(40), CARD VARCHAR(16), CVV VARCHAR(40), PW VARCHAR(6), USER INT, foreign key(USER) references User(NO))");
			s.executeUpdate("CREATE TABLE Type(NO INT primary KEY auto_increment, NAME VARCHAR(40), SELLER INT, foreign key(SELLER) references Seller(NO))");
			s.executeUpdate("CREATE TABLE Menu(NO INT primary KEY auto_increment, NAME VARCHAR(100), DESCRIPTION TEXT, PRICE INT, COOKTIME TIME, SELLER INT, TYPE INT, foreign key(SELLER) references Seller(NO), foreign key(TYPE) references Type(NO))");
			s.executeUpdate("CREATE TABLE Options(NO INT primary KEY auto_increment, TITLE VARCHAR(50), NAME VARCHAR(50), PRICE INT, MENU INT, foreign key(MENU) references Menu(NO))");
			s.executeUpdate("CREATE table Favorite(NO INT primary KEY auto_increment, SELLER INT, USER INT, foreign key(SELLER) references Seller(NO), foreign key(USER) references User(NO))");
			s.executeUpdate("CREATE TABLE Receipt(NO INT primary KEY auto_increment, PRICE INT, RECEIPT_TIME DATETIME, SELLER INT, USER INT, PAYMENT INT, STATUS INT, foreign key(SELLER) references Seller(NO), foreign key(USER) references User(NO), foreign key(PAYMENT) references Payment(NO))");
			s.executeUpdate("CREATE TABLE Receipt_Detail(NO INT primary KEY auto_increment, MENU INT, COUNT INT, PRICE INT, RECEIPT INT, foreign key(MENU) references Menu(NO), foreign key(RECEIPT) references Receipt(NO))");
			s.executeUpdate("CREATE TABLE Receipt_Options(NO INT primary KEY auto_increment, OPTIONS INT, PRICE INT, RECEIPT_DETAIL INT, foreign key(OPTIONS) references Options(NO), foreign key(RECEIPT_DETAIL) references Receipt_Detail(NO))");
			s.executeUpdate("CREATE TABLE Delivery(NO INT primary KEY auto_increment, RIDER INT, RECEIPT INT, START_TIME DATETIME ,foreign key(RIDER) references Rider(NO), foreign key(RECEIPT) references Receipt(NO))");
			s.executeUpdate("CREATE TABLE Review(NO INT primary KEY auto_increment, TITLE VARCHAR(30), CONTENT TEXT, RATE INT, USER INT, SELLER INT, RECEIPT INT, foreign key(USER) references User(NO), foreign key(SELLER) references Seller(NO), foreign key(RECEIPT) references Receipt(NO))");
			s.executeUpdate("drop user if exists user@'localhost'");
			s.executeUpdate("create user if not exists user@'localhsot' identified by '1234'");
			s.executeUpdate("grant select, insert, delete, update on eats.* to user@'localhsot'");
			s.executeUpdate("set global local_infile = 1");
			
			String st[] = "category, map, user, seller, rider, payment, type, menu, options, favorite, receipt, receipt_detail, receipt_options, delivery, review".split(", ");
			
			for (int i = 0; i < st.length; i++) {
				s.executeUpdate("load data local infile '지급자료/" + st[i] + ".txt' into table " + st[i] + " lines terminated by '\r\n' ignore 1 lines");
			}
			
			JOptionPane.showMessageDialog(null, "셋팅 완료");
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	public static void main(String[] args) {
		new Setting();
	}

}
