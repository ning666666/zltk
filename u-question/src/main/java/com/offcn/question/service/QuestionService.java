package com.offcn.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.offcn.common.utils.PageUtils;
import com.offcn.question.entity.QuestionEntity;
import com.offcn.question.entity.TypeEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author jialei
 * @email 1694601613@qq.com
 * @date 2022-08-15 19:20:33
 */
public interface QuestionService extends IService<QuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //导入
    Map importExcel(MultipartFile file);
    //导出
    Workbook exportExcel();
    //统计分类题目数量
    List<Map<String, Object>> countTypeQuestion();
}

