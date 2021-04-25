package library.frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.JInternalFrame;

public class MenuActions {
	private static HashMap<String,JInternalFrame> frames 
								= new HashMap<String, JInternalFrame>();// 子窗体集合
	private static BookModiDelAction 	BOOK_MODIFYDELETE; 	 //图书信息修改窗体动作
	private static BookAddAction  	 	BOOK_ADD; 			 //图书信息添加和删除窗体动作
	private static BookQueryAction 	 	BOOK_QUERY; 		 //图书信息查询
	private static BookTypeAction	 	BOOK_TYPE;			 //图书类型修改
	private static BookBorrowAction		BOOK_BORROW;		 //图书借阅管理
	private static AboutSoftAction	 	About_Soft;			 //关于软件
	private static UserManageAction		User_Manage;		 //用户信息管理
	private static UserPasswordAction   User_Password;		 //修改用户密码
	
	private MenuActions() {
		super();
	}
	
	private static class BookAddAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//如果添加图书信息的窗口已经被关闭则重新new一个
			if (!frames.containsKey("图书信息添加") || frames.get("图书信息添加").isClosed()) {
				try {
					BookAddFrame frame = new BookAddFrame();
					frames.put("图书信息添加", frame);
					Library.addFrame(frame);
					//显示JInteralFrame窗口
					frame.setSelected(true);
				} catch (MalformedURLException |UnknownHostException | PropertyVetoException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static ActionListener getBookAddAction(){
		if(BOOK_ADD == null)
			BOOK_ADD = new BookAddAction();
		return BOOK_ADD;
		
	}
	
	//图书修改与删除
	private static class BookModiDelAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//如果修改图书信息的窗口已经被关闭则重新new一个
			if (!frames.containsKey("图书信息修改") || frames.get("图书信息修改").isClosed()) {
				try {
					BookModiAndDelFrame frame = new BookModiAndDelFrame();
					frames.put("图书信息修改", frame);
					Library.addFrame(frame);
					//显示JInteralFrame窗口
					frame.setSelected(true);
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static ActionListener getBookModiDelAction(){
		if(BOOK_MODIFYDELETE == null)
			BOOK_MODIFYDELETE = new BookModiDelAction();
		return BOOK_MODIFYDELETE;
	}
	
	//查询图书信息
	private static class BookQueryAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!frames.containsKey("图书信息查询") || frames.get("图书信息查询").isClosed()) {
				try {
					BookQueryFrame frame = new BookQueryFrame();
					frames.put("图书信息查询", frame);
					Library.addFrame(frame);
					//显示JInteralFrame窗口
					frame.setSelected(true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	
	public static ActionListener getBookQueryAction() {
		if(BOOK_QUERY == null)
			BOOK_QUERY = new BookQueryAction();
		return BOOK_QUERY;
	}
	
	//修改图书类别的窗口
	private static class BookTypeAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!frames.containsKey("修改图书类别") || frames.get("修改图书类别").isClosed()) {
				try {
					BookTypeFrame frame = new BookTypeFrame();
					frames.put("修改图书类别", frame);
					Library.addFrame(frame);
					//显示JInteralFrame窗口
					frame.setSelected(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static ActionListener getBookTypeAction() {
		if(BOOK_TYPE == null)
			BOOK_TYPE = new BookTypeAction();
		return BOOK_TYPE;
	}
	
	//关于软件
	private static class AboutSoftAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!frames.containsKey("关于软件") || frames.get("关于软件").isClosed()) {
				try {
					AboutSoftFrame frame = new AboutSoftFrame();
					frames.put("关于软件", frame);
					Library.addFrame(frame);
					//显示JInteralFrame窗口
					frame.setSelected(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static ActionListener getAboutSoftAction() {
		if(About_Soft == null)
			About_Soft = new AboutSoftAction();
		return About_Soft;
	}
	
	//用户信息管理
	private static class UserManageAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!frames.containsKey("用户信息管理") || frames.get("用户信息管理").isClosed()) {
				try {
					UserManageFrame frame = new UserManageFrame();
					frames.put("用户信息管理",frame);
					//显示JInteralFrame窗口
					Library.addFrame(frame);
					frame.setSelected(true);
				}catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static ActionListener getUserManageAction() {
		if(User_Manage == null)
			User_Manage = new UserManageAction();
		return User_Manage;
	}
	
	//修改用户密码
	private static class UserPasswordAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!frames.containsKey("修改用户密码") || frames.get("修改用户密码").isClosed()) {
				try {
					UserPasswordModiFrame frame = new UserPasswordModiFrame();
					frames.put("修改用户密码", frame);
					//显示JInteralFrame窗口
					Library.addFrame(frame);
					frame.setSelected(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
			}
		}	
	}
	
	public static ActionListener getUserPasswordAction() {
		if(User_Password == null)
			User_Password = new UserPasswordAction();
		return User_Password;
	}
	
	//图书借阅管理
	private static class BookBorrowAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!frames.containsKey("图书借阅管理") || frames.get("图书借阅管理").isClosed()) {
				try {
					BorrowFrame frame = new BorrowFrame();
					frames.put("图书借阅管理",frame);
					Library.addFrame(frame);
					//显示窗口
					frame.setSelected(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
			}
		}	
	}
	
	public static ActionListener getBookBorrowAction() {
		if(BOOK_BORROW == null)
			BOOK_BORROW = new BookBorrowAction();
		return BOOK_BORROW;
	}
}
