package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.article.pojo.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
//事务
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    public ApArticleMapper articleMapper;
    /**
     * 根据参数加载文章列表
     *
     * @param loadtype 1为加载更多  2为加载最新
     * @param dto
     * @return
     */
    @Override
    public ResponseResult load(Short loadtype, ArticleHomeDto dto) {
        //1.校验参数
        //校验size
        Integer size = dto.getSize();
        if ( size == null || size == 0 ){
            dto.setSize(10);
        }
        //分页的值不超过50
        size=Math.max(size, ArticleConstants.MAX_PAGE_SIZE);

        //校验参数loadtype
        if (loadtype.equals(ArticleConstants.LOADTYPE_LOAD_MORE) || loadtype.equals(ArticleConstants.LOADTYPE_LOAD_NEW)){
            loadtype=ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        //频道参数校验
        if (StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        //时间校验
        if (dto.getMaxBehotTime()==null ) dto.setMaxBehotTime(new Date());
        if(dto.getMinBehotTime()== null)  dto.setMinBehotTime(new Date());
        //2.查询数据
        List<ApArticle> apArticles = articleMapper.loadArticleList(dto, loadtype);
        //3.返回结果
        return ResponseResult.okResult(apArticles);
    }
}
