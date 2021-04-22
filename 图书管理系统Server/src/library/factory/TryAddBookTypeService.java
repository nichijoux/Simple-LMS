package library.factory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import library.database.OperateDataBase;

//添加图书类别
public class TryAddBookTypeService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		String addBookTypeName = read.readLine();
		DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
		boolean flag = OperateDataBase.addBookType(addBookTypeName);
		outputStream.writeBoolean(flag);
		outputStream.flush();
		
		read.close();
		outputStream.close();
	}
}
