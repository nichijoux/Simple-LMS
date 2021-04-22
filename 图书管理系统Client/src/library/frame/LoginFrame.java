package library.frame;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import library.util.LoadIcon;

//登录窗口
@SuppressWarnings("serial")
public class LoginFrame extends JFrame{
	@SuppressWarnings("unused")
	private static String UserName;			//能正确登录的用户名
	@SuppressWarnings("unused")
	private static String PassWord;			//能正确登录的密码
	private JTextField username;		//用户名
	private JPasswordField password;	//密码
	private JButton loginButton;		//登录按钮
	private JButton resetButton;		//重置按钮
	private JButton registerButton;		//注册按钮
	
	public static boolean UserType;		//用户类型
	public static String hostname = "localhost";
	public static int port = 4399;
	public static String charset = "UTF-8";

	//构造函数
	public LoginFrame() throws UnknownHostException{
		super();
		
		UserName = "";
		PassWord = "";
		
		setTitle("学生管理系统登录界面");		//窗口标题
		setBounds(530,350,404,289);			//默认出现位置和默认大小
		setResizable(false);				//不可调整登录窗口大小
		getContentPane().setLayout(new GridLayout(4,1));		//设置网格布局
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Font font = new Font("仿宋", Font.PLAIN, 16);
		username = new JTextField(15);		//用户名框
		username.setFont(font);
		password = new JPasswordField(15);	//密码框
		password.setFont(font);
		JLabel userLabel = new JLabel();	//显示用户名三字
		userLabel.setIcon(LoadIcon.getIcon("username.jpg"));
		userLabel.setText("用户名：");
		userLabel.setFont(font);
		JLabel passLabel = new JLabel();	//显示密码二字
		passLabel.setIcon(LoadIcon.getIcon("password.jpg"));
		passLabel.setText("密  码：");
		passLabel.setFont(font);
		
		//登录按钮设置
		loginButton = new JButton();
		loginButton.addActionListener(new LoginAction());
		loginButton.setFont(font);
		loginButton.setIcon(LoadIcon.getIcon("loginin.jpg"));
		loginButton.setText("登录");
		//重置按钮设置
		resetButton = new JButton();
		resetButton.addActionListener(new ResetAction());
		resetButton.setFont(font);
		resetButton.setIcon(LoadIcon.getIcon("reset.jpg"));
		resetButton.setText("重置");
		//注册按钮设置
		registerButton = new JButton();
		registerButton.addActionListener(new RegisterAction());
		registerButton.setFont(font);
		registerButton.setIcon(LoadIcon.getIcon("register.jpg"));
		registerButton.setText("注册");
		
		//设置布局
		JPanel paneluser = new JPanel();
		//登录图标
		JLabel iconLabel = new JLabel();		//图片标签
		ImageIcon loginIcon = LoadIcon.getIcon("login.jpg");
		iconLabel.setIcon(loginIcon);			//设置为icon图片
		iconLabel.setOpaque(true);				//不透明
		
		getContentPane().add(iconLabel);
		paneluser.add(userLabel);
		paneluser.add(username);
		JPanel panelpass = new JPanel();
		panelpass.add(passLabel);
		panelpass.add(password);
		JPanel panelbutton = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(40);//水平间隔
		panelbutton.setLayout(flowLayout);
		panelbutton.add(loginButton);
		panelbutton.add(resetButton);
		panelbutton.add(registerButton);

		getContentPane().add(paneluser);
		getContentPane().add(panelpass);
		getContentPane().add(panelbutton);
		
		setIconImage(LoadIcon.getIcon("loginWindow.png").getImage());
		UserType = false;//初始值为false
		setVisible(true);
	}
	
	//清空用户密码
	private class ResetAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			username.setText("");
			password.setText("");
		}
	}
	
	//登录监听器
	private class LoginAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {	
			String user = username.getText().trim();
			String pass = new String(password.getPassword()).trim();
			if(user.length() == 0 || pass.length() == 0)
			{
			 	JOptionPane.showMessageDialog(null, "请输入账号和密码");
				username.setText("");
				password.setText("");
				return;
			}
			else {
				//尝试连接
				try {
					Socket socket = new Socket(hostname,port);
					//建立连接后获得输出流
				    OutputStream outputStream = socket.getOutputStream();
				    outputStream.write("login\n".getBytes(charset));
				    outputStream.write((user + "\n").getBytes(charset));
				    outputStream.write((pass + "\n").getBytes(charset));
				    outputStream.flush();
				    //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
				    socket.shutdownOutput();
				    
				    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
				    boolean flag = inputStream.readBoolean();
				    if(flag)
				    	UserType = inputStream.readBoolean();	//用户类型
				    //关闭输入输出
				    inputStream.close();
				    outputStream.close();
				    
				    if(flag == true) {
				    	UserName = user;
				    	PassWord = pass;
				    	//如果登陆成功
				    	Library library = new Library();
				    	library.setVisible(true);
				    	LoginFrame.this.setVisible(false);
				    }
				    else {
					 	JOptionPane.showMessageDialog(null, "密码错误或用户不存在");
						username.setText("");
						password.setText("");
				    }
				    socket.close();
				} 
				catch (Exception exception) {
					JOptionPane.showMessageDialog(null, "连接超时,请重试");
					exception.printStackTrace();
				}
			}
		}
	}

	//注册账号的监听器
	private class RegisterAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			new RegisterFrame();
		}
	}
}
