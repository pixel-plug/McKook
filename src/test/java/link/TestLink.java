package link;

import com.meteor.mckook.model.link.KookUser;

import java.util.List;

public class TestLink {
    public static void main(String[] args) {
        KookUser kookUser = new KookUser();
        kookUser.setId("1");
        kookUser.setBot(false);
        kookUser.setAvatar("avater");
        kookUser.setUserName("username");
        kookUser.setPlayer("playername");
        kookUser.setMobileVerified(false);
        kookUser.setIdentifyNum("idn");
        kookUser.setJoinedAt(System.currentTimeMillis());

        List<Object> fieldList = kookUser.getFieldList();
        for (Object o : fieldList) {
            System.out.print(o+",");
        }
    }
}
