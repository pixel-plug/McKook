package com.meteor.mckook.model.link;

import com.meteor.mckook.reflect.orm.FieldListable;
import com.meteor.mckook.reflect.orm.FieldOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KookUser implements FieldListable {
    @FieldOrder(1)
    private String id;
    // 绑定玩家名
    @FieldOrder(2)
    private String playerName;
    @FieldOrder(3)
    private String userName;
    @FieldOrder(4)
    private String nickName;
    @FieldOrder(5)
    private String identifyNum;
    @FieldOrder(6)
    private String avatar;
    // 是否为vip
    @FieldOrder(7)
    private boolean vip;
    // 是否为机器人
    @FieldOrder(8)
    private boolean bot;
    // 手机号码是否已验证
    @FieldOrder(9)
    private boolean mobileVerified;
    // 加入服务器(kook)时间
    @FieldOrder(10)
    private long joinedAt;

    public KookUser(ResultSet resultSet){
        try {
            this.id = resultSet.getString("id");
            this.playerName = resultSet.getString("playerName");
            this.userName = resultSet.getString("userName");
            this.nickName = resultSet.getString("nickName");
            this.identifyNum = resultSet.getString("identifyNum");
            this.avatar = resultSet.getString("avatar");
            this.vip = resultSet.getBoolean("vip");
            this.bot = resultSet.getBoolean("bot");
            this.mobileVerified = resultSet.getBoolean("mobileVerified");
            this.joinedAt = resultSet.getLong("joinedAt");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
