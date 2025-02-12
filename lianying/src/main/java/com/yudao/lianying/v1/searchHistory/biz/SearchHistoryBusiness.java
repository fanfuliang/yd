package com.yudao.lianying.v1.searchHistory.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.common.yudaocommon.utils.ExcelUtils;
import com.yudao.common.yudaocommon.utils.ParameterValidUtils;
import com.yudao.lianying.v1.searchHistory.dao.SearchHistory;
import com.yudao.lianying.v1.searchHistory.dao.mapper.SearchHistoryMapper;
import com.yudao.lianying.v1.searchHistory.vo.SearchHistoryModifyVO1;
import com.yudao.lianying.v1.searchHistory.vo.SearchHistoryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 查询历史表 服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Service
@Slf4j
public class SearchHistoryBusiness extends ServiceImpl<SearchHistoryMapper, SearchHistory> {

    public Object saveAuto(SearchHistoryVO entityVO) {
        SearchHistory entity = CommonUtils.copyPropertiesAndCreateId(entityVO, SearchHistory.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {

    }

    public void modifyAllColumnInObjectAuto(SearchHistoryModifyVO1 modifyVO1) {
        SearchHistory entity = selectById(modifyVO1.getId());
        ParameterValidUtils.entityExist(modifyVO1.getId(), entity);
        BeanUtils.copyProperties(modifyVO1, entity);
        if (!(updateAllColumnById(entity))) {
            throw new YudaoException(Result.ERROR, "Failed to modify");
        }
    }

    public void modifyByIdAuto(List<String> idList, String[] modifyFieldNameArray, String[] modifyFieldValueArray) {
        baseMapper.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
    }

    public Object getByIdAuto(String id) {
        SearchHistory entity = selectById(id);
        if (null == entity) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<SearchHistory> page, String keyword, Date beginTime, Date endTime, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<SearchHistory> entityWrapper = new EntityWrapper<>();
        for (int i = 0; i < filterFieldNameArray.length; i++) {
            entityWrapper.in(filterFieldNameArray[i], filterFieldValueArray[i]);
        }
        if (StringUtils.isNotBlank(keyword)) {
            entityWrapper.like(FieldNameEnum.text.name(), keyword);
        }
        if (null != beginTime) {
            entityWrapper.ge(FieldNameEnum.create_time.name(), beginTime);
        }
        if (null != beginTime) {
            entityWrapper.lt(FieldNameEnum.create_time.name(), endTime);
        }
        if (StringUtils.isNotBlank(orderBy)) {
            entityWrapper.orderBy(orderBy);
        }
        // 不管有没有排序，最后都对创建时间倒序
        entityWrapper.orderBy(FieldNameEnum.create_time.name(), false);
        return selectPage(page, entityWrapper);
    }

    public void export(String keyword, Date beginTime, Date endTime, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy, HttpServletResponse response) {
        EntityWrapper<SearchHistory> entityWrapper = new EntityWrapper<>();
        for (int i = 0; i < filterFieldNameArray.length; i++) {
            entityWrapper.in(filterFieldNameArray[i], filterFieldValueArray[i]);
        }
        if (StringUtils.isNotBlank(keyword)) {
            entityWrapper.like(FieldNameEnum.text.name(), keyword);
        }
        if (null != beginTime) {
            entityWrapper.ge(FieldNameEnum.create_time.name(), beginTime);
        }
        if (null != beginTime) {
            entityWrapper.lt(FieldNameEnum.create_time.name(), endTime);
        }
        if (StringUtils.isNotBlank(orderBy)) {
            entityWrapper.orderBy(orderBy);
        }
        // 不管有没有排序，最后都对创建时间倒序
        entityWrapper.orderBy(FieldNameEnum.create_time.name(), false);
        List<SearchHistory> list = selectList(entityWrapper);
        //excel文件名
        String fileName = "统计分析_" + DateUtils.getDateFormat(new Date(), "yyyyMMdd") + ".xlsx";

        //sheet名
        String sheetName = "Sheet1";

        //excel标题
        String[] title = {"类型", "项目名称", "结果", "操作账户", "时间"};
        int rowNum = list.size();
        int columnNum = title.length;
        String[][] content = new String[rowNum][];
        for (int i = 0; i < rowNum; i++) {
            content[i] = new String[columnNum];
            SearchHistory searchHistory = list.get(i);
            content[i][0] = "term".equals(searchHistory.getCategory()) ? "术语查询" : "文档质检";
            content[i][1] = searchHistory.getText();
            content[i][2] = searchHistory.getResultNum() == null ? "" : searchHistory.getResultNum().toString();
            content[i][3] = searchHistory.getCreateBy();
            content[i][4] = DateUtils.getDateFormat(searchHistory.getCreateTime(), "yyyy/MM/dd HH:mm");
        }
        // xlsx格式
        XSSFWorkbook wb = ExcelUtils.getXSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    private void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Object getAccount(String category) {
        category = CommonUtils.blankToEmpty(category);
        return baseMapper.getAccount(category);
    }

    public void clearTermSearchHistory(String account) {
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setCategory("term");
        searchHistory.setCreateBy(account);
        delete(new EntityWrapper<>(searchHistory));
    }
}
