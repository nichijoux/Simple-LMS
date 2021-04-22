package library.frame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import library.util.CSThread;
import library.util.LoadIcon;

//服务器窗口
@SuppressWarnings("serial")
public class ServerFrame extends JFrame{
	private JButton open;
	private JButton close;
	private JLabel	Message;
	private int port = 4399;
	private ServerSocket serverSocket;
	
	public ServerFrame() {
		super();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font font = new Font("楷体", Font.PLAIN, 16);
		
		setTitle("数据库管理服务器");
		setResizable(false);				//不可调整
		setBounds(530,350,400,120);			//默认出现位置和默认大小
		//setResizable(false);				//不可调整登录窗口大小
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new LineBorder(SystemColor.activeCaptionBorder,1,false));
		getContentPane().add(buttonPanel,BorderLayout.NORTH);
		
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(20);
		flowLayout.setAlignment(FlowLayout.CENTER);
		buttonPanel.setLayout(flowLayout);
		open = new JButton();
		open.setText("开启服务器");
		open.setFont(font);
		open.setBorder(BorderFactory.createRaisedBevelBorder());
		open.addActionListener(new OpenActionListener());
		buttonPanel.add(open);

		close = new JButton();
		close.setText("关闭服务器");
		close.setFont(font);
		close.setBorder(BorderFactory.createRaisedBevelBorder());
		close.addActionListener(new CloseActionListener());
		buttonPanel.add(close);
		
		JPanel messagePanel = new JPanel();
		FlowLayout messageLayout = new FlowLayout();
		messageLayout.setAlignment(FlowLayout.CENTER);
		messagePanel.setLayout(messageLayout);
		messagePanel.setLayout(messageLayout);
		
		Message = new JLabel();
		Message.setFont(font);
		Message.setText("等待服务器启动");
		messagePanel.add(Message);
		getContentPane().add(messagePanel,BorderLayout.SOUTH);
		
		//设置window图标
		setIconImage(LoadIcon.getIcon("ServerWindow.png").getImage());
		
		setVisible(true);	
	}
	
	class OpenActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(serverSocket == null || serverSocket.isClosed())
			{
				try {
					serverSocket = new ServerSocket(port);
					ServerThread thread = new ServerThread(serverSocket);
					thread.start();
					Message.setText("服务器已启动");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}	
	}
	
	class CloseActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(serverSocket != null && !serverSocket.isClosed()){
				try {
					serverSocket.close();
					System.out.println("服务器已关闭");
					Message.setText("服务器已关闭");
				} catch (IOException e1) {
					e1.printStackTrace();
				}	
			}
		}
	}
}

class ServerThread extends Thread{
	private static ServerSocket server;
	
	public ServerThread(ServerSocket serverSocket) {
		super();
		ServerThread.server = serverSocket;
	}
	
	//用来开启服务器
	@Override
	public void run(){
        System.out.println("正在启动服务器···");
        while(!server.isClosed())
        {
			try {
				Socket client = server.accept();
	        	System.out.println("客户端: " + client.getInetAddress() + " 已连接到服务器");
	        	CSThread thread = new CSThread(client);
	        	thread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
}
