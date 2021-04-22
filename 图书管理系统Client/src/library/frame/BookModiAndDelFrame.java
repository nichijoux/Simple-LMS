package library.frame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
//自己的包
import library.data.Book;
import library.data.ListBook;
import library.util.Chooser;
import library.util.LoadIcon;
import library.util.MyDocument;

@SuppressWarnings("serial")
public class BookModiAndDelFrame extends JInternalFrame {
	private static JTable table;		//显示查询结果
	private JTextField ISBN;			//编号
	private JTextField bookName;		//书名
	private JTextField writer;			//作者
	private JComboBox<String> bookType;	//书籍类别
	private JFormattedTextField pubDate;//出版日期
	private JTextField bookNum;			//书籍剩余数量
	private static String[] columnNames = { "图书编号","图书名称", "作者","书籍类别","出版日期","剩余数量"};
	private static int width[] = {70,150,100,90,100,80};//列宽度
	private static Object[][] results;				//存放查询结果
	
	//取数据库中图书相关信息放入表格中
	private static void putObjectIntoTable(List<Book> list){
		if(list == null) {
			return;
		}
		results = new Object[list.size()][columnNames.length];
		for(int i = 0;i < list.size();i++){
			Book bookinfo = list.get(i);
			results[i][0] = Integer.toString(bookinfo.getBookID());
			results[i][1] = bookinfo.getBookName();
			results[i][2] = bookinfo.getAuthor();
			results[i][3] = bookinfo.getBookType();
			results[i][4] = bookinfo.getBookDate();
			results[i][5] = bookinfo.getBookNum();
		}  		
	}
	
	//根据model创建JTable
    private static JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setShowGrid(true);	//显示表格
        table.setRowHeight(20);		//行高度
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//被选择行的背景色为蓝色
        table.setSelectionBackground(Color.BLUE);
        table.setFont(new Font("楷体", Font.PLAIN, 12));
        return table;
    }
	
    //设置JTable间隔
    private static TableColumnModel getColumn(JTable table, int[] width) {
        TableColumnModel columns = table.getColumnModel();
        for (int i = 0; i < width.length; i++) {
            TableColumn column = columns.getColumn(i);
            column.setPreferredWidth(width[i]);
        }
        return columns;
    }
    
    //Table的初始化和修改
    private void buildTable() {
    	//可选中,不可直接编辑
		DefaultTableModel model = new DefaultTableModel(results,columnNames) {
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		table = buildTable(model);
		//600像素宽,设置各个模块的宽度
		table.setColumnModel(getColumn(table, width));
		//设置不可自动调整
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//添加点击选中事件
		table.addMouseListener(new TableListener());
        //列大小不能变化
        table.getTableHeader().setResizingAllowed(false);
		table.setVisible(true);
    }
    
    //构造函数
	public BookModiAndDelFrame() throws Exception {
		super();
		
		Font font = new Font("宋体", Font.PLAIN, 16);
		
		BorderLayout borderLayout = new BorderLayout();
		getContentPane().setLayout(borderLayout);
		setIconifiable(false);							//设置窗口可最小化
		setClosable(true);								//设置窗口可关闭
		setTitle("图书信息修改");						//设置窗口标题
		setBounds(100, 100, 640, 406);					//设置窗口初始位置和大小
		setResizable(false);							//不可更改窗口大小
		
		JPanel panel_1 = new JPanel();
		// 为当前活动窗口的窗口标题提供的背景色。
		panel_1.setBorder(new LineBorder(SystemColor.activeCaptionBorder, 1, false));
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		FlowLayout flowLayout = new FlowLayout();				//流式布局
		flowLayout.setVgap(2);
		flowLayout.setHgap(30);
		flowLayout.setAlignment(FlowLayout.RIGHT);				//右对齐
		panel_1.setLayout(flowLayout);
		
		//更改书籍信息按钮
		JButton buttonmodiry = new JButton();
		buttonmodiry.addActionListener(new ModifyBookActionListener());
		buttonmodiry.setFont(font);
		buttonmodiry.setText("修改");
		panel_1.add(buttonmodiry);
		
		//删除书籍信息按钮
		JButton buttondelete = new JButton();
		buttondelete.setText("删除");
		buttondelete.setFont(font);
		buttondelete.addActionListener(new DeleteBookActionListener());
		panel_1.add(buttondelete);
		
		//刷新书籍信息按钮
		JButton buttonflush = new JButton();
		buttonflush.setText("刷新");
		buttonflush.setFont(font);
		buttonflush.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					flushFrame();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel_1.add(buttonflush);
		

		//图片加载
		JLabel headLogo = new JLabel();
		headLogo.setIcon(LoadIcon.getIcon("bookModiAndDel.jpg"));
		headLogo.setOpaque(true);
		headLogo.setBackground(Color.CYAN);//获取 ARGB 值为 #FF00FFFF 的系统定义的颜色。
		headLogo.setPreferredSize(new Dimension(640, 80));
		headLogo.setBorder(new LineBorder(SystemColor.activeCaptionBorder, 1, false));
		getContentPane().add(headLogo, BorderLayout.NORTH);
		
		JPanel tablePanel = new JPanel();
		BorderLayout borderLayout_1 = new BorderLayout();
		borderLayout_1.setVgap(5);
		tablePanel.setLayout(borderLayout_1);
		tablePanel.setBorder(new EmptyBorder(5, 10, 5, 10));
		getContentPane().add(tablePanel);
		
		JScrollPane scrollPane = new JScrollPane();				//滑动条
		tablePanel.add(scrollPane);

		//读取数据库,获取信息
		putObjectIntoTable(getBookInfo());
		//JTable显示书籍信息
		buildTable();
		table.getTableHeader().setFont(new Font("宋体", Font.PLAIN, 12));
		//默认选中第一行
		if(results != null && results.length != 0)
			table.setRowSelectionInterval(0,0);
		//添加滑动块
		scrollPane.setViewportView(table);
		
		//书籍信息的布局设置
		JPanel bookPanel = new JPanel();
		tablePanel.add(bookPanel, BorderLayout.SOUTH);
		GridLayout gridLayout = new GridLayout(0, 6);			//网格布局
		gridLayout.setVgap(5);
		gridLayout.setHgap(5);
		bookPanel.setLayout(gridLayout);

		JLabel bookISBNLabel = new JLabel();
		bookISBNLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookISBNLabel.setText("书号：");
		bookISBNLabel.setFont(font);
		bookPanel.add(bookISBNLabel);

		ISBN = new JTextField();
		ISBN.setDocument(new MyDocument(13)); 	//ISBN最多为13位
		ISBN.setEditable(false);				//设置不可更改
		bookPanel.add(ISBN);

		JLabel bookNameLabel = new JLabel();
		bookNameLabel.setFont(font);
		bookNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookNameLabel.setText("书名：");
		bookPanel.add(bookNameLabel);

		bookName = new JTextField();
		bookName.setFont(font);
		bookPanel.add(bookName);

		JLabel authorLabel = new JLabel();
		authorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		authorLabel.setFont(font);
		authorLabel.setText("作者：");
		bookPanel.add(authorLabel);

		writer = new JTextField();
		writer.setFont(font);
		bookPanel.add(writer);

		JLabel bookTypeLabel = new JLabel();
		bookTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookTypeLabel.setFont(font);
		bookTypeLabel.setText("书籍类别: ");
		bookPanel.add(bookTypeLabel);
		
		bookType = new JComboBox<String>();
		bookType.setFont(font);
		bookPanel.add(bookType);
		
		JLabel DataLabel = new JLabel();
		DataLabel.setHorizontalAlignment(SwingConstants.CENTER);
		DataLabel.setText("出版日期：");
		DataLabel.setFont(font);
		bookPanel.add(DataLabel);
		
		SimpleDateFormat myfmt = new SimpleDateFormat("yyyy-MM-dd");
		pubDate = new JFormattedTextField(myfmt);
		pubDate.setFont(font);
		pubDate.setValue(new java.util.Date());
		Chooser.getInstance().register(pubDate);
		bookPanel.add(pubDate);

		JLabel bookNumLabel = new JLabel();
		bookNumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookNumLabel.setText("剩余数量:");
		bookNumLabel.setFont(font);
		bookPanel.add(bookNumLabel);
		
		bookNum = new JTextField();
		bookNum.setFont(font);
		bookPanel.add(bookNum);
		
		//Jcombox框添加项目
		List<String> list = BookTypeFrame.getBookTypes();
		for(String item:list) {
			bookType.addItem(item);
		}
		
		//默认选中
		if(results != null) {
			ISBN.setText(results[0][0].toString());
			bookName.setText(results[0][1].toString());
			writer.setText(results[0][2].toString());
			bookType.setSelectedItem(results[0][3]);
			pubDate.setText(results[0][4].toString());
			bookNum.setText(results[0][5].toString());
		}
		
		//设置窗口可见
		setVisible(true);
	}
	
	//显示选中书本的ISBN,bookName,author,date数据
	private class TableListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			String ISBNs,bookNames,writers,dates,bookNums;
			int selRow = table.getSelectedRow();
			ISBNs = table.getValueAt(selRow, 0).toString().trim();
			bookNames = table.getValueAt(selRow, 1).toString().trim();
			writers = table.getValueAt(selRow, 2).toString().trim();
			dates = table.getValueAt(selRow, 4).toString().trim();
			bookNums = table.getValueAt(selRow, 5).toString().trim();
			
			ISBN.setText(ISBNs);		
			bookName.setText(bookNames);
			writer.setText(writers);
			bookType.setSelectedItem(table.getValueAt(selRow, 3));
			pubDate.setText(dates);
			bookNum.setText(bookNums);
		}
	}
	
	//修改事件
	class ModifyBookActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(table.getSelectedRowCount() > 1) {
				JOptionPane.showMessageDialog(null, "不支持多行修改");
				return;
			}
			
			String id = ISBN.getText().trim();		
			String bookNames = bookName.getText().trim();
			String writers = writer.getText().trim();
			String pubDates = pubDate.getText().trim();
			String bookNums = bookNum.getText().trim();
			
			//修改图书信息表
			if(bookNames.length() == 0){
				JOptionPane.showMessageDialog(null, "图书名称文本框不可以为空");
				return;
			}
			else if(writers.length() == 0){
				JOptionPane.showMessageDialog(null, "作者文本框不可以为空");
				return;
			}
			else if(pubDates.length() == 0){
				JOptionPane.showMessageDialog(null, "出版日期文本框不可以为空");
				return;
			}else if(bookNums.length() == 0) {
				JOptionPane.showMessageDialog(null, "剩余数量不可为空");
				return;
			}else if(Integer.parseInt(bookNums) < 0){
				JOptionPane.showMessageDialog(null, "剩余数量不可能为负数");
				return;
			}
			
			//接下来建立TCP连接修改代码
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				//建议连接后尝试修改数据
				String command = "modifyBookInfo\n";
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				
				outputStream.write((id + "\n").getBytes(LoginFrame.charset));
				outputStream.write((bookNames + "\n").getBytes(LoginFrame.charset));
				outputStream.write((writers + "\n").getBytes(LoginFrame.charset));
				outputStream.write((bookType.getSelectedItem() + "\n").getBytes(LoginFrame.charset));
				outputStream.write((pubDates + "\n").getBytes(LoginFrame.charset));
				outputStream.write(bookNums.getBytes(LoginFrame.charset));
				outputStream.flush();
				//通知server端,client不会再次输入数据
				client.shutdownOutput();
				//查看是否修改成功
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(flag) {
					JOptionPane.showMessageDialog(null, "修改成功");
					int row = table.getSelectedRow();
					//JTable的内容也要修改
					results[row][0] = id;
					results[row][1] = bookNames;
					results[row][2] = writers;
					results[row][3] = bookType.getSelectedItem();
					results[row][4] = pubDates;
					results[row][5] = bookNums;
					DefaultTableModel model = new DefaultTableModel() {
						public boolean isCellEditable(int row, int column)
						{
							return false;
						}
					};
					model.setDataVector(results, columnNames);
					table.setPreferredSize(table.getSize());
					table.setModel(model);
			        //列大小不能变化
			        table.getTableHeader().setResizingAllowed(false);
					//600像素宽,设置各个模块的宽度
					table.setColumnModel(getColumn(table, width));
				}else {
					JOptionPane.showMessageDialog(null, "修改失败");
				}
				
				//关闭输入输出流
				outputStream.close();
				inputStream.close();	
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//删除图书信息
	class DeleteBookActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table.getSelectedRowCount() > 1) {
				JOptionPane.showMessageDialog(null, "不支持多行删除");
				return;
			}
			
			String id = ISBN.getText().trim();
			if(id.length() == 0) {
				return;
			}
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				//建议连接后尝试修改数据
				String command = "deleteBookInfo\n";
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				outputStream.write(id.getBytes(LoginFrame.charset));
				outputStream.flush();
				//通知server端,client不会再次输入数据
				client.shutdownOutput();
				
				//查看是否修改成功
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(flag) {
					JOptionPane.showMessageDialog(null, "删除成功");
					//JTable的内容也要修改
					//results会根据查询结果更改
					putObjectIntoTable(getBookInfo());
					DefaultTableModel model = new DefaultTableModel() {
						public boolean isCellEditable(int row, int column)
						{
							return false;
						}
					};
					model.setDataVector(results, columnNames);
					table.setModel(model);
			        //列大小不能变化
			        table.getTableHeader().setResizingAllowed(false);
					//600像素宽,设置各个模块的宽度
					table.setColumnModel(getColumn(table, width));
				}else {
					JOptionPane.showMessageDialog(null, "删除失败");
				}
				//关闭输入输出流
				outputStream.close();
				inputStream.close();	
				client.close();
			} catch (IOException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//读取数据库,获取书籍信息
	public static List<Book> getBookInfo() throws UnknownHostException, IOException, ClassNotFoundException{
		List<Book> bookinfo;
		//建立连接
		Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
		//建立连接后尝试读取数据
		String command = "getBookInfo";
		
		OutputStream outputStream = client.getOutputStream();
		outputStream.write(command.getBytes(LoginFrame.charset));
		outputStream.flush();
		//通知server端,client不会再次输入数据
		client.shutdownOutput();
		
		//接受List<Book>数据
		ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
		//包名也应该相同
		ListBook listBook = (ListBook)inputStream.readObject();
		bookinfo = listBook.getList();
		
		//关闭输入输出流
		outputStream.close();
		inputStream.close();	
		client.close();
		return bookinfo;
	}

	//更新窗口信息
	public static void flushFrame() throws UnknownHostException, ClassNotFoundException, IOException {
		putObjectIntoTable(getBookInfo());
		DefaultTableModel model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		model.setDataVector(results, columnNames);
		table.setPreferredSize(table.getSize());
        //列大小不能变化
        table.getTableHeader().setResizingAllowed(false);
		table.setModel(model);
		//600像素宽,设置各个模块的宽度
		table.setColumnModel(getColumn(table, width));
		if(results != null && results.length != 0)
			table.setRowSelectionInterval(0, 0);
	}
}