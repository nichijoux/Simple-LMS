package library.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import library.factory.*;

//处理客户端的进程
public class CSThread extends Thread{
	private Socket client;
	
	//构造函数
	public CSThread(Socket socket) {
		super();
		client = socket;
	}
	
	//核心代码
	@Override
	public void run() {
    	BufferedReader read = null;
    	//获取输入流
		try {
			read = new BufferedReader(new InputStreamReader(client.getInputStream()));
		}catch (Exception e) {
			e.printStackTrace();
		}
    	
		//第一行读指令,判断是那种指令
    	String command = null;
		try {
			command = read.readLine();
		}catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(command);
		
		//策略加工厂模式优化if-else语句
		//数据库核心操作,有多种类型,登录,读取数据,修改数据等
		OperateService operateService = ServicesFactory.getOperateService(command);
		try {
			operateService.tryAction(client, read);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	//关闭连接
    	try {
    		System.out.println("服务器和client断开连接");
			client.close();
		}catch (IOException e) {
			e.printStackTrace();
		}	
	}
}