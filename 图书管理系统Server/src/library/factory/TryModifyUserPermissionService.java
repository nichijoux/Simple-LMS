package library.factory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import library.database.OperateDataBase;

//修改用户权限
public class TryModifyUserPermissionService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		String username = read.readLine();
		boolean flag = OperateDataBase.modifyUserPermission(username);
		DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
		outputStream.writeBoolean(flag);
		outputStream.flush();
		
		read.close();
		outputStream.close();
	}
}
