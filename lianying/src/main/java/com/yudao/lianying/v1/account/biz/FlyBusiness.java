package com.yudao.lianying.v1.account.biz;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.lianying.utils.FlyUtil;
import com.yudao.lianying.v1.account.dao.Fly;
import com.yudao.lianying.v1.account.dao.mapper.FlyMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fanfl
 * @since 2023-06-10
 */
@Service
@Slf4j
public class FlyBusiness extends ServiceImpl<FlyMapper, Fly> {

    public String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMm4hmCT9n+e1lLQw9Eo/9dV+Oot20MgRKTWEr+4H6B+WiDOuKbOYgtOWc43VInP8X9cXCucEUPldPOvGGGNFRUCAwEAAQ==";

    public void checkFly() {
        if (!check()) {
            //throw new YudaoException(Result.ERROR, "服务异常，联系厂商！");
        }
    }

    public boolean check() {
        try {
            String id = "ly";
            Fly entity = selectById(id);
            if (entity != null) {
                byte[] publicKey = Base64.decodeBase64(publicKeyStr);
                byte[] licenseBaseByte = Base64.decodeBase64(entity.getLicense());
                byte[] licenseByte = FlyUtil.decryptByPublicKey(licenseBaseByte, publicKey);
                String license = new String(licenseByte);
                if (license.startsWith(id)) {
                    String dateStr = license.substring(id.length());
                    if (StringUtils.isNotBlank(dateStr)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        Date now = DateUtils.getNow();
                        try {
                            Date flyDate = sdf.parse(dateStr);
                            if (flyDate.compareTo(now) >= 0) {
                                return true;
                            }
                        } catch (ParseException e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("fly check error", e);
        }
        return false;
    }
}
