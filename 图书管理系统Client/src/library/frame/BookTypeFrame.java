package library.frame;
import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import library.data.ListBookType;
import library.data.MapBookTypeNum;
//自己的包
import library.util.LoadIcon;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JComboBox;

//图书类别修改
@SuppressWarnings("serial")
public class BookTypeFrame extends JInternalFrame{
	private static List<String> bookType = new ArrayList<String>();						//书籍类别
	private static Map<String, Integer> bookNumMap = new HashMap<String, Integer>();	//键值对
	private static JPanel panel;
	private static JComboBox<String> comboBox;
	private static JScrollPane scrollPane;
	private static JTable table;		//显示table
	private String preSelect;			//comboBox前一次选择的内容
	private static ChartPanel chartPanel;
	private JTextField bookTypeField;
	
	public BookTypeFrame() throws UnknownHostException, ClassNotFoundException, IOException {
		super();
		
		setClosable(true);
		setTitle("图书类别修改");
		getContentPane().setFont(new Font("宋体", Font.PLAIN, 16));
		setBounds(250, 40, 600, 687);
		setResizable(false);
		JLabel headLogo = new JLabel();
		headLogo.setIcon(LoadIcon.getIcon("booktypemodify.jpg"));
		headLogo.setOpaque(true);
		headLogo.setBackground(Color.CYAN);
		headLogo.setPreferredSize(new Dimension(600,80));
		headLogo.setBorder(new LineBorder(SystemColor.activeCaptionBorder,1,false));
		
		JButton modiButton = new JButton("修改");
		modiButton.setFont(new Font("宋体", Font.PLAIN, 14));
		modiButton.addActionListener(new modiAction());
		
		JButton deleteButton = new JButton("删除");
		deleteButton.setFont(new Font("宋体", Font.PLAIN, 14));
		deleteButton.addActionListener(new deleteAction());
		
		bookTypeField = new JTextField();
		bookTypeField.setFont(new Font("宋体", Font.PLAIN, 14));
		bookTypeField.setColumns(10);
		
		JLabel message = new JLabel("修改/添加为:");
		message.setFont(new Font("宋体", Font.PLAIN, 16));
		
		JButton addButton = new JButton("添加");
		addButton.setFont(new Font("宋体", Font.PLAIN, 14));
		addButton.addActionListener(new addAction());
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "书籍类别及数量显示", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("宋体", Font.PLAIN, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"图书类别", "图书数量"
			}
		) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		//表头不能变化
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setFont(new Font("宋体",Font.PLAIN,16));
		//添加点击事件
		table.addMouseListener(new TableListener());
		scrollPane.setViewportView(table);
	
		//初始时刻填充数据
		fillTable();
		
		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("宋体", Font.PLAIN, 14));
		comboBox.addItem("默认显示");
		comboBox.addItem("柱状图显示");
		comboBox.addItem("饼状图显示");
		comboBox.addActionListener(new showDiffGraphListener());
		comboBox.setEditable(false);
		preSelect = (String)comboBox.getSelectedItem();
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(33)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
						.addComponent(message))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(bookTypeField, GroupLayout.PREFERRED_SIZE, 356, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(46)
							.addComponent(modiButton, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
							.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
							.addGap(39)
							.addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
							.addGap(29))))
				.addComponent(headLogo, GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(headLogo, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 466, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(message)
						.addComponent(bookTypeField, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(modiButton, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
						.addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addGap(17))
		);
		getContentPane().setLayout(groupLayout);
		
		setVisible(true);
	}

	//点击显示不同的图片
	private class showDiffGraphListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String selectItem = (String) comboBox.getSelectedItem();
			if(selectItem.equals(preSelect)) {
				return;
			}
			
			if(selectItem.equals("默认显示"))
			{
				clearPanel();
				panel.setLayout(new BorderLayout());
				panel.add(scrollPane);
			}
			else if(selectItem.equals("柱状图显示")) {
				clearPanel();
				drawBar();
			}
			else if(selectItem.equals("饼状图显示")) {
				clearPanel();
				drawCircle();
			}
			preSelect = selectItem;
		}
	}
	
	//panel清空
	private void clearPanel() {
		panel.removeAll();
		panel.updateUI();
		panel.repaint();
	}
	
	//设置兼容中文
	private void setLanuage(){
		//创建主题样式  
		   StandardChartTheme standardChartTheme = new StandardChartTheme("CN");  
		   //设置标题字体  
		   standardChartTheme.setExtraLargeFont(new Font("隶体",Font.BOLD,20));  
		   //设置图例的字体  
		   standardChartTheme.setRegularFont(new Font("宋体",Font.PLAIN,10));  
		   //设置轴向的字体  
		   standardChartTheme.setLargeFont(new Font("宋体",Font.PLAIN,20));  
		   //应用主题样式  
		   ChartFactory.setChartTheme(standardChartTheme);
	}
	
	//柱状图
	private void drawBar() {
		setLanuage();
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();//创建一个数据集
		for(String type:bookType) {
			int value = bookNumMap.containsKey(type)?bookNumMap.get(type):0;
			dataSet.addValue(value, "类别", type);
		}
		//JFreeChart标题,目录轴显示标签,数值轴显示标签,数据源,图表方向：水平、垂直
		//是否是否显示图例,是否生成热点工具,是否生成URL连接
		JFreeChart chart = ChartFactory.createBarChart3D("书籍类别统计图", "书籍类别", "数量", dataSet, PlotOrientation.VERTICAL, true, false, false);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(570,440));
		panel.add(chartPanel,BorderLayout.CENTER);
		panel.setLayout(new FlowLayout());
		panel.updateUI();
		panel.repaint();
	}
	
	//饼状图
	private void drawCircle() {
		setLanuage();
		DefaultPieDataset dataSet = new DefaultPieDataset();//创建数据集
		for(String type:bookType) {
			int value = bookNumMap.containsKey(type)?bookNumMap.get(type):0;
			dataSet.setValue(type, value);
		}
		JFreeChart chart = ChartFactory.createPieChart("书籍类别统计表", dataSet, true, true, false);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(570,440));
		panel.add(chartPanel,BorderLayout.CENTER);
		panel.setLayout(new FlowLayout());
		panel.updateUI();
		panel.repaint();
	}
	
	//点击事件
	private class TableListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			if(table.getSelectedRowCount() > 1) {
				bookTypeField.setText("");
			}else {
				bookTypeField.setText(bookType.get(table.getSelectedRow()));
			}
		}
	}
	
	//修改按钮
	private class modiAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table.getSelectedColumnCount() > 1) {
				JOptionPane.showMessageDialog(null, "不支持多行修改");
				return;
			}else if(table.getSelectedRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "未选中要修改的类别");
				return;
			}
			
			String newType = bookTypeField.getText().trim();
			if(newType.length() == 0) {
				JOptionPane.showMessageDialog(null, "请输入修改后的信息");
				return;
			}
			String preTypeName = bookType.get(table.getSelectedRow()) + "\n";
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				String command = "modifyBookType\n";
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				outputStream.write(preTypeName.getBytes(LoginFrame.charset));
				outputStream.write(newType.getBytes(LoginFrame.charset));
				outputStream.flush();
				client.shutdownOutput();
				
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(!flag) {
					JOptionPane.showMessageDialog(null, "修改失败,请重试");
				}
				else {
					fillTable();
					JOptionPane.showMessageDialog(null, "修改成功");
				}
				//如果修改成功,则更新table数据
				inputStream.close();
				outputStream.close();
				client.close();
			} catch (ClassNotFoundException|IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	//添加动作
	private class addAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String addTypeName = bookTypeField.getText().trim();
			if(addTypeName.length() == 0) {
				JOptionPane.showMessageDialog(null, "请输入数据");
				return;
			}
			String command = "addBookType\n";
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				outputStream.write(addTypeName.getBytes(LoginFrame.charset));
				outputStream.flush();
				client.shutdownOutput();
				
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(!flag) {
					JOptionPane.showMessageDialog(null, "添加失败");
				}else {
					fillTable();
					JOptionPane.showMessageDialog(null, "添加成功");
				}
				
				client.close();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//删除按钮
	private class deleteAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(table.getSelectedColumnCount() > 1) {
				JOptionPane.showMessageDialog(null, "不支持多行删除");
				return;
			}else if(table.getSelectedRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "未选中要删除的图书类别");
				return;
			}
			
			String command = "deleteBookType\n";
			String typeName = bookType.get(table.getSelectedRow());
			//删除时hashmap中可能不存在typename的键
			int num = bookNumMap.containsKey(typeName)?bookNumMap.get(typeName):0;
			if(num != 0) {
				JOptionPane.showMessageDialog(null, "选中图书类别下仍有图书,不可删除");
				return;
			}
			try {
				Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
				OutputStream outputStream = client.getOutputStream();
				outputStream.write(command.getBytes(LoginFrame.charset));
				outputStream.write(typeName.getBytes(LoginFrame.charset));
				outputStream.flush();
				client.shutdownOutput();
				
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				boolean flag = inputStream.readBoolean();
				if(!flag) {
					JOptionPane.showMessageDialog(null, "删除失败");
				}else {
					fillTable();
					JOptionPane.showMessageDialog(null, "删除成功");
				}
				inputStream.close();
				outputStream.close();
				client.close();
			} catch (ClassNotFoundException|IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//获取当前数据库中存在的图书类别
	public static List<String> getBookTypes() throws UnknownHostException, IOException, ClassNotFoundException {
		//连接服务器,得到所有存在的图书类别
		Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
		String command = "getAllBookType";
		OutputStream outputStream = client.getOutputStream();
		outputStream.write(command.getBytes(LoginFrame.charset));
		outputStream.flush();
		client.shutdownOutput();
		
		//得到所有的图书类别
		ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
		ListBookType listBookType = (ListBookType)inputStream.readObject();
		List<String> bookType = listBookType.getList();
		
		//关闭连接
		outputStream.close();
		inputStream.close();
		client.close();
		return bookType;
	}
	
	@SuppressWarnings("unchecked")
	private static void fillTable() throws UnknownHostException,IOException,ClassNotFoundException{
		//连接数据库,得到存在的图书类别以及数量,填充本table的数量
		Socket client = new Socket(LoginFrame.hostname,LoginFrame.port);
		String command = "getAllBookTypeNum";
		OutputStream outputStream = client.getOutputStream();
		outputStream.write(command.getBytes(LoginFrame.charset));
		outputStream.flush();
		client.shutdownOutput();
		
		ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
		MapBookTypeNum map = (MapBookTypeNum)inputStream.readObject();
		//得到数据,填充table数据
		outputStream.close();
		inputStream.close();
		client.close();
		
		bookType = getBookTypes();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setRowCount(0);
		Map<String,Integer> data = map.getMap();
		BookTypeFrame.bookNumMap = data;
		for(String type:bookType) {
			@SuppressWarnings("rawtypes")
			Vector vector = new Vector();
			vector.add(type);
			if(!data.containsKey(type)) {
				vector.add(0);
			}else {
				vector.add(data.get(type));
			}
			model.addRow(vector);
		}
		table.setModel(model);
	}
}