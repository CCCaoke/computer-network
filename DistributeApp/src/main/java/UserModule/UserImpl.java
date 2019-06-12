package UserModule;

import bean.Item;
import org.omg.CORBA.ORB;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.*;

public class UserImpl extends UserPOA {
    private Date date;
    private DateFormat dateFormat;
    private String dateRegex;
    private List<Item> personalList;                  //单个用户的to-do-list
    private Map<String, List<Item>> allPersonListMap;   //所有用户的to-do-list
    private ORB orb;

    public UserImpl(String name) {
        init();
        personalList = allPersonListMap.get(name);
        if (personalList == null) {
            personalList = new ArrayList<Item>();
            allPersonListMap.put(name, personalList);
        }

        date = new Date();
        dateFormat = DateFormat.getDateInstance();
        //日期格式规定为：xxxx-xx-xx,xx:xx
        dateRegex = "\\d{4}-\\d{1,2}-\\d{1,2},\\d{2}:\\d{2}";

    }
    private void init()
    {
        //从文件中读取to-do-list列表,转化为HashMap
        try {
            FileInputStream fin = new FileInputStream("item.file");
            ObjectInputStream oin = new ObjectInputStream(fin);
            try {
                Object object = oin.readObject();
                allPersonListMap = (HashMap<String, List<Item>>) object;
            } catch (ClassNotFoundException e) {
                System.out.println("object cast error");
                allPersonListMap = new HashMap<String, List<Item>>();
            }
            oin.close();
            fin.close();
        } catch (Exception e) {
            allPersonListMap = new HashMap<String, List<Item>>();
        }
    }

    public void setOrb(ORB orb) {
        this.orb = orb;
    }

    //将to-do-list表保存到本地文件中
    private void saveData() {
        try {
            FileOutputStream fout = new FileOutputStream("item.file");
            ObjectOutputStream oout = new ObjectOutputStream(fout);
            oout.writeObject(allPersonListMap);
            oout.flush();
            fout.flush();
            oout.close();
            fout.close();
        } catch (Exception e) {
            System.out.println("save error.");
            e.printStackTrace();
        }
    }
    //判断输入日期是否符合格式要求
    private boolean isFormatMatch(String dateStr) {
        return dateStr.matches(dateRegex);
    }

    //将字符串转化为日期
    private Date turnToDate(String dateStr) {
        String[] str = dateStr.split("[,|:]");
        try {
            date = dateFormat.parse(str[0]);
            date.setHours(Integer.parseInt(str[1]));
            date.setMinutes(Integer.parseInt(str[2]));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }


    public boolean add(String time, String note) {
        Date date=turnToDate(time);
        Item item = new Item(date, note);
        personalList.add(item);
        saveData();
        return true;
    }

    public String query(String startTime) {
        Date startDate;
        int index = 0;
        String queryResult = "";
        if (isFormatMatch(startTime)) {
            startDate = turnToDate(startTime);
            for (Item item : personalList) {
                if (item.getDate().after(startDate)) {
                    index++;
                    queryResult += index + " : " + item.getDate() +
                            "-->" + item.getNote() + "\n";
                }
            }
        } else {
            queryResult = "Date format is wrong!\n";
        }
        return queryResult;
    }

    public boolean delete(String key) {
        int index = Integer.parseInt(key);
        if (index <= personalList.size()-1 && index >= 0) {
            personalList.remove(index-1);
            saveData();
            return true;
        } else {
            return false;
        }
    }

    public boolean clear() {
        int index = personalList.size()-1;
        if (index < 0) {
            return false;
        } else {
            while (index >= 0) {
                personalList.remove(index);
                index--;
            }
            saveData();
            return true;
        }
    }

    public String show() {
        String result = "";
        int index = 0;
        if (personalList.size() > 0) {
            for (Item item : personalList) {
                index++;
                result += index + " : " + item.getDate() +
                        "-->" + item.getNote() + "\n";
            }
        } else {
            result = "Empty to-do-list!\n";
        }
        return result;
    }
}
