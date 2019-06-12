package com.util;


import com.mail.Mail;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.apache.commons.codec.binary.Base64;


import java.io.IOException;

public class MailUtil {
    private static String charset = "GBK";
    public static Mail parseMail(String mailText) throws IOException {
        Mail res = new Mail();
        res.setContent(getContent(mailText));
        res.setReceivedFrom(getReceivedFrom(mailText));
        res.setSubject(getSubject(mailText));
        return res;
    }
    private static String getSubject(String mailText) throws IOException {
        int start = mailText.indexOf("Subject: ")+9;
        int end = start + mailText.substring(start).indexOf("\n")-1;
        if(mailText.substring(start).contains("=?"))
        {
            start = start + mailText.substring(start).indexOf("B?")+2;
            end = start + mailText.substring(start).indexOf("?=");
            Base64 base64 = new Base64();
            byte[] source = base64.decode(mailText.substring(start,end).getBytes());
            return new String(source,charset);
        }
        return mailText.substring(start,end);
    }
    private static String getReceivedFrom(String mailText) {
        int fromStart = mailText.indexOf("From:");
        int start = mailText.substring(fromStart).indexOf("<")+fromStart+1;
        int end = mailText.substring(fromStart).indexOf(">")+fromStart;
        return mailText.substring(start,end);
    }
    private static String getContent(String mailText) throws IOException {
        int charSetStart = mailText.indexOf("charset")+8;
        int charSetEnd = mailText.substring(charSetStart).indexOf("\n")+charSetStart;
        charset = mailText.substring(charSetStart,charSetEnd-1);
        if(charset.contains("\""))
        {
            charset = charset.substring(1,charset.length()-1);
        }
        int start  = mailText.indexOf("charset");
        start = start + "charset".length()+6;
        String result =  new String(mailText.substring(start).getBytes(),charset);
        result = parseHtml(result);
        return result;
    }
    private static String parseHtml(String html) {
        if (html == null)
        {
            return "";
        }

        final Document document = Jsoup.parse(html);
        final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        document.outputSettings(outputSettings);
        document.select("br").append("\\n");
        document.select("p").prepend("\\n");
        document.select("p").append("\\n");
        final String newHtml = document.html().replaceAll("\\\\n", "\n");
        final String plainText = Jsoup.clean(newHtml, "", Whitelist.none(), outputSettings);
        final String result = StringEscapeUtils.unescapeHtml3(plainText.trim());
        return result.replaceAll("(\\r+\\n+)+","");
        //return result;
    }
}
