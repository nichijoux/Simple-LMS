package library.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import library.data.ListUser;
import library.database.OperateDataBase;

//获取所有的用户信息
public class TryGetAllUserInfoService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		ListUser listUser = OperateDataBase.getAllUserInfo();
		ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
		outputStream.writeObject(listUser);
		outputStream.flush();
		
		read.close();
		outputStream.close();
	}
}
