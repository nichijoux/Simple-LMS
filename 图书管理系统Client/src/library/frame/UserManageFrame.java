package library.frame;
import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;
import library.data.ListUser;
import library.data.User;
import library.util.LoadIcon;

import javax.swing.JButton;

//用户管理的窗口
@SuppressWarnings("serial")
public class UserManageFrame extends JInternalFrame {
	private String result[][];
	private JTable table;
	private String[] columnNames = {"用户账号","用户密码","用户类型"};
	private String[] userType = {"管理员","用户"};
	
	//构造函数
	public UserManageFrame() {
		super();
		setClosable(true);
		setBounds(300, 250, 380, 379);
		setTitle("用户信息管理");
		
		JLabel headLogo = new JLabel();
		headLogo.setBackground(Color.CYAN);
		headLogo.setOpaque(true);
		headLogo.setPreferredSize(new Dimension(380,80));
		headLogo.setIcon(LoadIcon.getIcon("UserInfoManage.jpg"));
		headLogo.setBorder(new LineBorder(SystemColor.activeCaptionBorder,1,false));
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton modifyButton = new JButton("修改权限");
		modifyButton.setFont(new Font("宋体", Font.PLAIN, 14));
		modifyButton.addActionListener(new modifyAction());
		
		JButton deleteButton = new JButton("删除");
		deleteButton.setFont(new Font("宋体", Font.PLAIN, 14));
		deleteButton.addActionListener(new deleteAction());
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(69)
					.addComponent(modifyButton, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addGap(32)
					.addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(75, Short.MAX_VALUE))
				.addComponent(headLogo, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(headLogo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(modifyButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
					.addGap(119))
		);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"用户账号", "用户密码", "用户类别"
			}
			) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		//表头不能变化
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setFont(new Font("宋体",Font.PLAIN,16));
		table.setFont(new Font("宋体", Font.PLAIN, 14));
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);
		
		//初始化table数据
		try {
			buildTable();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		//窗口可视
		setVisible(true);
	}
	
	//修改权限的监听器
	private class modifyAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table.getSelectedRowCount() > 1) {
				JOptionPane.showMessageDialog(null, "不支持多行修改");
				return;
			}else if(table.getSelectedRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "请选中要修改的用户");
				return;
			}
			
			String command = "modifyUserPermission\n";				//修改用户权限
			int row = table.getSelectedRow();
			String username = result[row][0];	//用户名
			if(username.equals("root")) {
				JOptionPane.showMessageDialog(null, "该用户不可修改权限");
				return;
			}
			
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				outputStream.write(username.getBytes(LoginFrame.charset));
				outputStream.flush();
				client.shutdownOutput();
				
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(!flag) {
					JOptionPane.showMessageDialog(null, "修改失败");
				}else {
					JOptionPane.showMessageDialog(null, "修改成功");
					result[row][2] = (result[row][2].equals(userType[0]))?userType[1]:userType[0];
					DefaultTableModel model = (DefaultTableModel)table.getModel();
					model.setDataVector(result, columnNames);
				}
				
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//删除用户的监听器
	private class deleteAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table.getSelectedRowCount() > 1) {
				JOptionPane.showMessageDialog(null, "不支持多行删除");
				return;
			}else if(table.getSelectedRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "请选中要删除的用户");
				return;
			}
			
			String command = "deleteUser\n";
			String username = result[table.getSelectedRow()][0];
			if(username.equals("root")) {
				JOptionPane.showMessageDialog(null, "该用户不可删除");
				return;
			}
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				outputStream.write(username.getBytes(LoginFrame.charset));
				outputStream.flush();
				client.shutdownOutput();
				
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(!flag) {
					JOptionPane.showMessageDialog(null, "删除失败,请重试");
				}else {
					JOptionPane.showMessageDialog(null, "删除成功");
					buildTable();
				}
				
				outputStream.close();
				inputStream.close();
				client.close();
			} catch (IOException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//取数据库中的相关信息放入表格
	private void buildTable() throws UnknownHostException, ClassNotFoundException, IOException {
		List<User> list = getUserInfo();
		int len = list.size();
		result = new String[len][3];
		for(int i = 0;i < len;i++) {
			User user = list.get(i);
			result[i][0] = user.getUserName();
			result[i][1] = user.getPassword();
			if(!LoginFrame.UserName.equals("root") && result[i][0].equals("root"))
				result[i][1] = "*********";
			result[i][2] = user.getUserType();
			if(result[i][0].equals("root"))
				result[i][2] = "超级管理员";
		}
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setDataVector(result, columnNames);
	}
	
	//读取数据库,获得用户数据
	private List<User> getUserInfo() throws UnknownHostException, IOException, ClassNotFoundException{
		String command = "getAllUserInfo\n";
		Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
		OutputStream outputStream = client.getOutputStream();
		outputStream.write(command.getBytes(LoginFrame.charset));
		outputStream.flush();
		client.shutdownOutput();
		
		ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
		ListUser listUser = (ListUser)inputStream.readObject();
		
		outputStream.close();
		inputStream.close();
		client.close();
		return listUser.getList();
	}
}
