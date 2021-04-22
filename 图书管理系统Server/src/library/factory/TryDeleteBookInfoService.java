package library.factory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import library.database.OperateDataBase;
//尝试删除图书信息的函数
public class TryDeleteBookInfoService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		//尝试删除数据
		int id = Integer.parseInt(read.readLine());
		
		boolean flag = (OperateDataBase.DeleteBook(id) == 1);
	    //输出流,向socket中输出数据
	    DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
	    outputStream.writeBoolean(flag);
	    
	    read.close();
	    outputStream.close();
	}
}
