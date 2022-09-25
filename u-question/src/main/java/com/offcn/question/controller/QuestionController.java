package com.offcn.question.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.offcn.question.entity.TypeEntity;
import com.offcn.question.service.TypeService;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.offcn.question.entity.QuestionEntity;
import com.offcn.question.service.QuestionService;
import com.offcn.common.utils.PageUtils;
import com.offcn.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 
 *
 * @author jialei
 * @email 1694601613@qq.com
 * @date 2022-08-15 19:20:33
 */
@RestController
@RequestMapping("question/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = questionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息,获取根据实体编号获取题目详情数据
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		QuestionEntity question = questionService.getById(id);

        return R.ok().put("question", question);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody QuestionEntity question){
		questionService.save(question);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody QuestionEntity question){
		questionService.updateById(question);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		questionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    //题目上传导入
    @PostMapping("/upload")
    public R upload(MultipartFile file) {
        Map result = questionService.importExcel(file);
        return R.ok().put("result", result.get("result")).put("msg", result.get("msg")).put("num", result.get("num"));
    }

    //导出excel
    @GetMapping("exportExcel")
    public void export(String tableName, HttpServletResponse response) {
        System.out.println("导出excele");
        Workbook workbook = questionService.exportExcel();

        if (workbook != null) {
            //如果导出的文件（excel表格）不为空，重命名
            String fileName = "uxue_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //设置响应类型
            response.setContentType("application/octet-stream;charset=GB2312");
            //设置响应的字符集
            response.setCharacterEncoding("GB2312");
            OutputStream outputStream;
            try {
                outputStream = response.getOutputStream();
                //通过流将表格写出
                workbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().print("error");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Autowired
    private TypeService typeService;

    //题目类型为x轴，题目数为y，表type的id，对应表question的TYPE，但是是在同一张表question中查询出list（map（TYPE（id），num（id数）））
    //获取按照题库分类的统计数据
    @RequestMapping("countTypeQuestion")
    public R countTypeQuestion() {
        List<Map<String, Object>> mapList = questionService.countTypeQuestion();
        //System.out.println("mapList = " + mapList);
        //mapList = [{num=14, TYPE=6}]
        for (Map<String, Object> map : mapList) {

            //根据分类id，读取对应的分类数据
            Long typeId = (Long) map.get("TYPE");
            TypeEntity typeEntity = typeService.getById(typeId);
            //重新封装分类名称到map
            map.put("name", typeEntity.getType());
            System.out.println("typeEntity = " + typeEntity);
            //typeEntity = TypeEntity(id=6, type=JAVA基础, comments=JAVA基础, logoUrl=https://java0817-001.oss-cn-guangzhou.aliyuncs.com/2020-12-18/0cdabddd-3f3b-4b49-8cd5-e008b3e6ad08_1.jpg, delFlag=0, createTime=Fri Dec 18 19:44:18 CST 2020, updateTime=Fri Dec 18 19:44:18 CST 2020)
        }
        return R.ok().put("mapList", mapList);

    }
}
