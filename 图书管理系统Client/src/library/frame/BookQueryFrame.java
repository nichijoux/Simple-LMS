package library.frame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
//自己的包
import library.data.Book;
import library.data.ListBook;
import library.util.LoadIcon;
import library.util.MyDocument;

//查询窗口
@SuppressWarnings("serial")
public class BookQueryFrame extends JInternalFrame {
	private JTextField queryField;				//查询框
	private JButton	queryButton;				//查询按钮
	private JTable table;						//显示查询结果
	private JComboBox<String> box;				//选项卡
	private int maxQueryLen = 20;				//查询框最多查询的字数
	private static Object[][] results;			//存放查询结果
	private static String[] columnNames = { "图书编号","图书名称", "作者","书籍类别","出版日期","剩余数量"};
	private static int width[] = {70,150,100,90,100,80};//列宽度

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
	
	//建立JTable
    private void buildTable() {
    	//可选中,不可直接编辑
		DefaultTableModel model = new DefaultTableModel(results,columnNames) {
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		table = new JTable(model);
		table.setShowGrid(true);			//显示表格
		table.setRowHeight(20);				//设置航高度
		//600像素宽,设置各个模块的宽度
		table.setColumnModel(getColumn(table, width));
		//设置不可自动调整
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//被选择行的背景色为蓝色
        table.setSelectionBackground(Color.BLUE);
        //列大小不能变化
        table.getTableHeader().setResizingAllowed(false);
		//显示table
		table.setVisible(true);
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
	
	//构造函数
	public BookQueryFrame() throws UnknownHostException, MalformedURLException {
		super();
		setClosable(true);								//设置窗口可关闭
		setTitle("图书信息查询");						//设置窗口标题
		setBounds(100, 100, 640, 406);					//设置窗口初始位置和大小
		setResizable(false);							//不可更改窗口大小
		
		Font font = new Font("楷体", Font.PLAIN, 16);
		
		//图片加载
		JLabel headLogo = new JLabel();
		headLogo.setIcon(LoadIcon.getIcon("bookQueryInfo.jpg"));
		headLogo.setOpaque(true);//透明设置
		headLogo.setBackground(Color.CYAN);//获取 ARGB 值为 #FF00FFFF 的系统定义的颜色。
		headLogo.setPreferredSize(new Dimension(590, 80));
		headLogo.setBorder(new LineBorder(SystemColor.activeCaptionBorder, 1, false));
		getContentPane().add(headLogo, BorderLayout.NORTH);//图片放置最顶端
		
		//查询界面的布局
		JPanel queryPanel = new JPanel();
		//为当前活动窗口的窗口标题提供的背景色。
		queryPanel.setBorder(new LineBorder(SystemColor.activeCaptionBorder, 1, false));
		getContentPane().add(queryPanel, BorderLayout.SOUTH);	//放在最下端
		FlowLayout flowLayout = new FlowLayout();				//流式布局
		flowLayout.setVgap(10);
		flowLayout.setHgap(12);
		flowLayout.setAlignment(FlowLayout.CENTER);				//中间对齐
		queryPanel.setLayout(flowLayout);
		
		//table设计
		JPanel tablePanel = new JPanel();
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(5);
		tablePanel.setLayout(borderLayout);
		tablePanel.setBorder(new EmptyBorder(5, 10, 5, 10));
		getContentPane().add(tablePanel);
		
		buildTable();
		JScrollPane scrollPane = new JScrollPane();				//滑动条
		table.getTableHeader().setFont(new Font("楷体", Font.PLAIN, 12));
		tablePanel.add(scrollPane);
		scrollPane.setViewportView(table);						//添加滑动条
		
		
		//query界面的框体设计
		JLabel suggestLabel = new JLabel();
		suggestLabel.setText("请输入查询信息: ");
		suggestLabel.setFont(font);
		queryPanel.add(suggestLabel);
		
		//查询框
		queryField = new JTextField(maxQueryLen);
		//查询框最多maxQueryLen个字
		queryField.setDocument(new MyDocument(maxQueryLen));
		queryField.setFont(font);
		queryPanel.add(queryField);
		
		//下拉框
		box = new JComboBox<String>();
		box.addItem("精确查询作者");
		box.addItem("精确查询书名");
		box.addItem("模糊查询作者");
		box.addItem("模糊查询书名");
		box.setFont(font);
		queryPanel.add(box);
		
		//query按钮
		queryButton = new JButton();
		queryButton.setText("查询");
		queryButton.setFont(font);
		queryButton.addActionListener(new QueryBookInfo());
		queryPanel.add(queryButton);
		
		//窗口可见
		setVisible(true);
	}
	
	//查询事件
	class QueryBookInfo implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String info = queryField.getText().trim();
			if(info.length() == 0) {
				JOptionPane.showMessageDialog(null, "请输入查询信息");
				return;
			}
			
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				String command = "queryBookInfo\n";
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				String selectIndex = Integer.toString(box.getSelectedIndex()) + "\n";
				outputStream.write(selectIndex.getBytes(LoginFrame.charset));
				outputStream.write(info.getBytes());
				//通知server端,client不会再次输入数据
				client.shutdownOutput();
				
				//接受List<Book>数据
				ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
				ListBook listBook = (ListBook)inputStream.readObject();
				List<Book> bookinfo = listBook.getList();
				//JTable显示数据
				if(bookinfo == null)
				{
					JOptionPane.showMessageDialog(null, "查询失败");
				}else {
					JOptionPane.showMessageDialog(null, "查询成功");
					putObjectIntoTable(bookinfo);
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
				}
				
				outputStream.close();
				inputStream.close();
				client.close();
			} catch (IOException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}
