package UserModule;


/**
* UserModule/UserOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从user.idl
* 2019年6月10日 星期一 下午05时06分19秒 CST
*/

public interface UserOperations 
{
  boolean add (String startTime, String note);
  String query (String startTime);
  boolean delete (String key);
  boolean clear ();
  String show ();
} // interface UserOperations
