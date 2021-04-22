package library.frame;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import java.net.MalformedURLException;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import library.util.LoadIcon;

//图书馆管理页面窗口
@SuppressWarnings("serial")
public class Library extends JFrame {
	private static JDesktopPane DESKTOP_PANE = new JDesktopPane();
	
	//添加子窗体
	public static void addFrame(JInternalFrame frame) {
		DESKTOP_PANE.add(frame);
	}
	
	//设置权限
	private void setPermission(Component e) {
		if(!LoginFrame.UserType) {
			//如果用户为读者,则限制其权限
			e.setEnabled(false);
		}
	}
	
	public Library() throws Exception {
		super();
		//设置默认关闭模式为全部关闭
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("重庆大学图书馆管理系统");
		setBounds(220, 80, 1024, 840);
		//设置不可变化
		setResizable(false);
		JMenuBar menuBar = createMenu(); 	//创建菜单栏
		setJMenuBar(menuBar);				//添加菜单栏
		
		JToolBar toolBar = createToolBar(); //创建工具栏
		//获取面板内容,并将toolBar添加到当前面板的最上方
		getContentPane().add(toolBar, BorderLayout.NORTH);

		JLabel label = new JLabel();
		label.setIcon(null); 				// 窗体背景

		DESKTOP_PANE.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				label.setSize(e.getComponent().getSize());
				//添加背景图片
				label.setIcon(LoadIcon.getIcon("backImg.jpg"));
			}
		});
		
		DESKTOP_PANE.add(label,Integer.MIN_VALUE);
		getContentPane().add(DESKTOP_PANE);
		
		setIconImage(LoadIcon.getIcon("Library.png").getImage());
		setVisible(true);
	}
	
	//为toolBar添加button按钮
	private void BarAddButton(JToolBar toolBar,JButton button,String ImageName) throws MalformedURLException {
		//为button添加path的图片
		button.setIcon(LoadIcon.getIcon(ImageName));
		//设置隐藏掉文本
		button.setHideActionText(true);
		//添加组件
		toolBar.add(button);
	}
	
	//创建工具栏
	private JToolBar createToolBar() throws MalformedURLException {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(new BevelBorder(BevelBorder.RAISED));
	
		JButton bookAddButton = new JButton();
		//添加菜单栏图标
		BarAddButton(toolBar,bookAddButton, "bookAddtb.jpg");
		//添加图书的动作监听器
		bookAddButton.addActionListener(MenuActions.getBookAddAction());
		setPermission(bookAddButton);
		
		//在工具栏中添加图书修改与删除图标
		JButton bookModiAndDelButton = new JButton();
		BarAddButton(toolBar,bookModiAndDelButton, "bookModiAndDeltb.jpg");
		//修改图书信息的动作监听器
		bookModiAndDelButton.addActionListener(MenuActions.getBookModiDelAction());
		setPermission(bookModiAndDelButton);
		
		//查找书籍信息(模糊查询)
		JButton bookCheckButton = new JButton();
		BarAddButton(toolBar,bookCheckButton, "newbookQuery.jpg");
		//查询信息动作监听器
		bookCheckButton.addActionListener(MenuActions.getBookQueryAction());
		
		//图书类别添加
		JButton bookTypeModiButton = new JButton();
		BarAddButton(toolBar,bookTypeModiButton, "bookTypeAddtb.jpg");
		//修改图书类别动作监听器
		bookTypeModiButton.addActionListener(MenuActions.getBookTypeAction());
		setPermission(bookTypeModiButton);
		
		//书籍借阅
		JButton bookBorrowButton = new JButton();
		BarAddButton(toolBar,bookBorrowButton, "bookBorrow.jpg");
		
		//用户信息修改与删除
		JButton userModiAndDelButton = new JButton();
		BarAddButton(toolBar,userModiAndDelButton, "userModiAndDeltb.jpg");
		userModiAndDelButton.addActionListener(MenuActions.getUserManageAction());
		setPermission(userModiAndDelButton);
		
		//退出按钮,点击即关闭
		JButton ExitButton = new JButton();
		BarAddButton(toolBar,ExitButton, "exitBook.jpg");
		ExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		return toolBar;
	}
	
	//创建菜单栏
	private JMenuBar createMenu() throws Exception {
		JMenuBar menuBar = new JMenuBar();

		//初始化新书订购管理菜单
		JMenu bookOrderMenu = new JMenu();
		bookOrderMenu.setIcon(LoadIcon.getIcon("xsdgcd.jpg"));
		bookOrderMenu.addActionListener(new TempListener());

		 
		//初始化基础数据维护菜单
		JMenu baseMenu = new JMenu();
		baseMenu.setIcon(LoadIcon.getIcon("jcsjcd.jpg"));
		{
			JMenu readerManagerMenu = new JMenu("读者信息管理");
			readerManagerMenu.setIcon(LoadIcon.getIcon("ReaderManage.png"));
			readerManagerMenu.addActionListener(new TempListener());
			setPermission(readerManagerMenu);
			
			JMenu BookInformationMenu = new JMenu("图书信息管理");
			BookInformationMenu.setIcon(LoadIcon.getIcon("BookManage.png"));
			
			JMenuItem AddBookInfoMenuItem = new JMenuItem("添加图书信息");
			AddBookInfoMenuItem.setIcon(LoadIcon.getIcon("addBookInfoMenuBar.png"));
			AddBookInfoMenuItem.addActionListener(MenuActions.getBookAddAction());
			setPermission(AddBookInfoMenuItem);

			JMenuItem modifyDelInfoMenuItem = new JMenuItem("修改或删除图书信息");
			modifyDelInfoMenuItem.addActionListener(MenuActions.getBookModiDelAction());
			modifyDelInfoMenuItem.setIcon(LoadIcon.getIcon("modifyBookInfoMenuBar.png"));
			setPermission(modifyDelInfoMenuItem);
			
			JMenuItem queryBookInfoMenuItem = new JMenuItem("查询图书信息");
			queryBookInfoMenuItem.addActionListener(MenuActions.getBookQueryAction());
			queryBookInfoMenuItem.setIcon(LoadIcon.getIcon("queryBookInfoMenuBar.png"));

			JMenuItem modifyBookTypeMenuItem = new JMenuItem("修改图书类别");
			modifyBookTypeMenuItem.addActionListener(MenuActions.getBookTypeAction());
			modifyBookTypeMenuItem.setIcon(LoadIcon.getIcon("modifyBookTypeMenuBar.png"));
			setPermission(modifyBookTypeMenuItem);

			//添加菜单栏
			BookInformationMenu.add(AddBookInfoMenuItem);
			BookInformationMenu.add(modifyDelInfoMenuItem);
			BookInformationMenu.add(queryBookInfoMenuItem);
			BookInformationMenu.add(modifyBookTypeMenuItem);
			
			baseMenu.add(readerManagerMenu);
			baseMenu.addSeparator();//添加分隔符
			baseMenu.add(BookInformationMenu);
			baseMenu.addActionListener(new TempListener());
		}
		
		JMenu borrowManageMenu = new JMenu(); 							// 借阅管理
		borrowManageMenu.setIcon(LoadIcon.getIcon("jyglcd.jpg"));
		borrowManageMenu.addActionListener(new TempListener());

		JMenu sysManageMenu = new JMenu(); 								// 系统维护
		sysManageMenu.setIcon(LoadIcon.getIcon("jcwhcd.jpg"));
		sysManageMenu.addActionListener(new TempListener());
		
		JMenuItem aboutSoftItem = new JMenuItem("关于软件");				//关于软件
		aboutSoftItem.setIcon(LoadIcon.getIcon("AboutSoftware.png"));
		aboutSoftItem.addActionListener(MenuActions.getAboutSoftAction());
		sysManageMenu.add(aboutSoftItem);
		
		JMenuItem userManageMenuItem = new JMenuItem("用户管理"); 		// 用户管理
		userManageMenuItem.setIcon(LoadIcon.getIcon("UserManage.png"));
		userManageMenuItem.addActionListener(MenuActions.getUserManageAction());
		setPermission(userManageMenuItem);
		sysManageMenu.add(userManageMenuItem);
		
		//以后再写
		JMenuItem modifyPassWordItem = new JMenuItem("修改当前用户密码");
		sysManageMenu.add(modifyPassWordItem);
		
		menuBar.add(baseMenu); 				// 添加基础数据维护菜单到菜单栏
		menuBar.add(bookOrderMenu);			// 添加新书订购管理菜单到菜单栏
		menuBar.add(borrowManageMenu); 		// 添加借阅管理菜单到菜单栏
		menuBar.add(sysManageMenu); 		// 添加系统维护菜单到菜单栏
		return menuBar;
	}
	
	//不做任何事
	private class TempListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//do nothing
		}
	}
}
