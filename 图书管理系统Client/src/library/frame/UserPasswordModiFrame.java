package library.frame;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;

//修改密码的窗口
@SuppressWarnings("serial")
public class UserPasswordModiFrame extends JInternalFrame {
	private JTextField passwordField1;			//原密码
	private JPasswordField passwordField2;		//要修改的密码
	private JPasswordField passwordField3;		//重复密码
	
	//构造函数
	public UserPasswordModiFrame() {
		super();
		setResizable(true);
		setClosable(true);
		setBounds(300, 200, 317, 277);
		setTitle("用户密码修改");
		
		JLabel passwordLabel1 = new JLabel("当前密码：");
		passwordLabel1.setFont(new Font("宋体", Font.PLAIN, 14));
		
		JLabel passwordLabel2 = new JLabel("修改密码：");
		passwordLabel2.setFont(new Font("宋体", Font.PLAIN, 14));
		
		JLabel passwordLabel3 = new JLabel("重复密码：");
		passwordLabel3.setFont(new Font("宋体", Font.PLAIN, 14));
		
		passwordField1 = new JTextField();
		passwordField1.setFont(new Font("宋体", Font.PLAIN, 14));
		passwordField1.setColumns(10);
		
		passwordField2 = new JPasswordField();
		passwordField3 = new JPasswordField();
		
		JButton modiButton = new JButton("修改");
		modiButton.addActionListener(new modifyAction());
		modiButton.setFont(new Font("宋体", Font.PLAIN, 14));
		
		JButton exitButton = new JButton("退出");
		exitButton.addActionListener(new exitAction());
		exitButton.setFont(new Font("宋体", Font.PLAIN, 14));
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(passwordLabel3, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(passwordField3))
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addComponent(passwordLabel2, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(passwordField2))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(passwordLabel1, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)))
							.addContainerGap(54, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGap(20)
							.addComponent(modiButton, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
							.addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addGap(58))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(28)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(passwordLabel1, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel2, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(passwordField2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel3, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(passwordField3, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(modiButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
		
		setVisible(true);
	}
	
	//修改按钮
	private class modifyAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String password1 = passwordField1.getText().trim();//原密码
			if(!password1.equals(LoginFrame.PassWord)) {
				JOptionPane.showMessageDialog(null, "原密码输入错误");
				return;
			}
			String password2 = new String(passwordField2.getPassword()).trim();
			String password3 = new String(passwordField3.getPassword()).trim();
			if(!password2.equals(password3)) {
				JOptionPane.showMessageDialog(null, "修改密码与重复密码不同,请重新输入");
				return;
			}
			if(password2.equals(password1)) {
				JOptionPane.showMessageDialog(null, "修改密码不可与原密码相同");
				return;
			}
			
			String command = "modifyUserPassword\n";
			String username = LoginFrame.UserName + "\n";
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				outputStream.write(username.getBytes(LoginFrame.charset));
				outputStream.write(password2.getBytes(LoginFrame.charset));
				outputStream.flush();
				client.shutdownOutput();
				
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(flag) {
					JOptionPane.showMessageDialog(null, "修改成功");
					passwordField1.setText("");
					passwordField2.setText("");
					passwordField3.setText("");
				}else {
					JOptionPane.showMessageDialog(null, "修改失败,请重试");
				}
				
				outputStream.close();
				inputStream.close();
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}	
	}

	//退出按钮
	private class exitAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
