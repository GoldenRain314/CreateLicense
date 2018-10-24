package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static sample.AESUtil.parseByte2HexStr;

public class Controller {

    @FXML
    TextField userId;
    @FXML
    TextField license;
    @FXML
    TextField date;

    @FXML
    Label userIdTips;
    @FXML
    Label dateTips;

    public void createLicense(){
        String sUserId = userId.getText();
        if(sUserId == null || sUserId.length() != 32){
            userIdTips.setText("用户名格式错误");
            return;
        }
        userIdTips.setText("");
        String sDate = date.getText();
        if(sDate == null || sDate.length() != 8){
            dateTips.setText("时间格式错误");
            return;
        }
        dateTips.setText("");

        sUserId = sUserId.substring(11,15);
        String key = sDate + sUserId;

        try {
            byte[] a = AESUtil.aesEncrypt(key, "123             ");
            license.setText(parseByte2HexStr(a));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
