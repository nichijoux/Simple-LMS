package library.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import library.data.MapBookTypeNum;
import library.database.OperateDataBase;
//获得book表中存在的Booktype和CountNum键值对
public class TryGetAllBookTypeNumService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		MapBookTypeNum map = OperateDataBase.getAllBookTypeNum();
		ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
		outputStream.writeObject(map);
		outputStream.flush();
		
		read.close();
		outputStream.close();
	}
}
