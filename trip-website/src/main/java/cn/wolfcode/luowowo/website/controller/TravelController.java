package cn.wolfcode.luowowo.website.controller;

import cn.wolfcode.luowowo.article.domain.Destination;
import cn.wolfcode.luowowo.article.domain.StrategyDetail;
import cn.wolfcode.luowowo.article.domain.Travel;
import cn.wolfcode.luowowo.article.query.TravelQuery;
import cn.wolfcode.luowowo.article.service.IDestinationService;
import cn.wolfcode.luowowo.article.service.IStrategyDetailService;
import cn.wolfcode.luowowo.article.service.ITravelService;
import cn.wolfcode.luowowo.cache.service.ITravelDetailRedisService;
import cn.wolfcode.luowowo.cache.vo.TravelStatisVO;
import cn.wolfcode.luowowo.comment.domain.TravelComment;
import cn.wolfcode.luowowo.comment.query.TravelCommentQuery;
import cn.wolfcode.luowowo.comment.service.ITravelCommentService;
import cn.wolfcode.luowowo.common.annoations.CheckLogin;
import cn.wolfcode.luowowo.common.annoations.UserParam;
import cn.wolfcode.luowowo.common.utils.JsonResult;
import cn.wolfcode.luowowo.member.domain.UserInfo;
import cn.wolfcode.luowowo.member.service.IUserService;
import cn.wolfcode.luowowo.website.utils.CookieUtil;
import cn.wolfcode.luowowo.website.utils.UMEditorUploader;
import cn.wolfcode.luowowo.website.utils.UploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/travel")
public class TravelController {

    @Reference
    private ITravelService travelService;

    @Reference
    private IDestinationService destinationService;

    @Reference
    private IStrategyDetailService detailService;

    @Reference
    private ITravelCommentService commentService;

    @Reference
    private ITravelDetailRedisService redisService;

    @Reference
    private IUserService userService;

    //分页查询所有的游记
    @RequestMapping("")
    public String listForPage(Model model, @ModelAttribute("qo") TravelQuery qo){
        qo.setState(Travel.STATE_RELEASE);
        PageInfo<Travel> pageInfo = travelService.queryForList(qo);
        model.addAttribute("pageInfo",pageInfo);
        //显示最新的游记评论
        TravelCommentQuery qc=new TravelCommentQuery();
        qc.setCurrentPage(1);
        qc.setPageSize(10);
        Page query = commentService.query(qc);
        List content = query.getContent();
        model.addAttribute("tcs",content);
        return "travel/list";
    }

    //查询具体的游记明细
    @RequestMapping("/detail")
    public String showDetail(Model model,Long id,HttpServletRequest req){
        String key = CookieUtil.getCookie(req);
        if (key.length()!=0){
            UserInfo user = userService.getUserByCookie(key);
            model.addAttribute("userInfo",user);
            //查询登录是否已经收藏了该游记
            boolean flag = redisService.isUserHasTravel(user.getId(), id);
            model.addAttribute("isFavor",flag);
        }
        Travel travel = travelService.getTravel(id);
        model.addAttribute("detail",travel);

        //吐司
        List<Destination> toasts = destinationService.getToasts(travel.getDest().getId());
        Destination destination = toasts.remove(toasts.size() - 1);
        model.addAttribute("toasts",toasts);

        //相关sds 攻略分类
        List<StrategyDetail> details = detailService.queryDetailByDestIdTop3(destination.getId());
        model.addAttribute("sds",details);

        //相关目的地游记
        List<Travel> travels = travelService.queryTravelByDestIdTop3(destination.getId());
        model.addAttribute("ts",travels);

        TravelCommentQuery qo=new TravelCommentQuery();
        qo.setTravelId(id);
        qo.setCurrentPage(1);
        qo.setPageSize(Integer.MAX_VALUE);
        Page page = commentService.queryForList(qo);
        //获取page对象中的文本
        List list = page.getContent();
        //查询评论
        model.addAttribute("list", list);
        //添加阅读数
        redisService.increaseViewnum(id, 1);
        //查找Redis中的统计数
        TravelStatisVO vo = redisService.getTravelRedisVO(id);
        model.addAttribute("vo",vo);
        return "travel/detail";
    }

    //编写游记,编辑传入游记id
    @CheckLogin
    @RequestMapping("/input")
    public String editTravel(Model model,Long id){
        List<Destination> destinations = destinationService.selectDestByDeep(3);
        model.addAttribute("dests",destinations);
        if(id!=null){
            Travel travel = travelService.getTravel(id);
            model.addAttribute("tv",travel);
        }
        return "travel/input";
    }

    //保存游记
    @CheckLogin
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public JsonResult saveOrUpdate(Travel travel, @UserParam UserInfo userInfo){
        travel.setAuthor(userInfo);
        Long travelId = travelService.insert(travel);
        JsonResult result=new JsonResult();
        result.setData(travelId);
        return result;
    }

    @Value("${file.path}")
    private String filePath;

    //上传封面
    @CheckLogin
    @RequestMapping("/coverImageUpload")
    @ResponseBody
    public Object coverImageUpload(MultipartFile pic){
        String upload = UploadUtil.upload(pic, filePath);
        return upload;
    }


    //百度的富文本图片上传要求
    @CheckLogin
    @RequestMapping("/contentImage")
    @ResponseBody
    public String uploadUEImage(MultipartFile upfile,HttpServletRequest request) throws Exception{
        UMEditorUploader up = new UMEditorUploader(request);
        String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
        up.setAllowFiles(fileType);
        up.setMaxSize(10000); //单位KB
        up.upload(upfile, filePath);

        String callback = request.getParameter("callback");
        String result = "{\"name\":\""+ up.getFileName() +"\", \"originalName\": \""+ up.getOriginalName() +"\", \"size\": "+ up.getSize()
                +", \"state\": \""+ up.getState() +"\", \"type\": \""+ up.getType() +"\", \"url\": \""+ up.getUrl() +"\"}";
        result = result.replaceAll( "\\\\", "\\\\" );
        if(callback == null ){
            return result ;
        }else{
            return "<script>"+ callback +"(" + result + ")</script>";
        }
    }


    //添加游记评论
    @RequestMapping("/commentAdd")
    @CheckLogin
    public String addComment(Model model,TravelComment comment,int floor,@UserParam UserInfo userInfo){
        //设置评论人信息
        comment.setHeadUrl(userInfo.getHeadImgUrl());
        comment.setLevel(userInfo.getLevel());
        comment.setUserId(userInfo.getId());
        comment.setUsername(userInfo.getNickname());
        comment.setCity(userInfo.getCity());
        TravelComment travelComment = commentService.saveOrUpdate(comment, floor);
        model.addAttribute("c",travelComment);
        model.addAttribute("floor",floor+1);
        redisService.increaseReplyNum(comment.getTravelId(),1);
        return "travel/commentTpl";
    }

    //游记点赞操作
    @RequestMapping("/travelThumbup")
    @ResponseBody
    public JsonResult thumbUp(Long tid,@UserParam UserInfo userInfo){
        if(userInfo==null){
            return new JsonResult("请先登录",JsonResult.CODE_TYPE_UNLOGIN);
        }
        boolean flag = redisService.thumbUp(tid, userInfo.getId());
        TravelStatisVO travelRedisVO = redisService.getTravelRedisVO(tid);
        return new JsonResult(flag,"").addData(travelRedisVO);
    }

    //收藏操作
    @RequestMapping("/favor")
    @ResponseBody
    public JsonResult favor( HttpServletRequest request,Long tid,@UserParam UserInfo userInfo) {
        //点击收藏时,要表明是哪个用户收藏了该篇攻略
        if (userInfo == null) {
            return new JsonResult("请先登录", JsonResult.CODE_TYPE_UNLOGIN);
        }
        boolean flag = redisService.increaseFavor(tid, userInfo.getId());
        TravelStatisVO redisVO = redisService.getTravelRedisVO(tid);
        return new JsonResult(flag, "").addData(redisVO);

    }
}
