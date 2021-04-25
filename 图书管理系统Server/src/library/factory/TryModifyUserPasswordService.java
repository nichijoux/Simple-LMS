package library.factory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import library.database.OperateDataBase;

public class TryModifyUserPasswordService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		String username = read.readLine();
		String password = read.readLine();
		boolean flag = OperateDataBase.modifyUserPassword(username, password);
		DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
		outputStream.writeBoolean(flag);
		
		read.close();
		outputStream.close();
	}
}
