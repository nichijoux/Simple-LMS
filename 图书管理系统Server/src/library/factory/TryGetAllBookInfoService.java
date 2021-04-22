package library.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import library.data.Book;
import library.data.ListBook;
import library.database.OperateDataBase;

//得到所有的图书信息
public class TryGetAllBookInfoService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
   		//得到所有的信息
		List<Book> list = OperateDataBase.getAllBookInfo();
		//然后把list中的数据全部发送过去
		ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
		ListBook listBook = new ListBook();
		listBook.setList(list);
		outputStream.writeObject(listBook);
		outputStream.flush();
		//准备关闭输入端
		read.close();
		outputStream.close();
	}
}
