package com.jyp.controller;


import com.jyp.pojo.*;
import com.jyp.service.*;
import com.jyp.common.ToutiaoUtil;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    ProducerService producerService;
//    @Autowired
//    NewsConsumerService newsConsumerService;

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;
    @Autowired
    CommentService commentService;

//    @Autowired
//    ImageService imageService;

    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String showNewsDetail(@PathVariable("newsId") int newsId,Model model)
    {
        News news = newsService.getNewsById(newsId);
        if(news != null)
        {
            List<Comment> comments = commentService.getCommentsByEntity(news.getId(),EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            for(Comment comment : comments)
            {
                ViewObject commentVO = new ViewObject();
                commentVO.set("comment",comment);
                commentVO.set("user",userService.getUser(comment.getUserId()));
                commentVOs.add(commentVO);
                if(localUserId >0)
                {
                    int like = likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId());
                    model.addAttribute("like",like);
                }
                else
                {
                    model.addAttribute("like",0);
                }
            }
            model.addAttribute("comments",commentVOs);
        }
        model.addAttribute("news",news);
        model.addAttribute("owner",userService.getUser(news.getUserId()));
        return "detail";
    }

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(Model model, @RequestParam("file") MultipartFile file)
    {
        try{
            String image = ToutiaoUtil.addImage(file);
            if(image == null)
            {
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }
            else
            {
                return ToutiaoUtil.getJSONString(0,image);
            }
        }catch(Exception e)
        {
            logger.error("上传文件异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(0,"上传文件异常");
        }
    }
    @RequestMapping(path={"/image"},method={RequestMethod.GET})
    @ResponseBody
    public void showImage(@RequestParam("name") String imageName,
                          HttpServletResponse response)
    {
        try{
            response.setContentType("image/jpeg");
           // StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)),response.getOutputStream());
            //imageService.resize(ToutiaoUtil.IMAGE_DIR+imageName,response.getOutputStream());
            Image img = ImageIO.read(new File(ToutiaoUtil.IMAGE_DIR+imageName)); // 构造Image对象
            BufferedImage _image = new BufferedImage(100, 80,BufferedImage.TYPE_INT_RGB);
            _image.getGraphics().drawImage(img, 0, 0, 100, 80, null); // 绘制缩小后的图
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
            //encoder.encode(_image); // 近JPEG编码
            //out.close();
            ImageIO.write(_image,"jpg",response.getOutputStream());
        }catch(Exception e)
        {
            logger.error("显示图片异常"+e.getMessage());
        }
    }
    @RequestMapping(path={"/user/addNews/"},method=RequestMethod.POST)
    @ResponseBody
    public String addNews(@RequestParam("image") String imageName,
                          @RequestParam("link") String link,
                          @RequestParam("title") String title)
    {
        try {
            News news = new News();
            news.setImage(imageName);
            news.setTitle(title);
            news.setLink(link);
            news.setCreatedDate(new Date());
            if (hostHolder.getUser() == null) {
                news.setUserId(1);
            } else {
                news.setUserId(hostHolder.getUser().getId());
            }
            news.setLikeCount(0);
            news.setCommentCount(0);
            newsService.addNews(news);
            EventModel asyncData = new EventModel()
                    .setActorId(news.getUserId())
                    .setEntityId(news.getId())
                    .setExt("title", news.getTitle());
            String jsonString = ToutiaoUtil.getJsonObjectString(asyncData);
            org.apache.rocketmq.common.message.Message msg = new Message("LoginTopic",// topic
                    "TagA",// tag
                    jsonString.getBytes()// body
            );
            Map<String, Object> result = producerService.send("NewsProducer",msg);
//            newsConsumerService.consumerNewsMsg("NewsConsumer");
            return ToutiaoUtil.getJSONString(0,"发布资讯成功");
        }catch (Exception e)
        {
            logger.error("发布资讯异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布资讯异常");
        }
    }
    @RequestMapping(path = {"/addComment"},method=RequestMethod.POST)
    public String addComment(@RequestParam("content") String content,
                             @RequestParam("newsId") int newsId)
    {
        try{
            Comment comment  =  new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setUserId(hostHolder.getUser().getId());
            commentService.addComment(comment);

            //异步更新评论数量
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);

        }catch (Exception e)
        {
            logger.error("评论异常"+e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

}
