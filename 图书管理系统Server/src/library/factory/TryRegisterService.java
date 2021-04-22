package library.factory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import library.database.OperateDataBase;

//尝试注册的函数
public class TryRegisterService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		String username = read.readLine();
		String password = read.readLine();
		DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
		int ans = OperateDataBase.registerUser(username, password);
		outputStream.writeInt(ans);
		//关闭输入输出流
		read.close();
		outputStream.close();
	}
}
