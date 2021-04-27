package library.frame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import library.util.LoadIcon;

//注册窗口界面
@SuppressWarnings("serial")
public class RegisterFrame extends JFrame {
	private JPanel contentPane;				//界面布局
	private JTextField usernameField;		//用户名
	private JPasswordField passwordField1;	//初始密码
	private JPasswordField passwordField2;	//确认密码
	private Font font = new Font("宋体", Font.PLAIN, 16);

	//构造函数
	public RegisterFrame() {
		setResizable(false);	//不可调整
		setTitle("用户注册");
		setFont(font);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(530, 370, 408, 248);
		setIconImage(LoadIcon.getIcon("registerFrame.png").getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel usernameLabel = new JLabel("用 户 名：");
		usernameLabel.setFont(font);
		usernameLabel.setIcon(LoadIcon.getIcon("username.jpg"));
		
		JLabel passwordLabel1 = new JLabel("密    码：");
		passwordLabel1.setFont(font);
		passwordLabel1.setIcon(LoadIcon.getIcon("password.jpg"));
		
		JLabel passwordLabel2 = new JLabel("确认密码：");
		passwordLabel2.setFont(font);
		passwordLabel2.setIcon(LoadIcon.getIcon("password.jpg"));
		
		JButton registerButton = new JButton("注册");
		//注册事件
		registerButton.addActionListener(new registerAction());
		registerButton.setFont(font);
		registerButton.setIcon(LoadIcon.getIcon("register.jpg"));
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		passwordField1 = new JPasswordField();
		
		passwordField2 = new JPasswordField();
		
		JButton exitButton = new JButton("退出");
		exitButton.setIcon(LoadIcon.getIcon("exit.jpg"));
		//设置退出事件
		exitButton.addActionListener(new exitAction());
		exitButton.setFont(font);
		
		//窗口布局设置
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(70)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(passwordLabel2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(usernameLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
								.addComponent(passwordLabel1, Alignment.LEADING))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(passwordField2)
								.addComponent(passwordField1)
								.addComponent(usernameField, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(95)
							.addComponent(registerButton)
							.addGap(45)
							.addComponent(exitButton)))
					.addContainerGap(251, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(33)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel1)
						.addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel2)
						.addComponent(passwordField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(registerButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(exitButton))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		//设置窗口可见
		setVisible(true);
	}
	
	//注册账号
	private class registerAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String username = usernameField.getText().trim();
			String password1 = String.valueOf(passwordField1.getPassword()).trim();
			String password2 = String.valueOf(passwordField2.getPassword()).trim();
			if(username.length() == 0 || password1.length() == 0 || password2.length() == 0)
			{
				JOptionPane.showMessageDialog(null, "请输入用户名和密码");
				return;
			}
			if(!password1.equals(password2)) {
				JOptionPane.showMessageDialog(null,"两次输入密码应该一致");
				return;
			}
			username += "\n";
			password1 += "\n";
			//尝试注册
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				//输入流
				OutputStream outputStream = client.getOutputStream();
				outputStream.write("register\n".getBytes(LoginFrame.charset));
				outputStream.write(username.getBytes(LoginFrame.charset));
				outputStream.write(password1.getBytes(LoginFrame.charset));
				outputStream.flush();
				client.shutdownOutput();
				
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				int result = inputStream.readInt();
				
				if(result == 1) {
					JOptionPane.showMessageDialog(null, "账号注册成功");
				}else if(result == 0) {
					JOptionPane.showMessageDialog(null, "用户名已经存在");
				}else {
					JOptionPane.showMessageDialog(null, "账号注册失败,请重试");
				}
				
				//关闭输入输出
				outputStream.close();
				inputStream.close();
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//退出动作
	private class exitAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
