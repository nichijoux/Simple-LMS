package library.factory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import library.database.OperateDataBase;

public class TryReturnBookService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		String username = read.readLine();
		String bookname = read.readLine();
		String author = read.readLine();
		String booktype = read.readLine();
		boolean flag = OperateDataBase.returnBook(username, bookname, booktype, author);
		DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
		outputStream.writeBoolean(flag);
		outputStream.flush();
		
		read.close();
		outputStream.close();
	}
}
