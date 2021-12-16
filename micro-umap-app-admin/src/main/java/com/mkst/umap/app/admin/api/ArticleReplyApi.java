package com.mkst.umap.app.admin.api;

import cn.hutool.core.util.StrUtil;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.cms.domain.Article;
import com.mkst.mini.systemplus.cms.service.IArticleService;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.api.bean.param.article.ArticleReplyParam;
import com.mkst.umap.app.admin.api.bean.result.article.ArticleReplyResult;
import com.mkst.umap.app.admin.domain.Reply;
import com.mkst.umap.app.admin.service.IReplyService;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: ltt
 * @Date: 2020/08/24/19:48
 * @Description:
 */
@Slf4j
@Api(value = "文章评论接口")
@RestController
@RequestMapping(value = "/api/article")
public class ArticleReplyApi extends BaseApi {

    @Autowired
    private IArticleService articleService;
    @Autowired
    private IReplyService replyService;
    @Autowired
    private ISysUserService userService;

    @Login
    @ApiOperation(value = "回复文章")
    @PostMapping(value = "/reply")
    @Transactional(rollbackFor = Exception.class)
    public Result reply(HttpServletRequest request, @RequestBody @ApiParam ArticleReplyParam param){

        if(param.getId() == null) {
            return ResultGenerator.genFailResult("id为空，请检查参数");
        }
        if(StrUtil.isBlank(param.getReplyContent())) {
            return ResultGenerator.genFailResult("评论内容为空，请检查参数");
        }
        Article article = articleService.selectArticleById(param.getId().toString());
        if(article == null) {
            return ResultGenerator.genFailResult("文章不存在，请联系管理员");
        }

        Reply insertReply = new Reply();
        insertReply.setContent(param.getReplyContent());
        insertReply.setCreateBy(getLoginName(request));
        insertReply.setBusinessType(BusinessTypeEnum.UMAP_ARTICLE.getValue());
        insertReply.setObjectId(param.getId());
        int row = replyService.insertReply(insertReply);
        int i = 0;
        String msg;
        if(row >0){
            Long replyNum = article.getReplyNum();
            replyNum++;
            article.setReplyNum(replyNum);
            i = articleService.updateArticle(article);
            msg = i>0 ? "回复成功,评论数加一" : "回复成功，评论数增加失败";
        }else{
            msg = "回复失败，请刷新重试或联系管理员！";
        }
        return ResultGenerator.genSuccessResult(msg);
    }


    @Login
    @ApiOperation(value = "获取回复列表")
    @PostMapping(value = "/replyList")
    public Result replyList(HttpServletRequest request,@RequestBody @ApiParam ArticleReplyParam param){
        if(param.getId() == null) {
            return ResultGenerator.genFailResult("id为空，请检查参数");
        }
        Reply selectReply = new Reply();
        selectReply.setObjectId(param.getId());
        selectReply.setBusinessType(BusinessTypeEnum.UMAP_ARTICLE.getValue());

        // 暂时先这么写，后期如果列表较多的话，需要把功能在sql里解决
        String loginName = getLoginName(request);
        startPage();
        ArrayList<ArticleReplyResult> results = new ArrayList<>();
        List<Reply> replyList = replyService.selectReplyList(selectReply);
        replyList.stream().forEach(reply -> {
            // id content createTime
            ArticleReplyResult result = transObject(reply, ArticleReplyResult.class);
            SysUser user = userService.selectUserByLoginName(reply.getCreateBy());
            String userName = "";
            if (reply.getCreateBy().equals(loginName)){
                userName = "我";
            }else {
                userName = user == null ? "" : user.getUserName();
            }

            result.setResponder(userName);
            result.setAvatar(user.getAvatar());
            results.add(result);

        });

        return ResultGenerator.genSuccessResult("查询成功",results);
    }
}
