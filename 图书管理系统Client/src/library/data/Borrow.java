package library.data;

import java.io.Serializable;
import java.util.Date;

//借阅信息
public class Borrow implements Serializable {
	private static final long serialVersionUID = 2800902431352062098L;
	private String BookName;		//书名
	private String Author;			//作者
	private String BookType;		//书籍类型
	private Date BorrowDate;		//借阅日期
	private Date ReturnDate;		//归还日期
	
	public Borrow() {}

	public Borrow(String bookname,String author,String booktype)
	{
		BookName = bookname;
		Author = author;
		BookType = booktype;
		BorrowDate = new Date();
		ReturnDate = new Date();

	}
	
	public String getBookName() {
		return BookName;
	}

	public void setBookName(String bookName) {
		BookName = bookName;
	}
	
	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getBookType() {
		return BookType;
	}

	public void setBookType(String bookType) {
		BookType = bookType;
	}

	public Date getBorrowDate() {
		return BorrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		BorrowDate = borrowDate;
	}

	public Date getReturnDate() {
		return ReturnDate;
	}

	public void setReturnDate(Date returnDate) {
		ReturnDate = returnDate;
	}
	
}
