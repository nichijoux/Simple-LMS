package library.factory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import library.database.OperateDataBase;

//删除图书类别
public class TryDeleteBookTypeService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		String typename = read.readLine();
		boolean flag = OperateDataBase.deleteBookType(typename);
		DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
		outputStream.writeBoolean(flag);
		outputStream.flush();
		
		read.close();
		outputStream.close();
	}
}
