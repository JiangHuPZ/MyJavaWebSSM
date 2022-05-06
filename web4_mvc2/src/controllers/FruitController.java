package controllers;


import pojo.Fruit;
import service.FruitService;
import util.StringUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Jayce Xu
 * @create 2022502
 */

public class FruitController {
    private FruitService fruitService = null;

    private String index(String oper, String keyword, Integer pageNo, HttpServletRequest request){
        HttpSession session = request.getSession() ;
        if (pageNo == null) {
            pageNo = 1;
        }

        if(StringUtil.isNotEmpty(oper) && "search".equals(oper)){
            pageNo = 1 ;
            //如果keyword为null，需要设置为空字符串""，否则查询时会拼接成 %null% , 我们期望的是 %%
            if(StringUtil.isEmpty(keyword)){
                keyword = "" ;
            }
            session.setAttribute("keyword",keyword);
        }else{
            Object keywordObj = session.getAttribute("keyword");
            if(keywordObj!=null){
                keyword = (String)keywordObj ;
            }else{
                keyword = "" ;
            }
        }
        session.setAttribute("pageNo",pageNo);

        List<Fruit> fruitList = fruitService.getFruitList(keyword , pageNo);
        session.setAttribute("fruitList",fruitList);

        int pageCount = fruitService.getPageCount(keyword);

        session.setAttribute("pageCount",pageCount);
        return "index";
    }

    private String add(Integer fid, String fname, Integer price,Integer fcount,String remark)  {
        Fruit fruit = new Fruit(0, fname, price, fcount, remark);
        fruitService.addFruit(fruit);
        return "redirect:fruit.do";

    }


    private String del(Integer fid) {
        if (fid!=null) {
            fruitService.delFruit(fid);
            return "redirect:fruit.do";
        }
        return null;
    }

    private String edit(Integer fid, HttpServletRequest request) {
        if (fid!=null) {
            Fruit fruit = fruitService.getFruitByFid(fid);
            request.setAttribute("fruit",fruit);
//            super.processTemplate("edit",req,resp);
            return "edit";


        }
        return null;
    }

    private String update(Integer fid, String fname, Integer price,Integer fcount,String remark) {

        fruitService.updateFruit(new Fruit(fid,fname,price,fcount,remark));
        //资源跳转
        return "redirect:fruit.do";
    }
}
