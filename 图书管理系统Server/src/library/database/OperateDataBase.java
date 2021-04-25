package library.database;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

import library.data.Book;
import library.data.Borrow;
import library.data.ListBorrow;
import library.data.ListUser;
import library.data.MapBookTypeNum;
import library.data.User;

//操纵数据库的类
public class OperateDataBase {
	private static String DataBaseClassName = "com.mysql.cj.jdbc.Driver";
	private static String DataBaseUrl = "jdbc:mysql://localhost:3306/library?usrSSL=true&characterEncoding=utf-8";
	private static String DataBaseUser = "root";			//账号
	private static String DataBasePass = "52583344zh?";		//密码
	private static String alterSql = "alter table Book AUTO_INCREMENT = 1";
	private static Connection connection = null;	//管理指向数据库的连接
	private static Statement statement = null;		//指向数据库查询
	
	//单例模式
	private OperateDataBase(){
		try {
			if(connection == null) {
				Class.forName(DataBaseClassName);//加载数据库驱动
				connection = DriverManager.getConnection(DataBaseUrl,DataBaseUser,DataBasePass);
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//关闭数据库连接
	public static void close() {
		try {
			connection.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		connection = null;
	}

	//sql查询结果集
	private static ResultSet executeQuery(String sql) {
		try {
			if (connection == null) {
				new OperateDataBase();
			}
			ResultSet ans = statement.executeQuery(sql);
			return ans;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//sql更改数据库
	private static int executeUpdate(String sql) {
		try {
			if (connection == null) 
				new OperateDataBase();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}
	
	//登录检查
	public static boolean[] checkLogin(String user, String pass) {
		String userCheckSQL = "select * from User where username ='" + user + "' and password ='" + pass +"'";
		System.out.println(userCheckSQL);
		ResultSet resultSet = executeQuery(userCheckSQL);
		boolean ans[] = {false,false};//ans[0]表示登录是否成功,ans[1]表示登录成功后的用户类型
		try {
			if (resultSet != null) {
				//将当前行定位到数据库记录集最后一行
				resultSet.last();
				if (resultSet.getRow() >= 1) {
					ans[0] = true;
					ans[1] = resultSet.getBoolean("UserType");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	
	//注册账号(默认注册读者账号)
	public static int registerUser(String username,String password) {
		String userCheckSQL = "select * from User where username = '" + username + "'";
		System.out.println(userCheckSQL);
		ResultSet resultSet = executeQuery(userCheckSQL);
		try {
			resultSet.last();
			if(resultSet.getRow() == 1) {
				return 0;//说明用户名已经存在
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String registerSql = "insert into User (UserName,PassWord) values('" + username + "','" + password + "')";
		return executeUpdate(registerSql); 
	}
	
	//查看书籍信息
	public static List<Book> getAllBookInfo() {
		List<Book> list = new ArrayList<Book>();
		String sql = "select * from Book";
		System.out.println(sql);
		ResultSet resultSet = OperateDataBase.executeQuery(sql);
		try {
			//当resultSet有信息存在时,不断向list中添加bookinfo
			while (resultSet.next()) {
				Book bookinfo = new Book(
						resultSet.getInt("BookID"),
						resultSet.getString("BookName"),
						resultSet.getString("Author"),
						resultSet.getString("BookType"),
						resultSet.getDate("bookdate"),
						resultSet.getInt("BookNum"));
				list.add(bookinfo);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		//返回查询结果
		return list;
	}
	
	//插入书籍信息
	public static int InsertBook(String bookname,String authors,String bookType,Date date,int booknum){
		int index = 0;
		try{
			String findSql = "select * from Book where Bookname ='" + bookname + "'and Author = '" + authors + "' and BookDate ='" + date +"'";
			System.out.println(findSql);
			ResultSet ret = executeQuery(findSql);
			ret.last();
			if(ret != null && ret.getRow() >= 1) {
				return index;//说明此时书籍以及存在
			}
			//防止ID增长不连续
			statement.execute(alterSql);
			String sql = "insert into Book (Bookname,Author,BookType,BookDate,BookNum) values('"+ bookname +"','"+ authors + "','" + bookType +"','"+ date + "','" + booknum +"')";
			System.out.println(sql);
			index = OperateDataBase.executeUpdate(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return index;	
	}
	
	//删除书籍信息
	public static int DeleteBook(int id) {
		int index = 0;
		try {
			String sql = "delete from Book where BookId = '" + id + "'";
			System.out.println(sql);
			index = OperateDataBase.executeUpdate(sql);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return index;
	}
	
	//更新书籍信息
	public static int UpdateBook(int id, String bookname,String authors,String bookType, Date date,int num){
		int index = 0;
		String sql = "update Book set Bookname = '"+ bookname +"',Author = '"+ authors + "',BookType = '" + bookType +"',BookDate = '"+ date + "',BookNum = '" + num +"' where BookId = '"+ id +"'";
		System.out.println(sql);
		index = OperateDataBase.executeUpdate(sql);
		return index;
	}
	
	//精确查询
	//filed 查询字段 , info 查询信息
	public static List<Book> refindedQuery(String filed,String info)
	{
		List<Book> list = new ArrayList<Book>();
		String sql = "select * from Book where " + filed + " = '" + info + "'";
		System.out.println(sql);
		ResultSet resultSet = OperateDataBase.executeQuery(sql);
		if(resultSet == null) {
			JOptionPane.showMessageDialog(null, "null result");
			list = null;
		}
		try {
			while(resultSet.next()) {
				Book bookinfo = new Book(
						resultSet.getInt("BookID"),
						resultSet.getString("BookName"),
						resultSet.getString("Author"),
						resultSet.getString("BookType"),
						resultSet.getDate("bookdate"),
						resultSet.getInt("BookNum"));
				list.add(bookinfo);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//模糊查询
	//filed 查询字段 , info 查询信息
	public static List<Book> fuzzyQuery(String filed,String info)
	{
		List<Book> list = new ArrayList<Book>();
		String sql = "select * from Book where " + filed + " like '%" + info + "%'";
		System.out.println(sql);
		ResultSet resultSet = OperateDataBase.executeQuery(sql);
		try {
			while(resultSet.next()) {
				Book bookinfo = new Book(
						resultSet.getInt("BookID"),
						resultSet.getString("BookName"),
						resultSet.getString("Author"),
						resultSet.getString("BookType"),
						resultSet.getDate("bookdate"),
						resultSet.getInt("BookNum"));
				list.add(bookinfo);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//得到所有可能的图书类别
	public static List<String> getAllBookType()
	{
		List<String> list = new ArrayList<String>();
		String sql = "select* from BookType";
		System.out.println(sql);
		ResultSet resultSet = OperateDataBase.executeQuery(sql);
		if(resultSet == null) {
			System.out.println("null result");
		}else {
			try {
				while(resultSet.next()) {
					list.add(resultSet.getString("BookType"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	//修改图书类别(由于book表有参考booktype的外键,因此会自行连级修改)
	public static Boolean modifyBookType(String preTypeName,String newType) {
		int index = 0;
		String sql = "update BookType set BookType = '" + newType + "' where BookType = '" + preTypeName + "'";
		System.out.println(sql);
		index = OperateDataBase.executeUpdate(sql);
		return index == 1;
	}
	
	//添加图书类别
	public static Boolean addBookType(String addBookType) {
		int index = 0;
		String sql = "insert into BookType values('" + addBookType + "')";
		System.out.println(sql);
		index = OperateDataBase.executeUpdate(sql);
		return index == 1;
	}
	
	//删除图书类别
	public static Boolean deleteBookType(String typename) {
		int index = 0;
		String sql = "delete from BookType where BookType = '" + typename + "'";
		System.out.println(sql);
		index = OperateDataBase.executeUpdate(sql);
		return index == 1;
	}
	
	//得到book表中所有对应的类别和书籍种类
	public static MapBookTypeNum getAllBookTypeNum()
	{
		Map<String, Integer> map = new HashMap<String, Integer>();
		String sql = "select BookType,COUNT(*) as CountNum from Book GROUP BY BookType";
		System.out.println(sql);
		ResultSet resultSet = OperateDataBase.executeQuery(sql);
		if(resultSet == null) {
			System.out.println("null result");
		}else {
			try {
				while(resultSet.next()) {
					map.put(resultSet.getString("BookType"), resultSet.getInt("CountNum"));
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		MapBookTypeNum ans = new MapBookTypeNum(map);
		return ans;
	}
	
	//获取用户信息
	public static ListUser getAllUserInfo() {
		String sql = "select* from User";
		System.out.println(sql);
		ResultSet resultSet = OperateDataBase.executeQuery(sql);
		List<User> list = new ArrayList<User>();
		if(resultSet == null) {
			System.out.println("null result");
		}else {
			try {
				while(resultSet.next()) {
					User user = new User();
					user.setUserName(resultSet.getString("UserName"));
					user.setPassword(resultSet.getString("PassWord"));
					String userType = resultSet.getBoolean("UserType")?"管理员":"用户";
					user.setUserType(userType);
					list.add(user);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new ListUser(list);
	}
	
	//删除用户
	public static boolean deleteUser(String username) {
		String sql = "delete from User where UserName = '" + username + "'";
		System.out.println(sql);
		return 1 == OperateDataBase.executeUpdate(sql);
	}
	
	//修改用户权限
	public static boolean modifyUserPermission(String username) {
		String sql = "update User set UserType = !UserType where UserName = '" + username +"'";
		System.out.println(sql);
		return 1 == OperateDataBase.executeUpdate(sql);
	}
	
	//修改用户密码
	public static boolean modifyUserPassword(String username,String password) {
		String sql = "update User set PassWord = '" + password + "' where UserName = '" + username + "'";
		System.out.println(sql);
		return 1 == OperateDataBase.executeUpdate(sql);
	}
	
	//历史借阅查询
	public static ListBorrow getHistoryBorrowInfo(String username) {
		String sql = "select BookName,Author,BookType,BorrowDate,ReturnDate from Borrow,User,Book where Borrow.BookID = Book.BookID and Borrow.UserID = User.UserID and UserName = '" + username + "' and ReturnDate is not null";
		System.out.println(sql);
		ResultSet resultSet = OperateDataBase.executeQuery(sql);
		List<Borrow> list = new ArrayList<Borrow>();
		if(resultSet == null) {
			System.out.println("null result");
		}else {
			try {
				while(resultSet.next()) {
					Borrow borrow = new Borrow();
					borrow.setBookName(resultSet.getString("BookName"));
					borrow.setAuthor(resultSet.getString("author"));
					borrow.setBookType(resultSet.getString("BookType"));
					borrow.setBorrowDate(resultSet.getDate("BorrowDate"));
					borrow.setReturnDate(resultSet.getDate("ReturnDate"));
					list.add(borrow);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new ListBorrow(list);
	}
	
	//当前借阅查询
	public static ListBorrow getCurrentBorrowInfo(String username) {
		String sql = "select BookName,Author,BookType,BorrowDate,ReturnDate from Borrow,User,Book where Borrow.BookID = Book.BookID and Borrow.UserID = User.UserID and UserName = '" + username + "' and ReturnDate is null";
		System.out.println(sql);
		ResultSet resultSet = OperateDataBase.executeQuery(sql);
		List<Borrow> list = new ArrayList<Borrow>();
		if(resultSet == null) {
			System.out.println("null result");
		}else {
			try {
				while(resultSet.next()) {
					Borrow borrow = new Borrow();
					borrow.setBookName(resultSet.getString("BookName"));
					borrow.setAuthor(resultSet.getString("author"));
					borrow.setBookType(resultSet.getString("BookType"));
					borrow.setBorrowDate(resultSet.getDate("BorrowDate"));
					borrow.setReturnDate(resultSet.getDate("ReturnDate"));
					list.add(borrow);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new ListBorrow(list);
	}
	
	//图书借阅
	public static boolean borrowBook(String username,String bookname,String booktype,String author) {
		String sql = "insert into Borrow (UserID,BookID) values((select UserID from User where username = '" + username + "'),(select BookID from Book where Bookname ='" + bookname + "' and author = '" + author + "' and booktype = '" + booktype + "'))";
		System.out.println(sql);
		return 1 == OperateDataBase.executeUpdate(sql);
	}
	
	//归还图书
	public static boolean returnBook(String username,String bookname,String booktype,String author) {
		String sql = "UPDATE Borrow set returnDate = CURRENT_TIMESTAMP WHERE BorrowID in (select* from (select b.borrowID from borrow b,book,user where b.userID = user.userID and book.bookid = b.bookid and username ='" + username + "' and author = '" + author + "' and booktype ='" + booktype + "')b)";
		System.out.println(sql);
		return 1 == OperateDataBase.executeUpdate(sql);
	}
	
	//单例模式
	public static Connection getInstance() {
		if(connection == null) {
			new OperateDataBase();
		}
		return connection;
	}
}
