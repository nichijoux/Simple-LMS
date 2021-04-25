package library.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import library.data.ListBorrow;
import library.database.OperateDataBase;

public class TryGetBorrowInfoService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		String username = read.readLine();
		String queryMessage = read.readLine();
		ListBorrow listBorrow = null;
		if(queryMessage.equals("current"))
			listBorrow = OperateDataBase.getCurrentBorrowInfo(username);
		else
			listBorrow = OperateDataBase.getHistoryBorrowInfo(username);
		
		ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
		outputStream.writeObject(listBorrow);
		outputStream.flush();
		
		read.close();
		outputStream.close();
	}
}
