package library.frame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import library.util.Chooser;
//自己的包
import library.util.LoadIcon;
import library.util.MyDocument;

//图书添加窗口
@SuppressWarnings("serial")
public class BookAddFrame extends JInternalFrame {
	private JFormattedTextField pubDate;//日期
	private JTextField writer;			//作者
	private JTextField bookName;		//书名
	private JTextField bookNum;			//书籍数量
	private JComboBox<String> bookType;	//书籍类别
	private JButton buttonadd;			//增加按钮
	private JButton buttonclose;		//关闭按钮
	
	public BookAddFrame() throws MalformedURLException, UnknownHostException {
		super();
		
		BorderLayout borderLayout = new BorderLayout();
		getContentPane().setLayout(borderLayout);
		setIconifiable(false);							//设置窗体不可最小化
		setClosable(true);								//设置窗体可关闭
		setTitle("图书信息添加");						//设置窗体标题
		setBounds(200, 200, 396, 240);					//设置窗体位置和大小
		setResizable(false);							//不可更改窗口大小
		
		Font font = new Font("仿宋", Font.PLAIN, 14);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 10, 5, 10));	//上左下右逆时针方法依次指定四个方向距离边框的空白像素
		GridLayout gridLayout = new GridLayout(0, 4);	//网格布局,4列
		gridLayout.setVgap(5);
		gridLayout.setHgap(5);
		panel.setLayout(gridLayout);
		getContentPane().add(panel);

		JLabel nameLabel = new JLabel();
		nameLabel.setText(" 书    名：");
		nameLabel.setFont(font);
		panel.add(nameLabel);
		bookName = new JTextField();
		bookName.setFont(font);
		panel.add(bookName);

		JLabel authorLabel = new JLabel();
		authorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		authorLabel.setText("作    者：");
		authorLabel.setFont(font);
		panel.add(authorLabel);
		writer = new JTextField();
		writer.setFont(font);
		writer.setDocument(new MyDocument(30));
		panel.add(writer);
		
		JLabel booktypeLabel = new JLabel("书籍类型: ");
		booktypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		booktypeLabel.setFont(font);
		panel.add(booktypeLabel);
		bookType = new JComboBox<String>();
		bookType.setFont(font);
		try {
			List<String> list;
			list = BookTypeFrame.getBookTypes();
			for(String item:list) {
				bookType.addItem(item);
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		panel.add(bookType);
		
		JLabel pudataLabel = new JLabel();
		pudataLabel.setText(" 出版日期：");
		pudataLabel.setFont(font);
		panel.add(pudataLabel);

		SimpleDateFormat myfmt = new SimpleDateFormat("yyyy-MM-dd");
		pubDate = new JFormattedTextField(myfmt);
		pubDate.setFont(font);
		pubDate.setValue(new java.util.Date());
		Chooser.getInstance().register(pubDate);//添加日期控件
		panel.add(pubDate);

		JLabel bookNumLabel = new JLabel();
		bookNumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookNumLabel.setText("剩余数量:");
		bookNumLabel.setFont(font);
		panel.add(bookNumLabel);
		
		bookNum = new JTextField();
		bookNum.setFont(font);
		panel.add(bookNum);
		
		JPanel panel2 = new JPanel();
		//边缘框组件
		panel2.setBorder(new LineBorder(SystemColor.activeCaptionBorder, 1, false));
		getContentPane().add(panel2, BorderLayout.SOUTH);
		FlowLayout flowLayout = new FlowLayout();//流式布局
		flowLayout.setVgap(2);
		flowLayout.setHgap(30);
		flowLayout.setAlignment(FlowLayout.RIGHT);//右对齐
		panel2.setLayout(flowLayout);

		buttonadd = new JButton();
		buttonadd.setText("添加");
		buttonadd.setFont(font);
		buttonadd.addActionListener(new AddBookActionListener());
		panel2.add(buttonadd);

		buttonclose = new JButton();
		buttonclose.setText("关闭");
		buttonclose.setFont(font);
		buttonclose.addActionListener(new CloseActionListener());
		panel2.add(buttonclose);

		JLabel label_4 = new JLabel();
		//设置背景
		label_4.setIcon(LoadIcon.getIcon("newBookorderImg.jpg"));
		label_4.setPreferredSize(new Dimension(400, 80));
		label_4.setBorder(new LineBorder(SystemColor.activeCaptionBorder, 1, false));
		getContentPane().add(label_4, BorderLayout.NORTH);
		label_4.setText("新书订购");
		
		//默认选中
		bookType.setSelectedIndex(0);
		setVisible(true);											// 显示窗体可关闭－－－必须在添加所有控件之后执行该语句
	}

	//添加关闭按钮的事件监听器
	class CloseActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			doDefaultCloseAction();
		}
	}
	
	// 添加按钮的单击事件监听器
	class AddBookActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//.trim()删除首位空白字符
			String bookNames = bookName.getText().trim();
			String writers = writer.getText().trim();
			String pubDates = pubDate.getText().trim();
			String bookNums = bookNum.getText().trim();
			
			if(bookNames.length() == 0){
				JOptionPane.showMessageDialog(null, "图书名称文本框不可以为空");
				return;
			}
			if(writers.length() == 0){
				JOptionPane.showMessageDialog(null, "作者文本框不可以为空");
				return;
			}
			if(pubDates.length() == 0){
				JOptionPane.showMessageDialog(null, "出版日期文本框不可以为空");
				return;
			}else if(bookNums.length() == 0) {
				JOptionPane.showMessageDialog(null, "剩余数量不可为空");
				return;
			}else if(Integer.parseInt(bookNums) < 0){
				JOptionPane.showMessageDialog(null, "剩余数量不可能为负数");
				return;
			}
			
			//添加数据窗口
			//接下来建立TCP连接修改代码
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				//建议连接后尝试修改数据
				String command = "addBookInfo\n";
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				
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
					JOptionPane.showMessageDialog(null, "添加信息成功");
					BookModiAndDelFrame.flushFrame();
				} else {
					JOptionPane.showMessageDialog(null, "添加失败,书籍可能已经存在");
				}
				//最后关闭连接
				inputStream.close();
				outputStream.close();
				client.close();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
