package library.factory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import library.database.OperateDataBase;

//修改图书类别
public class TryModifyBookTypeService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		String preTypeName = read.readLine();
		String newType = read.readLine();
		boolean flag = OperateDataBase.modifyBookType(preTypeName, newType);
		DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
		outputStream.writeBoolean(flag);
		outputStream.flush();
		
		read.close();
		outputStream.close();
	}
}
