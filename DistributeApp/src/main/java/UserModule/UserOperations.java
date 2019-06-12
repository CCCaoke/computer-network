package UserModule;


/**
* UserModule/UserOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��user.idl
* 2019��6��10�� ����һ ����05ʱ06��19�� CST
*/

public interface UserOperations 
{
  boolean add (String startTime, String note);
  String query (String startTime);
  boolean delete (String key);
  boolean clear ();
  String show ();
} // interface UserOperations
