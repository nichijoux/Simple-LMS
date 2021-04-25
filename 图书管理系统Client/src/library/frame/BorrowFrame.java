package library.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import library.data.Borrow;
import library.data.ListBorrow;
import library.util.LoadIcon;

//借阅管理的窗口界面
@SuppressWarnings("serial")
public class BorrowFrame extends JInternalFrame {
	private JTable table;							//显示信息的table
	private String[][] historyResult;				//历史借阅信息
	private String[][] currentResult;				//当前借阅图书信息
	private String[] columnNames = {"借阅图书","作者","图书类别","借阅日期","归还日期"};					//table首行列名
	private JComboBox<String> queryComboBox;		//历史借阅查询 or 当前借阅查询
	private JComboBox<String> bookTypeBox;			//图书类别
	private JTextField bookNameField;				//书名
	private JTextField authorField;					//作者
	private int preSelect;							//queryComboBox前一次选择的index
	private JPanel panel;
	private JButton borrowBookButton;
	private JButton returnBookButton;
	
	
	public BorrowFrame() {
		super();
		setClosable(true);		//可关闭
		setBounds(200, 140, 700, 435);
		setTitle("借阅管理");
		
		//logo图片显示
		JLabel headLogo = new JLabel();
		headLogo.setBackground(Color.CYAN);
		headLogo.setOpaque(true);
		headLogo.setPreferredSize(new Dimension(700,80));
		headLogo.setIcon(LoadIcon.getIcon("borrowManage.jpg"));
		headLogo.setBorder(new LineBorder(SystemColor.activeCaptionBorder,1,false));
		
		JScrollPane scrollPane = new JScrollPane();
		
		queryComboBox = new JComboBox<String>();
		queryComboBox.setFont(new Font("宋体", Font.PLAIN, 14));
		queryComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"当前借阅", "历史借阅"}));
		queryComboBox.setSelectedIndex(0);
		queryComboBox.addActionListener(new selectQueryAction());
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "想要借阅的书", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		FlowLayout layout = new FlowLayout();
		layout.setHgap(20);
		panel.setLayout(layout);
		
		borrowBookButton = new JButton("确认借书");
		borrowBookButton.setFont(new Font("宋体", Font.PLAIN, 16));
		borrowBookButton.addActionListener(new borrowBookAction());
		
		returnBookButton = new JButton("归还图书");
		returnBookButton.setFont(new Font("宋体", Font.PLAIN, 16));
		returnBookButton.addActionListener(new returnBookAction());
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(headLogo, GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 560, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(queryComboBox, 0, 105, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(104)
					.addComponent(borrowBookButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 225, Short.MAX_VALUE)
					.addComponent(returnBookButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addGap(128))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(headLogo, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
							.addComponent(queryComboBox, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
							.addGap(77))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(borrowBookButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addComponent(returnBookButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addGap(15))
		);
		
		JLabel bookNameLabel = new JLabel("书名：");
		bookNameLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		panel.add(bookNameLabel);
		
		bookNameField = new JTextField();
		bookNameField.setFont(new Font("宋体", Font.PLAIN, 16));
		panel.add(bookNameField);
		bookNameField.setColumns(10);
		
		JLabel authorLabel = new JLabel("作者：");
		authorLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		panel.add(authorLabel);
		
		authorField = new JTextField();
		authorField.setFont(new Font("宋体", Font.PLAIN, 16));
		panel.add(authorField);
		authorField.setColumns(10);
		
		JLabel bookTypeLabel = new JLabel("书籍类别：");
		bookTypeLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		panel.add(bookTypeLabel);
		
		bookTypeBox = new JComboBox<String>();
		panel.add(bookTypeBox);
		bookTypeBox.setFont(new Font("宋体", Font.PLAIN, 16));
		
		//bookTypeBox添加项目
		try {
			List<String> list = BookTypeFrame.getBookTypes();
			for(String item:list)
				bookTypeBox.addItem(item);
			if(list.size() != 0)
				bookTypeBox.setSelectedIndex(0);//默认选中
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		table = new JTable();
		table.setFont(new Font("宋体", Font.PLAIN, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"借阅图书","作者","图书类别","借阅日期","归还日期"
			}
		) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);
		
		//填充默认数据
		try {
			buildCurrentTable();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		//窗口可视
		setVisible(true);
	}
	
	private class selectQueryAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int curselect = queryComboBox.getSelectedIndex();
			if(curselect == preSelect) {
				return;
			}
			
			if(curselect == 0) {
				try {
					buildCurrentTable();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				//设置可访问
				panel.setEnabled(true);
				borrowBookButton.setEnabled(true);
				returnBookButton.setEnabled(true);
			}else {
				try {
					buildHistoryTable();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				//设置不可访问
				panel.setEnabled(false);
				borrowBookButton.setEnabled(false);
				returnBookButton.setEnabled(false);
			}
			preSelect = curselect;
		}
	}
	
	//借阅图书
	private class borrowBookAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String bookname = bookNameField.getText().trim();
			String author = authorField.getText().trim();
			String bookType = (String)bookTypeBox.getSelectedItem();
			if(bookname.length() == 0 || author.length() == 0) {
				JOptionPane.showMessageDialog(null, "借阅书籍信息错误,请重试");
				return;
			}
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				String command = "borrowBook\n";
				bookname += "\n";
				author += "\n";
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				outputStream.write((LoginFrame.UserName + "\n").getBytes(LoginFrame.charset));
				outputStream.write(bookname.getBytes(LoginFrame.charset));
				outputStream.write(author.getBytes(LoginFrame.charset));
				outputStream.write(bookType.getBytes(LoginFrame.charset));
				outputStream.flush();
				client.shutdownOutput();
				
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(flag) {
					buildHistoryTable();
					buildCurrentTable();
					JOptionPane.showMessageDialog(null, "借阅成功");
				}else {
					JOptionPane.showMessageDialog(null, "借阅失败,请重试");
				}
				
				outputStream.close();
				inputStream.close();
				client.close();
			} catch (IOException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}	
	}

	//归还图书
	private class returnBookAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table.getSelectedRowCount() > 1) {
				JOptionPane.showMessageDialog(null, "不支持一件归还");
				return;
			}else if(table.getSelectedRowCount() == 0) {
				return;
			}
			int index = table.getSelectedRow();
			//C/S通信
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				String command = "returnBook\n";
				String bookname = currentResult[index][0] + "\n";
				String author = currentResult[index][1] + "\n";
				String booktype = currentResult[index][2];
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				outputStream.write((LoginFrame.UserName + "\n").getBytes(LoginFrame.charset));
				outputStream.write(bookname.getBytes(LoginFrame.charset));
				outputStream.write(author.getBytes(LoginFrame.charset));
				outputStream.write(booktype.getBytes(LoginFrame.charset));
				outputStream.flush();
				client.shutdownOutput();
				
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(flag) {
					buildHistoryTable();
					buildCurrentTable();
					JOptionPane.showMessageDialog(null, "归还成功");
				}else {
					JOptionPane.showMessageDialog(null, "归还失败,请重试");
				}
				
				outputStream.close();
				client.close();
			} catch (IOException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//读取数据库的相关信息放入表格
	private void buildHistoryTable() throws UnknownHostException,ClassNotFoundException,IOException{
		List<Borrow> list = getBorrowInfo("history");
		int len = list.size();
		historyResult = new String[len][columnNames.length];
		for(int i = 0;i < len;i++) {
			Borrow borrow = list.get(i);
			historyResult[i][0] = borrow.getBookName();
			historyResult[i][1] = borrow.getAuthor();
			historyResult[i][2] = borrow.getBookType();
			historyResult[i][3] = borrow.getBorrowDate().toString();
			historyResult[i][4] = borrow.getReturnDate().toString();
		}
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setDataVector(historyResult, columnNames);
	}
	
	//读取数据库的相关信息放入表格
	private void buildCurrentTable() throws UnknownHostException,ClassNotFoundException,IOException{
		List<Borrow> list = getBorrowInfo("current");
		int len = list.size();
		currentResult = new String[len][columnNames.length];
		for(int i = 0;i < len;i++) {
			Borrow borrow = list.get(i);
			currentResult[i][0] = borrow.getBookName();
			currentResult[i][1] = borrow.getAuthor();
			currentResult[i][2] = borrow.getBookType();
			currentResult[i][3] = borrow.getBorrowDate().toString();
			currentResult[i][4] = borrow.getReturnDate() == null?"未归还":borrow.getReturnDate().toString();
		}
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setDataVector(currentResult, columnNames);
	}
	
	//读取数据库,获得借阅数据
	private List<Borrow> getBorrowInfo(String queryMessage) throws UnknownHostException,ClassNotFoundException,IOException{
		String command = "getBorrowInfo\n";
		Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
		OutputStream outputStream = client.getOutputStream();
		outputStream.write(command.getBytes(LoginFrame.charset));
		outputStream.write((LoginFrame.UserName + "\n").getBytes(LoginFrame.charset));
		outputStream.write(queryMessage.getBytes(LoginFrame.charset));
		outputStream.flush();
		client.shutdownOutput();
		
		ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
		ListBorrow listBorrow = (ListBorrow)inputStream.readObject();
		
		outputStream.close();
		inputStream.close();
		client.close();
		return listBorrow.getList();
	}
}
