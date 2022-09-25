package com.offcn.question.service.impl;

import com.offcn.question.entity.TypeEntity;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.common.utils.PageUtils;
import com.offcn.common.utils.Query;

import com.offcn.question.dao.QuestionDao;
import com.offcn.question.entity.QuestionEntity;
import com.offcn.question.service.QuestionService;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service("questionService")
public class QuestionServiceImpl extends ServiceImpl<QuestionDao, QuestionEntity> implements QuestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //1、获取查询关键字
        String key = (String) params.get("key");
        //2、创建查询条件对象
        QueryWrapper<QuestionEntity> queryWrapper = new QueryWrapper<>();
        //3、设置查询条件
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.eq("title", key).or().like("TYPE", key);

        }
        IPage<QuestionEntity> page = this.page(
                new Query<QuestionEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public Map importExcel(MultipartFile file) {
        Map result = new HashMap();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取文件名字
        String fileName = file.getOriginalFilename();
        //通过文件名字获取文件格式
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {

            result.put("result", false);
            result.put("msg", "文件扩展名不正确，导入失败");
            result.put("num", 0);
        }
        //准备list集合存文件数据
        List<QuestionEntity> list = new ArrayList<>();
        try {
            //获取流读取文件
            InputStream is = file.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(is));
            HSSFSheet sheet = workbook.getSheetAt(0);
            //获取多少行
            int rows = sheet.getPhysicalNumberOfRows();
            QuestionEntity questionEntity = null;
            for (int j = 1; j < rows; j++) {
                questionEntity = new QuestionEntity();
                //获得第 j 行
                HSSFRow row = sheet.getRow(j);
                System.out.println("row=" + row + "++++++++++++++++++++++++++++++++++++++++++");
                //获取单元格
                questionEntity.setTitle(row.getCell(0).getStringCellValue());
                System.out.println("title=" + questionEntity.getTitle());

                questionEntity.setAnswer(row.getCell(1).getStringCellValue());
                System.out.println("Answer=" + questionEntity.getAnswer());

                questionEntity.setLevel((int) row.getCell(2).getNumericCellValue());
                System.out.println("Level=" + questionEntity.getLevel());

                questionEntity.setDisplayOrder((int) row.getCell(3).getNumericCellValue());
                System.out.println("type=" + questionEntity.getDisplayOrder());

                questionEntity.setSubTitle(row.getCell(4).getStringCellValue());
                System.out.println("subTitle:" + questionEntity.getSubTitle());

                questionEntity.setType((long) row.getCell(5).getNumericCellValue());
                System.out.println("type:" + questionEntity.getType());

                questionEntity.setEnable((int) row.getCell(6).getNumericCellValue());
                System.out.println("Enable:" + questionEntity.getEnable());


                list.add(questionEntity);

                //调用数据库接口保存到数据库
                this.save(questionEntity);
                System.out.println("----------------");
            }
            System.out.println("总条数=" + list.size());
            System.out.println("结束");
            System.out.println("退出循环");
            result.put("result", true);
            result.put("msg", "导入成功");
            result.put("num", list.size());
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", "文件导入失败:" + e.getMessage());
            result.put("num", 0);
        }

        return result;
    }

    @Override
    public Workbook exportExcel() {
        // 创建新的Excel 工作簿
        Workbook workbook = new HSSFWorkbook();

        // 在Excel工作簿中建一工作表，其名为缺省值 Sheet0
        //Sheet sheet = workbook.createSheet();

        // 创建工作表
        Sheet sheet = workbook.createSheet("题库");

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("题目标题");
        row.createCell(1).setCellValue("题目解答");
        row.createCell(2).setCellValue("题目难度等级");
        row.createCell(3).setCellValue("排序");
        row.createCell(4).setCellValue("副标题");
        row.createCell(5).setCellValue("题目类型");
        row.createCell(6).setCellValue("是否显示");
        List<QuestionEntity> list = this.list();
        for (int i = 0; i < list.size(); i++) {
            Row row2 = sheet.createRow(i + 1);
            //创建表格
            row2.createCell(0).setCellValue(list.get(i).getTitle());
            row2.createCell(1).setCellValue(list.get(i).getAnswer());
            row2.createCell(2).setCellValue(list.get(i).getLevel());
            row2.createCell(3).setCellValue(list.get(i).getDisplayOrder());
            row2.createCell(4).setCellValue(list.get(i).getSubTitle());
            row2.createCell(5).setCellValue(list.get(i).getType());
            row2.createCell(6).setCellValue(list.get(i).getEnable());
        }

        return workbook;
    }

    @Autowired
    private QuestionDao questionDao;

    //题目类型为x轴，题目数为y，表type的id，对应表question的TYPE list（map（id，TYPE））
    @Override
    public List<Map<String, Object>> countTypeQuestion() {
        QueryWrapper<QuestionEntity> queryWrapper = new QueryWrapper<QuestionEntity>().select("TYPE,COUNT(TYPE) AS num").groupBy("type");
        List<Map<String, Object>> mapList = questionDao.selectMaps(queryWrapper);
        System.out.println("mapList = " + mapList);
        return mapList;
    }
}
