package library.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import library.data.ListBookType;
import library.database.OperateDataBase;

//得到所有可能的图书类别
public class TryGetAllBookTypeService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		List<String> list = OperateDataBase.getAllBookType();
		ListBookType bookType = new ListBookType(list);
		ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
		outputStream.writeObject(bookType);
		outputStream.flush();
		
		read.close();
		outputStream.close();
	}
}
